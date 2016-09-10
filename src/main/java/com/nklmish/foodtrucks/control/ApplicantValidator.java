package com.nklmish.foodtrucks.control;

import com.nklmish.foodtrucks.integration.datasf.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ApplicantValidator implements Validator<ApiResponse> {

    public boolean validate(ApiResponse response) {
        return response != null && isValidApplicant(response);

    }

    private boolean isValidApplicant(ApiResponse response) {
        return StringUtils.isNotBlank(response.getApplicant());
    }

}
