package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class TeacherCourse extends AppCompatActivity {
    private Toolbar actionbarCourse;
    DatabaseHelper db;
    private Button addCourse;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText courseCode;
    private EditText courseName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_course);
        //Login edilen hocanın emailini aldık
        Intent intent1=getIntent();
        final String Email= (String) intent1.getStringExtra(TeacherPage.EMAIL_TEXT);


        db=new DatabaseHelper(this);
        actionbarCourse=(Toolbar)findViewById(R.id.courseActionbar);
        setSupportActionBar(actionbarCourse);
        getSupportActionBar().setTitle("                        Courses");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ListView listView=(ListView)findViewById(R.id.listOfCourses);
        arrayList=db.getCourses(Email);
        //arrayList.add("");
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);



         courseCode=(EditText)findViewById(R.id.courseCode);
         courseName=(EditText)findViewById(R.id.courseName);
         addCourse=(Button)findViewById(R.id.addCourse);
       //Add course işlemleri
        addCourse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                 String  course_code=courseCode.getText().toString();
                 String course_name=courseName.getText().toString();
                boolean insert = db.insertCourses(course_code,course_name,Email);

                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Course added Successfully", Toast.LENGTH_SHORT).show();
                    arrayList.add(course_code+"|"+course_name+"|"+Email);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getApplicationContext(), "Could not added!", Toast.LENGTH_SHORT).show();

            }

        });
        //üstüne tıklayınca course stream e gidiyor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String courseInfo=adapter.getItem(position);
                Intent intent = new Intent(TeacherCourse.this,CourseStreamTeacher.class);
                intent.putExtra("courseInfo",courseInfo);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(),"Course information: "+adapter.getItem(position),Toast.LENGTH_LONG).show();
            }
        });
        //Üstüne uzun tıklayıp silmek için
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item=position;
                String[] info=(arrayList.get(which_item)).split("\\|");
                new AlertDialog.Builder(TeacherCourse.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete "+info[1]+" course? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //arraylist ve db den silindi
                                String[] info=(arrayList.get(which_item)).split("\\|");
                                db.deleteCourse(info[0],info[2]);
                                Toast.makeText(getApplicationContext(),info[0],Toast.LENGTH_SHORT).show();
                                arrayList.remove(which_item);
                                adapter.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("No",null).show();

                return true;
            }
        });









    }
}
