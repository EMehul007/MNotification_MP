package com.example.mnotification.Call_History;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mnotification.R;

public class InsertCallLogActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtContactName, edtContactNumber;
    private Button btnAddContact, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_call_log);

        edtContactName = (EditText) findViewById(R.id.edtContactName);
        edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);

        btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnAddContact.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(this, Call_HistoryActivity.class);
        if (v.getId() == R.id.btnAddContact) {
            if (edtContactName.getText().toString().equals("")
                    && edtContactNumber.getText().toString().equals("")) {
                Toast.makeText(this, "Please fill both fields...",
                        Toast.LENGTH_SHORT).show();
            } else {
                CallLogHelper.insertPlaceholderCall(getContentResolver(),
                        edtContactName.getText().toString(), edtContactNumber
                                .getText().toString());
                edtContactName.setText("");
                edtContactNumber.setText("");

                startActivity(intent);
            }

        } else if (v.getId() == R.id.btnCancel)
        {
            startActivity(intent);
        }

    }


}
