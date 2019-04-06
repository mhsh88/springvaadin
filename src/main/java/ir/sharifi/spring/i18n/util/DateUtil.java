package ir.sharifi.spring.i18n.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class DateUtil {

    public static LocalDateTime addWorkingDay(LocalDateTime date, int workdays) {
        if (workdays < 1) {
            return date;
        }
        LocalDateTime result = date;

        for (int i=0; i<workdays; i++)
            do {
                result = result.plusDays(1);
            } while ( !isWorkingDay(result));
            if(isWorkingHour(result))
                return result;
            else
                return addWorkingMinutes(result,1);
    }
    public static LocalDateTime subtractWorkingDay(LocalDateTime date, int workdays) {
        if (workdays < 1) {
            return date;
        }
        LocalDateTime result = date;

        for (int i=0; i<workdays; i++)
            do {
                result = result.minusDays(1);
            } while ( !isWorkingDay(result));
        return result;
    }

    public static LocalDateTime addWorkingHour(LocalDateTime date, int hours) {
        if (hours < 1) {
            return date;
        }
        LocalDateTime result = date;

        for (int i=0; i<hours; i++)
            do {
                result = result.plusHours(1);
            } while ( !isWorkingHour(result));
        return result;
    }
    public static LocalDateTime subtractWorkingHour(LocalDateTime date, int hours){
        if (hours < 1) {
            return date;
        }
        LocalDateTime result = date;

        for (int i=0; i<hours; i++)
            do {
                result = result.minusHours(1);
            } while ( !isWorkingHour(result));
        return result;
    }
    public static LocalDateTime addWorkingMinutes(LocalDateTime date, int minutes) {
        if (minutes < 1) {
            return date;
        }
        LocalDateTime result = date;

        for (int i=0; i<minutes; i++)
            do {
                result = result.plusMinutes(1);
            } while ( !isWorkingHour(result));
        return result;
    }
    public static LocalDateTime subtractWorkingMinutes(LocalDateTime date, int minutes){
        if (minutes < 1) {
            return date;
        }
        LocalDateTime result = date;

        for (int i=0; i<minutes; i++)
            do {
                result = result.minusMinutes(1);
            } while ( !isWorkingHour(result));
        return result;
    }

    public static boolean isWorkingDay(LocalDateTime time) {
        int dayOfWeek = time.get(ChronoField.DAY_OF_WEEK);
        return dayOfWeek != 6 && dayOfWeek != 7;
    }

    public static boolean isWorkingHour(LocalDateTime time) {
        int hourOfDay = time.get(ChronoField.CLOCK_HOUR_OF_DAY);
        return hourOfDay >= 8 && hourOfDay <= 16;
    }


}
