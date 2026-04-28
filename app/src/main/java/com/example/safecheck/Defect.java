package com.example.safecheck;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

//same as Safetychecks too allow for a comprehensive list
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
}
