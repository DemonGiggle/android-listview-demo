package com.example.listdemoproject;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

class Item {
	
	public Item(String title, String caption, boolean isChecked) {
		this.title = title;
		this.caption = caption;
		this.isChecked = isChecked;
	}
	
	public String title;
	public String caption;
	public boolean isChecked;
}

class ItemAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> objects;
	
	public ItemAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}	
	
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
	
	@Override
	public int getCount() {
		return objects.size();
	}
	
	@Override
	public boolean isEnabled(int position) {
		return true;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_twoline_item_checkbox, null);
		}
		
		Item item = objects.get(position);
		((TextView) v.findViewById(R.id.list_twoline_caption)).setText(item.caption);
		((TextView) v.findViewById(R.id.list_twoline_title)).setText(item.title);
		((CheckBox) v.findViewById(R.id.checkBoxEnable)).setChecked(item.isChecked);
		
		return v;
	}
}

public class ListProjectActivity extends ListActivity {

	private ItemAdapter mAdapter;
	private ArrayList<Item> mItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_project);
		
		mItems = new ArrayList<Item>();
		for (int i = 0; i < 20; i++) {
			mItems.add(new Item(String.valueOf(i), String.valueOf(i), (i % 2) == 0));
		}
		
		mAdapter = new ItemAdapter(this, R.layout.list_twoline_item_checkbox, mItems);
		
		setListAdapter(mAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_project, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {		
		Item item = (Item) l.getItemAtPosition(position);
		item.isChecked = !item.isChecked;
		
		l.getAdapter().getView(position, v, l);		
		Toast.makeText(this, "Click: " + item.caption, Toast.LENGTH_SHORT).show();
	}

}
