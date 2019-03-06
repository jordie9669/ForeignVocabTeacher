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
public class MyWordsListAdapter extends RecyclerView.Adapter<MyWordsListAdapter.ViewHolder> {
    public List<MyWords> myWordsList;
    public Context context;
    public MyWordsListAdapter(Context context, List myWordsList){
        this.myWordsList = myWordsList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyWordsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_word_item, parent, false);
        return new MyWordsListAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyWordsListAdapter.ViewHolder holder2, final int position) {
        holder2.my_word_button.setText(myWordsList.get(position).getName());    //Gets each value  using getName from MyWords.java
        holder2.my_word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Globals g = Globals.getInstance();
                g.setMySelectedWord(holder2.my_word_button.getText().toString());
                Intent myWordIntent = new Intent(context, LearnMyWordActivity.class);
                context.startActivity(myWordIntent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return myWordsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView my_word_button;
        public ImageButton searchButton;
        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            my_word_button = mView.findViewById(R.id.my_word_button);
            searchButton = mView.findViewById(R.id.search_button);
        }
    }
}
