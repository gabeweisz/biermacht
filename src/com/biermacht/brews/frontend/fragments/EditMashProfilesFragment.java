package com.biermacht.brews.frontend.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biermacht.brews.R;
import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.frontend.EditCustomMashProfileActivity;
import com.biermacht.brews.frontend.adapters.MashProfileArrayAdapter;
import com.biermacht.brews.recipe.MashProfile;
import com.biermacht.brews.utils.Constants;
import com.biermacht.brews.utils.comparators.ToStringComparator;
import com.biermacht.brews.utils.interfaces.BiermachtFragment;

import java.util.ArrayList;
import java.util.Collections;

public class EditMashProfilesFragment extends Fragment implements BiermachtFragment {
  private static int resource = R.layout.fragment_view;
  private OnItemClickListener mClickListener;
  private ListView listView;
  private ArrayList<MashProfile> list;
  private DatabaseAPI databaseApi;
  MashProfileArrayAdapter arrayAdapter;
  View pageView;
  Context c;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    pageView = inflater.inflate(resource, container, false);

    setHasOptionsMenu(true);

    // Context
    c = getActivity();
    databaseApi = new DatabaseAPI(c);

    // Get ingredient list
    list = databaseApi.getMashProfiles(Constants.DATABASE_USER_RESOURCES);
    Collections.sort(list, new ToStringComparator());

    // Initialize list
    listView = (ListView) pageView.findViewById(R.id.listview);
    arrayAdapter = new MashProfileArrayAdapter(c, list);

    // Set up the onClickListener
    mClickListener = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parentView, View childView, int pos, long id) {
        Intent i = new Intent(c, EditCustomMashProfileActivity.class);
        i.putExtra(Constants.KEY_RECIPE_ID, Constants.MASTER_RECIPE_ID);
        i.putExtra(Constants.KEY_PROFILE_ID, list.get(pos).getId());
        i.putExtra(Constants.KEY_PROFILE, list.get(pos));
        startActivity(i);

      }
    };

    // Set whether or not we show the list view
    if (list.size() > 0) {
      listView.setVisibility(View.VISIBLE);
      listView.setAdapter(arrayAdapter);
      registerForContextMenu(listView);
      listView.setOnItemClickListener(mClickListener);
    }
    else {
      TextView noListView = (TextView) pageView.findViewById(R.id.nothing_to_show_view);
      noListView.setText("No mash profiles to display");
      noListView.setVisibility(View.VISIBLE);
    }

    return pageView;
  }

  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_mash_profiles_menu, menu);
  }

  //**************************************************************************
  // The following set of methods implement the Biermacht Fragment Interface
  //**************************************************************************
  @Override
  public void handleClick(View v) {

  }

  @Override
  public void update() {
    // Get the full list of profiles from the custom database.
    ArrayList<MashProfile> loadedList = databaseApi.getMashProfiles(Constants.DATABASE_USER_RESOURCES);

    // Add the loaded profiles to the list for the list view.
    list.removeAll(list);
    list.addAll(loadedList);

    // Sort the list.
    Collections.sort(list, new ToStringComparator());

    // Notify the adapter that the list has changed.
    arrayAdapter.notifyDataSetChanged();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return false;
  }

  @Override
  public String name() {
    return "Mash Profiles";
  }
}
