package com.example.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    //They are all named constants
    public final String USER_TABLE ="user";
    public final String COURSES_TABLE ="courses";
    public final String STD_COURSE_TABLE ="stdCourses";
    public final String ANNOUNCEMENTS_TABLE="announcements";
    public final String ASSIGNMENTS_TABLE="assignments";
    public final String ASSIGNMENTS_ANSWER_TABLE="assignments_answers";
    public final String MESSAGE_TABLE="messages";
    public final String POSTS_TABLE="posts";
    public final String COMMENTS_TABLE="comments";



    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table "+USER_TABLE+"(email text primary key,password text,type text)");
        db.execSQL("Create table "+COURSES_TABLE+"(ID integer primary key AUTOINCREMENT, Code text,course_name text,lecturer_email text)");
        db.execSQL("Create table "+STD_COURSE_TABLE+"(ID integer primary key AUTOINCREMENT, Code text,course_name text,std_email text)");
        db.execSQL("Create table "+ANNOUNCEMENTS_TABLE+"(ID integer primary key AUTOINCREMENT, subject text,content text,lecturer_email text)");
        db.execSQL("Create table "+ASSIGNMENTS_TABLE+"(ID integer primary key AUTOINCREMENT, Code text,description text,lecturer_email text)");
        db.execSQL("Create table "+ASSIGNMENTS_ANSWER_TABLE+"(ID integer primary key AUTOINCREMENT, Code text,description text,answer text,std_email text)");
        db.execSQL("Create table "+MESSAGE_TABLE+"(ID integer primary key AUTOINCREMENT,sender_email text,receiver_email text, message text)");
        db.execSQL("Create table "+POSTS_TABLE+"(ID integer primary key AUTOINCREMENT,course_code text,teacher_email text,post text)");
        db.execSQL("Create table "+COMMENTS_TABLE+"(ID integer primary key AUTOINCREMENT,course_code text,post_ID text,commentator_email text,comment text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+USER_TABLE);
        db.execSQL("drop table if exists "+COURSES_TABLE);
        db.execSQL("drop table if exists "+STD_COURSE_TABLE);
        db.execSQL("drop table if exists "+ANNOUNCEMENTS_TABLE);
        db.execSQL("drop table if exists "+ASSIGNMENTS_TABLE);
        db.execSQL("drop table if exists "+ASSIGNMENTS_ANSWER_TABLE);
        db.execSQL("drop table if exists "+MESSAGE_TABLE);
        db.execSQL("drop table if exists "+POSTS_TABLE);
        db.execSQL("drop table if exists "+COMMENTS_TABLE);

        onCreate(db);
    }

    //INSERTING TO user Table
    public boolean insertUser(String email,String password,String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email",email);
        contentValues.put("password",password);
        contentValues.put("type",type);
        long ins = db.insert(USER_TABLE,null,contentValues);
        if (ins==-1) return false;
        else return true;
    }
    //INSERTING TO course Table
    public boolean insertCourses(String code,String name,String lecturer_email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Code",code);
        contentValues.put("course_name",name);
        contentValues.put("lecturer_email",lecturer_email);
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery("Select * from "+COURSES_TABLE+" where Code=?",new String[]{code});
        if(cursor.getCount()>1) return false;
        else {
            long ins = db.insert(COURSES_TABLE,null,contentValues);
            if (ins==-1)return false;
            return true;
        }
    }
    //INSERTING TO std_courses Table
    public boolean enrollToCourse(String code,String name,String std_email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Code",code);
        contentValues.put("course_name",name);
        contentValues.put("std_email",std_email);
        //eğer o ders codu ile maile sahip öğrenci bir enroll kaydı var ise false return ediyoruz
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery("Select * from "+STD_COURSE_TABLE+" where Code=? and std_email=? ",new String[]{code,std_email});
        if(cursor.getCount()!=0 ) return false;
        else {
            long ins = db.insert(STD_COURSE_TABLE, null, contentValues);
            if (ins == -1) return false;
            return true;
        }
    }
    //INSERTING TO  announcements table
    public boolean publishAnnouncement(String subject,String content,String lecturer_mail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("subject",subject);
        contentValues.put("content",content);
        contentValues.put("lecturer_email",lecturer_mail);
        //eğer o ders codu ile maile sahip öğrenci bir enroll kaydı var ise false return ediyoruz
        //SQLiteDatabase db2 = this.getReadableDatabase();
        //Cursor cursor = db2.rawQuery("Select * from "+ANNOUNCEMENTS_TABLE+" where subject=? and content=? and lecturer_email=? ",new String[]{subject,content,lecturer_mail});
        //if(cursor.getCount()!=0 ) return false;
        long ins = db.insert(ANNOUNCEMENTS_TABLE, null, contentValues);
        if (ins == -1) return false;
        else return true;

    }
    //INSERTING TO assignments table
    public boolean creatAssignment(String Code,String description,String lecturer_mail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Code",Code);
        contentValues.put("description",description);
        contentValues.put("lecturer_email",lecturer_mail);
        //O ders o hocanın ise assignment oluşturabilisin
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery("Select * from "+COURSES_TABLE+" where Code=? and lecturer_email=? ",new String[]{Code,lecturer_mail});
        if(cursor.getCount()<1 ) {return false;}
        else {
            long ins = db.insert(ASSIGNMENTS_TABLE, null, contentValues);
            if (ins == -1) return false;
            else return true;
        }
    }
    //INSERTING to assignments answer table
    public boolean answerAssignment(String Code,String description,String answer, String std_email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Code",Code);
        contentValues.put("description",description);
        contentValues.put("answer",answer);
        contentValues.put("std_email",std_email);
        //O ders o hocanın ise assignment oluşturabilisin
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor2=db2.rawQuery("Select * from "+ASSIGNMENTS_TABLE+" where Code=? and description=?",new String[]{Code,description});
        Cursor cursor = db2.rawQuery("Select * from "+ASSIGNMENTS_ANSWER_TABLE+" where Code=? and description=? and answer=? and std_email=? ",new String[]{Code,description,answer,std_email});
        if(cursor.getCount()>0 &&cursor2.getCount()<1) {return false;}
        else {
            long ins = db.insert(ASSIGNMENTS_ANSWER_TABLE, null, contentValues);
            if (ins == -1) return false;
            else return true;
        }
    }
    //INSERTING TO messages table
    public boolean sendMessage(String sender,String receiver,String message){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sender_email",sender);
        contentValues.put("receiver_email",receiver);
        contentValues.put("message",message);
        long ins = db.insert(MESSAGE_TABLE, null, contentValues);
        if (ins == -1) return false;
        else return true;


    }
    //INSERTING TO POSTS table
    public boolean publishPost(String course_code,String teacher_email,String post){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("course_code",course_code);
        contentValues.put("teacher_email",teacher_email);
        contentValues.put("post",post);
        long ins = db.insert(POSTS_TABLE, null, contentValues);
        if (ins == -1) return false;
        else return true;
    }
    //INSERTING TO COMMENTS table
    public boolean makeComment(String course_code,String commentator_email,String comment,String post_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("course_code",course_code);
        contentValues.put("commentator_email",commentator_email);
        contentValues.put("comment",comment);
        contentValues.put("post_ID",post_ID);
        long ins = db.insert(COMMENTS_TABLE, null, contentValues);
        if (ins == -1) return false;
        else return true;
    }


    //Delete course
    public void deleteCourse(String code,String email){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(COURSES_TABLE,"Code = ? and lecturer_email = ?",new String[]{code,email});
        db.close();
    }
    public void deleteStdCourse(String code,String email){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(STD_COURSE_TABLE,"Code = ? and std_email = ?",new String[]{code,email});
        db.close();
    }
    //Delete annoncement
    public void deleteAnnouncement(String subject,String content,String lecturer_mail){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(ANNOUNCEMENTS_TABLE,"subject = ? and content = ? and lecturer_email = ? ",new String[]{subject,content,lecturer_mail});
        db.close();
    }
    //Delete assignment
    public void deleteAssignment(String code,String description){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(ASSIGNMENTS_TABLE,"Code = ? and description = ?",new String[]{code,description});
        db.close();
    }
    public void deleteAnswer(String Code,String description, String answer, String std_email){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(ASSIGNMENTS_ANSWER_TABLE,"Code = ? and description = ? and std_email= ? ",new String[]{Code,description,std_email});
        db.close();

    }
    public void deleteAnswersByTeacher(String Code){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(ASSIGNMENTS_ANSWER_TABLE,"Code = ? ",new String[]{Code});
        db.close();
    }
    //delete post
    public void deletePost(String course_code, String teacher_email, String post_content){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(POSTS_TABLE,"course_code = ? and teacher_email = ? and post = ? ",new String[]{course_code,teacher_email,post_content});
        db.close();
    }
    //delete comment
    public void deleteComment(String postID, String commentator_mail, String comment ){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(COMMENTS_TABLE,"post_ID = ? and commentator_email = ? and comment = ? ",new String[]{postID,commentator_mail,comment});
        db.close();
    }
    //delete comment by POSTID
    public void deleteCommentsByPostId(String post_ID){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(COMMENTS_TABLE,"post_ID = ?",new String[]{post_ID});
        db.close();
    }


    //Get teacher courses table info
    public ArrayList getCourses(String email){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+COURSES_TABLE+" where lecturer_email = ? ",new String[]{email});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("Code"))
                            +"|"+cursor.getString(cursor.getColumnIndex("course_name"))
                            +"|"+cursor.getString(cursor.getColumnIndex("lecturer_email"));
            arrayList.add(info);
            cursor.moveToNext();
        }

        return arrayList;
    }
    //Get student courses table info
    public ArrayList getStdCourses(String email){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+STD_COURSE_TABLE+" where std_email = ? ",new String[]{email});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("Code"))
                    +"|"+cursor.getString(cursor.getColumnIndex("course_name"))
                    +"|"+cursor.getString(cursor.getColumnIndex("std_email"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }

        return arrayList;
    }
    //Get announcement table info
    public ArrayList getAnnouncements(String lecturer_mail){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ANNOUNCEMENTS_TABLE+" where lecturer_email = ? ",new String[]{lecturer_mail});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("subject"))
                    +"|"+cursor.getString(cursor.getColumnIndex("content"))
                    +"|"+cursor.getString(cursor.getColumnIndex("lecturer_email"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }

        return arrayList;

    }
    public ArrayList getAllAnnouncement(){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ANNOUNCEMENTS_TABLE,new String[]{});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("subject"))
                    +"|"+cursor.getString(cursor.getColumnIndex("content"))
                    +"|"+cursor.getString(cursor.getColumnIndex("lecturer_email"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }

        return arrayList;


    }
    //GetAssignmentsforTeacher
    public ArrayList getAssigmentsTeacher(String lecturer_mail){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ASSIGNMENTS_TABLE+" where lecturer_email = ? ",new String[]{lecturer_mail});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("Code"))
                    +"|"+cursor.getString(cursor.getColumnIndex("description"))
                    +"|"+cursor.getString(cursor.getColumnIndex("lecturer_email"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }

        return arrayList;

    }
    //GetStdAnswers for Assignment FOR teacher
    public ArrayList getAnswers(String Code,String description){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ASSIGNMENTS_ANSWER_TABLE+" where Code = ? and description=? ",new String[]{Code,description});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("Code"))
                    +"|"+cursor.getString(cursor.getColumnIndex("description"))
                    +"|"+cursor.getString(cursor.getColumnIndex("answer"))
                    +"|"+cursor.getString(cursor.getColumnIndex("std_email"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }

        return arrayList;

    }
    //Single user ın cevabı hemen aşaığıda göstermek için answers table ından alındı
    public ArrayList getSingleAnswer(String Code,String description,String std_email){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ASSIGNMENTS_ANSWER_TABLE+" where Code = ? and description=? and std_email=?",new String[]{Code,description,std_email});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("Code"))
                    +"|"+cursor.getString(cursor.getColumnIndex("description"))
                    +"|"+cursor.getString(cursor.getColumnIndex("answer"))
                    +"|"+cursor.getString(cursor.getColumnIndex("std_email"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }

        return arrayList;

    }
    //For student(can see only assignments of course he enrolled)
    public ArrayList getAssignmentsStd(String Email){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ASSIGNMENTS_TABLE+" where Code IN (SELECT Code FROM "+STD_COURSE_TABLE+" WHERE std_email=? ) ",new String[]{Email});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("Code"))
                    +"|"+cursor.getString(cursor.getColumnIndex("description"))
                    +"|"+cursor.getString(cursor.getColumnIndex("lecturer_email"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }
        return arrayList;

    }
    //Get sent emails
    public ArrayList getSentMessages(String Email){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+MESSAGE_TABLE+" where sender_email=?",new String[]{Email});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("sender_email"))
                    +"|"+cursor.getString(cursor.getColumnIndex("receiver_email"))
                    +"|"+cursor.getString(cursor.getColumnIndex("message"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }
        return arrayList;


    }
    //Get received emails
    public ArrayList getReceivedMessages(String Email){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+MESSAGE_TABLE+" where receiver_email=?",new String[]{Email});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("sender_email"))
                    +"|"+cursor.getString(cursor.getColumnIndex("receiver_email"))
                    +"|"+cursor.getString(cursor.getColumnIndex("message"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }
        return arrayList;

    }
    //Get Posts son indeks post ID
    public ArrayList getPosts(String course_code){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+POSTS_TABLE+" where course_code=?",new String[]{course_code});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("course_code"))
                    +"|"+cursor.getString(cursor.getColumnIndex("teacher_email"))
                    +"|"+cursor.getString(cursor.getColumnIndex("post"))
                    +"|"+cursor.getString(cursor.getColumnIndex("ID"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }
        return arrayList;
    }
    //Get comments
    public ArrayList getComments(String course_code,String post_ID ){
        //Get readable Database
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        //Performing query
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+COMMENTS_TABLE+" where course_code=? and post_ID=?",new String[]{course_code,post_ID});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String info=cursor.getString(cursor.getColumnIndex("course_code"))
                    +"|"+cursor.getString(cursor.getColumnIndex("commentator_email"))
                    +"|"+cursor.getString(cursor.getColumnIndex("comment"));
            if(!info.equals(null))
                arrayList.add(info);
            cursor.moveToNext();
        }
        return arrayList;


    }
    //Get counts of comments for posts
    public int getNumOfComments(String post_ID){
        //Get readable Database
        String countQuery = "SELECT  * FROM " + COMMENTS_TABLE+ " where post_ID=?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, new String[]{post_ID});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }




    //Checks if email exists
    public boolean checkEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+USER_TABLE+" where email=?",new String[]{email});
        if(cursor.getCount()>0) return false;
        else return true;
    }
    public boolean checkEmailPassword(String email,String password,String type){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+USER_TABLE+" where email=? and password=? and type=?",new String[]{email,password,type});
        if(cursor.getCount()>0) return true;
        else return false;

    }
    public boolean editDB(String email){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+USER_TABLE+" where email=?",new String[]{email});
        if(cursor.getCount()>0) return false;
        else return true;

    }
    public Cursor getInformation(){
        SQLiteDatabase db= this.getReadableDatabase();
       Cursor cursor = db.rawQuery("select * from "+USER_TABLE+"",null);
       return cursor;
    }

}
