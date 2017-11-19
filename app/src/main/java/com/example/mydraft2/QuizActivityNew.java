package com.example.mydraft2;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Path;
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
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.R.attr.textAppearanceLarge;
import static android.support.v7.cardview.R.color.cardview_dark_background;
import static android.support.v7.cardview.R.color.cardview_light_background;
import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;
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
    //public static List<List<Integer>> OptionSelected = new ArrayList<List<Integer>>(Collections.nCopies(QuestionCount, -1));
    public static List<List<Integer>> OptionSelected = new ArrayList<List<Integer>>();
    static
    {
        for(int i=0; i< QuestionCount;i++)
        {
            List<Integer> innerList = new ArrayList<Integer>();
            OptionSelected.add(innerList);
        }

    }
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
                Snackbar.make(view, "Swap to go next question", Snackbar.LENGTH_SHORT)
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
        for(List<Integer> value : OptionSelected)
        {
            if(value==null) //No question answered for this
                continue;

            QuestionAttempted++; //Else question attempted

            for(int i=0;i<value.size();i++) {
                {
                 String weightageColumnName = "opt"+(i+1)+"Weightage";
                    result += queryResult.getInt(queryResult.getColumnIndex(weightageColumnName)); //OptWeight2
                }
                //Clear exiting value so that if someone click back it should not use old values
                value.clear();
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
        private static final String OPTIONARRAYLIST = "OptionArrayList";
        private static final String OPTIONCOUNT = "OptionCount";
        private static final String ISMULTICHOICEQUESION = "IsMultiChoiceQuestion";
        /*private static final String Option1 = "Option1";
        private static final String Option2 = "Option2";
        private static final String Option3 = "Option3";
        private static final String Option4 = "Option4";*/
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
            int OptionCount = queryResult.getInt(queryResult.getColumnIndex(DbUtility.NUMBEROFOPTIONS));
            int isMultiChoiceQuestion = queryResult.getInt(queryResult.getColumnIndex(DbUtility.MULTICHOICEQUESTION));
            ArrayList<String> optionArrayList = new ArrayList<String>();
            for(Integer i =1;i<=OptionCount;i++)
            {
                String OptionNumber = "option"+ Integer.toString(i);
                Log.d(TAG,"Option Name ["+OptionNumber+"]");
                String Value = queryResult.getString(queryResult.getColumnIndex(OptionNumber));
                if(!Value.isEmpty())
                {
                    Log.d(TAG,"Value is ["+Value+"]");
                    optionArrayList.add(Value);
                }
            }

            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            Log.d(TAG,"ARG section number is "+ sectionNumber);

            String QuestionText = queryResult.getString(queryResult.getColumnIndex(DbUtility.QUESTION));

            args.putString(Question,QuestionText);
            args.putStringArrayList(OPTIONARRAYLIST,optionArrayList);
            args.putInt(OPTIONCOUNT,OptionCount);
            args.putInt(ISMULTICHOICEQUESION,isMultiChoiceQuestion);

           /* args.putString(Option1,queryResult.getString(queryResult.getColumnIndex(DbUtility.OPTION1)));
            args.putString(Option2,queryResult.getString(queryResult.getColumnIndex(DbUtility.OPTION2)));
            args.putString(Option3,queryResult.getString(queryResult.getColumnIndex(DbUtility.OPTION3)));
            args.putString(Option4,queryResult.getString(queryResult.getColumnIndex(DbUtility.OPTION4)));*/

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
            LinearLayout QuizLayout = (LinearLayout) rootView.findViewById(R.id.OptionsLayout);
            //VS added for testing
            Log.d(TAG,"OnCreateView called");
            AddCardView(QuizLayout,savedInstanceState);

            populateQuestions(rootView,savedInstanceState);

            setListenters(rootView,savedInstanceState);

                      return rootView;
        }

        private void AddCardView(LinearLayout QuizLayout, Bundle savedInstanceState)
        {
            int Position = getArguments().getInt(ARG_SECTION_NUMBER);
            Log.d(TAG,"Position is "+ Position);
            ArrayList<String> optionList = getArguments().getStringArrayList(OPTIONARRAYLIST);
            for(int i=0;i<optionList.size();i++)
            {
                CardView card = new CardView(getContext());

                // Set the CardView layoutParams
                LayoutParams params = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 30, 0, 0);
                card.setLayoutParams(params);

                // Set CardView corner radius
                card.setRadius(4);

                // Set cardView content padding
                //   card.setContentPadding(15, 15, 15, 15);

                //make it clickable
                card.setClickable(true);

                //set ID
              //  String ID = "Opt"+i+"Card";
                if(i==0)
                    card.setId(R.id.Opt1Card);
                else if(i==1)
                    card.setId(R.id.Opt2Card);
                else if(i==2)
                    card.setId(R.id.Opt3Card);
                else if(i==3)
                    card.setId(R.id.Opt4Card);
                else if(i==4)
                    card.setId(R.id.Opt5Card);
                else if(i==5)
                    card.setId(R.id.Opt6Card);
                else if(i==6)
                    card.setId(R.id.Opt7Card);
                else if(i==7)
                    card.setId(R.id.Opt8Card);
                else if(i==8)
                    card.setId(R.id.Opt9Card);
                else if(i==9)
                    card.setId(R.id.Opt10Card);
                else if(i==10)
                    card.setId(R.id.Opt11Card);
                else if(i==11)
                    card.setId(R.id.Opt12Card);
                // Set a background color for CardView
                card.setCardBackgroundColor(getResources().getColor(R.color.cardview_light_background));
                // card.setCardBackgroundColor(getResources().getColor(R.color.cardview_light_background));

                // Set the CardView maximum elevation
                //card.setMaxCardElevation(15);

                // Set CardView elevation
                //card.setCardElevation(9);

                // Initialize a new TextView to put in CardView
                TextView tv = new TextView(getContext());
                LayoutParams TextLayoutparams = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
                );
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setLayoutParams(params);
                tv.setText(optionList.get(i));
                tv.setTextAppearance(getContext(), android.R.style.TextAppearance_Large);
                tv.setTextColor(Color.MAGENTA);
                // Put the TextView in CardView
                card.addView(tv);

                // Finally, add the CardView in root layout
                QuizLayout.addView(card);
            }
        }
        private void setListenters(View rootView, Bundle savedInstanceState)
        {
            int optCount = getArguments().getInt(OPTIONCOUNT);
            int IsMultiChoiceQuestion = getArguments().getInt(ISMULTICHOICEQUESION);
           // Log.d(TAG,"Optcount is "+optCount);
            CardView OptionCard[] = new CardView[optCount];
            for (int i=0;i < optCount;i++)
            {
                String ID = "Opt"+(i+1)+"Card";
                int resId = getResources().getIdentifier(ID,"id", getContext().getPackageName());
                //Log.d(TAG,"Optcount is "+optCount+" Res id is "+ resId +" ID "+ ID);
                OptionCard[i] = (CardView) rootView.findViewById(resId);
                OptionCard[i].setOnClickListener(new optionListener((i+1),IsMultiChoiceQuestion,OptionCard));
            }
//            CardView Option1Card = (CardView) rootView.findViewById(R.id.Opt1Card);
//            CardView Option2Card = (CardView) rootView.findViewById(R.id.Opt2Card);
//            CardView Option3Card = (CardView) rootView.findViewById(R.id.Opt3Card);
//            CardView Option4Card = (CardView) rootView.findViewById(R.id.Opt4Card);
//
//            Option1Card.setOnClickListener(new option1Listener());
//            Option2Card.setOnClickListener(new option2Listener());
//            Option3Card.setOnClickListener(new option3Listener());
//            Option4Card.setOnClickListener(new option4Listener());
        }

        class optionListener implements  View.OnClickListener
        {
            int optNumber;
            int isMultiChoiceQuestion;
            CardView[] optionCard;

            public optionListener(int optNumber, int isMultiChoiceQuestion, CardView[] optionCard)
            {
                this.isMultiChoiceQuestion=isMultiChoiceQuestion;
                this.optNumber=optNumber;
                this.optionCard=optionCard;

            }

            @Override
            public void onClick(View v)
            {
                // int position = PageListener.currentposition+1;
                int position = PageListener.currentposition +1;
                View vRootView=  mViewPager.findViewWithTag("myView"+ position);
                Log.d(TAG , "Page onclick opt "+ optNumber + "myView "+ position );
                printOptionSelected();

                //Setting CardView as clicked
                for(int i =0; i < optionCard.length;i++)
                {
                    if(optNumber==(i+1))//if selected position then mark dark else
                    {
                        //denotes Option selected for this page , this index starts from ZERo so no doing currentposition+1
                        List<Integer> OptionValues = OptionSelected.get(PageListener.currentposition);

                            if(isMultiChoiceQuestion ==1)
                            {
                                //In case of multi choice if options re-clicked then un select that & remove from result
                                if(OptionValues.contains(i+1))
                                {
                                    optionCard[i].setCardBackgroundColor(getResources().getColor(cardview_light_background));
                                    OptionValues.remove(new Integer(i+1)); //remove Int object 
                                }
                                else {
                                    optionCard[i].setCardBackgroundColor(getResources().getColor(cardview_dark_background));
                                    OptionValues.add((i + 1)); //In case of mulichoice add
                                }
                            }
                            else
                            {
                                OptionValues.clear();//Else first remove earlier entry then add
                                optionCard[i].setCardBackgroundColor(getResources().getColor(cardview_dark_background));
                                OptionValues.add((i+1));
                            }

                    }
                    else if(isMultiChoiceQuestion!=1)
                    {
                        //Un setting other view
                        optionCard[i].setCardBackgroundColor(getResources().getColor(cardview_light_background));
                    }
                }
                /*C1.setCardBackgroundColor(getResources().getColor(cardview_dark_background));

                //Un setting other view
                C2.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C3.setCardBackgroundColor(getResources().getColor(cardview_light_background));
                C4.setCardBackgroundColor(getResources().getColor(cardview_light_background));
*/
                printOptionSelected();
                            }
        }

        void printOptionSelected()
        {
            String Values ;
            for(int i =0 ; i < QuestionCount;i++)
            {
                Values = "At Index "+i+" ==>";
                List<Integer> tempList = OptionSelected.get(i);
                for(int j =0 ; j<tempList.size();j++)
                {
                    Values += ", "+tempList.get(j);
                }
                Log.d(TAG,Values);
            }
        }

//        class option4Listener implements  View.OnClickListener
//        {
//            @Override
//            public void onClick(View v)
//            {
//                int position = PageListener.currentposition+1;
//                View vRootView=  mViewPager.findViewWithTag("myView"+ position);
//                Log.d(TAG , "Page onclick opt 4 myView "+ position );
//                CardView C1 = (CardView) vRootView.findViewById(R.id.Opt1Card);
//                CardView C2 = (CardView) vRootView.findViewById(R.id.Opt2Card);
//                CardView C3 = (CardView) vRootView.findViewById(R.id.Opt3Card);
//                CardView C4 = (CardView) v.findViewById(R.id.Opt4Card);
//
//                //Setting CardView as clicked
//                C4.setCardBackgroundColor(getResources().getColor(cardview_dark_background));
//
//                //Un setting other view
//                C1.setCardBackgroundColor(getResources().getColor(cardview_light_background));
//                C2.setCardBackgroundColor(getResources().getColor(cardview_light_background));
//                C3.setCardBackgroundColor(getResources().getColor(cardview_light_background));
//
//                //denotes Option4 selected for this page,this index starts from ZERo so no doing currentposition+1
//                OptionSelected.set(PageListener.currentposition,4);
//            }
//        }
        private void populateQuestions(View rootView, Bundle savedInstanceState)
        {
            TextView questionText = (TextView) rootView.findViewById(R.id.QuestionText);
            Integer IsMultiChoice = getArguments().getInt(ISMULTICHOICEQUESION);
            String questionOnly = getArguments().getString(Question);

            CharSequence  question = getQuestionTextFormatted(questionOnly,IsMultiChoice);

            questionText.setText(question);
            int Position = getArguments().getInt(ARG_SECTION_NUMBER);
            //setting tag so that later on can be used for retriving view
            rootView.setTag("myView"+Position);
            Log.d(TAG , "ONcreate view myView"+ Position );
            //If not -1 then one of the option was selected before view got recycle
            List<Integer> OptionEarlierSelcted = OptionSelected.get(Position-1);
            Log.d(TAG , "AT "+ Position +" Size is "+ OptionEarlierSelcted.size());
            if (OptionEarlierSelcted!=null)
            {
                for(int i =0 ;i < OptionEarlierSelcted.size();i++)
                {
                    String ID = "Opt"+OptionEarlierSelcted.get(i)+"Card";
                    int resId = getResources().getIdentifier(ID,"id",getContext().getPackageName());
                    CardView SelectedOption = (CardView) rootView.findViewById(resId);
                    SelectedOption.setCardBackgroundColor(getResources().getColor(cardview_dark_background));
                }
            }

          }

        private CharSequence getQuestionTextFormatted(String questionOnly, Integer isMultiChoice)
        {
            String tip ;
            int largeText = getResources().getDimensionPixelSize(R.dimen.large_text);
            int mediumText = getResources().getDimensionPixelSize(R.dimen.medium_text);

            if(isMultiChoice==1)
            {
                tip="(Select one or more options)";
            }
            else
            {
                tip ="(Selet anyone option)";
            }

            SpannableString span1 = new SpannableString(questionOnly);
            span1.setSpan(new AbsoluteSizeSpan(largeText), 0, questionOnly.length(), SPAN_INCLUSIVE_INCLUSIVE);

            SpannableString span2 = new SpannableString(tip);
            span2.setSpan(new AbsoluteSizeSpan(mediumText), 0, tip.length(), SPAN_INCLUSIVE_INCLUSIVE);

// let's put both spans together with a separator and all
            CharSequence finalText = TextUtils.concat(span1, " ", span2);
            return finalText;
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
