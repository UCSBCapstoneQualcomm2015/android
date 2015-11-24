package com.sniffit.sniffit.Activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sniffit.sniffit.Dialogs.AddItemDialogFragment;
import com.sniffit.sniffit.Dialogs.AddRoomDialogFragment;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.Objects.Room;

/**
 * Created by sohanshah on 11/19/15.
 */
public class RoomActivity extends Activity implements AddRoomDialogFragment.AddRoomListener{
    Room room;
    Button editRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hi","hi");
        setContentView(R.layout.room);
        TextView header = (TextView)findViewById(R.id.header_title);
        Intent intent = getIntent();
        room = (Room) intent.getExtras().getSerializable("object");
        header.setText(room.getName());
    }

    // when edit room button is clicked

    public void editRoom(View view) {
        DialogFragment editItem = AddRoomDialogFragment.newInstance(2, room.getName(), room.getLength(), room.getWidth());       //SHOULD PASS IN NAME AND ID OF SNIFFITLIST.GET(POSITION)
        editItem.show(getFragmentManager(), "editItem");
    }

    public void getSnapList(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //POSSIBILITY MAKE ALL INTENTS TO LISTDISPLAY BUNDLES AND GRAB THINGS OUT ACCORDINGLY
        intent.putExtra("displayFlag", 3);
        startActivity(intent);
    }

    @Override
    public void roomConfirm(DialogFragment dialog, String roomName, String length, String width) {
        Log.d(roomName, length);
        //would add it to the database here and reload the intent to update room
        Intent intent = new Intent(this, RoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",room);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }



    public void goToRooms(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void goToItems(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        Bundle bundle = new Bundle();
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
