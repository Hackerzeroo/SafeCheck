package com.example.safecheck;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private SafetyCheck currentCheck;
    private List<Defect> currentDefects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        SafetyViewModel viewModel = new ViewModelProvider(this).get(SafetyViewModel.class);
        int checkId = getIntent().getIntExtra("checkId", -1);

        TextView detailsText = findViewById(R.id.detailsText);
        TextView defectsText = findViewById(R.id.defectsText);
        Button emailButton = findViewById(R.id.emailButton);

        //pulling check + defects off main so screen doesnt freeze
        viewModel.getRepository().runOnBackground(() -> {
            currentCheck = viewModel.getRepository().getCheckById(checkId);
            currentDefects = viewModel.getRepository().getDefectsForCheck(checkId);

            runOnUiThread(() -> {
                if (currentCheck != null) {
                    detailsText.setText("Date: " + currentCheck.date
                            + "\nVehicle: " + currentCheck.vehicleRegistration
                            + "\nDriver: " + currentCheck.driverName
                            + "\nStatus: " + currentCheck.overallStatus);

                    StringBuilder sb = new StringBuilder("Defects:\n");
                    for (Defect d : currentDefects) {
                        sb.append("- ").append(d.description)
                                .append(" (").append(d.severity).append(")\n");
                    }
                    defectsText.setText(sb.toString());
                }
            });
        });

        emailButton.setOnClickListener(v -> {
            if (currentCheck == null) return;

            //just dumping defects into the body
            StringBuilder body = new StringBuilder();
            for (Defect d : currentDefects) {
                body.append("- ").append(d.description).append("\n");
            }

            // opens whatever email app the user has installed
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Safety Defect Report: " + currentCheck.vehicleRegistration);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body.toString());
            startActivity(emailIntent);
        });
    }
}