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

public class StudentReceivedActivity extends AppCompatActivity {
    private Toolbar actionbarMessage;
    private DatabaseHelper db;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_received_avtivity);
        actionbarMessage=(Toolbar)findViewById(R.id.std_received_message_actionbar);
        Intent intent7=getIntent();
        final String sender_email= (String) intent7.getStringExtra(StudentMessageActivity.SENDER_INFO);
        db=new DatabaseHelper(this);
        setSupportActionBar(actionbarMessage);
        getSupportActionBar().setTitle("                 Received Messages");

        ListView listView=(ListView)findViewById(R.id.listOfStdReceivedMessages);
        arrayList=db.getReceivedMessages(sender_email);
        // arrayList.add("");
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        //üstüne tıklayınca info toast u çıkıyor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] str=adapter.getItem(position).split("\\|");
                Toast.makeText(getApplicationContext(),"Announcement subject :  "+str[0]+"\n"+str[1]+"\n"+str[2],Toast.LENGTH_LONG).show();
            }
        });

    }
}
