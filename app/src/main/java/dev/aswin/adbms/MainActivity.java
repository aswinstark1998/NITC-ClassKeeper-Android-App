package dev.aswin.adbms;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    public static int recyclerReuse = 0;
    public static String roomNo = null;
    public static String buildingName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        new CountDownTimer(2000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent intent;

                if (Settings.Global.getInt(getApplicationContext().getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        SharedPreferences signedInOrNot = MainActivity.this.getSharedPreferences("SIGNED_IN_OR_NOT", Context.MODE_PRIVATE);
                        signedInOrNot.edit().putString("signedIn", "true").apply();
                        intent = new Intent(MainActivity.this, FacultyHomeActivity.class /* Already logged in, go to teacher home Activity */);
                        finish();
                        startActivity(intent);
                    } else {
                        SharedPreferences signedInOrNot = MainActivity.this.getSharedPreferences("SIGNED_IN_OR_NOT", Context.MODE_PRIVATE);
                        signedInOrNot.edit().putString("signedIn", "false").apply();
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Date Fetch Failed")
                            .setCancelable(false)
                            .setMessage("Please Enable Automatic Network Date-Time in your device !")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).show();
                }


            }
        }.start();
    }
}
