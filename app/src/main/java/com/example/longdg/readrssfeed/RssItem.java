package com.example.longdg.readrssfeed;

/**
 * This class handle RSS Item <item> node in rss xml
 */
public class RssItem {
    //All <item> node name;
    String _tieuDe;
    String _link;
    String _mieuTa;
    String _ngay;
    String _guid;

    //constructor

    public RssItem() {
    }

    //constructor with paremeters

    public RssItem(String _tieuDe, String _link, String _mieuTa, String _ngay, String _guid) {
        this._tieuDe = _tieuDe;
        this._link = _link;
        this._mieuTa = _mieuTa;
        this._ngay = _ngay;
        this._guid = _guid;
    }

    /**
     * All Set mothods
     */
    public void set_tieuDe(String _tieuDe) {
        this._tieuDe = _tieuDe;
    }

    public void set_link(String _link) {
        this._link = _link;
    }

    public void set_mieuTa(String _mieuTa) {
        this._mieuTa = _mieuTa;
    }

    public void set_ngay(String _ngay) {
        this._ngay = _ngay;
    }

    public void set_guid(String _guid) {
        this._guid = _guid;
    }

    /**
     * All Get methods
     */
    public String get_tieuDe() {
        return _tieuDe;
    }

    public String get_link() {
        return _link;
    }

    public String get_mieuTa() {
        return _mieuTa;
    }

    public String get_ngay() {
        return _ngay;
    }

    public String get_guid() {
        return _guid;
    }
}
