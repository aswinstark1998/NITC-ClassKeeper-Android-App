package dev.aswin.adbms;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewSpecificRoomAdapter extends RecyclerView.Adapter<RecyclerViewSpecificRoomAdapter.SpecificRoomViewHolder> {

    public List<SpecificRoomDetailsModal> mdata;
    public Context mContext;

    public RecyclerViewSpecificRoomAdapter(List<SpecificRoomDetailsModal> mdata, Context mContext) {
        this.mdata = mdata;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SpecificRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_specific_room_details, parent, false);
        return new SpecificRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SpecificRoomViewHolder holder, final int position) {
        holder.tv_teacher_name.setText(mdata.get(position).getTeacher());
        if (mdata.get(position).getEngaged() == 0) {
            holder.tv_booking_status.setText("Not Yet Engaged !");
        } else {
            holder.tv_booking_status.setText("Engaged");
        }

        holder.tv_slot.setText(mdata.get(position).getSlot());
        holder.tv_slot.setTextSize(25);
        holder.tv_subject.setText(mdata.get(position).getSubject());
        if (mdata.get(position).getEngaged() == 0) {
            holder.cardView_specific_slot.setClickable(false);
            holder.tv_booking_status.setBackgroundColor(0xfff00000);
            holder.tv_booking_status.setTextColor(Color.WHITE);
        }

        holder.cardView_specific_slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = mContext.getSharedPreferences("SIGNED_IN_OR_NOT", Context.MODE_PRIVATE);
                String signedIn = preferences.getString("signedIn", "false");
                if (signedIn.equals("true")) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.teacher_edit_custom);
                    dialog.setTitle("Title...");
                    dialog.setCancelable(false);
                    dialog.show();
                    TextView text_Subject = (TextView) dialog.findViewById(R.id.custom_dialog_subject);
                    final TextView text_Remarks = (TextView) dialog.findViewById(R.id.custom_dialog_Remarks);
                    final Switch sw = dialog.findViewById(R.id.enable_class);
                    Button btn_OK = dialog.findViewById(R.id.custom_dialog_OK);
                    Button btn_Cancel = dialog.findViewById(R.id.custom_dialog_Cancel);
                    btn_Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btn_OK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                            SpecificRoomDetailsModal updateModal = new SpecificRoomDetailsModal(0, "No class Today", mdata.get(position).getSlot(), mdata.get(position).getSubject(), mdata.get(position).getTeacher());
                            DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference("Defaults").child(MainActivity.buildingName)
                                    .child("" + MainActivity.roomNo + Specific_Room_Detail_Activity.dayOfWeek).child(mdata.get(position).getSlot());
                            if (sw.isChecked()) {
                                mDatabaseReference.child("Engaged").setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        holder.tv_booking_status.setText("Engaged");
                                        holder.tv_booking_status.setBackgroundColor(Color.GREEN);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mContext, "Failed to connect to DB !", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                mDatabaseReference.child("Engaged").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        holder.tv_booking_status.setText("Not Engaged !");
                                        holder.tv_booking_status.setBackgroundColor(Color.GREEN);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(mContext, "Failed to connect to DB !", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                            String remarks = text_Remarks.getText().toString();
                            mDatabaseReference.child("Remarks").setValue(remarks).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(mContext, "Remarks Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(mContext, "Failed to connect to DB !", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });
                    Toast.makeText(mContext, "Access Granted to edit !", Toast.LENGTH_SHORT).show();

                }
                if (signedIn.equals("false")) {
                    Toast.makeText(mContext, "Who are you, pal !?", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }


    public static class SpecificRoomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_teacher_name;
        TextView tv_booking_status;
        TextView tv_slot;
        TextView tv_subject;
        CardView cardView_specific_slot;

        public SpecificRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_slot = itemView.findViewById(R.id.item_recycler_slot);
            tv_teacher_name = itemView.findViewById(R.id.txt_teacherName_specific_room_slot_booking);
            tv_booking_status = itemView.findViewById(R.id.txt_booking_status);
            tv_subject = itemView.findViewById(R.id.txt_subject);
            cardView_specific_slot = itemView.findViewById(R.id.cardview_specific_room);
        }
    }
}
