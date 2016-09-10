package com.nklmish.foodtrucks.control

import com.nklmish.foodtrucks.integration.datasf.ApiResponse
import spock.lang.Specification

class StatusValidatorTest extends Specification {

    def "we should be able to successfully validate status"() {

        given:'a status validator'
        Validator<ApiResponse> statusValidator = new StatusValidator()

        and:'an API response containing valid status'

        ApiResponse apiResponse = new ApiResponse()
        apiResponse.setStatus("APPROVED")

        when:'we validate status'
        boolean isValidStatus = statusValidator.validate(apiResponse)

        then:
        isValidStatus

    }

    def "we should be able invalidate invalid status"() {

        given:'a status validator'
        Validator<ApiResponse> statusValidator = new StatusValidator()

        and:'an API response containing invalid status'

        ApiResponse apiResponse = new ApiResponse()

        when:
        boolean isInvalidResponse = statusValidator.validate(apiResponse)

        then:
        !isInvalidResponse

    }
}
