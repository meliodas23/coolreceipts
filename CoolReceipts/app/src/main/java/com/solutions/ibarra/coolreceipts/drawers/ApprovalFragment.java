package com.solutions.ibarra.coolreceipts.drawers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.solutions.ibarra.coolreceipts.MainActivity;
import com.solutions.ibarra.coolreceipts.NavigationDrawerFragment;
import com.solutions.ibarra.coolreceipts.R;
import com.solutions.ibarra.coolreceipts.adapters.ApprovalListAdapter;
import com.solutions.ibarra.coolreceipts.pojo.ApprovalPOJO;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.CheckBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApprovalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApprovalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApprovalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public ArrayList<String> checkedValue = new ArrayList<String>();
    ;
    public String trans = "";
    private String mParam1;
    private String mParam2;
    private int check = 1, pos, pos2;
    private OnFragmentInteractionListener mListener;
    public static String URL_Transactions = "http://gz123.site90.net/coolreceipts_transactions/";
    public String URL_BATCH = "http://gz123.site90.net/coolreceipts_batch/";

    public ViewGroup root;
    public ArrayList<ApprovalPOJO> approval_list = new ArrayList<ApprovalPOJO>();
    public ListView listview;
    public Button btnApprove, btnReject;
    public String user_id_prefs;
    public int MAX = 1000;
    public int batchNO = 0;
    public String sizebatch, date, batch_no;

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
     * @return A new instance of fragment ApprovalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApprovalFragment newInstance(String param1, String param2) {
        ApprovalFragment fragment = new ApprovalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ApprovalFragment() {
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_approval, null);

        listview = (ListView) root.findViewById(R.id.listView);
        btnApprove = (Button) root.findViewById(R.id.btnApproved);
        btnReject = (Button) root.findViewById(R.id.btnReject);

        approval_list.clear();
        SharedPreferences settings = getActivity().getSharedPreferences(USER_ID_PREFS, 0);
        user_id_prefs = settings.getString("userid_prefs", "");

        //Toast.makeText(getActivity(), "USER ID " + user_id_prefs, Toast.LENGTH_SHORT).show();
        new getApprovalTransactions().execute();
        //setHasOptionsMenu(true);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox);

                cb.performClick();
                if (cb.isChecked()) {
                    checkedValue.add(approval_list.get(position).getTransact_id());

                } else if (!cb.isChecked()) {
                    checkedValue.remove(approval_list.get(position).getTransact_id());

                }
                //Toast.makeText(getActivity(), "CHECKED" + checkedValue.toString(), Toast.LENGTH_SHORT).show();
            }

        });

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!checkedValue.isEmpty()) {

                    int size = checkedValue.size();
                    Random rnd = new Random();
                    batchNO = 0 + rnd.nextInt(MAX);
                    batch_no = String.valueOf(batchNO);


                    for (int i = 0; i < size; i++) {
                        Log.i("Member name: ", checkedValue.get(i));
                        //trans = checkedValue.get(i);
                        String tran = checkedValue.get(i);
                        new updateApproveStatus().execute(tran);
                        //delay();
                    }
                    new updateApprovedBatchStatus().execute();

                } else {
                    Toast.makeText(getActivity(), "Please select items to approve.", Toast.LENGTH_SHORT).show();
                }
            }


        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkedValue.isEmpty()) {
                    int size = checkedValue.size();
                    for (int i = 0; i < size; i++) {
                        Log.i("Member name: ", checkedValue.get(i));
                        //trans = checkedValue.get(i);
                        String tran = checkedValue.get(i);
                        new updateRejectStatus().execute(tran);
                        //delay();
                    }

                } else {
                    Toast.makeText(getActivity(), "Please select items to reject.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Inflate the layout for this fragment
        return root;
    }

    public void delay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                //  // Do something after 5s = 5000ms
                //   buttons[inew][jnew].setBackgroundColor(Color.BLACK);
            }

        }, 5000);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class updateApprovedBatchStatus extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String s = postData(params);
            return s;
        }

        private String postData(String[] params) {
            String jsonResult = "";
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost updateApprovedBatchStatus = new HttpPost(URL_BATCH);


            int size = checkedValue.size();

            sizebatch = String.valueOf(size);
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
            date = df.format(Calendar.getInstance().getTime());


            try {
                List<NameValuePair> updateparamsbatch = new ArrayList<NameValuePair>();
                updateparamsbatch.add(new BasicNameValuePair("batch_no", batch_no));
                updateparamsbatch.add(new BasicNameValuePair("status", "Approved"));
                updateparamsbatch.add(new BasicNameValuePair("patients", sizebatch));
                updateparamsbatch.add(new BasicNameValuePair("date_uploaded", date));
                updateparamsbatch.add(new BasicNameValuePair("amount", ""));
                updateparamsbatch.add(new BasicNameValuePair("user_id", user_id_prefs));
                updateApprovedBatchStatus.setEntity(new UrlEncodedFormEntity(
                        updateparamsbatch));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                HttpParams httpParameters = updateApprovedBatchStatus.getParams();
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                // new
                HttpResponse httpResponse = httpclient.execute(updateApprovedBatchStatus);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String result) {
            batchNO = 0;
            batch_no = "";

            FragmentManager fragmentManager;
            FragmentTransaction tx;
            fragmentManager = getActivity().getSupportFragmentManager();
            tx = getActivity().getSupportFragmentManager().beginTransaction();
            tx.replace(R.id.container,
                    Fragment.instantiate(getActivity(), tab_select[1]));
            tx.commit();
           // mListener.onFragmentInteraction("Approval");
        }
    }

    private class updateRejectStatus extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String s = postData(params);
            return s;
        }

        private String postData(String[] params) {
            String jsonResult = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost updateApproveStatus = new HttpPost(URL_Transactions);
            try {

                int size = checkedValue.size();
                try {
                    for (int init = 0; init < size; init++) {

                        List<NameValuePair> updateparams = new ArrayList<NameValuePair>();
                        trans = params[init];
                        updateparams.add(new BasicNameValuePair("transact_id", trans));
                        updateparams.add(new BasicNameValuePair("status", "Rejected"));
                        updateApproveStatus.setEntity(new UrlEncodedFormEntity(
                                updateparams));
                        //sent
                        //delay();
                        String haha = updateparams.toString();
                    }
                } catch (RuntimeException e) {

                }


                HttpParams httpParameters = updateApproveStatus.getParams();
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                HttpResponse httpResponse = httpclient
                        .execute(updateApproveStatus);

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
            Toast.makeText(getActivity(), "Rejected Successfully", Toast.LENGTH_SHORT).show();
            //checkedValue.clear();
        }
    }


    private class updateApproveStatus extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String s = postData(params);

            return s;
        }

        private String postData(String[] params) {
            String jsonResult = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost updateApproveStatus = new HttpPost(URL_Transactions);
            //HttpPost updateApprovedBatchStatus = new HttpPost(URL_BATCH);

            try {

                int size = checkedValue.size();

                sizebatch = String.valueOf(size);
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                date = df.format(Calendar.getInstance().getTime());
                try {


                    for (int init = 0; init < size; init++) {

                        List<NameValuePair> updateparams = new ArrayList<NameValuePair>();
                        trans = params[init];
                        updateparams.add(new BasicNameValuePair("transact_id", trans));
                        updateparams.add(new BasicNameValuePair("status", "For PR Generation"));
                        updateparams.add(new BasicNameValuePair("batch_no", batch_no));
                        updateApproveStatus.setEntity(new UrlEncodedFormEntity(
                                updateparams));

                        //sent
                        // delay();
                        //String haha = updateparams.toString();
                    }


                } catch (RuntimeException e) {

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                HttpParams httpParameters = updateApproveStatus.getParams();
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                HttpResponse httpResponse = httpclient
                        .execute(updateApproveStatus);

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


            Toast.makeText(getActivity(), "Approved Successfully", Toast.LENGTH_SHORT).show();
            //checkedValue.clear();

        }


    }

    private class getApprovalTransactions extends AsyncTask<String, Void, String> {

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
            HttpGet getTransact = new HttpGet(URL_Transactions);

            //String status, patients, dateUploaded, hospital, amount, transact_id;
            String patients, transact_id, hospital_no, patient_lastname, patient_firstname, hospital_start, hospital_end, dateUploaded, amount, hospital, status;
            try {
                HttpResponse response = httpclient.execute(getTransact);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                JSONObject obj1 = new JSONObject(jsonResult);
                jsonArray = obj1.getJSONArray("coolreceipts_transactions");
                String uploaded = "Uploaded";
                String review = "Review Ongoing";
                String rejected = "Rejected";

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj2 = jsonArray.getJSONObject(i);
                    String statusone = obj2.getString("status").trim();
                    String user_id = obj2.getString("user_id");

                    if (user_id_prefs.equals(user_id)) {
                        if (uploaded.equals(statusone) || review.equals(statusone) || rejected.equals(statusone)) {
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
//  public ApprovalPOJO(String status, String patients, String dateUploaded, String hospital,
// String amount, String transact_id, String hospital_no, String hospital_start,
// String hospital_end, String patient_firstname, String patient_lastname) {


                            approval_list.add(new ApprovalPOJO(status, patients, dateUploaded, hospital, amount, transact_id, hospital_no
                                    , hospital_start, hospital_end, patient_firstname, patient_lastname));

                            //approval_list.add(new ApprovalPOJO(transact_id, status, patients, dateUploaded, hospital, amount));
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
            listview.setAdapter(new ApprovalListAdapter(getActivity(), approval_list));


        }
        // MoodListFragment.listview3.setAdapter(new MusicListAdapter(
        //        getActivity(), mood_list));

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
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

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
        public void onFragmentInteraction(String title);
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


}
