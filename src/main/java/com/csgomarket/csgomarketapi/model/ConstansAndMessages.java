package com.csgomarket.csgomarketapi.model;

public interface ConstansAndMessages {

    int DEFAULT_PAGE_SIZE = 25;
    String SORTING_ASC = "asc";
    String SORTING_DESC = "desc";

//    ITEMS COLLECTION
    String ITEMS_COLLECTION = "items";
    String ITEM_NAME = "name";
    String ITEM_TYPE = "type";
    String ITEM_RARITY = "rarity";
    String ITEM_EXTERIOR = "exterior";
    String ITEM_OPENABLE = "openable";
    String ITEM_PRICE = "price";

//    USERS COLLECTION
    String USERS_COLLECTION = "users";
    String USER_USERNAME = "username";

    String SUCCESS = "Success";
    String FAIL = "Fail";
    String USERNAME_ALREADY_EXISTS = "Username already exists";
}
