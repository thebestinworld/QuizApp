package com.example.quizapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quizapp.BroadcastReceiver.AlarmReceiver;
import com.example.quizapp.constants.Common;
import com.example.quizapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    /*
    Fields for sign up
     */
    MaterialEditText edtNewUser;

    MaterialEditText edtNewPassword;

    MaterialEditText edtNewEmail;

    /*
    Fields for sing in
     */
    MaterialEditText edtUser;

    MaterialEditText edtPassword;

    Button btnSignUp;

    Button btnSignIn;

    FirebaseDatabase database;

    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerAlarm();

        //Firebase
        this.database = FirebaseDatabase.getInstance();
        this.users = database.getReference("Users");

        this.edtUser = findViewById(R.id.edtUser);
        this.edtPassword = findViewById(R.id.edtPassword);

        this.btnSignIn = findViewById(R.id.btn_sign_in);
        this.btnSignUp = findViewById(R.id.btn_sign_up);

        this.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        this.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(edtUser.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,20);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void signIn(final String username, final String password) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(username).exists()){
                    if(!username.isEmpty()){
                        User loggedUser =   dataSnapshot.child(username).getValue(User.class);
                        String hash = new String(Hex.encodeHex(DigestUtils.sha256(password)));
                        if(loggedUser.getPassword().equals(hash)){
                            Toast.makeText(MainActivity.this,"Login Successful!",Toast.LENGTH_SHORT).show();
                            Intent homActivity = new Intent(MainActivity.this,Home.class);
                            Common.currentUser = loggedUser;
                            startActivity(homActivity);
                            finish();
                        }else {
                            Toast.makeText(MainActivity.this,"Wrong Password!",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(MainActivity.this,"Please enter your username!",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"User does not exist!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(MainActivity.this);
        alerDialog.setTitle("Sign Up!");
        alerDialog.setMessage("Please fill your information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        this.edtNewUser = sign_up_layout.findViewById(R.id.edtNewUserName);
        this.edtNewPassword = sign_up_layout.findViewById(R.id.edtNewPassword);
        this.edtNewEmail = sign_up_layout.findViewById(R.id.edtNewEmail);

        alerDialog.setView(sign_up_layout);
        alerDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alerDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hash = new String(Hex.encodeHex(DigestUtils.sha256(edtNewPassword.getText().toString())));
                final User user = new User(edtNewUser.getText().toString(), hash, edtNewEmail.getText().toString());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists()) {
                            Toast.makeText(MainActivity.this, "User already exist!", Toast.LENGTH_SHORT).show();
                        } else {
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(MainActivity.this, "User registration successful!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                dialog.dismiss();
            }
        });
        alerDialog.show();
    }
}
