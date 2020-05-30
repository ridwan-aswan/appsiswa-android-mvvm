package com.ridwanrsup94atgmaildotcom.appsiswa.helper;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by RIDWAN on 26/05/20.
 * ridwanrsup94@gmail.com
 */

public class Helper {

    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
    private String strDate = mdformat.format(calendar.getTime());

    public String getDate() {
        return strDate;
    }

    public String ConvertDate(String date, String outputFormats) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat(outputFormats);

        return timeFormat.format(myDate);

    }

    public String setDateParsing(String date) throws ParseException {

        // This is the format date we want
        DateFormat mSDF = new SimpleDateFormat("dd/MM/yyyy");

        // This format date is actually present
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return mSDF.format(formatter.parse(date));
    }
}
