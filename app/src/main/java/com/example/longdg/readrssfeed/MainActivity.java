package com.example.longdg.readrssfeed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    //Progess Dialog
    private ProgressDialog pDialog;

    //Array list for list view
    ArrayList<HashMap<String,String>> rssFeedList;

    RSSParser rssParser = new RSSParser();

    RSSFeed rssFeed;

    // button add new web
    ImageButton btnAddSite;

    // array to trace sqlite ids

    String [] sqliteIds;

    public static String TAG_ID = "id";
    public static String TAG_TITLE = "title";
    public static String TAG_LINK = "link";

    //ListView
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site_list);

        btnAddSite = (ImageButton) findViewById(R.id.btnAddSite);


        // Hashmap for ListView
        rssFeedList = new ArrayList<HashMap<String, String>>();

        new loadStoreSites().execute();

        lv = (ListView)findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String sqlite_id = ((TextView) view.findViewById(R.id.sqlite_id)).getText().toString();
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity.class);
                // passing sqlite row id
                in.putExtra(TAG_ID, sqlite_id);
                startActivity(in);
            }
        });
        btnAddSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AddNewSiteActivity.class);
                startActivityForResult(i,100);
            }
        });
    }
    /**
     * Response from AddnewSiteActivity.java
     * if response is 100 means new site is added to sqlite
     * reload this activity again to show
     * newly added website in listview
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            //reload this screen again
            Intent i = getIntent();
            finish();
            startActivity(i);
        }
    }

    /**
     * Building a context menu for listview
     * long press on List row to see context menu
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()==R.id.list){
            menu.setHeaderTitle("Xoa");
            menu.add(Menu.NONE,0,0,"Xoa tin");
        }
    }
    /**
     * Responding to context menu selected option
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
       AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        if(menuItemIndex==0){
            //user selected delete
            // delete the feed
            RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getApplicationContext());
            Website site=new Website();
            site.set_id(Integer.parseInt(sqliteIds[info.position]));
            rssDb.deleteSite(site);
            //reloading same activity again
            Intent intent = getIntent();
            finish();
            startActivity(intent);

        }
        return true;
    }

    /**
     * Background Async Task to get RSS data from URL
     */
    class loadStoreSites extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Dang tai du lieu ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RSSDatabaseHandler rssDb = new RSSDatabaseHandler(getApplicationContext());
                    List<Website> siteList = rssDb.getAllSites();
                    sqliteIds = new String[siteList.size()];

                    // loop through each website
                    for(int i = 0; i < siteList.size();i++){

                        Website s  = siteList.get(i);

                        // Creating new hashmap
                        HashMap<String,String> map = new HashMap<String, String>();

                        map.put(TAG_ID,s.get_id().toString());
                        map.put(TAG_TITLE,s.get_tieuDe());
                        map.put(TAG_LINK,s.get_link());
                       // Toast.makeText(getApplicationContext(),s.get_tieuDe() + "   "  + s.get_id() + "  " + s.get_link(),Toast.LENGTH_SHORT).show();
                        rssFeedList.add(map);
                        // add sqlite id to array
                        // used when deleting a website from sqlite
                        sqliteIds[i] = s.get_id().toString();
                        /**
                         * Updating list view with website
                         */
                        ListAdapter adapter = new SimpleAdapter(
                                MainActivity.this,rssFeedList,R.layout.site_list_row,
                                new String[]{TAG_ID,TAG_TITLE,TAG_LINK },
                                new int[]{R.id.sqlite_id,R.id.title,R.id.link});
                        lv.setAdapter(adapter);
                        registerForContextMenu(lv);
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
        }
    }
}
