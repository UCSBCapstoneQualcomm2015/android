package com.sniffit.sniffit.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.app.DialogFragment;
import android.app.Dialog;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Toast;

import com.sniffit.sniffit.R;


/**
 * Created by sohanshah on 11/20/15.
 */public class AddItemDialogFragment extends DialogFragment {

    public interface AddItemListener {
        public void itemConfirm(DialogFragment dialog, String tagName, String tagId, int itemFlag, String oldId);
        public void itemDelete(DialogFragment dialog, String id);
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
        final int flag = getArguments().getInt("num");
        String itemName = getArguments().getString("itemName");
        final String itemId = getArguments().getString("itemId");
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
                builder.setNegativeButton(R.string.cancel, null);
                break;
            case 2:
                builder.setTitle("Edit Item");
                builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.itemDelete(AddItemDialogFragment.this, itemId);
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
                if(isEmpty(tagId) || isEmpty(tagName)){
                    CharSequence text = "Enter all required fields";
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else {
                    String name = tagName.getText().toString();
                    mListener.itemConfirm(AddItemDialogFragment.this, name.substring(0, 1).toUpperCase() + name.substring(1),
                            tagId.getText().toString(), flag, itemId);
                }
            }
        });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
}

