package com.example.sma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Handlers.RegisterHandler;
import Models.User;
import Utils.ArrayFunctions;
import Utils.ExtraFunctions;

public class RegisterActivity extends AppCompatActivity {

    EditText firstNameField ;
    EditText lastNameField ;
    EditText emailField ;
    EditText passwordField ;
    NumberPicker ageNumberPicker;
    EditText heightField;
    EditText weightField;

    TextView textViewStatus;
    Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        ageNumberPicker = findViewById(R.id.ageNumberPicker);
        heightField = findViewById(R.id.heightField);
        weightField = findViewById(R.id.weightField);

        textViewStatus = findViewById(R.id.textViewStatus);
        registerButton = findViewById(R.id.registerButton2);

        float density = this.getResources().getDisplayMetrics().density;
        //float px = someDpValue * density;
        //float dp = somePxValue / density;

        ageNumberPicker.setMinValue(14);
        ageNumberPicker.setMaxValue(99);
        ageNumberPicker.setValue(20);
        ageNumberPicker.setWrapSelectorWheel(false);    //  stops scroll loopback
        ageNumberPicker.setTextSize( 12 * density);

        ArrayFunctions arrayFunctions = new ArrayFunctions(){};
        String[] data = new String[7];
        ExtraFunctions extraFunctions = new ExtraFunctions();

        String localhost = "192.168.43.51";//"192.168.1.102";//"192.168.1.7";
        String login_url_server = "https://csh-nodejs-api.azurewebsites.net/api/users";
        String login_url_local = "http://" + localhost + ":8090/api/register";

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data[0] = String.valueOf(firstNameField.getText());
                data[1] = String.valueOf(lastNameField.getText());
                data[2] = String.valueOf(emailField.getText());
                data[3] = String.valueOf(passwordField.getText());
                data[4] = String.valueOf(ageNumberPicker.getValue());
                data[5] = String.valueOf(heightField.getText());
                data[6] = String.valueOf(weightField.getText());

                textViewStatus.setText("");

                User user = new User();

                arrayFunctions.printArray(data);
                System.out.println(Arrays.toString(data));

                extraFunctions.checkForEmptyData(
                        data,
                        user.getFieldNameString(),
                        new TextView[]{firstNameField,lastNameField,emailField,passwordField,null,weightField,heightField},
                        RegisterActivity.this,
                        getResources(),
                        R.color.red
                );
                if(!data[2].isEmpty()) {
                    if (isEmailValid(data[2])) {
                        System.out.println(data[2] + " ---> valid");
                        if (!arrayFunctions.checkIFArrayIsNull(data)) {
                            //System.out.println("not null");
                            user = new User(data[0], data[1], data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]));
                            //System.out.println(user);

                            RegisterHandler registerHandler = new RegisterHandler(RegisterActivity.this, user, login_url_local, textViewStatus);

                            try {
                                registerHandler.execute().get();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (registerHandler.status >= 100 && registerHandler.status<400 )
                            {
                                System.out.println("---- Registered ----");
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                intent.putExtra("CurrentUser", (Serializable) user);
                                startActivity(intent);
                            }
                        } else {
                            System.out.println("null");
                        }
                    } else {
                        System.out.println(data[2] + " ---> NOT valid");
                        Toast.makeText(RegisterActivity.this, "Email Address is not Valid", Toast.LENGTH_LONG).show();
                        extraFunctions.setHintTextAndColor(emailField, "Email is incorrect, please enter a valid Email", getResources().getColor(R.color.red));
                    }
                }

            }
        });

    }

    public boolean isEmailValid(String email) {


        String regEx = "^(?:(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*)[^\\s]{8,}$"; // for password
        String regex = "^[a-zA-Z0-9]{1,}([_\\.-][a-zA-Z0-9]{1,})?@[a-zA-Z0-9]*-?[a-zA-Z0-9]*\\.[a-z]{2,}$";

        Pattern pattern = Pattern.compile(regex,Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

}