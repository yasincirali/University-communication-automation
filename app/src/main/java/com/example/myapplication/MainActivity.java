package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Handler handler;
    Runnable runnable;
    ImageView img;
    DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Default email ve password verilerini database e upload eder
        uploadToDatabase();

        img=findViewById(R.id.img);
        img.animate().alpha(4000).setDuration(0);


        handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent dap = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(dap);
                finish();

            }
        },1000 );





    }
    //Default Database Upload to the system
    private void uploadToDatabase() {

        //ListADT
        List<String[]> list = new ArrayList<>();
        db= new DatabaseHelper(this);
        String Data[][] = new String[33][3];
        Data[0][0] = "yasin.cirali@ceng.deu.edu.tr";
        Data[0][1] = "123456";
        Data[0][2] = "Student";
        list.add(Data[0]);

        Data[1][0] = "feristah.dalkilic@cs.deu.edu.tr";
        Data[1][1] = "frsth0";
        Data[1][2] = "Teacher";
        list.add(Data[1]);

        Data[2][0] = "sila.kilinc@ceng.deu.edu.tr";
        Data[2][1] = "silyas";
        Data[2][2] = "Student";
        list.add(Data[2]);

        Data[3][0] = "zehraozdemir@ceng.deu.edu.tr";
        Data[3][1] = "zaho19";
        Data[3][2] = "Student";
        list.add(Data[3]);

        //For each loop in ArrayList
        for(String[] s: list){
            boolean checkEmail = db.checkEmail(s[0]);

            if (checkEmail == true) {

                boolean insert = db.insertUser(s[0], s[1],s[2]);

                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Can not register", Toast.LENGTH_SHORT).show();
            }

        }


        /*
        for (int i = 0; i < list.size(); i++) {

            boolean checkEmail = db.checkEmail(list.get(i)[0]);

            if (checkEmail == true) {

                boolean insert = db.insertUser(list.get(i)[0], list.get(i)[1],list.get(i)[2]);

                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Can not register", Toast.LENGTH_SHORT).show();
            }


        }*/

    }


}
