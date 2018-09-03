package com.gcappslab.gcorso.letsbookmark.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gcappslab.gcorso.letsbookmark.R;

public class AboutActivity extends ActionBarActivity implements View.OnClickListener{

    private Button button_facebook;
    private Button button_twitter;
    private Button button_google;
    private ImageView logogc;
    private Button encryptstudio;
    private Button quadros;
    private Button clashofstars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.button_facebook = (Button) this.findViewById(R.id.button_facebook);
        this.button_facebook.setOnClickListener(this);

        this.button_google = (Button) this.findViewById(R.id.button_google);
        this.button_google.setOnClickListener(this);

        this.button_twitter = (Button) this.findViewById(R.id.button_twitter);
        this.button_twitter.setOnClickListener(this);

        this.logogc = (ImageView) this.findViewById(R.id.logogcIV);
        this.logogc.setOnClickListener(this);

        this.encryptstudio = (Button) this.findViewById(R.id.button_encryptstudio);
        this.encryptstudio.setOnClickListener(this);

        this.quadros = (Button) this.findViewById(R.id.button_quadros);
        this.quadros.setOnClickListener(this);

        this.clashofstars = (Button) this.findViewById(R.id.button_clashofstars);
        this.clashofstars.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_facebook){
            Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/gcappslab"));
            startActivity(openBookmark);

        } else if (v.getId() == R.id.button_twitter){
            Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/gcappslab"));
            startActivity(openBookmark);

        } else if (v.getId() == R.id.button_google){
            Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/101076442224640513108/posts"));
            startActivity(openBookmark);

        } else if (v.getId() == R.id.logogcIV){
            Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("http://gcappslab.com/"));
            startActivity(openBookmark);

        } else if (v.getId() == R.id.button_encryptstudio){
            Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gcappslab.encryptstudio"));
            startActivity(openBookmark);

        } else if (v.getId() == R.id.button_quadros){
            Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.flappyspace.quadros"));
            startActivity(openBookmark);

        } else if (v.getId() == R.id.button_clashofstars){
            Intent openBookmark = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.flappyspace.game"));
            startActivity(openBookmark);

        }
    }

}
