package com.contacts.androidproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * states of the person name entered by the user
     *
     * @NAME_RESULT.NULL : no name has entered yet
     * @NAME_RESULT.CORRECT : user entered a correct name
     * @NAME_RESULT.INCORRECT :  user entered an incorrect name
     */
    enum NAME_RESULT {NULL, CORRECT, INCORRECT}

    //the person name passed from the SecondActivity
    private String personName = "";
    //the state of the personName, in default null
    private NAME_RESULT nameState = NAME_RESULT.NULL;
    //the requestCode to the SecondActivity intent
    final int SECOND_ACTIVITY_CODE = 100;
    //Button one and Button two
    private Button button1, button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get the screen orientation (portrait) or (landscape) to show the suitable layout.
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main_portrait);
        } else {
            setContentView(R.layout.activity_main_landscape);
        }

        //init the buttons one,two..
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        //listen to click events on button one
        button1.setOnClickListener(v -> {
            /**
             * start the  activity @SecondActivity.class to allow the user enter his name
             * and wait an activity result with the code @SECOND_ACTIVITY_CODE
             */
            Intent secondActivityIntent = new Intent(this, SecondActivity.class);
            startActivityForResult(secondActivityIntent, SECOND_ACTIVITY_CODE);
        });

        //listen to click events on button two
        button2.setOnClickListener(v -> {
            //if the user entered his name correctly
            //invoke the @editContact() method
            if (nameState == NAME_RESULT.CORRECT) {
                editContact();
            }
            //else if the user entered an incorrect name
            //show a toast message with that name
            else if (nameState == NAME_RESULT.INCORRECT) {
                Toast.makeText(getApplicationContext(), getString(R.string.incorrect_name) +
                        " : " + personName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * this method is called when the user enter a correct name in the @SecondActivity.class and press on the Button Two
     */
    private void editContact() {
        //make an intent to allow the user insert new contact with the default system contact provider
        //for more details check this link
        //https://developer.android.com/training/contacts-provider/modify-data
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        // Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        //put the person name passed from the SecondActivity as the name to the contact
        intent.putExtra(ContactsContract.Intents.Insert.NAME, personName);
        //finally start the contact activity
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //listen to the intents come back from other activities..
        //if the request code match @SECOND_ACTIVITY_CODE and there is an (name) extra
        if (requestCode == SECOND_ACTIVITY_CODE && data != null && data.hasExtra("name")) {
            //read the extra name to the global variable @personName
            personName = data.getStringExtra("name");
            //set the @nameState variable based on the activity result
            //Activity.RESULT_OK        ->>>> NAME_RESULT.CORRECT
            //Activity.RESULT_CANCELED  ->>>> NAME_RESULT.INCORRECT
            if (resultCode == Activity.RESULT_OK) nameState = NAME_RESULT.CORRECT;
            else if (resultCode == Activity.RESULT_CANCELED) nameState = NAME_RESULT.INCORRECT;
        }
    }

}