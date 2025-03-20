package khuong.com.smartorder_domain2.exception;

import jakarta.servlet.http.HttpServletRequest;
import khuong.com.smartorder_domain2.menu.dto.exception.ErrorResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        
        if (statusCode == null) {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        
        String errorMessage = "Unknown error";
        if (exception != null) {
            errorMessage = exception.getMessage();
        } else if (statusCode == 404) {
            errorMessage = "Resource not found";
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
                statusCode,
                "ERROR_" + statusCode,
                errorMessage
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(statusCode));
    }
}