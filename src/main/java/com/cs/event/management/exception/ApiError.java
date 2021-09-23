package com.cs.event.management.exception;

import lombok.Data;

@Data
public class ApiError {
    private int code;
    private String message;
}
