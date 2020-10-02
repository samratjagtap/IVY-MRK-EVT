package com.jagtapazad.ivymrkevt.HelperClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.jagtapazad.ivymrkevt.R;

public class LoadingDialog {


    Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;


    }


    public void startDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loadingdialog, null));
        builder.setCancelable(false);


        dialog = builder.create();
        dialog.show();
    }


    public void dismissDialog() {
        dialog.dismiss();
    }

}
