package com.mrgreenapps.coursemanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.AttendanceViewHolder> {
    private List<UserInfo> studentList = new ArrayList<>();
    private HashMap<String, Boolean> attendanceListMap = new HashMap<String, Boolean>();

    @NonNull
    @Override
    public AttendanceListAdapter.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_attendance, parent, false);

        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceListAdapter.AttendanceViewHolder holder, int position) {
        UserInfo student = studentList.get(position);

        if (student.getName() != null) {
            holder.nameView.setText(student.getName());
        } else {
            holder.nameView.setText("No Name");
        }

        if (student.getRegId() != null) {
            holder.idView.setText(student.getRegId());
        } else {
            holder.idView.setText("No Id");
        }

        holder.attendanceButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                attendanceListMap.put(student.getUid(), isChecked);
            }
        });

        if (attendanceListMap.get(student.getUid()) != null)
            holder.attendanceButton.setChecked(attendanceListMap.get(student.getUid()));
        else holder.attendanceButton.setChecked(false);

    }

    public AttendanceListAdapter() {

    }

    public void setList(List<UserInfo> studentList, HashMap<String, Boolean> attendanceList) {
        this.studentList = studentList;
        for (UserInfo userInfo : studentList) {
            if (attendanceList != null && attendanceList.get(userInfo.getUid()) != null)
                attendanceListMap.put(userInfo.getUid(), attendanceList.get(userInfo.getUid()));
            else attendanceListMap.put(userInfo.getUid(), false);
        }
        notifyDataSetChanged();
    }

    public HashMap<String, Boolean> getAttendanceList() {
        return attendanceListMap;
    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }


    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView idView;
        private ToggleButton attendanceButton;

        public AttendanceViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.name);
            idView = itemView.findViewById(R.id.id);

            attendanceButton = itemView.findViewById(R.id.attendance_button);
        }
    }

}