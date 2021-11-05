package com.csgomarket.csgomarketapi.service;

import com.csgomarket.csgomarketapi.model.item.Item;
import com.csgomarket.csgomarketapi.model.user.User;
import com.csgomarket.csgomarketapi.payload.request.getitems.FiltersData;
import com.csgomarket.csgomarketapi.payload.request.getitems.GetItemsRequest;
import com.csgomarket.csgomarketapi.payload.request.getitems.PaginatorData;
import com.csgomarket.csgomarketapi.payload.request.getitems.SortingData;
import com.csgomarket.csgomarketapi.payload.response.ApiResponse;
import com.csgomarket.csgomarketapi.payload.response.getitems.GetItemsResponse;
import com.csgomarket.csgomarketapi.security.userdetails.UserDetailsImpl;
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

    public ApiResponse<GetItemsResponse> getMarketItems(GetItemsRequest request) {
        Query query = getMarketItemsQuery(request.getFiltersData(), request.getPaginatorData());
        long querySize = getQuerySize(query);
        List<Item> items = querySize > 0 ? mongoTemplate.find(query, Item.class) : List.of();
        return getApiResponse(SUCCESS, null, GetItemsResponse.builder()
                .items(items)
                .querySize(querySize)
                .build());
    }

    public ApiResponse<GetItemsResponse> getOwnedItems(GetItemsRequest request) {
        Query query = getOwnedItemsQuery(request.getFiltersData(), request.getPaginatorData());
        long querySize = getQuerySize(query);
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

    private Query getMarketItemsQuery(FiltersData filtersData, PaginatorData paginatorData) {
        Query queryWithFilters = getQueryWithFilters(filtersData, true);
        Query queryWithSorting = addSorting(queryWithFilters, filtersData.getSorting());
        return addPagination(queryWithSorting, paginatorData);
    }

    private Query getOwnedItemsQuery(FiltersData filtersData, PaginatorData paginatorData) {
        Query queryWithFilters = getQueryWithFilters(filtersData, false);
        Query queryWithOwnedItems = getQueryWithOwnedItems(queryWithFilters);
        Query queryWithSorting = addSorting(queryWithOwnedItems, filtersData.getSorting());
        return addPagination(queryWithSorting, paginatorData);
    }

    private Query getQueryWithFilters(FiltersData filtersData, boolean purchasable) {
        return query(
                new Criteria().andOperator(
                        where(ITEM_PURCHASABLE).is(purchasable),
                        new Criteria().orOperator(
                                where(ITEM_NAME).regex(validatePropertyValue(filtersData.getName())),
                                where(ITEM_NAME).exists(false)
                        ),
                        new Criteria().orOperator(
                                where(ITEM_TYPE).regex(validatePropertyValue(filtersData.getType())),
                                where(ITEM_TYPE).exists(false)
                        ),
                        new Criteria().orOperator(
                                where(ITEM_RARITY).regex(validatePropertyValue(filtersData.getRarity())),
                                where(ITEM_RARITY).exists(false)
                        ),
                        new Criteria().orOperator(
                                where(ITEM_EXTERIOR).regex(validatePropertyValue(filtersData.getExterior())),
                                where(ITEM_EXTERIOR).exists(false)
                        ),
                        new Criteria().orOperator(
                                where(ITEM_OPENABLE).is(filtersData.isOpenable()),
                                where(ITEM_OPENABLE).exists(false))
                        )
                );
    }

    private String validatePropertyValue(String value) {
        return value == null ? "" : Pattern.quote(value);
    }

    private Query addSorting(Query query, SortingData sortingData) {
        Direction direction = Direction.ASC;

        if (sortingData != null && sortingData.getPrice() != null) {
            direction = SORTING_DESC.equals(sortingData.getPrice()) ? Direction.DESC : Direction.ASC;
        }

        return query.with(Sort.by(direction, ITEM_PRICE));
    }

    private Query addPagination(Query query, PaginatorData paginatorData) {
        int pageSize = paginatorData.getPageSize() == 0 ? DEFAULT_PAGE_SIZE : paginatorData.getPageSize();
        int pageNumber = paginatorData.getPageNumber() == 0 ? 1 : paginatorData.getPageNumber() - 1;
        long skipValue = (long) (pageNumber - 1) * pageSize;
        return query.skip(skipValue).limit(pageSize);
    }

    private Query getQueryWithOwnedItems(Query query) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return query.addCriteria(where(ID).in(userDetails.getOwnedItems()));
    }

    private long getQuerySize(Query query) {
        long prevSkip = query.getSkip();
        int prevLimit = query.getLimit();
        long querySize = mongoTemplate.count(query.skip(0).limit(0), Item.class);
        query.skip(prevSkip).limit(prevLimit);
        return querySize;
    }
}
