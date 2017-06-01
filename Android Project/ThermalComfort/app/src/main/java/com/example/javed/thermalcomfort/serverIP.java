package com.example.javed.thermalcomfort;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.InputFilter;

/**
 * Created by Javed on 9/7/2016.
 */
public class serverIP extends AppCompatActivity
{

    private static EditText server_ip;
    private static Button okbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipserver);

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar3);

        okbtn = (Button) findViewById(R.id.ipsubmit);
        server_ip = (EditText) findViewById(R.id.serverip);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Connection Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //add back button on action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverip = server_ip.getText().toString();
                if (serverip.length()<7){
                    Toast.makeText(serverIP.this, "Invalid IP",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("serveripvalue",serverip);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }
            }
        });

    }

    //hide keyboard if touch outside text field
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
