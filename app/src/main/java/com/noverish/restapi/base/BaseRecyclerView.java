package com.noverish.restapi.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.noverish.restapi.rss.cnn.CNNItem;
import com.noverish.restapi.twitter.TwitterArticleView;

import java.util.ArrayList;

/**
 * Created by Noverish on 2017-02-21.
 */

public class BaseRecyclerView extends RecyclerView {
    public static final int CNN = 2;

    private Context context;
    private LinearLayoutManager layoutManager;
    private CustomAdapter adapter;
    private ArrayList<ArticleItem> items = new ArrayList<>();

    public BaseRecyclerView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setLayoutManager(layoutManager = new LinearLayoutManager(context));
        setAdapter(adapter = new CustomAdapter());
    }

    public void addItem(ArrayList<ArticleItem> items) {
        this.items.addAll(items);
    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case CNN:
                    return new CustomViewHolder(new TwitterArticleView(context, null));
                default:
                    return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(items.get(position) instanceof CNNItem)
                return CNN;
            else
                return -1;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
//            holder.articleView.setItem(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {
        private ArticleView articleView;

        public CustomViewHolder(ArticleView articleView) {
            super(articleView);
            this.articleView = articleView;
        }
    }
}
