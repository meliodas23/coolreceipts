package com.solutions.ibarra.coolreceipts;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.solutions.ibarra.coolreceipts.drawers.DashboardFragment;
import com.solutions.ibarra.coolreceipts.drawers.LogoutFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, DashboardFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    public static NavigationDrawerFragment mNavigationDrawerFragment;
    public static FragmentManager fragmentManager;
    public static FragmentTransaction tx;
    public static ActionBar actionBar;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    final String[] tab_select = {"com.solutions.ibarra.coolreceipts.drawers.DashboardFragment",
            "com.solutions.ibarra.coolreceipts.drawers.ApprovalFragment",
            "com.solutions.ibarra.coolreceipts.drawers.BatchFragment",
            "com.solutions.ibarra.coolreceipts.drawers.IssuedFragment",
            "com.solutions.ibarra.coolreceipts.drawers.LogoutFragment"};

    public Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mTitle = getString(R.string.title_section1);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        fragmentManager = getSupportFragmentManager();
        tx = getSupportFragmentManager().beginTransaction();
        String sampInt = String.valueOf(position);
        //  Toast.makeText(getApplicationContext(), sampInt, Toast.LENGTH_LONG).show();
        switch (position) {

            case 0: {
                tx.replace(R.id.container,
                        Fragment.instantiate(MainActivity.this, tab_select[0]));
                tx.commit();

                mTitle = getString(R.string.title_section1);
                break;
            }
            case 1: {
                tx.replace(R.id.container,
                        Fragment.instantiate(MainActivity.this, tab_select[1]));
                tx.commit();
                mTitle = getString(R.string.title_section2);
                break;
            }
            case 2: {
                tx.replace(R.id.container,
                        Fragment.instantiate(MainActivity.this, tab_select[2]));
                tx.commit();
                mTitle = getString(R.string.title_section3);
                break;
            }
            case 3: {
                tx.replace(R.id.container,
                        Fragment.instantiate(MainActivity.this, tab_select[3]));
                tx.commit();
                mTitle = getString(R.string.title_section4);
                break;
            }
            case 4: {
                //tx.replace(R.id.container,
                //        Fragment.instantiate(MainActivity.this, tab_select[4]));
              //  tx.commit();
                showDialog();
                mTitle = getString(R.string.title_section5);
                break;
            }
        }
        /*
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
                */

    }

    public void showDialog(){
        LogoutFragment dialog = new LogoutFragment();
        dialog.show(getFragmentManager(), LogoutFragment.TAG);
    }

    public void restoreActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void setTitleActionBar(String title) {
        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            // getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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


    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }
}
