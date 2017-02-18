package tech.ozak.sportmix.webView;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


/**
 * Created by aknka on 10/30/2016.
 */

public class Preferences {

    @VisibleForTesting
    static Boolean sReleaseNotesSeen = null;

    public static Set<String> getAllChannelPrefs(Context mContext) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        Set<String> channel_prefs = pref.getStringSet("channel_prefs", null);


        return channel_prefs;
    }

    public static void removeAllChannelPrefs(Context mContext) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.remove("channel_prefs");

        editor.commit();

    }

    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";

    public static String getUsername(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
        final String id = prefs.getString(PREFS_DEVICE_ID, "K");

        return id;
    }

    public static void setUsername(Context context, String userName) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);

        prefs.edit().putString(PREFS_DEVICE_ID, userName).commit();
    }

    public static void putLoggedIn(Context context, boolean isLogIn) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);

        prefs.edit().putBoolean("is_logged_in", isLogIn).commit();
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        return isLoggedIn;
    }

    public static Set<String> getAllFavoritesPrefs(Context mContext) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        Set<String> channel_prefs = pref.getStringSet("favorite_pref", null);


        return channel_prefs;
    }

    public static void removeAllFavoritesPrefs(Context mContext) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.remove("favorite_pref");

        editor.commit();
    }

    public static void addToChannelPreferences(Context mContext, String newChannel) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("channel_prefs", null);

        if (channel_prefs != null) {

            channel_prefs.add(newChannel);
            editor.remove("channel_prefs");
            editor.commit();
            editor.putStringSet("channel_prefs", channel_prefs);
            editor.commit();
        } else {
            Set<String> strings = new HashSet<>();
            strings.add(newChannel);
            editor.putStringSet("channel_prefs", strings);
            editor.commit();
        }


    }


    public static boolean isInPreferencesChannels(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        Set<String> channel_prefs = pref.getStringSet("channel_prefs", null);

        if (channel_prefs != null && !channel_prefs.isEmpty()) {

            if (channel_prefs.contains(feedLink)) {
                return true;
            }

        }
        return false;
    }


    public static Set<String> getAllCountryPrefs(Context mContext) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        Set<String> channel_prefs = pref.getStringSet("country_pref", null);

        return channel_prefs;
    }


    public static void addToCountryPref(Context mContext, String newChannel) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("country_pref", null);

        if (channel_prefs != null) {

            channel_prefs.add(newChannel);
            editor.remove("country_pref");
            editor.commit();
            editor.putStringSet("country_pref", channel_prefs);
            editor.commit();
        } else {
            Set<String> strings = new HashSet<>();
            strings.add(newChannel);
            editor.putStringSet("country_pref", strings);
            editor.commit();
        }

    }

    public static void removeFromCountryPreferences(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("country_pref", null);

        if (channel_prefs != null) {

            channel_prefs.remove(feedLink);
            editor.remove("country_pref");
            editor.commit();
            editor.putStringSet("country_pref", channel_prefs);
            editor.commit();
        }

    }


    public static void addToLikedPref(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("like_pref", null);

        if (channel_prefs != null) {

            channel_prefs.add(feedLink);
            editor.remove("like_pref");
            editor.commit();
            editor.putStringSet("like_pref", channel_prefs);
            editor.commit();
        } else {
            Set<String> strings = new HashSet<>();
            strings.add(feedLink);
            editor.putStringSet("like_pref", strings);
            editor.commit();
        }

    }

    public static void addToSortingPref(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("sorting_value", feedLink); // Storing string

        editor.commit(); // commit changes

    }

    public static void removeFromSortingPreferences(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("sort_pref", null);

        if (channel_prefs != null) {

            channel_prefs.remove(feedLink);
            editor.remove("sort_pref");
            editor.commit();
            editor.putStringSet("sort_pref", channel_prefs);
            editor.commit();
        }

    }

    public static String getSortPrefs(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        String sort_pref = pref.getString("sorting_value", null);// getting String

        return sort_pref != null ? sort_pref : "publishedDate";


    }

    public static void removeAllSortPrefs(Context mContext) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.remove("sort_pref");
        editor.commit();
    }




    public static void addToReadedNews(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("read_pref", null);

        if (channel_prefs != null) {

            channel_prefs.add(feedLink);
            editor.remove("read_pref");
            editor.commit();
            editor.putStringSet("read_pref", channel_prefs);
            editor.commit();
        } else {
            Set<String> strings = new HashSet<>();
            strings.add(feedLink);
            editor.putStringSet("read_pref", strings);
            editor.commit();
        }

    }


    public static void removeFirstReadedNews(Context mContext) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("read_pref", null);

        if (channel_prefs!=null && channel_prefs.size()>0){
            List<String> list = new ArrayList<>(channel_prefs);
            Set<String> subSet = new LinkedHashSet<>(list.subList(0, Math.min(list.size(), 10)));


            editor.putStringSet("read_pref", subSet);
            editor.commit();
        }


    }


    public static void removeFromMyFavoritePreferences(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("favorite_pref", null);

        if (channel_prefs != null) {

            channel_prefs.remove(feedLink);
            editor.remove("favorite_pref");
            editor.commit();
            editor.putStringSet("favorite_pref", channel_prefs);
            editor.commit();
        }

    }

    public static void addToFavorites(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("favorite_pref", null);

        if (channel_prefs != null) {

            channel_prefs.add(feedLink);
            editor.remove("favorite_pref");
            editor.commit();
            editor.putStringSet("favorite_pref", channel_prefs);
            editor.commit();
        } else {
            Set<String> strings = new HashSet<>();
            strings.add(feedLink);
            editor.putStringSet("favorite_pref", strings);
            editor.commit();
        }

    }

    public static boolean isInPreferencesFavorites(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        Set<String> channel_prefs = pref.getStringSet("favorite_pref", null);

        if (channel_prefs != null && !channel_prefs.isEmpty()) {

            if (channel_prefs.contains(feedLink)) {
                return true;
            }

        }
        return false;
    }

    public static void removeFromLikedPreferences(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("like_pref", null);

        if (channel_prefs != null) {

            channel_prefs.remove(feedLink);
            editor.remove("like_pref");
            editor.commit();
            editor.putStringSet("like_pref", channel_prefs);
            editor.commit();
        }

    }

    public static boolean isInPreferencesReaded(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        Set<String> channel_prefs = pref.getStringSet("read_pref", null);

        if (channel_prefs != null && !channel_prefs.isEmpty()) {

            if (channel_prefs.contains(feedLink)) {
                return true;
            }

        }
        return false;
    }

    public static boolean isInPreferencesLiked(Context mContext, String feedLink) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        Set<String> channel_prefs = pref.getStringSet("like_pref", null);

        if (channel_prefs != null && !channel_prefs.isEmpty()) {

            if (channel_prefs.contains(feedLink)) {
                return true;
            }

        }
        return false;
    }


    public static void removeFromChannelPref(Context mContext, String newChannel) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Set<String> channel_prefs = pref.getStringSet("channel_prefs", null);

        if (channel_prefs != null) {

            channel_prefs.remove(newChannel);
            editor.remove("channel_prefs");
            editor.commit();
            editor.putStringSet("channel_prefs", channel_prefs);
            editor.commit();
        }

    }

    public static boolean isInPreferencesChannel(Context mContext, String newChannel) {

        SharedPreferences pref = mContext.getSharedPreferences("MyChannelPref", 0); // 0 - for private mode
        Set<String> channel_prefs = pref.getStringSet("channel_prefs", null);

        if (channel_prefs != null) {

            if (channel_prefs.contains(newChannel)) {
                return true;
            }

        }
        return false;
    }



    public static void setTextSize(){

    }

    public static boolean isLanguageChanged(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean("isLanguageChanged", false);
    }


    private static void setInt(Context context, @StringRes int key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(context.getString(key), value)
                .apply();
    }


    public static void setLanguage(Context c, Integer language){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("language", language);
        editor.apply();
    }

    public static void setTextSize(Context c, Integer txtSize){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("text_size", txtSize);
        editor.apply();

    }

    public static void setTextColor(Context c, Integer txtSize){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("text_color", txtSize);
        editor.apply();
    }


    public static void setFirstTime(Context c){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("first_time", false);
        editor.apply();

    }

    public static void setChooserCompleted(Context c){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("chooser_completed", true);
        editor.apply();

    }

    public static void setNotification(Context c, boolean isNotif){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("auto_update_media", isNotif);
        editor.apply();

    }

    public static boolean isNotificationEnabled(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean("auto_update_media", true);

    }

    public static boolean isFirstTime(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean("first_time", true);
    }

    public static boolean isChooserCompleted(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean("chooser_completed", false);
    }


    public static int getTextColor(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getInt("text_color", 0);

    }


    public static String getLanguageCode(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        int language = sharedPreferences.getInt("language", 0);

        if (language == 0) {
            return "en";
        } else if (language == 1) {
            return "tr";
        }
        else if (language==2){
            return "ru";
        }
        else if (language==3){
            return "de";
        }
        else if (language==4){
            return "fr";
        }
        else {
            return Locale.getDefault().getLanguage();
        }
    }


    public static String getTextSizeCode(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        int text_size = sharedPreferences.getInt("text_size", 1);

        if (text_size == 0) {
            return "Small";
        } else if (text_size == 1) {
            return "Medium";
        }
        else if (text_size==2){
            return "Large";
        }
        else {
            return "Medium";
        }
    }


    public static int getTextSizeInt(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        int textSize = sharedPreferences.getInt("text_size", 1);

        if (textSize == 0) {
            return 13;
        } else if (textSize == 1) {
            return 17;
        } else if(textSize==2){
            return 21;
        }
        else{
            return 17;
        }
    }

    public static int getLanguagePlace(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        int language = sharedPreferences.getInt("language", 0);

       return language;
    }

    public static int getTextSizePlace(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        int language = sharedPreferences.getInt("text_size", 1);

        return language;
    }

    public static int getNotificationNumber(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        int language = sharedPreferences.getInt("notif_no", 0);

        return language;
    }

    public static void setNotifNo(Context c, int no){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("notif_no", no);
        editor.apply();

    }


    public static void setHowMany(Context c,int howMany){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("how_many", howMany);
        editor.apply();
    }

    public static int getHowMany(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getInt("how_many", 0);

    }
}
