package com.example.javed.thermalcomfort;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.BoolRes;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{

    public static SeekBar seek_bar;
    public static TextView connection_status;
    public static TextView name_of_user;
    public static TextView status_of_signin;
    private boolean threadcomplete = false;
    private boolean connectionstatus = false;
    public Boolean sign_in_status = false;
    public  Boolean sign_in_thread_complete = false;
    public  Boolean sign_up_thread_complete = false;
    public String Name_user = null;
    public String user_name  =null;
    public Boolean ask_cnct_on_start = true;
    public Boolean ask_login_on_start = true;


    private static final String hostname="192.168.1.113";
    public static final int portnumber = 61000;

    public Socket socket = null;

    public static final String debugString = "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seek_bar = (SeekBar)findViewById(R.id.seekBar);
        connection_status = (TextView) findViewById(R.id.connection_stat);
        name_of_user = (TextView) findViewById(R.id.name_user);
        status_of_signin = (TextView) findViewById(R.id.status_signin);
        status_of_signin.setText("Not Logged In");
        status_of_signin.setTextColor(Color.RED);
        connection_status.setText("Not Connected");
        connection_status.setTextColor(Color.RED);


        Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle(R.string.my_tb_title);
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
    //-----------------------end of hide keyboard if touch outside text field-------------------------


    public void seekbbarr()
    {
       // seek_bar = (SeekBar)findViewById(R.id.seekBar);
        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
                    int thermal_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                    {
                        thermal_value = progress;
                       // text_view.setText("Level: "+ (progress-50)/10);
                        //    Toast.makeText(MainActivity.this,"SeekBar in Progress",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar)
                    {
                        //  Toast.makeText(MainActivity.this,"SeekBar in StartTracking",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar)
                    {
                      //  text_view.setText("Level: "+ (thermal_value-50)/10);
                        //   Toast.makeText(MainActivity.this,"SeekBar in StopTracking",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void onSubmit(View v)
    {
        BufferedWriter bw = null;
        //Send message to server
        if (connectionstatus && sign_in_status)
        {
            try {

                //Send data to server
                String level = String.valueOf((seek_bar.getProgress()-50)/10);
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(user_name+ "\t" + level);
                bw.newLine();
                bw.flush();

                Toast.makeText(MainActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();

            }catch (SocketException e)
            {
                Log.e(debugString, e.getMessage());
            }

            catch (IOException e) {
                Log.e(debugString, e.getMessage());
                connectionstatus = false;
                Toast.makeText(MainActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
            }
        } else if (!connectionstatus)
        {
            Toast.makeText(MainActivity.this, "Connection failed!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Please Log In first !", Toast.LENGTH_SHORT).show();
        }

    }

    public void onReset(View v)
    {

        seek_bar.setProgress(50);
        seek_bar.refreshDrawableState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.menu_1:
                if (sign_in_status){
                    Toast.makeText(MainActivity.this,"Please Log Out first !", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent signin_intent = new Intent(MainActivity.this,signin.class);
                    startActivityForResult(signin_intent,1);
                }
                break;
            case R.id.menu_2:
                if (sign_in_status){
                    Toast.makeText(MainActivity.this,"Please Log Out first !", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent signup_intent = new Intent(MainActivity.this, signup.class);
                    startActivityForResult(signup_intent, 2);
                }
                break;
            case R.id.menu_3:
                if (!connectionstatus) {
                    Intent serverip_intent = new Intent(MainActivity.this, serverIP.class);
                    startActivityForResult(serverip_intent, 3);
                }
                else {
//                    Toast.makeText(MainActivity.this,"Already Connected", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_4:
                if (sign_in_status){
                sign_in_status = false;
                user_name = null;
                Name_user = null;
                status_of_signin.setText("Not Logged In");
                status_of_signin.setTextColor(Color.RED);
                name_of_user.setText("");
                Toast.makeText(MainActivity.this, "Log Out Success !", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"Not Logged In !", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_5:
                Intent about_intent = new Intent(MainActivity.this,about.class);
                startActivity(about_intent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestcode,int resultcode, Intent data)
    {
        super.onActivityResult(requestcode,resultcode,data);

        //---------------Sign In---------------------------------------------------------------------
        if (requestcode == 1)
        {
            if (resultcode == RESULT_OK)
            {

                String signin_data_received = data.getStringExtra("data_signin");
                String[] signin_data = signin_data_received.split("\t");
                Boolean request_for_login = false;

                if(connectionstatus)
                {
                    sign_in_thread_complete = false;
                    Toast.makeText(MainActivity.this, "Attempting to Log In...!", Toast.LENGTH_SHORT).show();
                    user_signin u = new user_signin();
                    u.execute(signin_data);
                    while(!sign_in_thread_complete);
                    if (!sign_in_status){
                        status_of_signin.setText("Not Logged In");
                        status_of_signin.setTextColor(Color.RED);
                        name_of_user.setText("");
                        Toast.makeText(MainActivity.this, "Failed to Log In !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        status_of_signin.setText("Logged In as");
                        status_of_signin.setTextColor(Color.GREEN);
                        Toast.makeText(MainActivity.this, "Login Success !", Toast.LENGTH_SHORT).show();
                        name_of_user.setText(Name_user);
                    }


                }
                else
                    Toast.makeText(MainActivity.this, "Failed to connect...", Toast.LENGTH_SHORT).show();
            }
        }
        //---------------------End of Sign In-----------------------------------------------------------------------------

        //---------------Sign Up---------------------------------------------------------------------
        if (requestcode == 2)
        {
            if (resultcode == RESULT_OK)
            {

                String signup_data_received = data.getStringExtra("data_signup");
                String[] signup_data = signup_data_received.split("\t");

                if(connectionstatus)
                {
                    sign_up_thread_complete = false;
                    Toast.makeText(MainActivity.this, "Attempting to Sign Up...!", Toast.LENGTH_SHORT).show();
                    user_signup u = new user_signup();
                    u.execute(signup_data);
                    while(!sign_up_thread_complete);
                    if (!sign_in_status){
                        status_of_signin.setText("Not Logged In");
                        status_of_signin.setTextColor(Color.RED);
                        name_of_user.setText("");
                        Toast.makeText(MainActivity.this, "User Name already in use !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        status_of_signin.setText("Logged In as");
                        status_of_signin.setTextColor(Color.GREEN);
                        Toast.makeText(MainActivity.this, "Sign Up Success !", Toast.LENGTH_SHORT).show();
                        name_of_user.setText(Name_user);
                    }


                }
                else
                    Toast.makeText(MainActivity.this, "Failed to connect...", Toast.LENGTH_SHORT).show();
            }
        }

        //---------------------End of Sign In-----------------------------------------------------------------------------


        //---------------------Connection Setting-----------------------------------------------------------------------------

        else if (requestcode == 3)
        {
            if (resultcode == RESULT_OK)
            {
                final String server_ip_read = data.getStringExtra("serveripvalue");


               threadcomplete = false;
                if(!connectionstatus) {
                    Toast.makeText(MainActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();

                    new Thread() {
                        @Override
                        public void run() {

                            try {
                                //connecting
                                Log.i(debugString, "Attempting to connect");
                                socket = new Socket(server_ip_read, portnumber);
                                Log.i(debugString, "Connection established");
                                connectionstatus = true;
//                                connection_status.setText("Connected");
//                                connection_status.setTextColor(Color.GREEN);
                            } catch (IOException e) {

                                Log.e(debugString, e.getMessage());

                                connectionstatus = false;
                            } finally {
                                threadcomplete = true;

                            }
                        }

                    }.start();
                }

                while(!threadcomplete);
                if (connectionstatus && threadcomplete){
                    Toast.makeText(MainActivity.this,"Connection established!",Toast.LENGTH_SHORT).show();
                    connection_status.setText("Connected");
                    connection_status.setTextColor(Color.GREEN);
                }
                else if(!connectionstatus && threadcomplete){
                    Toast.makeText(MainActivity.this,"Failed to establish connection!",Toast.LENGTH_SHORT).show();
                }
            }
        }

        //---------------------End of Connection Setting-----------------------------------------------------------------------------



    }
    private class user_signin extends AsyncTask <String, Void, String>
    {
        @Override
        protected String doInBackground(String[] signin_data) {

            try {
                BufferedWriter bw1 = null;
                //send sign in data to server
                bw1 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw1.write("signin" + "\t" + signin_data[0] + "\t" + signin_data[1]);
                bw1.newLine();
                bw1.flush();

                Log.i(debugString, "Attempting to Login...");
            }
            catch (SocketException e)
            {
                Log.e(debugString, e.getMessage());
            }
            catch (IOException e) {
                Log.e(debugString, e.getMessage());
                connectionstatus = false;
            }

            try{
                BufferedReader br_signin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.i(debugString, "Data  received from server...");
                String Server_response = br_signin.readLine();
                Log.i(debugString, "Server response read...");
                System.out.println("Message from the server: " + Server_response);
                        String[] Server_Data = Server_response.split("\t");
                            if (Server_Data[0].equals("yes")) {
                                sign_in_status = true;
                                Name_user = Server_Data[1];
                                user_name = signin_data[0];
                                Log.i(debugString, "Success!");
                            }
                            else
                            {
                                sign_in_status = false;
                                Name_user = null;
                                user_name = null;
                                Log.i(debugString, "Failed to Log In!");
                            }

            }
            catch (IOException e) {
                Log.e(debugString, e.getMessage());
//                        connectionstatus = false;
            }
            finally {
                sign_in_thread_complete = true;
            }

            return null;
        }
    }

    //--------------------------------Signup------------------------
    private class user_signup extends AsyncTask <String, Void, String>
    {
        @Override
        protected String doInBackground(String[] signup_data) {
            try
            {
                BufferedWriter bw2 = null;
                //Register new user to server
                bw2 = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw2.write( "signup" + "\t" + signup_data[0] + "\t" + signup_data[1] + "\t"+  signup_data[2]);
                bw2.newLine();
                bw2.flush();

            }catch (SocketException e)
            {
                Log.e(debugString, e.getMessage());
            }

            catch (IOException e) {
                Log.e(debugString, e.getMessage());
                connectionstatus = false;
            }

            try{
                BufferedReader br_signup = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.i(debugString, "Data  received from server...");
                String Server_response = br_signup.readLine();
                Log.i(debugString, "Server response read...");
                System.out.println("Message from the server: " + Server_response);
                String[] Server_Data = Server_response.split("\t");
                if (Server_Data[0].equals("no")) {
                    sign_in_status = true;
                    Name_user = signup_data[0];
                    user_name = signup_data[1];
                    Log.i(debugString, "Success!");
                }
                else
                {
                    sign_in_status = false;
                    Name_user = null;
                    user_name = null;
                    Log.i(debugString, "User Name not Available!");
                }

            }
            catch (IOException e) {
                Log.e(debugString, e.getMessage());
//                        connectionstatus = false;
            }
            finally {
                sign_up_thread_complete = true;
            }

            return null;
        }
    }

}




