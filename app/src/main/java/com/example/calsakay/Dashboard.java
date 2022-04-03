package com.example.calsakay;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Dashboard extends AppCompatActivity implements DatabaseAccessCallback{
    private SpaceNavigationView snv;
    private List<String[]> userData;
    ProfileFragment fgProfile = new ProfileFragment();
    HistoryFragment fgHistory = new HistoryFragment();
    FindDriversFragment fgFindDrivers = new FindDriversFragment();
    MessagesFragment fgMessages = new MessagesFragment();
    PassengerAcceptedFragment fgPassengerAcceptedFragment = new PassengerAcceptedFragment();
    public boolean accepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Intent intent = getIntent();
        this.userData = (List<String[]>) intent.getSerializableExtra("userData");
        getSupportActionBar().hide();
        this.accepted = false;

        this.snv = (SpaceNavigationView) findViewById(R.id.space);
        this.snv.initWithSaveInstanceState(savedInstanceState);
        this.snv.showIconOnly();
        this.snv.addSpaceItem(new SpaceItem("Profile", R.drawable.ic_dashboard_profile));
        this.snv.addSpaceItem(new SpaceItem("History", R.drawable.ic_dashboard_history));
        this.snv.addSpaceItem(new SpaceItem("Messages", R.drawable.ic_dashboard_messages));
        this.snv.addSpaceItem(new SpaceItem("Logout", R.drawable.ic_dashboard_logout));
        this.snv.setCentreButtonSelectable(true);
        showFragment(0);

        this.snv.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                snv.setCentreButtonSelected();
                showFragment(4);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                showFragment(itemIndex);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                showFragment(itemIndex);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.snv.onSaveInstanceState(outState);
    }


    private void logout(){
        File file = new File(Dashboard.this.getFilesDir(), "text");
        try {
            File gpxfile = new File(file, "config");
            FileWriter writer = new FileWriter(gpxfile);
            writer.write("0");
            writer.flush();
            writer.close();
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        Intent mIntent = new Intent(this, MainActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void showFragment(int itemIndex){
        switch (itemIndex){
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flDashboard, this.fgProfile)
                        .commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flDashboard, this.fgHistory)
                        .commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flDashboard, this.fgMessages)
                        .commit();
                break;
            case 3:
                new SweetAlertDialog(Dashboard.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Logout")
                        .setContentText("Are you sure you want to logout?")
                        .setCancelText("Cancel")
                        .setConfirmText("Logout")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                logout();
                            }
                        })
                        .show();
                break;
            case 4:
                if(accepted){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flDashboard, this.fgPassengerAcceptedFragment)
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flDashboard, this.fgFindDrivers)
                            .commit();
                }
                break;
        }
    }

    public boolean isAccepted(){
        return accepted;
    }

    public void setAccepted(boolean accpt, int driverId, int rideTraceId, ArrayList<String> rideTraceInfo){
        this.accepted = accpt;
        Bundle bundle = new Bundle();
        bundle.putInt("driverId", driverId);
        bundle.putInt("rideTraceId", rideTraceId);
        bundle.putStringArrayList("rideTraceInfo", rideTraceInfo);
        this.fgPassengerAcceptedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flDashboard, this.fgPassengerAcceptedFragment)
                .commit();
    }

    public void droppedOff(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flDashboard, this.fgFindDrivers)
                .commit();
        this.accepted = false;
    }

    public List<String[]> getUserData() {
        return this.userData;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void QueryResponse(List<String[]> data) {
        if(data.size() != 0){
            switch (data.get(0)[data.get(0).length - 1]){
                case "DRIVER DETAILS":
                    fgPassengerAcceptedFragment.setDriverDetails(data);
                    break;
                case "PASSENGER DETAILS":
                    fgPassengerAcceptedFragment.initializeMessageDetails(data);
                    break;
                default:
                    this.fgFindDrivers.fillPickupPoint(data);
                    break;
            }
        } else {
            fgPassengerAcceptedFragment.initializeMessageDetails(null);
        }
    }
}