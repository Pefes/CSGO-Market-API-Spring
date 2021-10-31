package com.csgomarket.csgomarketapi.service;

import com.csgomarket.csgomarketapi.model.item.Item;
import com.csgomarket.csgomarketapi.payload.request.getitems.FiltersData;
import com.csgomarket.csgomarketapi.payload.request.getitems.GetItemsRequest;
import com.csgomarket.csgomarketapi.payload.request.getitems.PaginatorData;
import com.csgomarket.csgomarketapi.payload.request.getitems.SortingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.regex.Pattern;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class ItemsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Item> getMarketItems(GetItemsRequest request) {
        System.out.println(request);
        Query query = getItemsQuery(request.getFiltersData(), request.getPaginatorData());
        return mongoTemplate.find(query, Item.class, ITEMS_COLLECTION);
    }

    private Query getItemsQuery(FiltersData filtersData, PaginatorData paginatorData) {
        Query queryWithFilters = getQueryWithFilters(filtersData);
        Query queryWithSorting = addSorting(queryWithFilters, filtersData.getSorting());
        return addPagination(queryWithSorting, paginatorData);
    }

    private Query getQueryWithFilters(FiltersData filtersData) {
        return query(
                where(ITEM_NAME).regex(validatePropertyValue(filtersData.getName()))
                .and(ITEM_TYPE).regex(validatePropertyValue(filtersData.getType()))
                .and(ITEM_RARITY).regex(validatePropertyValue(filtersData.getRarity()))
                .and(ITEM_EXTERIOR).regex(validatePropertyValue(filtersData.getExterior()))
                .and(ITEM_OPENABLE).is(filtersData.isOpenable()));
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
}
