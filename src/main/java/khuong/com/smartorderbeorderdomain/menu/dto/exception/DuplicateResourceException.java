package khuong.com.smartorderbeorderdomain.menu.dto.exception;



public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}