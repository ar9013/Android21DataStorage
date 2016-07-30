package com.javaclass.anima.android21datastorage;

import android.Manifest;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    
    private View save1,read1,saveInnerIO,readInnerIO,saveSDCard,readSDCard,insertSqlite,querySqlite,deleteSqlite,updateSqlite;
    private TextView info;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private File sdRoot,appRoot;
    int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;

    private MyDBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new MyDBHelper(this,"brad",null,1);
        db = dbHelper.getWritableDatabase();

        insertSqlite = findViewById(R.id.insertSqlite);
        querySqlite = findViewById(R.id.querySqlite);
        deleteSqlite = findViewById(R.id.deleteSqlite);
        updateSqlite = findViewById(R.id.updateSqlite);

        insertSqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertSqliteData();
            }
        });

        querySqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                querySqliteData();
            }
        });

        deleteSqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSQLiteData();
            }
        });

        updateSqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSqlite();
            }
        });

        sdRoot = Environment.getExternalStorageDirectory();
        appRoot = new File(sdRoot,"Android/data/"+getPackageName());

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }

        if(!appRoot.exists()){
            appRoot.mkdirs();
        }

        sp= getSharedPreferences("game",MODE_PRIVATE);
        editor = sp.edit();
        
        info = (TextView) findViewById(R.id.info);
        save1 = findViewById(R.id.save1);
        read1 = findViewById(R.id.read1);
        saveInnerIO = findViewById(R.id.saveInnerIO);
        readInnerIO = findViewById(R.id.readInnerIO);
        saveSDCard = findViewById(R.id.saveSDCard);
        readSDCard = findViewById(R.id.readSDCard);

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreferences();
            }
        });

        read1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readPreferences();
            }
        });

        saveInnerIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInnerIO();
            }
        });

        readInnerIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readInnerIO();
            }
        });

        saveSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSDCard();
            }
        });

        readSDCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readSDCard();
            }
        });
    }

    private void savePreferences(){
        // 清除資料

        editor.putBoolean("send",true);
        editor.putInt("stage",4);
        editor.putString("user","brad");
        editor.commit();

        Toast.makeText(this, "Save OK", Toast.LENGTH_SHORT).show();
    }

    private void readPreferences(){

        String user = sp.getString("user","nobody");
        boolean sound = sp.getBoolean("sound",false);
        int stage = sp.getInt("stage",0);

        info.setText("User : "+user+"\n"+"Sound : "+(sound?"on":"off")+"\n"+"Stage : "+stage);
    }

    private void saveInnerIO(){
        FileOutputStream fout ;
        String data = "Hello , World! \n 我是羅康瑀";
        try{
            fout = openFileOutput("MyData.txt",MODE_PRIVATE);
            fout.write(data.getBytes());
            fout.flush();
            fout.close();

        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readInnerIO(){
        FileInputStream fin;

        try{
            fin = openFileInput("MyData.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
            String line;
            info.setText("");
            while ((line = reader.readLine())!= null){
                info.append(line+"\n");
            }
            reader.close();

            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void saveSDCard(){
        String data = "Hello , SDCard";
        FileOutputStream fout;
        try{
            fout = new FileOutputStream(new File(appRoot,"brad.data"));
            fout.write(data.getBytes());
            fout.flush();
            fout.close();

            Toast.makeText(getApplicationContext(),"saveSDCard OK",Toast.LENGTH_LONG).show();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readSDCard(){
        FileInputStream fin ;
        try{
            info.setText("");
            fin = new FileInputStream(new File(appRoot,"brad.data"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(fin));

            String line;

            while ((line = reader.readLine())!= null){
                info.append(line+"\n");
                Log.d("rural",line);
            }
            reader.close();
            fin.close();

        }catch (FileNotFoundException e){

        }catch (IOException e){

        }
    }

    @Override
    public void finish() {

        if(db.isOpen()){
            db.close();
        }
        super.finish();
    }

    private void insertSqliteData(){
        ContentValues values = new ContentValues();
        values.put("name","test"+(int)(Math.random()*100));
        values.put("tel","0999-123456");
        values.put("birthday","2001-02-01");
        db.insert("cust",null,values);
    }

    private void querySqliteData() {
	//	Cursor c = db.query("cust", null, null, null, null, null, null);
        Cursor c = db.query("cust", new String[] { "_id", "name", "birthday" },
                "_id > ?", new String[] { "4" }, null, null, "_id");
        info.setText("筆數:" + c.getCount() + "\n");
        while (c.moveToNext()) {
            info.append(c.getString(c.getColumnIndex("_id")) + ":"
                    + c.getString(c.getColumnIndex("name")) + ":"
//					+ c.getString(c.getColumnIndex("tel")) + ":"
                    + c.getString(c.getColumnIndex("birthday")) + "\n");
        }
    }

    private void deleteSQLiteData() {
        // db.delete("cust", "name like ?", new String[]{"test_"});
        db.delete("cust", "name like ?", new String[] { "test%" });
    }

    private void updateSqlite(){
        ContentValues values = new ContentValues();
        values.put("name","Luo 趙令文");
        values.put("birthday","1999-01-01");
        db.update("cust",values,"name =?",new String[]{"Brad"});
    }
}
