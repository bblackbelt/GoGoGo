package co.uk.goeuro;

import java.util.List;

import co.uk.goeuro.GetSuggestionRunnable.GetSuggestionResult;
import co.uk.goeuro.model.Station;
import co.uk.goeuro.utils.Utils;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class SearchProvider extends ContentProvider {

	public static final String AUTHORITY = "co.uk.goeuro.SearchProvider";

	@SuppressWarnings("unused")
	private static final String TAG = SearchProvider.class.getSimpleName();

	private static UriMatcher uriMatcher;
	private static final int SUGGESTIONS = 1;

	public static final Uri SEARCH_URI = Uri.parse("content://" + AUTHORITY
			+ "/search");

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,
				SUGGESTIONS);
	}


	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		return null;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		MatrixCursor cursor = null;
		int match = uriMatcher.match(uri);
		switch (match) {
		case SUGGESTIONS:

			final MatrixCursor mCursor = new MatrixCursor(new String[] {
					Utils.CONTENTPROVIDE_ID,
					SearchManager.SUGGEST_COLUMN_TEXT_1,
					SearchManager.SUGGEST_COLUMN_INTENT_DATA });

			String query = selectionArgs[0];
			if (query.equals(""))
				return mCursor;

			new GetSuggestionRunnable(new GetSuggestionResult() {

				@Override
				public void getResult(List<Station> stations) {
					for (int i = 0; i < stations.size(); i++) {
						String stationName = stations.get(i).name;
						mCursor.addRow(new String[] { Integer.toString(i),
								stationName, stationName });
					}
				}
			}, query).run();

			cursor = mCursor;
			break;
		default:
			break;
		}

		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		return 0;
	}

}
