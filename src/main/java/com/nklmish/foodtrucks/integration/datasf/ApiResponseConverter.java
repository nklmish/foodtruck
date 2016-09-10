package com.nklmish.foodtrucks.integration.datasf;

import com.nklmish.foodtrucks.entity.FoodTruck;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ApiResponseConverter implements Converter<FoodTruck, ApiResponse> {

    @Override
    public FoodTruck convert(ApiResponse dto) {
        FoodTruck foodTruck = new FoodTruck();
        foodTruck.setApplicant(dto.getApplicant());
        foodTruck.setAddress(dto.getAddress());
        foodTruck.setDaysHours(dto.getDayshours());
        foodTruck.setFoodItems(extractFoodItems(dto));
        foodTruck.setLocation(new GeoJsonPoint(dto.getLongitude(), dto.getLatitude()));
        foodTruck.setLocationDescription(dto.getLocationdescription());
        foodTruck.setSchedule(dto.getSchedule());
        foodTruck.setStatus(FoodTruck.Status.getByCode(dto.getStatus()));
        return foodTruck;
    }

    public List<String> extractFoodItems(ApiResponse dto) {
        List<String> foodItems = new ArrayList<>();

        if (hasFoodItems(dto)) {
            Arrays.stream(dto.getFooditems().split(":")).map(String::toLowerCase).forEach(foodItem -> foodItems.add(foodItem.trim()));
        }

        return foodItems;
    }

    private boolean hasFoodItems(ApiResponse dto) {
        return dto.getFooditems() != null && !dto.getFooditems().isEmpty();
    }
}
