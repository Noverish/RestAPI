package com.noverish.restapi.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookFragment;
import com.noverish.restapi.kakao.KakaoFragment;
import com.noverish.restapi.other.BaseFragment;
import com.noverish.restapi.other.Essentials;
import com.noverish.restapi.twitter.TwitterFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BaseFragment nowFragment;
    private Toolbar toolbar;
    private FrameLayout main, level1, level2, level3;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                makePostPopup();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        main = (FrameLayout) findViewById(R.id.content_main_fragment_layout);
        level1 = (FrameLayout) findViewById(R.id.content_main_fragment_level_1);
        level2 = (FrameLayout) findViewById(R.id.content_main_fragment_level_2);
        level3 = (FrameLayout) findViewById(R.id.content_main_fragment_level_3);

        Essentials.changeFragment(this, R.id.activity_main_splash_fragment_layout, new SplashFragment());

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setOnFirstLoadFinishedRunnable(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.findViewById(R.id.activity_main_splash_fragment_layout).setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
        nowFragment = homeFragment;
        Essentials.changeFragment(this, R.id.content_main_fragment_layout, homeFragment);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(main.getVisibility() == View.INVISIBLE) {
            main.setVisibility(View.VISIBLE);
            level1.setVisibility(View.VISIBLE);
            level2.setVisibility(View.VISIBLE);
            level3.setVisibility(View.VISIBLE);
        } else if(level3.getChildCount() > 0) {
            level3.removeAllViews();
        } else if(level2.getChildCount() > 0) {
            level2.removeAllViews();
        } else if(level1.getChildCount() > 0) {
            level1.removeAllViews();
        } else if(!nowFragment.getClass().getSimpleName().equals("HomeFragment")) {
            Essentials.changeFragment(this, R.id.content_main_fragment_layout, new HomeFragment());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Essentials.changeFragment(this, R.id.content_main_fragment_level_1, new SettingActivity());
            fab.setVisibility(View.GONE);
        } else if(id == R.id.action_refresh) {
            nowFragment.onFreshButtonClicked();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_facebook) {
            FacebookFragment fragment = new FacebookFragment();
            nowFragment = fragment;
            Essentials.changeFragment(this, R.id.content_main_fragment_layout, fragment);
        } else if (id == R.id.nav_twitter) {
            TwitterFragment fragment = new TwitterFragment();
            nowFragment = fragment;
            Essentials.changeFragment(this, R.id.content_main_fragment_layout, fragment);
        } else if (id == R.id.nav_kakao) {
            KakaoFragment fragment = new KakaoFragment();
            nowFragment = fragment;
            Essentials.changeFragment(this, R.id.content_main_fragment_layout, fragment);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void makePostPopup() {
        RelativeLayout layout = (RelativeLayout) MainActivity.this.findViewById(R.id.activity_main_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.article_add, null);

        if(nowFragment.getClass().getSimpleName().equals("KakaoFragment"))
            popupView.setBackground(ContextCompat.getDrawable(this, R.drawable.border_kakao));

        if(nowFragment.getClass().getSimpleName().equals("TwitterFragment"))
            popupView.setBackground(ContextCompat.getDrawable(this, R.drawable.border_twitter));

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 223, this.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, this.getResources().getDisplayMetrics());

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height);

        Button cancelButton = (Button) popupView.findViewById(R.id.article_add_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        final EditText editText = (EditText) popupView.findViewById(R.id.article_add_edit_text);

        Button postButton = (Button) popupView.findViewById(R.id.article_add_add);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nowFragment != null) {
                    nowFragment.onPostButtonClicked(editText.getText().toString());
                    popupWindow.dismiss();
                }
            }
        });

        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        popupWindow.setFocusable(true);
        popupWindow.update();
    }
}
