package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sniffit.sniffit.Dialogs.AddItemDialogFragment;
import com.sniffit.sniffit.Dialogs.AddRefTagDialogFragment;
import com.sniffit.sniffit.Dialogs.AddSnapdragonDialogFragment;
import com.sniffit.sniffit.Objects.RFIDItem;
import com.sniffit.sniffit.Objects.ReferenceTag;
import com.sniffit.sniffit.Objects.Snapdragon;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.Objects.SniffitObject;
import com.sniffit.sniffit.REST.User;


import java.util.ArrayList;

/**
 * Created by sohanshah on 11/16/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<SniffitObject> sniffitList;
    int displayFlag;
    FragmentManager manager;
    User user;
    Bundle bundle;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        FragmentManager manager;
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
    public RecyclerAdapter(ArrayList<SniffitObject> myDataset, int flag, User user, FragmentManager manager) {   //FOR NOW: SHOULD END UP ARRAYLIST OF OBJECTS (EITHER ROOMS OR ITEMS)
        sniffitList = myDataset;
        displayFlag = flag;
        this.user = user;
        this.manager = manager;
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
                switch (displayFlag) {      //1: rooms page  2: edit item dialog  3: edit snapdragon dialog  4. edit reftag dialog
                    case 1:
                        intent = new Intent(v.getContext(), RoomActivity.class);
                        bundle = new Bundle();
                        bundle.putSerializable("room",sniffitList.get(position));
                        bundle.putSerializable("user", user);

                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Activity activity = (Activity) v.getContext();
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        break;
                    case 2:         //updating one of the items (should edit it in the db)
                        intent = new Intent(v.getContext(), ItemActivity.class);
                        bundle = new Bundle();
                        bundle.putSerializable("item",sniffitList.get(position));
                        bundle.putSerializable("user", user);

                        intent.putExtras(bundle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Activity itemActivity = (Activity) v.getContext();
                        itemActivity.startActivity(intent);
                        itemActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


//                        RFIDItem currItem = (RFIDItem) sniffitList.get(position);
//                        DialogFragment editItem = AddItemDialogFragment.newInstance(2, currItem.getName(), currItem.getTagId());       //SHOULD PASS IN NAME AND ID OF SNIFFITLIST.GET(POSITION)
//                        editItem.show(manager, "editItem");
                        break;
                    case 3:
                        Snapdragon curr = (Snapdragon) sniffitList.get(position);
                        DialogFragment editSnap = AddSnapdragonDialogFragment.newInstance(2, curr.getName(), curr.getIpAddress(),curr.getxCoord(),curr.getyCoord(), curr.get_id());
                        editSnap.show(manager, "editSnap");
                        break;
                    case 4:
                        ReferenceTag currRef = (ReferenceTag) sniffitList.get(position);
                        DialogFragment editRefTag = AddRefTagDialogFragment.newInstance(2,currRef.getName(), currRef.getId(), currRef.getX(), currRef.getY(), currRef.get_id());
                        editRefTag.show(manager, "editRefTag");
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
