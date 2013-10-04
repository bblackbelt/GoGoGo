package co.uk.goeuro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.uk.goeuro.model.Station;
import co.uk.goeuro.utils.Utils;

import android.util.Log;

public class GetSuggestionRunnable implements Runnable {

	private static final String TAG = GetSuggestionRunnable.class.getSimpleName();
	
	private GetSuggestionResult mListener;
	private String query;
	

	
	public interface GetSuggestionResult {
		public void getResult(List<Station>  stations);
	}

	public GetSuggestionRunnable(GetSuggestionResult mListener, String query) {
		if (mListener == null)
			throw new IllegalArgumentException("GetSuggestionResult can not be null");
		this.mListener = mListener;
		this.query = query;
	}
	
	private List<Station> getSuggestion(String suggestion) {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

		try {

			String url = Utils.getLocationURL(suggestion);
			HttpGet getLocation = new HttpGet(url);
			HttpResponse response = null;
			try {
				response = defaultHttpClient.execute(getLocation);

				if (response == null)
					return null;

				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode != HttpStatus.SC_OK) {
					return null;
				}

				String jsonResponse = EntityUtils
						.toString(response.getEntity());

				return parseLocation(jsonResponse);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		} finally {
			if (defaultHttpClient != null)
				defaultHttpClient.getConnectionManager().shutdown();
		}
	}


	private List<Station> parseLocation(String jsonResponse) {
		List<Station> stationsRes = new ArrayList<Station>();
		try {
			JSONObject jsonObject = new JSONObject(jsonResponse);
			JSONArray stations = jsonObject
					.getJSONArray(Utils.JSONKEY_RESULTS);
			int stationsLen = stations.length();
			for (int i = 0; i < stationsLen; i++) {
				JSONObject stationJSONObject = stations.getJSONObject(i);
				Station station = Utils.getStationFrom(stationJSONObject);
				if (station == null)
					continue;

				stationsRes.add(station);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Log.i(TAG, "" + stationsRes);

		return stationsRes;
	}
	
	@Override
	public void run() {
		mListener.getResult(getSuggestion(query));
	}

}
