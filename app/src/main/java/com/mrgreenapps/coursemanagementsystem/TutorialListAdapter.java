package com.mrgreenapps.coursemanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.model.CourseClass;
import com.mrgreenapps.coursemanagementsystem.model.Tutorial;

import java.util.List;

public class TutorialListAdapter extends RecyclerView.Adapter<TutorialListAdapter.TutorialViewHolder> {
    private List<DocumentSnapshot> tutorialSnapshotList;

    private OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TutorialListAdapter.TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_exam, parent, false);

        return new TutorialViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialListAdapter.TutorialViewHolder holder, int position) {
        Tutorial tutorial = tutorialSnapshotList.get(position).toObject(Tutorial.class);


        if (tutorial != null)
            holder.nameView.setText(tutorial.getName());
        else holder.nameView.setText("No Name");

    }

    public TutorialListAdapter(List<DocumentSnapshot> membersSnapshot) {
        this.tutorialSnapshotList = membersSnapshot;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return tutorialSnapshotList.size();
    }

    public void setTutorialSnapshotList(List<DocumentSnapshot> tutorialSnapshotList) {
        this.tutorialSnapshotList = tutorialSnapshotList;
        notifyDataSetChanged();
    }

    public static class TutorialViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;

        public TutorialViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            nameView = itemView.findViewById(R.id.name);

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