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
public class signin extends AppCompatActivity{

    private static EditText username_login;
    private static EditText password_login;
    private static Button login_btn;
    String username_entr = null;
    String password_entr = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username_login = (EditText) findViewById(R.id.username);
        password_login = (EditText) findViewById(R.id.password);
        login_btn = (Button) findViewById(R.id.btnlogin);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_entr = username_login.getText().toString();
                password_entr = password_login.getText().toString();

                if(username_entr.matches("") || password_entr.matches("")) {
                    Toast.makeText(signin.this, "No field can be left blank!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("data_signin",username_entr + "\t" + password_entr);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }

//                if (username_entr.isEmpty()){
//                    Toast.makeText(signin.this,"Input a User Name! ",Toast.LENGTH_SHORT).show();
//                }
//                else if (password_entr == null){
//                    Toast.makeText(signin.this,"Input a Password! ",Toast.LENGTH_SHORT).show();
//                }
//                else if (!username_entr.equals("Javed")){
//                    Toast.makeText(signin.this,"User Name doesn't Exist!",Toast.LENGTH_SHORT).show();
//                }
//                else if (!password_entr.equals("12345")) {
//                    Toast.makeText(signin.this, "Wrong Password! ", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(signin.this,"Login Successful!",Toast.LENGTH_SHORT).show();
//                    finish();
//                }
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
