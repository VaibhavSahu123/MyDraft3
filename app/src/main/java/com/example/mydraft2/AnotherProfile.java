package com.example.mydraft2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by dikki on 04/01/2017.
 */

public class AnotherProfile  extends AppCompatActivity
{
    EditText edtRelationFirstName ,edtRelationLastName, edtRelationAge ;
    Button btnRelationNextPage, btnRelationFinish;
    RadioButton rdtRelationMale, rdtRelationFemale,rdtRelationMarried ,rdtRelationUnMarried;
    Spinner spnRelationOccupation,spnRelationHobbeis,spnRelationInterest,spnRelationReligion,spnRelationCountry;
    Spinner spnRelationKnowing,spnRelationCloseness,spnRelationcommFreq,spnRelationCommTopic;
    final DbUtility dbUtility = new DbUtility();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.another_profile);
        edtRelationFirstName = (EditText) findViewById(R.id.edtRelationFirsName);
        edtRelationLastName = (EditText) findViewById(R.id.edtRelationLastName);
        edtRelationAge = (EditText) findViewById(R.id.edtRelationAge);
        
        rdtRelationMale = (RadioButton) findViewById(R.id.radioRelationBtnMale);
        rdtRelationFemale = (RadioButton) findViewById(R.id.radioRelationBtnfemale);

        rdtRelationMarried = (RadioButton) findViewById(R.id.radioRelationBtnMarried);
        rdtRelationUnMarried = (RadioButton) findViewById(R.id.radioRelationBtnUnmarried);

        spnRelationOccupation = (Spinner) findViewById(R.id.spinRelationOccupation);
        spnRelationHobbeis = (Spinner) findViewById(R.id.spinRelationHobbies);
        spnRelationInterest = (Spinner) findViewById(R.id.spinRelationInterest);


        btnRelationNextPage = (Button) findViewById(R.id.btnRelationNext);

        ArrayAdapter<CharSequence> adptOccupation = ArrayAdapter.createFromResource(this,R.array.occupation,android.R.layout.simple_spinner_item);
        adptOccupation.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnRelationOccupation.setAdapter(adptOccupation);

        ArrayAdapter<CharSequence> adptInterest = ArrayAdapter.createFromResource(this,R.array.interest,android.R.layout.simple_spinner_item);
        adptInterest.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnRelationInterest.setAdapter(adptInterest);

        ArrayAdapter<CharSequence> adptHobbbeis = ArrayAdapter.createFromResource(this,R.array.hobbies,android.R.layout.simple_spinner_item);
        adptHobbbeis.setDropDownViewResource(android.R.layout.select_dialog_item);
        spnRelationHobbeis.setAdapter(adptHobbbeis);



        btnRelationNextPage.setOnClickListener(new btnNextPageClickListener());

    }

    class btnNextPageClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.another_profile_page2);
            btnRelationFinish = (Button) findViewById(R.id.btnRelationFinish);

            spnRelationCountry = (Spinner) findViewById(R.id.spinRelationCountry);
            spnRelationReligion = (Spinner) findViewById(R.id.spinRelationReligion);
            spnRelationCloseness = (Spinner) findViewById(R.id.spinRelationCloseness);
            spnRelationcommFreq = (Spinner) findViewById(R.id.spinRelationCommunication);
            spnRelationCommTopic = (Spinner) findViewById(R.id.spinRelationCommTopic);
            spnRelationKnowing = (Spinner) findViewById(R.id.spinRelationKnowing);

            ArrayAdapter<CharSequence> adptCountry = ArrayAdapter.createFromResource(AnotherProfile.this,R.array.Country,android.R.layout.simple_spinner_item);
            adptCountry.setDropDownViewResource(android.R.layout.select_dialog_item);
            spnRelationCountry.setAdapter(adptCountry);

            ArrayAdapter<CharSequence> adptReligion = ArrayAdapter.createFromResource(AnotherProfile.this,R.array.Religion,android.R.layout.simple_spinner_item);
            adptReligion.setDropDownViewResource(android.R.layout.select_dialog_item);
            spnRelationReligion.setAdapter(adptReligion);

            ArrayAdapter<CharSequence> adptCloseness = ArrayAdapter.createFromResource(AnotherProfile.this,R.array.Closeness,android.R.layout.simple_spinner_item);
            adptCloseness.setDropDownViewResource(android.R.layout.select_dialog_item);
            spnRelationCloseness.setAdapter(adptCloseness);

            ArrayAdapter<CharSequence> adptKnowing = ArrayAdapter.createFromResource(AnotherProfile.this,R.array.Knowing,android.R.layout.simple_spinner_item);
            adptKnowing.setDropDownViewResource(android.R.layout.select_dialog_item);
            spnRelationKnowing.setAdapter(adptKnowing);

            ArrayAdapter<CharSequence> adptCommunication = ArrayAdapter.createFromResource(AnotherProfile.this,R.array.Communication,android.R.layout.simple_spinner_item);
            adptCommunication.setDropDownViewResource(android.R.layout.select_dialog_item);
            spnRelationcommFreq.setAdapter(adptCommunication);

            ArrayAdapter<CharSequence> adptCommTopic = ArrayAdapter.createFromResource(AnotherProfile.this,R.array.Communication_Topic,android.R.layout.simple_spinner_item);
            adptCommTopic.setDropDownViewResource(android.R.layout.select_dialog_item);
            spnRelationCommTopic.setAdapter(adptCommTopic);
            Log.d("TAG","Finish Called");
            btnRelationFinish.setOnClickListener(new btnFinishClickListener());
        }
    }

    class btnFinishClickListener implements View.OnClickListener
    {
        String firstName ,lastName , gender, marriedStatus, occupation, interest , hobbies ;
        String religion , country , Knowing , Closeness , commFreq ,commTopic ;
        Integer age;
        @Override
        public void onClick(View v) {
            Log.d("Inside btnFinishClick", "onClick called");
            firstName = edtRelationFirstName.getText().toString();
            lastName = edtRelationLastName.getText().toString();
            
            if(rdtRelationMale.isChecked())
                gender ="Male";
            else
                gender ="Female";
            
            if (rdtRelationMarried.isChecked())
                marriedStatus="Married";
            else
                marriedStatus= "UnMarried";
            age = Integer.valueOf(edtRelationAge.getText().toString());
            
            occupation = spnRelationOccupation.getSelectedItem().toString();
            interest   = spnRelationInterest.getSelectedItem().toString();
            hobbies    = spnRelationHobbeis.getSelectedItem().toString();
            religion   = spnRelationReligion.getSelectedItem().toString();
            country    = spnRelationCountry.getSelectedItem().toString();
            Knowing    = spnRelationKnowing.getSelectedItem().toString();
            Closeness  = spnRelationCloseness.getSelectedItem().toString();
            commFreq   = spnRelationcommFreq.getSelectedItem().toString();
            commTopic  = spnRelationCommTopic.getSelectedItem().toString();
            dbUtility.insertProfileDetails(firstName,lastName,gender,marriedStatus,age,occupation,interest,hobbies,religion,country,Knowing,Closeness,commFreq,commTopic);
            Toast.makeText(getApplicationContext(),"Profile "+firstName+" Created SucessFully",Toast.LENGTH_SHORT);
            Log.d("Inside btnFinishClick", "onClick End");
            Intent intent = new Intent(getApplicationContext(),FirstPage.class);
            startActivity(intent);
        }
    }
}
