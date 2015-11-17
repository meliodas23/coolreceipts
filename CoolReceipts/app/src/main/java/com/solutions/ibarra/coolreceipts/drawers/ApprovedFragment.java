package com.solutions.ibarra.coolreceipts.drawers;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.solutions.ibarra.coolreceipts.FileDownloader;

import com.solutions.ibarra.coolreceipts.R;

import com.solutions.ibarra.coolreceipts.adapters.ApprovedListAdapter;

import com.solutions.ibarra.coolreceipts.pojo.ApprovedPOJO;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApprovedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApprovedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApprovedFragment extends Fragment implements ApprovedListAdapter.customButtonListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    protected Button btn_view_pr, btn_generate_or;
    private OnFragmentInteractionListener mListener;
    public ViewGroup root;
    public String URL_APPROVED = "http://gz123.site90.net/coolreceipts_transactions/";

    ApprovedListAdapter adapter;
    public ArrayList<ApprovedPOJO> approved_list = new ArrayList<ApprovedPOJO>();
    public ListView listview2;
    public String getStatus_value = "";
    public ArrayList<String> checkedValue = new ArrayList<String>();
    public ArrayList<String> checkRequestOr = new ArrayList<String>();
    public ArrayList<String> checkPaidStatus = new ArrayList<String>();
    public ArrayList<String> check_PR_File = new ArrayList<String>();
    public String trans = "", Check_PR_FILE, filename;
    private ProgressDialog pDialog;
    public File pdfFile_2;
    public static final int progress_bar_type = 0;
    public String batchno_bundle, dateuploaded_bundle;
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
     * @return A new instance of fragment ApprovedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApprovedFragment newInstance(String param1, String param2) {
        ApprovedFragment fragment = new ApprovedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ApprovedFragment() {
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

        root = (ViewGroup) inflater.inflate(R.layout.fragment_approved, null);
        // Inflate the layout for this fragment
        listview2 = (ListView) root.findViewById(R.id.listView2);
        TextView batchno_bundleTxt = (TextView) root.findViewById(R.id.batchno_bundle);
        TextView dateuploaded_bundleTxt = (TextView) root.findViewById(R.id.dateuploaded_bundle);
        approved_list.clear();
        checkRequestOr.clear();
        checkPaidStatus.clear();

        batchno_bundle = BatchFragment.batchno;
        dateuploaded_bundle = BatchFragment.dateuploaded_bundle;
        SharedPreferences settings = getActivity().getSharedPreferences(USER_ID_PREFS, 0);
        user_id_prefs = settings.getString("userid_prefs", "");
        new ApprovedTransactionsTask().execute();


        btn_generate_or = (Button) root.findViewById(R.id.btn_generate_or);


        batchno_bundleTxt.setText(batchno_bundle);
        dateuploaded_bundleTxt.setText(dateuploaded_bundle);


        btn_generate_or.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   //new generateOrTransactions().execute();
                                                   if (!checkedValue.isEmpty()) {
                                                       int size = checkedValue.size();
                                                       for (int i = 0; i < size; i++) {
                                                           Log.i("Member name: ", checkedValue.get(i));
                                                           //trans = checkedValue.get(i);
                                                           String tran = checkedValue.get(i);
                                                           String checkStatus = checkPaidStatus.get(i);
                                                           if (checkStatus.equals("Paid")) {
                                                               new generateOrTransactions().execute(tran);
                                                           } else {

                                                               checkRequestOr.add(checkedValue.get(i));

                                                           }


                                                       }


                                                   } else {
                                                       Toast.makeText(getActivity(), "Please select paid transactions to request OR.", Toast.LENGTH_SHORT).show();
                                                   }
                                                   if (!checkRequestOr.isEmpty()) {
                                                       Toast.makeText(getActivity(), "Please pay this transaction no: " + checkRequestOr.toString(), Toast.LENGTH_LONG).show();
                                                       checkRequestOr.clear();
                                                   }

                                               }
                                           }

        );

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkBoxApproved);
                cb.performClick();
                if (cb.isChecked()) {
                    checkedValue.add(approved_list.get(position).getTransact_id());
                    checkPaidStatus.add(approved_list.get(position).getStatus());
                    check_PR_File.add(approved_list.get(position).getPr_url());

                } else if (!cb.isChecked()) {
                    checkedValue.remove(approved_list.get(position).getTransact_id());
                    checkPaidStatus.remove(approved_list.get(position).getStatus());
                    check_PR_File.remove(approved_list.get(position).getPr_url());
                }
                //Toast.makeText(getActivity(), "CHECKED" + checkedValue.toString() + " : " + checkPaidStatus.toString(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(getActivity(), "PR_URL:  " + check_PR_File.toString(), Toast.LENGTH_LONG).show();


            }
        });


        return root;
    }

    @Override
    public void onButtonClickListner(int position, String value) {
        // Toast.makeText(getActivity(), "Button click " + value,
        //        Toast.LENGTH_SHORT).show();
        if (!value.isEmpty()) {

            // Log.i("Member name: ", check_PR_File.get(0));
            //trans = checkedValue.get(i);
            //String tran = checkedValue.get(i);
            Check_PR_FILE = value;

            filename = Check_PR_FILE.replace("http://gz123.site90.net/coolreceipts_issuedor/", "");

            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/coolreceipts/" + filename);  // -> filename = maven.pdf
            if (pdfFile.length() == 0) {
                new DownloadPRFromWeb().execute(Check_PR_FILE);
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
            Toast.makeText(getActivity(), "PR is not available.", Toast.LENGTH_SHORT).show();
        }
    }


    public class ApprovedTransactionsTask extends AsyncTask<String, Void, String> {

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
            HttpGet getTransact = new HttpGet(URL_APPROVED);


            String hospital_no;
            String lastname;
            String firstname;
            String start_date;
            String end_date;
            String pr_number;
            String amount;
            String transact_id;
            String status;
            String pr_url;

            try {
                HttpResponse response = httpclient.execute(getTransact);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
                JSONObject obj1 = new JSONObject(jsonResult);
                jsonArray = obj1.getJSONArray("coolreceipts_transactions");
                String uploaded = "Approved";
                String review = "PRs Received";
                String forPRGeneration = "For PR Generation";
                String paid = "Paid";


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj2 = jsonArray.getJSONObject(i);
                    String statusone = obj2.getString("status");
                    String batchno_ex = obj2.getString("batch_no");
                    String user_id = obj2.getString("user_id");
                    if (user_id_prefs.equals(user_id)) {
                        if (batchno_bundle.equals(batchno_ex)) {
                            if (uploaded.equals(statusone) || review.equals(statusone) || paid.equals(statusone) || forPRGeneration.equals(statusone)) {


                                hospital_no = obj2.getString("hospital_control_no");
                                lastname = obj2.getString("patient_lastname");
                                firstname = obj2.getString("patient_firstname");
                                start_date = obj2.getString("hospital_start_date");
                                end_date = obj2.getString("hospital_end_date");
                                pr_number = obj2.getString("pr_number");
                                amount = obj2.getString("amount");
                                transact_id = obj2.getString("transact_id");
                                status = obj2.getString("status");
                                pr_url = obj2.getString("pr_url");

                                approved_list.add(new ApprovedPOJO(hospital_no, lastname, firstname, start_date, end_date, pr_number, amount, transact_id, status, pr_url));

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
            adapter = new ApprovedListAdapter(getActivity(), approved_list);
            adapter.setCustomButtonListner(ApprovedFragment.this);
            listview2.setAdapter(adapter);

            //listview2.setAdapter(new ApprovedListAdapter(getActivity(), approved_list));

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


    private class DownloadPRFromWeb extends AsyncTask<String, String, String> {
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
        protected String doInBackground(String... strings) {
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


    private class generateOrTransactions extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String s = postData(params);


            return s;
        }

        private String postData(String[] params) {
            String jsonResult = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost updateApproveStatus = new HttpPost(URL_APPROVED);
            try {
                int size = checkedValue.size();
                try {
                    for (int init = 0; init < size; init++) {

                        List<NameValuePair> updateparams = new ArrayList<NameValuePair>();
                        trans = params[init];
                        updateparams.add(new BasicNameValuePair("transact_id", trans));
                        updateparams.add(new BasicNameValuePair("status", "For OR Generation"));
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
            Toast.makeText(getActivity(), "Sent OR Successfully", Toast.LENGTH_SHORT).show();
            checkRequestOr.clear();
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
                            Fragment.instantiate(getActivity(), tab_select[2]));
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
