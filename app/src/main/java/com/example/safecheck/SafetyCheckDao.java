package com.example.safecheck;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SafetyCheckDao {

    // Insert a full inspection
    @Insert
    long insertCheck(SafetyCheck check);

    // Insert a single defect linked to a check
    @Insert
    void insertDefect(Defect defect);

    @Delete
    void deleteCheck(SafetyCheck check);

    // LiveData auto-updates the UI when data changes; runs off main thread automatically
    // Get all inspections
    //newest first
    @Query("SELECT * FROM safety_checks ORDER BY checkId DESC")
    LiveData<List<SafetyCheck>> getAllChecks();

    // Get one specific inspection by its ID
    @Query("SELECT * FROM safety_checks WHERE checkId = :id")
    SafetyCheck getCheckById(int id);

    // Get all defects belonging to one inspection
    //used when you tap into a check
    @Query("SELECT * FROM defects WHERE checkId = :checkId")
    List<Defect> getDefectsForCheck(int checkId);

    @Query("SELECT COUNT(*) FROM defects WHERE checkId = :checkId")
    int countDefectsForCheck(int checkId);
}
