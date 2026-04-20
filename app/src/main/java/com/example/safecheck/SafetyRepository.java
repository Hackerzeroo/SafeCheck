package com.example.safecheck;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class SafetyRepository {

    private SafetyCheckDao dao;
    private ExecutorService executor;

    public SafetyRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        dao = db.safetyCheckDao();
        executor = Executors.newFixedThreadPool(2);
    }

    // Writes run on background thread
    public void insertCheck(SafetyCheck check,
                            Consumer<Long> onDone) {
        executor.execute(() -> {
            long newId = dao.insertCheck(check);
            if (onDone != null) onDone.accept(newId);
        });
    }

    public void insertDefect(Defect defect) {
        executor.execute(() -> dao.insertDefect(defect));
    }

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

    // LiveData reads — Room returns immediately, query runs on background thread
    public LiveData<List<SafetyCheck>> getAllChecks() {
        return dao.getAllChecks();
    }

    // Reads — call on background thread from Activity
    public List<Defect> getDefectsForCheck(int checkId) {
        return dao.getDefectsForCheck(checkId);
    }

    // Synchronous reads — only call from a background thread
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
