package com.inc.sk.modules;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.inc.sk.R;
import com.inc.sk.imgload.ImageLoader;
import com.inc.sk.utils.BitmapUtil;
import com.inc.sk.utils.SystemBarUtil;
import com.inc.sk.widgets.WaterfallView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar = null;
    private DrawerLayout mDrawer = null;
    private WaterfallView mWaterfall = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        initStatBar();
        setContentView(R.layout.activity_main);
        initAppBar();
        initDrawer();
        initWaterfall();
        initNavView();
    }

    private void initStatBar(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //获取样式中的属性值
            TypedValue typedValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
            int[] attribute = new int[] { android.R.attr.colorPrimary };
            TypedArray array = this.obtainStyledAttributes(typedValue.resourceId, attribute);
            int color = array.getColor(0, Color.TRANSPARENT);
            array.recycle();
            window.setStatusBarColor(color);
        }else {
            SystemBarUtil.setSystemBartint(this, R.color.cloorSystemBar);
        }

    }

    //need repair
    public void initNavView(){
        View v = LayoutInflater.from(this).inflate(R.layout.nav_header_main,null);
        LinearLayout header = (LinearLayout)v.findViewById(R.id.nav_header);
        ImageLoader loader = ImageLoader.newInstance(this);
        String url = getResources().getString(R.string.starking_url);
        Bitmap bm = loader.getFromMemCache(url);
        if(bm == null){
             bm = BitmapUtil.decodeResource(getResources(),R.drawable.nav_header_img,
                    300,getResources().getDimensionPixelSize(R.dimen.nav_header_height));
            loader.putToMemCache(url,bm);
        }
        header.setBackground(getResources().getDrawable(R.drawable.nav_header_img));
    }

    public static final String PLAYER_NO = "player_id";

    private void initWaterfall() {
        mWaterfall= (WaterfallView)this.findViewById(R.id.waterfall_scroll_view);
        mWaterfall.setDatas(Image.mUrls);
        mWaterfall.setOnItemClickListener(new WaterfallView.OnItemClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra(PLAYER_NO,v.getId());
                startActivity(i);
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initDrawer(){
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    private void initAppBar(){
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        } else if(id == R.id.action_clean){

            mWaterfall.cleanDiskCache();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
