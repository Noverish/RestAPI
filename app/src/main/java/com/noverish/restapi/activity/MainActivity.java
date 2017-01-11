package com.noverish.restapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookClient;
import com.noverish.restapi.facebook.FacebookFragment;
import com.noverish.restapi.kakao.KakaoFragment;
import com.noverish.restapi.base.BaseFragment;
import com.noverish.restapi.other.Essentials;
import com.noverish.restapi.twitter.TwitterClient;
import com.noverish.restapi.twitter.TwitterFragment;
import com.noverish.restapi.webview.HtmlParseWebView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.noverish.restapi.activity.MainActivity.Status.MESSAGE;
import static com.noverish.restapi.activity.MainActivity.Status.NOTI;
import static com.noverish.restapi.activity.MainActivity.Status.SETTING;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static SubMenu subMenu;
    public static MainActivity instance;

    private BaseFragment nowFragment;

    public HtmlParseWebView anotherWebView;
    private HtmlParseWebView kakaoWebView, twitterWebView, facebookWebView;
    private FrameLayout mainFrame, level1Frame, level2Frame, level3Frame;

    private FloatingActionButton fab;

    private int debugStatus = 0;

    public enum Status {HOME, MESSAGE_FACEBOOK, MESSAGE, NOTI, SETTING}
    private Status status;

    public String nowCategory = "전체보기";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WriteActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu m = navigationView.getMenu();
        subMenu = m.addSubMenu("Category");
        subMenu.add("전체보기");

        mainFrame = (FrameLayout) findViewById(R.id.activity_main_fragment_main);
        level1Frame = (FrameLayout) findViewById(R.id.activity_main_fragment_level_1);
        level2Frame = (FrameLayout) findViewById(R.id.activity_main_fragment_level_2);
        level3Frame = (FrameLayout) findViewById(R.id.activity_main_fragment_level_3);

        anotherWebView = (HtmlParseWebView) findViewById(R.id.activity_main_another_web_view);
        twitterWebView = (HtmlParseWebView) findViewById(R.id.activity_main_twitter_web_view);
        kakaoWebView = (HtmlParseWebView) findViewById(R.id.activity_main_kakao_web_view);
        facebookWebView = (HtmlParseWebView) findViewById(R.id.activity_main_facebook_web_view);

        FacebookClient.getInstance().setWebView(facebookWebView);
        TwitterClient.getInstance().setWebView(twitterWebView);

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
        } else {
            switch (status) {
                case HOME: {
                    finish();
                }
                case SETTING:
                case NOTI:
                case MESSAGE: {
                    level1Frame.removeAllViews();
                    setStatus(Status.HOME);
                    break;
                }
                case MESSAGE_FACEBOOK: {
                    changeVisibleLevel(LEVEL_3);
                    setStatus(MESSAGE);
                    break;
                }
            }
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
            setStatus(SETTING);
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
        } else if(id == R.id.action_notifications) {
            Essentials.changeFragment(this, R.id.activity_main_fragment_level_1, new NotificationFragment());
            fab.setVisibility(GONE);
            setStatus(NOTI);
        } else if(id == R.id.action_messages) {
            Essentials.changeFragment(this, R.id.activity_main_fragment_level_1, new MessageFragment());
            fab.setVisibility(GONE);
            setStatus(MESSAGE);
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
        } else {
            nowFragment.scrollTop();
            nowFragment.showArticleByCategory(item.getTitle().toString());
            setNowCategory(item.getTitle().toString());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setNowCategory(String category) {
        this.nowCategory = category;
    }

    public String getNowCategory() {
        return nowCategory;
    }
}
