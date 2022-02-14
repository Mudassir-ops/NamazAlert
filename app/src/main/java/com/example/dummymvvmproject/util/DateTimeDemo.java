package com.example.dummymvvmproject.util;

import static java.lang.String.format;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormat;

public class DateTimeDemo {



    public static void main(String[] args) {

        // Your date/time values, I'll assume you missed a digit off the time ;)
        String eventDate = "3/5/2015";
        String eventTime = "13:20";

        // convert these to a DateTime object
        DateTime targetDateTime = DateTime.parse(format("%s %s", eventDate, eventTime), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));

        // print out the millis, or in your case, save it to DB
        System.out.println("targetDateTime in millis is " + targetDateTime.getMillis());

        // grab a timestamp
        DateTime now = DateTime.now();

        // print it out, just for demo
        System.out.println("millis for now is " + now.getMillis());

        // create a period object between the two
        Period period = new Period(now, targetDateTime);

        // print out each part
        System.out.println("seconds " + period.getSeconds());
        System.out.println("hours " + period.getHours());
        System.out.println("months " + period.getMonths());

        // convert the period to a printable String
        String prettyPeriod = PeriodFormat.getDefault().print(period);

        // write it out!
        System.out.println(prettyPeriod);


    }
}