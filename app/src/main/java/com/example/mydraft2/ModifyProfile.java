package com.example.mydraft2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by dikki on 03/09/2017.
 */

public class ModifyProfile extends AppCompatActivity
{
    private static final String TAG = "MODIFY PROFILE";
    EditText edtRelationFirstName ,edtRelationLastName, edtRelationAge ;
    Button btnRelationNextPage, btnRelationFinish;

    Button spnRelationOccupation,spnRelationInterest,spnRelationHobbeis;
    Button spnRelationReligion,spnRelationCountry;
    Button spnRelationKnowing,spnRelationCloseness,spnRelationcommFreq,spnRelationCommTopic;

    RadioButton rdtRelationMale, rdtRelationFemale,rdtRelationMarried ,rdtRelationUnMarried;
    Context appCnt;

    StringWrapper strWrpfirstName = new StringWrapper(),strWrphowKnowHim = new StringWrapper(), strWrphowClose = new StringWrapper();
    StringWrapper strWrplastName = new StringWrapper(), strWrpgender = new StringWrapper(), strWrpmarriedStatus = new StringWrapper();
    StringWrapper strWrpage = new StringWrapper()  ,strWrpoccupation = new StringWrapper(), strWrpinterest= new StringWrapper();
    StringWrapper strWrphobbies = new StringWrapper(),strWrpreligion = new StringWrapper() ,strWrpcountry = new StringWrapper();
    StringWrapper strWrpcommFrequency = new StringWrapper(), strWrpcommTopic = new StringWrapper();

    ModifyProfile()
    {
        appCnt=ModifyProfile.this;
    }

    String[] listOccupation,listInterest,listHobbies,listReligion,listCountry,listKnowing,listCloseness,listCommFreq ,listCommTopic;

    //Page one field
    StringWrapper firstName ;
    StringBuilder occupation = new StringBuilder();
    StringBuilder interest = new StringBuilder();
    StringBuilder hobbies = new StringBuilder();
    //Page two fields
    StringBuilder religion = new StringBuilder();
    StringBuilder country = new StringBuilder();
    StringBuilder knowing = new StringBuilder();

    StringBuilder closeness = new StringBuilder();
    StringBuilder commFreq = new StringBuilder();
    StringBuilder commTopic = new StringBuilder();


    final DbUtility dbUtility = new DbUtility();

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
         strWrpfirstName.setString(getIntent().getStringExtra("SelectedProfile"));
        Log.d(TAG," ProfileName is " + firstName);
        dbUtility.getProfileDetails(strWrpfirstName,strWrplastName,strWrpgender,strWrpmarriedStatus,
                strWrpage,strWrpoccupation,strWrpinterest,strWrphobbies,strWrpreligion,strWrpcountry,
                strWrphowKnowHim,strWrphowClose,strWrpcommFrequency,strWrpcommTopic);
        setContentView(R.layout.another_profile);
        edtRelationFirstName = (EditText) findViewById(R.id.edtRelationFirsName);
        edtRelationLastName = (EditText) findViewById(R.id.edtRelationLastName);
        edtRelationAge = (EditText) findViewById(R.id.edtRelationAge);

        edtRelationFirstName.setText(strWrpfirstName.getString());
        edtRelationLastName.setText(strWrplastName.getString());
        edtRelationAge.setText(strWrpage.getString());

        rdtRelationMale = (RadioButton) findViewById(R.id.radioRelationBtnMale);
        rdtRelationFemale = (RadioButton) findViewById(R.id.radioRelationBtnfemale);

        rdtRelationMarried = (RadioButton) findViewById(R.id.radioRelationBtnMarried);
        rdtRelationUnMarried = (RadioButton) findViewById(R.id.radioRelationBtnUnmarried);

        spnRelationOccupation = (Button) findViewById(R.id.spinRelationOccupation);
        spnRelationHobbeis = (Button) findViewById(R.id.spinRelationHobbies);
        spnRelationInterest = (Button) findViewById(R.id.spinRelationInterest);


        btnRelationNextPage = (Button) findViewById(R.id.btnRelationNext);

        listOccupation = getResources().getStringArray(R.array.occupation);
        spnRelationOccupation.setOnClickListener(new MySpinnerListener(appCnt,listOccupation,occupation,spnRelationOccupation));

        listInterest = getResources().getStringArray(R.array.interest);
        spnRelationInterest.setOnClickListener(new MySpinnerListener(appCnt,listInterest,interest, spnRelationInterest));

        listHobbies = getResources().getStringArray(R.array.hobbies);
        spnRelationHobbeis.setOnClickListener(new MySpinnerListener(appCnt,listHobbies,hobbies, spnRelationHobbeis));;

        btnRelationNextPage.setOnClickListener(new btnNextPageClickListener());

    }
class btnNextPageClickListener implements View.OnClickListener
{
    @Override
    public void onClick(View v) {
        if(!IsMandatoryFieldPopulated(true))//This check is for first page
        {
            Toast.makeText(getApplicationContext(),"Some Fields are missing, please fill those before moving to next page ",Toast.LENGTH_SHORT).show();
        }
        else
        {
            setContentView(R.layout.another_profile_page2);
            btnRelationFinish = (Button) findViewById(R.id.btnRelationFinish);

            spnRelationCountry = (Button) findViewById(R.id.spinRelationCountry);
            spnRelationReligion = (Button) findViewById(R.id.spinRelationReligion);
            spnRelationCloseness = (Button) findViewById(R.id.spinRelationCloseness);
            spnRelationcommFreq = (Button) findViewById(R.id.spinRelationCommunication);
            spnRelationCommTopic = (Button) findViewById(R.id.spinRelationCommTopic);
            spnRelationKnowing = (Button) findViewById(R.id.spinRelationKnowing);

            listCountry = getResources().getStringArray(R.array.Country);
            spnRelationCountry.setOnClickListener(new MySpinnerListener(appCnt, listCountry, country, spnRelationCountry));

            listReligion = getResources().getStringArray(R.array.Religion);
            spnRelationReligion.setOnClickListener(new MySpinnerListener(appCnt, listReligion, religion, spnRelationReligion));

            listCloseness = getResources().getStringArray(R.array.Closeness);
            spnRelationCloseness.setOnClickListener(new MySpinnerListener(appCnt, listCloseness, closeness, spnRelationCloseness));
            ;

            listKnowing = getResources().getStringArray(R.array.Knowing);
            spnRelationKnowing.setOnClickListener(new MySpinnerListener(appCnt, listKnowing, knowing, spnRelationKnowing));

            listCommFreq = getResources().getStringArray(R.array.Communication);
            spnRelationcommFreq.setOnClickListener(new MySpinnerListener(appCnt, listCommFreq, commFreq, spnRelationcommFreq));
            ;

            listCommTopic = getResources().getStringArray(R.array.Communication_Topic);
            spnRelationCommTopic.setOnClickListener(new MySpinnerListener(appCnt, listCommTopic, commTopic, spnRelationCommTopic));
            ;

            Log.d("TAG", "Finish Called");
            btnRelationFinish.setOnClickListener(new btnFinishClickListener());
        }
    }
}

    private boolean IsMandatoryFieldPopulated(boolean pageOne ) {

        if (pageOne)
        {
            String firstName ,lastName , gender, marriedStatus ;
            String strAge;
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
            strAge = edtRelationAge.getText().toString();

            //Checking value from page one
            if(firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || marriedStatus.isEmpty() || strAge.isEmpty()
                    || interest.length() ==0 || hobbies.length() ==0 || occupation.length() ==0)
                return false;
            else
                return true;

        }
        else
        {
            if(knowing.length() ==0 || closeness.length() ==0 || commFreq.length() ==0 || commTopic.length() ==0 ||
                    country.length() ==0 || religion.length()==0)
                return false;
            else
                return true;
        }

    }

class btnFinishClickListener implements View.OnClickListener
{
    String firstName ,lastName , gender, marriedStatus ;

    Integer age;
    @Override
    public void onClick(View v) {

        if(!IsMandatoryFieldPopulated(false))//firstPage = false ,This check is for second page
        {
            Toast.makeText(getApplicationContext(),"Some Fields are missing, please fill those before finish ",Toast.LENGTH_SHORT).show();
        }
        else {

            Log.d("Inside btnFinishClick", "onClick called");
            firstName = edtRelationFirstName.getText().toString();
            lastName = edtRelationLastName.getText().toString();

            if (rdtRelationMale.isChecked())
                gender = "Male";
            else
                gender = "Female";

            if (rdtRelationMarried.isChecked())
                marriedStatus = "Married";
            else
                marriedStatus = "UnMarried";
            age = Integer.valueOf(edtRelationAge.getText().toString());

            dbUtility.insertProfileDetails(firstName, lastName, gender, marriedStatus, age, occupation.toString(),
                    interest.toString(), hobbies.toString(), religion.toString(), country.toString(),
                    knowing.toString(), closeness.toString(), commFreq.toString(), commTopic.toString());
            Toast.makeText(getApplicationContext(), "Profile " + firstName + " Created SucessFully", Toast.LENGTH_SHORT);
            Log.d("Inside btnFinishClick", "onClick End");
            Intent intent = new Intent(getApplicationContext(), FirstPage.class);
            startActivity(intent);
        }
    }
}
}
