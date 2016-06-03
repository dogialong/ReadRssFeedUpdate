package com.example.longdg.readrssfeed;

/**
 * This class file used while inserting data or retreving data from
 * SQLite database
 */
public class Website {
    Integer _id;
    String _tieuDe;
    String _link;
    String _rss_link;
    String _mieuTa;

    //constructor

    public Website() {
    }

    public Website( String _tieuDe, String _link, String _rss_link, String _mieuTa) {

        this._tieuDe = _tieuDe;
        this._link = _link;
        this._rss_link = _rss_link;
        this._mieuTa = _mieuTa;
    }

    /**
     * All set mothods;
     */
    public void set_id(Integer _id) {
        this._id = _id;
    }

    public void set_tieuDe(String _tieuDe) {
        this._tieuDe = _tieuDe;
    }

    public void set_link(String _link) {
        this._link = _link;
    }

    public void set_rss_link(String _rss_link) {
        this._rss_link = _rss_link;
    }

    public void set_mieuTa(String _mieuTa) {
        this._mieuTa = _mieuTa;
    }

    /**
     * All get mothods;
     */

    public Integer get_id() {
        return _id;
    }

    public String get_tieuDe() {
        return _tieuDe;
    }

    public String get_link() {
        return _link;
    }

    public String get_rss_link() {
        return _rss_link;
    }

    public String get_mieuTa() {
        return _mieuTa;
    }
}
