package com.example.calsakay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmailFragment newInstance(String param1, String param2) {
        EmailFragment fragment = new EmailFragment();
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

    private EditText et_email;
    private Button btn_proceed;
    private RequestQueue requestQueue;
    private Signup signup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_email, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        signup = (Signup) getActivity();
        et_email = view.findViewById(R.id.et_sue);
        btn_proceed = view.findViewById(R.id.btn_sue);
        btn_proceed.setEnabled(false);

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()){
                    btn_proceed.setEnabled(true);
                }else{
                    btn_proceed.setEnabled(false);
                    et_email.setError("Invalid Email Address format");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOTP();
                signup.setEmail(et_email.getText().toString());
                VerifyEmail verifyEmail = new VerifyEmail();
                getFragmentManager().beginTransaction().replace(R.id.signupLayout, verifyEmail).commit();

            }
        });
        return view;
    }

    public void getOTP(){
        String email = et_email.getText().toString();
        String url = "https://www.ucc-bsit-2021.com/calsakay/calsakaywebapp/mail/index.php?email="+email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("response");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject json_response = jsonArray.getJSONObject(i);
                                String otp = json_response.getString("otp");
                                signup.setEmailOTP(otp);
//                                Toast.makeText(getActivity(), otp, Toast.LENGTH_LONG).show();
                                Log.d("EMAIL OTP:", otp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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