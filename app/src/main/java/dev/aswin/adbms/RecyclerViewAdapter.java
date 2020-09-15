package dev.aswin.adbms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<BuildingModalClass> data;


    public RecyclerViewAdapter(Context context, List<BuildingModalClass> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_cardview_buildings, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Random random = new Random();
        final int color = Color.argb(100, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        holder.tv_building_name.setText(data.get(position).getBuildingName());
        holder.tv_building_name.setBackgroundColor(color);

        if (MainActivity.recyclerReuse == 0) { // RecyclerReuse is 0 => It's like normal visit or first encounter.
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.recyclerReuse = 1;
                    MainActivity.buildingName = data.get(position).getBuildingName();
                    Intent intent = new Intent(context, Room_No_Activity.class);
                    intent.putExtra("BuildingName", data.get(position).getBuildingName());
                    Log.e("BUILDING NAME", data.get(position).getBuildingName());
                    context.startActivity(intent);
                }
            });
        }
        if (MainActivity.recyclerReuse == 1) {
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.recyclerReuse = 0;
                    MainActivity.roomNo = data.get(position).getBuildingName(); // here we re-used the class. It's giving room no here.
                    Intent intent = new Intent(context, Specific_Room_Detail_Activity.class);
                    intent.putExtra("RoomNo", data.get(position).getBuildingName()); //I'm re-using BuildingModalClass here
                    Log.e("ROOM NUMBER", data.get(position).getBuildingName());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_building_name;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_buildings);
            tv_building_name = itemView.findViewById(R.id.txt_buildingNameItem);
        }
    }
}
