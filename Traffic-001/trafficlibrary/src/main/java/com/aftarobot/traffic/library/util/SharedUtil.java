package com.aftarobot.traffic.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.aftarobot.traffic.library.data.CityDTO;
import com.aftarobot.traffic.library.data.CountryDTO;
import com.aftarobot.traffic.library.data.DepartmentDTO;
import com.aftarobot.traffic.library.data.DeviceDTO;
import com.aftarobot.traffic.library.data.TicketDTO;
import com.aftarobot.traffic.library.data.UserDTO;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by aubreymalabie on 7/13/16.
 * <p>
 * Updated: Ishmael Makitla - 23/10/2016
 */

public class SharedUtil {

    //always good to have Shared-Preference Keys Declared as Static Finals Here
    public static final String SHARED_PREF_KEY_ASSOCIATION_CONTACTS = "associationContacts";
    public static final String SHARED_PREF_KEY_ASSOCIATION_CONTACTS_EXPIRY = "associationContacts_expiry";

    public static final String TAG = SharedUtil.class.getSimpleName();
    private final static Gson gson = new Gson();


    public static void saveUser(UserDTO user, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("user", gson.toJson(user));
        ed.commit();
        Log.d(TAG, "saveUser: " + user.getEmail());
    }

    public static UserDTO getUser(Context ctx) {

        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("user", null);
        if (json == null) {
            return null;
        }
        UserDTO u = gson.fromJson(json, UserDTO.class);
        if (u != null) {
            Log.w(TAG, "User retrived from cache: ".concat(u.getFullName()));
        }
        return u;
    }

    public static void saveCity(CityDTO city, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("cityxx", gson.toJson(city));
        ed.commit();
        Log.d(TAG, "$$$$$$$$$$$$$$$$ saveCity: " + city.getCityName());
    }

    public static CityDTO getCity(Context ctx) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("cityxx", null);
        if (json == null) {
            return null;
        }
        try {
            CityDTO r = gson.fromJson(json, CityDTO.class);
            Log.e(TAG, "++++++++++++++++ getCity: retrieved: " + r.getCityName());
            return r;
        } catch (Exception e) {

        }
        return null;
    }


    public static void saveCloudMsgToken(String token, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("token", token);
        ed.commit();
        Log.d(TAG, "saveCloudMsgToken " + token);
    }

    public static String getCloudMsgToken(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String token = sp.getString("token", null);
        return token;
    }


    public static void saveFCMStatus(boolean statusIsOK, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("fcm", statusIsOK);
        ed.commit();
        Log.d(TAG, "saveFCMStatus: " + statusIsOK);
    }

    public static boolean getFCMStatus(Context ctx) {
        if (ctx == null) return false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean ok = sp.getBoolean("fcm",false);
        return ok;
    }


    public static void saveAccount(String token, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("account", token);
        ed.commit();
        Log.d(TAG, "saveAccount: " + token);
    }

    public static String getAccount(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String token = sp.getString("account", null);
        if (token == null) {
            token = "aubrey@aftarobot.com";
        }
        return token;
    }

    public static void saveTime(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putLong("date", new Date().getTime());
        ed.commit();
        Log.d(TAG, "saveTime " + new Date().toString());
    }

    public static long getTime(Context ctx) {
        if (ctx == null) return 0;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        long token = sp.getLong("date", 0);
        return token;
    }

    public static void saveLastArrivalRecord(String token, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("token", token);
        ed.commit();
        Log.d(TAG, "saveCloudMsgToken " + token);
    }

    public static String geLastArrivalRecord(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String token = sp.getString("token", null);
        return token;
    }

    public static void saveRouteServiceSwitch(boolean busy, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("busy", busy);
        ed.commit();
        Log.d(TAG, "saveRouteServiceSwitch: " + busy);
    }

    public static boolean getRouteServiceSwitch(Context ctx) {
        if (ctx == null) return false;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean token = sp.getBoolean("busy", false);
        return token;
    }

    public static void saveDevice(DeviceDTO device, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("device", gson.toJson(device));
        ed.commit();
    }

    public static DeviceDTO getDevice(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("device", null);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, DeviceDTO.class);
    }

    /**
     * Utility method that erases cached contacts that have expired.
     * The same technique can be used, for instance, to get rid of old "remembered" logins
     */
    private static void purgeExpiredCachedContacts(String sharedPrefKey, Context context) {
        if (sharedPrefKey == null || sharedPrefKey.trim().isEmpty()) {
            return;
        }

        try {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (!sharedPrefs.contains(sharedPrefKey)) {
                //trying to delete a key that does not exist -
                return;
            }
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.remove(sharedPrefKey);
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, "Error While Trying to Purge Expired Cached Data. Error Log :: ", e);
        }

    }

    public static void saveLocationRecord(double latitude, double longitude, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        LocationRecord r = new LocationRecord(latitude, longitude, new Date().getTime());
        String json = gson.toJson(r);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("location", json);
        ed.commit();
        Log.d(TAG, "saveLocationRecord: " + json);
    }

    public static LocationRecord getLocationRecord(Context ctx) {
        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("location", null);
        if (json == null) {
            return null;
        }
        LocationRecord r = gson.fromJson(json, LocationRecord.class);
        Log.d(TAG, "getLocationRecord: " + json);
        return r;
    }

    public static class LocationRecord implements Serializable {
        double latitude, longitude;
        long date;

        public LocationRecord(double latitude, double longitude, long date) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.date = date;
        }

        public Location getLocation() {
            Location loc = new Location("");
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            return loc;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }
    }

    public static void saveCountry(CountryDTO country, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        String json = gson.toJson(country);
        ed.putString("countryxx", json);
        ed.commit();
        Log.d(TAG, "#################################saveCountry" +
                " on disk: " + country.getCountryName());
    }

    public static CountryDTO getCountry(Context ctx) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("countryxx", null);
        if (json == null) {
            return null;
        }
        try {
            CountryDTO t = gson.fromJson(json, CountryDTO.class);
            Log.i(TAG, "************************* getCountry: retrieved: " + t.getCountryName());
            return t;
        } catch (Exception e) {
            Log.e(TAG, "?????????? getCountry: unable to parse country");
            return null;
        }

    }

    public static void saveTicket(TicketDTO t, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        String json = gson.toJson(t);
        ed.putString("ticket", json);
        ed.commit();
        Log.d(TAG, "################################# saveTicket" +
                " on disk: " + t.getTicketNumber());
    }

    public static TicketDTO getTicket(Context ctx) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("ticket", null);
        if (json == null) {
            return null;
        }
        try {
            TicketDTO t = gson.fromJson(json, TicketDTO.class);
            Log.i(TAG, "************************* getTicket: retrieved: " + t.getTicketNumber());
            return t;
        } catch (Exception e) {
            Log.e(TAG, "?????????? getCountry: unable to parse ticket");
            return null;
        }

    }

    public static void saveDepartmentn(DepartmentDTO ass, Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor ed = sp.edit();
        String json = gson.toJson(ass);
        ed.putString("association", json);
        ed.commit();
        Log.d(TAG, "@@@@@@@@@@@@@@@@@ saveAssociation" +
                " on disk: " + ass.getDepartmentName());
    }

    public static DepartmentDTO getDepartment(Context ctx) {

        if (ctx == null) return null;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String json = sp.getString("association", null);
        Log.d(TAG, "getAssociation: json : " + json);
        if (json == null) {
            return null;
        }
        try {
            DepartmentDTO t = gson.fromJson(json, DepartmentDTO.class);
            Log.i(TAG, "getDepartment: ---------------- retrieved: " + t.getDepartmentName());
            return t;
        } catch (Exception e) {
            return null;
        }


    }

}
//-KUDmp00nCCVZoYEM77M"