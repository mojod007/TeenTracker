package com.trace.product.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private boolean isAjaxRequest(WebRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }

    private ModelAndView createErrorView(HttpStatus status, String error, String message, WebRequest request) {
        ModelAndView mav = new ModelAndView("error");
        mav.setStatus(status);
        mav.addObject("timestamp", LocalDateTime.now());
        mav.addObject("status", status.value());
        mav.addObject("error", error);
        mav.addObject("message", message);
        mav.addObject("path", request.getDescription(false).replace("uri=", ""));
        return mav;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        if (isAjaxRequest(request)) {
            Map<String, Object> errors = new HashMap<>();
            Map<String, String> fieldErrors = new HashMap<>();

            ex.getBindingResult().getFieldErrors()
                    .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

            errors.put("timestamp", LocalDateTime.now());
            errors.put("status", HttpStatus.BAD_REQUEST.value());
            errors.put("error", "Validation Error");
            errors.put("message", "Erreur de validation des données");
            errors.put("path", request.getDescription(false).replace("uri=", ""));
            errors.put("fieldErrors", fieldErrors);

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        return createErrorView(HttpStatus.BAD_REQUEST, "Validation Error", "Erreur de validation des données", request);
    }

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException ex, WebRequest request) {
        if (isAjaxRequest(request)) {
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("status", HttpStatus.NOT_FOUND.value());
            error.put("error", "Not Found");
            error.put("message", ex.getMessage());
            error.put("path", request.getDescription(false).replace("uri=", ""));

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return createErrorView(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, WebRequest request) {
        if (isAjaxRequest(request)) {
            Map<String, Object> error = new HashMap<>();
            error.put("timestamp", LocalDateTime.now());
            error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            error.put("error", "Internal Server Error");
            error.put("message", "Une erreur interne s'est produite");
            error.put("path", request.getDescription(false).replace("uri=", ""));

            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return createErrorView(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "Une erreur interne s'est produite", request);
    }
}
