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

import com.sniffit.sniffit.R;

/**
 * Created by sohanshah on 11/26/15.
 */
public class AddRefTagDialogFragment extends DialogFragment{
    public interface AddRefTagListener {
        public void refTagConfirm(DialogFragment dialog, String tagName, String tagId, String x, String y, int refFlag);
    }

    AddRefTagListener mListener;
    private EditText refTagName, id, x, y;

    public static AddRefTagDialogFragment newInstance(int num, String tagName, String tagId,String x, String y) {
        AddRefTagDialogFragment f = new AddRefTagDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("tagName", tagName);
        args.putString("id",tagId);
        args.putString("x", x);
        args.putString("y", y);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddRefTagListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement AddRefTagListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final int flag = getArguments().getInt("num");
        String tagName = getArguments().getString("tagName");
        String tagId = getArguments().getString("id");
        String tagX = getArguments().getString("x");
        String tagY = getArguments().getString("y");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_reference_tag_dialog, null);
        builder.setView(v);
        refTagName = (EditText) v.findViewById(R.id.tag_name);
        id = (EditText) v.findViewById(R.id.ref_tag_id);
        x = (EditText) v.findViewById(R.id.refTagX);
        y = (EditText) v.findViewById(R.id.refTagY);
        refTagName.setText(tagName);
        id.setText(tagId);
        x.setText(tagX);
        y.setText(tagY);
        switch (flag) {
            case 1:
                builder.setTitle("Add New Reference Item");
                break;
            case 2:
                builder.setTitle("Edit Reference Item");
                break;
            default:
                break;
        }
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.refTagConfirm(AddRefTagDialogFragment.this, refTagName.getText().toString(),
                        id.getText().toString(), x.getText().toString(), y.getText().toString(), flag);
            }
        })
                .setNegativeButton(R.string.cancel, null);


        // Create the AlertDialog object and return it
        return builder.create();
    }

}
