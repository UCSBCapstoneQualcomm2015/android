package com.sniffit.sniffit.Dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.app.DialogFragment;
import android.app.Dialog;
import android.widget.EditText;
import android.app.AlertDialog;

import com.sniffit.sniffit.R;


/**
 * Created by sohanshah on 11/20/15.
 */public class AddItemDialogFragment extends DialogFragment {

    public interface AddItemListener {
        public void itemConfirm(DialogFragment dialog, String tagName, String tagId);
    }

    AddItemListener mListener;
    private EditText tagName, tagId;

    public static AddItemDialogFragment newInstance(int num, String itemName, String itemId) {
        AddItemDialogFragment f = new AddItemDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putString("itemName", itemName);
        args.putString("itemId",itemId);
        f.setArguments(args);

        return f;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddItemListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement AddItemListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int flag = getArguments().getInt("num");
        String itemName = getArguments().getString("itemName");
        String itemId = getArguments().getString("itemId");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.add_item_dialog, null);
        builder.setView(v);
        tagName = (EditText) v.findViewById(R.id.item_name);
        tagId = (EditText) v.findViewById(R.id.item_id);
        tagName.setText(itemName);
        tagId.setText(itemId);
        switch (flag) {
            case 1:
                builder.setTitle("Add New Item");
                break;
            case 2:
                builder.setTitle("Edit Item");
                break;
            default:
                break;
        }
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.itemConfirm(AddItemDialogFragment.this, tagName.getText().toString(),
                        tagId.getText().toString());
            }
        })
                .setNegativeButton(R.string.cancel, null);


        // Create the AlertDialog object and return it
        return builder.create();
    }

}

