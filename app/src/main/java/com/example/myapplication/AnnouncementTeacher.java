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

public class AnnouncementTeacher extends AppCompatActivity {
    public static  final String ASSIGNMENT_TEXT = "com.example.myapplication.EMAIL_TEXT";
    private Toolbar actionbarAnnouncement;
    DatabaseHelper db;
    private EditText subject;
    private EditText content;
    private Button publish;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_teacher);
        Intent intent2=getIntent();
        final String Email= (String) intent2.getStringExtra(TeacherPage.EMAIL_TEXT);

        actionbarAnnouncement=(Toolbar)findViewById(R.id.announcementActionbar);
        subject=(EditText)findViewById(R.id.announcementSubject);
        content=(EditText)findViewById(R.id.announcementContent);
        publish=(Button)findViewById(R.id.publishAnnouncement);



        db=new DatabaseHelper(this);
        setSupportActionBar(actionbarAnnouncement);
        getSupportActionBar().setTitle("                  Announcements");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String subject1=subject.getText().toString();
        final String content1=content.getText().toString();

        ListView listView=(ListView)findViewById(R.id.listOfAnnouncements);
        arrayList=db.getAnnouncements(Email);
       // arrayList.add("");
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject1=subject.getText().toString();
                String content1=content.getText().toString();
                boolean insert = db.publishAnnouncement(subject1,content1,Email);

                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Announcement published Successfully", Toast.LENGTH_SHORT).show();
                    arrayList.add(subject1+"|"+content1+"|"+Email);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getApplicationContext(), "Could not published!", Toast.LENGTH_SHORT).show();
            }
        });
        //üstüne tıklayınca info toast u çıkıyor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] str=adapter.getItem(position).split("\\|");
                Toast.makeText(getApplicationContext(),"Announcement subject :  "+str[0]+"\n"+str[1]+"\n"+str[2],Toast.LENGTH_LONG).show();
            }
        });
        //Üstüne uzun tıklayıp silmek için
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item=position;
                String[] info=(arrayList.get(which_item)).split("\\|");
                new AlertDialog.Builder(AnnouncementTeacher.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete "+info[1]+" course? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //arraylist ve db den silindi
                                String[] info=(arrayList.get(which_item)).split("\\|");
                                db.deleteAnnouncement(info[0],info[1],info[2]);
                                //Toast.makeText(getApplicationContext(),info[0],Toast.LENGTH_SHORT).show();
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
