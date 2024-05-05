package it.unimib.communimib.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import it.unimib.communimib.R;

public class DateFormatter {
    public static String format(long timestamp, Context context){
        long timeDifference = System.currentTimeMillis() - timestamp;
        Locale.getDefault().getDisplayLanguage();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
        long days = TimeUnit.MILLISECONDS.toDays(timeDifference);

        String minutesUnit = context.getString(R.string.minutes_unit);
        String hoursUnit = context.getString(R.string.hours_unit);
        String daysUnit = context.getString(R.string.days_unit);

        if(seconds < 60){
            return context.getString(R.string.just_now);
        }

        if(minutes >= 1 && minutes < 60){
            return minutes + " " + minutesUnit;
        }

        if(hours >= 1 && hours < 24){
            return hours + " " + hoursUnit;
        }

        if(days >= 1 && days < 7){
            return days + " " + daysUnit;
        }

        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(new Date(timestamp));
    }
}
