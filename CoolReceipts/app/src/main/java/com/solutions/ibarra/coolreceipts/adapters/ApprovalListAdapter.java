package com.solutions.ibarra.coolreceipts.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.solutions.ibarra.coolreceipts.R;
import com.solutions.ibarra.coolreceipts.pojo.ApprovalPOJO;

import java.util.ArrayList;

/**
 * Created by Asrock01 on 3/14/2015.
 */
public class ApprovalListAdapter extends BaseAdapter {
    boolean[] itemChecked;
    public static ArrayList<ApprovalPOJO> approval_list = new ArrayList<ApprovalPOJO>();
    LayoutInflater inflater;
    Context context;

    public ApprovalListAdapter(Context context, ArrayList<ApprovalPOJO> approval_list) {
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
            convertView = inflater.inflate(R.layout.approval_listitem, null);
            myViewHolder = new MyViewHolder();
            myViewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        myViewHolder.dateTxt = detail(convertView, R.id.dateTxt, approval_list.get(position).getDateUploaded());
        myViewHolder.hospitalTxt = detail(convertView, R.id.hospitalTxt, approval_list.get(position).getHospital());
        myViewHolder.amountTxt = detail(convertView, R.id.amountTxt, approval_list.get(position).getAmount());
        // myViewHolder.patientsTxt = detail(convertView, R.id.patientsTxt, approval_list.get(position).getPatients() + " patients");
        myViewHolder.hospital_no = detail(convertView, R.id.hospital_no, approval_list.get(position).getHospital_no());
        myViewHolder.patient_lastname = detail(convertView, R.id.patient_lastname, approval_list.get(position).getPatient_lastname() + ",");
        myViewHolder.patient_firstname = detail(convertView, R.id.patient_firstname, approval_list.get(position).getPatient_firstname());
        myViewHolder.hospital_start = detail(convertView, R.id.hospital_start, approval_list.get(position).getHospital_start());
        myViewHolder.hospital_end = detail(convertView, R.id.hospital_end, approval_list.get(position).getHospital_end());

        myViewHolder.statusTxt = detail(convertView, R.id.statusTxt, approval_list.get(position).getStatus());
        if (approval_list.get(position).getStatus().trim().equals("PRs Received") ||
                approval_list.get(position).getStatus().trim().equals("Paid")) {
            myViewHolder.statusTxt.setBackgroundColor(Color.GREEN);
        } else if (approval_list.get(position).getStatus().trim().equals("Rejected")) {
            myViewHolder.statusTxt.setBackgroundColor(Color.RED);
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
        CheckBox cb;

    }
}
