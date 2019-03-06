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

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    public List<Words> wordList;
    public Context context;

    public WordListAdapter(Context context, List wordList){

        this.wordList = wordList;
        this.context = context;
    }


    @NonNull
    @Override
    public WordListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new WordListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WordListAdapter.ViewHolder holder, final int position) {

        holder.word_button.setText(wordList.get(position).getName());    //Gets each value  using getName from Languages.java

        holder.word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals g = Globals.getInstance();
                g.setSelectedWord(holder.word_button.getText().toString());
                Intent wordIntent = new Intent(context, LearnWordActivity.class);
                context.startActivity(wordIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView word_button;
        public ImageButton searchButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            word_button = mView.findViewById(R.id.word_button);
            searchButton = mView.findViewById(R.id.search_button);

        }
    }

}
