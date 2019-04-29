package com.example.uzair_laptop.QuizApp;
/*
    Uzair Foolat (@00466850)
    Biology Business (General Purpose Android Quiz)
    Mobile Development Assignment 1
    13/12/2018
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //All the required elements are instanciated here where the public instances are used in other classes.
    private Button startQuiz, highScores, soundToggle, musicToggle, quitApp; //each button has been assigned a value.
    public static boolean sound = true, music = false; //sound is enabled by default and is static as it applies to all activities.
    QuizGame quizGame = new QuizGame(); //creates an instance of the quizgame class.
    public MediaPlayer mediaPlayer; //used for music.
    public SoundPool soundPool; //used for sound effects.
    public AudioAttributes audioAttributes; //used for sound effects.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (music) {
            playMusic(this, "music"); //music plays if boolean music is true.
        }
        startQuiz = findViewById(R.id.startQuiz); //gets the button ID.
        startQuiz.setOnClickListener((v) -> {
            navigateQuizGame(); //goes to the quizgame page.
        });

        highScores = (findViewById(R.id.highScores));
        highScores.setOnClickListener((v) -> {
            navigateHighScores();
        });

        soundToggle = (findViewById(R.id.soundToggle));
        soundToggle.setOnClickListener((v) -> {
            if (sound) { //if sound is true then it gets set to false and vise versa.
                sound = false;
                playSound(v.getContext(), "chord");
            } else {
                sound = true;
                playSound(v.getContext(), "chimes");
            }
        });

        musicToggle = (findViewById(R.id.musicToggle));
        musicToggle.setOnClickListener((v) -> {
            if (music) { //if music is true then it gets set to false and vise evrsa.
                music = false;
                playMusic(v.getContext(), "music");
            } else {
                music = true;
                playMusic(v.getContext(), "music");
            }
        });

        quitApp = findViewById(R.id.quitApp);
        quitApp.setOnClickListener((v) -> {
            quitApp();
        });
    }

    private void quitApp() {
        DialogInterface.OnClickListener dialogClickListener = ((dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    android.os.Process.killProcess(android.os.Process.myPid()); //kills the application process.
                    System.exit(1); //exits the application.
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        //sets the values for the AlertDialog.
    }

    public void playMusic(Context context, String sFile) {
        if (music) {
            Resources resources = context.getResources(); //gets the resources.
            int soundID = resources.getIdentifier(sFile, "raw", context.getPackageName()); //gets the ID of the sound file based on a string value which enables dynamic method calls.
            mediaPlayer = MediaPlayer.create(this, soundID); //creates an object with the sound ID.
            mediaPlayer.start(); //starts playing.
        } else {
            mediaPlayer.stop(); //stops playing.
        }
    }

    public void playSound(Context context, String sFile) {
        if (sound) {
            Resources resources = context.getResources(); //gets resources.
            int soundID = resources.getIdentifier(sFile, "raw", context.getPackageName()); //gets the ID of a sound file based on a string value.
            audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) //sets the content type.
                    .setUsage(AudioAttributes.USAGE_GAME) //sets the usage type.
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(2)
                    .setAudioAttributes(audioAttributes)
                    .build();
            soundPool.load(this, soundID, 1); //loads the sound based on the sound ID.
        } else {
            //do nothing
        }
    }

    public void navigateQuizGame() {
        Intent intent = new Intent(this, QuizGame.class); //tells the startActivity method where to go.
        startActivity(intent);
    }

    public void navigateHighScores() {
        quizGame.resetScore(); //resets the score of the quiz.
        Intent intent = new Intent(this, HighScores.class);
        startActivity(intent);
    }

    public boolean checkSound() {
        return sound;
    }
}
