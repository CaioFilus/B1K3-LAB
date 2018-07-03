package com.npdeas.b1k3labapp.Route.Npdeas;

import android.content.Context;

import com.npdeas.b1k3labapp.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by NPDEAS on 03/05/2018.
 */

public class FileNames {

    public static String fileName(Context context) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour < 12) {
            return context.getString(R.string.morning_pedal);
        } else if ((hour > 12) && (hour < 18)) {

            return context.getString(R.string.afternoon_pedal);
        } else {
            return context.getString(R.string.evening_pedal);
        }
    }
}
