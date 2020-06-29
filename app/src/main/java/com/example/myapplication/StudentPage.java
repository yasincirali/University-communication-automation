package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentPage extends AppCompatActivity {
    public static  final String EMAIL_STD = "com.example.myapplication.EMAIL_STD";
    DatabaseHelper db;
    LoginActivity loginact;
    private Button logOut;
    private ImageView course;
    private ImageView announcement;
    private ImageView assignment;
    private ImageView message;

    //!!BURADA LOGIN DEN EMAİL İ ALDIK

    TextView baslik;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);

        Intent intent1=getIntent();
        final String Email= (String) intent1.getStringExtra(LoginActivity.EXTRA_TEXT);



       // Toast.makeText(getApplicationContext(),Email,Toast.LENGTH_SHORT).show();
        baslik = (TextView)findViewById(R.id.textStudent);
        baslik.setText(Email);
        announcement=(ImageView)findViewById(R.id.std_announcements);
        assignment=(ImageView)findViewById(R.id.std_assignments);
        course=(ImageView)findViewById(R.id.std_courses);
        message=(ImageView)findViewById(R.id.std_messages);
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==course.getId()){
                    Intent intent = new Intent(StudentPage.this, StudentCourse.class);
                    intent.putExtra(EMAIL_STD,Email);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==announcement.getId()){
                    Intent intent = new Intent(StudentPage.this, AnnouncementStudent.class);
                    //intent.putExtra(EMAIL_STD,Email);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==assignment.getId()){
                    Intent intent = new Intent(StudentPage.this, AssignmentStudent.class);
                    intent.putExtra(EMAIL_STD,Email);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId()==message.getId()){
                    Intent intent = new Intent(StudentPage.this, StudentMessageActivity.class);
                    intent.putExtra(EMAIL_STD,Email);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        logOut=(Button)findViewById(R.id.Logout_button);
        //logouta tıklanıp login sayfasına gidildi
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentPage.this, LoginActivity.class);
                startActivity(intent);
                //finish();
            }
        });













    }
}
