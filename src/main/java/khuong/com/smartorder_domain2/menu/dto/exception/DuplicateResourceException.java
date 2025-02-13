package khuong.com.smartorder_domain2.menu.dto.exception;



public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}