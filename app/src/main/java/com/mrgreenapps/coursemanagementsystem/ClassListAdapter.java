package com.mrgreenapps.coursemanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;

import java.text.SimpleDateFormat;
import java.util.List;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ClassViewHolder> {
    private List<DocumentSnapshot> classSnapshotList;

    private OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ClassListAdapter.ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_class, parent, false);

        return new ClassViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassListAdapter.ClassViewHolder holder, int position) {
        CourseClass lCourseClass = classSnapshotList.get(position).toObject(CourseClass.class);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        if (lCourseClass != null)
            holder.dateView.setText(dateFormat.format(lCourseClass.getDate()));
        else holder.dateView.setText("No Date");

    }

    public ClassListAdapter(List<DocumentSnapshot> membersSnapshot) {
        this.classSnapshotList = membersSnapshot;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return classSnapshotList.size();
    }

    public void setClassSnapshotList(List<DocumentSnapshot> classSnapshotList) {
        this.classSnapshotList = classSnapshotList;
        notifyDataSetChanged();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {

        private TextView dateView;

        public ClassViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            dateView = itemView.findViewById(R.id.date);

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