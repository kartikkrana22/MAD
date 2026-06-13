package com.techiguru.jinookartikey;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView iv_speak ;
    int processId = 100 ;
    MediaPlayer mp ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        iv_speak = findViewById(R.id.iv_speak);
        // to click on the speak image
        iv_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // to recognize the voice
                Intent voice = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                // to get all languages
                voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                //to get the select language from device
                voice.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                // to show the message for speak
                voice.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now");
                // pass the intent to OS
                startActivityForResult(voice, processId);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //to get the os output voice to text format

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == processId && data != null)
        {
            // to get the text from intent
            ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(this, list.get(0).toString(), Toast.LENGTH_SHORT).show();
            // to pass the no. of operations based on statements
            switch (list.get(0).toString())
            {
                case "open camera" :
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(camera);
                    break;

                case "play song" :
                    if (mp != null)
                    {
                        mp.release();
                        mp = null;
                    }
                    mp = new MediaPlayer();
                    try
                    {
                        mp.setDataSource("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3");
                        mp.prepareAsync();
                    } catch (Exception e) {
                        Toast.makeText(this, "Error loading song", Toast.LENGTH_SHORT).show();
                    }
                    mp.setOnPreparedListener(mediaPlayer -> mp.start());
                    break;

                case "stop song" :
                    mp.stop();
                    mp.release();
                    break;

                // to open google using voice command

                case "open Google" :
                    Intent google = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                    startActivity(google);
                    break;

                case "set alarm" :
                    Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
                    alarm.putExtra(AlarmClock.EXTRA_MESSAGE, "Voice Alarm") ;
                    alarm.putExtra(AlarmClock.EXTRA_HOUR,7);
                    alarm.putExtra(AlarmClock.EXTRA_MINUTES,40);
                    alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, false);
                    startActivity(alarm);
                    break;

                default:
                    Intent share = new Intent(Intent.ACTION_SEND);
                    // to attach the message with intent
                    share.putExtra(Intent.EXTRA_TEXT, list.get(0).toString());
                    // to define the data type

                    share.setType("text/plain");
                    startActivity(Intent.createChooser(share,"Share via"));
            }
        }
    }
}