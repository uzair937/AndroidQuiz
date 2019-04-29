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
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class HighScores extends AppCompatActivity {
    private QuizGame quizGame = new QuizGame(); //creates a new QuizGame class instance.
    public int score; //sets the score.
    private Button menuButton, clearButton; //buttons for each action.
    private ImageView imageView; //imageview.
    private int scoreCheck; //checks the score from the Quiz.
    TextView highScore, nameView; //highscore textview, name textview.
    FileHandler fileHandler, fileHandler2; //used for methods and string name 2.
    private String name, name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        scoreCheck = quizGame.returnScore(); //gets the score from the quiz.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores);
        name2 = fileHandler2.readName(this); //gets the name from the name file.
        setDoge(); //sets the image to doge.
        setScore(); //sets the score.
        setName(); //sets the name from the file.
        menuButton = findViewById(R.id.menu); //menu button.
        menuButton.setOnClickListener((v) -> {
            mainMenu();
        });
        clearButton = findViewById(R.id.clear); //clears the highscores.
        clearButton.setOnClickListener((v) -> {
            resetScore();
        });
    }

    public void setDoge() {
        imageView = findViewById(R.id.imageView2); //gets the doge image and sets it to the imageview.
        imageView.setImageResource(R.drawable.doge);
    }

    private void setScore() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        score = sharedPreferences.getInt("key", 0); //sharedpreferences contains the high score.
        if (scoreCheck > score) { //checks if the new score is higher than the existing score.
            storeScore(); //stores it
            highScore = findViewById(R.id.highScore);
            highScore.setText("Highscore: " + scoreCheck); //displays the new high score.
        } else if (scoreCheck == 0) {
            highScore = findViewById(R.id.highScore); //displays the existing high score.
            highScore.setText("Highscore: " + score);
        } else {
            AlertBox("Sorry, you didn't manage to beat the high score. :("); //tells the user their score didn't beat the high score.
            highScore = findViewById(R.id.highScore);
            highScore.setText("Highscore: " + score); //displays the highest score.
        }
    }

    private void setName() {
        nameView = findViewById(R.id.nameView);
        if (scoreCheck > score) { //checks if the new score is higher than the existing score.
            final TextView nameView = findViewById(R.id.nameView); //declares the nameview.
            AlertDialog.Builder builder = new AlertDialog.Builder(this); //pops up an alert dialog.
            builder.setTitle("What is your name?"); //asks for name input.
            final EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(editText); //allows the user to input their name.
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    name = editText.getText().toString(); //gets the name from the input and strings it.
                    storeName(); //stores the name string.
                    nameView.setText("Name: " + name); //sets the text to the name.
                }
            });
            builder.show();
            nameView.setText("Name: " + name);
        } else {
            nameView.setText("Name: " + name2); //sets the text to the name from the name file.
        }

    }

    private void storeScore() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //stores the score into shared preferences.
        editor.putInt("key", scoreCheck);
        editor.commit(); //commits the store.
    }

    private void storeName() {
        fileHandler = new FileHandler();
        fileHandler.saveName(this, name); //saves the name into a file.
    }

    private void AlertBox(String alert) { //used for creating alerboxes with a string for alert.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("*poke*");
        builder.setMessage(alert); //the message is set to string alert.
        builder.setPositiveButton("OK", null); //does nothing.
        AlertDialog dialog = builder.create();
        dialog.show(); //displays the alert.
    }

    private void mainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); //goes to the mainactivity class.
    }

    public int getScore() {
        return score;
    }

    public void resetScore() { //method to reset the high score.
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    quizGame.resetScore();
                    SharedPreferences sharedPreferences = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    sharedPreferences.edit().clear().commit(); //clears anything stored in shared preferences.
                    highScore.setText("Highscore: " + score); //sets the score.
                    nameView.setText("Name "); //sets the text.
                    //fileHandler.saveName(context, null);  //doesn't work for some reason.
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break; //do nothing
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //displays the dialog box.
        builder.setMessage("Are you sure you want to reset the high score?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        //sets the message of the dialog box with a positive and negative action.
    }
}
