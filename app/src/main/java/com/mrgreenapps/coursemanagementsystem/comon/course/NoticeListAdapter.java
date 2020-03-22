package com.mrgreenapps.coursemanagementsystem.comon.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.R;
import com.mrgreenapps.coursemanagementsystem.model.Notice;

import java.text.SimpleDateFormat;
import java.util.List;

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.NoticeViewHolder> {
    private List<DocumentSnapshot> noticeSnapshotList;

    @NonNull
    @Override
    public NoticeListAdapter.NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_notice_item, parent, false);

        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeListAdapter.NoticeViewHolder holder, int position) {
        Notice notice = noticeSnapshotList.get(position).toObject(Notice.class);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        if (notice.getTitle() != null)
            holder.titleTextView.setText(notice.getTitle());
        else holder.titleTextView.setText("No Title");

        if (notice.getDetails() != null)
            holder.detailsTextView.setText(notice.getDetails());
        else holder.detailsTextView.setText("No Details");

        if (notice.getDate() != null)
            holder.dateView.setText(dateFormat.format(notice.getDate()));
        else holder.dateView.setText("No Date");

    }

    public NoticeListAdapter(List<DocumentSnapshot> membersSnapshot) {
        this.noticeSnapshotList = membersSnapshot;
    }


    @Override
    public int getItemCount() {
        return noticeSnapshotList.size();
    }

    public void setNoticeSnapshotList(List<DocumentSnapshot> noticeSnapshotList) {
        this.noticeSnapshotList = noticeSnapshotList;
        notifyDataSetChanged();
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView detailsTextView;
        private TextView dateView;

        public NoticeViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.title);
            detailsTextView = itemView.findViewById(R.id.details);
            dateView = itemView.findViewById(R.id.date);


        }
    }

}