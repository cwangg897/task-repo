package com.task.controller.response;

import com.task.ErrorType;

public record ErrorResponse(String errorMessage, ErrorType errorType) {

}
