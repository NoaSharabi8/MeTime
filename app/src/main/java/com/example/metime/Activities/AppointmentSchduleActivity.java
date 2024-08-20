package com.example.metime.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metime.Fragments.AvailableHoursFragment;
import com.example.metime.Interfaces.CallBack_HourClicked;
import com.example.metime.Models.Treatment;
import com.example.metime.R;
import com.example.metime.Utilities.CalendarAdapter;
import com.example.metime.Utilities.DataBaseManager;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class AppointmentSchduleActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    private MaterialButton BTN_next;

    private static LocalDate selectedDate;
    private static String tId;
    private String chosenDate;
    private static String chosenDay;
    private AvailableHoursFragment listFragment;
    private static String chosenHour;
    private Treatment myT;

    CallBack_HourClicked callBack_SendClick = new CallBack_HourClicked() {
        public void userClicked(String hour) {
            setChosenHour(hour);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_schedule);
        findViews();
        initViews();
        initFragments();
        selectedDate = LocalDate.now();
        setMonthView();
    }

    private void initViews() {
        BTN_next.setOnClickListener(v->moveToAppointmentConfirmationPage());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnToSelectionPage();
            }
        });
    }

    protected void onStart() {
        Intent prev = getIntent();
        tId=prev.getExtras().getString("SERVICE_ID");
        myT = DataBaseManager.findTreatmentById(tId);
        super.onStart();
        DataBaseManager.setCurrentAvailableHours("0");
        beginTransactions();
    }
    private void returnToSelectionPage() {
        Intent i = new Intent(this, ServiceSelectionActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        onStop();
    }
    private void moveToAppointmentConfirmationPage() {
        if(chosenHour!=null && chosenDate!=null) {
            saveAppointment();
            Intent i = new Intent(this, AppointmentConfirmationActivity.class);
            Bundle bundle = new Bundle();
            LocalDate temp = selectedDate.withDayOfMonth(Integer.parseInt(chosenDay));
            bundle.putString("DAY",temp.getDayOfWeek().toString().toLowerCase());
            bundle.putString("HOUR",chosenHour);
            bundle.putString("DATE", dayMonthYearFromDateV2(selectedDate, chosenDay));
            bundle.putString("SERVICE_ID", tId);

            i.putExtras(bundle);
            startActivity(i);
            onStop();
        }
    }
    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.appointment_schedule_LISTVIEW_time_list, listFragment).commit();
    }
    private void saveAppointment() {
        DataBaseManager.addAppointmentToDB(tId, chosenDate, chosenHour);
        int year = selectedDate.getYear();
        int month = selectedDate.getMonthValue();
        int day = Integer.parseInt(chosenDay);
        DataBaseManager.updateSchedule(year,month,day, chosenHour, myT.getDuration());
    }
    private void findViews()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        BTN_next= findViewById(R.id.BTN_next);
    }
    private void initFragments() {
        listFragment = new AvailableHoursFragment();
        listFragment.setCallBack(callBack_SendClick);
    }

    public static int getMonth() {
        return selectedDate.getMonthValue();
    }
    public static int getYear() {
        return selectedDate.getYear();
    }


    private void setMonthView()
    {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setChosenDate(null);
        setChosenHour(null);
        listFragment.showAvailableHours(0,0,0);

    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        // Get the first day of the month and its day of the week
        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        // Adjust dayOfWeek if your calendar starts on Sunday
        // If Sunday is the first day of the week in your calendar, set dayOfWeek to 0
        if (dayOfWeek == 7) {
            dayOfWeek = 0; // Convert Sunday to 0 if necessary
        }

        // Fill in empty spaces for days before the start of the month
        for (int i = 0; i < dayOfWeek; i++) {
            daysInMonthArray.add("");
        }

        // Add days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            daysInMonthArray.add(String.valueOf(day));
        }

        // Fill in empty spaces after the end of the month to make the grid 42 days long
        int totalDaysInGrid = 42; // 6 weeks * 7 days
        int daysAfterMonth = totalDaysInGrid - daysInMonthArray.size();
        for (int i = 0; i < daysAfterMonth; i++) {
            daysInMonthArray.add("");
        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH);
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        LocalDate temp=LocalDate.now();
        int currentMonth = temp.getMonthValue();
        int currentYear = temp.getYear();
        if(currentMonth==selectedDate.getMonthValue() && currentYear==selectedDate.getYear()) {
        } else {
            selectedDate = selectedDate.minusMonths(1);
            setMonthView();
        }

    }

    public void nextMonthAction(View view)
    {
        LocalDate temp = selectedDate.plusMonths(1);
        DataBaseManager.checkIfMonthAvailable(String.valueOf(temp.getMonthValue()),String.valueOf(temp.getYear()), new DataBaseManager.CallBack<Boolean>() {
            @Override
            public void res(Boolean res) {
                if(res) {
                    selectedDate = selectedDate.plusMonths(1);
                    setMonthView();
                }

            }
        });

    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            int month = selectedDate.getMonthValue();
            int year = selectedDate.getYear();
            chosenDay = dayText;
            chosenDate = dayText + "/"+ month+ "/"+ year ;
            setChosenHour(null);
            listFragment.showAvailableHours(Integer.parseInt(dayText),month,year);
        }



    }
    private String dayMonthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private String dayMonthYearFromDateV2(LocalDate date, String day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        String res=day+"/"+date.format(formatter);
        return res;
    }
    public static String gettId() {
        return tId;
    }

    public static void setChosenHour(String chosenHour) {
        AppointmentSchduleActivity.chosenHour = chosenHour;
    }

    public void setChosenDate(String chosenDate) {
        this.chosenDate = chosenDate;
    }
}
