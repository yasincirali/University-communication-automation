package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AssignAnswersTeacher extends AppCompatActivity {
    private Toolbar actionbarAssignmentAnswers;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_answers_teacher);
        Intent intent4=getIntent();
        final String assignmentInfo= (String) intent4.getStringExtra(AssignmentTeacher.ASSIGN_INFO);
        actionbarAssignmentAnswers=(Toolbar)findViewById(R.id.assignmentAnswerActionbar);
        setSupportActionBar(actionbarAssignmentAnswers);
        getSupportActionBar().setTitle("               Assignment Uploads");

        db=new DatabaseHelper(this);
        String[]info=assignmentInfo.split("\\|");
        ListView listView=(ListView)findViewById(R.id.answersOfAssignment);
        arrayList=db.getAnswers(info[0],info[1]);
        // arrayList.add("");
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        //üstüne tıklayınca info toast u çıkıyor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] str=adapter.getItem(position).split("\\|");
                Toast.makeText(getApplicationContext(),"Announcement subject :  "+str[0]+"\n"+str[1]+"\n"+str[2]+"\n"+str[3],Toast.LENGTH_LONG).show();
            }
        });


    }
}
