package com.accipio.tutorme;

/**
 * Created by rachel on 2016-11-03.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;


public class TutorsAdapter extends RecyclerView.Adapter<TutorsAdapter.TutorViewHolder> implements Filterable, View.OnClickListener {

    protected ArrayList<Tutor> mDataSet;
    protected ArrayList<Tutor> filteredList;
    protected ArrayList<Tutor> original;

    public TutorsAdapter(ArrayList<Tutor> mDataSet) {
        this.mDataSet = mDataSet;
        this.original = mDataSet;
        this.filteredList = mDataSet;
    }

    private int expandedPosition = -1;
    private boolean isOther = true;

    @Override
    public TutorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        TutorViewHolder tutorViewHolder = new TutorViewHolder(v);

        tutorViewHolder.cardView.setOnClickListener(this);
        tutorViewHolder.cardView.setTag(tutorViewHolder);

        return tutorViewHolder;
    }

    @Override
    public void onBindViewHolder(TutorViewHolder holder, int position) {
        holder.name.setText(filteredList.get(position).getName());
        holder.desc.setText(filteredList.get(position).getDesc());
        holder.rate.setText("$" + filteredList.get(position).getRate());

        float rating = filteredList.get(position).getRating();
        holder.rating.setRating(rating);
        //holder.rating.setStepSize(0.01f);

        if (position == expandedPosition && isOther) {
            holder.message.setVisibility(View.VISIBLE);
            holder.hire.setVisibility(View.VISIBLE);
        } else {
            holder.message.setVisibility(View.GONE);
            holder.hire.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (filteredList != null) {
            return filteredList.size();
        }
        return 0;
    }

    public class TutorViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView icon, name, desc, rate;
        RatingBar rating;
        Button message, hire;

        TutorViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.browse_layout);
            icon = (TextView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            desc = (TextView) itemView.findViewById(R.id.desc);
            rate = (TextView) itemView.findViewById(R.id.rate);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            message = (Button) itemView.findViewById(R.id.messageButton);
            hire = (Button) itemView.findViewById(R.id.hireButton);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                filteredList = (ArrayList<Tutor>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<Tutor> FilteredArrList = new ArrayList<Tutor>();

                if (original == null) {
                    original = new ArrayList<Tutor>(mDataSet);
                }

                if (constraint == null || constraint.length() == 0) {
                    results.count = original.size();
                    results.values = original;
                }
                else {
                    boolean none = false;
                    String constraints[] = constraint.toString().toLowerCase().split("-");
                    String rateStr = constraints[0];
                    String ratingStr = constraints[1];
                    String courseStr = constraints[2];
                    if (courseStr.equals("none")) {
                        none = true;
                        courseStr = " ";
                    }

                    int status = Integer.parseInt(constraints[3]);
                    int rateNum = Integer.parseInt(rateStr.split("_")[1]);
                    int ratingNum = Integer.parseInt(ratingStr.split("_")[1]);

                    for (Tutor item : mDataSet) {
                        if ((Integer.parseInt(item.getRate()) <= rateNum) && (item.getRating() >= ratingNum) && (item.getStatus() >= status)) {
                            if (!none) {
                                for (int i = 0; i < item.getCourses().length; i++) {
                                    if (item.getCourses()[i].toLowerCase().equals(courseStr)) {
                                        FilteredArrList.add(item);
                                    }
                                }
                            }
                            if (none) {
                                FilteredArrList.add(item);
                            }
                        }
                    }
                    results.values = FilteredArrList;
                    results.count = FilteredArrList.size();
                }
                return results;
            }
        };
        return filter;
    }

    @Override
    public void onClick(View view) {
        TutorViewHolder holder = (TutorViewHolder) view.getTag();
        if (holder != null) {
            // Check for an expanded view, collapse if you find one
            if (expandedPosition >= 0) {
                notifyItemChanged(expandedPosition);
            }
            if (isOther) {
                isOther = (holder.getPosition() == expandedPosition) ? false : true;
            }
            else {
                isOther = true;
            }
            expandedPosition = holder.getPosition();
            notifyItemChanged(expandedPosition);
        }
    }
}

