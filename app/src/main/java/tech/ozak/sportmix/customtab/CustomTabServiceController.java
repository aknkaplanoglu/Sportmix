package tech.ozak.sportmix.customtab;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

/**
 * Created by Akin Kaplanoglu on 12/13/2016.
 * Elysium Ltd
 * akn.kaplanoglu@gmail.com
 */

public class CustomTabServiceController extends CustomTabsServiceConnection {

    private static final String PKG_NAME_CHROME = "com.android.chrome";

    private WeakReference<Context> mContextWeakRef;
    private String mWebsiteUrl;
    private CustomTabsSession mCustomTabsSession;

    public CustomTabServiceController(Context context, String websiteUrl) {
        mContextWeakRef = new WeakReference<Context>(context);
        mWebsiteUrl = websiteUrl;
    }

    @Override
    public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {

        if (customTabsClient != null) {
            customTabsClient.warmup(0L);

            // Create a new session
            mCustomTabsSession = customTabsClient.newSession(null);

            // Let the session know that it may launch a URL soon
            if (!TextUtils.isEmpty(mWebsiteUrl)) {
                Uri uri = Uri.parse(mWebsiteUrl);
                if (uri != null && mCustomTabsSession != null) {

                    // If this returns true, custom tabs will work,
                    // otherwise, you need another alternative if you don't want the user
                    // to be launched out of the app by default
                    mCustomTabsSession.mayLaunchUrl(uri, null, null);
                }
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mCustomTabsSession = null;
    }

    public void bindCustomTabService() {
        Context ctx = mContextWeakRef.get();
        if (ctx != null) {
            CustomTabsClient.bindCustomTabsService(ctx, PKG_NAME_CHROME, this);
        }
    }

    public void unbindCustomTabService() {
        Context ctx = mContextWeakRef.get();
        if (ctx != null) {
            ctx.unbindService(this);
        }
    }
}