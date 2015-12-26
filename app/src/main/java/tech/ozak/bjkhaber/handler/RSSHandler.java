package tech.ozak.bjkhaber.handler;

import android.util.Log;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import tech.ozak.bjkhaber.dto.RssItem;


public class RSSHandler extends DefaultHandler {

	// Feed and Article objects to use for temporary storage
	private RssItem currentArticle = new RssItem();
	private List<RssItem> articleList = new ArrayList<RssItem>();

	// Number of articles added so far
	private int articlesAdded = 0;

	// Number of articles to download
	private static final int ARTICLES_LIMIT = 15;
	
	//Current characters being accumulated
	StringBuffer chars = new StringBuffer();

	
	/* 
	 * This method is called everytime a start element is found (an opening XML marker)
	 * here we always reset the characters StringBuffer as we are only currently interested
	 * in the the text values stored at leaf nodes
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) {
		chars = new StringBuffer();
	}



	/* 
	 * This method is called everytime an end element is found (a closing XML marker)
	 * here we check what element is being closed, if it is a relevant leaf node that we are
	 * checking, such as Title, then we get the characters we have accumulated in the StringBuffer
	 * and set the current Article's title to the value
	 * 
	 * If this is closing the "Item", it means it is the end of the article, so we add that to the list
	 * and then reset our Article object for the next one on the stream
	 * 
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (localName.equalsIgnoreCase("title"))
		{
			Log.d("LOGGING RSS XML", "Setting article title: " + chars.toString());
			currentArticle.setTitle(chars.toString());

		}
		else if (localName.equalsIgnoreCase("description"))
		{
			Log.d("LOGGING RSS XML", "Setting article description: " + chars.toString());
			currentArticle.setDescription(chars.toString());
		}
		else if (localName.equalsIgnoreCase("pubDate"))
		{
			Log.d("LOGGING RSS XML", "Setting article published date: " + chars.toString());
			currentArticle.setPubDate(chars.toString());
		}
		else if (localName.equalsIgnoreCase("encoded"))
		{
			Log.d("LOGGING RSS XML", "Setting article content: " + chars.toString());
			currentArticle.setEncodedContent(chars.toString());
		}
		else if (localName.equalsIgnoreCase("item"))
		{

		}
		else if (localName.equalsIgnoreCase("link"))
		{
			try {
				Log.d("LOGGING RSS XML", "Setting article link url: " + chars.toString());
				currentArticle.setUrl(new URL(chars.toString()));
				currentArticle.setFeedLink(chars.toString());
			} catch (MalformedURLException e) {
				Log.e("RSA Error", e.getMessage());
			}

		}




		// Check if looking for article, and if article is complete
		if (localName.equalsIgnoreCase("item")) {

			articleList.add(currentArticle);
			
			currentArticle = new RssItem();

			// Lets check if we've hit our limit on number of articles
			articlesAdded++;
			if (articlesAdded >= ARTICLES_LIMIT)
			{
				throw new SAXException();
			}
		}
	}
	
	



	/* 
	 * This method is called when characters are found in between XML markers, however, there is no
	 * guarante that this will be called at the end of the node, or that it will be called only once
	 * , so we just accumulate these and then deal with them in endElement() to be sure we have all the
	 * text
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char ch[], int start, int length) {
		chars.append(new String(ch, start, length));
	}



   /* public static String stripInvalidXmlCharacters(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (XMLChar.isValid(c)) {
                sb.append(c);
            }
        }

        return sb.toString();
    }*/



	/**
	 * This is the entry point to the parser and creates the feed to be parsed
	 * 
	 * @param feedUrl
	 * @return
	 */
	public List<RssItem> getLatestArticles(String feedUrl) {
		URL url = null;
		try {

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			url = new URL(feedUrl);


            // for resolve java.io.FileNotFoundException: http://m.ligtv.com.tr/rss/ana-sayfa
            URLConnection urlc = url.openConnection();
            urlc.addRequestProperty("User-Agent", "firefox");
            InputStream inputStream = callWebSErvice(feedUrl);

            // for java.net exception
            System.setProperty("http.keepAlive", "false");

            xr.setContentHandler(this);

            String stringFromInputStream = getStringFromInputStream(inputStream);
           // stringFromInputStream = excludeTurkishChar(stringFromInputStream);

            Log.d("StringFromInputStream: ",stringFromInputStream);
            String unescapeHtml4 = StringEscapeUtils.unescapeHtml4(stringFromInputStream);
            Log.d("unescapeHtml4 :  ",unescapeHtml4);

            InputSource inputSource = new InputSource();


           // inputSource.setEncoding("ISO-8859-9");
            inputSource.setCharacterStream(new StringReader(stringFromInputStream));
        //   InputSource inputSource=new InputSource(inputStream);
            xr.parse(inputSource);





		} catch (IOException e) {
			Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
		} catch (SAXException e) {
			//Log.e("RSS Handler SAX", e.toString());
            e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Log.e("RSSHandlerParserConfig", e.toString());
		}
		
		return articleList;
	}

    private String excludeTurkishChar(String stringFromInputStream) {
        stringFromInputStream = stringFromInputStream.replaceAll("ı", "i");
        stringFromInputStream = stringFromInputStream.replaceAll("ş", "s");
        stringFromInputStream = stringFromInputStream.replaceAll("Ş", "S");
        stringFromInputStream = stringFromInputStream.replaceAll("Ç", "C");
        stringFromInputStream = stringFromInputStream.replaceAll("ç", "c");
        stringFromInputStream = stringFromInputStream.replaceAll("ğ", "g");
        stringFromInputStream = stringFromInputStream.replaceAll("ü", "u");
        stringFromInputStream = stringFromInputStream.replaceAll("Ü", "U");
        stringFromInputStream = stringFromInputStream.replaceAll("ö", "o");
        stringFromInputStream = stringFromInputStream.replaceAll("Ö", "O");
        return stringFromInputStream;
    }


    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }


  private InputStream callWebSErvice(String serviceURL){
        // http get client
        HttpClient client=new DefaultHttpClient();
        HttpGet getRequest=new HttpGet();

        try {
            // construct a URI object
            getRequest.setURI(new URI(serviceURL));
        } catch (URISyntaxException e) {
            Log.e("URISyntaxException", e.toString());
        }

        // buffer reader to read the response
        BufferedReader in=null;
        // the service response
        HttpResponse response=null;
        try {
            // execute the request
            response = client.execute(getRequest);
        } catch (ClientProtocolException e) {
            Log.e("ClientProtocolException", e.toString());
        } catch (IOException e) {
            Log.e("IO exception", e.toString());
        }
        if(response!=null)
            try {
                HttpEntity entity = response.getEntity();
                return entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        else
            return null;

      return null;

    }


}
