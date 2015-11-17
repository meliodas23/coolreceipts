package com.solutions.ibarra.coolreceipts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class CoolLoginReceipts extends ActionBarActivity implements View.OnClickListener {


    public String verify_user_account = "http://gz123.site90.net/coolreceipts_login/";
    public EditText edtUsername, edtPassword;
    public Button button_login;
    public static boolean grant_access = false;
    public static final String USER_ID_PREFS = "Auth_PREFS";
    public static String user, pass;
    public static String user_id;
    public String shared_user = "";
    public String shared_pass = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_login_receipts);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        button_login = (Button) findViewById(R.id.button_login);

        button_login.setOnClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cool_login_receipts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class VerifyAccountTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String s = postData(params);

            return s;
        }
        private String postData(String[] params) {

            user = edtUsername.getText().toString();
            pass = edtPassword.getText().toString();

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet verifyAccountget = new HttpGet(verify_user_account);
            String jsonResult = "";
            JSONArray jsonArray;

            try {
                HttpResponse response = httpclient.execute(verifyAccountget);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                JSONObject obj1 = new JSONObject(jsonResult);
                jsonArray = obj1.getJSONArray("coolreceipts_login");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj2 = jsonArray.getJSONObject(i);
                    JSONObject obj3 = jsonArray.getJSONObject(i);
                    if (obj2.getString("username").equals(user)
                            && obj2.getString("password").equals(pass)) {
                        user_id = obj2.getString("user_id");

                        grant_access = true;
                    }


                }
                /*
                SharedPreferences settings = getSharedPreferences(
                        PREFS_NAME_AUTH, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("grant_access", grant_access);
                editor.commit();
                */

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String result) {
            if (grant_access) {

                SharedPreferences settings = getSharedPreferences(
                        USER_ID_PREFS, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("userid_prefs", user_id);
                editor.putBoolean("grant_access", grant_access);
                editor.commit();
                //Toast.makeText(getApplicationContext(), "USER ID login  "+user_id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                edtUsername.setText("");
                edtPassword.setText("");
                grant_access = false;
                button_login.setText("log in");
/*
                SharedPreferences settings = getSharedPreferences(
                        PREFS_NAME_AUTH, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("username_prefs", user);

*/
            }else{
                button_login.setText("LOG IN");
                Toast.makeText(getApplicationContext(), "Invalid account. Please Sign up.",
                        Toast.LENGTH_LONG).show();
            }

        }


        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        }
    }

    private boolean validate() {
        if (edtUsername.getText().toString().trim().equals(""))
            return false;
        else if (edtPassword.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.button_login:{

                if (!validate()) {
                    Toast.makeText(getBaseContext(), "Enter some data!",
                            Toast.LENGTH_LONG).show();

                } else {
                    button_login.setText("Logging in...");
                    new VerifyAccountTask().execute();
                }
            }

        }

    }
}
