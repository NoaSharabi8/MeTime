package com.example.metime.Utilities;

import static com.example.metime.Utilities.DataBaseManager.findTreatmentById;

import android.content.Context;

import java.util.ArrayList;

public class TimeManager {

    private static TimeManager timeManager;
    private static Context context;
    private static ArrayList<String> activityHours = new ArrayList<>();
    private static ArrayList<String> duration = new ArrayList<>();
    private TimeManager(Context context) {
        this.context = context;
    }
    public static void init(Context context) {
        if (timeManager == null){
            synchronized (TimeManager.class){
                if (timeManager == null){
                    timeManager = new TimeManager(context.getApplicationContext());
                    initData();
                }
            }
        }
    }
    public static void initData() {
        activityHours.add(0,"8:00");
        activityHours.add(1,"8:30");
        activityHours.add(2,"9:00");
        activityHours.add(3,"9:30");
        activityHours.add(4,"10:00");
        activityHours.add(5,"10:30");
        activityHours.add(6,"11:00");
        activityHours.add(7,"11:30");
        activityHours.add(8,"12:00");
        activityHours.add(9,"12:30");
        activityHours.add(10,"13:00");
        activityHours.add(11,"13:30");
        activityHours.add(12,"14:00");
        activityHours.add(13,"14:30");
        activityHours.add(14,"15:00");
        activityHours.add(15,"15:30");
        activityHours.add(16,"16:00");
        activityHours.add(17,"16:30");
        activityHours.add(18,"17:00");
        activityHours.add(19,"17:30");
        activityHours.add(20,"18:00");
        activityHours.add(21,"18:30");
        activityHours.add(22,"19:00");
        activityHours.add(23,"19:30");

        duration.add(0,"0:00");
        duration.add(1,"30 minutes");
        duration.add(2,"1 hour");
        duration.add(3,"1 hour 30 minutes");
        duration.add(4,"2 hours");
        duration.add(5,"2 hours 30 minutes");
        duration.add(6,"3 hours");
        duration.add(7,"3 hours 30 minutes");
        duration.add(8,"4 hours");
        duration.add(9,"4 hours 30 minutes");
        duration.add(10,"5 hours");
        duration.add(11,"5 hours 30 minutes");
        duration.add(12,"6 hours");
        duration.add(13,"6 hours 30 minutes");
        duration.add(14,"7 hours");
        duration.add(15,"7 hours 30 minutes");

    }

    public static TimeManager getInstance() {
        return timeManager;
    }
    public static ArrayList<String> findOptionalHoursForTreatment(String availabilityHours, String tId) {
        int duration = findTreatmentById(tId).getDuration();
        ArrayList<String> temp = new ArrayList<>();
        if(availabilityHours!=null) {
            for(int i=0; i<availabilityHours.length(); i++) {
                if(availabilityHours.charAt(i)=='1') {
                    if(checkAvailabilitySequence(availabilityHours,i,duration))
                        temp.add(activityHours.get(i));
                }
            }
        }
        return temp;
    }

    private static boolean checkAvailabilitySequence(String availabilityHours, int StartIndex, int duration) {
        for(int j=StartIndex; j<StartIndex+duration; j++) {
            if(j==availabilityHours.length())
                return false;
            if(availabilityHours.charAt(j)=='0')
                return false;
        }
        return true;
    }

    public static String calculateEndTime(int duration, String beginningTime) {
        int index = getIndexOfSpecificHour(beginningTime);
        if(index+duration!=24)
            return activityHours.get(index+duration);
        return "20:00";
    }
    public static int getIndexOfSpecificHour(String hour) {
        int i=0;
        for(i=0; i<activityHours.size(); i++) {
            if(activityHours.get(i).equals(hour))
                break;
        }
        return i;
    }
    public static String calculateNewAvailabilityHours(String oldVal, String chosenHour, int duration) {
        int index = getIndexOfSpecificHour(chosenHour);
        char[] temp = oldVal.toCharArray();
        for(int i=index; i<index+duration; i++)
               temp[i]='0';
        return new String(temp);
    }
    public static String convertDurationToHours(int index) {
        return duration.get(index);
    }

    public static String returnHoursToAvailability(String tId, String beginningTime, String oldVal) {
        int duration = DataBaseManager.findTreatmentById(tId).getDuration();
        char[] temp = oldVal.toCharArray();
        int start = getIndexOfSpecificHour(beginningTime);
        int end= start+duration;
        for(int i=start; i<end; i++) {
            temp[i]='1';
        }
        return new String(temp);
    }
}
