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

/**
 * Created by Javed on 9/8/2016.
 */
public class signup extends AppCompatActivity {

    private static EditText name_signup;
    private static EditText username_signup;
    private static EditText password_signup;
    private static EditText repeat_password_signup;
    private static Button btn_signup;

    String name = null;
    String username = null;
    String password = null;
    String repeat_password = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar5);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name_signup = (EditText) findViewById(R.id.name_Signup);
        username_signup = (EditText) findViewById(R.id.username_signup);
        password_signup = (EditText) findViewById(R.id.password_signup);
        repeat_password_signup = (EditText) findViewById(R.id.repeat_password_signup);
        btn_signup = (Button) findViewById(R.id.signup_btn);


        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = name_signup.getText().toString();
                username = username_signup.getText().toString();
                password = password_signup.getText().toString();
                repeat_password = repeat_password_signup.getText().toString();

                if(name.matches("") || username.matches("") || password.matches(""))
                {
                    Toast.makeText(signup.this,"No field can be left blank!",Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals(repeat_password)){
                    Toast.makeText(signup.this,"Passwords don't match!",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("data_signup",name + "\t" + username + "\t" + password);
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
