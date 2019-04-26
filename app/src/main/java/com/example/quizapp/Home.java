package com.example.quizapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.quizapp.constants.Common;

import java.util.Random;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("registrationComplete"));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter(Common.STR_PUSH));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        registerNotification();

        bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragmeent = null;
                switch (menuItem.getItemId()) {
                    case R.id.action_category:
                        selectedFragmeent = CategoryFragment.newInstance();
                        break;
                    case R.id.action_ranking:
                        selectedFragmeent = RankingFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragmeent);
                transaction.commit();
                return true;
            }


        });

        setDefaultFragment();
    }

    private void registerNotification() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Common.STR_PUSH)){
                    String message = intent.getStringExtra("message");
                    System.out.println(message);
                    showNotification("Jose",message);
                }
            }
        };
    }
    private void  showNotification(String title,String message){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());

    }
    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        transaction.commit();
    }
}
