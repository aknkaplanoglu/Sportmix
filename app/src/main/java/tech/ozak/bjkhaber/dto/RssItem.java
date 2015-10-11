package tech.ozak.bjkhaber.dto;

/**
 * This code encapsulates RSS item data.
 * Our application needs title and link data.
 * 
 * @author ITCuties
 *
 */
public class RssItem {
	
	// item title
	private String title;
	// item link
	private String link;
    // item's post date
    private String postDate;
    //
    private String postThumbUrl;

    public String getPostThumbUrl() {
        return postThumbUrl;
    }

    public void setPostThumbUrl(String postThumbUrl) {
        this.postThumbUrl = postThumbUrl;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return title;
	}
	
}
