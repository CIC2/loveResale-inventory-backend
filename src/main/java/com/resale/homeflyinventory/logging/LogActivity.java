package com.resale.homeflyinventory.logging;

import com.resale.homeflyinventory.models.ActionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogActivity {
    ActionType value();
}


