package com.ame.armymax;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ame.armymax.model.DataUser;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import eu.janmuller.android.simplecropimage.CropImage;

public class SignupActivity extends Activity {

	Context context;
	Activity act;

	Button signupVdomax;
	EditText textFName;
	EditText textLName;
	EditText txtUsername;
	EditText txtPassword;
	EditText txtEmail;
	ImageView avatar;

	String fname;
	String lname;
	String user;
	String pass;
	String email;

	AQuery aq;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_vm);

		context = this;
		act = this;
		DataUser.context = context;

		aq = new AQuery(act);

		avatar = (ImageView) findViewById(R.id.avatar);
		textFName = (EditText) findViewById(R.id.firstName);
		textLName = (EditText) findViewById(R.id.lastName);
		txtEmail = (EditText) findViewById(R.id.emailSignup);
		txtUsername = (EditText) findViewById(R.id.username);
		txtPassword = (EditText) findViewById(R.id.password);

		avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectImage();
			}
		});

		signupVdomax = (Button) findViewById(R.id.signUpButton);
		signupVdomax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				fname = textFName.getText().toString();
				lname = textLName.getText().toString();
				user = txtUsername.getText().toString();
				pass = txtPassword.getText().toString();
				email = txtEmail.getText().toString();

				Map<String, Object> params = new HashMap<String, Object>();

				if (user.length() >= 8 && isEmailValid(email)) {

					params.put("firstname", fname);
					params.put("lastname", lname);
					params.put("username", user);
					params.put("password", pass);
					params.put("email", email);
					params.put("cname", 1);
					params.put("cemail", 1);
					params.put("cpass", 1);
					params.put("cfname", 1);
					params.put("clname", 1);

					String signUpUrl = "https://api.vdomax.com/service/signup/mobile/";

					aq.ajax(signUpUrl, params, JSONObject.class,
							new AjaxCallback<JSONObject>() {

								int loginStatus;

								@Override
								public void callback(String url,
										JSONObject json, AjaxStatus status) {
									if (json != null) {
										Log.e("jsonreg", json.toString());
										loginStatus = json.optInt("status");
										if (loginStatus == 2001) {
											DataUser.VM_USER_TOKEN = json
													.optString("token");
											DataUser.VM_USER_ID = json
													.optInt("UserID") + "";
											DataUser.VM_USER_NAME = json
													.optString("username");
											;
											DataUser.VM_NAME = json
													.optString("Name");
											// DataUser.VM_USER_ID =
											// json.optString("userID");
											DataUser.VM_USER_STREAM = DataUser.STREAMER
													+ DataUser.VM_USER_NAME;

											Toast.makeText(context,
													json.optString("msg"),
													Toast.LENGTH_SHORT).show();

											uploadAvatar();

											/*
											 * Intent finishSignup = new Intent(
											 * SignupActivity.this,
											 * MainActivity.class);
											 * startActivity(finishSignup);
											 */

										} else {
											Toast.makeText(context,
													json.optString("msg"),
													Toast.LENGTH_SHORT).show();

										}
									} else {
										Toast.makeText(context,
												"something wrong",
												Toast.LENGTH_SHORT).show();
									}

								}
							});

					/*
					 * Intent i = new Intent(SignupFacebookActivity.this,
					 * FacebookAuthActivity.class);
					 * 
					 * i.putExtra("STATE", "signinfb"); i.putExtra("USERNAME",
					 * user); i.putExtra("PASSWORD", pass); i.putExtra("EMAIL",
					 * email); startActivity(i);
					 */
				} else {
					boolean cond1 = user.length() >= 8;
					boolean cond2 = true;
					boolean cond3 = isEmailValid(email);
					Toast.makeText(context,
							cond1 + " " + cond2 + " " + cond3 + " ",
							Toast.LENGTH_LONG).show();
					Toast.makeText(context, user + " " + email + " " + pass,
							Toast.LENGTH_LONG).show();
				}

			}

		});
	}

	public void uploadAvatar() {
		String url = "https://api.vdomax.com/service/updateProfileAvatar/mobile";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", DataUser.VM_USER_TOKEN);
		params.put("userID", DataUser.VM_USER_ID);
		params.put("pos", "0::0::0::0::0::0");
		params.put("photo", tempFile);
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setInverseBackgroundForced(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setTitle("Initializing for " + DataUser.VM_USER_FB_FNAME + " "
				+ DataUser.VM_USER_FB_LNAME);
		aq.progress(dialog).ajax(url, params, JSONObject.class, context,
				"uploadAvatarCb");

	}

	private void setAvatar(String path) {
		Bitmap b = BitmapFactory.decodeFile(path);
		avatar.setImageBitmap(b);
	}

	public static Bitmap RotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);
	}

	public void uploadAvatarCb(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (jo != null) {
			if (jo.optInt("status") == 4101) {

				Intent finishSignup = new Intent(SignupActivity.this,
						MainActivity.class);
				startActivity(finishSignup);

			}
		}
	}

	File tempFile;
	private static final int REQUEST_TAKE_PHOTO = 1;
	private static final int REQUEST_CHOOSE_PHOTO = 2;
	private static final int REQUEST_CODE_CROP_IMAGE = 3;
	
	private static final int PHOTO_SIZE_WIDTH = 200;
	private static final int PHOTO_SIZE_HEIGHT = 200;

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");

					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					// intent.putExtra("crop", "true");
					startActivityForResult(intent, REQUEST_TAKE_PHOTO);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setDataAndType(
							MediaStore.Images.Media.INTERNAL_CONTENT_URI,
							"image/*");
					intent.setType("image/*");
					//intent.putExtra("crop", "true");
					intent.putExtra("scale", true);
					intent.putExtra("aspectX", 1);
					intent.putExtra("aspectY", 1);
					intent.putExtra("outputX", PHOTO_SIZE_WIDTH);
					intent.putExtra("outputY", PHOTO_SIZE_HEIGHT);
					startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void startCropImage(String path) {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, path);
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Toast.makeText(context, requestCode + "", Toast.LENGTH_SHORT).show();
		if (resultCode == Activity.RESULT_OK) {
			File f = new File(Environment.getExternalStorageDirectory()
					.toString());
			for (File temp : f.listFiles()) {
				if (temp.getName().equals("temp.jpg")) {
					f = temp;
					break;
				}
			}

			if (requestCode == REQUEST_TAKE_PHOTO) {

				try {
					Bitmap bm;
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					tempFile = f;
					bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
							btmapOptions);

					String path = tempFile.getAbsolutePath();
					//setAvatar(path);
					startCropImage(path);

					f.delete();
					OutputStream fOut = null;
					File file = new File(path);
					try {
						fOut = new FileOutputStream(file);
						bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == REQUEST_CHOOSE_PHOTO) {

				Uri selectedImageUri = data.getData();

				// this one
				String path = getRealPathFromURI(context, selectedImageUri);

				tempFile = new File(path);
				//setAvatar(path);
				startCropImage(path);

			} else if (requestCode == REQUEST_CODE_CROP_IMAGE) {
				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				if (path == null) {
					return;
				}
				//Toast.makeText(context, "mypath:" + path, Toast.LENGTH_LONG).show();
				setAvatar(path);
				
				
				
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	public String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
}
