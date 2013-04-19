package com.example.listdemoproject;

import java.util.ArrayList;
import java.util.HashSet;

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

enum ItemViewType {
	ITEM_VIEW_HEADER (0),
	ITEM_VIEW_WITH_CHECKBOX (1);
	
	private final int id; 
	
	private ItemViewType(int id) {
		this.id = id;
	}
	
	public int getValue() { return id; }
}

abstract class ItemBase {
	
	protected Context mContext;
	
	public ItemBase(Context context) {
		mContext = context;
	}
	
	public abstract View createView(View convertView, ViewGroup parent);
	public abstract void onClick();
	public abstract boolean isEnabled();
	public abstract int getItemViewType();
}

class ItemHeader extends ItemBase {
 
	public String caption;
	
	public ItemHeader(Context context, String caption) {
		super(context);
		this.caption = caption;
	}

	@Override
	public View createView(View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_header_item, null);
		}

		((TextView) v.findViewById(R.id.list_header_item)).setText(caption);
		return v;		
	}

	@Override
	public void onClick() {		
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public int getItemViewType() {
		return ItemViewType.ITEM_VIEW_HEADER.getValue();
	}
	
}

class ItemWithCheckBox extends ItemBase {
	
	public ItemWithCheckBox(Context context, String title, String caption, boolean isChecked) {
		super(context);
		this.title = title;
		this.caption = caption;
		this.isChecked = isChecked;
	}
	
	public String title;
	public String caption;
	public boolean isChecked;
	
	@Override
	public View createView(View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_twoline_item_checkbox, null);
		}

		((TextView) v.findViewById(R.id.list_twoline_caption)).setText(caption);
		((TextView) v.findViewById(R.id.list_twoline_title)).setText(title);
		((CheckBox) v.findViewById(R.id.checkBoxEnable)).setChecked(isChecked);
		
		return v;
	}

	@Override
	public void onClick() {
		isChecked = !isChecked;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public int getItemViewType() {
		return ItemViewType.ITEM_VIEW_WITH_CHECKBOX.getValue();
	}
}

class ItemAdapter extends ArrayAdapter<ItemBase> {

	private ArrayList<ItemBase> objects;
	
	public ItemAdapter(Context context, int textViewResourceId, ArrayList<ItemBase> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}	
	
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}
	
	@Override
	public int getCount() {
		return objects.size();
	}
	
	@Override
	public int getItemViewType(int position) {
		return objects.get(position).getItemViewType();
	}
	
	@Override
	public int getViewTypeCount() {
		HashSet<Integer> typeSet = new HashSet<Integer>();
		for (ItemBase item : objects) {
			typeSet.add(item.getItemViewType());
		}
		return typeSet.size();
	}
	
	@Override
	public boolean isEnabled(int position) {
		return objects.get(position).isEnabled();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return objects.get(position).createView(convertView, parent);
	}
}

public class ListProjectActivity extends ListActivity {

	private ItemAdapter mAdapter;
	private ArrayList<ItemBase> mItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_project);
				
		mItems = new ArrayList<ItemBase>();
		for (int i = 0; i < 20; i++) {
			if (i == 0 || i == 5 || i == 10) {
				mItems.add(new ItemHeader(this, String.valueOf(i)));
				continue;
			}
			mItems.add(new ItemWithCheckBox(this, String.valueOf(i), String.valueOf(i), (i % 2) == 0));
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
		ItemBase item = (ItemBase) l.getItemAtPosition(position);
		item.onClick();
		
		l.getAdapter().getView(position, v, l);		
	}

}
