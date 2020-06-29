package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseStreamStudent extends AppCompatActivity {
    private final String class_ID="1";
    private DatabaseHelper db;
    private ListView postListView;
    private EditText post;
    private Button btn_post;
    PostListAdapter adapter;
    private ArrayList<String> arrayList;
    private Toolbar actionbarcourseStream;
    Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_stream_student);
        //Assign variables
        Intent intent=getIntent();
        final String course_info= (String) intent.getStringExtra("courseInfo");
        final String[] info=course_info.split("\\|");
        db=new DatabaseHelper(this);
        postListView =(ListView)findViewById(R.id.listview_posts_student);
        post=(EditText)findViewById(R.id.StreamPost);
        btn_post=(Button)findViewById(R.id.addPost) ;
        arrayList = new ArrayList<>();
        final String student_email=info[2];
        map = new HashMap<String,String>();
        map.put("classID",class_ID);

        arrayList=db.getPosts(info[0]);
        adapter = new PostListAdapter(getApplicationContext(), arrayList,Integer.parseInt(map.get("classID")));
        postListView.setAdapter(adapter);


        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String courseInfo=adapter.getItem(position).toString();
                String postInfo = arrayList.get(position).toString();
                final String[] info=postInfo.split("\\|");
                //Toast.makeText(getApplicationContext(),postInfo,Toast.LENGTH_LONG).show();
                String toCommentPage = info[0]+"|"+student_email+"|"+info[1]+"|"+info[2];
                Intent intent = new Intent(CourseStreamStudent.this,CourseStream.class);
                intent.putExtra("postInfo",toCommentPage);
                startActivity(intent);
            }
        });


        actionbarcourseStream=(Toolbar)findViewById(R.id.streamActionbar);
        setSupportActionBar(actionbarcourseStream);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.course_action_bar);
        TextView title=(TextView) findViewById(R.id.action_bar_title);
        title.setText(info[0]+"-"+info[1]);
    }
}
