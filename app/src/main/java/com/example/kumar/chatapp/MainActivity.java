package com.example.kumar.chatapp;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.telephony.SmsManager;

public class MainActivity extends AppCompatActivity {

    Button btnSend;
    EditText tvNumber;
    EditText tvMessage;
    IntentFilter intentFilter;
    RelativeLayout main_layout;
    Button contactsButton;
    private static final int PICK_CONTACT = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {

            switch (item.getItemId()) {
                case R.id.black: {
                    main_layout.setBackgroundColor(Color.BLACK);
                    return true;
                }
                case R.id.font_size: {
                    tvMessage.setTypeface(Typeface.DEFAULT_BOLD);
                    tvNumber.setTypeface(Typeface.DEFAULT_BOLD);
                    return true;
                }
                case R.id.white: {
                    main_layout.setBackgroundColor(Color.WHITE);
                    return true;
                }
                case R.id.default_font: {
                    tvMessage.setTypeface(Typeface.DEFAULT);
                    tvNumber.setTypeface(Typeface.DEFAULT);
                    return true;
                }
                case R.id.inbox: {
                    Intent intent = new Intent(this, SmsActivity.class);
                    this.startActivity(intent);
                    return true;
                }

                default:
                    return super.onOptionsItemSelected(item);

            }
        } catch (Exception e) {

        }
        return true;
    }

        private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TextView inTxt = (TextView) findViewById(R.id.textMsg);
                inTxt.setText(intent.getExtras().getString("message"));
            }
        };


        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            intentFilter = new IntentFilter();
            intentFilter.addAction("SMS_RECEIVED_ACTION");

            contactsButton = (Button) findViewById(R.id.contactsbutton);
            btnSend = (Button) findViewById(R.id.btnSend);
            tvNumber = (EditText) findViewById(R.id.tvNumber);
            tvMessage = (EditText) findViewById(R.id.tvMessage);
            main_layout = (RelativeLayout) findViewById(R.id.main_layout);

            btnSend.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               String myMsg = tvMessage.getText().toString();
                                               String theNumber = tvNumber.getText().toString();
                                               sendMsg(theNumber, myMsg);
                                               tvMessage.setText(null);
                                           }
                                       }

            );
            contactsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                }
            });


        }

    protected void sendMsg(String theNumber, String myMsg) {
        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";
        PendingIntent setPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber, null, myMsg, setPI, deliveredPI);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACT:
                    Cursor c = null;

                    try {
                        Uri uri = data.getData();
                        c = getContentResolver().query(uri, null, null, null, null);
                        c.moveToFirst();
                        String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        tvNumber.setText(number);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Log.e("Main Activity", "Failed to pick Contact");
        }

    }


    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}

