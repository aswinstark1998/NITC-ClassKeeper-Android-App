package dev.aswin.adbms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegistrationActiivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_actiivity);
    }

    public void RequestForAccount(View view) {
    }

    public void GoToLoginPage(View view) {
        Intent intent = new Intent(RegistrationActiivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
