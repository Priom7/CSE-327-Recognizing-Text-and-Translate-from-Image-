package com.compa.readerocr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.compa.readerocr.TranslatiorBackgroundTask.TranslatiorBackgroundTask;
import com.compa.readerocr.utils.CharDetectOCR;
import com.compa.readerocr.utils.CommonUtils;
import com.compa.readerocr.view.TouchImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static com.compa.readerocr.utils.CommonUtils.info;

/**
 * 
 * @author Sea
 *
 *///
public class RecognizeTextActivity extends Activity {
	static int REQUEST_IMAGE_CAPTURE = 1;
	static ProcessImage processImg = new ProcessImage();

	Context context=this;
	Button btnStartCamera;
	Button btnExit;
	TextView tv;
	Button Tbtn;
	Button Tbtn2; // new button for ENglish to spanish
	Button Tbtn3; // new button for English to Germany

	private String language;
	private TouchImageView image;
	private EditText recognizeResult; // We are going to use this result//

	private int sourceW = 0;
	private int sourceH = 0;
	private String lastFileName = "";
	private boolean isRecognized = false;
	private TextView txvResult;
	private TextView spch; // for speech translation result

	ProgressDialog progressBar;

	@SuppressLint("WrongViewCast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reader);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Bundle b = getIntent().getExtras();
		language = b.getString("language");
		ProcessImage.language = language;
		ProcessImage.thresholdMin =  Integer.parseInt(b.getString("threshold"));
		info("Language: "+ language + "   threshold: "+ ProcessImage.thresholdMin);

		btnStartCamera = (Button) findViewById(R.id.btnStartCamera);
		btnExit = (Button) findViewById(R.id.btnExit);
		txvResult = (TextView) findViewById(R.id.tvResult);

		tv = (TextView) findViewById(R.id.translation);
		Tbtn = (Button) findViewById(R.id.translation_btn);
		spch = (TextView) findViewById(R.id.speechResult);
		Tbtn2  = (Button) findViewById(R.id.translation_btn2);
		Tbtn3 = (Button) findViewById(R.id.translation_btn3);

		//YOYO is the new Added library functions for the animation
		YoYo.with(Techniques.TakingOff).duration(5000).repeat(4).playOn(findViewById(R.id.image2));
		YoYo.with(Techniques.Bounce).duration(5000).repeat(4).playOn(findViewById(R.id.image2));
		YoYo.with(Techniques.BounceInUp).duration(500).repeat(4).playOn(findViewById(R.id.btnSpeak));
		YoYo.with(Techniques.BounceInUp).duration(500).repeat(4).playOn(findViewById(R.id.translation_btn));

		Tbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String  textToBeTranslated = recognizeResult.getText().toString();  // text from image
				String textToBeTranslated2 = txvResult.getText().toString(); // text from speech
				String languagePair = "en-bn";  // translation option from english to bangla
				String res = Translate(textToBeTranslated,languagePair);  // image test translation
				String res2 = Translate(textToBeTranslated2,languagePair); // speech texts are sending for translation
				tv.setText(res); // image text result
				spch.setText(res2); // speech text result

				//animation is added on the result
				YoYo.with(Techniques.Wave).duration(500).repeat(2).playOn(tv);
				YoYo.with(Techniques.ZoomInUp).duration(500).repeat(2).playOn(spch);
				YoYo.with(Techniques.Wave).duration(5000).repeat(4).playOn(findViewById(R.id.image2));

			}

		});




		// This part is done by Sharif Ahmed ID: 1410286042

	Tbtn2.setOnClickListener(new View.OnClickListener() { // new onclick method for English to spanish
            @Override
            public void onClick(View view) {
                String  textToBeTranslated = recognizeResult.getText().toString();  // text from image
                String textToBeTranslated2 = txvResult.getText().toString(); // text from speech
                String languagePair = "en-es";  // translation option from english to Espanish
                String res = Translate(textToBeTranslated,languagePair);  // image test translation
                String res2 = Translate(textToBeTranslated2,languagePair); // speech texts are sending for translation
                tv.setText(res); // image text result
                spch.setText(res2); // speech text result

                //animation is added on the result
                YoYo.with(Techniques.Wave).duration(500).repeat(2).playOn(tv);
                YoYo.with(Techniques.ZoomInUp).duration(500).repeat(2).playOn(spch);
                YoYo.with(Techniques.Wave).duration(5000).repeat(4).playOn(findViewById(R.id.image2));
            }

        });



	// This part is done by Nafees Sadnan Joy ID: 1511023642

		Tbtn3.setOnClickListener(new View.OnClickListener() { // new onclick method for English to spanish
			@Override
			public void onClick(View view) {
				String  textToBeTranslated = recognizeResult.getText().toString();  // text from image
				String textToBeTranslated2 = txvResult.getText().toString(); // text from speech
				String languagePair = "en-de";  // translation option from english to german
				String res = Translate(textToBeTranslated,languagePair);  // image test translation
				String res2 = Translate(textToBeTranslated2,languagePair); // speech texts are sending for translation
				tv.setText(res); // image text result
				spch.setText(res2); // speech text result

				//animation is added on the result
				YoYo.with(Techniques.Wave).duration(500).repeat(2).playOn(tv);
				YoYo.with(Techniques.ZoomInUp).duration(500).repeat(2).playOn(spch);
				YoYo.with(Techniques.Wave).duration(5000).repeat(4).playOn(findViewById(R.id.image2));
			}

		});




		btnStartCamera.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					takePicture();
				}
				return false;
			}
		});

		btnExit.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					existApp();
				}
				return false;
			}
		});

		recognizeResult = (EditText) findViewById(R.id.recognize_result);
		image = (TouchImageView) findViewById(R.id.grid_img);
		image.setScaleType(ScaleType.CENTER_INSIDE);

		if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			new InitTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new InitTask().execute();
		}


	}

// Method for translating tasks . this will recive which string should be translated from which language to which
	public String Translate(String textToBeTranslated, String languagePair) {


		TranslatiorBackgroundTask translatorBackgroundTask= new TranslatiorBackgroundTask(context);
		String translationResult = null;
		try {
			translationResult = translatorBackgroundTask.execute(textToBeTranslated ,languagePair).get();
			return translationResult;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		//Log.d("Translation Result",translationResult); // Logs the result in Android Monitor
		// tv.setText(translationResult);


		return null;
	}

// Methods for getting speech input from the user
	public void getSpeechInput(View view) {

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(intent, 10);  // sending reqst code 10
		} else {
			Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recognize, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void takePicture() {
		Intent takePicIntent = new Intent(RecognizeTextActivity.this, AndroidCamera.class);
		lastFileName = CommonUtils.APP_PATH + "capture" + System.currentTimeMillis() + ".jpg";
		takePicIntent.putExtra("output", lastFileName);
		info(lastFileName);
		startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

// checking if the request case code is 10 means rqst for speech inputs
		switch (requestCode) {
			case 10:
				if (resultCode == RESULT_OK && data != null) {
					ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					txvResult.setText(result.get(0));
				}
				break;
		}

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			Bitmap imageBitmap = BitmapFactory.decodeFile(lastFileName, options);

			if (imageBitmap == null) {
				// Try again
				isRecognized = false;
				image.setImageBitmap(imageBitmap);
				hideProcessBar();
				dialogBox("Can not recognize sheet. Please try again", "Retry", "Exist", true);
				return;
			}
			final Bitmap finalImageBitmap = imageBitmap.getWidth() > imageBitmap.getHeight()
					? rotateBitmap(imageBitmap, 90) : imageBitmap;

			int top = data.getIntExtra("top", 0);
			int bot = data.getIntExtra("bot", 0);
			int right = data.getIntExtra("right", 0);
			int left = data.getIntExtra("left", 0);

			image.setImageBitmap(finalImageBitmap);
			displayResult(finalImageBitmap, top, bot, right, left);

		}
	}
// this is the result///////////////////////////////////////////////////////////////////////////////////////////////////
	public void displayResult(Bitmap imageBitmap, int top, int bot, int right, int left) {
		info("Origin size: " + imageBitmap.getWidth() + ":" + imageBitmap.getHeight());
		// Parser
		recognizeResult.setText("");
		if (processImg.parseBitmap(imageBitmap, top, bot, right, left)) {
			// TODO: set result
			recognizeResult.setText(processImg.recognizeResult);
			// TODO: write result to image
			// image.setImageBitmap(toBitmap(processImg.drawAnswered(numberAnswer)));
			isRecognized = true;
			hideProcessBar();
		} else {
			// Try again
			isRecognized = false;
			image.setImageBitmap(imageBitmap);
			hideProcessBar();
			dialogBox("Can not recognize sheet. Please try again", "Retry", "Exist", true);
		}
	}

	public Bitmap rotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public void dialogBox(String message, String bt1, String bt2, final boolean flagContinue) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setPositiveButton(bt1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (flagContinue) {
					takePicture();
				}
			}
		});

		if (bt2 != "") {
			alertDialogBuilder.setNegativeButton(bt2, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					existApp();
					// return false;
				}
			});
		}

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void existApp() {
		CommonUtils.cleanFolder();
		this.finish();
	}

	public void showProgressBar(String title, String message) {
		progressBar = ProgressDialog.show(this, title, message, false, false);
	}

	public void hideProcessBar() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (progressBar != null && progressBar.isShowing()) {
					progressBar.dismiss();
				}
			}
		});
	}

	private class InitTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... data) {
			try {
				CharDetectOCR.init(getAssets());
				return "";
			} catch (Exception e) {
				Log.e("COMPA", "Error init data OCR. Message: " + e.getMessage());
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}


}
