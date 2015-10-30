package com.ame.armymax.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ame.armymax.R;
import com.ame.armymax.model.DataUser;
import com.ame.armymax.xwalk.XWalkChatRoomActivity;
import com.squareup.picasso.Picasso;

public class ShowCallPopUp extends Activity implements OnClickListener {  
    Button ok;  
    Button cancel;  
    boolean click = true;  
    
    TextView msgTv;
    ImageView avatar;
    
    String title;
    String msg;
    
    String session;
    String from_id;
    String type;
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
         super.onCreate(savedInstanceState);  
         setContentView(R.layout.popdialog);  
         
         title = getIntent().getStringExtra("title");
         msg = getIntent().getStringExtra("msg");
         
         String from_avatar = getIntent().getStringExtra("from_avatar");
         from_id = getIntent().getStringExtra("from_id");
         session = getIntent().getStringExtra("session");
         type = getIntent().getStringExtra("type");
         setTitle(title);  
        
         msgTv = (TextView) findViewById(R.id.msg_tv);
         avatar = (ImageView) findViewById(R.id.avatar_img);
         Picasso.with(this).load(DataUser.BASE + from_avatar).into(avatar);
         
         msgTv.setText(msg);
         ok = (Button)findViewById(R.id.buttonOk);  
         ok.setOnClickListener(this);  
         cancel = (Button)findViewById(R.id.buttonCancel);  
         cancel.setOnClickListener(this);  
    }  
    
    @Override  
    public void onClick(View view) {  
          if(view == ok) {
        	  
        	  if(type.equals("504")) {
        		  String url = "http://www.armymax.com/RTCMultiConnection/demos/phone/audio.html?session="+session+"&userid="
        					+ from_id + "&r=" + from_id;
              	  Intent callRoom = new Intent(ShowCallPopUp.this,XWalkChatRoomActivity.class);
              	  callRoom.putExtra("roomUrl", url);
              	  callRoom.putExtra("friendName", msg);
              	  startActivity(callRoom);
        	  } else {
        		  String url = "https://apprtc.appspot.com/r/"
      					+ from_id + "to" + DataUser.VM_USER_ID;
            	  Intent callRoom = new Intent(ShowCallPopUp.this,XWalkChatRoomActivity.class);
            	  Log.e("vUrl",url);
            	  callRoom.putExtra("roomUrl", url);
            	  callRoom.putExtra("friendName", msg);
            	  startActivity(callRoom);
        	  }
        	  
        	  
        	  
          }
         finish();  
    }  
}  
