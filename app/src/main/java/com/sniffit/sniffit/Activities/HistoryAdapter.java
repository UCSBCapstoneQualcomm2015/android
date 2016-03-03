package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sniffit.sniffit.Objects.History;
import com.sniffit.sniffit.R;

import java.util.ArrayList;

/**
 * Created by andrewpang on 3/1/16.
 */
public class HistoryAdapter extends ArrayAdapter<History> {
    Context context;

    public HistoryAdapter(Context context, int resourceId,
                                 ArrayList<History> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView date, room, coord;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        History rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.history_card, parent, false);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView room = (TextView) convertView.findViewById(R.id.room);
        TextView coord = (TextView) convertView.findViewById(R.id.coord);

        date.setText(rowItem.getCreated_at());
        room.setText("Room: " + rowItem.getRoomId());
        coord.setText("Location: (" + rowItem.getxCoord() + ", " + rowItem.getyCoord() + ")");

        convertView.setTag(holder);

        return convertView;
    }
}
