package tech.ozak.sportmix.romerss;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rometools.utils.Strings;

import org.jdom2.Attribute;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tech.ozak.sportmix.dto.RssItem;

/**
 * Created by aknka on 8/24/2016.
 */
public class RomeRssHandler {

    private static int counter = 0;

    public List<RssItem> getAllNews(String rssLink) {

        List<RssItem> result = new ArrayList<>();

        SyndFeedInput input = new SyndFeedInput();
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";
        System.setProperty("http.agent", userAgent);
        try {
            URL url = new URL(rssLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", userAgent);

            InputStream inputStream = connection.getInputStream();
            InputSource source = new InputSource(inputStream);

            System.out.println(inputStream.toString());
            SyndFeed feed = input.build(source);

            for (SyndEntry syndEntry : feed.getEntries()) {
                if (counter <= 25) {
                    String image_url = "";
                    RssItem rssFeed = new RssItem();
                    String link = syndEntry.getLink();
                    rssFeed.setFeedLink(link);
                    String title = syndEntry.getTitle();
                    rssFeed.setTitle(title);
              /*  if (!syndEntry.getContents().equals(Collections.EMPTY_LIST)) {
                    String content = syndEntry.getContents().get(0).getValue();
                    rssFeed.setContent(content);
                }*/
                    Date publishedDate = syndEntry.getPublishedDate();
                    if (null != publishedDate)
                        rssFeed.setPubDate(publishedDate.toString());
                    // get current moment in default time zone
//			    DateTime dt = new DateTime();
//			    // translate to New York local time
//			    DateTime dtTurkey = dt.withZone(DateTimeZone.forID("Europe/Istanbul"));
//			    dt=new DateTime(publishedDate);
//			    Period p = new Period(dtTurkey, dt);
//			    int hours = p.getHours();
//			    int minutes = p.getMinutes();

                    String description = syndEntry.getDescription().getValue();
                    rssFeed.setDescription(description);


                    if (Strings.isBlank(image_url)) {
                        List<SyndEnclosure> encls = syndEntry.getEnclosures();
                        if (!encls.isEmpty()) {
                            for (SyndEnclosure e : encls) {
                                image_url = e.getUrl().toString();
                                if (!Strings.isBlank(image_url)){
                                    break;
                                }
                            }
                        }
                    }

                    if (Strings.isBlank(image_url)) {
                        List<org.jdom2.Element> foreignMarkup2 = syndEntry.getForeignMarkup();
                        for (org.jdom2.Element foreignMarkup : foreignMarkup2) {
                            Attribute url1 = foreignMarkup.getAttribute("url");
                            if (url1!=null){

                                image_url = url1.getValue();
                                if (!Strings.isBlank(image_url)){

                                    break;
                                }
                            }

                            if (Strings.isBlank(image_url)){
                                url1 = foreignMarkup.getAttribute("imageUrl");
                                if (url1!=null){
                                    image_url = url1.getValue();
                                    if (!Strings.isBlank(image_url)){

                                        break;
                                    }
                                }

                            }
                        }
                    }

                    if (Strings.isBlank(image_url)) {
                        Document doc = Jsoup.parse(description);
                        Elements imgs = doc.select("img");
                        image_url = getImgSrc(imgs);
                    }
                    rssFeed.setImgLink(image_url);
                    // for (Module module : syndEntry.getModules()) {
                    // if (module instanceof MediaEntryModule) {
                    // MediaEntryModule media = (MediaEntryModule)module;
                    // for (Thumbnail thumb : media.getMetadata().getThumbnail()) {
                    // System.out.println(thumb.getUrl());
                    // }
                    // }
                    result.add(rssFeed);
                    counter++;
                }
            }
        } catch (FeedException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        counter = 0;
        return result;

    }

    private static String getImgSrc(Elements imgs) {
        for (int j = 0; j < imgs.size(); j++) {
            Element img = imgs.get(j);
            if (img.hasAttr("src")) {
                return img.attr("src");
            }
        }

        return null;
    }
}
