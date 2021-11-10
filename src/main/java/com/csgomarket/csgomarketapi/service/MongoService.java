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
import java.util.regex.Pattern;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class MongoService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Query getQueryWithFilters(FiltersData filtersData, boolean purchasable) {
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

    public Query addSorting(Query query, SortingData sortingData) {
        Sort.Direction direction = Sort.Direction.ASC;

        if (sortingData != null && sortingData.getPrice() != null) {
            direction = SORTING_DESC.equals(sortingData.getPrice()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        }

        return query.with(Sort.by(direction, ITEM_PRICE));
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
