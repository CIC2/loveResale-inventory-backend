package com.resale.resaleinventory.models;

public enum ActionType {
    CREATE_LOCATION(1),
    UPDATE_LOCATION(2),
    GET_LOCATIONS(3),
    GET_LOCATION_DETAILS(4),
    DELETE_PROJECT_FROM_LOCATION(5),
    CUSTOMER_MODEL_FILTER(6),
    CUSTOMER_GET_MODELS(7),
    CUSTOMER_GET_MODEL_DETAILS(8),
    CUSTOMER_MODEL_COMPARISON(9),
    GET_MODEL_DETAILS(10),
    GET_MODELS(11),
    UPDATE_MODEL(12),
    GET_PROJECTS(13),
    GET_PROJECT_DETAILS(14),
    UNIT_FILTER(15),
    GET_UNITS_SEARCH(16),
    GET_UNIT_DETAILS(17),
    GET_UNIT_COMPARISON(18),
    GET_SINGLE_PAYMENT(19),
    GET_UNIT_PAYMENT_PLAN(20),
    GET_UNITS(21),
    USER_GET_CUSTOMER_MODELS_HISTORY(22),
    USER_UPDATE_PROJECT_CONFIGURATIONS(23),
    CHECK_UNIT_AVAILABILITY(24),
    CREATE_UNIT(25),


    UNKNOWN(999); // fallback



    private final int code;

    ActionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    }


