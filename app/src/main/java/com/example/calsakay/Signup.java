package com.example.calsakay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Signup extends AppCompatActivity {

    private String fname;
    private String lname;
    private String gender;
    private String birthday;
    private String email;
    private String contact_num;
    private String address;
    private String frontImageId, backImageId;
    private String medical_job, company_name, company_address, company_number;
    private String profile_picture;
    private String username, password;
    private String otp;
    private String emailOTP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        showFragment();
    }

    private void showFragment(){
        SignUp1Fragment signUp1Fragment = new SignUp1Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.signupLayout, signUp1Fragment)
                .commit();
    }
    public String getFname(){
        return fname;
    }
    public void setFname(String fname){
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_num() {
        return contact_num;
    }

    public void setContact_num(String contact_num) {
        this.contact_num = contact_num;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFrontImageId() {
        return frontImageId;
    }

    public void setFrontImageId(String frontImageId) {
        this.frontImageId = frontImageId;
    }

    public String getBackImageId() {
        return backImageId;
    }

    public void setBackImageId(String backImageId) {
        this.backImageId = backImageId;
    }

    public String getMedical_job() {
        return medical_job;
    }

    public void setMedical_job(String medical_job) {
        this.medical_job = medical_job;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_number() {
        return company_number;
    }

    public void setCompany_number(String company_number) {
        this.company_number = company_number;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOtp() { return otp; }

    public void setOtp(String otp) { this.otp = otp; }

    public String getEmailOTP() { return emailOTP; }

    public void setEmailOTP(String emailOTP) { this.emailOTP = emailOTP; }
}