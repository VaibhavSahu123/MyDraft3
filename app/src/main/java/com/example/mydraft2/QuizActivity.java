package com.example.mydraft2;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * Created by dikki on 18/12/2016.
 */



public class QuizActivity extends AppCompatActivity
{
    final DbUtility dbUtility = new DbUtility();
    Button btnNext , btnEndResult;
    public static final String TAG = "QUIZ";
    int result, questionAttempted = 0  ;
    int QuestionNumber = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_layout);
        //Log.d(TAG, "onCreate: of Quizactiviy");
        btnNext = (Button) findViewById(R.id.btnNextQuestion);
        final String profileName = getIntent().getStringExtra("SelectedProfile");
        Log.d(TAG," ProfileName is " + profileName);

        //btnEndResult = (Button) findViewById(R.id.btnEndResult);

        String rawQuery = "select * from questions";
        String [] Selection = null;
        final Cursor queryResult = dbUtility.QueryDb(rawQuery,Selection);
        Log.e(TAG, "After initialize",null);
        queryResult.moveToFirst();
        populateQuestions(queryResult);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult(queryResult);

                if (QuestionNumber >= 10)
                {
                    saveResultInDB(profileName);
                   LinearLayout quiz_layout = (LinearLayout) findViewById(R.id.layquiz_layout);
                    ViewGroup parent = (ViewGroup) quiz_layout.getParent();
                    parent.removeView(quiz_layout);
                    //LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    Log.d(TAG, "onClick: inside 5");
                    View child = getLayoutInflater().inflate(R.layout.quiz_end,parent,false);
                    parent.addView(child);
                    btnEndResult = (Button) findViewById(R.id.btnEndResult);
                    btnEndResult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                else
                {
                    queryResult.moveToNext();
                    populateQuestions(queryResult);
                }
            }
        });
       /* btnEndResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                startActivity(intent);
            }
        });
*/    }

    private void saveResultInDB(String profileName)
    {
        result = result/questionAttempted;// That is cumaltaive result divided by number of question answered
        dbUtility.insertResultDetails(result,profileName);
    }

    private void calculateResult(Cursor queryResult)
    {
        RadioButton rdOption1 = (RadioButton) findViewById(R.id.rdOption1);
        RadioButton rdOption2 = (RadioButton) findViewById(R.id.rdOption2);
        RadioButton rdOption3 = (RadioButton) findViewById(R.id.rdOption3);
        RadioButton rdOption4 = (RadioButton) findViewById(R.id.rdOption4);

        if (rdOption1.isChecked())
        {
            result += queryResult.getInt(6); //OptWeight1
            questionAttempted++;
        }
        else if (rdOption2.isChecked())
        {
            result += queryResult.getInt(7); //OptWeight2
            questionAttempted++;
        }
        else if (rdOption3.isChecked())
        {
            result += queryResult.getInt(8); //OptWeight3
            questionAttempted++;
        }
        else if (rdOption4.isChecked())
        {
            result += queryResult.getInt(9); //OptWeight4
            questionAttempted++;
        }
        Log.d(TAG," Result is " + result + "Question Attemted "+ questionAttempted);
    }

    private void populateQuestions(Cursor queryResult)
    {
        TextView QuestionCounter = (TextView) findViewById(R.id.txtQuestionCount);
        TextView QuestionText = (TextView) findViewById(R.id.txtQuestion);

        RadioButton rdOption1 = (RadioButton) findViewById(R.id.rdOption1);
        RadioButton rdOption2 = (RadioButton) findViewById(R.id.rdOption2);
        RadioButton rdOption3 = (RadioButton) findViewById(R.id.rdOption3);
        RadioButton rdOption4 = (RadioButton) findViewById(R.id.rdOption4);

        rdOption1.setChecked(false);
        rdOption2.setChecked(false);
        rdOption3.setChecked(false);
        rdOption4.setChecked(false);

        QuestionCounter.setText("Question "+ QuestionNumber + " of 9 Questions");
        QuestionNumber++;
        QuestionText.setText(queryResult.getString(1));
        rdOption1.setText(queryResult.getString(2));
        rdOption2.setText(queryResult.getString(3));
        rdOption3.setText(queryResult.getString(4));
        rdOption4.setText(queryResult.getString(5));
    }
}
