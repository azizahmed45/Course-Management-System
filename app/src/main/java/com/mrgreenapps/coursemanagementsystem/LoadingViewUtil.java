package com.mrgreenapps.coursemanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class LoadingViewUtil {
    private MutableLiveData<Integer> loaderCountData = new MutableLiveData<>(0);
    private Context context;
    private ProgressDialog progressDialog;

    public LoadingViewUtil(Context context){
        this.context = context;
        this.progressDialog = new ProgressDialog(context);

        loaderCountData.observe((AppCompatActivity)context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer counter) {
                if(counter > 0){
                    progressDialog.show();
                } else {
                    progressDialog.hide();
                }
            }
        });
    }

    public void showLoading(){
        loaderCountData.postValue(loaderCountData.getValue() + 1);
    }

    public void hideLoading(){
        loaderCountData.postValue(loaderCountData.getValue() -1);
    }

}
