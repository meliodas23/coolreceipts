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

import com.solutions.ibarra.coolreceipts.pojo.BatchPOJO;

import java.util.ArrayList;

/**
 * Created by Asrock01 on 4/4/2015.
 */
public class ApprovedBatchAdapter extends BaseAdapter {

    public ArrayList<BatchPOJO> batch_list = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public ApprovedBatchAdapter(Context context, ArrayList<BatchPOJO> batch_list) {
        this.batch_list = batch_list;
        this.context = context;

        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return batch_list.size();
    }

    @Override
    public Object getItem(int position) {
        return batch_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder myViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.approved_list_batch, null);
            myViewHolder = new MyViewHolder();
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }
        myViewHolder.status = detail(convertView, R.id.status_batch, batch_list.get(position).getStatus());
        myViewHolder.batch_no = detail(convertView, R.id.batchno, batch_list.get(position).getBatchno());
        myViewHolder.patients = detail(convertView, R.id.patients_batch, batch_list.get(position).getPatients());
        myViewHolder.dateuploaded = detail(convertView, R.id.uploaddate_batch, batch_list.get(position).getDateuploaded());
        myViewHolder.amount = detail(convertView, R.id.amount_batch, batch_list.get(position).getAmount());


        if (batch_list.get(position).getStatus().trim().equals("PRs Received") ||
                batch_list.get(position).getStatus().trim().equals("Paid")) {
            myViewHolder.status.setBackgroundColor(Color.GREEN);
        } else {
            myViewHolder.status.setBackgroundColor(Color.LTGRAY);
        }


        return convertView;
    }

    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }

    private class MyViewHolder {
        TextView status, batch_no, patients, dateuploaded, amount;

    }
}
