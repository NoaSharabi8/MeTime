package com.example.metime.Models;

import java.util.ArrayList;

public class TreatmentsList {
    private ArrayList<Treatment> list;
    public TreatmentsList() {
        list = new ArrayList<>();
    }

    public TreatmentsList addNewTreatment(Treatment newT) {
        int newId = list.size();
        newT.setId(""+newId);
        list.add(newT);
        return this;
    }

    public TreatmentsList addTreatment(Treatment newT) {
        list.add(Integer.parseInt(newT.getId()), newT);
        return this;
    }

    public ArrayList<Treatment> getList() {
        return list;
    }

    public void setList(ArrayList<Treatment> list) {
        ArrayList<Treatment> temp=new ArrayList<>();
        for(int i=0; i<list.size(); i++)
            temp.add(i,list.get(i));

        this.list=temp;
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
