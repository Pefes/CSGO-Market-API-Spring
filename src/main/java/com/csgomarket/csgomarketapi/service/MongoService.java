package com.csgomarket.csgomarketapi.service;

import com.csgomarket.csgomarketapi.model.item.Item;
import com.csgomarket.csgomarketapi.payload.request.getitems.FiltersData;
import com.csgomarket.csgomarketapi.payload.request.getitems.PaginatorData;
import com.csgomarket.csgomarketapi.payload.request.getitems.SortingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class MongoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Query getQueryWithFilters(FiltersData filtersData, boolean purchasable) {
        List<Criteria> andCriteria = new ArrayList<>();
        andCriteria.add(where(ITEM_PURCHASABLE).is(purchasable));
        andCriteria.add(getOrCriteriaForTextProperty(ITEM_NAME, filtersData.getName()));
        andCriteria.add(getOrCriteriaForTextProperty(ITEM_TYPE, filtersData.getType()));
        andCriteria.add(getOrCriteriaForTextProperty(ITEM_RARITY, filtersData.getRarity()));
        andCriteria.add(getOrCriteriaForTextProperty(ITEM_EXTERIOR, filtersData.getExterior()));

        if (filtersData.getOpenable() != null) {
            andCriteria.add(new Criteria().orOperator(
                where(ITEM_OPENABLE).is(filtersData.getOpenable()),
                where(ITEM_OPENABLE).exists(false)));
        }

        return query(new Criteria().andOperator(andCriteria));
    }

    private Criteria getOrCriteriaForTextProperty(String key, String value) {
        return new Criteria().orOperator(
            where(key).regex(validatePropertyValue(value)),
            where(key).exists(false));
    }

    private String validatePropertyValue(String value) {
        return value == null ? "" : Pattern.quote(value);
    }

    public Query addSorting(Query query, SortingData sortingData) {
        if (sortingData != null && sortingData.getPrice() != null) {
            Sort.Direction direction = SORTING_DESC.equals(sortingData.getPrice()) ? Sort.Direction.DESC : Sort.Direction.ASC;
            return query.with(Sort.by(direction, ITEM_PRICE));
        }

        return query;
    }

    public Query addPagination(Query query, PaginatorData paginatorData) {
        int pageSize = paginatorData.getPageSize() == 0 ? DEFAULT_PAGE_SIZE : paginatorData.getPageSize();
        int pageNumber = paginatorData.getPageNumber() == 0 ? 1 : paginatorData.getPageNumber() - 1;
        long skipValue = (long) (pageNumber - 1) * pageSize;
        return query.skip(skipValue).limit(pageSize);
    }

    public long getQuerySize(Query query) {
        long prevSkip = query.getSkip();
        int prevLimit = query.getLimit();
        long querySize = mongoTemplate.count(query.skip(0).limit(0), Item.class);
        query.skip(prevSkip).limit(prevLimit);
        return querySize;
    }
}
