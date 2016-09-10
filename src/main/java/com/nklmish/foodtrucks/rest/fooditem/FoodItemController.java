package com.nklmish.foodtrucks.rest.fooditem;

import com.nklmish.foodtrucks.repository.FoodTruckRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.nklmish.foodtrucks.rest.ApiConstant.Urls.FOOD_ITEMS;
import static com.nklmish.foodtrucks.rest.ApiConstant.Urls.FOOD_TRUCKS;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = FOOD_ITEMS, produces = APPLICATION_JSON_VALUE)
@Api(value = FOOD_TRUCKS, description = "Manage food items", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class FoodItemController {

    private FoodTruckRepository foodTruckRepository;

    @Autowired
    public FoodItemController(FoodTruckRepository foodTruckRepository) {
        this.foodTruckRepository = foodTruckRepository;
    }

    @ApiOperation(value = "find available food items offers by food trucks", nickname = "findAvailableFoodItems",
            notes = "List all available food items that are currently offered by various food truck vendors ",
            response = FoodItemResource.class,
            responseContainer = "List"
    )
    @RequestMapping
    public List<FoodItemResource> findAvailableFoodItems() {
        return foodTruckRepository.findDistinctFoodItems().stream().map(FoodItemResource::new).collect(toList());
    }
}
