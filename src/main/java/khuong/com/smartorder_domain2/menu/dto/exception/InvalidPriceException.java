package khuong.com.smartorder_domain2.menu.dto.exception;



public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(String message) {
        super(message);
    }
}