package com.example.longdg.readrssfeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longdg on 31/05/2016.
 */
public class RSSDatabaseHandler extends SQLiteOpenHelper {

    private static final int DB_VER  = 1;
    private static final String DB_NAME = "rssReader";
    private static final String TABLE_RSS = "website";

    private static final String KEY_ID = "id";
    private static final String KEY_TIEUDE = "tieuDe";
    private static final String KEY_LINK = "link";
    private static final String KEY_RSS_LINK = "rss_link";
    private static final String KEY_MOTA = "moTa";

    public RSSDatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RSS_TABLE = "create table " + TABLE_RSS + "( " + KEY_ID + " integer primary key, " + KEY_TIEUDE
                + " text, " + KEY_LINK + " text, " + KEY_RSS_LINK + " text, " + KEY_MOTA + " text" + ")";
        db.execSQL(CREATE_RSS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_RSS);
        onCreate(db);
    }

    /**
     * Adding a new website in websites table Function will check if a site
     * already existed in database. If existed will update the old one else
     * creates a new row
     * */
    public void addSite(Website site){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIEUDE,site.get_tieuDe());
        values.put(KEY_LINK,site.get_link());
        values.put(KEY_RSS_LINK,site.get_rss_link());
        values.put(KEY_MOTA,site.get_mieuTa());

        if(!isSiteExists(db,site.get_rss_link())){
            db.insert(TABLE_RSS,null,values);
            db.close();
        } else {
            updateSite(site);
            db.close();
        }
    }

    /**
     * Reading all rows from db
     */
    public List<Website> getAllSites(){
        List<Website> siteList = new ArrayList<Website>();
        // Select All query
        String selectQuery = "select * from " + TABLE_RSS + " order by id DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Website site = new Website();
                site.set_id(Integer.parseInt(cursor.getString(0)));
                site.set_tieuDe(cursor.getString(1));
                site.set_link(cursor.getString(2));
                site.set_rss_link(cursor.getString(3));
                site.set_mieuTa(cursor.getString(4));
                siteList.add(site);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return siteList;
    }

    /**
     * Reading a row(website) row is indentified by id
     */
    public Website getSite(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RSS,new String []{KEY_ID,KEY_TIEUDE,KEY_LINK,KEY_RSS_LINK,KEY_MOTA},KEY_ID + " =?", new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
            Website site = new Website(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
            site.set_id(Integer.parseInt(cursor.getString(0)));
            site.set_tieuDe(cursor.getString(1));
            site.set_link(cursor.getString(2));
            site.set_rss_link(cursor.getString(3));
            site.set_mieuTa(cursor.getString(4));
            cursor.close();
            db.close();
            return site;

    }

    /**
     * Deleting single row
     */
    public void deleteSite(Website site){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RSS,KEY_ID+"=?",new String[]{String.valueOf(site.get_id())});
        db.close();
    }
    /**
     * Updating a single row , row will be identified by rss link
     * */
    public int updateSite(Website site){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIEUDE,site.get_tieuDe());
        values.put(KEY_LINK,site.get_link());
        values.put(KEY_RSS_LINK,site.get_rss_link());
        values.put(KEY_MOTA,site.get_mieuTa());

        //updating row return
        int update = db.update(TABLE_RSS,values,KEY_RSS_LINK + " = ? ",new String[]{String.valueOf(site.get_rss_link())});
        db.close();
        return update;
    }

    /**
     * Checking whether a site is already existed check is done by matching rss
     * link
     * */
    public  boolean isSiteExists(SQLiteDatabase db,String rss_link){

        Cursor cursor = db.rawQuery("select 1 from " + TABLE_RSS
        + " where rss_link = '" + rss_link +"'",new String[]{});
        boolean exists = (cursor.getCount()>0);
        return exists;
    }
}
