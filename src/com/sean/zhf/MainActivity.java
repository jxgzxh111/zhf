package com.sean.zhf;

import android.os.Bundle;
import android.app.ActivityGroup;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements View.OnClickListener{
	
	// init definition
	private final static Class<?>[] sActivityClasses = {Activity1.class, Activity2.class, Activity3.class};
	private final static int[] sResIds = {R.id.btn1, R.id.btn2, R.id.btn3};
	private final static String[] sActivityIds = {"Activity1", "Activity2", "Activity3"};
	private RelativeLayout mViewContainer;
    private Button[] mBtns = new Button[sResIds.length];
    private int mCurId = 0;
	
	// oncreate
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_main);
		setupViews();
		
		// default view
		processViews(sResIds[0]);
	}
	
	private void setupViews() {
        mViewContainer = (RelativeLayout) findViewById(R.id.container);
        final Button[] btns = mBtns;
        for(int i=0; i< btns.length; i++) {
            btns[i] = (Button) findViewById(sResIds[i]);
            btns[i].setOnClickListener(this);
        }
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		final int id = arg0.getId();
        if(mCurId == id) {
            return ;
        }
        mCurId = id;
        processViews(id);
	}
	
	private void processViews(int rid) {
        mViewContainer.removeAllViews();
        final int index = getButtonIndex(rid);
        final View tempView = getLocalActivityManager().startActivity(sActivityIds[index],
                new Intent(this, sActivityClasses[index]).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT))
                .getDecorView();
        mViewContainer.addView(tempView);
        
        // 要重新设置子布局的属性，不然加载进去之后子布局里面的控件不能对齐
        tempView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams( 
        		android.widget.RelativeLayout.LayoutParams.MATCH_PARENT, 
        		android.widget.RelativeLayout.LayoutParams.MATCH_PARENT)); 
    }

	private int getButtonIndex(int rid) {
        final int length = sResIds.length;
        for (int i = 0; i < length; i++) {
            if (rid == sResIds[i]) {
                return i;
            }
        }
        return 0;
    }

}

