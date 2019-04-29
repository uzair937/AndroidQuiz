package com.example.uzair_laptop.QuizApp;
/*
    Uzair Foolat (@00466850)
    Biology Business (General Purpose Android Quiz)
    Mobile Development Assignment 1
    13/12/2018
 */

import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class QuizGame extends AppCompatActivity {
    //All the required elements are instanciated here where the public instances are used in other classes.
    private RadioGroup radioGroup; //radiogroup is a group of rasdio buttons.
    private RadioButton answer1, answer2, answer3, answer4; //each answer is also instanced.
    private Button quitButton, submitButton, hintButton, test;
    private ImageView imageView;
    public static int Score; //needed by high scores class so it's static.
    private ArrayList<String> imageList, questionList, answerList, hintList; //stores string values of the image file name, the questions, the answers and the hints.
    private SensorManager sensorManager; //phone sensors.
    private boolean hasHint = true; //one hint per game.
    FileHandler fileHandler = new FileHandler(); //FileHandler class handles the reading and writing of files.
    private int q = 0, a = 0; //current question/answer/hint number.
    private float accelVal, accelLast, shake; //sensor values for shake detection.
    private boolean shaking = true;
    private TextView timeView; //timer field.
    private CountDownTimer countDownTimer; //countdown timer.

    final Notification notification = new Notification();

    @Override
    protected void onCreate(Bundle savedInstanceState) { //all code in here is initialised with the application.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizgame); //layout for this class.
        setImageView(); //calls the setImageView method.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        accelVal = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f; //sets all the shake detection values.
        resetScore(); //resets the score to 0 as it is static.
        hintButton = findViewById(R.id.hintButton); //hint button for emulator testing.
        hintButton.setOnClickListener((v) -> {
            getHint(); //calls the getHint method.
        });
        try {
            setData(); //calls the setData method which sets the questions.
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener((v) -> {
            checkAnswer();
        });
        quitButton = findViewById(R.id.quit);
        quitButton.setOnClickListener((v) -> {
            quitQuiz();
        });
        test = findViewById(R.id.test);
        test.setOnClickListener((v) -> {
            Score = 10; //tests high scores.
            navigateHiscores();
        });
        timeView = findViewById(R.id.timeView);
        countDownTimer = new CountDownTimer(15000, 1000) { //sets timer to 15 seconds.
            @Override
            public void onTick(long millisUntilFinished) {
                timeView.setText("Time left: " + millisUntilFinished / 1000); //displays the time remaining to answer.
            }

            @Override
            public void onFinish() {
                countDownTimer.cancel(); //if the timer runs out
                Score--;
                setScore();
                AlertBox("Looks like you ran out of time!", "Too slow!");
                Vibrate();
                a++;
                refreshQuestions();
            }
        };
        countDownTimer.start();
    }

    private final SensorEventListener sensorListener = new SensorEventListener() { //handles the shake detection
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0], y = sensorEvent.values[1], z = sensorEvent.values[2];

            accelLast = accelVal;
            accelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = accelVal - accelLast;
            shake = shake * 0.9f + delta; //values based on device position

            if (shake > 15 && shaking == true) { //this will only bring up the hint once so shaking again does nothing.
                getHint(); //displays hint on shake
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    public void setImageView() {
        String draw = getImage(); //gets the string image value from the method.
        imageView = findViewById(R.id.imageView); //finds the imageview and sets the field.
        imageView.setImageResource(getResources().getIdentifier(draw, "drawable", getPackageName()));
    }

    public void setScore() {
        TextView scoreText = findViewById(R.id.score);
        scoreText.setText("Score " + Score); //displays the score.
    }

    private int checkIndex() {
        radioGroup = findViewById(R.id.answerGroup); //uses the answergroup field.
        int radioButtonID = radioGroup.getCheckedRadioButtonId(); //gets the ID of selected radio button.
        View radioButton = radioGroup.findViewById(radioButtonID); //checks the view to see if it is selected.
        int index = radioGroup.indexOfChild(radioButton); //the index is set to the selected radio button.
        index++; //incremented by 1 so it doesn't go from 0 - 3
        return index; //returns the index
    }

    public void setData() throws Exception {
        if (questionList == null) { //if the question list is empty.
            fileHandler.readFile(this); //reads the question file.
            questionList = fileHandler.getQuestionList(); //sets the question list to the text data from file handler.
        }
        q++; //each time the method is called, q is incremented to check the end of the quiz.

        TextView question = findViewById(R.id.question); //finds the fiew for the question field.
        answer1 = findViewById(R.id.answer1); //finds the view for each answer field.
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);

        int i = 0;
        while (i < 5) {
            question.setText(questionList.get(i)); //done in a while and not a for so the values change individually.
            i++;
            answer1.setText(questionList.get(i));
            i++;
            answer2.setText(questionList.get(i));
            i++;
            answer3.setText(questionList.get(i));
            i++;
            answer4.setText(questionList.get(i));
            i++;
        }
    }

    public String getAnswer() {
        fileHandler.readAnswer(this); //readAnswer method is called from FileHandler.
        answerList = fileHandler.getAnswerList(); //the answerList is set from that file.
        return answerList.get(a); //gets the value based on int a.
    }

    public String getImage() {
        fileHandler.readImage(this); //readImage method is called from FileHandler.
        imageList = fileHandler.getImageList(); //the imageList is set from that file.
        return imageList.get(q); //returns the image based on int q.
    }

    public void refreshQuestions() { //method for setting new questions without going to a new page.
        if (q < questionList.size() - 1) { //if q, the current question number is smaller then the size of the questionList - 1.
            for (int j = 0; j < 5; j++) {
                questionList.remove(0); //the first 5 values from the array are removed.
            }
            setImageView(); //the imageview is set.
            countDownTimer.start(); //the countdowntimer starts/restarts.
            answer1.setChecked(false); //all answers get unchecked as radiobuttons by default remain checked.
            answer2.setChecked(false);
            answer3.setChecked(false);
            answer4.setChecked(false);
            try {
                setData(); //new data is set based on the first 5 values of the array after removal of the previous 5 values.
            } catch (Exception e) {
                e = new Exception();
            }
        } else {
            AlertBox("You've finished the quiz!", "Congratulations!"); //else if there are no more questions left.
            navigateHiscores(); //go to high scores page.
        }
    }

    public void checkAnswer() { //checks the answer is correct.
        int ans = Integer.parseInt(getAnswer()); //getAnswer uses a string arraylist which contains numbers, allowing it to be converted to an integer.
        if (checkIndex() == ans) { //if the index of the radio button matches the int ans.
            Score++; //increment score, set the score, increment a to match the next questions answer and image.
            setScore();
            //AlertBox("Correct!", "Chosen Answer: ");
            notification.ledARGB = Color.GREEN;
            a++;
            refreshQuestions(); //new questions get set.
        } else if (checkIndex() == 0) {
            AlertBox("You need to choose an answer!", "No answer selected"); //if nothing is selected
        } else { //else if the answer doesn't match the index
            Score--; //score is decremented, the score is set, the phone will vibrate, a is incremented and new questions are set.
            setScore();
            //AlertBox("Wrong answer", "Oops!");
            Vibrate();
            notification.ledARGB = Color.RED;
            a++;
            refreshQuestions();
        }
    }

    private void getHint() {
        if (hasHint) { //if hasHint is true
            fileHandler.readHint(this); //the readHint method is called from FileHandler.
            hintList = fileHandler.getHintList(); //the hintList is set.
            AlertBox(hintList.get(a), "Here is your one hint: "); //gets the string from position a of the arraylist.
            hasHint = false; //hasHint becomes false as you get only one hint.
            shaking = false; //once the hint is called via a shake, it becomes false so further shaking does nothing.
        } else {
            AlertBox("You have used your one and only hint!", "Sorry..."); // if you click the hint button after using a hint.
        }

    }

    private void AlertBox(String alert, String title) { //dynamically sets the AlertBox with an alert and title on call.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title); //sets the title.
        builder.setMessage(alert); //sets the alert.
        builder.setPositiveButton("OK", null); //what happens when the button on the alert is clicked.
        AlertDialog dialog = builder.create(); //creates the alert dialog.
        dialog.show(); //displays the alert.
    }

    private void Vibrate() { //handles device vibrations.
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)); //this works!
    }

    private void navigateHiscores() {
        Intent intent = new Intent(this, HighScores.class);
        startActivity(intent); //navigates to high scores.
    }

    private void navigateMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent); //navigate to main activity.
    }

    private void quitQuiz() {
        DialogInterface.OnClickListener dialogClickListener = ((dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    navigateMainActivity(); //goes to the main activity class.
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to return to the menu?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        //sets the values for the AlertDialog with a positive and negative action.
    }

    public int returnScore() {
        return Score;
    }

    public void resetScore() {
        Score = 0;
    }

    public void setScore(int l) {
        Score = l;
    }
}
