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

import Models.Day;

public class DayHandler extends AsyncTask<String, Void, String> {
        AlertDialog alertDialog;
        private Context context;
        int status;
        ProgressDialog progressDialog;
        private String URL;
        private Day day;

        public DayHandler(Context context, Day day, String URL)
        {
            this.context = context;
            this.day = day;
            this.URL = URL;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(context, "", "Registering you in...... Please wait");
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

                String data = URLEncoder.encode("Date", "UTF-8") + "=" + URLEncoder.encode(day.getDate().toString(), "UTF-8") + "&"
                            + URLEncoder.encode("Meal1Id", "UTF-8") + "=" + day.getMeal1Id()+ "&"
                            + URLEncoder.encode("Meal2Id", "UTF-8") + "=" + day.getMeal2Id() + "&"
                            + URLEncoder.encode("Meal3Id", "UTF-8") + "=" + day.getMeal3Id() + "&"
                            + URLEncoder.encode("Meal4Id", "UTF-8") + "=" + day.getMeal4Id();

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
//            try {
////                JSONArray jsonarray = new JSONArray(""+response);
////                for (int i = 0; i < jsonarray.length(); i++) {
////                    JSONObject jsonobject = jsonarray.getJSONObject(i);
////                    status = connection.getResponseCode();
////                }
//                status = connection.getResponseCode();
//
//                System.out.println(status);
//                System.out.println(response);
//            } catch (Exception e) {
//                e.printStackTrace();
//                //return response;
//                return "fail";
//            }

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
            //progressDialog.dismiss();
            if (result.equals("fail")) {
                progressDialog.dismiss();
            }
            if (status >= 100 && status < 400) {

            }
            else if (status == 404)
            {
            }
            else if (status == 409)
            {

            }
            else if (status == 0 || status >= 400 && status != 409)
            {

                Toast.makeText(context, "Register Failed", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
}
