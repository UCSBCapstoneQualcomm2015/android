package com.sniffit.sniffit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sniffit.sniffit.R;
import com.sniffit.sniffit.Snapdragon;
import com.sniffit.sniffit.SniffitObject;

import java.util.ArrayList;

/**
 * Created by sohanshah on 11/16/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {

    ArrayList<SniffitObject> sniffitList;
    int displayFlag;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(ArrayList<SniffitObject> myDataset, int flag) {   //FOR NOW: SHOULD END UP ARRAYLIST OF OBJECTS (EITHER ROOMS OR ITEMS)
        sniffitList = myDataset;
        displayFlag = flag;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView tView = (TextView) holder.mView.findViewById(R.id.card_text);
        String entry = sniffitList.get(position).getName();
        tView.setText(entry);
        final int currentItem = position;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (displayFlag) {
                    case 1:
                        intent = new Intent(v.getContext(), RoomActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("object",sniffitList.get(position));

                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        v.getContext().startActivity(intent);       //NEXT STEP = CREATE ROOMACTIVITY
                        break;
                    case 4:
                        intent = new Intent(v.getContext(), ListDisplay.class);
                        intent.putExtra("displayFlag", 4);
                        v.getContext().startActivity(intent);
                        break;
                    default:
                        break;


                }
                // SHOULD TAKE YOU TO PAGE OF ROOM YOU WANT (SHOULD ADD INTENT EXTRAS)
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent dateLog = new Intent(v.getContext(), LogActivity.class);
//                        dateLog.putExtra("currentDate", mDataset.get(myDatePosition).date.getTime());
//                        dateLog.putExtra("dateList", dates);
//                        v.getContext().startActivity(dateLog);
//                    }
//                });
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return sniffitList.size();
    }
}
