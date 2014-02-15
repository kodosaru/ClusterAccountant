/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.*;
import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author donj
 */
public class DateConvert {
/* 
 * Note: the DateConversion object contains the epoch begin date (calEpochBeginDate)
 * automatically adjusted for daylight savings time and the local time zone. It's
 * adjusted  relative to January 1, 1970, midnight for GMT.
 */
    public enum TimeOfDay {
        BEGINNING, NOW, ENDING;
    }
    
    public static void main(String[] args) {
        // This static function main() is only used for testing
        String strDate;
        String strEpochBegin = "";
        String strNow = "";
        Calendar calDate;
        Calendar calEpochBegin = Calendar.getInstance();
        Calendar calNow = Calendar.getInstance();
        Long longDate = 0L;
        Long longEpochBegin = 0L;
        Long longNow = 0L;
        
        try {
            // Initialize dates used for testing
            calEpochBegin = epochBegin();
            longDate = DateConvert.calToEpoch(calEpochBegin);
            strDate = DateConvert.calToStrLong(calEpochBegin);
            System.out.println("calEpochBegin: " + strDate);

            calNow = now();
            strDate = DateConvert.calToStrLong(calNow);
            System.out.println("calNow: " + strDate);

            strEpochBegin = DateConvert.calToStr(calEpochBegin);
            System.out.println("strEpochBegin: " + strEpochBegin);

            strNow = DateConvert.calToStr(calNow);
            System.out.println("strNow: " + strNow);

            longEpochBegin = DateConvert.calToEpoch(calEpochBegin);
            strDate = DateConvert.epochToStrLong(longEpochBegin);
            System.out.println("longEpochBegin: " + strDate);

            longNow = DateConvert.calToEpoch(calNow);
            strDate =  DateConvert.epochToStrLong(longNow);
            System.out.println("longNow: " + strDate);
            
            // Test Calendar to * Conversions on the epoch begin date
            strDate=DateConvert.calToStr(calEpochBegin);
            System.out.println("Epoch Begin Date CalToStr Check: " + strDate);
            
            strDate=DateConvert.calToStrLong(calEpochBegin);
            System.out.println("Epoch Begin Date CalToStrLong Check: " + strDate);

            longDate=DateConvert.calToEpoch(calEpochBegin);
            strDate=DateConvert.epochToStr(longDate);
            System.out.println("Epoch Begin Date CalToEpoch Check: " + strDate);
            
            // Test Calendar to * Conversions on now 
            strDate=DateConvert.calToStr(calNow);
            System.out.println("Now Date CalToStr Check: " + strDate);
            
            strDate=DateConvert.calToStrLong(calNow);
            System.out.println("Now Date CalToStrLong Check: " + strDate);

            longDate=DateConvert.calToEpoch(calNow);
            strDate=DateConvert.epochToStr(longDate);
            System.out.println("Now Date CalToEpoch Check: " + strDate);

            // Test Epoch to * Conversions on epoch begin date 
            strDate=DateConvert.epochToStr(longEpochBegin);
            System.out.println("Epoch begin EpochToStr Check: " + strDate);

            strDate=DateConvert.epochToStrLong(longEpochBegin);
            System.out.println("Epoch begin EpochToStrLong Check: " + strDate);

            calDate=DateConvert.epochToCal(longEpochBegin);
            System.out.println("Epoch begin EpochToCal Check: " + strDate);

            // Test Epoch to * Conversions on now 
            strDate=DateConvert.epochToStr(longNow);
            System.out.println("Now EpochToStr Check: " + strDate);

            strDate=DateConvert.epochToStrLong(longNow);
            System.out.println("Now EpochToStrLong Check: " + strDate);

            calDate=DateConvert.epochToCal(longNow);
            strDate=DateConvert.calToStrLong(calDate);
            System.out.println("Now EpochToCal Check: " + strDate);

            // Test String to * Conversions on now 
            // Can't test the string convert functions on the
            // epoch begin date because by using only YY/MM/DD
            // without HH:MM:SS, puts the input date before the
            // beginning of the epoch
            longDate=DateConvert.strToEpoch(strNow,
                    TimeOfDay.BEGINNING);
            strDate=DateConvert.epochToStrLong(longDate);
            System.out.println("Now strToEpoch Check: " + strDate);

            longDate=DateConvert.strToEpoch(strNow,
                    TimeOfDay.NOW);
            strDate=DateConvert.epochToStrLong(longDate);
            System.out.println("Now strToEpoch Check: " + strDate);

            longDate=DateConvert.strToEpoch(strNow,
                    TimeOfDay.ENDING);
            strDate=DateConvert.epochToStrLong(longDate);
            System.out.println("Now strToEpoch Check: " + strDate);

            calDate=DateConvert.strToCal(strNow,
                    TimeOfDay.BEGINNING);
            strDate=DateConvert.calToStrLong(calDate);
            System.out.println("Now strToCal Check: " + strDate);

            calDate=DateConvert.strToCal(strNow,
                    TimeOfDay.NOW);
            strDate=DateConvert.calToStrLong(calDate);
            System.out.println("Now strToCal Check: " + strDate);
            
            calDate=DateConvert.strToCal(strNow,
                    TimeOfDay.ENDING);
            strDate=DateConvert.calToStrLong(calDate);
            System.out.println("Now strToCal Check: " + strDate);

        } catch (DateException de) {
            System.err.println("Error: "+de.getMessage());
            return;
        }
    }

    public static Calendar epochBegin() {
        Calendar epochDate = Calendar.getInstance(); 
        epochDate.setLenient(false);
        epochDate.setTimeInMillis(0);
        return(epochDate);
    }

    public static Calendar now() {
        Calendar calDate = Calendar.getInstance(); 
        calDate.setLenient(false);
        return(calDate);
    }

    public static Calendar epochToCal(long epochDateIn) throws DateException {
        //Done
        // Convert Unix epoch date to Calendar date
        Calendar calDate = Calendar.getInstance();
        calDate.setTimeInMillis(epochDateIn);
        if (calDate.compareTo(epochBegin()) < 0) {
           throw new DateException("Date cannot be earlier the 1/1/1970 GMT");
        }
        return(calDate);
    }
   
    public static String epochToStr(long epochDateIn) throws DateException {
        //Done
        String strDate = "";
        // Convert Unix epoch date to string date
        strDate = calToStr(epochToCal(epochDateIn));
        return(strDate);
    }

    public static String epochToStrLong(long epochDateIn) throws DateException {
        //Done
        String strDate = "";
        Date dateIn = new Date(epochDateIn);
        strDate = DateFormat.getDateTimeInstance(DateFormat.LONG,
                DateFormat.LONG).format(dateIn);
        return(strDate);
    }
    
    public static Long calToEpoch(Calendar calDateIn) throws DateException {
        //Done
        // Convert Calendar date to Unix epoch date
        Long epochDate = 0L;
        try {
            int year = calDateIn.get(Calendar.YEAR);
            int month = calDateIn.get(Calendar.MONTH);
            int date = calDateIn.get(Calendar.DATE);
        } catch (IllegalArgumentException e) {
            throw new DateException(
                    "Invalid date: Problem with " + e.getMessage());
        }

        if (calDateIn.compareTo(epochBegin()) < 0) {
            throw new DateException("Date cannot be earlier the 1/1/1970 GMT");
        }
        epochDate = calDateIn.getTimeInMillis();
        return(epochDate);
    }
   
    public static String calToStr(Calendar calDateIn) throws DateException {
        //Done
        String strDate = "";
        int year = 0;
        int month = 0;
        int date = 0;
       
        calDateIn.setLenient(false);
        try {
            year = calDateIn.get(Calendar.YEAR);
            month = calDateIn.get(Calendar.MONTH);
            month += 1; // January is month 0 in Java
            date = calDateIn.get(Calendar.DATE);
        } catch (IllegalArgumentException e) {
            throw new DateException(
                    "Invalid date: Problem with " + e.getMessage());
        }
        if (calDateIn.compareTo(epochBegin()) < 0) {
            throw new DateException("Date cannot be earlier the 1/1/1970");
        }
        strDate = Integer.toString(month) + '/' + Integer.toString(date) + '/'
                + Integer.toString(year);
        return(strDate);
    }
   
    public static String calToStrLong(Calendar calDateIn) throws DateException {
        //Done
        String strDate = "";
        Date dateIn = new Date(calToEpoch(calDateIn));
        strDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(
                dateIn);
        return(strDate);
    }
    
    public static Calendar strToCal(String strDateIn, TimeOfDay tod) throws DateException {
        //Done
        Calendar calDate = Calendar.getInstance();
        int year = 0;
        int month = 0;
        int date = 0;
       
        // Determine separation character
        String splitarray[];
        Pattern slash = Pattern.compile("/");

        strDateIn = strDateIn.replace('-','/'); 
        if (strDateIn.indexOf('/') < strDateIn.length()) {
            splitarray = slash.split(strDateIn);
        } else {
            throw new DateException(
                    "Date must be of the form MM/DD/YYYY or MM-DD-YYYY");
        }
        if (splitarray.length < 3) {
            throw new DateException(
                    "Date must be of the form MM/DD/YYYY or MM-DD-YYYY");
        }

        // Validate year
        year = Integer.parseInt(splitarray[2]);
        if (year < 100) {
            year += 2000;
        }

        if (year < epochBegin().get(Calendar.YEAR)
                || year > now().get(Calendar.YEAR)) {
            throw new DateException(
                    "Year must be greater than or equal to 1970 "
                            + "and less than or equal to the current year");
        }
       
        // Validate month
        month = Integer.parseInt(splitarray[0]);
        if (month < 1 || month > 12) {
            throw new DateException("Month must be between 1 and 12");
        }
       
        // Validate date
        date = Integer.parseInt(splitarray[1]);
        Calendar cal = new GregorianCalendar(year, month - 1, 1);

        cal.setLenient(false);
        int maxdays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (date < 0 || date > maxdays) {
            throw new DateException(
                    "Date has to be valid for given month. "
                            + "e.g. You cannot use Janauary 32, 2011");
        }

        switch(tod)
        {
            case BEGINNING:
                calDate.set(year, month - 1, date, 0, 0, 0);
                break;
            case NOW:
                Calendar now  = now();
                calDate.set(year, month - 1, date, now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE), now.get(Calendar.SECOND));
                break;
            case ENDING:
                calDate.set(year, month - 1, date, 23, 59, 59);
                break;
        }
        return(calDate);
    }
   
    public static Long strToEpoch(String strDateIn,TimeOfDay tod) throws DateException {
        Long epochDate = 0L;
        epochDate = calToEpoch(strToCal(strDateIn, tod));
        return(epochDate);
    }

} // end of class
