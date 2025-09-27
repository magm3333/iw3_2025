package ar.edu.iua.iw3.model.business;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationException extends Exception {

    @Builder
    public ValidationException(String message, Throwable ex) {
        super(message, ex);
    }

    @Builder
    public ValidationException(String message) {
        super(message);
    }

    @Builder
    public ValidationException(Throwable ex) {
        super(ex);
    }
}

