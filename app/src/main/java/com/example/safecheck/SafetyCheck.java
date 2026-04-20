package com.example.safecheck;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "safety_checks")
public class SafetyCheck {
    @PrimaryKey(autoGenerate = true)
    public int checkId;
    public String date;
    public String location;
    public String vehicleRegistration;
    public String vehicleType;
    public String driverName;
    public String overallStatus; // "Pass" or "Fail"
}