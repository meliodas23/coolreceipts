package com.solutions.ibarra.coolreceipts.drawers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.solutions.ibarra.coolreceipts.R;
import com.solutions.ibarra.coolreceipts.adapters.ApprovedBatchAdapter;
import com.solutions.ibarra.coolreceipts.pojo.BatchPOJO;

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
 * {@link BatchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BatchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String batchno;
    public static String dateuploaded_bundle;

    private OnFragmentInteractionListener mListener;

    public ArrayList<BatchPOJO> batcharray = new ArrayList<BatchPOJO>();
    public ListView list_batch;
    public ViewGroup root;
    public String URL_BATCH = "http://gz123.site90.net/coolreceipts_batch/";
    private CharSequence mTitle;
    public ActionBar mActionBar;
    public String user_id_prefs;
    public static final String USER_ID_PREFS = "Auth_PREFS";
    final String[] tab_select = {"com.solutions.ibarra.coolreceipts.drawers.DashboardFragment",
            "com.solutions.ibarra.coolreceipts.drawers.ApprovalFragment",
            "com.solutions.ibarra.coolreceipts.drawers.ApprovedFragment",
            "com.solutions.ibarra.coolreceipts.drawers.IssuedFragment",
            "com.solutions.ibarra.coolreceipts.drawers.LogoutFragment"};

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApprovedBatchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BatchFragment newInstance(String param1, String param2) {
        BatchFragment fragment = new BatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM1, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BatchFragment() {
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
        root = (ViewGroup) inflater.inflate(R.layout.fragment_approved_batch, null);
        // Inflate the layout for this fragment
        list_batch = (ListView) root.findViewById(R.id.batch_list);
        SharedPreferences settings = getActivity().getSharedPreferences(USER_ID_PREFS, 0);
        user_id_prefs = settings.getString("userid_prefs", "");
        new BatchApprovedTask().execute();


        list_batch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                batchno = batcharray.get(position).getBatchno();
                String date = batcharray.get(position).getDateuploaded();

                dateuploaded_bundle = date;
                newInstance(batchno, batchno);

                FragmentManager fragmentManager;
                FragmentTransaction tx;
                fragmentManager = getActivity().getSupportFragmentManager();
                tx = getActivity().getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.container,
                        Fragment.instantiate(getActivity(), tab_select[2]));
                tx.commit();

            }
        });

        return root;
    }

    private class BatchApprovedTask extends AsyncTask<String, Void, String> {

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

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String result) {
            Collections.reverse(batcharray);
            list_batch.setAdapter(new ApprovedBatchAdapter(getActivity(), batcharray));

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
