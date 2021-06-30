package com.contacts.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {
    private EditText personName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //init the edit text filed where the user would write his name
        personName = findViewById(R.id.person_name);
        //listen for the editText when user press (done) button in the keyboard
        personName.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if ((actionId & EditorInfo.IME_ACTION_DONE) != 0) {
                //read the text typed by the user
                //tim() method is used to remove Leading and trailing white spaces
                String name = textView.getText().toString().trim();
                //prepare  the intent result that will back to the main activity
                Intent returnIntent = new Intent();
                //add the name as an extra in that intent
                returnIntent.putExtra("name", name);
                /**
                 * check the name with the method @isLegalName
                 * if the @name isLegalName then the activity result would be (RESULT_OK)
                 * otherwise it would be (RESULT_CANCELED)
                 */
                setResult(isLegalName(name) ? Activity.RESULT_OK : Activity.RESULT_CANCELED, returnIntent);
                //finally end this activity and back to the main activity
                finish();
                return true;
            }
            return false;
        });
    }

    /**
     * this method checks if the name is legal or not
     * @param name: the name types by the user
     * this method does two functions:
     * - make sure that all characters in the @name are alphabetic.
     * - make sure that the @name consists of only two words.
     */
    private boolean isLegalName(String name) {
        //check if all chars are alphabetic
        char[] charArray = name.toCharArray();
        for (char c : charArray) {
            if (!Character.isAlphabetic(c) && c != ' ')
                return false;
        }
        //check if user entered only two words
        String arr[] = name.split(" ");
        if (arr.length == 2) return true;

        return false;
    }
}