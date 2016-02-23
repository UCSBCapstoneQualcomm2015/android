package com.sniffit.sniffit.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by andrewpang on 2/10/16.
 */
public class ConfirmDialogFragment extends DialogFragment {

    public interface ConfirmDialogListener {
        public void onConfirmPositiveClick(DialogFragment dialog);
    }

    ConfirmDialogListener listener;

    public static ConfirmDialogFragment newInstance(String item, String room) {
        ConfirmDialogFragment f = new ConfirmDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("item", item);
        args.putString("room",room);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (ConfirmDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String itemName = getArguments().getString("item");
        final String roomName = getArguments().getString("room");

        builder.setMessage("Look for " + itemName + " in " + roomName + "?")
                .setTitle("Confirm")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onConfirmPositiveClick(ConfirmDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", null);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
