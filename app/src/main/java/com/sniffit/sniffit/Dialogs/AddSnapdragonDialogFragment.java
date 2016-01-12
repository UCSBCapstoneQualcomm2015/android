package com.sniffit.sniffit.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.R;

/**
 * Created by sohanshah on 11/23/15.
 */
public class AddSnapdragonDialogFragment extends DialogFragment {

    public interface AddSnapDragonListener {
        public void snapdragonConfirm(DialogFragment dialog, String tagName, String tagId, String x, String y);
    }

    AddSnapDragonListener mListener;
    private EditText snapdragonName, ip, x, y;

    public static AddSnapdragonDialogFragment newInstance(int num, String snapdragonName, String ip,String x, String y) {
        AddSnapdragonDialogFragment f = new AddSnapdragonDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("snapdragonName", snapdragonName);
        args.putString("ip",ip);
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

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement AddSnapdragonListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int flag = getArguments().getInt("num");
        String snapName = getArguments().getString("snapdragonName");
        String snapIp = getArguments().getString("ip");
        String snapX = getArguments().getString("x");
        final String snapY = getArguments().getString("y");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_snapdragon_dialog, null);
        builder.setView(v);
        snapdragonName = (EditText) v.findViewById(R.id.snap_name);
        ip = (EditText) v.findViewById(R.id.snap_ip);
        x = (EditText) v.findViewById(R.id.snapX);
        y = (EditText) v.findViewById(R.id.snapY);
        snapdragonName.setText(snapName);
        ip.setText(snapIp);
        switch (flag) {
            case 1:
                builder.setTitle("Add New Snapdragon");
                break;
            case 2:
                builder.setTitle("Edit Snapdragon");

//                x.setText(snapX);
   //             y.setText(snapY);
                break;
            default:
                break;
        }
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.snapdragonConfirm(AddSnapdragonDialogFragment.this, snapdragonName.getText().toString(),
                        ip.getText().toString(), x.getText().toString(), y.getText().toString());
            }
        })
                .setNegativeButton(R.string.cancel, null);


        // Create the AlertDialog object and return it
        return builder.create();
    }

}
