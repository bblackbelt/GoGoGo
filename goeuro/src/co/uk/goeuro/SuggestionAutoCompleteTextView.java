package co.uk.goeuro;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.ResourceCursorAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class SuggestionAutoCompleteTextView extends AutoCompleteTextView {

	/**
	 * 
	 * 
	 * An editable text view that shows completion suggestions automatically
	 * while the user is typing. The list of suggestions is displayed in a drop
	 * down menu from which the user can choose an item to replace the content
	 * of the edit box with.
	 */

	private SuggestionCursorAdater mSuggestionCursorAdater;

	public SuggestionAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		setOnItemClickListener(mOnItemClickListener);
		updateSearchAutoComplete();
	}

	private void updateSearchAutoComplete() {

		if (mSuggestionCursorAdater != null) {
			mSuggestionCursorAdater.changeCursor(null);
		}

		mSuggestionCursorAdater = new SuggestionCursorAdater(getContext());
		setAdapter(mSuggestionCursorAdater);
	}

	public void setSearchableInfo(SearchableInfo searchable) {
		updateSearchAutoComplete();
	}

	private void sendQuery(CharSequence query) {
		dismissKeyobard();
		setText(query);
	}

	private class SuggestionCursorAdater extends ResourceCursorAdapter {

		public SuggestionCursorAdater(Context context) {

			super(context, R.layout.location_row_layout, null, true);
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor cursor) {
			ViewHolder holder = (ViewHolder) arg0.getTag();

			String location = cursor
					.getString(cursor
							.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1));

			holder.location.setText(location);

		}

		/*
		 * 
		 * Runs a query with the specified constraint. This query is requested
		 * by the filter attached to this adapter. The query is provided by a
		 * FilterQueryProvider. If no provider is specified, the current cursor
		 * is not filtered and returned. After this method returns the resulting
		 * cursor is passed to changeCursor(Cursor) and the previous cursor is
		 * closed. This method is always executed on a background thread, not on
		 * the application's main thread (or UI thread.) Contract: when
		 * constraint is null or empty, the original results, prior to any
		 * filtering, must be returned.
		 * 
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.support.v4.widget.CursorAdapter#runQueryOnBackgroundThread
		 * (java.lang.CharSequence)
		 */

		public Cursor runQueryOnBackgroundThread(CharSequence constraint) {

			String query = (constraint == null) ? "" : constraint.toString();

			Cursor cursor = null;
			try {
				cursor = getSuggestions(query, 20);
				if (cursor != null) {
					cursor.getCount();
					return cursor;
				}
			} catch (RuntimeException e) {
				e.printStackTrace();
			}

			return null;

		}

		public Cursor getSuggestions(String query, int limit) {

			String authority = SearchProvider.AUTHORITY;
			if (authority == null) {
				return null;
			}

			Uri.Builder uriBuilder = new Uri.Builder().scheme(
					ContentResolver.SCHEME_CONTENT).authority(authority);

			uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY);
			String selection = "";
			String[] selArgs = null;
			if (selection != null) {
				selArgs = new String[] { query };
			}

			if (limit > 0) {
				uriBuilder.appendQueryParameter("limit", String.valueOf(limit));
			}

			Uri uri = uriBuilder.build();

			return mContext.getContentResolver().query(uri, null, selection,
					selArgs, null);
		}

		@Override
		public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
			View view = LayoutInflater.from(arg0).inflate(
					R.layout.location_row_layout, null);

			ViewHolder holder = new ViewHolder();
			holder.location = (TextView) view.findViewById(R.id.location);
			view.setTag(holder);
			return view;
		}

		class ViewHolder {
			public TextView location;
		}
	}

	private void dismissKeyobard() {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm != null) {
			imm.hideSoftInputFromWindow(getWindowToken(), 0);
		}
	}

	private void manageItemClick(int position) {
		CharSequence currentQuery = getText();
		Cursor cursor = mSuggestionCursorAdater.getCursor();
		if (cursor == null) {
			return;
		}
		if (cursor.moveToPosition(position)) {
			CharSequence newQuery = cursor
					.getString(cursor
							.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1));
			if (newQuery != null) {
				sendQuery(newQuery);
			} else {
				sendQuery(currentQuery);
			}
		} else {
			sendQuery(currentQuery);
		}
	}

	private final OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			manageItemClick(position);

		}

	};

}
