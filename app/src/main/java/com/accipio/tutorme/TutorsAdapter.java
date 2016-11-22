package com.accipio.tutorme;

/**
 * Created by rachel on 2016-11-03.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

public class TutorsAdapter extends RecyclerView.Adapter<TutorsAdapter.TutorViewHolder> implements Filterable{
    protected ArrayList<Tutor> mDataSet;
    protected ArrayList<Tutor> filteredList;
    protected ArrayList<Tutor> original;
    protected Filter tutorFilter;

    public TutorsAdapter(ArrayList<Tutor> mDataSet) {
        this.mDataSet = mDataSet;
        this.original = mDataSet;
        this.filteredList = mDataSet;

    }

    //private final List<Tutor> tutorList;



    @Override
    public TutorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        TutorViewHolder tutorViewHolder = new TutorViewHolder(v);
        return tutorViewHolder;
    }

    @Override
    public void onBindViewHolder(TutorViewHolder holder, int position) {
        holder.name.setText(filteredList.get(position).getName());
        holder.desc.setText(filteredList.get(position).getDesc());
        holder.rating.setText(filteredList.get(position).getRating());
        holder.price.setText(filteredList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public class TutorViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView icon, name, desc, rating, price;

        TutorViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.browse_layout);
            icon = (TextView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            desc = (TextView) itemView.findViewById(R.id.desc);
            rating = (TextView) itemView.findViewById(R.id.rating);
            price = (TextView) itemView.findViewById(R.id.price);
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

                filteredList = (ArrayList<Tutor>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }


            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<Tutor> FilteredArrList = new ArrayList<Tutor>();

                if (original == null) {
                    original = new ArrayList<Tutor>(mDataSet); // saves the original data in mOriginalValues
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = original.size();
                    results.values = original;
                }
                else {
                    //
                    //filteredTutorList = getFilteredResults(constraint.toString().toLowerCase() );
                    //ArrayList<Tutor> filteredTList = new ArrayList<Tutor>() ;
                    boolean none = false;
                    String splits[] = constraint.toString().toLowerCase().split("-");
                    String priceStr = splits[0];
                    String ratingStr = splits[1];
                    String courseStr = splits[2];
                    int status = Integer.parseInt(splits[3]);

                    if (courseStr.equals("none")) {
                        none = true;
                        courseStr = " ";
                    }
                    Log.d("none", courseStr);
                    int priceNum = Integer.parseInt(priceStr.split("_")[1]);
                    int ratingNum = Integer.parseInt(ratingStr.split("_")[1]);




                    //String dfi[] = {"f","f"};
                    //Tutor tutor1 = new Tutor("123456", "Person One", "Grad student studying Memes",dfi, "4.1", 0, "35");
                    //FilteredArrList.add(tutor1);
                    //
                    for (Tutor item : mDataSet) {

                        if ((Integer.parseInt(item.getPrice()) <= priceNum) && (Float.parseFloat(item.getRating()) >= ratingNum) && (item.getStatus() >= status)) {
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
                    /**
                    //}
                    // if (found)
                    //{ results.add(item);}
                    // }
                    //}
                     **/
                    results.values = FilteredArrList;
                    results.count = FilteredArrList.size();
                }


                //filteredList.addAll((ArrayList<Tutor> ) results.values);
                return results;
            }
        };
        return filter;

    /**
    protected ArrayList<Tutor> getFilteredResults(String constraint) {
        ArrayList<Tutor> results = new ArrayList<>();
        boolean found = false;
        String splits[] = constraint.split("-");
        String priceStr = splits[0];
        String ratingStr = splits[1];
        String courseStr = splits[2];

        int priceNum = Integer.parseInt(priceStr.split(".")[1]);
        int ratingNum = Integer.parseInt(ratingStr.split(".")[1]);

        for (Tutor item : mDataSet) {
           // if ((Integer.parseInt(item.getPrice()) <= priceNum) && (Integer.parseInt(item.getRating()) >= ratingNum)) {
               // for (int i = 0; i < item.getCourses().length; i++ ) {
                //    if (item.getCourses()[i].contains(courseStr)) {
                        results.add(item);
                       // found = true;
                    }
                //}
               // if (found)
                //{ results.add(item);}
           // }
        //}
        //results
        return results;
    }
**/
}
}

