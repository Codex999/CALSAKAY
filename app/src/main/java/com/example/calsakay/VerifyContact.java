package com.example.calsakay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyContact extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerifyContact() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerifyContact.
     */
    // TODO: Rename and change types and number of parameters
    public static VerifyContact newInstance(String param1, String param2) {
        VerifyContact fragment = new VerifyContact();
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

    private TextView tv_num;
    private Button btn_send_num;
    Signup signup;
    String digit = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_contact, container, false);
        signup = (Signup) getActivity();
        tv_num = view.findViewById(R.id.tv_digits);
        btn_send_num = view.findViewById(R.id.btn_ver);

        btn_send_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                digit = signup.getOtp();
//                Toast.makeText(getActivity(), digit, Toast.LENGTH_SHORT).show();
                if (!tv_num.getText().toString().matches(digit)){
                    Toast.makeText(getActivity(), "Wrong verification number", Toast.LENGTH_SHORT).show();
                }else if(tv_num.getText().toString().matches("")){
                    Toast.makeText(getActivity(), "Verification number is required, please enter the 6 digit number that we sent to your device.", Toast.LENGTH_SHORT).show();
                }else{
//                    Toast.makeText(getActivity(), "match", Toast.LENGTH_SHORT).show();
                  EmailFragment emailFragment = new EmailFragment();
                  getFragmentManager().beginTransaction().replace(R.id.signupLayout, emailFragment).commit();

                }

            }
        });
        return view;
    }
}