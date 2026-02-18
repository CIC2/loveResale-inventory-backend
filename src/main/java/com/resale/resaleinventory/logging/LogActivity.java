package com.resale.resaleinventory.logging;

import com.resale.resaleinventory.models.ActionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogActivity {
    ActionType value();
}


