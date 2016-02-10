package com.sniffit.sniffit.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.R;

/**
 * Created by sohanshah on 11/23/15.
 */
public class AddSnapdragonDialogFragment extends DialogFragment {

    public interface AddSnapDragonListener {
        public void snapdragonConfirm(DialogFragment dialog, String tagName, String tagId, String x, String y, int snapFlag, String oldIp);

    }

    public interface DeleteSnapDragonListener {
        public void snapdragonDelete(DialogFragment dialog, String snapIp);
    }

    AddSnapDragonListener mListener;
    DeleteSnapDragonListener dListener;

    private EditText snapdragonName, ip, x, y;

    public static AddSnapdragonDialogFragment newInstance(int num, String snapdragonName, String ip,String x, String y, String id) {
        AddSnapdragonDialogFragment f = new AddSnapdragonDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("snapdragonName", snapdragonName);
        args.putString("ip",ip);
        args.putString("id", id);
        args.putString("x", x);
        args.putString("y", y);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddSnapDragonListener) activity;
            dListener = (DeleteSnapDragonListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement AddSnapdragonListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int flag = getArguments().getInt("num");
        final String snapName = getArguments().getString("snapdragonName");
        final String snapIp = getArguments().getString("ip");
        String snapX = getArguments().getString("x");
        String snapY = getArguments().getString("y");
        final String snapId = getArguments().getString("id");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_snapdragon_dialog, null);
        builder.setView(v);
        snapdragonName = (EditText) v.findViewById(R.id.snap_name);
        ip = (EditText) v.findViewById(R.id.snap_ip);
        x = (EditText) v.findViewById(R.id.snapX);
        y = (EditText) v.findViewById(R.id.snapY);
        snapdragonName.setText(snapName);
        ip.setText(snapIp);
        x.setText(snapX);
        y.setText(snapY);

        switch (flag) {
            case 1:
                builder.setTitle("Add New Sensor");
                builder.setNegativeButton("Cancel", null);
                break;
            case 2:
                builder.setTitle("Edit Sensor");
                builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dListener.snapdragonDelete(AddSnapdragonDialogFragment.this, snapIp);
                    }
                });
                break;
            default:
                break;
        }
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Context context = getActivity();
                int duration = Toast.LENGTH_LONG;
                String xText = x.getText().toString();
                String yText = y.getText().toString();
                if(isEmpty(x) || isEmpty(y) || isEmpty(snapdragonName) || isEmpty(ip)){
                    CharSequence text = "Enter all required fields";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else if(!(isNumber(xText) && isNumber(yText))){
                    CharSequence text = "Coordinates must be valid numbers";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else {
                    String name = snapdragonName.getText().toString();
                    mListener.snapdragonConfirm(AddSnapdragonDialogFragment.this, name.substring(0,1).toUpperCase() + name.substring(1),
                            ip.getText().toString(), xText, yText, flag, snapIp);
                }
            }
        });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static boolean isNumber(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

}
