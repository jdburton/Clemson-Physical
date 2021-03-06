/**
 * 
 */
package edu.clemson.physicaltherapy.app;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.clemson.soundrecorder.SoundRecorder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.CamcorderProfile;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

/**
 * @author jburton
 * 
 */
public abstract class DisplayActivity extends Activity
{

	// Progress Dialog
	protected ProgressDialog	pDialog					= null;

	protected boolean			autoSave				= false;
	protected int				colorTheme				= LayoutUtils.HIGHLIGHT_COLOR;

	// Camera variables.
	public static final int		IMAGE_REQUEST_CODE		= 1;
	public static final int		REQUEST_IMAGE_CAPTURE	= 2;
	public static final int		REQUEST_VIDEO_CAPTURE	= 3;

	public static final int		REQUEST_SOUND_CAPTURE	= 4;

	private Bitmap				bitmap;
	protected Uri				mediaUri;

	@Override
	/**
	 * @fn protected void onCreate(Bundle savedInstanceState)
	 * @brief Method called when activity is created. Sets the content view to activity_main. 
	 * 
	 * @param savedInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// / Preferences from
		// http://developer.android.com/guide/topics/data/data-storage.html#pref
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		// autoSave = settings.getBoolean(SettingsActivity.KEY_AUTOSAVE, false);
		// LayoutUtils.setHighlightColor(Integer.parseInt(settings.getString(SettingsActivity.KEY_HIGHLIGHT_COLOR,
		// "2")));
		// colorTheme = LayoutUtils.HIGHLIGHT_COLOR;
		// String urlBase = settings.getString(SettingsActivity.KEY_BASEURL,
		// null);
		// if (urlBase == null)
		// {
		// displayMessageDialog("URL not set",
		// "Database URL is not set! Please set this in the preferences.");
		//
		// }
		// else
		// {
		// DatabaseObject.setBaseUrl(urlBase);
		// }
		try
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				// Show the Up button in the action bar.
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		catch (java.lang.NullPointerException npe)
		{

		}

	}

	/**
	 * @fn public void displayToast(String message)
	 * @brief Displays a popup "Toast" message to the user. Displaying toasts
	 *        from
	 *        http://developer.android.com/guide/topics/ui/notifiers/toasts.html
	 * @param message
	 *            Message to display
	 */
	public void displayToast(String message)
	{
		Context context = this.getApplicationContext();
		LayoutUtils.displayToast(context, message);
	}

	/**
	 * @fn public void displayMessageDialog(String title, String message)
	 * @brief Displays a message dialog to the user. Displaying message dialogs
	 *        from http://www.mkyong.com/android/android-alert-dialog-example/
	 * @param message
	 *            Message to display
	 * @param title
	 *            Title of dialog
	 * 
	 */

	public void displayMessageDialog(String title, String message)
	{

		LayoutUtils.displayMessageDialog(this, title, message);
	}

	/**
	 * @fn public boolean onOptionsItemSelected(MenuItem item)
	 * @brief Handles menu item selection. Only menu item here is the
	 *        "action_about" for the info activity.
	 * @param item
	 *            MenuItem that was selected
	 * @return true
	 */
	public void onAboutButtonClick(View v)
	{

		Intent intent = new Intent(this, InfoView.class);
		startActivity(intent);

	}

	public void onDateFieldClick(View v)
	{

		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		LayoutUtils.displayDatePickerDialog(this, v, year, month, day);

	}

	//
	// Camera utilities
	//
	//

	public void dispatchSelectImageIntent()
	{
		// / Working with an image picker from
		// http://www.vogella.com/tutorials/AndroidCamera/article.html

		// create the intent
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(intent, IMAGE_REQUEST_CODE);

	}

	public void dispatchTakePictureIntent()
	{

		// / Started with this
		// http://developer.android.com/training/camera/photobasics.html
		// / But ran into this problem
		// http://stackoverflow.com/questions/13912095/java-lang-nullpointerexception-on-bundle-extras-data-getextras

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null)
		{
			// Create the File where the photo should go
			File photoFile = null;
			try
			{
				photoFile = createImageFile();
			}
			catch (IOException ex)
			{
				// Error occurred while creating the File

			}
			// Continue only if the File was successfully created
			if (photoFile != null)
			{
				mediaUri = Uri.fromFile(photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
				// takePictureIntent.putExtra("filename",
				// "file:"+photoFile.getAbsolutePath());
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
			}
		}
	}

	private File createImageFile() throws IOException
	{
		return createFile("JPEG", ".jpg", "images");
	}

	private File createFile(String prefix, String suffix, String directory)
			throws IOException
	{
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = prefix + "_" + timeStamp + "_";
		File storageDir = this.getExternalFilesDir(directory);
		File output = File.createTempFile(imageFileName, /* prefix */
				suffix, /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intent
		return output;

	}

	public void dispatchTakeVideoIntent()
	{

		// save the view.

		// / Started with this
		// http://developer.android.com/training/camera/photobasics.html
		// / But ran into this problem
		// http://stackoverflow.com/questions/13912095/java-lang-nullpointerexception-on-bundle-extras-data-getextras

		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (intent.resolveActivity(getPackageManager()) != null)
		{
			// Create the File where the photo should go
			File videoFile = null;
			try
			{
				videoFile = createHDVideoFile();
			}
			catch (IOException ex)
			{
				// Error occurred while creating the File

			}
			// Continue only if the File was successfully created
			if (videoFile != null)
			{
				mediaUri = Uri.fromFile(videoFile);

				// Would like to force SD quality video, but not supported by
				// Android Camera.
				// intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,
				// CamcorderProfile.QUALITY_480P);
				// Low quality video is useless, so is HD, so force high
				// quality.
				intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
				// takePictureIntent.putExtra("filename",
				// "file:"+photoFile.getAbsolutePath());
				startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
			}
		}
	}

	public void dispatchTakeAudioIntent()
	{

		// Record audio using the built-in voice recorder.

		// Intent intent = new
		// Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
		Intent intent = new Intent(this, SoundRecorder.class);

		// Ensure that there's a sound recording activity to handle the intent
		if (intent.resolveActivity(getPackageManager()) != null)
		{

			// Create the File where the photo should go
			File audioFile = null;
			try
			{
				audioFile = createAudioFile();
			}
			catch (IOException ex)
			{
				// Error occurred while creating the File

			}
			// Continue only if the File was successfully created
			if (audioFile != null)
			{
				mediaUri = Uri.fromFile(audioFile);
				intent.setAction(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
				intent.setType("audio/mp4");
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
				startActivityForResult(intent, REQUEST_SOUND_CAPTURE);
			}

		}
	}

	private File createHDVideoFile() throws IOException
	{
		return createFile("VID", ".mp4", getVideoSubdirectory());
	}

	private File createVideoFile() throws IOException
	{
		return createFile("VID", ".3gp", getVideoSubdirectory());
	}

	private File createAudioFile() throws IOException
	{
		return createFile("AUD", ".m4a", getAudioSubdirectory());
	}

	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("media_uri", mediaUri);
	}

	/*
	 * Here we restore the fileUri again
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		mediaUri = savedInstanceState.getParcelable("media_uri");
	}

	protected void previewVideo(VideoView vv, String path)
	{
		try
		{

			vv.setVisibility(View.VISIBLE);
			vv.setVideoPath(path);
			// start playing
			vv.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void rotateAndSetImage(ImageView imageView, String path)
	{

		// bitmap factory
		BitmapFactory.Options options = new BitmapFactory.Options();

		// downsizing image as it throws OutOfMemory Exception for larger
		// images
		options.inSampleSize = 4;

		final Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		// is this damn thing sideways? Should be in portrait mode
		// / Rotating images from
		// http://www.higherpass.com/Android/Tutorials/Working-With-Images-In-Android/3/
		System.err.println("Bitmap: w=" + bitmap.getWidth() + " h="
				+ bitmap.getHeight());
		if (bitmap.getWidth() <= bitmap.getHeight())
		{
			imageView.setImageBitmap(bitmap);
		}
		else
		{
			Matrix mat = new Matrix();
			mat.postRotate(90);
			Bitmap bMapRotate = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
			imageView.setImageBitmap(bMapRotate);
		}
	}

	protected String getVideoSubdirectory()
	{
		return "user_videos";
	}

	protected String getAudioSubdirectory()
	{
		return "audio_notes";
	}

}
