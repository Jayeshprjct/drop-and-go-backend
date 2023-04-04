package com.dropandgo.backend.constants;

import java.util.Calendar;

public class AccountConstant {

    public static Long getTime() {
        return Calendar.getInstance().getTime().getTime();
    }
}
