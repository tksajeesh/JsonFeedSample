package com.saj.jsonfeedsample;

import android.util.Log;

import com.saj.jsonfeedsample.models.FeedItem;
import com.saj.jsonfeedsample.models.FeedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class JsonParser fo parsing the JSON data from server
 */
public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();
    private static String TAG_TITLE = "title";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_IMAGE_REF = "imageHref";
    private static String TAG_ROWS = "rows";



	public static Object getModelFromJson(String jsonString, Object modelObject)
			throws NullPointerException {

        JSONArray rows;
        FeedList feedList = null;

        if(jsonString != null){
            try{
                feedList =  new FeedList();
                List<FeedItem> feedItemList = new ArrayList<FeedItem>();
                JSONObject jsonObj = new JSONObject(jsonString);

                String title = jsonObj.getString(TAG_TITLE);
                Log.e(TAG, "Title is :" + title);
                feedList.setTitle(title);

                rows = jsonObj.getJSONArray(TAG_ROWS);
                for(int i =0; i<rows.length(); i++){
                    JSONObject item = rows.getJSONObject(i);

                    String itemTitle = item.getString(TAG_TITLE);
                    String description = item.getString(TAG_DESCRIPTION);
                    String imageUrl = item.getString(TAG_IMAGE_REF);

                    FeedItem feedItem = new FeedItem(itemTitle,description,imageUrl);
                    Log.e(TAG,"FeedItem:" + feedItem.toString());

                    feedItemList.add(feedItem);

                }

                // Add the feed item list to the feed list
                feedList.setFeedItemList(feedItemList);

            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        return feedList;
	}
}
