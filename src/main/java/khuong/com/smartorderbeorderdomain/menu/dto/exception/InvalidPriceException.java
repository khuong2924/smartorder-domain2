package khuong.com.smartorderbeorderdomain.menu.dto.exception;



public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(String message) {
        super(message);
    }
}