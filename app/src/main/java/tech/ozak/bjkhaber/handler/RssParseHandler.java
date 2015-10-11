package tech.ozak.bjkhaber.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import tech.ozak.bjkhaber.dto.RssItem;


/**
 * SAX tag handler
 *
 * @author ITCuties
 */
public class RssParseHandler extends DefaultHandler {

    private List<RssItem> rssItems;

    // Used to reference item while parsing
    private RssItem currentItem;

    // Parsing title indicator
    private boolean parsingTitle;
    // Parsing link indicator
    private boolean parsingLink;
    // Parsing pubDate indicator
    private boolean parsingPubDate;

    private boolean parsingPostThumbUrl;

    public RssParseHandler() {
        rssItems = new ArrayList<RssItem>();
    }

    public List<RssItem> getItems() {
        return rssItems;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("item".equals(qName)) {
            currentItem = new RssItem();
        } else if ("title".equals(qName)) {
            parsingTitle = true;
        } else if ("link".equals(qName)) {
            parsingLink = true;
        } else if ("pubDate".equals(qName)) {
            parsingPubDate = true;
        } else if ("jms-featured-image".equals(qName)) {
            parsingPostThumbUrl = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("item".equals(qName)) {
            rssItems.add(currentItem);
            currentItem = null;
        } else if ("title".equals(qName)) {
            parsingTitle = false;
        } else if ("link".equals(qName)) {
            parsingLink = false;
        } else if ("pubDate".equals(qName)) {
            parsingPubDate = false;
        }else if ("jms-featured-image".equals(qName)) {
            parsingPostThumbUrl = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null)
                currentItem.setTitle(new String(ch, start, length));
        } else if (parsingLink) {
            if (currentItem != null) {
                currentItem.setLink(new String(ch, start, length));
                parsingLink = false;
            }
        } else if (parsingPubDate) {
            if (currentItem != null) {
                currentItem.setPostDate(new String(ch, start, length));
                parsingPubDate = false;
            }
        }else if (parsingPostThumbUrl) {
            if (currentItem != null) {
                currentItem.setPostThumbUrl(new String(ch, start, length));
                parsingPostThumbUrl = false;
            }
        }
    }

}
