package com.example.longdg.readrssfeed;

/**
 * Created by Longdg on 30/05/2016.
 */

import java.util.List;

/**
 * This class handle rss xml
 */
public class RSSFeed {
    // xml nodes
    String _tieuDe;
    String _moTa;
    String _link;
    String _rss_link;
    String _ngonNgu;
    List<RssItem> _item;

    //constructor

    public RSSFeed(String _tieuDe, String _moTa, String _link, String _rss_link, String _ngonNgu) {
        this._tieuDe = _tieuDe;
        this._moTa = _moTa;
        this._link = _link;
        this._rss_link = _rss_link;
        this._ngonNgu = _ngonNgu;
    }

    /**
     * All set mothods
     */
    public void set_item(List<RssItem> _item) {
        this._item = _item;
    }
    /**
     * All get methods
     */
    public String get_tieuDe() {
        return _tieuDe;
    }

    public String get_moTa() {
        return _moTa;
    }

    public String get_link() {
        return _link;
    }

    public String get_rss_link() {
        return _rss_link;
    }

    public String get_ngonNgu() {
        return _ngonNgu;
    }

    public List<RssItem> get_item() {
        return _item;
    }
}
