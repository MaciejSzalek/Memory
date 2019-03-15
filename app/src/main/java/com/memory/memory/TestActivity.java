package com.memory.memory;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    TextView speechTextView;
    Button startButton;
    Button lineButton;
    List<Integer> list = new ArrayList<>();

    SpeechManager speech;

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        speechTextView = findViewById(R.id.speech_textView);
        startButton = findViewById(R.id.speech_start_button);
        lineButton = findViewById(R.id.speech_line_button);

        speech = new SpeechManager(this, this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //speech.promptSpeechInput();
                //showCont();
                //test();
                compereList();
            }
        });
        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //compereList();
                showTest();
            }
        });
    }
    private void showTest(){
        for(Integer integer: list){
            speechTextView.append("Number: " + Integer.toString(integer) + "\n");
        }
    }
    private void test(){
        int[] one = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        int[] two = {1,2,4,6,11,15};
        for (int aTwo : two) {
            for (int anOne : one) {
                if (anOne == aTwo) {
                    list.add(anOne);
                    break;
                }
            }
        }
    }
    public void compereList(){
        List<String> userList = new ArrayList<>();
        userList.add("514627228");
        userList.add("857463265");

        List<String> numberList = getNumber();
        for(String number: numberList){
            for(String user: userList){
                if(PhoneNumberUtils.compare(number, user)){
                    speechTextView.append("Number: " + number + "\n");
                }
            }
        }
    }

    public void showCont(){
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null , null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        if(cursor !=null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.NUMBER));
                    speechTextView.append(name + ": " + number + "\n");
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public List<String> getNumber(){
        List<String> contactList = new ArrayList<>();
        Cursor cursor= getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null , null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        if(cursor !=null){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex
                        (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex
                        (ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactList.add(number);
            }
            cursor.close();
        }
        return contactList;
    }

    /*private  static final int REQ_CODE_SPEECH_INPUT = 100;

    public void promptSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak");
        try{
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),
                    "speech not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    speechTextView.setText(result.get(0));

                }
                break;
            }
        }
    }
}
