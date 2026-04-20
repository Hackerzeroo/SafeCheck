package com.example.safecheck;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class AddCheckActivity extends AppCompatActivity {

    private SafetyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check);

        viewModel = new ViewModelProvider(this).get(SafetyViewModel.class);

        EditText vehicleInput = findViewById(R.id.vehicleInput);
        EditText driverInput = findViewById(R.id.driverInput);
        EditText defectInput = findViewById(R.id.defectInput);
        Button saveButton = findViewById(R.id.saveButton);

        // Restore typed text after rotation
        vehicleInput.setText(viewModel.draftVehicleReg);
        driverInput.setText(viewModel.draftDriverName);
        defectInput.setText(viewModel.draftDefectDescription);

        // Save typed text into ViewModel as user types
        vehicleInput.addTextChangedListener(new SimpleWatcher(s -> viewModel.draftVehicleReg = s));
        driverInput.addTextChangedListener(new SimpleWatcher(s -> viewModel.draftDriverName = s));
        defectInput.addTextChangedListener(new SimpleWatcher(s -> viewModel.draftDefectDescription = s));

        saveButton.setOnClickListener(v -> {
            String vehicle = vehicleInput.getText().toString().trim();
            String driver = driverInput.getText().toString().trim();
            String defectDesc = defectInput.getText().toString().trim();

            // Input validation — no crash on empty form
            if (vehicle.isEmpty()) {
                Toast.makeText(this, "Please enter vehicle details",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            SafetyCheck check = new SafetyCheck();
            check.date = new java.text.SimpleDateFormat("dd/MM/yyyy")
                    .format(new java.util.Date());
            check.vehicleRegistration = vehicle;
            check.driverName = driver.isEmpty() ? "Unknown" : driver;
            check.overallStatus = defectDesc.isEmpty() ? "Pass" : "Fail";

            List<Defect> defects = new ArrayList<>();
            if (!defectDesc.isEmpty()) {
                Defect d = new Defect();
                d.description = defectDesc;
                d.severity = "High";
                defects.add(d);
            }

            viewModel.getRepository().insertCheckWithDefects(check, defects);

            // Clear drafts after successful save
            viewModel.draftVehicleReg = "";
            viewModel.draftDriverName = "";
            viewModel.draftDefectDescription = "";

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Simple text watcher to save typing into ViewModel
    private static class SimpleWatcher implements TextWatcher {
        interface Callback { void onText(String s); }
        private Callback callback;
        SimpleWatcher(Callback callback) { this.callback = callback; }
        @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
        @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
        @Override public void afterTextChanged(Editable s) { callback.onText(s.toString()); }
    }
}
