package com.saj.jsonfeedsample;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saj.jsonfeedsample.models.FeedItem;
import com.saj.jsonfeedsample.models.FeedList;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Main Activity class showing the list view
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.lv_main)
    ListView lvMain;

    @InjectView(R.id.pb_waiting)
    ProgressBar pbWaiting;

    @InjectView(R.id.tv_error)
    TextView tvErrorLoading;


    Button refreshButton;

    private List<FeedItem> feedItemList;
    private FeedAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        feedItemList = new ArrayList<FeedItem>();
        adapter = new FeedAdapter(this, feedItemList);

        View footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        lvMain.addFooterView(footerView);


        refreshButton = (Button) footerView.findViewById(R.id.btn_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
            }
        });

        lvMain.setAdapter(adapter);

        refreshData();

    }


    void refreshData() {
        pbWaiting.setVisibility(VISIBLE);
        new FetchDataTask().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Async task for getting the data from the server
     */
    private class FetchDataTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected Object doInBackground(Void... voids) {
            Object list;
            String url = "https://dl.dropboxusercontent.com/u/746330/facts.json";
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonResponse = response.body().string();
                Log.e(TAG, "Response Json:" + jsonResponse);

                list = JsonParser.getModelFromJson(jsonResponse, FeedList.class);

            } catch (IOException e) {
                Log.e(TAG, "IO Exception while execute", e);
                e.printStackTrace();
                return null;
            }

            return list;

        }

        @Override
        protected void onPostExecute(Object feedList) {

            if (feedList != null) {
                FeedList list = (FeedList) feedList;
                feedItemList.clear();
                // add only the contents which are valid, remove the null contents
                for (FeedItem item : list.getFeedItemList()) {
                    if (!item.getTitle().equalsIgnoreCase("null") && !item.getDescription().equalsIgnoreCase("null")) {
                        feedItemList.add(item);
                    }
                }

                adapter.notifyDataSetChanged();
                setTitle(list.getTitle());
                pbWaiting.setVisibility(GONE);
                refreshButton.setVisibility(VISIBLE);

            } else {
                Log.e(TAG, "Received No Data");
                pbWaiting.setVisibility(GONE);
                if(feedItemList.size() == 0) {
                    tvErrorLoading.setVisibility(VISIBLE);
                }else{
                    tvErrorLoading.setVisibility(GONE);
                }
            }
        }
    }
}
