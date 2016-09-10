package com.nklmish.foodtrucks.repository

import com.nklmish.foodtrucks.integration.FoodTruckFetcher
import com.nklmish.foodtrucks.scheduler.FoodTruckScheduler
import spock.lang.Specification


class FoodTruckSchedulerTest extends Specification {

    def "we should save and retrieve new data while updating database"() {

        given:
        FoodTruckFetcher foodTruckLookupService = Mock(FoodTruckFetcher)
        FoodTruckRepository repository = Mock(FoodTruckRepository)

        FoodTruckScheduler foodTruckService = new FoodTruckScheduler(foodTruckLookupService, repository, true)

        when:
        foodTruckService.updateDb()

        then:
        1 * foodTruckLookupService.fetch() >> new ArrayList<>()
        1 * repository.save(_) >> new ArrayList<>()
    }
}
