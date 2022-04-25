package lk.ijse.dep8.library.exception;

import javax.xml.ws.http.HTTPException;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
