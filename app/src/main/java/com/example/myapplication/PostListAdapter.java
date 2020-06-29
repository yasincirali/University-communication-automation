package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PostListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList postList;
    private int id;

    public PostListAdapter(Context context, ArrayList postList,int id) {
        this.context = context;
        this.postList = postList;
        this.id=id;
    }
    //Overloading of constructor
    public PostListAdapter(Context context) {
        this.context = context;
    }
    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= View.inflate(context, R.layout.post_item_list,null);
        TextView tvEmail=(TextView)v.findViewById(R.id.post_teacher_email);
        TextView tvPost=(TextView)v.findViewById(R.id.post_teacher_content);
        TextView tvComment=(TextView)v.findViewById(R.id.post_teacher_comments);
        //Set texts for textView
        String[] infos= postList.get(position).toString().split("\\|");
        tvEmail.setText(infos[1]);
        tvPost.setText(infos[2]);
        int select=this.id;
        //switch case condition
        switch (select){
            case 0:
                tvComment.setVisibility(View.GONE);
            case 1:
                break;
        }

        //tvNumberOfComments.setText();
        //save product id to tag
        v.setTag(infos[2]);
        return v;
    }
}
