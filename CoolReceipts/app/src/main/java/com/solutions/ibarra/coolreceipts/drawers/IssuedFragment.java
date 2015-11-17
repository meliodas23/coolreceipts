package com.solutions.ibarra.coolreceipts.drawers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.solutions.ibarra.coolreceipts.FileDownloader;
import com.solutions.ibarra.coolreceipts.R;
import com.solutions.ibarra.coolreceipts.adapters.ApprovalListAdapter;
import com.solutions.ibarra.coolreceipts.adapters.IssuedListAdapter;
import com.solutions.ibarra.coolreceipts.pojo.ApprovalPOJO;
import com.solutions.ibarra.coolreceipts.pojo.IssuedPOJO;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IssuedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IssuedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssuedFragment extends Fragment implements IssuedListAdapter.customORButtonListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CharSequence mTitle;
    public ActionBar mActionBar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ViewGroup root;
    public static String URL_Transactions = "http://gz123.site90.net/coolreceipts_transactions/";
    private OnFragmentInteractionListener mListener;
    public ArrayList<IssuedPOJO> approval_list = new ArrayList<IssuedPOJO>();
    public ListView listview;
    private ProgressDialog pDialog;
    public String trans = "", Check_PR_FILE, filename;
    public File pdfFile_2;
    IssuedListAdapter adapter;
    public String user_id_prefs;
    public static final String USER_ID_PREFS = "Auth_PREFS";

    final String[] tab_select = {"com.solutions.ibarra.coolreceipts.drawers.DashboardFragment",
            "com.solutions.ibarra.coolreceipts.drawers.ApprovalFragment",
            "com.solutions.ibarra.coolreceipts.drawers.BatchFragment",
            "com.solutions.ibarra.coolreceipts.drawers.IssuedFragment",
            "com.solutions.ibarra.coolreceipts.drawers.LogoutFragment"};
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IssuedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IssuedFragment newInstance(String param1, String param2) {
        IssuedFragment fragment = new IssuedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public IssuedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_issued, null);
        // Inflate the layout for this fragment
        listview = (ListView) root.findViewById(R.id.listView3);
        SharedPreferences settings = getActivity().getSharedPreferences(USER_ID_PREFS, 0);
        user_id_prefs = settings.getString("userid_prefs", "");

        new getIssuedTransactions().execute();


        return root;
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        //   Toast.makeText(getActivity(), "Button click " + value,
        //        Toast.LENGTH_SHORT).show();
        if (!value.isEmpty()) {


            // Log.i("Member name: ", check_PR_File.get(0));
            //trans = checkedValue.get(i);
            //String tran = checkedValue.get(i);
            Check_PR_FILE = value;

            filename = Check_PR_FILE.replace("http://gz123.site90.net/coolreceipts_issuedor/", "");

            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/coolreceipts/" + filename);  // -> filename = maven.pdf
            if (pdfFile.length() == 0) {
                new DownloadORFromWeb().execute(Check_PR_FILE);
            } else {


                Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try {
                    startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "The selected item has no pdf.", Toast.LENGTH_SHORT).show();
                }
            }


        } else {
            Toast.makeText(getActivity(), "Please choose to VIEW PR.", Toast.LENGTH_SHORT).show();
        }

    }

    private class DownloadORFromWeb extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int count = 0;

            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "coolreceipts");
            folder.mkdir();

            // String filename = Check_PR_FILE.replace("http://gz123.site90.net/coolreceipts_issuedor/", "");
            pdfFile_2 = new File(folder, filename);
            // Toast.makeText(getActivity(), "filename: "+filename , Toast.LENGTH_LONG).show();
            try {
                pdfFile_2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(Check_PR_FILE, pdfFile_2);


            URL url = null;
            try {
                url = new URL(Check_PR_FILE);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            InputStream input = null;
            try {
                input = new BufferedInputStream(url.openStream(), 8192);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int lenghtOfFile = FileDownloader.urlConnection.getContentLength();
            byte data[] = new byte[1024];
            long total = 0;
            try {
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;

        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            Toast.makeText(getActivity(), "Finish downloading pdf. ", Toast.LENGTH_LONG).show();

            Uri path = Uri.fromFile(pdfFile_2);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class getIssuedTransactions extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String s = postData(params);


            return s;
        }

        private String postData(String[] params) {
            String jsonResult = "";

            JSONArray jsonArray;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet getIssuedTransact = new HttpGet(URL_Transactions);
            String patients, transact_id, hospital_no, patient_lastname, patient_firstname, hospital_start, hospital_end, dateUploaded, amount, hospital, status;
            String bank_app_no, deposit_conf_no, or_url;

            try {
                HttpResponse response = httpclient.execute(getIssuedTransact);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                JSONObject obj1 = new JSONObject(jsonResult);
                jsonArray = obj1.getJSONArray("coolreceipts_transactions");
                String issued = "OR Received";
                String forOrGeneration = "For OR Generation";


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj2 = jsonArray.getJSONObject(i);
                    String status_issued = obj2.getString("status").trim();
                    String user_id = obj2.getString("user_id");

                    if (user_id_prefs.equals(user_id)) {
                        if (issued.equals(status_issued) || forOrGeneration.equals(status_issued)) {
                            transact_id = obj2.getString("transact_id");
                            status = obj2.getString("status");
                            dateUploaded = obj2.getString("date_uploaded");
                            patients = obj2.getString("patients");
                            hospital = obj2.getString("hospital");
                            amount = obj2.getString("amount");
                            hospital_no = obj2.getString("hospital_control_no");
                            patient_lastname = obj2.getString("patient_lastname");
                            patient_firstname = obj2.getString("patient_firstname");
                            hospital_start = obj2.getString("hospital_start_date");
                            hospital_end = obj2.getString("hospital_end_date");
                            bank_app_no = obj2.getString("bank_app_no");
                            deposit_conf_no = obj2.getString("deposit_conf_no");
                            or_url = obj2.getString("or_url");


                            approval_list.add(new IssuedPOJO(status, transact_id, hospital_no, patient_lastname, patient_firstname, hospital_start, hospital_end
                                    , dateUploaded, amount, hospital, status, bank_app_no, deposit_conf_no, or_url));
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String result) {
            adapter = new IssuedListAdapter(getActivity(), approval_list);
            adapter.setCustomORButtonListner(IssuedFragment.this);
            listview.setAdapter(adapter);
            // listview.setAdapter(new IssuedListAdapter(getActivity(), approval_list));

        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                // Toast.makeText(context, "hey", Toast.LENGTH_SHORT).show();
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    // handle back button

                    FragmentManager fragmentManager;
                    FragmentTransaction tx;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    tx = getActivity().getSupportFragmentManager().beginTransaction();
                    tx.replace(R.id.container,
                            Fragment.instantiate(getActivity(), tab_select[0]));
                    tx.commit();
                    // mListener.onFragmentInteraction("Dashboard");
                    return true;

                }

                return false;
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
