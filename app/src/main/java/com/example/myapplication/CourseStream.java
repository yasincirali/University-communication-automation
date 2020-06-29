package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CourseStream extends Activity {
    private DatabaseHelper db;
    private ListView postListView;
    private EditText comment;
    private Button btn_comment;
    private PostListAdapter adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_stream);

        //Assign variables
        Intent intent=getIntent();
        final String post_info= (String) intent.getStringExtra("postInfo");
        final String post_info_Teacher = (String) intent.getStringExtra("postInfoTeacher");

        String courseCode1;
        String studentEmail1;
        String postContent1;
        String postID1;
        if(post_info_Teacher!=null){
            String[] infoTeacher = post_info_Teacher.split("\\|");
              courseCode1 = infoTeacher[0];
              studentEmail1 = infoTeacher[1];
              postContent1 = infoTeacher[2];
              postID1 = infoTeacher[3];
        }
        else {
            final String[] info=post_info.split("\\|");
              courseCode1 = info[0];
              studentEmail1 = info[1];
              postContent1 = info[2];
              postID1 = info[3];
        }

        final String courseCode = courseCode1;
        final String studentEmail = studentEmail1;
        final String postContent = postContent1;
        final String postID = postID1;



        db=new DatabaseHelper(this);
        postListView =(ListView)findViewById(R.id.listview_comment);
        comment=(EditText)findViewById(R.id.StreamComment);
        btn_comment=(Button)findViewById(R.id.addComment) ;

        //arrayList.add("a|æ|a");
        //adapter = new PostListAdapter(getApplicationContext(), arrayList);
        //postListView.setAdapter(adapter);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.6));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -50;
        getWindow().setAttributes(params);


        arrayList = new ArrayList<>();
        arrayList=db.getComments(courseCode,postID);
        //adapter.notifyDataSetChanged();

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_content=comment.getText().toString();
                boolean insert = db.makeComment(courseCode,studentEmail,comment_content,postID);

                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Comment published Successfully", Toast.LENGTH_SHORT).show();
                    String data=courseCode+"|"+studentEmail+"|"+comment.getText().toString();
                    arrayList.add(data);
                    adapter.notifyDataSetChanged();

                } else
                    Toast.makeText(getApplicationContext(), "Could not published!", Toast.LENGTH_SHORT).show();

          }

        });
        adapter = new PostListAdapter(getApplicationContext(), arrayList,0);
        postListView.setAdapter(adapter);

        //Üstüne uzun tıklayıp silmek için
        postListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                Toast.makeText(getApplicationContext(),postID,Toast.LENGTH_LONG).show();

                final int which_item=position;
                String[] info=(arrayList.get(which_item)).split("\\|");
                new AlertDialog.Builder(CourseStream.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete your comment? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //comment listboxtan tüm özellikleriyle alındı.
                                String[] info=(arrayList.get(which_item)).split("\\|");
                                //Eğer giriş yapan insan kendi yorumunu silmek isterse silebilecek
                                if(studentEmail.equals(info[1])) {
                                    //üstüne uzun tıklanmış olan commentin
                                    db.deleteComment(postID, studentEmail, info[2]);
                                    //Toast.makeText(getApplicationContext(),info[0],Toast.LENGTH_SHORT).show();
                                    arrayList.remove(which_item);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(),"Comment is deleted",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Comment can not be deleted by you",Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No",null).show();
                return true;
            }
        });


    }
}
