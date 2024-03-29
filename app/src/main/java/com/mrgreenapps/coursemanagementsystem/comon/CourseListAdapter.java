package com.mrgreenapps.coursemanagementsystem.comon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.Course;
import com.mrgreenapps.coursemanagementsystem.model.UserInfo;

import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {
    private List<DocumentSnapshot> courseSnapshotList;

    private OnItemClickListener listener;

    private String userType;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CourseListAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_course_item, parent, false);

        return new CourseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListAdapter.CourseViewHolder holder, int position) {
        Course course = courseSnapshotList.get(position).toObject(Course.class);

        if (course.getName() != null)
            holder.courseNameView.setText(course.getName());
        else holder.courseNameView.setText("No Name");

        if (course.getCode() != null)
            holder.courseCodeView.setText(course.getCode());
        else holder.courseNameView.setText("No Code");

        if (course.getInviteCode() != null && userType.equals(UserInfo.TYPE_TEACHER))
            holder.invitationCodeView.setText(course.getInviteCode());
        else holder.invitationCodeView.setVisibility(View.GONE);

    }

    public CourseListAdapter(List<DocumentSnapshot> membersSnapshot, String userType) {
        this.courseSnapshotList = membersSnapshot;
        this.userType = userType;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return courseSnapshotList.size();
    }

    public void setCourseSnapshotList(List<DocumentSnapshot> courseSnapshotList) {
        this.courseSnapshotList = courseSnapshotList;
        notifyDataSetChanged();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {

        private TextView courseNameView;
        private TextView courseCodeView;
        private TextView invitationCodeView;

        public CourseViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            courseNameView = itemView.findViewById(R.id.course_name);
            courseCodeView = itemView.findViewById(R.id.course_code);
            invitationCodeView = itemView.findViewById(R.id.invitation);

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