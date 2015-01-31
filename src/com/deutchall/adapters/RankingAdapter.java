package com.deutchall.adapters;

import java.util.List;

import com.deutchall.activities.R;
import com.deutchall.identification.Ranking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RankingAdapter extends ArrayAdapter<Ranking> {
	
	private final Context context;
    private final List<Ranking> ranking;
    
    public RankingAdapter(Context context, List<Ranking> ranking) {
        super(context, R.layout.rowrank, ranking);
        this.context = context;
        this.ranking = ranking;
    }
    
    @SuppressLint("ViewHolder")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.rowrank, parent, false);
    	
    	TextView txUser = (TextView) rowView.findViewById(R.id.txUser);
    	TextView txDate = (TextView) rowView.findViewById(R.id.txDate);
    	TextView txScore = (TextView) rowView.findViewById(R.id.txScore);
    	
    	txUser.setText(ranking.get(position).getName());
    	txDate.setText(ranking.get(position).getDate());
    	txScore.setText(String.valueOf(ranking.get(position).getScore()));
    	return rowView;
    }
}
