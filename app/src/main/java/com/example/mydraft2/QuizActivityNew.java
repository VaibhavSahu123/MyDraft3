package com.example.mydraft2;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.support.v7.cardview.R.color.cardview_dark_background;
import static android.support.v7.cardview.R.color.cardview_light_background;
import static com.example.mydraft2.R.attr.windowActionBar;

/**
 * Created by dikki on 25/03/2017.
 */

public class QuizActivityNew extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;
    private static final int QuestionCount = 9;

    final DbUtility dbUtility = new DbUtility();
    public static final String TAG = "QUIZ";
    public static Cursor queryResult = null;
    public static List<Integer> OptionSelected = new ArrayList<Integer>(Collections.nCopies(QuestionCount, -1));
    String profileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        profileName = getIntent().getStringExtra("SelectedProfile");
        Log.d(TAG," ProfileName is " + profileName);

        String rawQuery = "select * from questions";
        String [] Selection = null;
        queryResult = dbUtility.QueryDb(rawQuery,Selection);
        Log.e(TAG, "After initialize",null);
     //   queryResult.moveToFirst();


//       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//       setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        PageListener pageListener = new PageListener();
        mViewPager.setOnPageChangeListener(pageListener);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private static class PageListener extends ViewPager.SimpleOnPageChangeListener {
        static int currentposition;
        public void onPageSelected(int pos) {
            currentposition = pos;
        }
        public int getCurrentposition()
        {
            return currentposition;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.end_quiz)
        {
            calculateTestResult();

            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
            intent.putExtra("SelectedProfile",profileName);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void calculateTestResult()
    {
        int result =0;
        int QuestionAttempted = 0;
        for(int value : OptionSelected)
        {
            switch(value)
            {
                case -1:
                    break;
                case 1 :
                    result += queryResult.getInt(6); //OptWeight1
                    QuestionAttempted++;
                    break;
                case 2:
                    result += queryResult.getInt(7); //OptWeight2
                    QuestionAttempted++;
                    break;
                case 3 :
                    result += queryResult.getInt(8); //OptWeight3
                    QuestionAttempted++;
                    break;
                case 4:
                    result += queryResult.getInt(9); //OptWeight4
                    QuestionAttempted++;
                    break;
            }

        }
        result = result/QuestionAttempted;// That is cumaltaive result divided by number of question answered
        Log.d(TAG," Result is " + result + "Question Attemted "+ QuestionAttempted);
        saveResultInDB(profileName,result);

    }
    private void saveResultInDB(String profileName, int result)
    {

        dbUtility.insertResultDetails(result,profileName);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String Question = "Question";
        private static final String Option1 = "Option1";
        private static final String Option2 = "Option2";
        private static final String Option3 = "Option3";
        private static final String Option4 = "Option4";
        private static final String POSITION = "Position";


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            queryResult.moveToPosition(sectionNumber-1);

            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(Question,queryResult.getString(1));
            args.putString(Option1,queryResult.getString(2));
            args.putString(Option2,queryResult.getString(3));
            args.putString(Option3,queryResult.getString(4));
            args.putString(Option4,queryResult.getString(5));

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);

            populateQuestions(rootView,savedInstanceState);

            setListenters(rootView);
            return rootView;
        }

        private void setListenters(View rootView)
        {
            CardView Option1Card = (CardView) rootView.findViewById(R.id.Opt1Card);
            CardView Option2Card = (CardView) rootView.findViewById(R.id.Opt2Card);
            CardView Option3Card = (CardView) rootView.findViewById(R.id.Opt3Card);
            CardView Option4Card = (CardView) rootView.findViewById(R.id.Opt4Card);

            Option1Card.setOnClickListener(new option1Listener());
            Option2Card.setOnClickListener(new option2Listener());
            Option3Card.setOnClickListener(new option3Listener());
            Option4Card.setOnClickListener(new option4Listener());
        }

        class option1Listener implements  View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
               // int position = PageListener.currentposition+1;
                int position = PageListener.currentposition +1;
                View vRootView=  mViewPager.findViewWithTag("myView"+ position);
                Log.d(TAG , "Page onclick opt 1 myView "+ position );

                // based on the current position you can then cast the p
                CardView C1 = (CardView) v.findViewById(R.id.Opt1Card);
                CardView C2 = (CardView) vRootView.findViewById(R.id.Opt2Card);
                CardView C3 = (CardView) vRootView.findViewById(R.id.Opt3Card);
                CardView C4 = (CardView) vRootView.findViewById(R.id.Opt4Card);

                //Setting CardView as clicked
                C1.setCardBackgroundColor(getResources().getColor(cardview_dark_background));

                //Un setting other view
                C2.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C3.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C4.setCardBackgroundColor(getResources().getColor(cardview_light_background));

                //denotes Option1 selected for this page , this index starts from ZERo so no doing currentposition+1
                OptionSelected.set(PageListener.currentposition,1);
            }
        }
        class option2Listener implements  View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                int position = PageListener.currentposition+1;
                View vRootView=  mViewPager.findViewWithTag("myView"+ position);
                Log.d(TAG , "Page onclick opt 2 myView "+ position );

                CardView C1 = (CardView) vRootView.findViewById(R.id.Opt1Card);
                CardView C2 = (CardView) v.findViewById(R.id.Opt2Card);
                CardView C3 = (CardView) vRootView.findViewById(R.id.Opt3Card);
                CardView C4 = (CardView) vRootView.findViewById(R.id.Opt4Card);

                //Setting CardView as clicked
                C2.setCardBackgroundColor(getResources().getColor(cardview_dark_background));

                //Un setting other view
                C1.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C3.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C4.setCardBackgroundColor(getResources().getColor(cardview_light_background));

                //denotes Option2 selected for this page,this index starts from ZERo so no doing currentposition+1
                OptionSelected.set(PageListener.currentposition,2);

            }
        }
        class option3Listener implements  View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                int position = PageListener.currentposition+1;
                View vRootView=  mViewPager.findViewWithTag("myView"+ position);
                Log.d(TAG , "Page onclick opt 3 myView "+ position );
                CardView C1 = (CardView) vRootView.findViewById(R.id.Opt1Card);
                CardView C2 = (CardView) vRootView.findViewById(R.id.Opt2Card);
                CardView C3 = (CardView) v.findViewById(R.id.Opt3Card);
                CardView C4 = (CardView) vRootView.findViewById(R.id.Opt4Card);

                //Setting CardView as clicked
                C3.setCardBackgroundColor(getResources().getColor(cardview_dark_background));

                //Un setting other view
                C1.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C2.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C4.setCardBackgroundColor(getResources().getColor(cardview_light_background));

                //denotes Option3 selected for this page,this index starts from ZERo so no doing currentposition+1
                OptionSelected.set(PageListener.currentposition,3);

            }
        }

        class option4Listener implements  View.OnClickListener
        {
            @Override
            public void onClick(View v)
            {
                int position = PageListener.currentposition+1;
                View vRootView=  mViewPager.findViewWithTag("myView"+ position);
                Log.d(TAG , "Page onclick opt 4 myView "+ position );
                CardView C1 = (CardView) vRootView.findViewById(R.id.Opt1Card);
                CardView C2 = (CardView) vRootView.findViewById(R.id.Opt2Card);
                CardView C3 = (CardView) vRootView.findViewById(R.id.Opt3Card);
                CardView C4 = (CardView) v.findViewById(R.id.Opt4Card);

                //Setting CardView as clicked
                C4.setCardBackgroundColor(getResources().getColor(cardview_dark_background));

                //Un setting other view
                C1.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C2.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C3.setCardBackgroundColor(getResources().getColor(cardview_light_background));

                //denotes Option4 selected for this page,this index starts from ZERo so no doing currentposition+1
                OptionSelected.set(PageListener.currentposition,4);
            }
        }
        private void populateQuestions(View rootView, Bundle savedInstanceState)
        {
            TextView questionText = (TextView) rootView.findViewById(R.id.QuestionText);
            TextView option1Text = (TextView) rootView.findViewById(R.id.Option1Text);
            TextView option2Text = (TextView) rootView.findViewById(R.id.Option2Text);
            TextView option3Text = (TextView) rootView.findViewById(R.id.Option3Text);
            TextView option4Text = (TextView) rootView.findViewById(R.id.Option4Text);

            questionText.setText(getArguments().getString(Question));
            option1Text.setText(getArguments().getString(Option1));
            option2Text.setText(getArguments().getString(Option2));
            option3Text.setText(getArguments().getString(Option3));
            option4Text.setText(getArguments().getString(Option4));

            int Position = getArguments().getInt(ARG_SECTION_NUMBER);
            //setting tag so that later on can be used for retriving view
            rootView.setTag("myView"+Position);
            Log.d(TAG , "ONcreate view myView"+ Position );
            //If not -1 then one of the option was selected before view got recycle
            Integer OptionEarlierSelcted = OptionSelected.get(Position-1);
            if (OptionEarlierSelcted!= -1)
            {
                switch (OptionEarlierSelcted)
                {
                    case 1 :
                        CardView C1 = (CardView) rootView.findViewById(R.id.Opt1Card);
                        C1.setCardBackgroundColor(getResources().getColor(cardview_dark_background));
                        break;
               case 2 :
                        CardView C2 = (CardView) rootView.findViewById(R.id.Opt2Card);
                        C2.setCardBackgroundColor(getResources().getColor(cardview_dark_background));
                   break;
               case 3 :
                        CardView C3 = (CardView) rootView.findViewById(R.id.Opt3Card);
                        C3.setCardBackgroundColor(getResources().getColor(cardview_dark_background));
                   break;
               case 4 :
                        CardView C4 = (CardView) rootView.findViewById(R.id.Opt4Card);
                        C4.setCardBackgroundColor(getResources().getColor(cardview_dark_background));
                   break;
                }
            }

          }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return QuestionCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            /*switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }*/
            return Integer.toString(position+1);
        }

    }

}
