package com.example.nishad.tourmate.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nishad.tourmate.R;
import com.example.nishad.tourmate.adapter.CustomListAdapterDialog;
import com.example.nishad.tourmate.adapter.PhotoMomentAdapter;
import com.example.nishad.tourmate.constant.Constants;
import com.example.nishad.tourmate.database.EventsDataSource;
import com.example.nishad.tourmate.database.PhotoMomentDataSource;
import com.example.nishad.tourmate.model.Event;
import com.example.nishad.tourmate.model.PhotoMoment;

import java.util.ArrayList;

public class MomentsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EventsDataSource eventsDataSource;
    private PhotoMomentDataSource photoMomentDataSource;
    private String userEmail;
    private Event event;
    private int eventId;
    private String eventName;
    private String eventFrom;
    private String eventTo;
    private double eventBudget;
    private TextView tvEventName;
    private TextView tvEventFrom;
    private TextView tvEventTo;
    private TextView tvEventBudgetAmount;
    private ListView lvMomentList;
    private PhotoMomentAdapter photoMomentAdapter;
    private ArrayList<PhotoMoment> photoMoments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userEmail = getIntent().getStringExtra(Constants.USER_EMAIL);
        eventId = getIntent().getIntExtra(Constants.EVENT_ID, 0);
        eventsDataSource = new EventsDataSource(this);
        photoMomentDataSource = new PhotoMomentDataSource(this);

        findView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> myListOfItems = new ArrayList<>();
                myListOfItems.add("Add photo moment");
                myListOfItems.add("Add expense moment");

                final Dialog dialog = new Dialog(MomentsActivity.this);

                View view1 = getLayoutInflater().inflate(R.layout.dialog_main, null);

                ListView lv = (ListView) view1.findViewById(R.id.custom_list);

                // Change MyActivity.this and myListOfItems to your own values
                CustomListAdapterDialog clad = new CustomListAdapterDialog(MomentsActivity.this,
                        myListOfItems);

                lv.setAdapter(clad);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            Intent intent = new Intent(MomentsActivity.this, AddMomentPhotoActivity.class);
                            intent.putExtra(Constants.EVENT_ID, eventId);
                            startActivity(intent);
                            dialog.dismiss();
                        } else {
                            Intent intent = new Intent(MomentsActivity.this, AddMomentExpenseActivity.class);
                            intent.putExtra(Constants.EVENT_ID, eventId);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });

                dialog.setContentView(view1);
                dialog.setTitle("Select an option");

                dialog.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        Log.e("MomentsActivity ", "onStart: ");
        getEvent();
        getPhotoMoments();
        populateMomentActivity();
        super.onStart();
    }

    private void getPhotoMoments() {
        photoMoments = photoMomentDataSource.getAllPhotoMoments(eventId);
    }

    private void getEvent() {
        event = eventsDataSource.getEvent(eventId);
        eventName = event.getEventName();
        eventFrom = event.getFromDate();
        eventTo = event.getToDate();
        eventBudget = event.getBudget();
    }

    private void findView() {
        tvEventName = (TextView) findViewById(R.id.tvEventName);
        tvEventFrom = (TextView) findViewById(R.id.tvEventFrom);
        tvEventTo = (TextView) findViewById(R.id.tvEventTo);
        tvEventBudgetAmount = (TextView) findViewById(R.id.tvEventBudgetAmount);
        lvMomentList = (ListView) findViewById(R.id.lvMomentList);
    }

    private void populateMomentActivity() {
        tvEventName.setText(eventName);
        tvEventFrom.setText(eventFrom);
        tvEventTo.setText(eventTo);
        tvEventBudgetAmount.setText("$"+eventBudget);
        Log.e("MOMENTS_ACTIVITY", "populateMomentActivity: " + photoMoments.toString() );
        photoMomentAdapter = new PhotoMomentAdapter(this, photoMoments);
        lvMomentList.setAdapter(photoMomentAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.moments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
