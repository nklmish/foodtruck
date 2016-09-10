package com.nklmish.foodtrucks.rest.search;

import com.nklmish.foodtrucks.entity.FoodTruck;
import com.nklmish.foodtrucks.repository.FoodTruckRepository;
import com.nklmish.foodtrucks.rest.foodtruck.FoodTruckResource;
import com.nklmish.foodtrucks.rest.foodtruck.FoodTruckResourceAssembler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static com.nklmish.foodtrucks.rest.ApiConstant.Documentation.PAGINATED_RESULTS;
import static com.nklmish.foodtrucks.rest.ApiConstant.Format.APPLICATION_HAL_JSON;
import static com.nklmish.foodtrucks.rest.ApiConstant.Urls.*;
import static com.nklmish.foodtrucks.rest.PaginationConstant.DEFAULT_RETURN_RECORD_COUNT;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = FOOD_TRUCKS_SEARCH, produces = APPLICATION_HAL_JSON)
@Api(value = FOOD_TRUCKS, description = "Manage food trucks", produces = APPLICATION_HAL_JSON, consumes = APPLICATION_JSON_VALUE)
public class SearchController {

    private FoodTruckRepository foodTruckRepository;

    private FoodTruckResourceAssembler foodTruckResourceAssembler;

    @Value("${search.default.longitude}")
    private Double defaultLongitude;

    @Value("${search.default.latitude}")
    private Double defaultLatitude;

    @Autowired
    public SearchController(FoodTruckRepository foodTruckRepository, FoodTruckResourceAssembler assembler) {
        this.foodTruckRepository = foodTruckRepository;
        this.foodTruckResourceAssembler = assembler;
    }

    @ApiOperation(value = "find food trucks based on location proximity",
            nickname = "findFoodTrucksByProximity",
            notes = "You can control search result's proximity via maxDistance value. " + PAGINATED_RESULTS,
            response = FoodTruckResource.class, responseContainer ="List"
    )
    @ApiResponses( {
            @ApiResponse( code = 500, message = "Geo Location with invalid longitude and/or latitude " +
                    "or Page size i.e size variable is <= 0" )
    } )
    @RequestMapping()
    public HttpEntity<PagedResources<FoodTruckResource>> findByProximity(
            @RequestParam(value = "lng", defaultValue = "${search.default.longitude}") Double longitude,
            @RequestParam(value = "lat", defaultValue = "${search.default.latitude}") Double latitude,
            @RequestParam(value = "dist", required = false, defaultValue = "${search.default.distance.max}") Double maxDistance,
            @RequestParam(value = "unit", required = false, defaultValue = "${search.default.distance.unit}") String distanceUnit,
            @PageableDefault(size = DEFAULT_RETURN_RECORD_COUNT, page = 0)Pageable pageable,
            PagedResourcesAssembler<FoodTruck> assembler) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(maxDistance, Metrics.valueOf(distanceUnit));
        Page<FoodTruck> entities = foodTruckRepository.findByLocationNear(point, distance, pageable);
        return new ResponseEntity<>(assembler.toResource(entities, foodTruckResourceAssembler), HttpStatus.OK);
    }


    @ApiOperation(value = "find all food trucks based on location proximity and food preferences",
            nickname = "findAllByProximityAndFoodItems",
            notes = "You can control search result's proximity via maxDistance and/or food preferences value. ",
            response = FoodTruckResource.class, responseContainer ="List"
    )
    @ApiResponses( {
            @ApiResponse( code = 500, message = "Geo Location with invalid longitude and/or latitude" )
    } )
    @RequestMapping(FOOD_TRUCKS_SEARCH_ALL)
    public HttpEntity<List<FoodTruckResource>> findAllByProximityAndFoodItems(
            @RequestParam(value = "lng", defaultValue = "${search.default.longitude}") Double longitude,
            @RequestParam(value = "lat", defaultValue = "${search.default.latitude}") Double latitude,
            @RequestParam(value = "dist", required = false, defaultValue = "${search.default.distance.max}") Double maxDistance,
            @RequestParam(value = "unit", required = false, defaultValue = "${search.default.distance.unit}") String distanceUnit,
            @RequestParam(value = "items", required = false, defaultValue = "#{'${search.default.preference.food.items}'.split(',')}") List<String> foodItems
    ) {
        Point point = new Point(longitude, latitude);
        Distance distance = new Distance(maxDistance, Metrics.valueOf(distanceUnit));
        List<FoodTruck> entities = foodTruckRepository.findByLocationNear(point, distance, foodItems);

        return new ResponseEntity<>(entities.stream().map(entity -> foodTruckResourceAssembler.toResource(entity)).collect(toList()), HttpStatus.OK);
    }


    @ApiOperation(value = "find default search location preference",
            nickname = "getDefaultSearchLocation",
            response = DefaultSearchPreference.class, responseContainer ="List"
    )
    @RequestMapping(value = FOOD_TRUCKS_SEARCH_DEFAULT_LOCATION, produces = APPLICATION_JSON_VALUE)
    public List<DefaultSearchPreference> getDefaultSearchLocation() {
        return Arrays.asList(new DefaultSearchPreference("lng", defaultLongitude), new DefaultSearchPreference("lat", defaultLatitude));
    }
}
