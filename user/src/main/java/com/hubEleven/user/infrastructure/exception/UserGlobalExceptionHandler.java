package com.hubEleven.user.infrastructure.exception;

import com.commonLib.common.exception.GlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice(basePackages = "com.hubEleven.user.presentation.controller")
public class UserGlobalExceptionHandler extends GlobalExceptionHandler {}
