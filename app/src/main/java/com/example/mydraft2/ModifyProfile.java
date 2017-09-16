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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    ModifyProfile()
    {
        appCnt=ModifyProfile.this;
    }

    String[] listOccupation,listInterest,listHobbies,listReligion,listCountry,listKnowing,listCloseness,listCommFreq ,listCommTopic;

    //Page one field
    StringBuilder firstName = new StringBuilder();
    StringBuilder lastName = new StringBuilder();
    StringBuilder gender = new StringBuilder();
    StringBuilder marriedStatus = new StringBuilder() ;
    StringBuilder strAge = new StringBuilder() ;
    
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
         firstName.append(getIntent().getStringExtra("SelectedProfile"));
        Log.d(TAG," ProfileName is " + firstName);
        dbUtility.getProfileDetails(firstName,lastName,gender,marriedStatus,
                strAge,occupation,interest,hobbies,religion,country,
                knowing,closeness,commFreq,commTopic);
        setContentView(R.layout.another_profile);

        edtRelationFirstName = (EditText) findViewById(R.id.edtRelationFirsName);
        edtRelationLastName = (EditText) findViewById(R.id.edtRelationLastName);
        edtRelationAge = (EditText) findViewById(R.id.edtRelationAge);

        edtRelationFirstName.setText(firstName.toString());
        edtRelationLastName.setText(lastName.toString());
        edtRelationAge.setText(strAge.toString());

        rdtRelationFemale = (RadioButton) findViewById(R.id.radioRelationBtnfemale);
        rdtRelationMale = (RadioButton) findViewById(R.id.radioRelationBtnMale);
        rdtRelationMarried = (RadioButton) findViewById(R.id.radioRelationBtnMarried);
        rdtRelationUnMarried = (RadioButton) findViewById(R.id.radioRelationBtnUnmarried);

        if(gender.toString().equalsIgnoreCase("male"))
        {
            rdtRelationMale.setChecked(true);
        }
        else
        {
            rdtRelationFemale.setChecked(true);
        }

        if(marriedStatus.toString().equalsIgnoreCase("female"))
        {
            rdtRelationMarried.setChecked(true);
        }
        else
        {
            rdtRelationUnMarried.setChecked(true);
        }


        spnRelationOccupation = (Button) findViewById(R.id.spinRelationOccupation);
        spnRelationHobbeis = (Button) findViewById(R.id.spinRelationHobbies);
        spnRelationInterest = (Button) findViewById(R.id.spinRelationInterest);


        btnRelationNextPage = (Button) findViewById(R.id.btnRelationNext);

        listOccupation = getResources().getStringArray(R.array.occupation);
        boolean [] checkedItemForOccupation = compareIfItemChecked(listOccupation,occupation);
        //occupation.append(strWrpoccupation.getString());
        spnRelationOccupation.setOnClickListener(new MySpinnerListener(appCnt,listOccupation,occupation,spnRelationOccupation,checkedItemForOccupation ));

        listInterest = getResources().getStringArray(R.array.interest);
        boolean [] checkedItemForInterest = compareIfItemChecked(listInterest,interest);
        //interest.append(strWrpinterest.getString());
        spnRelationInterest.setOnClickListener(new MySpinnerListener(appCnt,listInterest,interest, spnRelationInterest,checkedItemForInterest));

        listHobbies = getResources().getStringArray(R.array.hobbies);
        boolean [] checkedItemForHobbies = compareIfItemChecked(listHobbies,hobbies);
        //hobbies.append(strWrphobbies.getString());
        spnRelationHobbeis.setOnClickListener(new MySpinnerListener(appCnt,listHobbies,hobbies, spnRelationHobbeis,checkedItemForHobbies));;

        btnRelationNextPage.setOnClickListener(new btnNextPageClickListener());

    }

    private boolean[] compareIfItemChecked(String[] listOccupation, StringBuilder strSelection)
    {
        boolean [] result = new boolean[listOccupation.length];//Default all element would be false
        List<String> ItemSelected = new ArrayList<String>(Arrays.asList(strSelection.toString().split("\\s*,\\s*")));
        for(String test : ItemSelected)
        {
            Log.d(TAG, "compareIfItemChecked: "+ test);

        }
        Log.d(TAG, "compareIfItemChecked: "+ strSelection.toString());

        //Compare to get boolean array of checked item
        for(int i =0 ; i < listOccupation.length ; i++) {
            for (int j = 0; j < ItemSelected.size(); j++) {
                if (listOccupation[i].equalsIgnoreCase(ItemSelected.get(j))) {
                    result[i] = true;
                    break;
                }
            }
        }

        for(int i =0 ; i < result.length;i++ )
            Log.d(TAG, "compareIfItemChecked: "+ result[i]);
        return result;
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
            boolean [] checkedItemForCountry = compareIfItemChecked(listCountry,country);
            spnRelationCountry.setOnClickListener(new MySpinnerListener(appCnt, listCountry, country, spnRelationCountry,checkedItemForCountry));

            listReligion = getResources().getStringArray(R.array.Religion);
            boolean [] checkedItemForReligion = compareIfItemChecked(listReligion,religion);
            spnRelationReligion.setOnClickListener(new MySpinnerListener(appCnt, listReligion, religion, spnRelationReligion,checkedItemForReligion));

            listCloseness = getResources().getStringArray(R.array.Closeness);
            boolean [] checkedItemForCloseness = compareIfItemChecked(listCloseness,closeness);
            spnRelationCloseness.setOnClickListener(new MySpinnerListener(appCnt, listCloseness, closeness, spnRelationCloseness,checkedItemForCloseness));
            ;

            listKnowing = getResources().getStringArray(R.array.Knowing);
            boolean [] checkedItemForKnowing = compareIfItemChecked(listKnowing,knowing);
            spnRelationKnowing.setOnClickListener(new MySpinnerListener(appCnt, listKnowing, knowing, spnRelationKnowing,checkedItemForKnowing));

            listCommFreq = getResources().getStringArray(R.array.Communication);
            boolean [] checkedItemForCommFreq = compareIfItemChecked(listCommFreq,commFreq );
            spnRelationcommFreq.setOnClickListener(new MySpinnerListener(appCnt, listCommFreq, commFreq, spnRelationcommFreq,checkedItemForCommFreq));
            ;

            listCommTopic = getResources().getStringArray(R.array.Communication_Topic);
            boolean [] checkedItemForCommTopic = compareIfItemChecked(listCommTopic,commTopic );
            spnRelationCommTopic.setOnClickListener(new MySpinnerListener(appCnt, listCommTopic, commTopic, spnRelationCommTopic,checkedItemForCommTopic));
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
            if(firstName.isEmpty() || lastName.isEmpty() ||
                    gender.isEmpty() || marriedStatus.isEmpty() ||
                    strAge.isEmpty() || interest.toString().isEmpty() ||
                    hobbies.toString().isEmpty()|| occupation.toString().isEmpty())
                return false;
            else
                return true;

        }
        else
        {
            if(knowing.toString().isEmpty() || closeness.toString().isEmpty() || commFreq.toString().isEmpty()
                    || commTopic.toString().isEmpty() || country.toString().isEmpty() || religion.toString().isEmpty())
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


            dbUtility.updateProfileDetails(firstName, lastName, gender, marriedStatus, age, occupation.toString(),
                    interest.toString(), hobbies.toString(), religion.toString(), country.toString(),
                    knowing.toString(), closeness.toString(), commFreq.toString(), commTopic.toString());

            Toast.makeText(getApplicationContext(), "Profile " + firstName + " Updated SuccessFully", Toast.LENGTH_LONG);
            Log.d("Inside btnFinishClick", "onClick End");
            Intent intent = new Intent(getApplicationContext(), FirstPage.class);
            startActivity(intent);
        }
    }
}
}
