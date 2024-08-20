package com.example.metime.Utilities;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.metime.Models.Appointment;
import com.example.metime.Models.Treatment;
import com.example.metime.Models.TreatmentsList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class DataBaseManager {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final String USERS_LIST = "users";
    public static final String TREATMENTS_LIST = "treatment_list";
    public static final String AVAILABILITY_LIST = "availability";
    public static String USER_APPOINTMENTS = "my_appointments";
    private static final DatabaseReference usersRef = database.getReference(USERS_LIST);
    private static DatabaseReference scheduleRef = database.getReference(AVAILABILITY_LIST);
    private static DatabaseReference treatmentListRef = database.getReference(TREATMENTS_LIST);
    private static TreatmentsList treatmentsList = new TreatmentsList();
    private static ArrayList<Appointment> appointmentList = new ArrayList<>();
    private static String currentAvailableHours =null;
    private static DataBaseManager dataManager;
    private static Context context;

    private DataBaseManager(Context context) {
        this.context = context;
    }
    public static DataBaseManager getInstance() {
        return dataManager;
    }
    public static void init(Context context) {
        if (dataManager == null){
            synchronized (DataBaseManager.class) {
                if (dataManager == null){
                    dataManager = new DataBaseManager(context.getApplicationContext());
                }
            }
        }
    }

    public static ArrayList<Appointment> getMyAppointments() {
        return appointmentList;
    }

    public static void loadAppointmentsDataFromDB(CallBack<List> callBack) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef.child(userUid).child(USER_APPOINTMENTS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Appointment> temp = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Appointment a = userSnapshot.getValue(Appointment.class);
                    temp.add(a);
                }
                appointmentList = new ArrayList<>();
                for(int i=0; i<temp.size(); i++)
                    appointmentList.add(temp.get(i));

                if (temp != null) {
                    callBack.res(temp);
                } else {
                    callBack.res(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void removeAppointmentFromDB(Appointment chosenAppointment) {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef.child(userUid).child(USER_APPOINTMENTS).child(chosenAppointment.getId()).removeValue();
    }

    public static void returnAvailabilityHours(String day, String month, String year, String newValue) {
        scheduleRef.child(year).child(month).child(day).setValue(newValue);

    }

    public interface CallBack<T> {
        void res(T res);
    }

    public static void getAvailableHours(String day, String month, String year, CallBack<String> callBack) {
        scheduleRef.child(year).child(month).child(day).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String daySchedule = snapshot.getValue(String.class);
                currentAvailableHours =daySchedule;
                if (daySchedule != null) {
                    callBack.res(daySchedule);
                } else {
                    callBack.res(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void checkIfMonthAvailable(String month, String year, CallBack<Boolean> callBack) {
        scheduleRef.child(year).child(month).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callBack.res(true);
                } else {
                    callBack.res(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

            public static void loadTreatmentsDataFromDB(CallBack<List> callBack) {
        treatmentListRef.child("list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Treatment> temp = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Treatment t = userSnapshot.getValue(Treatment.class);
                    temp.add(t);
                }
                for(int i=0; i<temp.size(); i++)
                    treatmentsList.addTreatment(temp.get(i));

                if (temp != null) {
                    callBack.res(temp);
               } else {
                    callBack.res(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static void addAppointmentToDB(String treatmentId, String chosenDate, String beginningTime) {
        Treatment myT= findTreatmentById(treatmentId);
        String endTime = TimeManager.calculateEndTime(myT.getDuration(), beginningTime);
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Appointment newA = new Appointment(userUid, treatmentId, chosenDate, beginningTime, endTime);
        chosenDate=chosenDate.replace('/','|');
        newA.setId(userUid+"|"+chosenDate+"|"+beginningTime);

        DatabaseReference aRef=usersRef.child(userUid).child(USER_APPOINTMENTS).child(newA.getId());
        aRef.setValue(newA);

    }
    public static Treatment findTreatmentById(String tId) {
        List<Treatment> temp = treatmentsList.getList();
        int i;
        for(i=0; i<temp.size(); i++) {
            if(temp.get(i).getId().equals(tId))
                break;
        }
        return temp.get(i);
    }

    public static void updateSchedule(int year, int month, int day, String chosenHour, int duration) {
        String newVal = TimeManager.calculateNewAvailabilityHours(currentAvailableHours, chosenHour, duration);
        scheduleRef.child(""+year).child(""+month).child(""+day).setValue(newVal);
    }

    public static TreatmentsList getTreatmentsList() {
        return treatmentsList;
    }

    public static void setCurrentAvailableHours(String currentAvailableHours) {
        DataBaseManager.currentAvailableHours = currentAvailableHours;
    }
    public static String getCurrentAvailableHours() {
        return currentAvailableHours;
    }

    public static void setTreatmentsList(TreatmentsList treatmentsList) {
        DataBaseManager.treatmentsList = treatmentsList;
    }
}




