package com.nklmish.foodtrucks.rest.fooditem;

public class FoodItemResource {

    private final String name;

    public FoodItemResource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
