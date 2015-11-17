package com.solutions.ibarra.coolreceipts;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class RespawnPage extends ActionBarActivity {

    public Button btn_login, btn_signup;
    private Typeface typeFace;
    private TextView txt;
    public static final String USER_ID_PREFS = "Auth_PREFS";
    public boolean grant_access = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respawn_page);


        txt = (TextView) findViewById(R.id.projName);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        typeFace = Typeface.createFromAsset(getAssets(), "DecoNeue-Light.otf");
        txt.setTypeface(typeFace);
        SharedPreferences settings = getSharedPreferences(USER_ID_PREFS, 0);
        grant_access = settings.getBoolean("grant_access", false);
        // user_id_prefs = settings.getString("userid_prefs", "");

        if (grant_access) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), CoolLoginReceipts.class);
                    startActivity(intent);


                }
            });

            btn_signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Sign_Up_Page.class);
                    startActivity(intent);

                }
            });

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_respawn_page, menu);
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
