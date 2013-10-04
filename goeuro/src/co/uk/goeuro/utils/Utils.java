package co.uk.goeuro.utils;

import org.json.JSONException;
import org.json.JSONObject;

import co.uk.goeuro.model.Coordinate;
import co.uk.goeuro.model.Station;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utils {

	private static final String TAG = Utils.class.getSimpleName();
	private static final String BASE_URL = "http://pre.dev.goeuro.de:12345/api/v1/";
	private static final String LOCATION_URL = BASE_URL
			+ "suggest/position/en/name/%s";

	public static final String JSONKEY_RESULTS = "results";
	public static final String JSONKEY_STATIONBOARD = "stationboard";

	private static final String JSONKEY_ID = "_id";
	private static final String JSONKEY_NAME = "name";
	private static final String JSONKEY_GEOPOSITION = "geo_position";
	private static final String JSONKEY__TYPE = "_type";
	private static final String JSONKEY_TYPE = "type";
	private static final String JSONKEY_LATITUDE = "latitude";
	private static final String JSONKEY_LONGITUDE = "longitude";

	public static final String CONTENTPROVIDE_DESTIONATION = "destination";
	public static final String CONTENTPROVIDE_DEPARTURE = "departure";
	public static final String CONTENTPROVIDE_CATEGORY = "category";
	public static final String CONTENTPROVIDE_ID = "_id";
	
	public static final String BUNDLE_DAY = "day";
	public static final String BUNDLE_MONTH = "month";
	public static final String BUNDLE_YEAR = "year";

	public static String getLocationURL(String query) {
		String locationURL = String.format(LOCATION_URL, query);
		Log.i(TAG, locationURL);
		return locationURL;
	}

	public static Station getStationFrom(JSONObject station) {

		if (station == null)
			return null;

		try {
			JSONObject coordinate = station.getJSONObject(JSONKEY_GEOPOSITION);
			String x = coordinate.getString(JSONKEY_LATITUDE);
			String y = coordinate.getString(JSONKEY_LONGITUDE);

			Coordinate coordinateObj = new Coordinate(x, y);
			String id = station.getString(JSONKEY_ID);
			String _type = station.getString(JSONKEY__TYPE);
			String type = station.getString(JSONKEY_TYPE);
			String name = station.getString(JSONKEY_NAME);
			return new Station(id, name, _type, type, coordinateObj);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return (activeNetwork != null && activeNetwork
				.isConnectedOrConnecting());
	}

	private Utils() {
	}
}
