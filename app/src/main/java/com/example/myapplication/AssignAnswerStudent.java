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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AssignAnswerStudent extends AppCompatActivity {
private Toolbar actionbarAnswers;
private DatabaseHelper db;
private TextView Code;
private TextView description;
private EditText answer;
private Button submit;
private ArrayList<String> arrayList;
private ArrayAdapter<String> adapter;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_answer_student);
        Intent intent6=getIntent();
        final String assignment_info= (String) intent6.getStringExtra(AssignmentStudent.ASSIGN_INFO);
        String[] info=assignment_info.split("\\|");
        final String coursecode=info[0];
        final String assignment_description=info[1];
        final String stdEmail=info[3];
        db=new DatabaseHelper(this);
        actionbarAnswers=(Toolbar)findViewById(R.id.assignmentAnswerStdActionbar);
        setSupportActionBar(actionbarAnswers);
        getSupportActionBar().setTitle("              "+info[0]+" Assignment");

        Code=(TextView)findViewById(R.id.assignmentCourseCode);
        Code.setText(info[0]);
        description=(TextView)findViewById(R.id.assignmentAnswer_description);
        description.setText(info[1]);
        answer=(EditText) findViewById(R.id.assignmentStdAnswer);
        submit=(Button)findViewById(R.id.submit);

        info[2]=answer.getText().toString();


        ListView listView=(ListView)findViewById(R.id.listOfAnswers);
        arrayList=db.getSingleAnswer(coursecode,assignment_description,stdEmail);
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String StdAnswer=answer.getText().toString();
                boolean insert=db.answerAssignment(coursecode,assignment_description,StdAnswer,stdEmail);
                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Assignment submitted Successfully", Toast.LENGTH_SHORT).show();
                    arrayList.add(coursecode+"|"+assignment_description+"|"+StdAnswer+"|"+stdEmail);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getApplicationContext(), "Could not submitted!", Toast.LENGTH_SHORT).show();
            }
        });
    //Üstüne uzun tıklayıp silmek için
    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
            final int which_item=position;
            String[] info=(arrayList.get(which_item)).split("\\|");
            new AlertDialog.Builder(AssignAnswerStudent.this)
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
                            db.deleteAnswer(info[0],info[1],info[2],info[3]);
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
