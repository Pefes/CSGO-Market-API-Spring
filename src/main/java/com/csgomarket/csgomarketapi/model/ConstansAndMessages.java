package com.csgomarket.csgomarketapi.model;

public interface ConstansAndMessages {

    int DEFAULT_PAGE_SIZE = 25;
    String SORTING_ASC = "asc";
    String SORTING_DESC = "desc";
    String ID = "id";

//    ITEMS COLLECTION
    String ITEMS_COLLECTION = "items";
    String ITEM_NAME = "name";
    String ITEM_TYPE = "type";
    String ITEM_RARITY = "rarity";
    String ITEM_EXTERIOR = "exterior";
    String ITEM_OPENABLE = "openable";
    String ITEM_PRICE = "price";
    String ITEM_PURCHASABLE = "purchasable";
    String EXTERIOR_FACTORY_NEW = "Factory New";
    String TYPE_WEAPON = "Weapon";

//    USERS COLLECTION
    String USERS_COLLECTION = "users";
    String USER_USERNAME = "username";

//    LAST_OPENED COLLECTION
    String LAST_OPENED_COLLECTION = "lastopeneds";
    String LAST_OPENED_OPENED_DATE = "openedDate";

//    MESSAGES
    String SUCCESS = "Success";
    String FAIL = "Fail";
    String MESSAGE_USERNAME_ALREADY_EXISTS = "Username already exists";
    String MESSAGE_BUY_ITEM_ERROR = "Could not buy item";
    String MESSAGE_SELL_ITEM_ERROR = "Could not sell item";
    String MESSAGE_OPEN_CONTAINER_ERROR = "Could not open container";
    String MESSAGE_OPEN_TRY_OUT_CONTAINER_ERROR = "Could not open try out container";
    String MESSAGE_GET_AUTOCOMPLETE_OPTIONS_ERROR = "Could not open get autocomplete options";
    String MESSAGE_SET_USER_SETTINGS_ERROR = "Could not set user settings";
}
