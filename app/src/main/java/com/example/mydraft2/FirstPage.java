package com.example.mydraft2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by dikki on 07/12/2016.
 */

public class FirstPage extends AppCompatActivity
{
    TextView txtWelcome;
    Context context;
    static String firstName;
    Button btnTakeQuiz, btnViewResult, btnProfileForQuiz,btnModifyProfile;
    final DbUtility dbUtility = new DbUtility();

    FirstPage()
    {
        context=FirstPage.this;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        txtWelcome = (TextView) findViewById(R.id.txtWelcomeText);
        btnTakeQuiz = (Button) findViewById(R.id.btnQuiz);
        btnViewResult = (Button) findViewById(R.id.btnViewResult);
        btnProfileForQuiz = (Button)findViewById(R.id.btnAnotherProfile);
        btnModifyProfile = (Button) findViewById(R.id.btnModifyProfile);
        Intent intent = getIntent();
        if(firstName==null) //Populating for first time
        {
            firstName = intent.getStringExtra("FirstName");
            Log.d("ONCreate", "onCreate: " + firstName);
        }
        txtWelcome.setText("Welcome "+ firstName+", What would you like to do ?");

        btnTakeQuiz.setOnClickListener(new btnQuizClickListner());
        btnViewResult.setOnClickListener(new btnResultClickListner());
        btnProfileForQuiz.setOnClickListener(new btnAnotherProfilebClickListner());
        btnModifyProfile.setOnClickListener(new btnModifyProfileClickListner());
    }

    class btnModifyProfileClickListner implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Pick Profile ");
            final List<String> profilesList = dbUtility.getProfileNames(false);//Don't need to have entry in result i.e false
            // If profile has no name ask user to create profile first
            if (profilesList.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Before Modify , need to create a profile first", Toast.LENGTH_SHORT);
            }
            else
            {
                builder.setItems(profilesList.toArray(new String[profilesList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code to perform action here
                        Intent intent = new Intent(getApplicationContext(),ModifyProfile.class);
                        Log.d("FirstPage", "Selected profile is "+ profilesList.get(which));
                        intent.putExtra("SelectedProfile",profilesList.get(which));
                        startActivity(intent);
                    }
                });
                builder.show();
            }

        }
    }

    class btnAnotherProfilebClickListner implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),AnotherProfile.class);
            startActivity(intent);
            //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    class btnQuizClickListner  implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Pick Profile ");
            final List<String> profilesList = dbUtility.getProfileNames(false);//Don't need to have entry in result i.e false
            // If profile has no name ask user to create profile first
            if (profilesList.isEmpty())
            {
                createProfileAlert();
            }
            else
            {
            builder.setItems(profilesList.toArray(new String[profilesList.size()]), new DialogInterface.OnClickListener() {
            @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Code to perform action here
                Intent intent = new Intent(getApplicationContext(),QuizActivityNew.class);
                Log.d("FirstPage", "Selected profile is "+ profilesList.get(which));
                intent.putExtra("SelectedProfile",profilesList.get(which));
                startActivity(intent);
            }
            });
            builder.show();
            }
            Log.d("FirstPage", "button clicked ");
                        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private void createProfileAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("No Relation profile present , please create profile first ")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
             Intent intent = new Intent(getApplicationContext(),AnotherProfile.class);
                startActivity(intent);
            }
        });
        AlertDialog profileAlert = builder.create();
        profileAlert.show();

    }

    class btnResultClickListner implements  View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Pick Profile for which you want to see Result");
            final List<String> profilesList = dbUtility.getProfileNames(true); // Must have entry in Result table i.e true
            if (profilesList.isEmpty())
            {
                createProfileAlert();
            }
            else {
                builder.setItems(profilesList.toArray(new String[profilesList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Code to perform action here
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        Log.d("FirstPage", "Selected profile is " + profilesList.get(which));
                        intent.putExtra("SelectedProfile", profilesList.get(which));
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        }
    }
}
