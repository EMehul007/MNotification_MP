package com.example.mnotification.Call_History;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.mnotification.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Call_HistoryActivity extends ListActivity {

    @BindView(R.id.btn_add)
    Button btnAdd;

    private ArrayList<String> conNames;
    private ArrayList<String> conNumbers;
    private ArrayList<String> conTime;
    private ArrayList<String> conDate;
    private ArrayList<String> conType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call__history);
        ButterKnife.bind(this);

        conNames = new ArrayList<String>();
        conNumbers = new ArrayList<String>();
        conTime = new ArrayList<String>();
        conDate = new ArrayList<String>();
        conType = new ArrayList<String>();


        Cursor curLog = CallLogHelper.getAllCallLogs(getContentResolver());

        setCallLogs(curLog);

        setListAdapter(new MyAdapter(this, android.R.layout.simple_list_item_1,
                R.id.tvNameMain, conNames));

    }

    @OnClick(R.id.btn_add)
    public void onViewClicked()
    {
        Intent intent = new Intent(Call_HistoryActivity.this, InsertCallLogActivity.class);
        startActivity(intent);
    }

    private class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         ArrayList<String> conNames) {
            super(context, resource, textViewResourceId, conNames);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = setList(position, parent);
            return row;
        }

        private View setList(int position, ViewGroup parent) {
            LayoutInflater inf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inf.inflate(R.layout.liststyle, parent, false);

            TextView tvName = (TextView) row.findViewById(R.id.tvNameMain);
            TextView tvNumber = (TextView) row.findViewById(R.id.tvNumberMain);
            TextView tvTime = (TextView) row.findViewById(R.id.tvTime);
            TextView tvDate = (TextView) row.findViewById(R.id.tvDate);
            TextView tvType = (TextView) row.findViewById(R.id.tvType);

            tvName.setText(conNames.get(position));
            tvNumber.setText(conNumbers.get(position));
            tvTime.setText("( " + conTime.get(position) + "sec )");
            tvDate.setText(conDate.get(position));
            tvType.setText("( " + conType.get(position) + " )");

            return row;
        }
    }

    private void setCallLogs(Cursor curLog) {
        while (curLog.moveToNext()) {
            String callNumber = curLog.getString(curLog
                    .getColumnIndex(CallLog.Calls.NUMBER));
            conNumbers.add(callNumber);

            String callName = curLog
                    .getString(curLog
                            .getColumnIndex(CallLog.Calls.CACHED_NAME));
            if (callName == null) {
                conNames.add("Unknown");
            } else
                conNames.add(callName);

            String callDate = curLog.getString(curLog
                    .getColumnIndex(CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long
                    .parseLong(callDate)));
            conDate.add(dateString);

            String callType = curLog.getString(curLog
                    .getColumnIndex(CallLog.Calls.TYPE));
            if (callType.equals("1")) {
                conType.add("Incoming");
            } else
                conType.add("Outgoing");

            String duration = curLog.getString(curLog
                    .getColumnIndex(CallLog.Calls.DURATION));
            conTime.add(duration);

        }
    }


}
