package com.memory.memory;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

import java.util.ArrayList;

/**
 * Created by Maciej Szalek on 2019-01-30.
 */

public class SpeechManager extends Activity{

    private Activity activity;

    public interface Speech{
        void setSpeechStr(String speechStr);
    }

    Speech speech;
    public SpeechManager(Activity activity){
        this.activity = activity;
    }

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    public void promptSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.Speak));
        activity.startActivity(intent);
        try{
            activity.startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException a ){
            a.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if(resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speech.setSpeechStr(result.get(0));
                }
                break;
            }
        }
    }
}
