package com.nklmish.foodtrucks.integration

import com.nklmish.foodtrucks.control.Validator
import com.nklmish.foodtrucks.integration.datasf.ApiResponse
import com.nklmish.foodtrucks.integration.datasf.Converter
import org.springframework.web.client.RestOperations
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject


class FoodTruckFetcherTest extends Specification {

    @Shared
    List<Validator<ApiResponse>> validators

    void setup() {
        validators = Arrays.asList(Mock(Validator))
    }

    def "we should be able to fetch and parse data from third party API"() {

        given:
        RestOperations restTemplate = Mock(RestOperations.class)
        String url = _
        Converter converter = Mock(Converter.class)
        ApiResponse [] apiResponses = new ApiResponse[1]
        apiResponses[0] = new ApiResponse(address: "aaa", applicant: "bbb", schedule: "ccc", status: "dddd",
                dayshours: "fff", fooditems: "eeee", locationdescription: "fff", latitude: 1, longitude: 1)
        and:

        @Subject
        FoodTruckFetcher lookupService = new FoodTruckFetcher(url, restTemplate, converter, validators)

        when:
        lookupService.fetch()

        then:
        1 * restTemplate.getForObject(url, ApiResponse[].class) >> apiResponses

        apiResponses.each {
            ApiResponse response = it
            1 * converter.convert(response) >> true
            validators.forEach({1 * it.validate(response) >> true})
        }

    }

    def "we should not consult with FoodTruck converter, when third party API  return's invalid data"() {

        given:
        RestOperations restTemplate = Mock(RestOperations.class)
        String url = _
        Converter converter = Mock(Converter.class)

        @Subject
        FoodTruckFetcher lookupService = new FoodTruckFetcher(url, restTemplate, converter, validators)

        when:
        lookupService.fetch()

        then:
        1 * restTemplate.getForObject(url, ApiResponse[].class)
        0 * converter.convert(Arrays.asList())

    }

}
