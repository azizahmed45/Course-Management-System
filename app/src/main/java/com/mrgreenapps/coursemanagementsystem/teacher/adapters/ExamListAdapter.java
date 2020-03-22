package com.mrgreenapps.coursemanagementsystem.teacher.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.Exam;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.List;

public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.TutorialViewHolder> {

    private static final String TAG = "ExamListAdapter";

    private List<DocumentSnapshot> examSnapshotList;

    private OnItemClickListener listener;
    private String userType;
    private String examType;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExamListAdapter.TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_exam_item, parent, false);

        return new TutorialViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamListAdapter.TutorialViewHolder holder, int position) {
        Exam exam = examSnapshotList.get(position).toObject(Exam.class);


        if (exam != null && exam.getName() != null)
            holder.nameView.setText(exam.getName());
        else holder.nameView.setText("No Name");

        if (userType.equals(UserInfo.TYPE_STUDENT)) holder.publishButton.setVisibility(View.GONE);

        if (exam != null) {
            holder.publishButton.setChecked(!exam.isPublished());
        }

        holder.publishButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (examType.equals(Exam.TYPE_TUTORIAL))
                    DB.publishTutorialMarks(examSnapshotList.get(position).getId(), !isChecked);
                else if (examType.equals(Exam.TYPE_EXAM))
                    DB.publishExamMarks(examSnapshotList.get(position).getId(), !isChecked);
            }
        });

    }

    public ExamListAdapter(List<DocumentSnapshot> membersSnapshot, String userType, String examType) {
        this.examSnapshotList = membersSnapshot;
        this.userType = userType;
        this.examType = examType;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return examSnapshotList.size();
    }

    public void setExamSnapshotList(List<DocumentSnapshot> examSnapshotList) {
        this.examSnapshotList = examSnapshotList;
        notifyDataSetChanged();
    }

    public static class TutorialViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private ToggleButton publishButton;

        public TutorialViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            nameView = itemView.findViewById(R.id.name);
            publishButton = itemView.findViewById(R.id.publish_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

}