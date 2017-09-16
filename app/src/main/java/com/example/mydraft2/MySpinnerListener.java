package com.example.mydraft2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import static android.graphics.Color.rgb;
import static com.example.mydraft2.QuizActivityNew.TAG;

/**
 * Created by dikki on 05/08/2017.
 */
class MySpinnerListener implements View.OnClickListener
{
    Context cnt;
    String[] listItems ;
    String Title;
    StringBuilder selectedText;
    boolean []checkedItems;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    Button btn ;
    MySpinnerListener(Context appContext, String[] listItems,  StringBuilder selectedText, Button btn)
    {
        cnt = appContext;
        this.listItems = listItems;
        checkedItems = new boolean[listItems.length];
        Title =  btn.getText().toString();
        this.selectedText = selectedText;
        this.btn = btn;
    }
//This is second version of Ctor for modify profile call
    MySpinnerListener(Context appContext, String[] listItems,  StringBuilder selectedText, Button btn, boolean []existingCheckedItems )
    {
        cnt = appContext;
        this.listItems = listItems;

        checkedItems = new boolean[listItems.length];
        checkedItems = existingCheckedItems;
        Title =  btn.getText().toString();
        this.selectedText = selectedText;
        this.btn = btn;
        //Initializing mUserItem for checked items
        for(int i =0 ; i < checkedItems.length;i++)
        {
            if(checkedItems[i]==true)
                mUserItems.add(i);
        }
    }
    void setLable()
    {
        if(!mUserItems.isEmpty())
        {
            btn.setText("done");
            // v.setBackgroundColor(rgb(144,238,144));//Light green
        }
        else
        {
            btn.setText(Title);
        }
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(cnt);
        mBuilder.setTitle(Title);//First element ould be title
        mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if (isChecked) {
                    if (!mUserItems.contains(position)) {
                        mUserItems.add(position);
                        //     checkedItems[position]=true;
                    }
                }
                else if (mUserItems.contains(position)) {
                    mUserItems.remove(mUserItems.indexOf(position));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                String item = "";
                for (int i = 0; i < mUserItems.size(); i++) {
                    item = item + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        item = item + ", ";
                    }
                }
                //mItemSelected.setText(item);
                selectedText.setLength(0);
                selectedText.append(item);
                setLable();
            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                setLable();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;

                }
                mUserItems.clear();
                selectedText.setLength(0);
                setLable();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
}

