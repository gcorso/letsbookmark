package com.gcappslab.gcorso.letsbookmark.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.gcappslab.gcorso.letsbookmark.ExpandableListAdapter;
import com.gcappslab.gcorso.letsbookmark.R;
import com.gcappslab.gcorso.letsbookmark.Site;
import com.gcappslab.gcorso.letsbookmark.SiteListDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SuggestionsActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    HashMap<String, List<Site>> listDataChild;
    private SiteListDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        databaseHelper = new SiteListDatabaseHelper(this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Site varSite = (Site) listAdapter.getChild(groupPosition, childPosition);
                databaseHelper.saveSite(varSite);
                Toast.makeText(SuggestionsActivity.this, "Bookmark added to the Homepage", Toast.LENGTH_SHORT).show();
                return true;
            }

        });

    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataChild = new HashMap<String, List<Site>>();

        // Adding child data
        List<Site> news = new ArrayList<Site>();
        news.add(new Site("CNN", "http://www.cnn.com/", "http://gcorso.000webhostapp.com/suggestions/cnn.png"));
        news.add(new Site("NYTimes", "http://www.nytimes.com/", "http://gcorso.000webhostapp.com/suggestions/nytimes.png"));
        news.add(new Site("NBC News", "http://www.nbcnews.com/", "http://gcorso.000webhostapp.com/suggestions/nbcnews.png"));
        news.add(new Site("Washington Post", "http://www.washingtonpost.com/", "http://gcorso.000webhostapp.com/suggestions/washingtonpost.png"));
        news.add(new Site("The Guardian", "http://www.theguardian.com/", "http://gcorso.000webhostapp.com/suggestions/theguardian.png"));
        news.add(new Site("Wall Street Journal", "http://www.wsj.com/", "http://gcorso.000webhostapp.com/suggestions/wallstreetjournal.png"));
        news.add(new Site("ABC News", "http://www.abcnews.go.com/", "http://gcorso.000webhostapp.com/suggestions/abcnews.png"));
        news.add(new Site("BBC News", "http://www.bbc.co.uk/", "http://gcorso.000webhostapp.com/suggestions/bbcnews.png"));
        news.add(new Site("USA Today", "http://www.usatoday.com/", "http://gcorso.000webhostapp.com/suggestions/usatoday.png"));

        List<Site> sport = new ArrayList<Site>();
        sport.add(new Site("ESPN", "http://www.espn.com/", "http://gcorso.000webhostapp.com/suggestions/espn.png"));
        sport.add(new Site("Bleacher Report", "http://www.bleacherreport.com/", "http://gcorso.000webhostapp.com/suggestions/bleacherreport.png"));
        sport.add(new Site("CBS Sports", "http://www.cbssports.com/", "http://gcorso.000webhostapp.com/suggestions/cbssports.png"));
        sport.add(new Site("Sport Illustrated", "http://www.si.com/", "http://gcorso.000webhostapp.com/suggestions/sportillustrated.png"));
        sport.add(new Site("NBA", "http://www.nba.com/", "http://gcorso.000webhostapp.com/suggestions/nba.png"));
        sport.add(new Site("NFL", "http://www.nfl.com/", "http://gcorso.000webhostapp.com/suggestions/nfl.png"));

        List<Site> social = new ArrayList<Site>();
        social.add(new Site("Facebook", "http://www.facebook.com/", "http://gcorso.000webhostapp.com/suggestions/facebook.png"));
        social.add(new Site("Twitter", "http://www.twitter.com/", "http://gcorso.000webhostapp.com/suggestions/twitter.png"));
        social.add(new Site("Linkedin", "http://www.linkedin.com/", "http://gcorso.000webhostapp.com/suggestions/linkedin.png"));
        social.add(new Site("Instagram", "http://www.instagram.com/", "http://gcorso.000webhostapp.com/suggestions/instagram.png"));
        social.add(new Site("Google+", "http://www.plus.google.com/", "http://gcorso.000webhostapp.com/suggestions/googleplus.png"));

        List<Site> other = new ArrayList<Site>();
        other.add(new Site("Amazon", "http://www.amazon.com/", "http://gcorso.000webhostapp.com/suggestions/amazon.png"));
        other.add(new Site("EBay", "http://www.ebay.com/", "http://gcorso.000webhostapp.com/suggestions/ebay.png"));
        other.add(new Site("Weather", "http://www.weather.com/", "http://gcorso.000webhostapp.com/suggestions/weather.png"));
        other.add(new Site("GC Apps Lab", "http://gcorso.000webhostapp.com/", "http://gcorso.000webhostapp.com/suggestions/gc.png"));
        other.add(new Site("Google", "http://www.google.com/", "http://gcorso.000webhostapp.com/suggestions/google.png"));

        listDataChild.put("News", news); // Header, Child data
        listDataChild.put("Sport", sport);
        listDataChild.put("Social Networks", social);
        listDataChild.put("Other", other);
    }
}