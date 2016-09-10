package com.nklmish.foodtrucks.control;

import com.nklmish.foodtrucks.integration.datasf.ApiResponse;
import org.springframework.stereotype.Component;

@Component
public class LocationValidator implements Validator<ApiResponse> {

    public boolean validate(ApiResponse response) {
        return response != null && !isInvalidLocation(response);

    }

    private boolean isInvalidLocation(ApiResponse response) {
        return response.getLongitude() == 0 && response.getLatitude() == 0;
    }

}
