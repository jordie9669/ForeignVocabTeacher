package com.example.c00204110.languagevocabteacher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 2560P on 12/07/2018.
 */

public class TranslationsListAdapter extends RecyclerView.Adapter<TranslationsListAdapter.ViewHolder> {

    public List<Translations> translationsList;
    public Context context;

    public TranslationsListAdapter(Context context, List translationsList){

        this.translationsList = translationsList;
        this.context = context;
    }


    @NonNull
    @Override
    public TranslationsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.translations_item, parent, false);
        return new TranslationsListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TranslationsListAdapter.ViewHolder holder, final int position) {

        holder.translations_button.setText(translationsList.get(position).getTranslations());    //Gets each value  using getName from Languages.java

        holder.translations_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals g = Globals.getInstance();
                g.setSelectedTranslations(holder.translations_button.getText().toString());
                //Intent wordIntent = new Intent(context, LearnWordActivity.class);
                //context.startActivity(wordIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return translationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView translations_button;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            translations_button = mView.findViewById(R.id.translations_button);


        }
    }

}