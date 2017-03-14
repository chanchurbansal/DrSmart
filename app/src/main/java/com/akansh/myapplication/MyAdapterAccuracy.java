package com.akansh.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapterAccuracy extends ArrayAdapter<Diseases> implements View.OnClickListener{

	private ArrayList<Diseases> dataSet;
	public Context mContext;
    public FragmentManager manager;
	public  String message1;
	// View lookup cache
	private static class ViewHolder {
		CircleDisplay accuracy;
		TextView diseaseName;
		CardView container;
	}

	public MyAdapterAccuracy(ArrayList<Diseases> modelsArrayList, Context context, FragmentManager manager) {
		super(context, R.layout.target_item_disease, modelsArrayList);
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

		Fragment pre=new prediction();
		Bundle args=new Bundle();
		args.putString("diseaseName", dataSet.get(position).getDiseaseName());
		args.putString("diseaseFileName", dataSet.get(position).getDiseaseFileName());

		pre.setArguments(args);

		manager.beginTransaction().replace(R.id.content_frame,
				pre,"PREV").commit();




	}

	private int lastPosition = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Diseases dataModel = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag

		final View result;

		if (convertView == null) {

			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.target_item_disease, parent, false);

			viewHolder.accuracy = (CircleDisplay) convertView.findViewById(R.id.circleDisplay);
			viewHolder.diseaseName = (TextView) convertView.findViewById(R.id.diseaseName);
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



		//viewHolder.accuracy.setText(dataModel.getTitle());
		viewHolder.diseaseName.setText(dataModel.getDiseaseName());
		viewHolder.container.setOnClickListener(this);

		viewHolder.accuracy.setColor(Color.alpha(R.color.bg_main));
		viewHolder.accuracy.setAnimDuration(1000);
		viewHolder.accuracy.setValueWidthPercent(55f);
		viewHolder.accuracy.setTextSize(15f);
		viewHolder.accuracy.setDrawText(true);
		viewHolder.accuracy.setDrawInnerCircle(true);
		viewHolder.accuracy.setFormatDigits(1);
		viewHolder.accuracy.setTouchEnabled(false);
		viewHolder.accuracy.setUnit("%");
		viewHolder.accuracy.setStepSize(0.5f);
		viewHolder.accuracy.showValue(dataModel.getAccuracy(), 100f, true);
		//String x[]={"Accuracy:",""+dataModel.getAccuracy()};
		//viewHolder.accuracy.setCustomText(x);

        viewHolder.container.setTag(position);

		// Return the completed view to render on screen
		return convertView;
	}
}