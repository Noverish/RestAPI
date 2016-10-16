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
import com.noverish.restapi.webview.HtmlParseWebView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BaseFragment nowFragment;

    private HtmlParseWebView anotherWebView, kakaoWebView, twitterWebView, facebookWebView;
    private FrameLayout mainFrame, level1Frame, level2Frame, level3Frame;

    private FloatingActionButton fab;

    private int debugStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        mainFrame = (FrameLayout) findViewById(R.id.activity_main_fragment_main);
        level1Frame = (FrameLayout) findViewById(R.id.activity_main_fragment_level_1);
        level2Frame = (FrameLayout) findViewById(R.id.activity_main_fragment_level_2);
        level3Frame = (FrameLayout) findViewById(R.id.activity_main_fragment_level_3);

        anotherWebView = (HtmlParseWebView) findViewById(R.id.activity_main_another_web_view);
        kakaoWebView = (HtmlParseWebView) findViewById(R.id.activity_main_kakao_web_view);
        twitterWebView = (HtmlParseWebView) findViewById(R.id.activity_main_twitter_web_view);
        facebookWebView = (HtmlParseWebView) findViewById(R.id.activity_main_facebook_web_view);

        Essentials.changeFragment(this, R.id.activity_main_fragment_splash, new SplashFragment());

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setOnFirstLoadFinishedRunnable(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.findViewById(R.id.activity_main_fragment_splash).setVisibility(GONE);
                fab.setVisibility(VISIBLE);
            }
        });
        nowFragment = homeFragment;
        Essentials.changeFragment(this, R.id.activity_main_fragment_main, homeFragment);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mainFrame.getVisibility() == View.GONE) {
            System.out.println("MAINFRAME - INVISIBLE");
           changeVisibleLevel(LEVEL_3);
        } else if(level3Frame.getChildCount() > 0) {
            level3Frame.removeAllViews();
        } else if(level2Frame.getChildCount() > 0) {
            level2Frame.removeAllViews();

            try {
                SettingFragment settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_fragment_level_2);
                if(settingFragment.isLoginStatusChanged())
                    refresh();
            } catch (Exception ex) {

            }
        } else if(level1Frame.getChildCount() > 0) {
            level1Frame.removeAllViews();
        } else if(!nowFragment.getClass().getSimpleName().equals("HomeFragment")) {
            Essentials.changeFragment(this, R.id.activity_main_fragment_main, new HomeFragment());
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
            Essentials.changeFragment(this, R.id.activity_main_fragment_level_1, new SettingFragment());
            fab.setVisibility(GONE);
        } else if(id == R.id.action_refresh) {
            refresh();
        } else if(id == R.id.action_debug) {
            if(debugStatus == 0) {
                changeVisibleLevel(LEVEL_FACEBOOK);
                debugStatus = 1;
            } else if(debugStatus == 1) {
                changeVisibleLevel(LEVEL_TWITTER);
                debugStatus = 2;
            } else if(debugStatus == 2) {
                changeVisibleLevel(LEVEL_3);
                debugStatus = 0;
            }
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
            Essentials.changeFragment(this, R.id.activity_main_fragment_main, fragment);
        } else if (id == R.id.nav_twitter) {
            TwitterFragment fragment = new TwitterFragment();
            nowFragment = fragment;
            Essentials.changeFragment(this, R.id.activity_main_fragment_main, fragment);
        } else if (id == R.id.nav_kakao) {
            KakaoFragment fragment = new KakaoFragment();
            nowFragment = fragment;
            Essentials.changeFragment(this, R.id.activity_main_fragment_main, fragment);
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

    public void refresh() {
        nowFragment.onFreshButtonClicked();
    }

    public static final int LEVEL_ANOTHER = 0;
    public static final int LEVEL_KAKAO = 1;
    public static final int LEVEL_TWITTER = 2;
    public static final int LEVEL_FACEBOOK = 3;
    public static final int LEVEL_MAIN = 4;
    public static final int LEVEL_1 = 5;
    public static final int LEVEL_2 = 6;
    public static final int LEVEL_3 = 7;

    public void changeVisibleLevel(int level) {
        View[] views = {anotherWebView, kakaoWebView, twitterWebView, facebookWebView, mainFrame, level1Frame, level2Frame, level3Frame};

        int i = 0;
        for (; i <= level; i++) {
            views[i].setVisibility(VISIBLE);
        }
        for (; i < 7; i++) {
            views[i].setVisibility(GONE);
        }
    }
}
