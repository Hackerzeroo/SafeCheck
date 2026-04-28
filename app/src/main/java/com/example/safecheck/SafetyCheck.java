package com.example.safecheck;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

//detailed table
@Entity(tableName = "safety_checks")
public class SafetyCheck {
    @PrimaryKey(autoGenerate = true)
    public int checkId;
    public String date;
    public String vehicleRegistration;
    public String driverName;
    public String overallStatus; // "Pass" or "Fail"
}