package com.example.metime.Models;
public class Appointment {
    private String id;
    private String userId;
    private String treatmentId;
    private String date;
    private String beginningTime;
    private String endTime;

    public Appointment(String userId, String treatmentId, String date, String beginningTime, String endTime) {
        this.userId = userId;
        this.treatmentId = treatmentId;
        this.date = date;
        this.beginningTime = beginningTime;
        this.endTime = endTime;
    }
    public Appointment() { }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDate() {
        return date;
    }
    public String getBeginningTime() {
        return beginningTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setBeginningTime(String beginningTime) {
        this.beginningTime = beginningTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", treatmentId='" + treatmentId + '\'' +
                ", date='" + date + '\'' +
                ", beginningTime='" + beginningTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
