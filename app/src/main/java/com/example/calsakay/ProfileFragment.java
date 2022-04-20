package com.example.calsakay;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {


    TextView tv_firstname, tv_lastname, tv_birthday, tv_gender, tv_mobile_number, tv_address, tv_medical_job, tv_company_name, tv_company_address, tv_company_number;
    ImageView id_front_image, id_back_image, iv_profile_image;
    List<String> list = new ArrayList<String>();
    private Dashboard currentActivity;
    private int userId;
    private Context currentContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        tv_firstname = view.findViewById(R.id.tv_firstname);
        tv_lastname = view.findViewById(R.id.tv_lastname);
        tv_birthday = view.findViewById(R.id.tv_birthday);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_mobile_number = view.findViewById(R.id.tv_contactnum);
        tv_address = view.findViewById(R.id.tv_address);
        tv_medical_job = view.findViewById(R.id.tv_medicaljob);
        tv_company_name = view.findViewById(R.id.tv_compname);
        tv_company_address = view.findViewById(R.id.tv_compaddress);
        tv_company_number = view.findViewById(R.id.tv_compnum);
        id_front_image = view.findViewById(R.id.iv_profile_id_front);
        id_back_image = view.findViewById(R.id.iv_profile_id_back);

        new getInfo().execute();


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.currentActivity = (Dashboard) getActivity();
//        this.userId = Integer.parseInt(currentActivity.getUserData().get(0)[0]);
    }




    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        this.currentContext = context;
    }



    public class getInfo extends AsyncTask<Void, Void, Void>{
        String firstname, lastname, birthday, gender, mobile_number, address, medical_job, company_name, company_address, company_number;
        String front_image, back_image, user_image;

        private String readFile() {
            File fileEvents = new File(getActivity().getApplicationContext().getFilesDir()+"/text/config");
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

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://163.44.242.10:3306/feqxsxpi_calsakay?characterEncoding=latin1","feqxsxpi_root", "UCC2021bsitKrazy");
                Statement statement = connection.createStatement();
                String sql = "SELECT * FROM calsakay_tbl_users WHERE id = " + readFile();
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()){
                    firstname = resultSet.getString("first_name");
                    lastname = resultSet.getString("last_name");
                    birthday = resultSet.getString("birthday");
                    gender = resultSet.getString("gender");
                    mobile_number = resultSet.getString("mobile_number");
                    address = resultSet.getNString("address");
                    medical_job = resultSet.getString("medical_job");
                    company_name = resultSet.getString("company_name");
                    company_address = resultSet.getNString("company_address");
                    company_number = resultSet.getString("company_number");
//                    front_image = resultSet.getString("front_image_name");
//                    back_image = resultSet.getString("back_image_name");
//                    user_image = resultSet.getString("user_image");
                }


            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

            tv_firstname.setText(firstname);
            tv_lastname.setText(lastname);
            tv_birthday.setText(birthday);
            tv_gender.setText(gender);
            tv_mobile_number.setText(mobile_number);
            tv_address.setText(address);
            tv_company_name.setText(company_name);
            tv_company_address.setText(company_address);
            tv_company_number.setText(company_number);
            tv_medical_job.setText(medical_job);
//
//            byte[] front_bytes = Base64.decode(front_image, Base64.DEFAULT);
//            Bitmap front_bitmap = BitmapFactory.decodeByteArray(front_bytes, 0, front_bytes.length);
//            byte[] back_bytes = Base64.decode(back_image, Base64.DEFAULT);
//            Bitmap back_bitmap = BitmapFactory.decodeByteArray(back_bytes, 0, back_bytes.length);
//            byte[] user_profile = Base64.decode(user_image, Base64.DEFAULT);
//            Bitmap user_bitmap = BitmapFactory.decodeByteArray(user_profile, 0, user_profile.length);
//
//            id_front_image.setImageBitmap(front_bitmap);
//            id_back_image.setImageBitmap(back_bitmap);
//            iv_profile_image.setImageBitmap(user_bitmap);

//            Log.d("front image", front_image);

            super.onPostExecute(unused);
        }
    }
}