package com.mrgreenapps.coursemanagementsystem.teacher.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.mrgreenapps.coursemanagementsystem.DB;
import com.mrgreenapps.coursemanagementsystem.R;
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


        if (tutorial != null && tutorial.getName() != null)
            holder.nameView.setText(tutorial.getName());
        else holder.nameView.setText("No Name");

        if (tutorial != null){
//            holder.publishButton.setOnCheckedChangeListener (null);
            holder.publishButton.setChecked(!tutorial.isPublished());
        }

        holder.publishButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DB.publishTutorialMarks(tutorialSnapshotList.get(position).getId(), !isChecked);
            }
        });

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