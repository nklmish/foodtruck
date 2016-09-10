package com.nklmish.foodtrucks.repository

import com.nklmish.foodtrucks.SpringSpecification
import com.nklmish.foodtrucks.entity.FoodTruck
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import spock.lang.Unroll

import static com.nklmish.foodtrucks.entity.FoodTruck.Status.APPROVED
import static org.springframework.data.geo.Metrics.MILES

class FoodTruckRepositoryImplTest extends SpringSpecification {

    @Autowired
    FoodTruckRepositoryImpl foodTruckRepository

    @Autowired
    private BaseRepo baseRepo

    List<FoodTruck> foodTrucks

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

        foodTrucks = [foodTruck1, foodTruck2, foodTruck3, foodTruck4, foodTruck5]

        foodTrucks.forEach({
            foodTruckRepository.save(it)
        })
    }

    def "we should update an existing item rather then creating another duplicate item during insert"() {
        given:
        FoodTruck foodTruck =new FoodTruck(location: new GeoJsonPoint(-122.404391, 37.785396), address: '773 MARKET ST',
                applicant: "Kettle Corn Star", locationDescription: 'location5', status: APPROVED,
                foodItems: ['kettle corn', 'funnel cakes', 'lemonade', 'cheese', 'beverages', 'flan'],
                daysHours: 'Mo-Su:10AM-6PM', schedule: 'http://somedomain5.com')

        when:
        foodTruckRepository.save(foodTruck)

        then:
        foodTruckRepository.findAll().size() == foodTrucks.size()
    }


    @Unroll("when I am searching for food trucks starting from location (#location) , within #maxDistance, I expect to find exactly #expectedFoodTrucks food trucks")
    def "we should be able to find food trucks closer to a given location"() {

        expect:
        List<FoodTruck> closestFoodTrucks = foodTruckRepository.findByLocationNear(location, maxDistance, new PageRequest(0, expectedFoodTrucks)).getContent()
        closestFoodTrucks.size() == expectedFoodTrucks

        where:
        location                            | maxDistance               || expectedFoodTrucks
        new Point(-122.408838, 37.782834)   | new Distance(0.1, MILES)  || 1
        new Point(-122.408838, 37.782834)   | new Distance(0.2, MILES)  || 3
        new Point(-122.408838, 37.782834)   | new Distance(0.3, MILES)  || 4
        new Point(-122.408838, 37.782834)   | new Distance(0.4, MILES)  || 5

    }

    @Unroll("when I am searching for food trucks that sells #foodItems, starting from location (#location) , within #maxDistance, I expect to find exactly #expectedFoodTrucks food trucks")
    def "we should be able to find food trucks closer to a given location based on food preferences"() {

        expect:
        List<FoodTruck> closestFoodTrucks = foodTruckRepository.findByLocationNear(location, maxDistance, foodItems)
        closestFoodTrucks.size() == expectedFoodTrucks

        where:
        location                            | maxDistance               | foodItems              || expectedFoodTrucks
        new Point(-122.408838, 37.782834)   | new Distance(0.1, MILES)  | ['hot dog']            || 1
        new Point(-122.408838, 37.782834)   | new Distance(0.2, MILES)  | ['hot dog']            || 2
        new Point(-122.408838, 37.782834)   | new Distance(0.3, MILES)  | ['filipino food']      || 1

    }

    def "we should be able to retrieve food truck based on applicant"() {
        given:
        String applicant = foodTrucks.get(0).applicant

        when:
        FoodTruck foodTruck = foodTruckRepository.findByApplicant(applicant)

        then:
        foodTruck.applicant == applicant
    }

    def "we should be able to find all distinct food items"() {
        given:
        Set<String> expectedDistinctItems = new HashSet<>()
        foodTrucks.each {expectedDistinctItems.addAll(it.foodItems)}

        when:
        List<String> foodItems = foodTruckRepository.findDistinctFoodItems()

        then:
        foodItems.size() == expectedDistinctItems.size()
        expectedDistinctItems.containsAll(foodItems)
    }

    void cleanup() {
        foodTruckRepository.deleteAll()
    }
}
