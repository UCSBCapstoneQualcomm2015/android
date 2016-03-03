package com.sniffit.sniffit.Activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sniffit.sniffit.Dialogs.AddItemDialogFragment;
import com.sniffit.sniffit.Dialogs.AddRoomDialogFragment;
import com.sniffit.sniffit.Objects.History;
import com.sniffit.sniffit.Objects.RFIDItem;
import com.sniffit.sniffit.Objects.ReferenceTag;
import com.sniffit.sniffit.Objects.Room;
import com.sniffit.sniffit.Objects.Snapdragon;
import com.sniffit.sniffit.R;
import com.sniffit.sniffit.REST.ServerRequest;
import com.sniffit.sniffit.REST.User;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ItemActivity extends ActionBarActivity implements AddItemDialogFragment.AddItemListener {

    RFIDItem item;
    Button currentPage;
    User user;
    Bundle bundle;
    final ServerRequest sr = new ServerRequest();
    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.header);
        TextView header = (TextView) toolbar.findViewById(R.id.header_title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView headerTitle = (TextView) findViewById(R.id.header_title);

        Intent intent = getIntent();
        item = (RFIDItem) intent.getExtras().getSerializable("item");
        user = (User) intent.getExtras().getSerializable("user");
        currentPage = (Button) findViewById(R.id.items_button);
        currentPage.setBackgroundColor(Color.parseColor("#294e6a"));
        headerTitle.setText(item.getName());
        bundle = new Bundle();
        bundle.putSerializable("user", user);

        final ListView listview = (ListView) findViewById(R.id.listview);

//        ArrayList<History> historyArrayList = new ArrayList<History>();
//        History he = new History();
//        he.setCreated_at("aslkdfj;lks");
//        he.setxCoord("3");
//        he.setyCoord("4");
//        he.setRoomId("alksfdj;");
//        he.setTagId("aslkfj");
//        historyArrayList.add(he);
//        final HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(),
//                R.layout.history_card, historyArrayList);
//        listview.setAdapter(adapter);

        sr.getId("history", item.getTagId(), user, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    History[] historyArray;
                    String json = response.body().string();
                    Gson gson = new Gson();
                    historyArray = gson.fromJson(json, History[].class);
                    ArrayList<History> historyArrayList = new ArrayList<History>();
                    for(History h: historyArray){
                        historyArrayList.add(h);
                    }
                    final HistoryAdapter adapter = new HistoryAdapter(getApplicationContext(),
                            R.layout.history_card, historyArrayList);
                    listview.setAdapter(adapter);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

    }

    public void editItem(View view) {
        DialogFragment editItem = AddItemDialogFragment.newInstance(2, item.getName(), item.getTagId());
        editItem.show(getFragmentManager(), "editItem");
    }

    @Override
    public void itemConfirm(DialogFragment dialog, String itemName, String itemId, int itemFlag, String oldId) {
        Log.d(itemName, itemId);
        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (itemFlag == 1) {
            sr.postRFIDTag(user, itemId, itemName, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else{
            sr.putRFIDTag(user, oldId, itemId, itemName, new Callback<ResponseBody>() {
                @Override
                public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                    try {
                        String jsonBody = response.body().string();
                        JSONObject object = new JSONObject(jsonBody);
                        String message = object.getString("message");
                        Context context = getApplicationContext();
                        int duration = Toast.LENGTH_LONG;
                        CharSequence text = message;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }

    }

    @Override
    public void itemDelete(DialogFragment dialog, String id) {

        final Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sr.deleteItem(user, id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                final SharedPreferences.Editor editor = pref.edit();
                editor.putInt("itemSpinnerPosition", -1);
                editor.commit();
                startActivity(intent);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    ///////// MENU STUFF /////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logged_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.log_out) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
        return super.onOptionsItemSelected(item);
    }




    ////////// FOOTER STUFF /////////////

    public void goToFind(View view){
        Intent intent = new Intent(this, FindActivity.class);
        bundle.putSerializable("flag", -1);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void goToRooms(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 1);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void goToItems(View view) {
        Intent intent = new Intent(this, ListDisplay.class);
        bundle.putInt("displayFlag", 2);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
