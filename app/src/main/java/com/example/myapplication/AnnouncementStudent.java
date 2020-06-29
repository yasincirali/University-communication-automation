package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AnnouncementStudent extends AppCompatActivity {
    private Toolbar actionbarStdAnnouncement;
    DatabaseHelper db;

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_student);
        actionbarStdAnnouncement=(Toolbar)findViewById(R.id.announcementStdActionbar);

        db=new DatabaseHelper(this);
        setSupportActionBar(actionbarStdAnnouncement);
        getSupportActionBar().setTitle("                  Announcements");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView=(ListView)findViewById(R.id.listOfStdAnnouncements);
        arrayList=db.getAllAnnouncement();
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
