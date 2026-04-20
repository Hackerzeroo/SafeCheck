package com.example.safecheck;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "defects",
        foreignKeys = @ForeignKey(
                entity = SafetyCheck.class,
                parentColumns = "checkId",
                childColumns = "checkId",
                onDelete = ForeignKey.CASCADE
        ))
public class Defect {
    @PrimaryKey(autoGenerate = true)
    public int defectId;
    public int checkId; // links to SafetyCheck
    public String description;
    public String severity; // "Low", "Medium" "High"
    public String category;       // "Tyres", "Lights", "Brakes"
    public String actionRequired; // "Replace part", "Change oil"
    public boolean isRepaired;    // Has it been fixed yet?
    public String repairedDate;   // When it was fixed

}
