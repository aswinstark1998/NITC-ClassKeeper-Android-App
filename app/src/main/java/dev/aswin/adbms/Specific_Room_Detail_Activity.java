package dev.aswin.adbms;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Specific_Room_Detail_Activity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    List<SpecificRoomDetailsModal> bookingStatus;
    RecyclerView recyclerView;
    TextView tv_roomNo;
    RecyclerViewSpecificRoomAdapter roomAdapter;
    public static int dayOfWeek;
    HashMap<String, GetTeacherName> teacherDetails = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific__room__detail_);
        tv_roomNo = findViewById(R.id.specific_room_activity_RoomNo);

        Calendar c = Calendar.getInstance();
        dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        Log.e("DAY INTEGER", "" + dayOfWeek);

        recyclerView = findViewById(R.id.recyclerview_roomBookingDetails);

        DatabaseReference mDatabaseReference = firebaseDatabase.getReference("Defaults").child(MainActivity.buildingName)
                .child("" + MainActivity.roomNo + dayOfWeek);

        Log.e("ROOM No And DAY", MainActivity.roomNo + dayOfWeek);
        tv_roomNo.setText(MainActivity.buildingName + ": Room - " + MainActivity.roomNo);

        bookingStatus = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference teacherRef = firebaseDatabase.getReference("Teachers");
        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds != null) {
                            String key = ds.getKey();
                            Log.e("Teacher", key);
                            GetTeacherName teacher = ds.getValue(GetTeacherName.class);
                            Log.e("TEACHER DETAILS", "" + key + ", " + teacher.getName());
                            teacherDetails.put(key, teacher);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds != null) {
                            SpecificRoomDetailsModal modal = ds.getValue(SpecificRoomDetailsModal.class);
                            String teacherKey = modal.getTeacher();
                            GetTeacherName teacher = teacherDetails.get(teacherKey);
                            modal.setTeacher(teacher.getName());
                            bookingStatus.add(modal);
                            Log.e("ENGAGED", "" + modal.getEngaged());
                            Log.e("REMARKS", modal.getRemarks());
                        }
                    }
                }
                roomAdapter = new RecyclerViewSpecificRoomAdapter(bookingStatus, Specific_Room_Detail_Activity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(Specific_Room_Detail_Activity.this));
                recyclerView.setAdapter(roomAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}



