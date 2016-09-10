package com.nklmish.foodtrucks.rest.foodtruck;

import com.nklmish.foodtrucks.entity.FoodTruck;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class FoodTruckResourceAssembler implements ResourceAssembler<FoodTruck, FoodTruckResource>{

    @Override
    public FoodTruckResource toResource(FoodTruck entity) {
        FoodTruckResource resource = new FoodTruckResource(entity);
        resource.add(linkTo(methodOn(FoodTruckController.class).getFoodTruckDetails(entity.getId()))
                .withSelfRel());
        return resource;
    }
}
