package dev.aswin.adbms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class FacultyHomeActivity extends AppCompatActivity {
    List<BuildingModalClass> buildings;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home);


        btn_logout = findViewById(R.id.btnLogOut);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FacultyHomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                SharedPreferences signedInOrNot = FacultyHomeActivity.this.getSharedPreferences("SIGNED_IN_OR_NOT", Context.MODE_PRIVATE);
                signedInOrNot.edit().putString("signedIn", "false").apply();
                startActivity(intent);
            }
        });

        buildings = new ArrayList<BuildingModalClass>();
        recyclerView = findViewById(R.id.teacher_BuildingNameRecyclerView);
        DatabaseReference mDatabaseReference = firebaseDatabase.getReference("Building");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String buildingName = ds.getKey();
                    BuildingModalClass modalClass = new BuildingModalClass(buildingName);
                    Log.e("Building Name", buildingName);
                    buildings.add(modalClass);
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(FacultyHomeActivity.this, buildings);
                recyclerView.setLayoutManager(new GridLayoutManager(FacultyHomeActivity.this, 2));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.recyclerReuse = 0;
    }
}
