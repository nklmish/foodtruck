package com.nklmish.foodtrucks.control

import com.nklmish.foodtrucks.integration.datasf.ApiResponse
import spock.lang.Specification
import spock.lang.Unroll

class LocationValidatorTest extends Specification {

    @Unroll("longitude #response.longitude , latitude #response.latitude")
    def "we shouldn't be able to detect invalid geo locations"() {
        Validator<ApiResponse> validator = new LocationValidator()

        expect:
        validator.validate(response) == expectedResult

        where:
        response                                       || expectedResult
        new ApiResponse(longitude: 1, latitude: 1)     ||   true
        new ApiResponse(longitude: 0, latitude: 1)     ||   true
        new ApiResponse(longitude: 0, latitude: 0.11)  ||   true
        new ApiResponse(longitude: 1, latitude: 0)     ||   true
        new ApiResponse(longitude: 0, latitude: 0)     ||   false
        new ApiResponse(longitude: -1, latitude: -1)   ||   true
        new ApiResponse(longitude: -0, latitude: -0)   ||   false

    }
}
