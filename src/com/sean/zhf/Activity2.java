package com.sean.zhf;

import android.app.Activity;  
import android.os.Bundle;  
import android.view.View;
import android.widget.Button;
  
public class Activity2 extends Activity {  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity2_layout);  
        
        Button guaBtn = (Button)findViewById(R.id.guaBtn);
        guaBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// send gua bonus
								
			}        	
        });
       
    }  
}  