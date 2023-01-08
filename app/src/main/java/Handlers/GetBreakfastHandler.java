package Handlers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.sma.R;

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

import Models.Meal;
import Models.Product;


public class GetBreakfastHandler extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        private TextView textViewStatus;
        private final Context context;
        int status;
        ProgressDialog progressDialog;
        private final String URL;
        //    private String username ;
//    private String password ;
        private Meal meal;
        private Product food;


        public GetBreakfastHandler(Context context, Meal meal, String URL, TextView textViewStatus) {
            this.context = context;
            this.meal = meal;
            this.URL = URL;
            this.textViewStatus = textViewStatus;
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

                String data = URLEncoder.encode("Email", "UTF-8") + "=" + meal.getMealId() + "&"
                        + URLEncoder.encode("Password", "UTF-8") + "=" + meal.getCalories();

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
                progressDialog.dismiss();
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
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
