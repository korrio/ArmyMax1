package com.ame.armymax.adapter;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataSearch;



public class ExampleAdapter extends CursorAdapter {
	 
    private List<DataSearch> items;
 
    private TextView text;
 
    public ExampleAdapter(Context context, Cursor cursor, List<DataSearch> items) {
 
        super(context, cursor, false);
 
        this.items = items;
 
    }
 
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
 
    	DataSearch data = (DataSearch) items.get(cursor.getPosition());
        text.setText(data.getUserName());
 
     
 
    }
 
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
 
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
        View view = inflater.inflate(R.layout.activity_search_result, parent, false);
 
        text = (TextView) view.findViewById(R.id.txtQuery);
 
        return view;
 
    }
 
}
