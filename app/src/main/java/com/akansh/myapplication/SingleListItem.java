package com.akansh.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SingleListItem extends Fragment {
    public LinearLayout listv;
    public LinearLayout listv1;
    public ArrayAdapter<String> adapter,adapter1;
    public  int position;
    public String[] adobe_products,food_servings,final_string;
    public ImageView im;
    public String uri;
    public Drawable res;
    public int imageResource;
    public Toolbar toolbar;
    Bundle bun;
    public RelativeLayout.LayoutParams p;

    @Override
    public View onCreateView (final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.content_main_diet, container, false);

        ((MainActivity)getActivity()).isDietChart=true;
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle b)
    {

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" hello ");
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);


        listv = (LinearLayout) view.findViewById(R.id.listView);
        listv1 = (LinearLayout) view.findViewById(R.id.listView1);
        ImageSwitcher im=(ImageSwitcher) getActivity().findViewById(R.id.backdrop);
        im.setImageDrawable(null);

        position = getArguments().getInt("pos");
         uri = "@drawable/myresource";  // where myresource (without the extension) is the file
        //initCollapsingToolbar();


        if(position==0){ adobe_products = getResources().getStringArray(R.array.food_info_types_beans);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_beans);
            uri = "@drawable/beans";
            collapsingToolbar.setTitle("Beans");
        }
        else if(position==1){ adobe_products = getResources().getStringArray(R.array.food_info_types_berries);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_berries);
            uri = "@drawable/berries";
            collapsingToolbar.setTitle("Berries");
        }
        else if(position==2){ adobe_products = getResources().getStringArray(R.array.food_info_types_other_fruits);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_other_fruits);
            uri = "@drawable/other_fruits";
            collapsingToolbar.setTitle("Other Fruits");}
        else if(position==3){ adobe_products = getResources().getStringArray(R.array.food_info_types_cruciferous_vegetables);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_cruciferous_vegetables);
            uri = "@drawable/cruciferous_vegetables";
            collapsingToolbar.setTitle("Cruciferous Vegetables");}
        else if(position==4){ adobe_products = getResources().getStringArray(R.array.food_info_types_greens);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_greens);
            uri = "@drawable/greens";
            collapsingToolbar.setTitle("Greens");}
        else if(position==5){ adobe_products = getResources().getStringArray(R.array.food_info_types_other_vegetables);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_other_vegetables);
            uri = "@drawable/other_vegetables";
            collapsingToolbar.setTitle("Other Vegetables");}
        else if(position==6){ adobe_products = getResources().getStringArray(R.array.food_info_types_flaxseeds);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_flaxseeds);
            uri = "@drawable/flaxseeds";
            collapsingToolbar.setTitle("Flax Seeds");}
        else if(position==7){ adobe_products = getResources().getStringArray(R.array.food_info_types_nuts);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_nuts);
            uri = "@drawable/nuts";
            collapsingToolbar.setTitle("Nuts");}
        else if(position==8){ adobe_products = getResources().getStringArray(R.array.food_info_types_spices);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_spices);
            uri = "@drawable/spices";
            collapsingToolbar.setTitle("Spices");}
        else if(position==9){ adobe_products = getResources().getStringArray(R.array.food_info_types_whole_grains);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_whole_grains);
            uri = "@drawable/whole_grains";
            collapsingToolbar.setTitle("Whole Grains");}
        else if(position==10){ adobe_products = getResources().getStringArray(R.array.food_info_types_beverages);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_beverages);
            uri = "@drawable/beverages";
            collapsingToolbar.setTitle("Beverages");}
        else if(position==11){ adobe_products = getResources().getStringArray(R.array.food_info_types_exercise);
            food_servings = getResources().getStringArray(R.array.food_info_serving_sizes_exercise);
            uri = "@drawable/exercise";
            collapsingToolbar.setTitle("Exercises");}

        imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
        res = getResources().getDrawable(imageResource);
        im.setImageDrawable(res);


        Log.i("I","size : "+position);
        adapter1 = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item, food_servings);


        int adapterCount = adapter1.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = adapter1.getView(i, null, null);
            listv1.addView(item);
        }



         adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item, adobe_products);
        adapterCount = adapter.getCount();
        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            listv.addView(item);
        }




    }


}