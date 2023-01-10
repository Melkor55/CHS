package com.example.sma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.WeakHashMap;

import Models.User;
import Utils.ExtraFunctions;

public class ProfileActivity extends AppCompatActivity {

    static User user;
    URL url;

    EditText firstNameField ;
    EditText lastNameField ;
    EditText emailField ;
    EditText passwordField ;
    NumberPicker ageNumberPicker;
    EditText heightField;
    EditText weightField;

    TextView textViewStatus;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        firstNameField = findViewById(R.id.firstNameField);
        lastNameField = findViewById(R.id.lastNameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        ageNumberPicker = findViewById(R.id.ageNumberPicker);
        heightField = findViewById(R.id.heightField);
        weightField = findViewById(R.id.weightField);

        textViewStatus = findViewById(R.id.textViewStatus);
        saveButton = findViewById(R.id.saveButton);

        float density = this.getResources().getDisplayMetrics().density;
        //float px = someDpValue * density;
        //float dp = somePxValue / density;

        ageNumberPicker.setMinValue(14);
        ageNumberPicker.setMaxValue(99);
        //ageNumberPicker.setValue(20);
        ageNumberPicker.setWrapSelectorWheel(false);    //  stops scroll loopback
        ageNumberPicker.setTextSize( 12 * density);

        //  need to and a patch endpoint and copy more or less the register flow
        fillFieldsWithData();


        String[] data = new String[7];
        ExtraFunctions extraFunctions = new ExtraFunctions();

        String localhost = "192.168.43.51";//"192.168.1.102";//"192.168.1.7";
        String login_url_server = "https://csh-nodejs-api.azurewebsites.net/api/users";
        String login_url_local = "http://" + localhost + ":8090/api/updateUser";

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            User updatedUser = new User();

			data[0] = String.valueOf(firstNameField.getText());
			data[1] = String.valueOf(lastNameField.getText());
			data[2] = String.valueOf(emailField.getText());
			data[3] = String.valueOf(passwordField.getText());
			data[4] = String.valueOf(ageNumberPicker.getValue());
			data[5] = String.valueOf(heightField.getText());
			data[6] = String.valueOf(weightField.getText());

            extraFunctions.checkForEmptyData(
                    data,
                    updatedUser.getFieldNameString(),
                    new TextView[]{firstNameField,lastNameField,emailField,passwordField,null,weightField,heightField},
                    ProfileActivity.this,
                    getResources(),
                    R.color.red
            );
            updatedUser = new User(user.getUserId(), data[0], data[1], data[2], data[3], Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]));
            System.out.println("User : " + user);
            System.out.println("Modified User : " + updatedUser);
            if(user.isEqualTo(updatedUser))
                //System.out.println("No Modifications !");
            {
                Toast.makeText(ProfileActivity.this, "No modifications for User !", Toast.LENGTH_SHORT).show();
            }
            else {
                //System.out.println("User Modified !");
                user = updatedUser;
                System.out.println("User Modified !" + user);
                updateProfileHandler(login_url_local);
                System.out.println("User Modified 2 !");
                Toast.makeText(ProfileActivity.this, "User Updated !", Toast.LENGTH_SHORT).show();


            }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("UpdatedUser", user);
                setResult(RESULT_OK, resultIntent);
                this.finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("UpdatedUser", user);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void fillFieldsWithData()
    {
        user = (User) getIntent().getSerializableExtra("CurrentUser");
        //System.out.println(user);
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        ageNumberPicker.setValue(user.getAge());
        heightField.setText(String.valueOf(user.getHeight()));
        weightField.setText(String.valueOf(user.getWeight()));
    }

    private String updateProfileHandler(String stringUrl){

        try {
            URL url = new URL(stringUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            System.out.println("*** User : " + user);

            String data = URLEncoder.encode("UserId", "UTF-8") + "=" + user.getUserId() + "&"
                        + URLEncoder.encode("FirstName", "UTF-8") + "=" + URLEncoder.encode(user.getFirstName(), "UTF-8") + "&"
                        + URLEncoder.encode("LastName", "UTF-8") + "=" + URLEncoder.encode(user.getLastName(), "UTF-8") + "&"
                        + URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&"
                        + URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(user.getPassword(), "UTF-8") + "&"
                        + URLEncoder.encode("Age", "UTF-8") + "=" + user.getAge() + "&"
                        + URLEncoder.encode("Height", "UTF-8") + "=" + user.getHeight() + "&"
                        + URLEncoder.encode("Weight", "UTF-8") + "=" + user.getWeight();

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int status = connection.getResponseCode();
            System.out.println("***Response Status :" + status);

            InputStream inputStream;
            if ( status >= 400 )
                inputStream = connection.getErrorStream();
            else
                inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "iso-8859-1"));

            String response = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }

            bufferedReader.close();
            inputStream.close();
            connection.disconnect();

            System.out.println(response);

            return "good";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "fail";
        } catch (IOException ee) {
            ee.printStackTrace();
            return "fail";
        }
    }
}