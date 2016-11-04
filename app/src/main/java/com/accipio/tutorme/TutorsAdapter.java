package com.accipio.tutorme;

/**
 * Created by rachel on 2016-11-03.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class TutorsAdapter extends RecyclerView.Adapter<TutorsAdapter.TutorViewHolder> {
    private ArrayList<Tutor> mDataSet;

    public TutorsAdapter(ArrayList<Tutor> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public TutorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        TutorViewHolder tutorViewHolder = new TutorViewHolder(v);
        return tutorViewHolder;
    }

    @Override
    public void onBindViewHolder(TutorViewHolder holder, int position) {
        holder.name.setText(mDataSet.get(position).getName());
        holder.desc.setText(mDataSet.get(position).getDesc());
        holder.rating.setText(mDataSet.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class TutorViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView icon, name, desc, rating;

        TutorViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.browse_layout);
            icon = (TextView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            desc = (TextView) itemView.findViewById(R.id.desc);
            rating = (TextView) itemView.findViewById(R.id.rating);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

