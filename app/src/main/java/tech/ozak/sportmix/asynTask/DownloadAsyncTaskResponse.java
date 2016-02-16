package tech.ozak.sportmix.asynTask;

import java.util.List;

import tech.ozak.sportmix.dto.RssItem;

/**
 * Created by ako on 17-Feb-16.
 */
public interface DownloadAsyncTaskResponse {
   public void processFinish(List<RssItem> output);
}
