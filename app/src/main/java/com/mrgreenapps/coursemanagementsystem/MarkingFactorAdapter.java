package com.mrgreenapps.coursemanagementsystem;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrgreenapps.coursemanagementsystem.model.MarkingFactor;
import com.mrgreenapps.coursemanagementsystem.teacher.adapters.AttendanceListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MarkingFactorAdapter extends RecyclerView.Adapter<MarkingFactorAdapter.MarkingFactorViewHolder> {

    List<MarkingFactor> markingFactorList;

    public MarkingFactorAdapter() {
        markingFactorList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MarkingFactorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_marking_factor_item, parent, false);

        return new MarkingFactorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarkingFactorViewHolder holder, int position) {
        holder.nameView.setText(markingFactorList.get(position).getName());

        holder.factorForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                    markingFactorList.get(position).setPercentage(Float.parseFloat(s.toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return markingFactorList.size();
    }

    public void setMarkingFactorList(List<MarkingFactor> markingFactorList) {
        this.markingFactorList = markingFactorList;
        notifyDataSetChanged();
    }

    public List<MarkingFactor> getMarkingFactorList() {
        return markingFactorList;
    }

    public static class MarkingFactorViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        EditText factorForm;

        public MarkingFactorViewHolder(@NonNull View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.name);
            factorForm = itemView.findViewById(R.id.factor_field);
        }
    }
}
