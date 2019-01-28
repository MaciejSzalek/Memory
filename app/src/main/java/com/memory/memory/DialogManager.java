package com.memory.memory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Maciej Szalek on 2019-01-21.
 */

public class DialogManager {

    private Context context;

    public interface Action{
        void setConfirmButton();
        void setDeleteButton();
        void setPositionString(String positionString);
    }

    public DialogManager(Context context){
        this.context = context;
    }

    public void editPositionDialogBuilder(String title, String posStr,  final Action action){
        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.edit_pos_dialog_layout, null);
        editDialogBuilder.setView(view);
        editDialogBuilder.setCancelable(true);
        final AlertDialog dialog = editDialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        TextView editDialogTitle = view.findViewById(R.id.edit_dialog_title);
        final AppCompatMultiAutoCompleteTextView autoCompleteTextView
                = view.findViewById(R.id.new_dialog_autoComplete_textView);
        ImageButton editButton = view.findViewById(R.id.edit_position_button);
        ImageButton deleteButton = view.findViewById(R.id.delete_position_button);

        editDialogTitle.setText(title);
        autoCompleteTextView.setText(posStr);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoCompleteTextView.length() < 1){
                    Toast.makeText(context, R.string.field_not_empty, Toast.LENGTH_SHORT ).show();
                }else{
                    action.setPositionString(autoCompleteTextView.getText().toString());
                    action.setConfirmButton();
                    dialog.dismiss();
                }
            }
        });
         deleteButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(autoCompleteTextView.length() < 1){
                     Toast.makeText(context, R.string.field_not_empty, Toast.LENGTH_SHORT ).show();
                 }else{
                     action.setPositionString(autoCompleteTextView.getText().toString());
                     action.setDeleteButton();
                     dialog.dismiss();
                 }
             }
         });
    }

    public void addNewPositionDialogBuilder(String title, final Action action){

        AlertDialog.Builder editDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.new_pos_dialog_layout, null);
        editDialogBuilder.setView(view);
        editDialogBuilder.setCancelable(true);
        final AlertDialog dialog = editDialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        TextView editDialogTitle = view.findViewById(R.id.new_dialog_title);
        final AppCompatMultiAutoCompleteTextView autoCompleteTextView
                = view.findViewById(R.id.new_dialog_autoComplete_textView);
        ImageButton confirmButton = view.findViewById(R.id.confirm_position_button);

        editDialogTitle.setText(title);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoCompleteTextView.length() < 1){
                    Toast.makeText(context, R.string.field_not_empty, Toast.LENGTH_SHORT ).show();
                }else{
                    action.setPositionString(autoCompleteTextView.getText().toString());
                    action.setConfirmButton();
                    dialog.dismiss();
                }
            }
        });
    }

    public void deleteAllCheckedDialogBuilder(final Action action){
        AlertDialog.Builder dialogClose = new AlertDialog.Builder(context);
        dialogClose.setTitle(R.string.remove_all);
        dialogClose.setMessage(R.string.remove_all_message);
        dialogClose.setCancelable(true);
        dialogClose.setNegativeButton(R.string.cancel_button_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogClose.setPositiveButton(R.string.ok_button_str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                action.setConfirmButton();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = dialogClose.create();
        dialog.show();
    }
}
