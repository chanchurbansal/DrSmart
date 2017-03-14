package com.akansh.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter extends ArrayAdapter<Model> implements View.OnClickListener{

	private ArrayList<Model> dataSet;
	public Context mContext;
    public FragmentManager manager;
	public String LOG = "com.vogella.testapp";
	public  String message1;
	// View lookup cache
	private static class ViewHolder {
		ImageView imgView ;
		TextView titleView ;
		TextView counterView ;
		CardView container;
	}

	public MyAdapter(ArrayList<Model> modelsArrayList, Context context,FragmentManager manager) {
		super(context, R.layout.target_item, modelsArrayList);
		this.dataSet = modelsArrayList;
		this.mContext=context;
        this.manager=manager;

	}


	@Override
	public void onClick(View v) {


		int position=(Integer) v.getTag();
		/*Object object= getItem(position);
		Model dataModel=(Model)object;
		Intent intent = new Intent(mContext, SingleListItem.class);
		Log.i(LOG,"av"+position);
		intent.putExtra("pos", position);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);*/



        Fragment pre=new SingleListItem();
        Bundle args=new Bundle();
        args.putInt("pos", position);


        pre.setArguments(args);
        //((MainActivity)mContext.
        manager.beginTransaction().replace(R.id.content_frame,
                pre,"PREV").commit();


	}

	private int lastPosition = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Model dataModel = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag

		final View result;

		if (convertView == null) {

			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.target_item, parent, false);
			viewHolder.imgView = (ImageView) convertView.findViewById(R.id.item_icon);
			viewHolder.titleView = (TextView) convertView.findViewById(R.id.item_title);
			viewHolder.counterView = (TextView) convertView.findViewById(R.id.item_counter);
			viewHolder.container= (CardView) convertView.findViewById(R.id.itemContainer);
			result=convertView;

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
			result=convertView;
		}

		//Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
		//result.startAnimation(animation);
		lastPosition = position;

		viewHolder.imgView.setImageResource(dataModel.getIcon());

		viewHolder.titleView.setText(dataModel.getTitle());
		viewHolder.counterView.setText(dataModel.getCounter());
		viewHolder.container.setOnClickListener(this);
		viewHolder.titleView.setTag(position);

        viewHolder.container.setTag(position);

		// Return the completed view to render on screen
		return convertView;
	}
}