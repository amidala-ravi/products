package com.philips.products.models;

import com.philips.products.models.ErrorMessage;
import com.philips.products.models.ProductCatalog;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to map the errors and set the error messages for input validation from POST end-point
 */
@Component
@NoArgsConstructor
public class ErrorMapper {

    private static final String ERROR_MESSAGE = "Validation failed";
    private static final HashMap<String, String> ERROR_MAP;

    static {
        ERROR_MAP = new HashMap<>();
        ERROR_MAP.put("NotNull", "MISSING_FIELD");
        ERROR_MAP.put("NotEmpty", "MISSING_FIELD");
        ERROR_MAP.put("NotBlank", "MISSING_FIELD");
    }

    public ErrorMessage mapErrors(final ProductCatalog request, final Errors errors) {
        List<ErrorMessage.Error> errorList = errors
                .getAllErrors()
                .stream()
                .map(this::mapToError)
                .collect(Collectors.toList());
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ERROR_MESSAGE);
        errorMessage.setErrors(errorList);
        return errorMessage;
    }

    private ErrorMessage.Error mapToError(ObjectError objectError) {
        FieldError fieldError = (FieldError) objectError;
        ErrorMessage.Error errorResponse = new ErrorMessage.Error();
        errorResponse.setField(fieldError.getField());
        errorResponse.setCode(ERROR_MAP.get(fieldError.getCode()));
        errorResponse.setMessage(fieldError.getDefaultMessage());
        return errorResponse;
    }

}