package com.example.calsakay;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class PassengerAcceptedFragment extends Fragment {
    private int driverId, userId, rideTraceId, rideStatus;
    private boolean droppedOff = false, pickedUp = false;
    String[] rideStatusText = {
            "Your driver will be picking you up now!",
            "You have been picked up!",
            "You are now on the way to your destination.",
    };
    ImageView ivDriverImage;
    FloatingActionMenu fmPassengerAcceptedMenu;
    FloatingActionButton fbPassengerAcceptedMenuItem1, fbPassengerAcceptedMenuItem2;
    TextView tvRideStatus, tvDriverName, tvDriverMobileNumber, tvDriverVehicleType, tvDriverPlateNumber, tvDriveEmail;
    Button btPassengerPickedup;
    Context currentContext;
    Dashboard currentActivity;
    List<String[]> driverDetails;
    ArrayList<String> rideTraceInfo;
    Messages messageData;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.ivDriverImage = view.findViewById(R.id.ivDriverImage);
        this.fmPassengerAcceptedMenu = view.findViewById(R.id.fmPassengerAcceptedMenu);
        this.fbPassengerAcceptedMenuItem1 = view.findViewById(R.id.fbPassengerAcceptedMenuItem1);
        this.fbPassengerAcceptedMenuItem2 = view.findViewById(R.id.fbPassengerAcceptedMenuItem2);
        this.tvRideStatus = view.findViewById(R.id.tvRideStatus);
        this.tvDriverName = view.findViewById(R.id.tvDriverName);
        this.tvDriverMobileNumber = view.findViewById(R.id.tvDriverMobileNumber);
        this.tvDriverVehicleType = view.findViewById(R.id.tvDriverVehicleType);
        this.tvDriverPlateNumber = view.findViewById(R.id.tvDriverPlateNumber);
        this.tvDriveEmail = view.findViewById(R.id.tvDriveEmail);
        this.btPassengerPickedup = view.findViewById(R.id.btPassengerPickedup);
        this.currentActivity = (Dashboard) getActivity();
        this.userId = Integer.parseInt(currentActivity.getUserData().get(0)[0]);

        this.fbPassengerAcceptedMenuItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoConvo = new Intent(currentContext, Conversation.class);
                gotoConvo.putExtra("messageData", messageData);
                currentContext.startActivity(gotoConvo);
            }
        });

        this.btPassengerPickedup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseAccess dbAccess = new DatabaseAccess(currentContext);
                dbAccess.executeNonQuery("INSERT INTO ride_trace SET " +
                                "trace_id = '" + rideTraceInfo.get(1) +
                                "', passenger = " + rideTraceInfo.get(2) +
                                ", driver = " + rideTraceInfo.get(3) +
                                ", persons = " + rideTraceInfo.get(4) +
                                ", pickup = " + rideTraceInfo.get(5) +
                                ", dropoff = " + rideTraceInfo.get(6) +
                                ", status = 4"
                        );
                pickedUp = true;
                btPassengerPickedup.setVisibility(View.INVISIBLE);
            }
        });

        this.fbPassengerAcceptedMenuItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(currentContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Cancel Ride")
                        .setContentText("Are you sure you want to cancel your ride?")
                        .setCancelText("No")
                        .setConfirmText("Yes.")
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
                                DatabaseAccess dbAccess = new DatabaseAccess(currentContext);
                                dbAccess.executeNonQuery("INSERT INTO ride_trace SET " +
                                        "trace_id = '" + rideTraceInfo.get(1) +
                                        "', passenger = " + rideTraceInfo.get(2) +
                                        ", driver = " + rideTraceInfo.get(3) +
                                        ", persons = " + rideTraceInfo.get(4) +
                                        ", pickup = " + rideTraceInfo.get(5) +
                                        ", dropoff = " + rideTraceInfo.get(6) +
                                        ", status = 6"
                                );
                                sweetAlertDialog.cancel();
                                currentActivity.droppedOff();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_passenger_accepted, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.driverId = getArguments().getInt("driverId");
        this.rideTraceId = getArguments().getInt("rideTraceId");
        this.rideTraceInfo = getArguments().getStringArrayList("rideTraceInfo");
        this.currentContext = context;
        DatabaseAccess dbAccess = new DatabaseAccess(context);
        dbAccess.executeQuery("SELECT *, 'DRIVER DETAILS' as Data FROM calsakay_tbl_users WHERE id = " + this.driverId);
    }

    public void setDriverDetails(List<String[]> data){
        this.driverDetails = data;

        String driverImageValue = this.driverDetails.get(0)[22],
                driverNameValue = this.driverDetails.get(0)[1] + " " + this.driverDetails.get(0)[2],
                driverMobileNo = this.driverDetails.get(0)[5],
                driverVehicle = this.driverDetails.get(0)[17],
                driverPlateNo = this.driverDetails.get(0)[18],
                driverEmail = this.driverDetails.get(0)[21];

        InputStream stream = new ByteArrayInputStream(Base64.decode(driverImageValue.getBytes(), Base64.DEFAULT));
        Bitmap chatImageBitmap = BitmapFactory.decodeStream(stream);
        this.tvRideStatus.setText(rideStatusText[0]);
        this.ivDriverImage.setImageBitmap(chatImageBitmap);
        this.tvDriverMobileNumber.setText(this.tvDriverMobileNumber.getText() + driverMobileNo);
        this.tvDriverVehicleType.setText(this.tvDriverVehicleType.getText() + driverVehicle);
        this.tvDriverPlateNumber.setText(this.tvDriverPlateNumber.getText() + driverPlateNo);
        this.tvDriveEmail.setText(this.tvDriveEmail.getText() + driverEmail);
        this.tvDriverName.setText(this.tvDriverName.getText() + driverNameValue);

        DatabaseAccess dbAccess = new DatabaseAccess(currentContext);
        dbAccess.executeQuery("SELECT calsakay_tbl_users.user_image AS chatmateImage, " +
                "CONCAT(calsakay_tbl_users.first_name, ' ', calsakay_tbl_users.last_name) AS threadName, " +
                "IF(messages.sender = " + this.userId + ", 'outgoing', 'ingoing') AS messageType, " +
                "messages.sender, messages.reciever, messages.message_id, messages.message, messages.read, messages.time, messages.read, 'PASSENGER DETAILS' as Data " +
                "FROM `messages` JOIN calsakay_tbl_users ON " +
                "IF(messages.sender = " + this.userId + ", messages.reciever = calsakay_tbl_users.id, messages.sender = calsakay_tbl_users.id)" +
                "WHERE (`reciever` = " + this.userId + " AND `sender` = " + this.driverId + ") " +
                "OR (`reciever` = " + this.driverId + " AND `sender` = " + userId + ") " +
                "ORDER BY message_id DESC LIMIT 1");
    }

    private Date convertToDateObject(String mysqlString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        Date date = formatter.parse(mysqlString);
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initializeMessageDetails(List<String[]> data){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            this.messageData = new Messages(driverId,
                    userId,
                    convertToDateObject(dtf.format(now)),
                    "",
                    0,
                    this.driverDetails.get(0)[1] + " " + this.driverDetails.get(0)[2],
                    "ingoing",
                    true,
                    this.driverId,
                    this.userId,
                    this.driverDetails.get(0)[22]);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 5000);
                    new CheckStatus().execute();
                }
            }, 3000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

        class CheckStatus extends AsyncTask<Void, Void, Void> {
        Statement statement;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://163.44.242.10:3306/feqxsxpi_calsakay?characterEncoding=latin1","feqxsxpi_root", "UCC2021bsitKrazy");
                statement = connection.createStatement();
                rideStatus = 2;

                ResultSet resultSet = statement.executeQuery("SELECT status FROM ride_trace WHERE trace_id = '" + rideTraceInfo.get(1) + "' ORDER BY id DESC LIMIT 1");
                while(resultSet.next()){
                    rideStatus = resultSet.getInt("status");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            switch (rideStatus){
                case 2:
                    tvRideStatus.setText(rideStatusText[0]);
                    if(pickedUp){
                        btPassengerPickedup.setVisibility(View.VISIBLE);
                    }
                    break;
                case 3:
                    tvRideStatus.setText(rideStatusText[1]);
                    break;
                case 4:
                    tvRideStatus.setText(rideStatusText[2]);
                    break;
                case 5:
                    if(droppedOff == false){
                        new SweetAlertDialog(currentContext, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Dropped Off")
                                .setContentText("You are now at your destination. Thank you for using CALSAKAY! Stay safe!")
                                .setConfirmText("Ok")
                                .showCancelButton(false)
                                .setCustomImage(getResources().getDrawable(R.drawable.ic_dialog_dropped_off))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .show();
                        currentActivity.droppedOff();
                        droppedOff = true;
                    }
                    break;
            }
            super.onPostExecute(unused);
        }
    }
}