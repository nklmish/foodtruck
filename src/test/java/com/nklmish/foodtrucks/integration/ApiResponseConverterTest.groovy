package com.nklmish.foodtrucks.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nklmish.foodtrucks.entity.FoodTruck
import com.nklmish.foodtrucks.integration.datasf.ApiResponse
import com.nklmish.foodtrucks.integration.datasf.ApiResponseConverter
import spock.lang.Shared
import spock.lang.Specification

class ApiResponseConverterTest extends Specification {

    @Shared
    List<ApiResponse> apiResponses

    ApiResponseConverter converter

    void setup() {
        apiResponses = readJson()
        converter = new ApiResponseConverter()
    }

    def "we should be able to convert API responses to FoodTruck entities"() {
        when:
        List<FoodTruck> models = converter.convert(apiResponses)

        then:
        models.size() == apiResponses.size()
    }


    def "we shouldn't loose any data during conversion of API response to foodTruck entities"() {

        when:
        List<FoodTruck> models = converter.convert(apiResponses)

        then:
        models.indexed().collect {index, model ->
            ApiResponse dto = apiResponses.get(index)
            assert model.address == dto.address
            assert model.applicant == dto.applicant
            assert model.schedule == dto.schedule
            assert model.schedule == dto.schedule
            assert model.status.toString() == dto.status
            assert model.locationDescription == dto.locationdescription
            assert model.getLocation().getX() == dto.longitude
            assert model.getLocation().getY() == dto.latitude
            assert model.getFoodItems() == converter.extractFoodItems(dto)
        }
    }


    def "in case of non-existing status mapping, we should always classify FoodTruck with unknown status"() {
        given:'an API response containing status with non-existing mapping'
        ApiResponse apiResponse = Stub(ApiResponse)
        apiResponse.getStatus() >> "abcd"

        when:
        FoodTruck model = converter.convert(apiResponse)

        then:
        model.status == FoodTruck.Status.UNKNOWN
    }

    private List<ApiResponse> readJson() {
        return Arrays.asList(
                new ObjectMapper().readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream("api-response.json"),
                        ApiResponse[].class)
        )
    }
}
