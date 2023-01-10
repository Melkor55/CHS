package com.example.sma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import Handlers.LoginHandler;
import Models.User;
import Utils.ArrayFunctions;

public class LoginActivity extends AppCompatActivity {

    TextView textViewStatus ;
    //EditText usernameField;
    EditText emailField;
    EditText passwordField;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewStatus = findViewById(R.id.textViewStatus);
        //usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton2);

        ArrayFunctions arrayFunctions = new ArrayFunctions(){};
        String[] data = new String[2];

//        URL url = new URL("https://api.myjson.com/bins/k3p10");
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        InputStream inputStream = httpURLConnection.getInputStream();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        String line = "";
//        while(line != null){
//            line = bufferedReader.readLine();
//            data = data + line;
//        }

        String localhost = "192.168.43.51";
        String login_url_server = "https://csh-nodejs-api.azurewebsites.net/api/login";
        String login_url_local = "http://" + localhost + ":8090/api/login";

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //data[0] = String.valueOf(username.getText());
                data[0] = String.valueOf(emailField.getText());
                data[1] = String.valueOf(passwordField.getText());

                User user;

                printArray(data);
                System.out.println(Arrays.toString(data));
                //if(data.length == 0)
                //System.out.println("null");
                if(!checkIFArrayIsNull(data))
                {

                    user = new User(data[0], data[1]);

                    LoginHandler loginHandler = new LoginHandler(LoginActivity.this, user, login_url_local, textViewStatus);

                    loginHandler.execute();

                    String loginSuccess = "";

                    try {
                        loginSuccess = loginHandler.get();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    for(int i = 0 ; i < 1000 ; i ++)
//                    {
//
//                        else {
//                            System.out.println("---- Status ----" + loginHandler.status);
//                            try {
//                                Thread.sleep(100);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
                    if (loginHandler.status >= 100 && loginHandler.status<400 )
                    {
                        System.out.println("---- Loged In ----" + loginSuccess);
                        user = loginHandler.getUser();
                        System.out.println("User us :" + user);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("CurrentUser", (Serializable) user);
                        startActivity(intent);
                        //break;
                    }

                    System.out.println(" lOGIN --> " + loginHandler.isLoginSuccessful());
//                    if( !loginHandler.isLoginSuccessful() )
//                    {
//                        setHintTextAndColor(emailField,"Incorrect", R.color.red);
//                        setHintTextAndColor(passwordField,"Incorrect", R.color.red);
//                        //Toast.makeText(LoginActivity.this, "The Email and/or Password are incorrect !", Toast.LENGTH_LONG).show();
//                    }
                    //textViewStatus = loginHandler.getTextViewStatus();
                }
                else
                {
                    if ( data[0].isEmpty() && data[1].isEmpty())
                    {
                        setHintTextAndColor(emailField,"Empty Email Field", R.color.red);
                        setHintTextAndColor(passwordField,"Empty Email Field", R.color.red);
                        Toast.makeText(LoginActivity.this, "The Email and Password Fields are Empty !", Toast.LENGTH_LONG).show();
                    }
                    else if( data[0].isEmpty() )
                    {
//                        emailField.setHint("Empty Email Field");
//                        emailField.setHintTextColor(ContextCompat.getColor(v.getContext(), R.color.red));
                        setHintTextAndColor(emailField,"Empty Email Field", R.color.red);
                        Toast.makeText(LoginActivity.this, "The Email Field is Empty !", Toast.LENGTH_LONG).show();
                    }
                    else if( data[1].isEmpty() )
                    {
                        setHintTextAndColor(passwordField,"Empty Password Field", R.color.red);
                        Toast.makeText(LoginActivity.this, "The Password Field is Empty !", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

    }

    void printArray( Object[] array )
    {
        for (int i = 0; i < array.length; i++)
        {
            System.out.println("Array[" + i + "] = '" + array[i] + "'");

        }
    }

    void setHintTextAndColor(TextView textView, String hintMessage, int color)
    {
        textView.setHint(hintMessage);
        //textView.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        textView.setHintTextColor(getResources().getColor(color));
        textView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(color)));
    }

    boolean checkIFArrayIsNull( Object[] array/*, int startPosition, int endPosition */ )
    {
        if (array == null)
        {
            System.out.println("Array is Null");
            return true;
        }
        else if (array.length == 0)
        {
            System.out.println("Array is Empty");
            return true;
        }
        else
        {
            for (int i = 0; i < array.length; i++)
            {
                if ((array[i] == null) || (array[i].equals("")))
                {
                    System.out.println("Element '" + i + "' of the Array is Null");
                    return true;
                }
            }
        }
        return false;
    }

}