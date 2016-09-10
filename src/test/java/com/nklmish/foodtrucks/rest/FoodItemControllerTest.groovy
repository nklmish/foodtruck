package com.nklmish.foodtrucks.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.nklmish.foodtrucks.SpringSpecification
import com.nklmish.foodtrucks.entity.FoodTruck
import com.nklmish.foodtrucks.repository.FoodTruckRepository
import com.nklmish.foodtrucks.rest.fooditem.FoodItemController
import com.nklmish.foodtrucks.rest.fooditem.FoodItemResource
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static com.nklmish.foodtrucks.entity.FoodTruck.Status.APPROVED
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class FoodItemControllerTest extends SpringSpecification {


    @Autowired
    FoodTruckRepository repository

    @Autowired
    FoodItemController foodItemController

    MockMvc mockMvc

    static String basePath = ApiConstant.Urls.FOOD_ITEMS

    List<FoodItemResource> expectedFoodItems

    void setup() {
        List<FoodTruck> foodTrucks = new ArrayList<>()

        FoodTruck foodTruck1 = new FoodTruck(location: new GeoJsonPoint(-122.408838, 37.782834), address: '945 MARKET ST',
                applicant: 'Yummy Hot Dogs', locationDescription: 'location1', status: APPROVED,
                foodItems: ["hot dogs", "pretzels", "chips", "juices and cold drinks"],
                daysHours: 'Mo/We/Fr:10AM-7PM', schedule: 'http://somedomain1.com')

        foodTrucks.add(foodTruck1)

        FoodTruck foodTruck2 = new FoodTruck(location: new GeoJsonPoint(-122.409669, 37.780694), address: '1028 MISSION ST',
                applicant: "Rita's Catering", locationDescription: 'location2', status: APPROVED,
                foodItems: ['filipino food'],
                daysHours: 'Mo/We/Fr:7AM-3PM', schedule: 'http://somedomain2.com')

        foodTrucks.add(foodTruck2)

        expectedFoodItems = new ArrayList<>()

        foodTrucks.each {
            repository.save(it)
            it.foodItems.each {expectedFoodItems.add(new FoodItemResource(it))}
        }
        mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build()
    }

    def "should list all food items offered by various vendors"() {
        when:
        def response = mockMvc.perform(get(basePath))

        then:
        response.andExpect(status().isOk()).andExpect(content()
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath('$.', Matchers.hasSize(expectedFoodItems.size())))

        cleanup:
        repository.deleteAll()

    }
}
