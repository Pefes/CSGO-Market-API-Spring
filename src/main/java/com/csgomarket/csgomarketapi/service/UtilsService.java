package com.csgomarket.csgomarketapi.service;

import com.csgomarket.csgomarketapi.payload.response.ApiResponse;
import com.mongodb.client.DistinctIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.*;
import static com.csgomarket.csgomarketapi.util.GetApiResponse.getApiResponse;

@Service
public class UtilsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ApiResponse<?> getAutocompleteOptions(String property) {
        if (!property.isEmpty()) {
            List<String> options = new ArrayList<>();
            DistinctIterable<String> optionsIterable = mongoTemplate.getCollection(ITEMS_COLLECTION).distinct(property, String.class);
            optionsIterable.forEach(options::add);

            return getApiResponse(SUCCESS, null, options);
        }

        return getApiResponse(FAIL, MESSAGE_GET_AUTOCOMPLETE_OPTIONS_ERROR, null);
    }
}
