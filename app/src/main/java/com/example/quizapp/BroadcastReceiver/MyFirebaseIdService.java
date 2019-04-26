package com.example.quizapp.BroadcastReceiver;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.quizapp.constants.Common;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseIdService  extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
         handleNotification(remoteMessage.getNotification().getBody());
    }

    private void handleNotification(String body) {
        Intent intent = new Intent(Common.STR_PUSH);
        intent.putExtra("message",body);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
