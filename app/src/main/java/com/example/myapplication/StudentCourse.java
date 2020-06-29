package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentCourse extends AppCompatActivity {
    public static  final String EMAIL_STDCRS = "com.example.myapplication.EMAIL_STD";

    private Toolbar actionbarCourse;
    private Button enroll;
    DatabaseHelper db;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText courseCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);
        Intent intent1=getIntent();
        final String Email= (String) intent1.getStringExtra(StudentPage.EMAIL_STD);
        db=new DatabaseHelper(this);
        courseCode=(EditText)findViewById(R.id.std_courseCode);
        actionbarCourse=(Toolbar)findViewById(R.id.courseActionbarStudent);
        setSupportActionBar(actionbarCourse);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("                        Courses");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //arrayList=new ArrayList<>();
        ListView listView=(ListView)findViewById(R.id.listOfStdCourses);
        arrayList=db.getStdCourses(Email);
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        enroll=(Button)findViewById(R.id.enrollCourse);
        //ENROLL BUTONUNA TIKLANINCA YAPILANLAR(DATABASE E INSERT YAPILIR)
        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String course_name = null;
                String  course_code=courseCode.getText().toString();
                Cursor cursor =db.getReadableDatabase().rawQuery("Select course_name from courses where Code=?",new String[]{course_code});
                cursor.moveToFirst();
                boolean insert;
                //courses table ında girilen code a ait bir course var mı yok mu yok ise insert ü false yap yoksa insert et
                if (cursor.getCount()==0){
                    insert=false;
                }else{
                    course_name=cursor.getString(cursor.getColumnIndex("course_name"));
                //Toast.makeText(getApplicationContext(), course_name, Toast.LENGTH_SHORT).show();
                insert = db.enrollToCourse(course_code,course_name,Email);
                }

                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Enrolled to Course Successfully", Toast.LENGTH_SHORT).show();
                    arrayList.add(course_code+"|"+course_name+"|"+Email);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getApplicationContext(), "Could not be enrolled!", Toast.LENGTH_SHORT).show();

            }
        });
        //üstüne tıklayınca info toast u çıkıyor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String courseInfo=adapter.getItem(position);
                Intent intent = new Intent(StudentCourse.this,CourseStreamStudent.class);
                intent.putExtra("courseInfo",courseInfo);
                startActivity(intent);
            }
        });
        //Üstüne uzun tıklayıp silmek için
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item=position;
                String[] info=(arrayList.get(which_item)).split("\\|");
                new AlertDialog.Builder(StudentCourse.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete "+info[1]+" course? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //arraylist ve db den silindi
                                String[] info=(arrayList.get(which_item)).split("\\|");
                                db.deleteStdCourse(info[0],info[2]);
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
