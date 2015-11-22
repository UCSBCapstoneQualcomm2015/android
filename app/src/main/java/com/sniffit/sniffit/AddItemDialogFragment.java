package com.sniffit.sniffit;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.app.DialogFragment;
import android.app.Dialog;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.app.AlertDialog;


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
        switch (flag) {
            case 1:
                builder.setTitle("Add New Item");
                break;
            case 2:
                builder.setTitle("Edit Item");
                tagName.setText(itemName);
                tagId.setText(itemId);
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
        //      score_one = (EditText) v.findViewById(R.id.score_one);
//        score_two = (EditText) v.findViewById(R.id.score_two);
//        team_one = (AutoCompleteTextView) v.findViewById(R.id.team_one_name);
//        team_two = (AutoCompleteTextView) v.findViewById(R.id.team_two_name);
//        String[] teams = getResources().getStringArray(R.array.team_list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, teams);
//        team_one.setAdapter(adapter);
//        team_two.setAdapter(adapter);
//        builder.setTitle(R.string.title)
//                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        mListener.onDialogPositiveClick(GameEntry.this, score_one.getText().toString(),
//                                score_two.getText().toString(),
//                                team_one.getText().toString(),
//                                team_two.getText().toString());
//                    }
//                });
//
//        return builder.create();
//    }


        // Create the AlertDialog object and return it
        return builder.create();
    }

}

