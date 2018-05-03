package com.rmobdick.twplant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by rodrigo on 12/04/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> mPlantNames = new ArrayList<>();
    private ArrayList<String> mPlantIds = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mPlantNames, ArrayList<String> mPlantIds, Context mContext) {
        this.mPlantNames = mPlantNames;
        this.mPlantIds = mPlantIds;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Responsavel por inflar a view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_plant, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: :)");
        holder.plantName.setText(mPlantNames.get(position));
        holder.plantId.setText(mPlantIds.get(position));

        holder.plantListItem.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked " + mPlantNames.get(position));


                Intent intent = new Intent(mContext, PlantInfoActivity.class);
                intent.putExtra("plant_name", mPlantNames.get(position));
                intent.putExtra("plant_ID", mPlantIds.get(position));
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlantNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView plantName;
        TextView plantId;
        RelativeLayout plantListItem;

        public ViewHolder(View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.list_plant_nome_planta);
            plantId = itemView.findViewById(R.id.list_plant_id_planta);
            plantListItem = itemView.findViewById(R.id.plant_list_item);

        }
    }
}
