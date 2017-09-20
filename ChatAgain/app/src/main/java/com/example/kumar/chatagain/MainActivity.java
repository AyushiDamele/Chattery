package com.example.kumar.chatagain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AbsListView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listview;
    private EditText chatText;
    private Button buttonSend;
    private boolean side=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend =(Button) findViewById(R.id.send);
        listview=(ListView)findViewById(R.id.msgview);
        chatArrayAdapter=new ChatArrayAdapter(getApplicationContext(),R.layout.right);
        listview.setAdapter(chatArrayAdapter);

        chatText =(EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
           public boolean onKey(View v,int keyCode, KeyEvent event) {
               if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                   return sendChatMessage();
               }
               return false;
           }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listview.setAdapter(chatArrayAdapter);

        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listview.setSelection(chatArrayAdapter.getCount() -1);
            }
        });
    }

    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side,chatText.getText().toString()));
        chatText.setText("");
        side=!side;
        return true;
    }
}


