package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper db;
    public static  final String EXTRA_TEXT = "com.example.myapplication.EXTRA_TEXT";
    public EditText Email;
    public EditText Password;
    private TextView Message;
    private Button Login;
    private EditText Type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        Email = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.pass);
        Login = (Button)findViewById(R.id.login);
        Type=(EditText) findViewById(R.id.type);
        String str="";
        //Click ten sonra email ve password kontrol edilir
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String email=Email.getText().toString();
                    String password=Password.getText().toString();
                    String type=Type.getText().toString();

                    if (email.equals("")||password.equals("")){
                        Toast.makeText(getApplicationContext(),"Fields are empty",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        boolean checkEmailPassword = db.checkEmailPassword(email,password,type);
                        if (checkEmailPassword==true) {
                            Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();

                            if (type.equals("Teacher")){
                                Intent intent = new Intent(LoginActivity.this, TeacherPage.class);
                                intent.putExtra(EXTRA_TEXT,email);
                                startActivity(intent);
                                finish();
                            }
                            else if(type.equals("Student")) {
                                Intent intent = new Intent(LoginActivity.this, StudentPage.class);
                                intent.putExtra(EXTRA_TEXT,email);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Wrong Email, Password or type",Toast.LENGTH_SHORT).show();

                    }

                }
        });
    }






}
