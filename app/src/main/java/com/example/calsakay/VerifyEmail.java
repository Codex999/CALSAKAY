package com.example.calsakay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyEmail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyEmail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerifyEmail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerifyEmail.
     */
    // TODO: Rename and change types and number of parameters
    public static VerifyEmail newInstance(String param1, String param2) {
        VerifyEmail fragment = new VerifyEmail();
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

    private TextView tv_ver_code;
    private Button btn_proceed;
    Signup signup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_email, container, false);
        tv_ver_code = view.findViewById(R.id.tv_digits_email);
        btn_proceed = view.findViewById(R.id.btn_ver_email);
        signup = (Signup) getActivity();

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!tv_ver_code.getText().toString().matches(signup.getEmailOTP())){
                    Toast.makeText(getActivity(), "Please check the digits that you input.", Toast.LENGTH_SHORT).show();
                }else if(tv_ver_code.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "The otp field is empty, please input the otp that we sent to your email", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(getActivity(), "TRUE", Toast.LENGTH_SHORT).show();
                    SignUp2Fragment signUp2Fragment = new SignUp2Fragment();
                    getFragmentManager().beginTransaction().replace(R.id.signupLayout, signUp2Fragment).commit();

                }
            }
        });

        return view;
    }


}