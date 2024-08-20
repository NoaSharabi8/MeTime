package com.example.metime.Models;

import java.util.ArrayList;

public class AppointmentsList {

    private ArrayList<Appointment> list = new ArrayList<>();
    public AppointmentsList() {
    }
    public ArrayList<Appointment> getList() {
        return list;
    }

    public void setList(ArrayList<Appointment> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (int i=0;i<list.size();i++) {
            s.append(list.get(i).toString());
        }
        return s.toString();
    }
}
