package com.akansh.myapplication;


import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.R.attr.onClick;
import static android.R.attr.targetActivity;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private Context mContext;
    private static FragmentActivity mActivity;
    private static ArrayList<UserInfo> mDataset;
    private static MyClickListener myClickListener;

    public MyRecyclerViewAdapter(Context context, ArrayList<UserInfo> userHistory,FragmentActivity mActivity) {
        this.mContext=context;
        this.mActivity=mActivity;
        this.mDataset = userHistory;
        Log.i("X","Size : "+userHistory.size());
    }


    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;
        TextView actual,predicted,position;
        TableLayout detailTable;
        private ViewGroup mContainerView;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            dateTime = (TextView) itemView.findViewById(R.id.textView2);
            actual = (TextView) itemView.findViewById(R.id.actual);
            predicted = (TextView) itemView.findViewById(R.id.predicted);
            detailTable = (TableLayout)itemView.findViewById((R.id.detailsTable));
            position = (TextView) itemView.findViewById((R.id.position));
            mContainerView = (ViewGroup) itemView.findViewById(R.id.itemlayout);

            Log.i(LOG_TAG, "Adding Listener");
            //detailTable.setVisibility(View.GONE);
            //detailTable.setEnabled(false);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(final View view) {

            boolean mIsViewExpanded;
            if (detailTable.getVisibility() == View.VISIBLE)
                mIsViewExpanded = true;
            else
                mIsViewExpanded = false;
            int originalHeight = 0, finalHeight = 0;
            if (originalHeight == 0) {
                view.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                originalHeight = view.getMeasuredHeight();
            }
            Log.i(LOG_TAG, " Original Height " + originalHeight);
            int h=0;
            // Declare a ValueAnimator object
            ValueAnimator valueAnimator;

            if (!mIsViewExpanded) {
                detailTable.setVisibility(View.VISIBLE);
                detailTable.setEnabled(true);
                detailTable.measure(0,0);
                h=detailTable.getMeasuredHeight();
                h +=30;
                finalHeight = h+originalHeight;
                mIsViewExpanded = true;
                Log.i(LOG_TAG, " Final Height " + finalHeight+" "+h);
                valueAnimator = ValueAnimator.ofInt(originalHeight,finalHeight ); // These values in this method can be changed to expand however much you like
            } else {
                mIsViewExpanded = false;
                detailTable.measure(0, TableLayout.LayoutParams.WRAP_CONTENT);
                h=detailTable.getMeasuredHeight();
                h+=30;
                //originalHeight=view.getHeight();
                Log.i(LOG_TAG, " Coll Height " + originalHeight+" "+h);


                valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight-h);
                //details.setVisibility(View.GONE);
                //details.setEnabled(false);

            Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

            a.setDuration(200);
            // Set a listener to the animation and configure onAnimationEnd
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    detailTable.setVisibility(View.GONE);
                    detailTable.setEnabled(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            // Set the animation on the custom view
            detailTable.startAnimation(a);
        }
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    view.getLayoutParams().height = value.intValue();
                    view.requestLayout();
                }
            });
            valueAnimator.start();

            if(mIsViewExpanded && actual.getText().toString().equals("Actual Value : NIL"))
            {
                Log.i(LOG_TAG, " here"+actual.getText());
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                int pos=Integer.parseInt(position.getText().toString());
                ArrayList<String>s = mDataset.get(pos).possibleValues;

                final String []res = s.toArray(new String[s.size()]);
                builder.setTitle("Please Enter actual Value")
                        .setItems(res, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                actual.setText("Actual Value : "+res[which]);
                                String diseaseName=label.getText().toString();
                                String ts=dateTime.getText().toString();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child(user.getUid()).child(ts+":"+diseaseName).child("actualVal").setValue(res[which]);
                                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame, new CardViewActivity());
                                ft.commit();
                            }
                        });
                AlertDialog a=builder.create();
                a.show();
            }
        }




    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {



        holder.label.setText(mDataset.get(position).diseaseName);
        holder.dateTime.setText(mDataset.get(position).timeStamp);
        holder.actual.setText("Actual Value : "+mDataset.get(position).actualVal);
        holder.predicted.setText("Predicted Value : "+mDataset.get(position).predVal);
        holder.position.setText(""+position);
        /*holder.details.setVisibility(View.GONE);
        holder.details.setEnabled(false);*/
        ArrayList<String> att_names=mDataset.get(position).att_name;
        ArrayList<String> att_values=mDataset.get(position).att_val;
        holder.detailTable.removeAllViewsInLayout();

        TextView[] textName = new TextView[att_names.size()];
        TableRow[] tr_head = new TableRow[att_names.size()];
        TextView[] textInput = new TextView[att_names.size()];
        TableRow.LayoutParams lp = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        holder.detailTable.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);


        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(

                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.8f
        );
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(

                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                0.2f
        );
        lp.setMargins(0, 0, 0, 0);
        lp1.setMargins(0, 0, 0, 0);
        lp2.setMargins(0, 0, 0, 0);
        //att_length=att_names.length;
        for(int i=0;i<att_names.size();i++) {
            //att_value[i]=att_value[i].substring(0,att_value[i].length()-1);
            tr_head[i] = new TableRow(mContext.getApplicationContext());
            textName[i] = new TextView(mContext.getApplicationContext());
            textInput[i] = new TextView(mContext.getApplicationContext());

            textName[i].setLayoutParams(lp1);
            textInput[i].setLayoutParams(lp2);


            textName[i].setTextColor(Color.GRAY);

            textName[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
            textName[i].setGravity(Gravity.LEFT);
            textName[i].setMaxLines(1);
            textName[i].setText(att_names.get(i));


            textInput[i].setTextColor(Color.GRAY);
            textInput[i].setLayoutParams(lp2);
            textInput[i].setImeOptions(EditorInfo.IME_ACTION_NEXT);
            textInput[i].setGravity(Gravity.RIGHT);
            textInput[i].setText(att_values.get(i));



            tr_head[i].setLayoutParams(lp);
            tr_head[i].addView(textName[i]);
            tr_head[i].addView(textInput[i]);
            tr_head[i].setGravity(Gravity.CENTER);
            holder.detailTable.addView(tr_head[i]);

        }
        holder.detailTable.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //holder.detailTable.setVisibility(View.GONE);
        //holder.detailTable.setEnabled(false);
        Log.i(LOG_TAG, " Card Height " + holder.detailTable.getMeasuredHeight());

    }

    public void addItem(UserInfo dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}