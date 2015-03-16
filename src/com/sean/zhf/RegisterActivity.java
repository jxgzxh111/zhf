package com.sean.zhf;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;  
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;  
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sean.zhf.GlobalFunction;
  
public class RegisterActivity extends Activity {  
	private EditText register_email;
	private EditText register_passwd;
	private EditText reregister_passwd;
	private EditText register_invite_email;
	private String retStr;
	private SharedPreferences sp;  // 做安卓轻量级的key value存储
	
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.register_layout);  
        
        register_email = (EditText)findViewById(R.id.email_text);
        register_passwd = (EditText)findViewById(R.id.pwd_text);
        reregister_passwd = (EditText)findViewById(R.id.pwd_text2);
        register_invite_email = (EditText)findViewById(R.id.invite_email);   
        sp = RegisterActivity.this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        
        // 返回按钮
        Button backBtn = (Button)findViewById(R.id.btn_register_back);
        backBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
        	
        });
        
        // 注册按钮
        Button registerBtn = (Button)findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(!CheckEdit())
				{
					return;
				}
				
				// 拿注册时间
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
				String date = sDateFormat.format(new Date(System.currentTimeMillis()));   
				
				// send register request
				boolean issuccess = false;
                String httpUrl="http://116.228.240.106/register.php?email="+register_email.getText().toString().trim()+
                				"&pwd="+register_passwd.getText().toString().trim()+
                				"&inviteEmail="+register_invite_email.getText().toString().trim()+
                				"&regTime="+date.trim();
                HttpGet httpRequest = new HttpGet(httpUrl);
                try
                {
	                 //取得HttpClient对象
	                 HttpClient httpclient = new DefaultHttpClient();
	                 //请求HttpClient，取得HttpResponse
	                 HttpResponse httpResponse = httpclient.execute(httpRequest);
	                 //请求成功
	                 if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	                 {
		                  //取得返回的字符串
		                  String strResult = EntityUtils.toString(httpResponse.getEntity());
		                  if(strResult.equals("0"))
		                  {
		                	  strResult = "注册成功";
		                	  issuccess = true;
		                  }
		                  else if(strResult.equals("-1"))
		                  {
		                	  strResult = "注册失败，此邮箱账号已注册！";
		                  }
		                  else if(strResult.equals("-2"))
		                  {
		                	  strResult = "系统繁忙，请稍候";
		                  }
		                  retStr = strResult;
	                 }
	                 else
	                 {
	                	  retStr = "请求错误!";
	                 }
                }
                catch (ClientProtocolException e)
                {
                	retStr = e.getMessage().toString();
                }
                catch (IOException e)
                {
                	retStr = e.getMessage().toString();
                }
                catch (Exception e)
                {
                	retStr = e.getMessage().toString();
                }   
                Toast.makeText(RegisterActivity.this, retStr, Toast.LENGTH_SHORT).show();
                
                if(issuccess == true)
		        {
                	// 本地存储账号密码
    				Editor editor = sp.edit();  
                    editor.putString("USER_EMAIL",register_email.getText().toString());  
                    editor.putString("USER_PWD",register_passwd.getText().toString());  
                    editor.commit();  
		        	ChangeToMainWindow();
		        }
			}
        	
        });
    } 
    
    public boolean CheckEdit()
    {
    	if(!GlobalFunction.btnValidateEmailAddress(register_email.getText().toString()))
		{
    		Toast.makeText(RegisterActivity.this, "Email地址无效。", Toast.LENGTH_SHORT).show();
		}
    	else if(!register_passwd.getText().toString().trim().equals(reregister_passwd.getText().toString().trim()))
    	{
    		Toast.makeText(RegisterActivity.this, "两次密码输入不一致。", Toast.LENGTH_SHORT).show();
    	}
    	else if(register_passwd.getText().toString().trim().length() < 6 || register_passwd.getText().toString().trim().length() > 16)
    	{
    		Toast.makeText(RegisterActivity.this, "密码长度必须在6~16位之间。", Toast.LENGTH_SHORT).show();
    	}
    	else if(reregister_passwd.getText().toString().trim().length() < 6 || reregister_passwd.getText().toString().trim().length() > 16)
    	{
    		Toast.makeText(RegisterActivity.this, "密码长度必须在6~16位之间。", Toast.LENGTH_SHORT).show();
    	}
    	else if((!register_invite_email.getText().toString().trim().equals("")) && !GlobalFunction.btnValidateEmailAddress(register_invite_email.getText().toString()))
    	{
    		Toast.makeText(RegisterActivity.this, "邀请人账号格式错误。", Toast.LENGTH_SHORT).show();
    	}
    	else
    	{
    		return true;
    	}
    	
    	return false;
    }    
    
    public void ChangeToMainWindow()
    {
    	Intent intent = new Intent();
		intent.setClass(RegisterActivity.this, MainActivity.class);
		RegisterActivity.this.startActivity(intent);
		RegisterActivity.this.finish();
    }
}  

