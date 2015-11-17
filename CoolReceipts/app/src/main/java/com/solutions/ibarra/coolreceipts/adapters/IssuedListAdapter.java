package com.solutions.ibarra.coolreceipts.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.solutions.ibarra.coolreceipts.R;
import com.solutions.ibarra.coolreceipts.pojo.ApprovalPOJO;
import com.solutions.ibarra.coolreceipts.pojo.IssuedPOJO;

import java.util.ArrayList;

/**
 * Created by Asrock01 on 3/27/2015.
 */
public class IssuedListAdapter extends BaseAdapter {

    customORButtonListener customListner;

    public interface customORButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomORButtonListner(customORButtonListener listener) {
        this.customListner = listener;
    }

    boolean[] itemChecked;

    public static ArrayList<IssuedPOJO> approval_list = new ArrayList<IssuedPOJO>();
    LayoutInflater inflater;
    Context context;

    public IssuedListAdapter(Context context, ArrayList<IssuedPOJO> approval_list) {
        this.approval_list = approval_list;
        this.context = context;
        itemChecked = new boolean[approval_list.size()];
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return approval_list.size();
    }

    @Override
    public Object getItem(int position) {
        return approval_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyViewHolder myViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.issued_list_fragment, null);
            myViewHolder = new MyViewHolder();
            myViewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkBoxIssued);
            myViewHolder.or_button = (Button) convertView.findViewById(R.id.viewor_issued);

            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        myViewHolder.dateTxt = detail(convertView, R.id.dateTxt_issued, approval_list.get(position).getDateUploaded());
        myViewHolder.hospitalTxt = detail(convertView, R.id.hospitalTxt_issued, approval_list.get(position).getHospital());
        myViewHolder.amountTxt = detail(convertView, R.id.amountTxt_issued, approval_list.get(position).getAmount());
        // myViewHolder.patientsTxt = detail(convertView, R.id.patientsTxt, approval_list.get(position).getPatients() + " patients");
        myViewHolder.hospital_no = detail(convertView, R.id.hospital_no_issued, approval_list.get(position).getHospital_no());
        myViewHolder.patient_lastname = detail(convertView, R.id.patient_lastname_issued, approval_list.get(position).getPatient_lastname() + ",");
        myViewHolder.patient_firstname = detail(convertView, R.id.patient_firstname_issued, approval_list.get(position).getPatient_firstname());
        myViewHolder.hospital_start = detail(convertView, R.id.hospital_start_issued, approval_list.get(position).getHospital_start());
        myViewHolder.hospital_end = detail(convertView, R.id.hospital_end_issued, approval_list.get(position).getHospital_end());
        myViewHolder.statusTxt = detail(convertView, R.id.statusTxt_issued, approval_list.get(position).getStatus());
        myViewHolder.bank_app_no = detail(convertView, R.id.bank_app_no_txt, approval_list.get(position).getBank_app_no());
        myViewHolder.deposit_conf_no = detail(convertView, R.id.deposit_conf_no_txt, approval_list.get(position).getDeposit_conf_no());
        if (approval_list.get(position).getStatus().equals("OR Received")
                || approval_list.get(position).getStatus().equals("For OR Generation")) {
            myViewHolder.statusTxt.setBackgroundColor(Color.GREEN);
        } else {
            myViewHolder.statusTxt.setBackgroundColor(Color.LTGRAY);
        }
        if (itemChecked[position]) {
            myViewHolder.cb.setChecked(true);
        } else {
            myViewHolder.cb.setChecked(false);
        }
        myViewHolder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myViewHolder.cb.isChecked()) {
                    itemChecked[position] = true;
                } else {
                    itemChecked[position] = false;
                }
            }
        });

        final String temp = approval_list.get(position).getOr_url();
        myViewHolder.or_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (customListner != null) {
                    customListner.onButtonClickListner(position, temp);
                }

            }
        });
        return convertView;
    }

    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }

    private class MyViewHolder {
        @SuppressWarnings("unused")
        TextView dateTxt, statusTxt, hospitalTxt, amountTxt, patientsTxt, hospital_no, patient_lastname, patient_firstname, hospital_start, hospital_end;
        TextView bank_app_no, deposit_conf_no;
        CheckBox cb;
        Button or_button;
    }
}
