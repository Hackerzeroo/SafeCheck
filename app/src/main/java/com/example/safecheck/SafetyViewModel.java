package com.example.safecheck;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class SafetyViewModel extends AndroidViewModel {

    private SafetyRepository repository;
    private LiveData<List<SafetyCheck>> allChecks;

    //keeping typed text here so rotation doesnt wipe it
    public String draftVehicleReg = "";
    public String draftDriverName = "";
    public String draftDefectDescription = "";

    public SafetyViewModel(@NonNull Application application) {
        super(application);
        repository = new SafetyRepository(application);
        allChecks = repository.getAllChecks();
    }

    public LiveData<List<SafetyCheck>> getAllChecks() {
        return allChecks;
    }

    public SafetyRepository getRepository() {
        return repository;
    }
}