package com.nklmish.foodtrucks.integration;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.nklmish.foodtrucks.control.Validator;
import com.nklmish.foodtrucks.entity.FoodTruck;
import com.nklmish.foodtrucks.integration.datasf.ApiResponse;
import com.nklmish.foodtrucks.integration.datasf.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FoodTruckFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(FoodTruckFetcher.class);

    private RestOperations restTemplate;

    private String url;

    private Converter<FoodTruck, ApiResponse> converter;

    private List<Validator<ApiResponse>> validators;

    @Autowired
    public FoodTruckFetcher(@Value("${foodtruck.api.url}") String url,
                            RestOperations restTemplate,
                            Converter<FoodTruck, ApiResponse> converter,
                            List<Validator<ApiResponse>> validators) {
        this.restTemplate = restTemplate;
        this.url = url;
        this.converter = converter;
        this.validators = validators;
    }

    @HystrixCommand(fallbackMethod = "defaultFoodTrucks")
    public List<FoodTruck> fetch() {
        LOG.trace("fetching data from {} ...", url);
        List<FoodTruck> foodTrucks = new ArrayList<>();

        try {
            ApiResponse[] apiResponses = restTemplate.getForObject(url, ApiResponse[].class);
            if (apiResponses != null) {
                List<ApiResponse> dtos = Arrays.asList(apiResponses);
                dtos.stream().filter(this::validate).forEach(dto -> {
                    foodTrucks.add(converter.convert(dto));
                });
            }
        } catch (RestClientException e) {
            LOG.error("error fetching date from {} details {}", url, e);
        }


        LOG.trace("total items fetched {}", foodTrucks.size());

        return foodTrucks;
    }

    public boolean validate(ApiResponse response) {
        for (Validator<ApiResponse> validator : validators) {
            if (!validator.validate(response)) {
                return false;
            }
        }
        return true;
    }

    private List<FoodTruck> defaultFoodTrucks() {
        return new ArrayList<>();
    }
}
