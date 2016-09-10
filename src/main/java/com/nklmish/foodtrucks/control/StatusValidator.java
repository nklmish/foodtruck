package com.nklmish.foodtrucks.control;

import com.nklmish.foodtrucks.integration.datasf.ApiResponse;
import org.springframework.stereotype.Component;

@Component
public class StatusValidator implements Validator<ApiResponse> {

    public boolean validate(ApiResponse response) {
        return response != null && isValidStatus(response);
    }

    private boolean isValidStatus(ApiResponse response) {
        return response.getStatus() != null;
    }

}
