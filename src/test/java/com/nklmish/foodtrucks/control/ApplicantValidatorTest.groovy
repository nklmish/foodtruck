package com.nklmish.foodtrucks.control

import com.nklmish.foodtrucks.integration.datasf.ApiResponse
import spock.lang.Specification
import spock.lang.Unroll

class ApplicantValidatorTest extends Specification {

    @Unroll("applicant #response.applicant")
    def "we should be able to successfully validate an applicant"() {
        Validator<ApiResponse> validator = new ApplicantValidator()

        expect:
        validator.validate(response) == expectedResult

        where:
        response                                || expectedResult
        new ApiResponse(applicant: "aaa")       ||   true
        new ApiResponse(applicant: "")          ||   false
        new ApiResponse(applicant: "  ")        ||   false
        new ApiResponse()                       ||   false

    }
}
