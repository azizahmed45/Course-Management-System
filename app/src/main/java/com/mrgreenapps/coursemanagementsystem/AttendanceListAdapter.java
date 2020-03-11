package com.mrgreenapps.coursemanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.AttendanceViewHolder> {
    private List<UserInfo> studentList;
    private HashMap<String, Boolean> attendanceListMap = new HashMap<>();

    @NonNull
    @Override
    public AttendanceListAdapter.AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_attendance, parent, false);

        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceListAdapter.AttendanceViewHolder holder, int position) {
        UserInfo student = studentList.get(position);

        if(student.getName() != null){
            holder.nameView.setText(student.getName());
        } else {
            holder.nameView.setText("No Name");
        }

        holder.attendanceButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                attendanceListMap.put(student.getUid(), isChecked);
            }
        });

    }

    public AttendanceListAdapter(List<UserInfo> studentList) {
        this.studentList = studentList;
        for(UserInfo userInfo: studentList){
            attendanceListMap.put(userInfo.getUid(), false);
        }
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