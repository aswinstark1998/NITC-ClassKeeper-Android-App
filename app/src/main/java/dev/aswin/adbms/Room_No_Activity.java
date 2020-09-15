package dev.aswin.adbms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Room_No_Activity extends AppCompatActivity {

    List<BuildingModalClass> roomNoList;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room__no_);
        recyclerView = findViewById(R.id.recyclerview_roomNo);

        final Intent intent = getIntent();
        String buildingName = intent.getStringExtra("BuildingName");

        DatabaseReference mDatabaseReference = firebaseDatabase.getReference("Building").child(buildingName);
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomNoList = new ArrayList<BuildingModalClass>();
                String roomNoString = dataSnapshot.getValue().toString();
                String[] roomNos = roomNoString.split(",");
                for(int i=0; i<roomNos.length; i++){
                    roomNoList.add(new BuildingModalClass(roomNos[i]));
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(Room_No_Activity.this, roomNoList);
                recyclerView.setLayoutManager(new GridLayoutManager(Room_No_Activity.this, 2));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
