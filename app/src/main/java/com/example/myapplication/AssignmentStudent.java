package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AssignmentStudent extends AppCompatActivity {
    public static  final String ASSIGN_INFO = "com.example.myapplication.EMAIL_TEXT";
    private Toolbar ActionbarAssignment;
    private ArrayList<String> arrayList;
    private DatabaseHelper db;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_student);
        Intent intent5=getIntent();
        final String Email= (String) intent5.getStringExtra(StudentPage.EMAIL_STD);

        db=new DatabaseHelper(this);
        ActionbarAssignment=(Toolbar)findViewById(R.id.assignmentStdActionbar);
        setSupportActionBar(ActionbarAssignment);
        getSupportActionBar().setTitle("                    Assignments");


        ListView listView=(ListView)findViewById(R.id.listOfStdAssignments);
        arrayList=db.getAssignmentsStd(Email);
        // arrayList.add("");
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        //üstüne tıklayınca info toast u çıkıyor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String assignmentInfo=adapter.getItem(position)+"|"+Email;
                Intent intent=new Intent(AssignmentStudent.this,AssignAnswerStudent.class);
                intent.putExtra(ASSIGN_INFO,assignmentInfo);
                startActivity(intent);
            }
        });


    }
}
