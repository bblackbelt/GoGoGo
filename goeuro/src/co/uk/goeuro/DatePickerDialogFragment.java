package co.uk.goeuro;

import java.util.Calendar;

import co.uk.goeuro.utils.Utils;


import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerDialogFragment extends DialogFragment implements
		OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		int year = 0;
		int month = 0;
		int day = 0;
		Bundle arguments = getArguments();
		if (arguments == null) {

			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		} else {
			year = arguments.getInt(Utils.BUNDLE_YEAR);
			month = arguments.getInt(Utils.BUNDLE_MONTH);
			day = arguments.getInt(Utils.BUNDLE_DAY);
		}
		
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
	}

}