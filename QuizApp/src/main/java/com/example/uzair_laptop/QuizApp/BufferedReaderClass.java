package com.example.uzair_laptop.QuizApp;
/*
    Uzair Foolat (@00466850)
    Biology Business (General Purpose Android Quiz)
    Mobile Development Assignment 1
    08/12/2018
 */

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BufferedReaderClass { //this class is never used due to incompatibility but has been kept for my own benefit.

    private static ArrayList<String> arrayList = new ArrayList<>();

    public static void main(String[] args) {
        String path = "C:\\Users\\Uzair-Laptop\\Documents\\MobDev\\app\\src\\main\\assets\\test.txt";

        try (java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new FileReader(path))) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String [] strs = line.split("/s/");
                for (int i = 0; i < 5; i++) {
                    arrayList.add(strs[i]);
                }
            }

        } catch  (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getArrayList () {

        return arrayList;
    }
    //method below is from quizgame to test that array value setting works on radio buttons.

    //    public void arrayTest1() {
//        array1 = new ArrayList<String>();
//        String values1 = "Just checking this works";
//        String [] strs = values1.split(" ");
//        for (int i = 0; i < 4; i++) {
//            array1.add(strs[i]);
//        }
//
//        RadioButton answer1 = findViewById(R.id.answer1);
//        RadioButton answer2 = findViewById(R.id.answer2);
//        RadioButton answer3 = findViewById(R.id.answer3);
//        RadioButton answer4 = findViewById(R.id.answer4);
//
//        int i = 0;
//        while (i < array1.size()) {
//
//            answer1.setText("" + array1.get(i));
//            i++;
//            answer2.setText("" + array1.get(i));
//            i++;
//            answer3.setText("" + array1.get(i));
//            i++;
//            answer4.setText("" + array1.get(i));
//            i++;
//        }
//    }
}
