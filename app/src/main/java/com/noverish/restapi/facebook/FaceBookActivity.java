package com.noverish.restapi.facebook;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import com.noverish.restapi.R;
import com.noverish.restapi.http.HttpConnectionThread;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Noverish on 2016-06-06.
 */
public class FaceBookActivity extends AppCompatActivity{
    private WebView webView;
    private Button button;
    private LinearLayout list;
    private LinearLayout webviewlayout;
    private String url = "http://www.facebook.com";

    private String htmlCode;

    private final String TAG = getClass().getSimpleName();
    public ArrayList<FacebookArticle> articles = new ArrayList<>();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_facebook);

        list = (LinearLayout) findViewById(R.id.activity_facebook_text_view_list);

        webView = (WebView) findViewById(R.id.activity_facebook_webview);
        webView.getSettings().setJavaScriptEnabled(true);

        webviewlayout = (LinearLayout) findViewById(R.id.activity_facebook_webview_layout);
/*
        String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        webView.getSettings().setUserAgentString(newUA);


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
*/
        button = (Button) findViewById(R.id.activity_facebook_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(
                        "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(final String html) {
                                htmlCode = html;
                                displayArticle(html);

                                webviewlayout.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);

        webView.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
        webView.loadUrl(url);
    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "shouldOverrideUrlLoading : " + url);
            return false;
        }

        @Override
        public void onPageFinished(final WebView view, String url) {

        }
    }

    private void displayArticle(String htmlCode) {
        htmlCode = htmlCode.replaceAll("(\\\\){1,7}\"","\"");
        htmlCode = htmlCode.replaceAll("&amp;","&");
        htmlCode = htmlCode.replaceAll("(\\\\){0,2}&quot;","\"");
        htmlCode = htmlCode.replaceAll("(\\\\){2,3}x3C", "<");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}u003C","<");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}u003E",">");
        htmlCode = htmlCode.replaceAll("(\\\\){1,2}/","/");
        htmlCode = HttpConnectionThread.unicodeToString(htmlCode);

        Pattern pattern = Pattern.compile("<article(\\S|\\s)*?</article>");
        Matcher matcher = pattern.matcher(htmlCode);

        while (matcher.find()) {
            String tmp = matcher.group();
            articles.add(new FacebookArticle(tmp));
        }

        for(FacebookArticle article : articles) {
            list.addView(new FacebookArticleView(this, article));
        }
    }









    /*
    private CallbackManager callbackManager;
    private com.facebook.AccessToken accessToken;
    private GraphResponse graphResponse;
    private JSONObject jsonObject;

    private Button button001, button002, button003;
    private TextView textView;
    private ImageView imageView;

    JSONArray array;

    private WebView webView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook);
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.activity_face_book_login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();

                Log.e("FacebookCallback", accessToken.toString());
            }

            @Override
            public void onCancel() {
                Log.e("FacebookCallback", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });



        textView = (TextView) findViewById(R.id.activity_face_book_text_view);
        imageView = (ImageView) findViewById(R.id.activity_face_book_image_view);

        button001 = (Button) findViewById(R.id.button001);
        button001.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button1();
            }
        });

        button002 = (Button) findViewById(R.id.button002);
        button002.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button2();
            }
        });
        button003 = (Button) findViewById(R.id.button003);

        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://www.facebook.com/");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void button1() {
        accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                graphResponse = response;
                jsonObject = object;
                Log.e("GraphJSONObjectCallback","success");
                Log.e("GraphJSONObjectCallback",object.toString());
                Log.e("GraphJSONObjectCallback", response.toString());

                textView.setText(jsonObject.toString());

                try {
                    String url = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                    Log.e("asdf",url);
                    imageView.setBackground(HttpConnectionThread.getImageDrawableFromUrl(url));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,age_range,picture,timezone,verified,gender,locale");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void button2() {
        try {
            String id = jsonObject.getString("id");

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/" + id + "/picture",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.e("Asdf",response.toString());
                        }
                    }
            ).executeAsync();
        } catch (Exception json) {
            json.printStackTrace();
        }
    }
    */
}
