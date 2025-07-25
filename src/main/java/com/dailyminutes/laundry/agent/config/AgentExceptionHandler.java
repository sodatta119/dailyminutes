/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 19/07/25
 */
package com.dailyminutes.laundry.agent.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class AgentExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // or BAD_REQUEST depending on use case
                .body(Map.of("message", ex.getMessage()));
    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(Map.of("message", ex.getMessage()));
//    }
//
//    @ExceptionHandler(EntityUpdateException.class)
//    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityUpdateException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(Map.of("message", ex.getMessage()));
//    }
//
//    @ExceptionHandler(EntityCreationException.class)
//    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityCreationException ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(Map.of("message", ex.getMessage()));
//    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(ResponseStatusException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }
}
