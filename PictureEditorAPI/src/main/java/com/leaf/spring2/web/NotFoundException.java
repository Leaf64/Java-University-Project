package com.leaf.spring2.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This file does not exist in database")  // 404
public class NotFoundException extends RuntimeException {
    // ...
}