package co.uk.goeuro;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.goeuro.utils.Utils;

public class MainActivity extends FragmentActivity implements OnClickListener {

	@SuppressWarnings("unused")
	private static final String TAG = MainActivity.class.getSimpleName();
	private Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		View view = findViewById(R.id.changeDate);
		view.setOnClickListener(this);
		view = findViewById(R.id.search);
		view.setOnClickListener(this);

		calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		String toSet = day + "." + month + "." + year;
		TextView date = (TextView) findViewById(R.id.dateTextView);
		date.setText(toSet);

		boolean amIconnected = Utils.isConnected(this);
		if (!amIconnected)
			Toast.makeText(this, R.string.no_coonection, Toast.LENGTH_SHORT)
					.show();

	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.changeDate:
			DialogFragment newFragment = new DatePickerDialogFragment() {
				public void onDateSet(android.widget.DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {

					calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					calendar.set(Calendar.MONTH, monthOfYear);
					calendar.set(Calendar.YEAR, year);

					int month = monthOfYear + 1;
					String toSet = dayOfMonth + "." + month + "." + year;
					TextView date = (TextView) findViewById(R.id.dateTextView);
					date.setText(toSet);

				};
			};

			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);

			Bundle bundle = new Bundle();
			bundle.putInt(Utils.BUNDLE_DAY, day);
			bundle.putInt(Utils.BUNDLE_MONTH, month);
			bundle.putInt(Utils.BUNDLE_YEAR, year);
			newFragment.setArguments(bundle);
			newFragment.show(getSupportFragmentManager(), "datePicker");
			break;

		case R.id.search:
			Toast.makeText(this, R.string.search_error, Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}
	}

}
