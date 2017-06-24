package com.example.mydraft2;

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

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

/**
 * Created by dikki on 03/12/2016.
 */

public class SignUp extends AppCompatActivity  {

    Button btnSave ;
    EditText edtFirstName ,edtLastName , edtSelectedUserName, edtSelectedPassword ;
    RadioButton rdtMale, rdtFemale ;
    Spinner spnHobbeis,spnInterest,spnReligion,spnCountry;
    MaterialBetterSpinner spnOccupation;
    final DbUtility dbUtility = new DbUtility();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_form);

        edtFirstName = (EditText) findViewById(R.id.edtFirsName);
        edtLastName = (EditText) findViewById(R.id.edtLastName);
        edtSelectedUserName = (EditText) findViewById(R.id.edtSelectedUser);
        edtSelectedPassword = (EditText) findViewById(R.id.edtSelectedPwd);
        rdtMale = (RadioButton) findViewById(R.id.radioBtnMale);
        rdtFemale = (RadioButton) findViewById(R.id.radioBtnfemale);

        spnOccupation = (MaterialBetterSpinner) findViewById(R.id.spinOccupation);
        spnHobbeis = (Spinner) findViewById(R.id.spinHobbies);
        spnInterest = (Spinner) findViewById(R.id.spinInterest);
        spnCountry = (Spinner) findViewById(R.id.spinCountry);
        spnReligion = (Spinner) findViewById(R.id.spinReligion);

       ArrayAdapter<CharSequence> adptOccupation = ArrayAdapter.createFromResource(this,R.array.occupation,android.R.layout.simple_spinner_item);
       /* ArrayAdapter<CharSequence> adptOccupation = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item)
        {
            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }
        };*/
        adptOccupation.setDropDownViewResource(android.R.layout.select_dialog_item);
       // adptOccupation.addAll(getResources().getStringArray(R.array.occupation));

        //spnOccupation.setPrompt("Select occupation");
        spnOccupation.setAdapter(adptOccupation);
       // spnOccupation.setSelection(adptOccupation.getCount());

        ArrayAdapter<CharSequence> adptInterest = ArrayAdapter.createFromResource(this,R.array.interest,android.R.layout.simple_spinner_item);
        adptInterest.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnInterest.setAdapter(adptInterest);

        ArrayAdapter<CharSequence> adptHobbbeis = ArrayAdapter.createFromResource(this,R.array.hobbies,android.R.layout.simple_spinner_item);
        adptHobbbeis.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnHobbeis.setAdapter(adptHobbbeis);

        ArrayAdapter<CharSequence> adptCountry = ArrayAdapter.createFromResource(this,R.array.Country,android.R.layout.simple_spinner_item);
        adptCountry.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnCountry.setAdapter(adptCountry);

        ArrayAdapter<CharSequence> adptReligion = ArrayAdapter.createFromResource(this,R.array.Religion,android.R.layout.simple_spinner_item);
        adptReligion.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnReligion.setAdapter(adptReligion);

        btnSave = (Button) findViewById(R.id.btnCreateAcccount);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName;
                String lastName;
                Editable occupation;
                String gender;
                String userName;
                String password;
                firstName = edtFirstName.getText().toString();
                lastName = edtLastName.getText().toString();
               occupation = spnOccupation.getText();

                if(rdtMale.isChecked())
                    gender ="Male";
                else
                    gender ="Female";

                userName = edtSelectedUserName.getText().toString();
                password = edtSelectedPassword.getText().toString();

                dbUtility.insertUserDetails(firstName,lastName,occupation.toString(),gender,userName,password);

                Toast.makeText(SignUp.this, "User Account Created Successfully ", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext() ,MainActivity.class);
                startActivity(intent);

            }
        });

    }
}


