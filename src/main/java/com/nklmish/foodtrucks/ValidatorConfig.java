package com.nklmish.foodtrucks;

import com.nklmish.foodtrucks.control.ApplicantValidator;
import com.nklmish.foodtrucks.control.StatusValidator;
import com.nklmish.foodtrucks.control.Validator;
import com.nklmish.foodtrucks.integration.datasf.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ValidatorConfig {
    @Bean
    public Validator<ApiResponse> statusValidator() {
        return new StatusValidator();
    }

    @Bean
    public Validator<ApiResponse> applicantValidator() {
        return new ApplicantValidator();
    }

    @Bean
    public List<Validator<ApiResponse>> validators() {
        return Arrays.asList(statusValidator(), applicantValidator());
    }
}
