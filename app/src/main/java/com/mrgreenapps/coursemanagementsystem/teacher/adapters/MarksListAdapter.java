package com.mrgreenapps.coursemanagementsystem.teacher.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarksListAdapter extends RecyclerView.Adapter<MarksListAdapter.MarksViewHolder> {
    private List<UserInfo> studentList = new ArrayList<>();
    private HashMap<String, Float> marksListMap = new HashMap<String, Float>();
    private double totalMark;
    private String userType;
    private Context context;

    @NonNull
    @Override
    public MarksListAdapter.MarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_mark_item, parent, false);

        return new MarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksListAdapter.MarksViewHolder holder, int position) {
        UserInfo student = studentList.get(position);

        if(student.getUid().equals(FirebaseAuth.getInstance().getUid())){
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.lite_green));
        }

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

        holder.markField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                    marksListMap.put(student.getUid(), Float.parseFloat(s.toString()));
                if (!s.toString().isEmpty() && Double.parseDouble(s.toString()) > totalMark) {
                    holder.markField.setText(String.valueOf(totalMark));
                    holder.markField.setError("Mark should not greater then total mark");
                }
            }
        });


        DecimalFormat formatter = new DecimalFormat("#.##");

        if (marksListMap.get(student.getUid()) != null) {
            holder.markField.setText(String.valueOf(formatter.format(marksListMap.get(student.getUid()))));
            holder.markView.setText(String.valueOf(formatter.format(marksListMap.get(student.getUid()))));
        } else {
            holder.markField.setText("0.00");
            holder.markView.setText("0.00");
        }

        if (userType.equals(UserInfo.TYPE_TEACHER)) {
            holder.markField.setVisibility(View.VISIBLE);
            holder.markView.setVisibility(View.GONE);
        } else if (userType.equals(UserInfo.TYPE_STUDENT)) {
            holder.markField.setVisibility(View.GONE);
            holder.markView.setVisibility(View.VISIBLE);
        }

    }

    public MarksListAdapter(String userType) {
        this.userType = userType;
    }

    public void setList(List<UserInfo> studentList, HashMap<String, Float> marksList) {
        this.studentList = studentList;
        for (UserInfo userInfo : studentList) {
            if (marksList != null && marksList.get(userInfo.getUid()) != null)
                marksListMap.put(userInfo.getUid(), marksList.get(userInfo.getUid()));
            else marksListMap.put(userInfo.getUid(), 0.00F);
        }
        notifyDataSetChanged();
    }

    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
    }

    public HashMap<String, Float> getMarksList() {
        return marksListMap;
    }


    @Override
    public int getItemCount() {
        return studentList.size();
    }


    public static class MarksViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView idView;
        private TextView markView;
        private EditText markField;
        private View itemView;

        public MarksViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            nameView = itemView.findViewById(R.id.name);
            idView = itemView.findViewById(R.id.id);
            markView = itemView.findViewById(R.id.mark);

            markField = itemView.findViewById(R.id.mark_field);
        }
    }

}