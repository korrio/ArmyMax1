package com.androidbegin.armymax.chat;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ame.armymax.R;

public class DialogCreateGroupChat extends Dialog implements
		android.view.View.OnClickListener {
	private EditText edtNameGroup;
	private Button btOK;

	public DialogCreateGroupChat(Context context) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_dialog_namegroup);
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		setTitle("สร้างห้อง");

		initUI();

	}

	void initUI() {
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(this.getWindow().getAttributes());
		lp.width = 700;
		lp.height = 450;
		this.getWindow().setAttributes(lp);
		
		edtNameGroup = (EditText) findViewById(R.id.edtNameGroup);
		btOK = (Button) findViewById(R.id.btOK);
		btOK.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btOK) {
			String nameGroup = edtNameGroup.getText().toString().trim();
			if (nameGroup.length() > 0) {

				DialogSelectFriendsForChatGroup SelectChatGroup = new DialogSelectFriendsForChatGroup(
						getContext(), nameGroup);
				this.dismiss();
				SelectChatGroup.show();
			} else {
				Toast.makeText(getContext(), "กรุณาใส่ชื่อห้องด้วย",
						Toast.LENGTH_LONG).show();
			}

		}
	}

}
