package com.example.calsakay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.dd.CircularProgressButton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class FindDriversFragment extends Fragment{

    private SmartMaterialSpinner<String> spPickupPoint, spDropoffPoint, spPersons;
    private List<String[]> locationList;
    private List<String> personNumber = new ArrayList<>();
    private ArrayList<String> rideTraceInfo = new ArrayList<>();
    private CircularProgressButton btFindDriver;
    private int btProgress, selectedPickupId, selectedDropoffId, userId, persons, listMaxPersons, rideId;
    private Bundle savedState = null;
    private Context currentContext;
    private Dashboard currentActivity;
    private boolean finding = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.currentActivity = (Dashboard) getActivity();
        this.btFindDriver = view.findViewById(R.id.btFindDrivers);
        this.spPickupPoint = view.findViewById(R.id.spPickupPoint);
        this.spDropoffPoint = view.findViewById(R.id.spDropoffPoint);
        this.spPersons = view.findViewById(R.id.spPersons);
        this.userId = Integer.parseInt(currentActivity.getUserData().get(0)[0]);
        this.listMaxPersons = 4;

        for(int i = 1; i <= this.listMaxPersons; i++){this.personNumber.add(String.valueOf(i));}
        this.spPersons.setItem(personNumber);
        this.spDropoffPoint.setEnabled(false);

        this.spPersons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                persons = Integer.parseInt(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.spPickupPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPickupId = Integer.parseInt(locationList.get(i)[0]);
                initializeDropOffLocationList(locationList, i);
                spDropoffPoint.setEnabled(true);
                btFindDriver.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.spDropoffPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDropoffId = findSelectedItem(spDropoffPoint.getSelectedItem());
                btFindDriver.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        this.btFindDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finding){
                    finding = false;
                    DatabaseAccess dbCancelFinding = new DatabaseAccess(currentContext);
                    dbCancelFinding.executeNonQuery("DELETE FROM `ride_trace` WHERE `ride_trace`.`id` = " + rideId);
                    btFindDriver.setProgress(0);
                } else {
                    finding = true;
                    new FindDrivers().execute();
                    btFindDriver.setProgress(0);
                    btFindDriver.setIndeterminateProgressMode(true);
                    btFindDriver.setProgress(50);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_drivers, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.currentContext = context;
        DatabaseAccess dbAccess = new DatabaseAccess(getContext());
        dbAccess.executeQuery("SELECT * FROM locations");
    }

    public void fillPickupPoint(List<String[]> data){
        this.locationList = data;
        initializePickupLocationList(data);
    }

    private void initializePickupLocationList(List<String[]> data){
        List<String> pickupList = new ArrayList<>();
        for(String[] row : data) {
            pickupList.add(row[1]);
        }
        this.spPickupPoint.setItem(pickupList);
    }

    private void initializeDropOffLocationList(List<String[]> data, int pickupPoint){
        List<String> dropoffList = new ArrayList<>();
        for(String[] row : data) {
            dropoffList.add(row[1]);
        }
        dropoffList.remove(pickupPoint);
        this.spDropoffPoint.setItem(dropoffList);
    }

    private int findSelectedItem(String name){
        int id = 0;
        for(String[] row : this.locationList){
            if(name.contentEquals(row[1])){
                id = Integer.parseInt(row[0]);
            }
        }
        return id;
    }

        class FindDrivers extends AsyncTask<Void, Void, Void> {
        int driverId;
        boolean passengerAccepted;
        String error, traceId;
        Statement statement;

        private void checkForDrivers(int id, String traceId) throws SQLException {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ride_trace WHERE id = " + id + " AND trace_id = '" + traceId + "'");
            int status = 0;
            while(resultSet.next()){
                status = resultSet.getInt("status");
            }

            if(status == 1){
                passengerAccepted = false;
            } else {
                resultSet = statement.executeQuery("SELECT driver FROM ride_trace WHERE id = " + id + " AND trace_id = '" + traceId + "'");
                while(resultSet.next()){
                    driverId = resultSet.getInt("driver");
                }

                passengerAccepted = true;
            }
        }

        private String generateTraceId() throws SQLException {
            String traceIdGen, previousTraceId = "";
            ResultSet resultSet = statement.executeQuery("SELECT trace_id FROM ride_trace ORDER BY SUBSTR(trace_id FROM 1 FOR 1), CAST(SUBSTR(trace_id FROM 6) AS UNSIGNED) DESC LIMIT 1");
            while(resultSet.next()){
                previousTraceId = resultSet.getString("trace_id");
            }

            if(previousTraceId == ""){
                previousTraceId = "CLSKY0";
            }

            traceIdGen = "CLSKY" + String.valueOf(Integer.parseInt(previousTraceId.substring(5)) + 1);
            return traceIdGen;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://163.44.242.10:3306/feqxsxpi_calsakay?characterEncoding=latin1","feqxsxpi_root", "UCC2021bsitKrazy");
                statement = connection.createStatement();

                traceId = generateTraceId();
                statement.executeUpdate("INSERT INTO ride_trace " +
                        "SET passenger = " + userId +
                        ", trace_id = '" + traceId +
                        "', persons = " + persons +
                        ", pickup = " + selectedPickupId +
                        ", dropoff = " + selectedDropoffId +
                        ", status = 1");

                ResultSet rs= statement.getGeneratedKeys();
                if(rs.next()){
                    rideId = rs.getInt(1);
                }

                passengerAccepted = false;

                while(passengerAccepted == false && finding == true){
                    checkForDrivers(rideId, traceId);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            if(finding){
                rideTraceInfo.add(String.valueOf(rideId));
                rideTraceInfo.add(traceId);
                rideTraceInfo.add(String.valueOf(userId));
                rideTraceInfo.add(String.valueOf(driverId));
                rideTraceInfo.add(String.valueOf(persons));
                rideTraceInfo.add(String.valueOf(selectedPickupId));
                rideTraceInfo.add(String.valueOf(selectedDropoffId));
                rideTraceInfo.add("2");
                currentActivity.setAccepted(true, driverId, rideId, rideTraceInfo);
                finding = false;
            }
            super.onPostExecute(unused);
        }
    }
}