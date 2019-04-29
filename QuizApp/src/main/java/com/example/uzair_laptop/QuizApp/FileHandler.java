package com.example.uzair_laptop.QuizApp;
/*
    Uzair Foolat (@00466850)
    Biology Business (General Purpose Android Quiz)
    Mobile Development Assignment 1
    13/12/2018
 */

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FileHandler {

    public ArrayList questionList, answerList, hintList, imageList; //arrays for each of the files that will be use.d
    public InputStream inputStream; //used for reading files.
    public FileWriter fileWriter; //used for writing to files.

    public FileHandler() {
        questionList = new ArrayList<String>(); //stores questions.
        answerList = new ArrayList<String>(); //stores answers.
        hintList = new ArrayList<String>(); //stores hints.
        imageList = new ArrayList<String>(); //stores image string (image name) values.
    }

    public void readFile(Context context) {
        String text = ""; //text is empty
        try {
            inputStream = context.getAssets().open("questions.txt"); //gets the asset file.
            int size = inputStream.available(); //the available space for the input stream.
            byte[] buffer = new byte[size]; //creates a byte buffer based on the size of the input stream.
            inputStream.read(buffer); //reads the buffer
            inputStream.close(); //closes the input stream.
            text = new String(buffer); //text is set to the values contained in the file.

            String [] strs = text.split("\\n"); //new string array for the split text
            for (int i = 0; i < strs.length; i++) {
                questionList.add(strs[i]); //adds each split value into the arraylist and increments until it's complete.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAnswer(Context context) {
        String text = "";
        try {
            inputStream = context.getAssets().open("answers.txt"); //gets the asset file.
            int size = inputStream.available(); //the available space for the input stream.
            byte[] buffer = new byte[size]; //creates a byte buffer based on the size of the input stream.
            inputStream.read(buffer); //reads the buffer.
            inputStream.close(); //closes the input stream.
            text = new String(buffer); //text is set to the values contained in the file.

            String [] strs = text.split(","); //new string array for the split text
            for (int i = 0; i < strs.length; i++) {
                answerList.add(strs[i]); //adds each split value into the arraylist and increments until it's complete.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readHint(Context context) {
        String text = "";
        try {
            inputStream = context.getAssets().open("hints.txt"); //gets the asset file.
            int size = inputStream.available(); //the available space for the input stream.
            byte[] buffer = new byte[size]; //creates a byte buffer based on the size of the input stream.
            inputStream.read(buffer); //reads the buffer.
            inputStream.close(); //closes the input stream.
            text = new String(buffer); //text is set to the values contained in the file.

            String [] strs = text.split("\\n"); //new string array for the split text
            for (int i = 0; i < strs.length; i++) {
                hintList.add(strs[i]); //adds each split value into the arraylist and increments until it's complete.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readImage(Context context) {
        String text = "";
        try {
            inputStream = context.getAssets().open("images.txt"); //gets the asset file.
            int size = inputStream.available(); //the available space for the input stream.
            byte[] buffer = new byte[size]; //creates a byte buffer based on the size of the input stream.
            inputStream.read(buffer); //reads the buffer.
            inputStream.close(); //closes the input stream.
            text = new String(buffer); //text is set to the values contained in the file.

            String [] strs = text.split(","); //new string array for the split text
            for (int i = 0; i < strs.length; i++) {
                imageList.add(strs[i]); //adds each split value into the arraylist and increments until it's complete.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readName(Context context) {
        StringBuilder stringBuilder = new StringBuilder(); //stringbuilder is initialised.
        String line;
        BufferedReader in = null; //input is null.
        try {
            File file = new File(context.getFilesDir(), "name.txt"); //gets the name file.
            in = new BufferedReader(new FileReader(file)); //reads the file.
            while ((line = in.readLine()) != null) //if the line is null.
                stringBuilder.append(line); //add line to the stringbuilder.

        } catch (FileNotFoundException e) {
            //
        } catch (IOException e) {
            e.getStackTrace();
        }

        return stringBuilder.toString(); //returns the string of the stringbuilder.
    }

    public void saveName(Context context, String text) {
        try {
            File file = new File(context.getFilesDir(), "name.txt"); //creates a new file called name.
            fileWriter = new FileWriter(file); //writes to the name file.
            fileWriter.write(text); //text is written to the name file.
            fileWriter.close(); //closes upon write completion.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //all the below methods return the arraylists which are initialised in this class for use in other classes.
    public ArrayList<String> getQuestionList () {

        return questionList;
    }

    public ArrayList<String> getAnswerList () {

        return answerList;
    }

    public ArrayList<String> getHintList() {

        return hintList;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }
}
