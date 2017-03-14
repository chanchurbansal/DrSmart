package com.akansh.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;

import java.util.ArrayList;

public class MainDiet extends Fragment {
    
	public MyAdapter adapter;
	public  ListView listView;
	public Intent intent;
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_main_diet, container, false);
        ((MainActivity)getActivity()).enableDietHome(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Diet Chart");
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        ImageSwitcher im=(ImageSwitcher) getActivity().findViewById(R.id.backdrop);
        int imageResource = getResources().getIdentifier("@drawable/exercise", null, getActivity().getPackageName());
        Drawable res = getResources().getDrawable(imageResource);
        im.setImageDrawable(res);
		return view;
	}
	public void onViewCreated (View view1, Bundle savedInstanceState) {

		super.onViewCreated(view1, savedInstanceState);
		// if extending Activity
//		setContentView(R.layout.activity_main_diet);
		
		// 1. pass context and data to the custom adapter
		adapter = new MyAdapter(generateData(),getActivity().getApplicationContext(),getFragmentManager() );
		
		// if extending Activity 2. Get ListView from activity_main_diet.xmlt.xml
		listView = (ListView) getActivity().findViewById(R.id.listview);
		
		// 3. setListAdapter
		listView.setAdapter(adapter);// if extending Activity
		//getActivity().setListAdapter(adapter);
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            view = adapter.getView(i, null, listView);
            //if (i == 0)
              //  view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, TableRow.LayoutParams.WRAP_CONTENT));

            //view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            view.measure(0,0);
            totalHeight += view.getMeasuredHeight();
            totalHeight+=20;
            Log.d("W", "Height : " + totalHeight);
        }Log.d("W", "Divider Height : " + listView.getDividerHeight());

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);


		//ListView lv = getListView();

		// listening to single list item on click



	}
	
	
	private ArrayList<Model> generateData(){
		ArrayList<Model> models = new ArrayList<Model>();
	    //models.add(new Model("Group Title"));
		String[] food_id_names = getResources().getStringArray(R.array.food_id_names);
		String[] food_quant = getResources().getStringArray(R.array.food_quantities);
		int[] myImageList = new int[]{R.drawable.ic_beans, R.drawable.ic_berries,R.drawable.ic_other_fruits,
				R.drawable.ic_cruciferous_vegetables,R.drawable.ic_greens,R.drawable.ic_other_vegetables,
				R.drawable.ic_flaxseeds,R.drawable.ic_nuts,R.drawable.ic_spices,R.drawable.ic_whole_grains,
				R.drawable.ic_beverages,R.drawable.ic_exercise};

		for(int i=0;i<food_id_names.length;i++){
	    models.add(new Model(myImageList[i],food_id_names[i],food_quant[i]));
		}

	    
	    return models;
	}



} 
