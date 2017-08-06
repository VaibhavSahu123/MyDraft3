package com.example.mydraft2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by dikki on 03/12/2016.
 */

public class SignUp extends AppCompatActivity  {

    Button btnSave ;
    EditText edtFirstName ,edtLastName ,edtAge, edtSelectedUserName, edtSelectedPassword ;
    RadioButton rdtMale, rdtFemale ;
    Button spnHobbeis,spnInterest,spnReligion,spnCountry,spnOccupation;
    Context appCnt;

    String[] listOccupation,listInterest,listHobbies,listReligion,listCountry,listKnowing,listCloseness,listCommFreq ,listCommTopic;

    StringBuilder occupation = new StringBuilder();
    StringBuilder interest = new StringBuilder();
    StringBuilder hobbies = new StringBuilder();

    StringBuilder religion = new StringBuilder();
    StringBuilder country = new StringBuilder();

    String firstName ,lastName,gender,strAge,userName,password;

    final DbUtility dbUtility = new DbUtility();
    SignUp(){appCnt = SignUp.this;}
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_form);

        edtFirstName = (EditText) findViewById(R.id.edtFirsName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtSelectedUserName = (EditText) findViewById(R.id.edtSelectedUser);
        edtSelectedPassword = (EditText) findViewById(R.id.edtSelectedPwd);
        edtAge = (EditText) findViewById(R.id.edtAge);

        rdtMale = (RadioButton) findViewById(R.id.radioBtnMale);
        rdtFemale = (RadioButton) findViewById(R.id.radioBtnfemale);

        spnOccupation = (Button) findViewById(R.id.spinOccupation);
        spnHobbeis = (Button) findViewById(R.id.spinHobbies);
        spnInterest = (Button) findViewById(R.id.spinInterest);
        spnCountry = (Button) findViewById(R.id.spinCountry);
        spnReligion = (Button) findViewById(R.id.spinReligion);

        listOccupation = getResources().getStringArray(R.array.occupation);
        spnOccupation.setOnClickListener(new MySpinnerListener(appCnt, listOccupation, occupation, spnOccupation));


        listInterest = getResources().getStringArray(R.array.interest);
        spnInterest.setOnClickListener(new MySpinnerListener(appCnt, listInterest, interest, spnInterest));


        listHobbies = getResources().getStringArray(R.array.hobbies);
        spnHobbeis.setOnClickListener(new MySpinnerListener(appCnt, listHobbies, hobbies, spnHobbeis));


        listCountry = getResources().getStringArray(R.array.Country);
        spnCountry.setOnClickListener(new MySpinnerListener(appCnt, listCountry, country, spnCountry));


        listReligion = getResources().getStringArray(R.array.Religion);
        spnReligion.setOnClickListener(new MySpinnerListener(appCnt, listReligion, religion, spnReligion));

        btnSave = (Button) findViewById(R.id.btnCreateAcccount);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!IsMandatoryFieldPopulated())//This check is for first page
                {
                    Toast.makeText(getApplicationContext(),"Some Fields are missing, please provide those details for account creation",Toast.LENGTH_SHORT).show();
                }
                else {

                    firstName = edtFirstName.getText().toString();
                    lastName = edtLastName.getText().toString();
                    strAge= edtAge.getText().toString();
                    //occupation = spnOccupation.getSelectedItem().toString();

                    if (rdtMale.isChecked())
                        gender = "Male";
                    else
                        gender = "Female";

                    userName = edtSelectedUserName.getText().toString();
                    password = edtSelectedPassword.getText().toString();


                    dbUtility.insertUserDetails(firstName, lastName, occupation.toString(), gender, userName, password);

                    Toast.makeText(SignUp.this, "User Account Created Successfully ", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private boolean IsMandatoryFieldPopulated()
    {
        firstName = edtFirstName.getText().toString();
        lastName = edtLastName.getText().toString();
        strAge= edtAge.getText().toString();
        //occupation = spnOccupation.getSelectedItem().toString();

        if (rdtMale.isChecked())
            gender = "Male";
        else
            gender = "Female";

        userName = edtSelectedUserName.getText().toString();
        password = edtSelectedPassword.getText().toString();

        if(firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty()||userName.isEmpty() ||password.isEmpty()
                ||strAge.isEmpty() || spnOccupation.length()==0 || spnReligion.length()==0
                ||spnInterest.length()==0 || spnHobbeis.length()==0  || spnCountry.length()==0)
            return false;
        else
            return true;

    }
}


