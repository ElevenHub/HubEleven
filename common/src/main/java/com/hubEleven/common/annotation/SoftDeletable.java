package com.hubEleven.common.annotation;

import org.hibernate.annotations.SQLRestriction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SQLRestriction("deleted_at IS NULL")
public @interface SoftDeletable {}