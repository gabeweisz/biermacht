package com.biermacht.brews.frontend.fragments;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.frontend.adapters.InstructionArrayAdapter;
import com.biermacht.brews.recipe.Instruction;
import com.biermacht.brews.recipe.Recipe;
import android.content.*;

public class InstructionViewFragment extends Fragment {

	private int resource;
	private Recipe r;
	private OnItemClickListener mClickListener;
	private ListView instructionListView;
	private ArrayList<Instruction> instructionList;
	private LinearLayout timerView;
	View pageView;
	Context c;
	
	public InstructionViewFragment(Context c, Recipe r)
	{
		this.resource = R.layout.fragment_instruction_view;
		this.r = r;
		this.c = c;
	}
	
	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		pageView = inflater.inflate(resource, container, false);
		
		setHasOptionsMenu(true);
		
		// Initialize important junk
		instructionListView = (ListView) pageView.findViewById(R.id.instruction_list);
		timerView = (LinearLayout) pageView.findViewById(R.id.timer_view);
		instructionList = r.getInstructionList();
  
		if (instructionList.size() > 0)
		{
			InstructionArrayAdapter instructionArrayAdapter = new InstructionArrayAdapter(c, instructionList);
			instructionListView = (ListView) pageView.findViewById(R.id.instruction_list);
			instructionListView.setAdapter(instructionArrayAdapter);
			instructionListView.setVisibility(View.VISIBLE);
		}
		else
		{
			TextView noInstructionsView = (TextView) pageView.findViewById(R.id.no_instructions_view);
			noInstructionsView.setVisibility(View.VISIBLE);
		}
		
		return pageView;
	}
	
	public void toggleTimer()
	{
		if (timerView.getVisibility() == View.VISIBLE)
			timerView.setVisibility(View.GONE);
		else
			timerView.setVisibility(View.VISIBLE);
			
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
			inflater.inflate(R.menu.fragment_instruction_menu, menu);
	}
	
}
