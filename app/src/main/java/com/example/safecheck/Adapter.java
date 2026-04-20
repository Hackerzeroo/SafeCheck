package com.example.safecheck;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.CheckViewHolder> {

    private List<SafetyCheck> checks = new ArrayList<>();
    private Context context;
    private SafetyViewModel viewModel;

    public Adapter(Context context, SafetyViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }

    public void setChecks(List<SafetyCheck> checks) {
        this.checks = checks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_check, parent, false);
        return new CheckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckViewHolder holder, int position) {
        SafetyCheck check = checks.get(position);

        // Count defects on background thread, then update UI
        viewModel.getRepository().runOnBackground(() -> {
            int count = viewModel.getRepository().countDefectsForCheck(check.checkId);
            holder.itemView.post(() ->
                    holder.summary.setText(check.date + " - "
                            + check.vehicleRegistration + " - "
                            + count + " Defects"));
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("checkId", check.checkId);
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            viewModel.getRepository().deleteCheck(check);
        });
    }

    @Override
    public int getItemCount() {
        return checks.size();
    }

    static class CheckViewHolder extends RecyclerView.ViewHolder {
        TextView summary;
        Button deleteButton;

        public CheckViewHolder(@NonNull View itemView) {
            super(itemView);
            summary = itemView.findViewById(R.id.summaryText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
