package com.example.conference.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FakeCalendar {
    public final static LocalDate LOCAL_DATE = LocalDate.of(1989, 10, 13);
    public final static LocalDateTime LOCAL_DATE_TIME = FakeCalendar.LOCAL_DATE.atStartOfDay();
}
