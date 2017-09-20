package com.example.kumar.chatapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;
import android.telephony.SmsMessage;

public class MessageReceiver extends BroadcastReceiver
{
    String sender;
    String message;
    Context context;
    private static final String SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";

    public  void onReceive(Context context,Intent intent)
    {
        if(intent.getAction().equals(SMS_RECEIVED)){
            Bundle bundle=intent.getExtras();
            if(bundle!=null){
                Object[] pdus=(Object[]) bundle.get("pdus");
                 SmsMessage[] messages=new SmsMessage[pdus !=null ? pdus.length:0];
                StringBuilder sb=new StringBuilder();
                for(int i=0;i< pdus.length;i++)
                {
                    messages[i]=SmsMessage.createFromPdu((byte[])(pdus!=null ? pdus[i] :null));
                   sb.append(messages[i].getMessageBody());
                }
                sender=messages[0].getOriginatingAddress();
                message=sb.toString();
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                abortBroadcast();

                addnotification();

                SmsActivity inst=SmsActivity.instance();
                inst.updateList(message);




            }



       /*
            Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
            Intent broadcastIntent=new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("message",str);
            context.sendBroadcast(broadcastIntent);*/

        }

    }
    private void addnotification(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
               .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentTitle(sender)
                .setContentText(message);

        Intent notificationintent=new Intent(context, SmsActivity.class);
        TaskStackBuilder stackbuilder=TaskStackBuilder.create(context);
        stackbuilder.addParentStack(SmsActivity.class);
        stackbuilder.addNextIntent(notificationintent);

        PendingIntent contentIntent=stackbuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        NotificationManager manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());



    }

}
