package com.example.safecheck;

import android.app.Application;

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

    // Reads — call on background thread from Activity
    public List<SafetyCheck> getAllChecks() {
        return dao.getAllChecks();
    }

    public List<Defect> getDefectsForCheck(int checkId) {
        return dao.getDefectsForCheck(checkId);
    }
}
