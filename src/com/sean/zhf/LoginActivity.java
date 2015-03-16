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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class LoginActivity extends Activity {  
	private EditText m_login_email;
	private EditText m_login_pwd;
	private String retStr;
	private SharedPreferences sp;  // 做安卓轻量级的key value存储
	
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.login_layout);  
        
        /////////////////////////////////
        // for test
        ChangeToMainWindow();
        /////////////////////////////////
        
        m_login_email = (EditText)findViewById(R.id.editText1);
        m_login_pwd = (EditText)findViewById(R.id.editText2);
        sp = LoginActivity.this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        
        // auto login
        if(!sp.getString("USER_EMAIL", "").equals("") && !sp.getString("USER_PWD", "").equals(""))
        {
        	SendLoginMsg(sp.getString("USER_EMAIL", ""), sp.getString("USER_PWD", ""));
        }
        
        Button registerBtn = (Button)findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				LoginActivity.this.startActivity(intent);
			}        	
        });
        
        // 开始登陆
        Button loginBtn = (Button)findViewById(R.id.button1);
        loginBtn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!CheckEdit())
				{
					return;
				}
				
				// 本地存储账号密码
				Editor editor = sp.edit();  
                editor.putString("USER_EMAIL",m_login_email.getText().toString());  
                editor.putString("USER_PWD",m_login_pwd.getText().toString());  
                editor.commit();  
                
				SendLoginMsg(m_login_email.getText().toString(), m_login_pwd.getText().toString());
			}
        });
    }  
    
    public boolean CheckEdit()
    {
    	if(!GlobalFunction.btnValidateEmailAddress(m_login_email.getText().toString()))
		{
    		Toast.makeText(LoginActivity.this, "Email地址无效。", Toast.LENGTH_SHORT).show();
		}
    	else if(m_login_pwd.getText().toString().trim().length() < 6 || m_login_pwd.getText().toString().trim().length() > 16)
    	{
    		Toast.makeText(LoginActivity.this, "密码长度必须在6~16位之间。", Toast.LENGTH_SHORT).show();
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
		intent.setClass(LoginActivity.this, MainActivity.class);
		LoginActivity.this.startActivity(intent);
		LoginActivity.this.finish();
    }
    
    public void SendLoginMsg(String email, String pwd)
    {
    	// 记录登录时间
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");       
		String date = sDateFormat.format(new Date(System.currentTimeMillis())); 
		
		// send login request
		boolean issuccess = false;
		String httpUrl="http://116.228.240.106/login.php?email="+email.trim()+
					   "&pwd="+pwd.trim()+
					   "&logTime="+date.trim();
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
                	  strResult = "登陆成功";
                	  issuccess = true;
                  }
                  else if(strResult.equals("-1"))
                  {
                	  strResult = "用户名或密码错误！";
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
        Toast.makeText(LoginActivity.this, retStr, Toast.LENGTH_SHORT).show();
        
        if(issuccess == true)
        {
        	ChangeToMainWindow();
        }
    }

}  

