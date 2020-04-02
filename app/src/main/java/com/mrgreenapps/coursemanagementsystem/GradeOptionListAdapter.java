package com.mrgreenapps.coursemanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GradeOptionListAdapter extends RecyclerView.Adapter<GradeOptionListAdapter.GradeOptionViewHolder> {

//    HashMap<String, Float> gradeList = new HashMap<>();
//    HashMap<String, Float> defaultGradeList = new HashMap<>();
//    HashMap<String, Float> customGradeList = new HashMap<>();

    List<String> gradeLetters = new ArrayList<String>();
    List<String> gradeLowerBounds = new ArrayList<>();

    List<String> defaultGdeLetters = new ArrayList<>();
    List<String> defaultGradeLowerBounds = new ArrayList<>();

    List<String> customGdeLetters = new ArrayList<>();
    List<String> customGradeLowerBounds = new ArrayList<>();

    boolean defaultGrade = true;

    Context context;


    public GradeOptionListAdapter(Context context) {
        this.context = context;

        defaultGdeLetters.addAll(Arrays.asList(context.getResources().getStringArray(R.array.grade_letters)));
        defaultGradeLowerBounds.addAll(Arrays.asList(context.getResources().getStringArray(R.array.grade_lower_bounds)));



//        for(int i = 0; i < defaultGdeLetters.size(); i++){
//            defaultGradeList.put(defaultGdeLetters.get(i), Float.parseFloat(defaultGradeLowerBounds.get(i)));
//        }

    }

    @NonNull
    @Override
    public GradeOptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_grade_item, parent, false);

        return new GradeOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeOptionViewHolder holder, int position) {
        holder.gradeLetterView.setText(gradeLetters.get(position));
        holder.lowerBound.setText(gradeLowerBounds.get(position));

        if(defaultGrade){
            holder.removeButton.setVisibility(View.GONE);
        } else {
            holder.removeButton.setVisibility(View.VISIBLE);
        }

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradeLetters.remove(position);
                gradeLowerBounds.remove(position);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return gradeLetters.size();
    }

    public void setDefaultGradeList() {
        gradeLetters = defaultGdeLetters;
        gradeLowerBounds = defaultGradeLowerBounds;
        defaultGrade = true;
        notifyDataSetChanged();
    }

    public void setCustomGradeList() {
        gradeLetters = customGdeLetters;
        gradeLowerBounds = customGradeLowerBounds;
        defaultGrade = false;
        notifyDataSetChanged();
    }

    public void addCustomGrade(String letter, String lowerBound){
        customGdeLetters.add(letter);
        customGradeLowerBounds.add(lowerBound);
        setCustomGradeList();
    }

    public HashMap<String, String> getGradeHashMap(){
        HashMap<String, String> grades = new HashMap<>();
        DecimalFormat formatter = new DecimalFormat("#.##");
        try{
            for(int i = 0; i < gradeLetters.size(); i++){
                grades.put(gradeLowerBounds.get(i), gradeLetters.get(i));
            }
        } catch (Exception e){
            if(e instanceof NumberFormatException){
                Toast.makeText(context, "Lower bound should be number", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something went wrong please restart the app.", Toast.LENGTH_SHORT).show();
            }
        }

        return grades;
    }

    public List<String> getGradeLetters(){
        return gradeLetters;
    }

    public List<String> getGradeLowerBounds() {
        return gradeLowerBounds;
    }

    public static class GradeOptionViewHolder extends RecyclerView.ViewHolder {

        TextView gradeLetterView;
        TextView lowerBound;
        ImageButton removeButton;

        public GradeOptionViewHolder(@NonNull View itemView) {
            super(itemView);

            gradeLetterView = itemView.findViewById(R.id.grade_letter);
            lowerBound = itemView.findViewById(R.id.lower_bound);
            removeButton = itemView.findViewById(R.id.remove_button);
        }
    }
}
