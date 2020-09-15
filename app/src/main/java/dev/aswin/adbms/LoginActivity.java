package dev.aswin.adbms;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LoginActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText edt_email, edt_password;
    DatePickerDialog datePickerDialog;
    FirebaseAuth firebaseAuth;
    public static final String DATE_FORMAT_1 = "dd-MM-yyyy";
    public static String dateToPerformDatabaseFetch = "";
    SharedPreferences signedInOrNot;

    @Override
    protected void onResume() {
        super.onResume();
        Switch sw = findViewById(R.id.StudentSwitch);
        sw.setChecked(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //====================   DATE DISCOVERY  =================================//
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        String date = "" + dateFormat.format(today);
        String[] dateElements = date.split("-");
        int day = Integer.parseInt(dateElements[0]);
        int month = Integer.parseInt(dateElements[1]);
        int year = Integer.parseInt(dateElements[2]);
        dateToPerformDatabaseFetch = ""+day+month+year;
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        Log.e("DAY INTEGER", "" + dayOfWeek);
      //  Toast.makeText(this, ""+dateToPerformDatabaseFetch, Toast.LENGTH_SHORT).show();
        datePickerDialog = new DatePickerDialog(this, LoginActivity.this, year, month, day);
        //=======================================================================//

        SharedPreferences signedInOrNot = LoginActivity.this.getSharedPreferences("SIGNED_IN_OR_NOT", Context.MODE_PRIVATE);

        firebaseAuth = FirebaseAuth.getInstance();

        Switch sw = (Switch) findViewById(R.id.StudentSwitch);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    datePickerDialog.show();
                } else {
                }
            }
        });
    }

    public void GoToRegistrationPage(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActiivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
    }

    public void FacultyLogin(View view) {
        edt_email = findViewById(R.id.txtEmail);
        edt_password = findViewById(R.id.txtPwd);

        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill empty fields !!", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Authenticating");
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Verifying credential authenticity...");
                    progressDialog.show();
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        signedInOrNot.edit().putString("signedIn", "false").apply();
                        Toast.makeText(LoginActivity.this, "Login Unsuccessful :(", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Successful :)", Toast.LENGTH_SHORT).show();
                        signedInOrNot = LoginActivity.this.getSharedPreferences("SIGNED_IN_OR_NOT", Context.MODE_PRIVATE);
                        signedInOrNot.edit().putString("signedIn", "true").apply();
                        Intent intent = new Intent(LoginActivity.this, FacultyHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Toast.makeText(this, "" + view.getDayOfMonth() + "/" + view.getMonth() + "/" + view.getYear(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, StudentHomeActivity.class);
        startActivity(intent);
    }
}