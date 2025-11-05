package com.hubEleven.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.annotations.SQLRestriction;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SQLRestriction("deleted_at IS NULL")
public @interface SoftDeletable {}
