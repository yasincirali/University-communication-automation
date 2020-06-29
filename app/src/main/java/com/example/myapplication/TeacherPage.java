package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TeacherPage extends AppCompatActivity {
    public static  final String EMAIL_TEXT = "com.example.myapplication.EMAIL_TEXT";
    private Button logOut;
    private TextView baslik;
    private ImageView course;
    private ImageView announcement;
    private ImageView assignment;
    private ImageView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_page);
        //Login edilen emaili aldık
        Intent intent0=getIntent();
        final String Email= (String) intent0.getStringExtra(LoginActivity.EXTRA_TEXT);

        //Emaili başlık olarak yazdık
        baslik=(TextView)findViewById(R.id.textLecturer);
        baslik.setText(Email);
        course=(ImageView)findViewById(R.id.teach_courses);
        announcement=(ImageView)findViewById(R.id.teach_announcements);
        assignment=(ImageView)findViewById(R.id.teach_assignments);
        message=(ImageView)findViewById(R.id.teach_messages);

        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==course.getId()){
                    Intent intent = new Intent(TeacherPage.this, TeacherCourse.class);
                    intent.putExtra(EMAIL_TEXT,Email);
                    startActivity(intent);
                   //finish();
                }
            }
        });
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==announcement.getId()){
                    Intent intent = new Intent(TeacherPage.this, AnnouncementTeacher.class);
                    intent.putExtra(EMAIL_TEXT,Email);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==assignment.getId()){
                    Intent intent = new Intent(TeacherPage.this, AssignmentTeacher.class);
                    intent.putExtra(EMAIL_TEXT,Email);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==message.getId()){
                    Intent intent = new Intent(TeacherPage.this, MessageAvtivity.class);
                    intent.putExtra(EMAIL_TEXT,Email);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        //log out butonu işlevselleşti
        logOut=(Button)findViewById(R.id.Logout_button);
       //logouta tıklanıp login sayfasına gidildi
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==logOut.getId()){
                Intent intent = new Intent(TeacherPage.this, LoginActivity.class);
                startActivity(intent);
                //finish();
                }
            }
        });

    }
}
