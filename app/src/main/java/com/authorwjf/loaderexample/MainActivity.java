package com.authorwjf.loaderexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class MainActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener,
        LoaderManager.LoaderCallbacks<List<String>> {

    private static final int THE_LOADER = 0x01;
    ListView listview;
    //private ListView listview;
    private SwipeRefreshLayout mSwipe;//=(SwipeRefreshLayout)findViewById(R.id.swipe_container);

    /*
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        listview = (ListView) findViewById(R.id.listview);

        SwipeRefreshLayout layout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        layout.setOnRefreshListener(this);

        //fix scroll to up
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {  }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                int topRowVerticalPosition =
                        (listview == null || listview.getChildCount() == 0) ?
                                0 : listview.getChildAt(0).getTop();
                mSwipe.setEnabled(i == 0 && topRowVerticalPosition >= 0);
            }
        });


        if(mSwipe!=null) {
            try {
                mSwipe.post(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    void loadData() {
        mSwipe.setRefreshing(true);
        Loader loader = getSupportLoaderManager().getLoader(THE_LOADER);
        if (loader == null) {
            loader = getSupportLoaderManager().initLoader(THE_LOADER, null, this);
        } else {
            loader = getSupportLoaderManager().restartLoader(THE_LOADER, null, this);
        }
        loader.forceLoad();
    }

    @Override
    public void onRefresh() {
        // говорим о том, что собираемся начать
        // начинаем показывать прогресс
        try {
            loadData();
        }
        catch (Exception e)
        {int i=0;}
    }


    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        SampleLoader loader = new SampleLoader(this);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> list) {
        try {
            if(mSwipe!=null) {
                mSwipe.setRefreshing(false);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, list);
                listview.setAdapter(adapter);
            }
        }
        catch (Exception e)
        {}
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        ListView lst=(ListView)findViewById(R.id.listview);
        if(lst!=null)
        {lst.setAdapter(null);}
    }

    private static class SampleLoader extends AsyncTaskLoader<List<String>> {

        public SampleLoader(Context context) {
            super(context);
        }

        @Override
        public List<String> loadInBackground() {

            final String[] animals = new String[]{"Ape", "Bird", "Cat", "Dog",
                    "Elephant", "Fox",
                    "Gorilla", "Hyena", "Inch Worm", "Jackalope", "King Salmon", "Lizard",
                    "Monkey", "Narwhal", "Octopus", "Pig", "Quail", "Rattle Snake",
                    "Salamander",
                    "Tiger", "Urchin", "Vampire Bat", "Wombat", "X-Ray Tetra", "Yak", "Zebra"};


            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < animals.length; ++i) {
                list.add(animals[i]);
                try {
                    Thread.sleep(100); //simulated network delay
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return list;
        }

    }

}
