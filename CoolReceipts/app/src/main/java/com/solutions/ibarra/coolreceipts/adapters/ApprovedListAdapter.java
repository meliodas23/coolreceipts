package com.solutions.ibarra.coolreceipts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


import com.solutions.ibarra.coolreceipts.R;
import com.solutions.ibarra.coolreceipts.drawers.ApprovedFragment;

import com.solutions.ibarra.coolreceipts.pojo.ApprovedPOJO;

import java.util.ArrayList;

/**
 * Created by Asrock01 on 3/14/2015.
 */
public class ApprovedListAdapter extends BaseAdapter {

    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position, String value);
    }

    public void setCustomButtonListner(customButtonListener listener) {
        this.customListner = listener;
    }

    boolean[] itemChecked;

    public static String url;


    public ArrayList<ApprovedPOJO> approval_list = new ArrayList<ApprovedPOJO>();
    LayoutInflater inflater;
    Context context;


    public ApprovedListAdapter(Context context, ArrayList<ApprovedPOJO> approval_list) {
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
            convertView = inflater.inflate(R.layout.approved_list_item, null);
            myViewHolder = new MyViewHolder();
            myViewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkBoxApproved);
            myViewHolder.btn = (Button) convertView.findViewById(R.id.viewpr_approved);

            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
//hospital_no, lastname, firstname, start_date, end_date, pr_number, amount, transact_id, status, pr_url;
        myViewHolder.hospital_no = detail(convertView, R.id.hospitalno_approved, approval_list.get(position).getHospital_no());
        myViewHolder.lastname = detail(convertView, R.id.lastname_approved, approval_list.get(position).getLastname());
        myViewHolder.firstname = detail(convertView, R.id.firstname_approved, approval_list.get(position).getFirstname());
        myViewHolder.start_date = detail(convertView, R.id.startdate_approved, approval_list.get(position).getStart_date());
        myViewHolder.end_date = detail(convertView, R.id.enddate_approved, approval_list.get(position).getEnd_date());
        myViewHolder.pr_number = detail(convertView, R.id.prnumber_approved, approval_list.get(position).getPr_number());
        myViewHolder.amount = detail(convertView, R.id.amount_approved, approval_list.get(position).getAmount());
        //myViewHolder.transact_id = detail(convertView, R.id.tran, approval_list.get(position).getTransact_id());
        //myViewHolder.status = detail(convertView, R.id.lastname_approved, approval_list.get(position).getStatus());
        // myViewHolder.pr_url = detail(convertView, R.id.lastname_approved, approval_list.get(position).getPr_url());
 /*
        myViewHolder.statusTxt = detail(convertView, R.id.statusTxt, approval_list.get(position).getStatus());

        if(approval_list.get(position).getStatus().trim().equals("PRs Received") ||
                approval_list.get(position).getStatus().trim().equals("Paid") ){
            myViewHolder.statusTxt.setBackgroundColor(Color.GREEN);
        }else{
            myViewHolder.statusTxt.setBackgroundColor(Color.LTGRAY);
        }
        */
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

        final String temp = approval_list.get(position).getPr_url();
        myViewHolder.btn.setOnClickListener(new View.OnClickListener() {

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
        TextView hospital_no, lastname, firstname, start_date, end_date, pr_number, amount, transact_id, status, pr_url;
        CheckBox cb;
        Button btn;
    }
}
