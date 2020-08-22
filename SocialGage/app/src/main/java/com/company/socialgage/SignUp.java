package com.company.socialgage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class SignUp extends AppCompatActivity {
    public LinearLayout signUp,alreadyUser;
    public ImageButton back,help;
    public EditText email,password;
    Spinner countriesSpinner;
    String countries [] ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUp = findViewById(R.id.signuP_button);
        alreadyUser = findViewById(R.id.alreadyUser);
        back = findViewById(R.id.back);
        help= findViewById(R.id.help);
        email = findViewById(R.id.emailEdt);
        password =findViewById(R.id.passwordEdt);
        countriesSpinner = findViewById(R.id.countries);
        countries = getResources().getStringArray(R.array.countries);

        imageButtonClick();
        setCountriesSpinner(countriesSpinner,countries);

    }
    public void setCountriesSpinner(Spinner spinner,String [] countries){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,countries);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
    public  void imageButtonClick(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
