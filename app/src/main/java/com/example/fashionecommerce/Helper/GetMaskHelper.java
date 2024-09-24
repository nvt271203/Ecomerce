package com.example.fashionecommerce.Helper;

import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GetMaskHelper {
    public static String getDate(long dateOder, int type){
        Locale locale = new Locale("vi", "VN");
//        String timeZone = "VietNam/SaiGon";
        String timeZone = "Asia/Ho_Chi_Minh";;
        SimpleDateFormat daySDF = new SimpleDateFormat("dd", locale);
        daySDF.setTimeZone(TimeZone.getTimeZone(timeZone));

        SimpleDateFormat monthSDF = new SimpleDateFormat("MM", locale);
        monthSDF.setTimeZone(TimeZone.getTimeZone(timeZone));

        SimpleDateFormat yearSDF = new SimpleDateFormat("yyyy", locale);
        yearSDF.setTimeZone(TimeZone.getTimeZone(timeZone));

        SimpleDateFormat hourSDF = new SimpleDateFormat("HH", locale);
        hourSDF.setTimeZone(TimeZone.getTimeZone(timeZone));

        SimpleDateFormat minuteSDF = new SimpleDateFormat("mm", locale);
        minuteSDF.setTimeZone(TimeZone.getTimeZone(timeZone));

        DateFormat dateFormat = DateFormat.getDateInstance();
        Date netDate = new Date(dateOder);

        String day = daySDF.format(netDate);
        String month = monthSDF.format(netDate);
        String year = yearSDF.format(netDate);
        String hour = hourSDF.format(netDate);
        String minute = minuteSDF.format(netDate);

        String time = "";
        if (type == 1 ){
            time = day + "/" + month + "/" + year;
        }else if (type == 2){
            time = day + "/" + month + "/" + year + " - " + hour + ":" + minute;
        }
        return time;
    }
    public static String getValue(double value){
//        NumberFormat nf = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("vi", "VN")));
        NumberFormat nf = new DecimalFormat("#,000", new DecimalFormatSymbols(new Locale("vi", "VN")));
        return nf.format(value);
    }
}
