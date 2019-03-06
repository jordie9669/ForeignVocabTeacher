package com.example.c00204110.languagevocabteacher;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
public class LanguagesListAdapter extends RecyclerView.Adapter<LanguagesListAdapter.ViewHolder> {
    public List<Languages> languagesList;
    public Context context;
    public LanguagesListAdapter(Context context, List languagesList){
        this.languagesList = languagesList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.language_button.setText(languagesList.get(position).getName());    //Gets each value  using getName from Languages.java
        holder.language_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals g = Globals.getInstance();
                g.setData(holder.language_button.getText().toString());
                Intent sectorIntent = new Intent(context, ChooseSectorActivity.class);
                context.startActivity(sectorIntent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return languagesList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView language_button;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            language_button = mView.findViewById(R.id.language_button);
        }
    }
}
