package com.nklmish.foodtrucks.rest;

public class ApiConstant {

    public static class Urls {
        public static final String API_ROOT = "/api";
        public static final String FOOD_TRUCKS = API_ROOT + "/" + "foodtrucks";
        public static final String FOOD_ITEMS = API_ROOT + "/" + "fooditems";
        public static final String FOOD_TRUCKS_SEARCH = API_ROOT + "/" + "search";
        public static final String FOOD_TRUCKS_SEARCH_ALL =  "/all";
        public static final String FOOD_TRUCKS_SEARCH_DEFAULT_LOCATION =  "/default/loc";
    }

    public static class Format {
        public static final String APPLICATION_HAL_JSON = "application/hal+json";
    }

    public static class Documentation {
        public static final String PAGINATED_RESULTS = "Please note results are paginated. Pagination starts from 0 and number of results on a specific page can be " +
                "altered via size parameter(default 25)";
    }

}
