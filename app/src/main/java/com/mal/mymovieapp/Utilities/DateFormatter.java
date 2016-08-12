package com.mal.mymovieapp.Utilities;

public class DateFormatter {

    public static String format(String date){
        String[] months = {
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec",
        };
        String[] days = {
                "th",
                "st",
                "nd",
                "rd",
                "th",
                "th",
                "th",
                "th",
                "th",
                "th"
        };
        String[] _date = date.split("-");
        String lastChar = date.substring(date.length() - 1, date.length());
        return String.format("%s %s%s, %s",
                months[Integer.parseInt(_date[1]) - 1],
                _date[2],
                days[Integer.parseInt(lastChar)],
                _date[0]
                );
    }
}
