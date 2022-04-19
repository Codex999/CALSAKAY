package com.example.calsakay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUp2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp2Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUp2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp2Fragment newInstance(String param1, String param2) {
        SignUp2Fragment fragment = new SignUp2Fragment();
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


    private Button btn_proceed, btn_back;
    private EditText et_comp_name, et_comp_num, et_comp_address, et_medical_job;
    private String compname, compnum, compaddress, str_medical_job;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up2, container, false);
        Signup signup = (Signup) getActivity();
        Bundle bundle = new Bundle();

        btn_proceed = view.findViewById(R.id.btn_signup2);
        btn_back = view.findViewById(R.id.btn_signup2_back);
        et_medical_job = view.findViewById(R.id.et_medical_job);
        et_comp_name = view.findViewById(R.id.company_name);
        et_comp_num = view.findViewById(R.id.company_num);
        et_comp_address = view.findViewById(R.id.company_address);

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_medical_job.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Medical job is required", Toast.LENGTH_SHORT).show();
                }else if (et_comp_name.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Company name is required.", Toast.LENGTH_SHORT).show();
                }else if(et_comp_address.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Company number is required.", Toast.LENGTH_SHORT).show();
                }else if(et_comp_num.getText().toString().length() != 11){
                    Toast.makeText(getActivity(), "Please check the company number, it must contain 11 digits.", Toast.LENGTH_SHORT).show();
                }else if(!et_comp_num.getText().toString().startsWith("09")){
                    Toast.makeText(getActivity(), "Contact number must start at 09", Toast.LENGTH_SHORT).show();
                }else{
                    signup.setMedical_job(et_medical_job.getText().toString());
                    signup.setCompany_name(et_comp_name.getText().toString());
                    signup.setCompany_address(et_comp_address.getText().toString());
                    signup.setCompany_number(et_comp_num.getText().toString());

                    SignUp3Fragment signUp3Fragment = new SignUp3Fragment();
                    signUp3Fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.signupLayout, signUp3Fragment).commit();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp1Fragment signUp1Fragment = new SignUp1Fragment();
                getFragmentManager().beginTransaction().replace(R.id.signupLayout, signUp1Fragment).commit();
            }
        });

        return view;
    }
}