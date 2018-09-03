package com.gcappslab.gcorso.letsbookmark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alessandro.c on 04/10/2015.
 */
public class SiteBaseAdapter extends BaseAdapter {

    public SiteBaseAdapter (){}

    private List<Site> siteList = new ArrayList<Site>();


    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return siteList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return siteList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param view The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view==null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.listview, parent, false);
        }

        Context context = parent.getContext();

        Site varSite = siteList.get(position);

        TextView nameTextView = (TextView) view.findViewById(R.id.tv_list_name);
        nameTextView.setText(varSite.getName());

        TextView URLTextView = (TextView) view.findViewById(R.id.tv_list_location);
        URLTextView.setText(varSite.getURL());

        ImageView imageImageView = (ImageView) view.findViewById(R.id.tv_list_image);
        String image = varSite.getPhotoRes();
        if (image.equals("folder")) {
            imageImageView.setImageResource(R.drawable.folder);
        } else if (image.equals("nothing")) {imageImageView.setImageResource(R.drawable.cnn);
        } else { Picasso.with(context).load(image).error(R.drawable.cnn).into(imageImageView);}

        return view;


    }

    public void setList(List<Site> folderlist) {
        siteList = folderlist;
    }


    public void addSite(Site varsite) {
        siteList.add(varsite);
    }


    public void remove(Object item) {
    }
}
