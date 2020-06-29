package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MessageAvtivity extends AppCompatActivity {
    public static  final String SENDER_INFO = "com.example.myapplication.EMAIL_TEXT";

    private Toolbar actionbarMessage;
    private EditText message;
    private EditText to;
    private Button send;
    private Button received;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String>adapter;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_avtivity);
        actionbarMessage=(Toolbar)findViewById(R.id.message_actionbar);
        Intent intent7=getIntent();
        final String sender_email= (String) intent7.getStringExtra(TeacherPage.EMAIL_TEXT);
        db=new DatabaseHelper(this);
        setSupportActionBar(actionbarMessage);
        getSupportActionBar().setTitle("                  Messages");
        to=(EditText)findViewById(R.id.receiverToSend);
        message=(EditText)findViewById(R.id.messageToSend);
        send=(Button)findViewById(R.id.sendMessage);
        received=(Button)findViewById(R.id.receivedMessage);

        ListView listView=(ListView)findViewById(R.id.listOfSentMessages);
        arrayList=db.getSentMessages(sender_email);
        adapter= new ArrayAdapter<String>(this,R.layout.list_courses,arrayList);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiver=to.getText().toString();
                String messageToSend=message.getText().toString();
                boolean insert=db.sendMessage(sender_email,receiver,messageToSend);
                if (insert == true) {
                    Toast.makeText(getApplicationContext(), "Message sent Successfully", Toast.LENGTH_SHORT).show();
                    arrayList.add(sender_email+"|"+receiver+"|"+messageToSend);
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getApplicationContext(), "Could not send!", Toast.LENGTH_SHORT).show();
            }
        });
        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageAvtivity.this, TeacherReceivedActivity.class);
                intent.putExtra(SENDER_INFO,sender_email);
                startActivity(intent);
                //finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] str=adapter.getItem(position).split("\\|");
                Toast.makeText(getApplicationContext(),"Message details :  "+"\nsender email: "+str[0]+"\nreceiver email: "+str[1]+"\nmessage: "+str[2]+"\n",Toast.LENGTH_LONG).show();
            }
        });


    }
}
