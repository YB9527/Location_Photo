package com.xp;


import android.net.Uri;
import android.os.Bundle;

import com.baidu.mapapi.map.MapView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xp.zjd.ZJDFragment;
import com.xp.common.AndroidTool;
import com.xp.map.MapFragment;
import com.xp.menu.SetCurrentXZQYFragment;
import com.xp.menu.SetingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FrameLayout frameLayout = null;
    private MapView mMapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       // drawer.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        init();

    }


    private void init() {

        frameLayout = (FrameLayout) findViewById(R.id.framelayout_content);
        InitFragment initFragment = new InitFragment();

        ///将页面注入到 AndroidTool
        AndroidTool.setMainActivity(this);
        AndroidTool.replaceFrameLayout(initFragment);



    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

    /**
     *主要（右边） menu点击事件
     * @param item 当前点击的item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case  R.id.set_current_xzqy:
                //设置行政区域
                AndroidTool.replaceFrameLayout(new SetCurrentXZQYFragment());
                break;
            case  R.id.action_settings:
                //软件设置
                AndroidTool.replaceFrameLayout(new SetingsFragment());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.dktable_item:
                //查看地块
               AndroidTool.replaceFrameLayout(new ZJDFragment());
                break;
            case  R.id.cbd_jtcy_item:
                //回到首页
                AndroidTool.replaceFrameLayout(new InitFragment());
                break;
            case  R.id.map_item:
                AndroidTool.replaceFrameLayout(new MapFragment());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private Button btn_camera;
    private ImageView imageView;

    private Button choose_photo;

    public static final int PICK_PHOTO = 102;


    public static final int TAKE_CAMERA = 101;
    private Uri imageUri;
    public void aa(View view) {

    }



}
