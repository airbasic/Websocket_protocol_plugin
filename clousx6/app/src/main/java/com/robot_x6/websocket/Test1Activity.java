package com.robot_x6.websocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.*;
import java.util.*;
import android.database.sqlite.*;
import java.io.*;
import android.view.View.*;
import android.view.*;
import android.content.*;
import android.database.*;
import android.util.*;


public class Test1Activity extends AppCompatActivity {

	private ListView listView;
    private BaseAdapter adapter;
    private int counter;
	private EditText editText1;
	private Button button1;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		button1 = (Button) findViewById(R.id.contenttestButton1);
		editText1 = (EditText) findViewById(R.id.contenttestEditText1);
		final ArrayList<String> list = new ArrayList<>();
		
		
		
		
    }
		
    
}
