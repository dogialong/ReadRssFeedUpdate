package com.example.longdg.readrssfeed;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Longdg on 01/06/2016.
 */
public class AddNewSiteActivity extends Activity {
    Button btnSubmit;
    Button btnCancel;
    EditText editUrl;
    TextView tvMes;
    RSSFeed rssFeed;
    RSSParser rssParser = new RSSParser();

    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_site);

        //Buttons
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        editUrl = (EditText)findViewById(R.id.txtUrl);
        tvMes=(TextView)findViewById(R.id.lblMessage);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editUrl.getText().toString();
                Log.d("URL length","" + url.length());
                if(url.length()>0){
                    tvMes.setText("");
                    String urlPattern = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
                    if(url.matches(urlPattern)){

                        new loadRSSFeed().execute(url);
                    } else {
                        tvMes.setText("Nhap dung url hop le");
                    }
                } else {
                    tvMes.setText("Hay nhap url website");
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    class loadRSSFeed extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddNewSiteActivity.this);
            pDialog.setMessage("Dang tim kiem thong tin RSS ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            rssFeed = rssParser.getRSSFeed(url);
            Log.d("rssFeed"," " + rssFeed);
            if(rssFeed != null){
                Log.e("RSS URL",rssFeed.get_tieuDe() +"" + rssFeed.get_link() + "" + rssFeed.get_moTa()
                + rssFeed.get_ngonNgu());
                RSSDatabaseHandler rssDB = new RSSDatabaseHandler(getApplicationContext());
                Website site = new Website(rssFeed.get_tieuDe(), rssFeed.get_link(), rssFeed.get_rss_link(),
                        rssFeed.get_moTa());
                rssDB.addSite(site);

                Intent i = getIntent();
                // send result code 100 to notify about product update
                setResult(100,i);
                finish();
            } else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMes.setText("Khong tim thay Rss URL. Kiem tra lai Rss URL");
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (rssFeed != null) {

                    }
                }
            });
        }
    }
}
