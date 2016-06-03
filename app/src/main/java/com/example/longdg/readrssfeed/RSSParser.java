package com.example.longdg.readrssfeed;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Longdg on 30/05/2016.
 */
public class RSSParser {
    //RSS XML document CHANNEL tag
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TIEUDE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_MOTA = "description";
    private static String TAG_NGONNGU = "language";
    private static String TAG_ITEM = "item";
    private static String TAG_NGAY = "pubDate";
    private static String TAG_GUID = "guid";
    // constructor
    public RSSParser() {

    }

    /***
     * Get RSS feed from url
     *
     * @param url - is url of the website
     * @return RSSFEED class object
     */
    public RSSFeed getRSSFeed(String url){
        RSSFeed rssFeed = null;
        String rss_feed_xml = null;

        // getting rss link from html source code
        String rss_url = this.getRSSLinkFromURL(url);

        // check if rss_link is found or not
        if(rss_url !=null){
            // RSS url found
            // get RSS XML from rss url
            rss_feed_xml = this.getXmlFromUrl(rss_url);
            // check if RSS XML fetched or not
            if(rss_feed_xml != null){
                // successfully ffetched rss xml
                // parse the xml
                try {
                    Document doc = this.getDOMElement(rss_feed_xml);
                    NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
                    Element e =(Element)nodeList.item(0);

                    // RSS nodes
                    String tieude = this.getValue(e, TAG_TIEUDE);
                    String link = this.getValue(e, TAG_LINK);
                    String mota = this.getValue(e, TAG_MOTA);
                    String ngonngu = this.getValue(e, TAG_NGONNGU);

                    // creating new RSS Feed
                    rssFeed = new RSSFeed(tieude,mota,link,rss_url,ngonngu);
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                // failed to fetch rss xml
            }
        } else {
            // no RSS url found
        }
        return rssFeed;

    }
    public String getRSSLinkFromURL(String url){
        //RSS url
        String rss_url = null;
        try {
            // Using JSoup library to parse the html source code
            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
            // finding rss links which are having link[type=application/rss+xml]
            org.jsoup.select.Elements links = doc
                    .select("link[type=application/rss+xml]");

            Log.d("No of RSS links found", " " + links.size());

            // check if urls found or not
            if (links.size() > 0) {
                rss_url = links.get(0).attr("abs:href").toString();
            } else {
                // finding rss links which are having link[type=application/rss+xml]
                org.jsoup.select.Elements links1 = doc
                        .select("link[type=application/rss+xml]");
                if(links1.size() > 0){
                    rss_url = links1.get(0).attr("abs:href").toString();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rss_url;
    }
    /**
     * Getting RSS feed items <item>
     *  @param - rss link url of the website
     *  @return - List of RSSItem class objects
     */
    public List<RssItem> getRSSFeedItems(String rss_url){
        List<RssItem> itemsList = new ArrayList<RssItem>();
        String rss_feed_xml;

        // Get RSS XML from rss url
        rss_feed_xml = this.getXmlFromUrl(rss_url);
        if(rss_feed_xml !=null){
            try{
                // lay thanh cong rss xml
                Document doc = this.getDOMElement(rss_feed_xml);
                NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
                Element e = (Element)nodeList.item(0);
                NodeList items = e.getElementsByTagName(TAG_ITEM);
                for(int i = 0 ; i<items.getLength();i++){
                    Element e1 = (Element) items.item(i);
                    String title = this.getValue(e1,TAG_TIEUDE);
                    String link = this.getValue(e1,TAG_LINK);
                    String description = this.getValue(e1,TAG_MOTA);
                    String pubdate = (this).getValue(e1,TAG_NGAY);
                    String guid = this.getValue(e1,TAG_GUID);
                    RssItem rssItem = new RssItem(title,link,description,pubdate,guid);
                    itemsList.add(rssItem);
                }

            }catch (Exception e ){
                e.printStackTrace();
            }
        }
        return itemsList;
    }

    /**
     * Method to get xml content from url HTTP GET request
     */
    public String getXmlFromUrl(String url){
        String xml = null;

        try{
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * Getting XML DOM Element
     */
    public Document getDOMElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
    public final String getElementValue(Node element){
        Node child;
        if(element !=null){
            if(element.hasChildNodes()){
                for(child = element.getFirstChild();child!=null;child=child.getNextSibling()){
                    if(child.getNodeType() == Node.TEXT_NODE || ( child.getNodeType() == Node.CDATA_SECTION_NODE)){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
}
