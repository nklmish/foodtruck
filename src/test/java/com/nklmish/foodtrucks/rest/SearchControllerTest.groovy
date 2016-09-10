package com.nklmish.foodtrucks.rest

import com.nklmish.foodtrucks.SpringSpecification
import com.nklmish.foodtrucks.entity.FoodTruck
import com.nklmish.foodtrucks.repository.FoodTruckRepository
import com.nklmish.foodtrucks.rest.search.SearchController
import org.hamcrest.Matchers
import org.junit.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.data.web.PagedResourcesAssemblerArgumentResolver
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.View
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.json.MappingJackson2JsonView
import spock.lang.Unroll

import static com.nklmish.foodtrucks.entity.FoodTruck.Status.APPROVED
import static com.nklmish.foodtrucks.rest.ApiConstant.Format.APPLICATION_HAL_JSON
import static com.nklmish.foodtrucks.rest.ApiConstant.Urls.FOOD_TRUCKS_SEARCH
import static com.nklmish.foodtrucks.rest.ApiConstant.Urls.FOOD_TRUCKS_SEARCH_ALL
import static com.nklmish.foodtrucks.rest.ApiConstant.Urls.FOOD_TRUCKS_SEARCH_DEFAULT_LOCATION
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class SearchControllerTest extends SpringSpecification {


    @Autowired
    FoodTruckRepository repository

    @Autowired
    SearchController searchController

    @Value('${search.default.distance.unit}')
    String unit

    @Value('${search.default.longitude}')
    private Double defaultLongitude;

    @Value('${search.default.latitude}')
    private Double defaultLatitude;

    MockMvc mockMvc

    static String basePath = FOOD_TRUCKS_SEARCH

    static GeoJsonPoint searchPoint = new GeoJsonPoint(-122.408838, 37.782834)

    HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        mappingJackson2HttpMessageConverter = Arrays.asList(converters)
                .find { it instanceof MappingJackson2HttpMessageConverter }

        Assert.assertNotNull("the JSON message converter must not be null", mappingJackson2HttpMessageConverter);
    }

    void setup() {


        FoodTruck foodTruck1 = new FoodTruck(location: new GeoJsonPoint(-122.408838, 37.782834), address: '945 MARKET ST',
                applicant: 'Yummy Hot Dogs', locationDescription: 'location1', status: APPROVED,
                foodItems: ["hot dogs", "pretzels", "chips", "juices and cold drinks"],
                daysHours: 'Mo/We/Fr:10AM-7PM', schedule: 'http://somedomain1.com')

        FoodTruck foodTruck2 = new FoodTruck(location: new GeoJsonPoint(-122.409669, 37.780694), address: '1028 MISSION ST',
                applicant: "Rita's Catering", locationDescription: 'location2', status: APPROVED,
                foodItems: ['filipino food'],
                daysHours: 'Mo/We/Fr:7AM-3PM', schedule: 'http://somedomain2.com')


        FoodTruck foodTruck3 = new FoodTruck(location: new GeoJsonPoint(-122.407423, 37.785111), address: '870 MARKET ST',
                applicant: "The New York Frankfurter Co. of CA, Inc. DBA: Annie's Hot Dogs",
                locationDescription: 'location3', status: APPROVED,
                foodItems: ['soft pretzels', 'hot dogs', 'sausages', 'chips', 'popcorn', 'soda', 'espresso', 'cappucino', 'pastry', 'ica cream', 'ices', 'italian sausage', 'shish-ka-bob', 'churros', 'juice, water', 'various drinks'],
                daysHours: 'Mo-Su:6AM-9PM', schedule: 'http://somedomain3.com')


        FoodTruck foodTruck4 = new FoodTruck(location: new GeoJsonPoint(-122.405677, 37.784990), address: '801 MARKET ST',
                applicant: "Creme Brulee To Go", locationDescription: 'location4', status: APPROVED,
                foodItems: ['kettle corn'],
                daysHours: 'Mo-Su:11AM-7PM', schedule: 'http://somedomain4.com')

        FoodTruck foodTruck5 = new FoodTruck(location: new GeoJsonPoint(-122.404391, 37.785396), address: '773 MARKET ST',
                applicant: "Kettle Corn Star", locationDescription: 'location5', status: APPROVED,
                foodItems: ['kettle corn', 'funnel cakes', 'hot dog', 'lemonade', 'beverages', 'flan'],
                daysHours: 'Mo-Su:10AM-6PM', schedule: 'http://somedomain5.com')

        List<FoodTruck> foodTrucks = [foodTruck1, foodTruck2, foodTruck3, foodTruck4, foodTruck5]

        foodTrucks.forEach({
            repository.save(it)
        })

        mockMvc = MockMvcBuilders.standaloneSetup(searchController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                new PagedResourcesAssemblerArgumentResolver(new HateoasPageableHandlerMethodArgumentResolver(), null))
                .setViewResolvers(new ViewResolver() {
            @Override
            public View resolveViewName(String viewName, Locale locale) throws Exception {
                return new MappingJackson2JsonView();
            }
        }).build();

    }

    @Unroll("lng #lng lat #lat distance #distance")
    def "should list food trucks based on proximity"() {
        expect:
        def response = mockMvc.perform(get(basePath + "/?lat=" + lat + "&lng=" + lng + "&dist=" + distance + "&unit=" + unit)
                .contentType(APPLICATION_JSON))

        response.andExpect(status().isOk()).andExpect(content()
                .contentType(APPLICATION_HAL_JSON))
                .andExpect(jsonPath('$.content', Matchers.hasSize(expectedFoodTrucks)))

        where:
        lng           | lat           | distance         || expectedFoodTrucks
        searchPoint.x | searchPoint.y | 0.1              || 1
        searchPoint.x | searchPoint.y | 0.2              || 3
        searchPoint.x | searchPoint.y | 0.4              || 5


    }


    @Unroll("lng #lng lat #lat distance #distance fooditems #items")
    def "should list food trucks based on proximity and food items"() {
        expect:
        def response = mockMvc.perform(get(basePath + FOOD_TRUCKS_SEARCH_ALL + "/?lat=" + lat + "&lng=" + lng + "&dist=" + distance + "&unit=" + unit + "&items=" + items)
                .contentType(APPLICATION_JSON))

        response.andExpect(status().isOk()).andExpect(content()
                .contentType(APPLICATION_HAL_JSON))
                .andExpect(jsonPath('$.', Matchers.hasSize(expectedFoodTrucks)))


        where:
        lng           | lat           | distance   | items                                           || expectedFoodTrucks
        searchPoint.x | searchPoint.y | 0.1        | ["hot dogs"]                                    || 1
        searchPoint.x | searchPoint.y | 0.2        | ["chips"]                                       || 2
        searchPoint.x | searchPoint.y | 0.4        | ["hot dogs", "chips", "kettle corn"]            || 4


    }

    def "should return default search geo location"() {
        when:
        def response = mockMvc.perform(get(basePath + FOOD_TRUCKS_SEARCH_DEFAULT_LOCATION)
                .contentType(APPLICATION_JSON))

        then:
        response.andExpect(status().isOk()).andExpect(content()
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(jsonPath('$.[0].name', Matchers.equalToIgnoringWhiteSpace("lng")))
                .andExpect(jsonPath('$.[0].value', Matchers.is(defaultLongitude)))
                .andExpect(jsonPath('$.[1].name', Matchers.equalToIgnoringWhiteSpace("lat")))
                .andExpect(jsonPath('$.[1].value', Matchers.is(defaultLatitude)))
    }
}
