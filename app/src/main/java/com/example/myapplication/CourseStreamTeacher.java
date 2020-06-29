package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseStreamTeacher extends AppCompatActivity {
    private final String class_ID="1";
    private DatabaseHelper db;
    private ListView postListView;
    private EditText post;
    private Button btn_post;
    PostListAdapter adapter;
    private ArrayList<String> arrayList;
    Map<String, String> map;
    //private ArrayAdapter<String> adapter;

    private Toolbar actionbarcourseStream;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_stream_teacher);
        //Assign variables
        Intent intent=getIntent();
        final String course_info= (String) intent.getStringExtra("courseInfo");
        final String[] info=course_info.split("\\|");
        final String teacher_email = info[2];
        db=new DatabaseHelper(this);
        postListView =(ListView)findViewById(R.id.listview_posts);
        post=(EditText)findViewById(R.id.StreamPost);
        btn_post=(Button)findViewById(R.id.addPost) ;
        //Associative array job is done
        map = new HashMap<String,String>();
        map.put("classID",class_ID);


        arrayList = new ArrayList<>();
        arrayList=db.getPosts(info[0]);
        int count_comments=0;

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post_content=post.getText().toString();
                boolean insert = db.publishPost(info[0],info[2],post_content);

                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Post published Successfully", Toast.LENGTH_SHORT).show();
                    String data=info[0]+"|"+info[2]+"|"+post_content;
                    arrayList.add(data);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getApplicationContext(), "Could not published!", Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new PostListAdapter(getApplicationContext(), arrayList,Integer.parseInt(map.get("classID")));
        postListView.setAdapter(adapter);
        //Üstüne uzun tıklayıp silmek için
        postListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

                final int which_item=position;
                String[] info=(arrayList.get(which_item)).split("\\|");
                new AlertDialog.Builder(CourseStreamTeacher.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to cancel your submit? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //arraylist ve db den silindi
                                String[] info=(arrayList.get(which_item)).split("\\|");
                                //BURADA SADECE CEVABI SİLEN BİR FONKSİYON OLMALI(ANSWERS TABLOSUNDAN)
                                //db.deleteAssignment(info[0],info[1]);
                                db.deletePost(info[0],info[1],info[2]);
                                db.deleteCommentsByPostId(info[2]);
                                //Toast.makeText(getApplicationContext(),info[0],Toast.LENGTH_SHORT).show();
                                arrayList.remove(which_item);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"Post is deleted",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No",null).show();
                return true;
            }
        });

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String courseInfo=adapter.getItem(position).toString();
                String postInfo = arrayList.get(position).toString();
                final String[] info=postInfo.split("\\|");
                //Toast.makeText(getApplicationContext(),postInfo,Toast.LENGTH_LONG).show();
                String toCommentPage = info[0]+"|"+teacher_email+"|"+info[1]+"|"+info[2];
                Intent intent = new Intent(CourseStreamTeacher.this,CourseStream.class);
                intent.putExtra("postInfoTeacher",toCommentPage);
                startActivity(intent);
            }
        });

        actionbarcourseStream=(Toolbar)findViewById(R.id.streamActionbar);
        setSupportActionBar(actionbarcourseStream);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.course_action_bar);
        TextView title=(TextView) findViewById(R.id.action_bar_title);
        title.setText(info[0]+"-"+info[1]);
        //getSupportActionBar().setTitle(""+info[1]);

    }
}
