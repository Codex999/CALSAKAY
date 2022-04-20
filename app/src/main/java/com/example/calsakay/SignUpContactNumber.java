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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpContactNumber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpContactNumber extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpContactNumber() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpContactNumber.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpContactNumber newInstance(String param1, String param2) {
        SignUpContactNumber fragment = new SignUpContactNumber();
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

    private TextView tv_contact;
    private Button btn_proceed;
    RequestQueue requestQueue;
    Signup signup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up_contact_number, container, false);
        tv_contact = rootView.findViewById(R.id.tv_sucn);
        btn_proceed = rootView.findViewById(R.id.btn_sucn);

        signup = (Signup) getActivity();
        requestQueue = Volley.newRequestQueue(getActivity());

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_contact.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Please enter your contact number", Toast.LENGTH_SHORT).show();
                }else if(!tv_contact.getText().toString().startsWith("09")){
                    Toast.makeText(getActivity(), "Contact must start with '09'", Toast.LENGTH_SHORT).show();
                }else if(tv_contact.getText().toString().length() != 11){
                    Toast.makeText(getActivity(), "Contact number must be exactly 11 digits", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(getActivity(), "TRUE", Toast.LENGTH_SHORT).show();
                    requestOtp();
                    signup.setContact_num(tv_contact.getText().toString());
                    VerifyContact verifyContact = new VerifyContact();
                    getFragmentManager().beginTransaction().replace(R.id.signupLayout, verifyContact).commit();
                }
            }
        });

        return rootView;
    }

    private void requestOtp(){
        String contactNum = tv_contact.getText().toString();
        String url = "https://www.ucc-bsit-2021.com/calsakay/calsakaywebapp/sms/index.php?number="+contactNum;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject json_response = jsonArray.getJSONObject(i);
                                String otp = json_response.getString("otp");
                                signup.setOtp(otp);
//                                Toast.makeText(getActivity(), otp, Toast.LENGTH_LONG).show();
                                Log.d("OTP:", otp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("ERROR: ", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}