package com.solutions.ibarra.coolreceipts;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Sign_Up_Page extends ActionBarActivity {
    public String register_new_account = "http://gz123.site90.net/coolreceipts_login/";

    public Button confirm_account;
    protected EditText username, password, confirmpassword, firstname, lastname, middlename;
    public String usernameTxt, passwordTxt, confirmpasswordTxt, firstnameTxt, lastnameTxt, middlenameTxt;
    public static String message_type = "ADDUSER", error_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up__page);
        confirm_account = (Button) findViewById(R.id.button_confirmaccount);
        username = (EditText) findViewById(R.id.edtUsername);
        password = (EditText) findViewById(R.id.edtPassword);
        confirmpassword = (EditText) findViewById(R.id.edtConfrimPassword);
        firstname = (EditText) findViewById(R.id.edtFirstname);
        lastname = (EditText) findViewById(R.id.edtLastname);
        middlename = (EditText) findViewById(R.id.edtMiddlename);

        confirm_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    Toast.makeText(getBaseContext(), error_message,
                            Toast.LENGTH_LONG).show();
                    error_message = "";
                } else {
                    new RegisterNewAccount().execute();
                }


            }
        });

    }

    private boolean validate() {

        confirmpasswordTxt = confirmpassword.getText().toString();

        if (username.getText().toString().trim().equals("")) {
            error_message = "ERROR! All data is required. ";

            return false;

        } else if (password.getText().toString().trim().equals("")) {
            error_message = "ERROR! All data is required. ";
            return false;
        } else if (firstname.getText().toString().trim().equals("")) {
            error_message = "ERROR! All data is required. ";
            return false;
        } else if (lastname.getText().toString().equals("")) {
            error_message = "ERROR! All data is required.";
            return false;
        } else if (middlename.getText().toString().equals("")) {
            error_message = "ERROR! All data is required. ";
            return false;
        } else if (!password.getText().toString().equals(confirmpasswordTxt)) {
            // error_message = error_message.concat(error_message
            // + " Error! Password mismatched.");
            error_message = "Error! Password mismatched.";
            return false;
        } else
            return true;

    }

    private class RegisterNewAccount extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String s = postData(params);

            return s;
        }

        private String postData(String[] params) {

            // protected EditText username, password, confirmpassword, firstname, lastname, middlename;
            usernameTxt = username.getText().toString();
            passwordTxt = password.getText().toString();
            confirmpasswordTxt = confirmpassword.getText().toString();
            firstnameTxt = firstname.getText().toString();
            lastnameTxt = lastname.getText().toString();
            middlenameTxt = middlename.getText().toString();

            HttpClient httpclient = new DefaultHttpClient();

            HttpPost register_post = new HttpPost(register_new_account);


            try {
                List<NameValuePair> accountparams = new ArrayList<NameValuePair>();
                accountparams.add(new BasicNameValuePair("message_type",
                        message_type));
                accountparams.add(new BasicNameValuePair("username", usernameTxt));
                accountparams.add(new BasicNameValuePair("password", passwordTxt));
                accountparams.add(new BasicNameValuePair("firstname", firstnameTxt));
                accountparams.add(new BasicNameValuePair("lastname", lastnameTxt));
                accountparams.add(new BasicNameValuePair("middlename", middlenameTxt));
                register_post.setEntity(new UrlEncodedFormEntity(accountparams));

                HttpParams httpParameters = register_post.getParams();
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                // new
                HttpResponse httpResponse = httpclient.execute(register_post);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(), "Account Registered.",
                    Toast.LENGTH_SHORT).show();
            username.setText("");
            password.setText("");
            confirmpassword.setText("");
            firstname.setText("");
            middlename.setText("");
            lastname.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign__up__page, menu);
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
}
