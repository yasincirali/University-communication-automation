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

public class AssignmentTeacher extends AppCompatActivity {
    public static  final String ASSIGN_INFO = "com.example.myapplication.EMAIL_TEXT";
    private Toolbar actionbarAssignment;
    DatabaseHelper db;
    private EditText code;
    private EditText description;
    private Button assign;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_teacher);

        Intent intent3=getIntent();
        final String Email= (String) intent3.getStringExtra(TeacherPage.EMAIL_TEXT);
        code=(EditText)findViewById(R.id.Course_Code);
        description=(EditText)findViewById(R.id.assignment_description);
        assign=(Button)findViewById(R.id.give_assignment);
        db=new DatabaseHelper(this);

        //db=new DatabaseHelper(this);
        actionbarAssignment=(Toolbar)findViewById(R.id.assignmentActionbar);
        setSupportActionBar(actionbarAssignment);
        getSupportActionBar().setTitle("                    Assignments");

        ListView listView=(ListView)findViewById(R.id.listOfAssignments);
        arrayList=db.getAssigmentsTeacher(Email);
        // arrayList.add("");
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String course_code=code.getText().toString();
                String assignment_description=description.getText().toString();
                boolean insert =db.creatAssignment(course_code,assignment_description,Email);
                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Assignment created Successfully", Toast.LENGTH_SHORT).show();
                    arrayList.add(course_code+"|"+assignment_description+"|"+Email);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getApplicationContext(), "Could not created!", Toast.LENGTH_SHORT).show();
            }
        });

        //üstüne tıklayınca info toast u çıkıyor
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String assignmentInfo=adapter.getItem(position);
                Intent intent=new Intent(AssignmentTeacher.this,AssignAnswersTeacher.class);
                intent.putExtra(ASSIGN_INFO,assignmentInfo);
                startActivity(intent);
            }
        });

        //Üstüne uzun tıklayıp silmek için
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_item=position;
                String[] info=(arrayList.get(which_item)).split("\\|");
                new AlertDialog.Builder(AssignmentTeacher.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete "+info[1]+" course? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //arraylist ve db den silindi
                                String[] info=(arrayList.get(which_item)).split("\\|");
                                db.deleteAssignment(info[0],info[1]);
                                //Aynı anda öğrenci cevaplarını da siler
                                db.deleteAnswersByTeacher(info[0]);
                                //BURADA CEVAPLAR DA SİLİNMELİ

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
