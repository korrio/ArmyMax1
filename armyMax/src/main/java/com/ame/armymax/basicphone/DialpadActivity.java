package com.ame.armymax.basicphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ame.armymax.R;
import com.ame.armymax.MainActivity;

import java.util.Arrays;

public class DialpadActivity extends Activity {


	private ImageButton mMenuButton;
	private ImageButton mAddButton;

	EditText numberEditText;
	ImageButton callButton;
	ImageButton deleteButton;

	Context context;

	
	String SELECTED_CODE;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		this.setContentView(R.layout.dialpad_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//String user = getIntent().getStringExtra("user");
		//String pass = getIntent().getStringExtra("pass");

		numberEditText = ((EditText) findViewById(R.id.number));
		numberEditText.setInputType(InputType.TYPE_NULL);
		
		final String[] recourseList=this.getResources().getStringArray(R.array.CountryCodes);
        final String[] list = new String[recourseList.length];
        Arrays.sort(recourseList);


        for(int i = 0 ; i < recourseList.length ; i ++) {
        	String[] code = recourseList[i].split(",");
        	String ccode = "+"+code[0];
        	list[i] = ccode;
        }

		final Spinner p = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        p.setAdapter(dataAdapter);
        p.setSelection(dataAdapter.getPosition("+66"));
        
        p.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int selectedPosition = arg2;
                String[] g=recourseList[selectedPosition].split(",");
                String correspondingCode = g[0];
                // Here is your corresponding country code
                SELECTED_CODE = correspondingCode;
                System.out.println(correspondingCode);
                Toast.makeText(context, correspondingCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            }
        });
		
		callButton = (ImageButton) this.findViewById(R.id.button_call);

		deleteButton = (ImageButton) this.findViewById(R.id.button_delete_1);

		ImageView button1 = (ImageView) this.findViewById(R.id.button1);
		ImageView button2 = (ImageView) this.findViewById(R.id.button2);
		ImageView button3 = (ImageView) this.findViewById(R.id.button3);
		ImageView button4 = (ImageView) this.findViewById(R.id.button4);
		ImageView button5 = (ImageView) this.findViewById(R.id.button5);
		ImageView button6 = (ImageView) this.findViewById(R.id.button6);
		ImageView button7 = (ImageView) this.findViewById(R.id.button7);
		ImageView button8 = (ImageView) this.findViewById(R.id.button8);
		ImageView button9 = (ImageView) this.findViewById(R.id.button9);
		ImageView button0 = (ImageView) this.findViewById(R.id.button0);
		ImageView buttonstar = (ImageView) this.findViewById(R.id.buttonstar);
		ImageView buttonpound = (ImageView) this.findViewById(R.id.buttonpound);

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("1");
			}
		});

		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("2");
			}
		});

		button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("3");
			}
		});

		button4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("4");
			}
		});

		button5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("5");
			}
		});

		button6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("6");
			}
		});

		button7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("7");
			}
		});

		button8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("8");
			}
		});

		button9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("9");
			}
		});

		button0.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("0");
			}
		});

		buttonstar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("*");
			}
		});

		buttonpound.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				numberEditText.append("#");
			}
		});

		deleteButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String str = numberEditText.getText().toString().trim();

				if (str.length() != 0) {
					str = str.substring(0, str.length() - 1);
					numberEditText.setText(str);
				}

			}
		});

		

		callButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String number = numberEditText.getEditableText().toString();
				Intent callActivity = new Intent(
						DialpadActivity.this,
						PhoneCallActivity.class);
				callActivity.putExtra("code", SELECTED_CODE);
				callActivity.putExtra("number", number);
				startActivity(callActivity);

			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.from(this).addNextIntent(upIntent)
						.startActivities();
				finish();
			} else {
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
