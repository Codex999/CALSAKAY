package com.example.calsakay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.List;

public class SplashScreen extends AppCompatActivity implements DatabaseAccessCallback{
    private String status;
    private Intent openMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        File file = new File(SplashScreen.this.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
            try {
                File gpxfile = new File(file, "config");
                FileWriter writer = new FileWriter(gpxfile);
                writer.write("0");
                writer.flush();
                writer.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        this.status = readFile();

        if (this.status.contentEquals("0")){
            openMainActivity = new Intent(SplashScreen.this, MainActivity.class);
            openActivity();
        } else {
            DatabaseAccess db = new DatabaseAccess(SplashScreen.this);
//            db.executeQuery("SELECT * FROM `calsakay_tbl_users` WHERE id = " + readFile());
            db.executeQuery("SELECT id, first_name, last_name FROM calsakay_tbl_users WHERE id = " + readFile());
        }
    }

    private String readFile() {
        File fileEvents = new File(SplashScreen.this.getFilesDir()+"/text/config");
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) { }
        String result = text.toString();
        return result;
    }


    private void openActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(openMainActivity);
                finish();
            }
        }, 3000);
    }

    @Override
    public void QueryResponse(List<String[]> data) {
        if(data.size() > 0){
            this.openMainActivity = new Intent(SplashScreen.this, Dashboard.class);
            this.openMainActivity.putExtra("userData", (Serializable) data);
            openActivity();
        }
    }
}