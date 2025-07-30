package bank.springbootmvc.exceptions;

import java.time.LocalDateTime;

public class ErrorMessageResponse {
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorMessageResponse(String message, LocalDateTime timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
