package com.example.calsakay;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUp1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp1Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUp1Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp1Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp1Fragment newInstance(String param1, String param2) {
        SignUp1Fragment fragment = new SignUp1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnNext, btnCancel;
    private EditText et_firstName, et_lastName, et_address;
    private TextView tv_date;

    private DatePickerDialog datePickerDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up1, container, false);
        Signup signup = (Signup) getActivity();
        initDatePicker();
        radioGroup = view.findViewById(R.id.grdb_gender);

        tv_date = view.findViewById(R.id.tv_datePicker);
        tv_date.setText(getTodaysDate());

        btnNext = (Button) view.findViewById(R.id.btn_signup1);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel_signup);

        et_firstName = (EditText) view.findViewById(R.id.et_fname);
        et_lastName = (EditText) view.findViewById(R.id.et_lname);

        et_address = (EditText) view.findViewById(R.id.et_address);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = getActivity().findViewById(radioId);

                if (et_firstName.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Firstname is required", Toast.LENGTH_LONG).show();
                }else if (et_lastName.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Lastname is required", Toast.LENGTH_LONG).show();
                }else if(et_address.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Address is required", Toast.LENGTH_SHORT).show();
                }else{
                    signup.setFname(et_firstName.getText().toString());
                    signup.setLname(et_lastName.getText().toString());
                    signup.setGender(radioButton.getText().toString());
                    signup.setBirthday(tv_date.getText().toString());
                    signup.setAddress(et_address.getText().toString());
//                    SignUp2Fragment signUp2Fragment = new SignUp2Fragment();
                    SignUpContactNumber signUpContactNumber = new SignUpContactNumber();
                    getFragmentManager().beginTransaction().replace(R.id.signupLayout, signUpContactNumber).commit();
                }
            }
        });



        return view;
    }

    public String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);


    }
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                tv_date.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + "-" + day + "-" + year;
    }

    private String getMonthFormat(int month){
        if (month == 1)
            return "01";
        if (month == 2)
            return "02";
        if (month == 3)
            return "03";
        if (month == 4)
            return "04";
        if (month == 5)
            return "05";
        if (month == 6)
            return "06";
        if (month == 7)
            return "07";
        if (month == 8)
            return "08";
        if (month == 9)
            return "09";
        if (month == 10)
            return "10";
        if (month == 11)
            return "11";
        if (month == 12)
            return "12";

        return "01";
    }

    public void openDatePicker(){
        datePickerDialog.show();
    }
}