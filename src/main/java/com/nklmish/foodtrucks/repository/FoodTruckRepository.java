package com.nklmish.foodtrucks.repository;

import com.nklmish.foodtrucks.entity.FoodTruck;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface FoodTruckRepository extends BaseRepo {
    List<String> findDistinctFoodItems();

    List<FoodTruck> findByLocationNear(Point point, Distance distance, List<String> foodItems);

}