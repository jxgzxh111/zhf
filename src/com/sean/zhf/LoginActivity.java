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
	private SharedPreferences sp;  // ����׿��������key value�洢
	
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
        
        // ��ʼ��½
        Button loginBtn = (Button)findViewById(R.id.button1);
        loginBtn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!CheckEdit())
				{
					return;
				}
				
				// ���ش洢�˺�����
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
    		Toast.makeText(LoginActivity.this, "Email��ַ��Ч��", Toast.LENGTH_SHORT).show();
		}
    	else if(m_login_pwd.getText().toString().trim().length() < 6 || m_login_pwd.getText().toString().trim().length() > 16)
    	{
    		Toast.makeText(LoginActivity.this, "���볤�ȱ�����6~16λ֮�䡣", Toast.LENGTH_SHORT).show();
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
    	// ��¼��¼ʱ��
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
             //ȡ��HttpClient����
             HttpClient httpclient = new DefaultHttpClient();
             //����HttpClient��ȡ��HttpResponse
             HttpResponse httpResponse = httpclient.execute(httpRequest);
             //����ɹ�
             if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
             {
                  //ȡ�÷��ص��ַ���
                  String strResult = EntityUtils.toString(httpResponse.getEntity());
                  if(strResult.equals("0"))
                  {
                	  strResult = "��½�ɹ�";
                	  issuccess = true;
                  }
                  else if(strResult.equals("-1"))
                  {
                	  strResult = "�û������������";
                  }
                  retStr = strResult;
             }
             else
             {
            	  retStr = "�������!";
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

