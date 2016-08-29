package com.noverish.restapi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.facebook.FacebookFragment;
import com.noverish.restapi.http.HttpConnectionThread;
import com.noverish.restapi.kakao.KakaoFragment;
import com.noverish.restapi.other.BaseFragment;
import com.noverish.restapi.other.Essentials;
import com.noverish.restapi.other.OnHtmlLoadSuccessListener;
import com.noverish.restapi.twitter.TwitterFragment;
import com.noverish.restapi.view.HtmlParsingWebView;

import org.jsoup.Jsoup;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BaseFragment nowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPostButtonClicked();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Essentials.changeFragment(this, R.id.content_main_fragment_layout, new HomeFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FrameLayout layout = (FrameLayout) findViewById(R.id.content_main_fragment_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(layout.getChildCount() > 0){
            layout.removeAllViews();
        } else {
            Log.d("back","super");
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
            startActivity(new Intent(this, LoginCenterActivity.class));
        } else if(id == R.id.action_favorite) {

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_facebook) {
            Bundle bundle = new Bundle();
            bundle.putString("url","https://m.facebook.com/?_rdr");

            HtmlParsingWebView webView = new HtmlParsingWebView();
            webView.setArguments(bundle);
            webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
                @Override
                public void onHtmlLoadSuccess(String htmlCode) {
                    FacebookFragment.getInstance().htmlHasChanged(htmlCode);
                }
            });

            FacebookFragment fragment = new FacebookFragment();
            nowFragment = fragment;

            Essentials.changeFragment(this, R.id.content_main_background_layout, webView);
            Essentials.changeFragment(this, R.id.content_main_fragment_layout, fragment);
        } else if (id == R.id.nav_twitter) {
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.content_main_background_layout);
            viewGroup.removeAllViews();

            TwitterFragment fragment = new TwitterFragment();
            nowFragment = fragment;

            Essentials.changeFragment(this, R.id.content_main_fragment_layout, fragment);
        } else if (id == R.id.nav_kakao) {
            Bundle bundle = new Bundle();
            bundle.putString("url","https://story.kakao.com/s/login");

            HtmlParsingWebView webView = new HtmlParsingWebView();
            webView.setArguments(bundle);
            webView.setOnHtmlLoadSuccessListener(new OnHtmlLoadSuccessListener() {
                @Override
                public void onHtmlLoadSuccess(String htmlCode) {
                    KakaoFragment kakaoFragment = KakaoFragment.getInstance();
                    String title = HttpConnectionThread.unicodeToString(Jsoup.parse(htmlCode).title());

                    if(kakaoFragment.getView() != null) {
                        if (title.contains("로그인")) {
                            kakaoFragment.getView().setVisibility(View.GONE);
                        } else {
                            kakaoFragment.getView().setVisibility(View.VISIBLE);
                            KakaoFragment.getInstance().htmlHasChanged(htmlCode);
                        }
                    } else {
                        Log.e("ERROR", "kakaoFragment.getView() is null");
                    }

                }
            });

            KakaoFragment fragment = new KakaoFragment();
            nowFragment = fragment;

            Essentials.changeFragment(this, R.id.content_main_background_layout, webView);
            Essentials.changeFragment(this, R.id.content_main_fragment_layout, fragment);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onPostButtonClicked() {
        RelativeLayout layout = (RelativeLayout) MainActivity.this.findViewById(R.id.activity_main_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.article_add, null);

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
