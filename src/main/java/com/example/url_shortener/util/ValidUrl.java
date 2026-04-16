package com.example.url_shortener.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidUrlValidator.class)
@Documented
public @interface ValidUrl {
    String message() default "Invalid URL format. Must include protocol (http/https) and a host";

    // Required by Spring Validation API
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
