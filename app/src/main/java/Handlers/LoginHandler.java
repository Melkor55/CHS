package Handlers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.sma.LoginActivity;
import com.example.sma.R;

import org.json.JSONArray;
import org.json.JSONObject;

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

import Models.User;

public class LoginHandler extends AsyncTask<String, Void, String> {

    AlertDialog alertDialog;
    private TextView textViewStatus;
    private final Context context;
    public int status;
    ProgressDialog progressDialog;
    private final String URL;
//    private String username ;
//    private String password ;
    private  User user;


    private boolean loginSuccessful;

//    public LoginHandler(Context context, String username, String password, String URL)
//    {
//        this.context = context;
//        this.username = username;
//        this.password = password;
//        this.URL = URL;
//    }

    public LoginHandler(Context context, User user, String URL,TextView textViewStatus)
    {
        this.context = context;
        this.user = user;
        this.URL = URL;
        this.textViewStatus = textViewStatus;
        this.loginSuccessful = false;
    }

    public boolean isLoginSuccessful() {
        return this.loginSuccessful;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        this.loginSuccessful = loginSuccessful;
    }

    public TextView getTextViewStatus() {
        return textViewStatus;
    }

    public void setTextViewStatus(TextView textViewStatus) {
        this.textViewStatus = textViewStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(context, "", "Logging you in...... Please wait");
    }

    @Override
    protected String doInBackground(String... params) {
        //String login_url = "https://csh-nodejs-api.azurewebsites.net/api/users";

        try {
            URL url = new URL(URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(outputStream, "UTF-8"));

            String data = URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(user.getEmail(), "UTF-8") + "&"
                    + URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(user.getPassword(), "UTF-8");

            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            status = connection.getResponseCode();
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
            try {
//                JSONArray jsonarray = new JSONArray(""+response);
//                for (int i = 0; i < jsonarray.length(); i++) {
//                    JSONObject jsonobject = jsonarray.getJSONObject(i);
//                    //status = connection.getResponseCode();
//                }
                //status = connection.getResponseCode();
                JSONObject jsonObject = new JSONObject(""+response);
                JSONObject jsonUser = jsonObject.getJSONArray("User").getJSONObject(0);
                //System.out.println(status);
                user = new User(jsonUser.getInt("UserId"),
                                jsonUser.getString("FirstName"),
                                jsonUser.getString("LastName"),
                                jsonUser.getString("Email"),
                                jsonUser.getString("Password"),
                                jsonUser.getInt("Age"),
                                jsonUser.getInt("Height"),
                                jsonUser.getInt("Weight"));
                System.out.println(jsonObject + "\n" + jsonUser);
            } catch (Exception e) {
                e.printStackTrace();
                //return response;
                return "fail";
            }
            return "good";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "fail";
        } catch (IOException ee) {
            ee.printStackTrace();
            return "fail";
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result)
    {
        progressDialog.dismiss();
        if (result.equals("fail")) {
            progressDialog.dismiss();
        }
        if (status >= 100 && status < 400) {
            // usernametext.setText("");
            //passwordtext.setText("");

            progressDialog.dismiss();
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
            //setLoginSuccessful(true);
            //loginSuccessful = true;
            //System.out.println(" log-> " + isLoginSuccessful());
        }
        else if (status == 404)
        {
            progressDialog.dismiss();
            Toast.makeText(context, "Login Failed - User Not Found", Toast.LENGTH_LONG).show();
            textViewStatus.setText("A User with these credentials doesn't exist ! Please insert a correct email address and password !");
            textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        else if (status == 409)
        {
            progressDialog.dismiss();
            Toast.makeText(context, "Login Failed - Duplicate Entry", Toast.LENGTH_LONG).show();
            textViewStatus.setText("The Email Address already exists ! Please insert another email address !");
            textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        else if ((status == 0 || status >= 400) && status != 409)
        {
            progressDialog.dismiss();
            Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }
}
