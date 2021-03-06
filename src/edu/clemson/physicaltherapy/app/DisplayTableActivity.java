package edu.clemson.physicaltherapy.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import edu.clemson.physicaltherapy.R;

/**
 * 
 * @author jburton
 * 
 * @class DisplayItemActivity
 * 
 * @brief This class implements functionality for the main activity in Android
 *        Lookup. Start here.
 */

public abstract class DisplayTableActivity extends DatabaseActivity
{

	protected abstract String getTableName();

	@SuppressLint("NewApi")
	@Override
	/**
	 * @fn protected void onCreate(Bundle savedInstanceState)
	 * @brief Method called when activity is created.  
	 * 
	 * @param savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onPause()
	{
		super.onPause();

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		// if any settings have changed, redraw the table on resume.
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		// if (autoSave != settings.getBoolean(SettingsActivity.KEY_AUTOSAVE,
		// false) || colorTheme != LayoutUtils.HIGHLIGHT_COLOR )
		// {
		drawTable();
		// }
	}

	protected abstract void drawTable();

	@Override
	/**
	 *
	 * @fn public boolean onCreateOptionsMenu(Menu menu)
	 * @brief Inflate the menu; this adds items to the action bar if it is present.
	 * @param menu Meny to be created.
	 * @return true
	 */
	public boolean onCreateOptionsMenu(Menu menu)
	{

		getMenuInflater().inflate(R.menu.table_menu, menu);
		return true;
	}

	@Override
	/**
	 * @fn public boolean onOptionsItemSelected(MenuItem item)
	 * @brief Handles menu item selection. 
	 * Only menu item here is the "action_about" for the info activity.
	 * @param item MenuItem that was selected
	 * @return true  
	 */
	public boolean onOptionsItemSelected(MenuItem item)
	{

		Intent intent = null;
		// / Menu from
		// http://developer.android.com/guide/topics/ui/menus.html#options-menu
		switch (item.getItemId())
		{

			case android.R.id.home:

				NavUtils.navigateUpFromSameTask(this);
				return true;

			case R.id.action_about:
				intent = new Intent(this, InfoView.class);
				startActivity(intent);
				break;

			case R.id.action_settings:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				break;
		}
		return true;
	}

	protected String getValueFromSpinner(TableRow tr, int index)
	{
		String spinnerValue;
		try
		{
			spinnerValue = ((Spinner) tr.getChildAt(index)).getSelectedItem().toString();
		}
		catch (java.lang.ClassCastException ce)
		{
			spinnerValue = ((TextView) tr.getChildAt(index)).getText().toString();
		}

		return spinnerValue;
	}

	protected static String getStringFromTableRow(TableRow parentRow, int field)
	{
		return ((TextView) (((TableRow) parentRow).getChildAt(field))).getText().toString();
	}
}
