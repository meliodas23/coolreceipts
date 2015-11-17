package com.solutions.ibarra.coolreceipts.drawers;


import android.annotation.TargetApi;
import android.app.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.solutions.ibarra.coolreceipts.R;
import com.solutions.ibarra.coolreceipts.Utility;
import com.solutions.ibarra.coolreceipts.adapters.ApprovalListAdapter;
import com.solutions.ibarra.coolreceipts.adapters.ApprovedBatchAdapter;
import com.solutions.ibarra.coolreceipts.adapters.IssuedListAdapter;
import com.solutions.ibarra.coolreceipts.pojo.ApprovalPOJO;
import com.solutions.ibarra.coolreceipts.pojo.BatchPOJO;
import com.solutions.ibarra.coolreceipts.pojo.IssuedPOJO;

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
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ViewGroup root;
    public static String URL_Transactions = "http://gz123.site90.net/coolreceipts_transactions/";
    private OnFragmentInteractionListener mListener;
    public ArrayList<BatchPOJO> batcharray = new ArrayList<BatchPOJO>();
    public ArrayList<ApprovalPOJO> approval_list = new ArrayList<ApprovalPOJO>();
    public ArrayList<IssuedPOJO> issued_list = new ArrayList<IssuedPOJO>();
    public String URL_BATCH = "http://gz123.site90.net/coolreceipts_batch/";
    public ListView list_approval, list_approved, list_transacttions;
    public String user_id_prefs;
    public static final String USER_ID_PREFS = "Auth_PREFS";

    final String[] tab_select = {"com.solutions.ibarra.coolreceipts.drawers.DashboardFragment",
            "com.solutions.ibarra.coolreceipts.drawers.ApprovalFragment",
            "com.solutions.ibarra.coolreceipts.drawers.BatchFragment",
            "com.solutions.ibarra.coolreceipts.drawers.IssuedFragment",
            "com.solutions.ibarra.coolreceipts.drawers.LogoutFragment"};

    public static final String TAG = "LogoutFragment";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DashboardFragment() {
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
        // Inflate the layout for this fragment
        root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, null);
        list_approval = (ListView) root.findViewById(R.id.listApproval_dashboard);
        list_approved = (ListView) root.findViewById(R.id.listApproved_dashboard);
        list_transacttions = (ListView) root.findViewById(R.id.listTransactions_dashboard);
        //Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        // toolbar.setTitle(R.string.title_section1);
        // mTitle = getActivity().getTitle();
        SharedPreferences settings = getActivity().getSharedPreferences(USER_ID_PREFS, 0);
        user_id_prefs = settings.getString("userid_prefs", "");
        mListener.onFragmentInteraction("Dashboard");

        try {
            new getApprovalTransactions().execute();


        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //   ((MainActivity) getActivity()).setActionBarTitle("potakadogay");


        list_approval.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                FragmentManager fragmentManager;
                FragmentTransaction tx;
                fragmentManager = getActivity().getSupportFragmentManager();
                tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.container,
                        Fragment.instantiate(getActivity(), tab_select[1]));
                tx.commit();
                //mTitle = getString(R.string.title_section1);
                mListener.onFragmentInteraction("Approval");

            }
        });
        list_approved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fragmentManager;
                FragmentTransaction tx;
                fragmentManager = getActivity().getSupportFragmentManager();
                tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.container,
                        Fragment.instantiate(getActivity(), tab_select[2]));
                tx.commit();

                mListener.onFragmentInteraction("Approved");
            }
        });
        list_transacttions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fragmentManager;
                FragmentTransaction tx;
                fragmentManager = getActivity().getSupportFragmentManager();
                tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.container,
                        Fragment.instantiate(getActivity(), tab_select[3]));
                tx.commit();

                mListener.onFragmentInteraction("Issued");

            }
        });
        //getActivity().getActionBar().setTitle(mTitle);

        return root;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                    if (i > 2) {
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


                                issued_list.add(new IssuedPOJO(status, transact_id, hospital_no, patient_lastname, patient_firstname, hospital_start, hospital_end
                                        , dateUploaded, amount, hospital, status, bank_app_no, deposit_conf_no, or_url));
                            }
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
            Collections.reverse(issued_list);
            //listview.setAdapter(new IssuedListAdapter(getActivity(), approval_list));
            list_transacttions.setAdapter(new IssuedListAdapter(getActivity(), issued_list));
            Utility.setListViewHeightBasedOnChildren(list_transacttions);

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

    private class getBatchApprovedTransactions extends AsyncTask<String, Void, String> {

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
            HttpGet getTransact = new HttpGet(URL_BATCH);
            String batch_no, status, patients, dateuploaded, amount;

            try {
                HttpResponse response = httpclient.execute(getTransact);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                JSONObject obj1 = new JSONObject(jsonResult);
                jsonArray = obj1.getJSONArray("coolreceipts_batch");
                String uploaded = "Approved";
                String review = "PRs Received";
                String paid = "Paid";

                for (int i = 0; i < jsonArray.length(); i++) {
                    if (i > 2) {
                        JSONObject obj2 = jsonArray.getJSONObject(i);
                        String statusone = obj2.getString("status").trim();
                        String user_id = obj2.getString("user_id");
                        if (user_id_prefs.equals(user_id)) {
                            if (uploaded.equals(statusone) || review.equals(statusone) || paid.equals(statusone)) {
                                batch_no = obj2.getString("batch_no");
                                status = obj2.getString("status");
                                patients = obj2.getString("patients");
                                dateuploaded = obj2.getString("date_uploaded");
                                amount = obj2.getString("amount");

                                batcharray.add(new BatchPOJO(status, batch_no, patients, dateuploaded, amount));
                            }
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
            Collections.reverse(batcharray);
            list_approved.setAdapter(new ApprovedBatchAdapter(getActivity(), batcharray));
            Utility.setListViewHeightBasedOnChildren(list_approved);
            new getIssuedTransactions().execute();
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
                    if (i > 2) {

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
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            Collections.reverse(approval_list);
            // listview.setAdapter(new ApprovalListAdapter(getActivity(), approval_list));
            list_approval.setAdapter(new ApprovalListAdapter(getActivity(), approval_list));
            Utility.setListViewHeightBasedOnChildren(list_approval);

            new getBatchApprovedTransactions().execute();


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
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void showDialog(){
        LogoutFragment dialog = new LogoutFragment();
        dialog.show(getActivity().getFragmentManager(), LogoutFragment.TAG);
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
                    showDialog();

                    // handle back button
                    //Toast.makeText(getActivity(), "You are having a hard time.", Toast.LENGTH_SHORT).show();
/*
                    FragmentManager fragmentManager;
                    FragmentTransaction tx;
                    fragmentManager = getActivity().getSupportFragmentManager();
                    tx = getActivity().getSupportFragmentManager().beginTransaction();
                    tx.replace(R.id.container,
                            Fragment.instantiate(getActivity(), tab_select[0]));
                    tx.commit();
*/
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
        public void onFragmentInteraction(String title);
    }

}
