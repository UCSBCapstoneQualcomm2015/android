package com.sniffit.sniffit.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.sniffit.sniffit.R;

/**
 * Created by sohanshah on 11/23/15.
 */
public class AddRoomDialogFragment extends DialogFragment{
    public interface AddRoomListener {
        public void roomConfirm(DialogFragment dialog, String roomName, String length, String width, int roomFlag, String oldName);

    }

    public interface DeleteRoomListener {
        public void roomDelete(DialogFragment dialog, String roomId);
    }

    AddRoomListener mListener;
    DeleteRoomListener dListener;
    private EditText edit_roomName, edit_length, edit_width;

    public static AddRoomDialogFragment newInstance(int num, String roomName, String length, String width, String id) {
        AddRoomDialogFragment f = new AddRoomDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("roomName", roomName);
        args.putString("length",length);
        args.putString("width",width);
        args.putString("id", id);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddRoomListener) activity;
            dListener = (DeleteRoomListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement AddRoomListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int flag = getArguments().getInt("num");
        final String oldName = getArguments().getString("roomName");
        String length = getArguments().getString("length");
        String width = getArguments().getString("width");
        final String id = getArguments().getString("id");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_room_dialog, null);
        builder.setView(v);
        edit_roomName = (EditText) v.findViewById(R.id.room_name);
        edit_length = (EditText) v.findViewById(R.id.length);
        edit_width = (EditText) v.findViewById(R.id.width);
        edit_roomName.setText(oldName);
        edit_length.setText(length);
        edit_width.setText(width);
        switch (flag) {
            case 1:
                builder.setTitle("Add New Room");
                builder.setNegativeButton("Cancel", null);
                break;
            case 2:
                builder.setTitle("Edit Room");
                Log.d("test", id);
                builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dListener.roomDelete(AddRoomDialogFragment.this, id);
                    }
                });
                break;
            default:
                break;
        }
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.roomConfirm(AddRoomDialogFragment.this, edit_roomName.getText().toString(),
                        edit_length.getText().toString(), edit_width.getText().toString(), flag, oldName);
            }
        });



        // Create the AlertDialog object and return it
        return builder.create();
    }

}


