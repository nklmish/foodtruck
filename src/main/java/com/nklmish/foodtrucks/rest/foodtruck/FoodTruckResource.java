package com.nklmish.foodtrucks.rest.foodtruck;

import com.nklmish.foodtrucks.entity.FoodTruck;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class FoodTruckResource extends Resource<FoodTruck> {

    public FoodTruckResource(FoodTruck content, Link... links) {
        super(content, links);
    }
}
