package com.nklmish.foodtrucks.repository;

import com.nklmish.foodtrucks.entity.FoodTruck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

interface BaseRepo extends MongoRepository<FoodTruck, String> {

    Page<FoodTruck> findByLocationNear(Point point, Distance distance, Pageable pageable);

    FoodTruck findByApplicant(String applicant);

    FoodTruck findByApplicantAndAddress(String applicant, String address);
}

