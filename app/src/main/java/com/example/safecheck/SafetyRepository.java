package com.example.safecheck;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SafetyRepository {

    // storing in this repo so everything db related lives in one spot
    private SafetyCheckDao dao;
    private ExecutorService executor;

    public SafetyRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.safetyCheckDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insertDefect(Defect defect) {
        executor.execute(() -> dao.insertDefect(defect));
    }

    // saves check first then loops defects so they get the right checkId
    public void insertCheckWithDefects(SafetyCheck check, List<Defect> defects) {
        executor.execute(() -> {
            long newId = dao.insertCheck(check);
            for (Defect d : defects) {
                d.checkId = (int) newId;
                dao.insertDefect(d);
            }
        });
    }

    public void deleteCheck(SafetyCheck check) {
        executor.execute(() -> dao.deleteCheck(check));
    }

    public LiveData<List<SafetyCheck>> getAllChecks() {
        return dao.getAllChecks();
    }

    //dont call this on main!!
    public List<Defect> getDefectsForCheck(int checkId) {
        return dao.getDefectsForCheck(checkId);
    }

    public SafetyCheck getCheckById(int id) {
        return dao.getCheckById(id);
    }

    public int countDefectsForCheck(int checkId) {
        return dao.countDefectsForCheck(checkId);
    }

    public void runOnBackground(Runnable task) {
        executor.execute(task);
    }
}
