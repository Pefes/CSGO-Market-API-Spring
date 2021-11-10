package com.csgomarket.csgomarketapi.service;

import com.csgomarket.csgomarketapi.model.item.Item;
import com.csgomarket.csgomarketapi.model.lastopened.LastOpened;
import com.csgomarket.csgomarketapi.model.user.User;
import com.csgomarket.csgomarketapi.payload.request.getitems.FiltersData;
import com.csgomarket.csgomarketapi.payload.request.getitems.GetItemsRequest;
import com.csgomarket.csgomarketapi.payload.request.getitems.PaginatorData;
import com.csgomarket.csgomarketapi.payload.request.getitems.SortingData;
import com.csgomarket.csgomarketapi.payload.response.ApiResponse;
import com.csgomarket.csgomarketapi.payload.response.getitems.GetItemsResponse;
import com.csgomarket.csgomarketapi.payload.response.opencontainer.OpenContainerResponse;
import com.csgomarket.csgomarketapi.security.userdetails.UserDetailsImpl;
import com.csgomarket.csgomarketapi.util.DrawItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.*;
import static com.csgomarket.csgomarketapi.util.GetApiResponse.getApiResponse;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ItemsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MongoService mongoService;

    public ApiResponse<GetItemsResponse> getMarketItems(GetItemsRequest request) {
        Query query = getMarketItemsQuery(request.getFiltersData(), request.getPaginatorData());
        long querySize = mongoService.getQuerySize(query);
        List<Item> items = querySize > 0 ? mongoTemplate.find(query, Item.class) : List.of();
        return getApiResponse(SUCCESS, null, GetItemsResponse.builder()
                .items(items)
                .querySize(querySize)
                .build());
    }

    public ApiResponse<GetItemsResponse> getOwnedItems(GetItemsRequest request) {
        Query query = getOwnedItemsQuery(request.getFiltersData(), request.getPaginatorData());
        long querySize = mongoService.getQuerySize(query);
        List<Item> items = querySize > 0 ? mongoTemplate.find(query, Item.class) : List.of();
        return getApiResponse(SUCCESS, null, GetItemsResponse.builder()
                .items(items)
                .querySize(querySize)
                .build());
    }

    public ApiResponse<GetItemsResponse> getTryOutItems() {
        List<Item> items = mongoTemplate.find(query(where(ITEM_OPENABLE).is(true)).limit(3), Item.class);
        return getApiResponse(SUCCESS, null, GetItemsResponse.builder()
                .items(items)
                .querySize(3)
                .build());
    }

    public ApiResponse<?> buyItem(String itemId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Item item = mongoTemplate.findById(itemId, Item.class);
        User user = mongoTemplate.findById(userDetails.getId(), User.class);

        if (item != null && user != null && item.isPurchasable() && user.getCash().compareTo(item.getPrice()) >= 0) {
            user.getOwnedItems().add(item.getId());
            user.setCash(user.getCash().subtract(item.getPrice()));
            item.setPurchasable(false);
            mongoTemplate.save(user);
            mongoTemplate.save(item);

            return getApiResponse(SUCCESS, null, null);
        }

        return getApiResponse(FAIL, MESSAGE_BUY_ITEM_ERROR, null);
    }

    public ApiResponse<?> sellItem(String itemId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Item item = mongoTemplate.findById(itemId, Item.class);
        User user = mongoTemplate.findById(userDetails.getId(), User.class);

        if (item != null && user != null && !item.isPurchasable() && user.getOwnedItems().contains(item.getId())) {
            user.getOwnedItems().remove(item.getId());
            user.setCash(user.getCash().add(item.getPrice()));
            item.setPurchasable(true);
            mongoTemplate.save(user);
            mongoTemplate.save(item);

            return getApiResponse(SUCCESS, null, null);
        }

        return getApiResponse(FAIL, MESSAGE_SELL_ITEM_ERROR, null);
    }

    public ApiResponse<OpenContainerResponse> openContainer(String containerId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Item container = mongoTemplate.findById(containerId, Item.class);
        User user = mongoTemplate.findById(userDetails.getId(), User.class);

        if (user != null && container != null && user.getOwnedItems().contains(container.getId()) && container.isOpenable()) {
            Item drawnItem = DrawItem.draw(container.getContent());
            Item createdItem = mongoTemplate.save(drawnItem);
            user.getOwnedItems().add(createdItem.getId());
            mongoTemplate.save(user);
            mongoTemplate.remove(container);

            return getApiResponse(SUCCESS, null, new OpenContainerResponse(createdItem));
        }

        return getApiResponse(FAIL, MESSAGE_OPEN_CONTAINER_ERROR, null);
    }

    public ApiResponse<OpenContainerResponse> openTryOutContainer(String containerId) {
        Item container = mongoTemplate.findById(containerId, Item.class);

        if (container != null && container.isOpenable()) {
            Item drawnItem = DrawItem.draw(container.getContent());
            return getApiResponse(SUCCESS, null, new OpenContainerResponse(drawnItem));
        }

        return getApiResponse(FAIL, MESSAGE_OPEN_TRY_OUT_CONTAINER_ERROR, null);
    }

    public ApiResponse<List<LastOpened>> getLastOpenedItems() {
        Query query = new Query()
                .with(Sort.by(Direction.ASC, LAST_OPENED_OPENED_DATE))
                .limit(20);

        List<LastOpened> items = mongoTemplate.find(query, LastOpened.class);

        return getApiResponse(SUCCESS, null, items);
    }

    private Query getMarketItemsQuery(FiltersData filtersData, PaginatorData paginatorData) {
        Query queryWithFilters = mongoService.getQueryWithFilters(filtersData, true);
        Query queryWithSorting = mongoService.addSorting(queryWithFilters, filtersData.getSorting());
        return mongoService.addPagination(queryWithSorting, paginatorData);
    }

    private Query getOwnedItemsQuery(FiltersData filtersData, PaginatorData paginatorData) {
        Query queryWithFilters = mongoService.getQueryWithFilters(filtersData, false);
        Query queryWithOwnedItems = getQueryWithOwnedItems(queryWithFilters);
        Query queryWithSorting = mongoService.addSorting(queryWithOwnedItems, filtersData.getSorting());
        return mongoService.addPagination(queryWithSorting, paginatorData);
    }

    private Query getQueryWithOwnedItems(Query query) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return query.addCriteria(where(ID).in(userDetails.getOwnedItems()));
    }
}
