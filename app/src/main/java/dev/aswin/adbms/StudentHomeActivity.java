package dev.aswin.adbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeActivity extends AppCompatActivity {

    List<BuildingModalClass> buildings;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        buildings = new ArrayList<BuildingModalClass>();
        recyclerView = findViewById(R.id.recyclerview_studentHome);
        DatabaseReference mDatabaseReference = firebaseDatabase.getReference("Building");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String buildingName = ds.getKey();
                    BuildingModalClass modalClass = new BuildingModalClass(buildingName);
                    Log.e("Building Name", buildingName);
                    buildings.add(modalClass);
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(StudentHomeActivity.this, buildings);
                recyclerView.setLayoutManager(new GridLayoutManager(StudentHomeActivity.this, 2));
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
