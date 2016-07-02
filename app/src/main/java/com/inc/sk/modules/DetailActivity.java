package com.inc.sk.modules;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.inc.sk.R;
import com.inc.sk.utils.BitmapUtil;


public class DetailActivity extends AppCompatActivity {

    private Toolbar mToolbar = null;

    private int mId = 0;

    public static final String FAVO_RESPONSE = "You Point a Great for Her";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initAppBar();
    }



    private void initAppBar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("player "+getIntent().getIntExtra(MainActivity.PLAYER_NO,-1));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_back_24_24);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("-->>finish");
                finish();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, FAVO_RESPONSE, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int rw = dm.widthPixels;
        int rh = getResources().getDimensionPixelOffset(R.dimen.app_bar_height);
        Bitmap bm = BitmapUtil.decodeResource(getResources(),R.drawable.collapsing_toolbar_bg,rw,rh);
        toolbarLayout.setBackground(new BitmapDrawable(bm));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
