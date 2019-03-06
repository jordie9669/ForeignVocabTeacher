package com.example.c00204110.languagevocabteacher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
public class SectorListAdapter extends RecyclerView.Adapter<SectorListAdapter.ViewHolder> {
    public List<Sectors> sectorList;
    public Context sectorContext;

    public SectorListAdapter(Context sectorContext, List sectorList) {
        this.sectorList = sectorList;
        this.sectorContext = sectorContext;
    }

    @NonNull
    @Override
    public SectorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sector_item, parent, false);
        return new SectorListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SectorListAdapter.ViewHolder holder, int position) {
        holder.sector_button.setText(sectorList.get(position).getName());    //Gets each value  using getName from Languages.java
        holder.sector_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals g = Globals.getInstance();
                g.setSelectedSector(holder.sector_button.getText().toString());
                Intent wordIntent = new Intent(sectorContext, ChooseWordActivity.class);
                sectorContext.startActivity(wordIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sectorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View sView;
        public TextView sector_button;

        public ViewHolder(View itemView) {
            super(itemView);
            sView = itemView;
            sector_button = sView.findViewById(R.id.sector_button);
        }
    }
}
