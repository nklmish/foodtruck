package com.nklmish.foodtrucks.rest.foodtruck;

import com.nklmish.foodtrucks.entity.FoodTruck;
import com.nklmish.foodtrucks.repository.FoodTruckRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nklmish.foodtrucks.rest.ApiConstant.Documentation.PAGINATED_RESULTS;
import static com.nklmish.foodtrucks.rest.ApiConstant.Format.APPLICATION_HAL_JSON;
import static com.nklmish.foodtrucks.rest.ApiConstant.Urls.FOOD_TRUCKS;
import static com.nklmish.foodtrucks.rest.PaginationConstant.DEFAULT_RETURN_RECORD_COUNT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = FOOD_TRUCKS, produces = APPLICATION_HAL_JSON)
@Api(value = FOOD_TRUCKS, description = "Manage food trucks", produces = APPLICATION_HAL_JSON, consumes = APPLICATION_JSON_VALUE)
public class FoodTruckController {
    private FoodTruckRepository foodTruckRepository;
    private FoodTruckResourceAssembler foodTruckResourceAssembler;

    @Autowired
    public FoodTruckController(FoodTruckRepository foodTruckRepository, FoodTruckResourceAssembler assembler) {
        this.foodTruckRepository = foodTruckRepository;
        this.foodTruckResourceAssembler = assembler;
    }

    @ApiOperation(value = "Get food truck details via id", nickname = "getFoodTruckDetails",
            notes = "Get details for a particular food truck",
            response = FoodTruckResource.class, responseContainer = "List"
    )
    @ApiResponses( {
            @ApiResponse( code = 500, message = "invalid truck id" )
    } )
    @RequestMapping(value = "/{id}")
    public HttpEntity<FoodTruckResource> getFoodTruckDetails(@PathVariable("id") String id) {
        FoodTruck entity = foodTruckRepository.findOne(id);
        return new ResponseEntity<>(foodTruckResourceAssembler.toResource(entity), HttpStatus.OK);
    }

   @ApiOperation(value = "get food trucks", nickname = "listFoodTrucks", notes = PAGINATED_RESULTS,
            response = FoodTruckResource.class, responseContainer = "PagedResources"
    )
    @ApiResponses( {
            @ApiResponse( code = 500, message = "invalid page size" )
    } )
    @RequestMapping()
    public HttpEntity<PagedResources<FoodTruckResource>> getFoodTruckResources(@PageableDefault(size = DEFAULT_RETURN_RECORD_COUNT, page = 0)Pageable pageable,
                                                                               PagedResourcesAssembler<FoodTruck> assembler) {

        Page<FoodTruck> entities = foodTruckRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(entities, foodTruckResourceAssembler), HttpStatus.OK);
    }

}
