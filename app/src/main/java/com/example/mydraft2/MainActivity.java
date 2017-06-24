package com.example.mydraft2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {


    Button btnSignUp ,btnSignIn;
    EditText edtUserName, edtPassWord;
    final DbUtility dbUtility = new DbUtility();

    protected void onCreate(Bundle savedInstanceState) {
        DeviceOpenHelper.getInstance(this); //To create new instance
        super.onCreate(savedInstanceState);
       // Stetho.initializeWithDefaults(this);

        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this));

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

        setContentView(R.layout.activity_main);

        btnSignUp = (Button) findViewById(R.id.sigup_form);
        btnSignIn = (Button) findViewById(R.id.email_sign_in_button);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassWord = (EditText) findViewById(R.id.edtPassword);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() ,SignUp.class);
                startActivity(intent);


            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String UserName , Password;
                StringBuilder FirstName = new StringBuilder();
                UserName = edtUserName.getText().toString();
                Password = edtPassWord.getText().toString();
                if (dbUtility.isUserValid(UserName,Password,FirstName))
                {
                    mt("Login Successful");
                    Intent intent = new Intent(getApplicationContext() , FirstPage.class);
                    intent.putExtra("FirstName",FirstName.toString());
                    startActivity(intent);
                }
                else
                {
                    mt("Invalid User or Password");
                }

            }
        });
    }

    void mt(String text)
    {
        Toast.makeText(this ,text,Toast.LENGTH_SHORT).show();
    }


}
