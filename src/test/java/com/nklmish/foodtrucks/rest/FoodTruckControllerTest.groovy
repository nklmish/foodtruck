package com.nklmish.foodtrucks.rest

import com.nklmish.foodtrucks.SpringSpecification
import com.nklmish.foodtrucks.entity.FoodTruck
import com.nklmish.foodtrucks.repository.FoodTruckRepository
import com.nklmish.foodtrucks.rest.foodtruck.FoodTruckController
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.json.MappingJackson2JsonView

import static com.nklmish.foodtrucks.entity.FoodTruck.Status.APPROVED
import static com.nklmish.foodtrucks.rest.ApiConstant.Format.APPLICATION_HAL_JSON
import static org.hamcrest.Matchers.containsInAnyOrder
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class FoodTruckControllerTest extends SpringSpecification {


    @Autowired
    FoodTruckRepository repository

    @Autowired
    FoodTruckController foodTruckController

    String id

    MockMvc mockMvc

    static String basePath = ApiConstant.Urls.FOOD_TRUCKS

    void setup() {
        FoodTruck foodTruck = new FoodTruck(location: new GeoJsonPoint(-122.408838, 37.782834), address: '945 MARKET ST',
                applicant: 'Yummy Hot Dogs', locationDescription: 'location1', status: APPROVED,
                foodItems: ["hot dogs", "pretzels", "chips", "juices and cold drinks"],
                daysHours: 'Mo/We/Fr:10AM-7PM', schedule: 'http://somedomain1.com')

        id = repository.save(foodTruck).getId()

        mockMvc = MockMvcBuilders.standaloneSetup(foodTruckController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                new PagedResourcesAssemblerArgumentResolver(new HateoasPageableHandlerMethodArgumentResolver(), null))
                .setViewResolvers(new ViewResolver() {
            @Override
            public View resolveViewName(String viewName, Locale locale) throws Exception {
                return new MappingJackson2JsonView();
            }
        }).build();
    }

    def "should able to find details for an existing food truck"() {
        given:
        FoodTruck expectedFoodTruck = repository.findOne(id)

        when:
        def response = mockMvc.perform(get(basePath + "/" + id))

        then:
        response.andExpect(status().isOk()).andExpect(content()
                .contentType(APPLICATION_HAL_JSON))
                .andExpect(jsonPath('$.applicant').value(expectedFoodTruck.applicant))
                .andExpect(jsonPath('$.address').value(expectedFoodTruck.address))
                .andExpect(jsonPath('$.daysHours').value(expectedFoodTruck.daysHours))
                .andExpect(jsonPath('$.foodItems', containsInAnyOrder(expectedFoodTruck.foodItems.toArray())))
                .andExpect(jsonPath('$.location.x').value(expectedFoodTruck.getLocation().getX()))
                .andExpect(jsonPath('$.location.y').value(expectedFoodTruck.getLocation().getY()))
                .andExpect(jsonPath('$.locationDescription').value(expectedFoodTruck.locationDescription))
                .andExpect(jsonPath('$.schedule').value(expectedFoodTruck.schedule))
                .andExpect(jsonPath('$.status').value(expectedFoodTruck.status.toString()))

        cleanup:
        repository.deleteAll()

    }

    def "should list all food trucks available in the repo"() {
        given:
        List<FoodTruck> expectedFoodTrucks = repository.findAll()

        when:
        def response = mockMvc.perform(get(basePath ))

        then:
        response.andExpect(status().isOk()).andExpect(content()
                .contentType(APPLICATION_HAL_JSON))
                .andExpect(jsonPath('$.content', Matchers.hasSize(expectedFoodTrucks.size())))

        cleanup:
        repository.deleteAll()


    }

}
