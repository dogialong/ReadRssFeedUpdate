package com.example.longdg.readrssfeed;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Longdg on 01/06/2016.
 */
public class ListRSSItemsActivity extends ListActivity {
    // Progess Dialog
    private ProgressDialog pDialog;

    ArrayList<HashMap<String,String>> rssItemList = new ArrayList<HashMap<String, String>>();

    RSSParser rssParser = new RSSParser();
    ImageButton btnAddSite;

    List<RssItem> rssItems = new ArrayList<RssItem>();

    RSSFeed rssFeed;

    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESCRIPTION = "description";
    private static String TAG_PUB_DATE = "pubDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rss_item_list);

        btnAddSite = (ImageButton)findViewById(R.id.btnAddSite);
        btnAddSite.setVisibility(View.GONE);
        Intent i = getIntent();

        // SQLITE Row id
        Integer site_id = Integer.parseInt(i.getStringExtra("id"));

        RSSDatabaseHandler rssDB = new RSSDatabaseHandler(getApplicationContext());
        Website site = rssDB.getSite(site_id);
        String rss_link = site.get_rss_link();

        new loadRssFeedItems().execute(rss_link);

        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(ListRSSItemsActivity.this,DisPlayWebPageActivity.class);
                String  page_url = ((TextView)view.findViewById(R.id.page_url)).getText().toString();
                Toast.makeText(getApplicationContext(),page_url,Toast.LENGTH_LONG).show();
                in.putExtra("page_url",page_url);
                startActivity(in);
            }
        });

    }
    class loadRssFeedItems extends AsyncTask<String ,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListRSSItemsActivity.this);
            pDialog.setMessage("Dang tai noi dung...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String rss_url = params[0];

            rssItems = rssParser.getRSSFeedItems(rss_url);
            // looping throung each item
            for(RssItem item : rssItems){
                HashMap<String,String> map = new HashMap<String,String>();
                map.put(TAG_TITLE,item.get_tieuDe());
                map.put(TAG_LINK , item.get_link());
                map.put(TAG_PUB_DATE,item.get_ngay());
                String mieuta = item.get_mieuTa();
                if(mieuta.length()>100){
                    mieuta = mieuta.substring(0,97)+"..";
                }
                map.put(TAG_DESCRIPTION,mieuta);
                rssItemList.add(map);
            }
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   ListAdapter adapter = new SimpleAdapter(ListRSSItemsActivity.this,rssItemList,R.layout.rss_item_list_row,new String[]{TAG_LINK,TAG_TITLE, TAG_PUB_DATE, TAG_DESCRIPTION },
                           new int[] {R.id.page_url, R.id.title, R.id.pub_date, R.id.link });
                   setListAdapter(adapter);
               }
           });

            return null;

        }
        protected  void onPostExecute(String args){
            pDialog.dismiss();
        }

    }
}
