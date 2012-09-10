package com.biermacht.brews;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IngredientArrayAdapter extends ArrayAdapter<Ingredient> {
	
	private Context context;
	private List<Ingredient> list;
	
	public IngredientArrayAdapter(Context c, List<Ingredient> list)
	{
		super(c, android.R.layout.simple_list_item_1, list);
		this.context = c;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Get inflater
		LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		// View to return
		View row = inflater.inflate(R.layout.ingredient_row_layout, parent, false);
		TextView labelView = (TextView) row.findViewById(R.id.label);
		TextView amountView = (TextView) row.findViewById(R.id.amount);
		ImageView imageView = (ImageView) row.findViewById(R.id.ingredient_row_icon);
		labelView.setText(list.get(position).getName() + " " + list.get(position).getType());
		amountView.setText(list.get(position).getAmount() + " " + list.get(position).getUnit());
		
		// Set imageView based on ingredient type
		String ingType = list.get(position).getType();
		
		if(ingType == Ingredient.TYPE_HOPS)
			imageView.setImageResource(R.drawable.icon_hops);
		else if(ingType == Ingredient.TYPE_MALT)
			imageView.setImageResource(R.drawable.icon_idk);
		else if(ingType == Ingredient.TYPE_SPICE)
			imageView.setImageResource(R.drawable.icon_idk);
		else if (ingType == Ingredient.TYPE_YEAST)
			imageView.setImageResource(R.drawable.icon_idk);
		else
			imageView.setImageResource(R.drawable.icon_idk);
		
		return row;
	}
}
