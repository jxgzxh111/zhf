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
	private SharedPreferences sp;  // ����׿��������key value�洢
	
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.register_layout);  
        
        register_email = (EditText)findViewById(R.id.email_text);
        register_passwd = (EditText)findViewById(R.id.pwd_text);
        reregister_passwd = (EditText)findViewById(R.id.pwd_text2);
        register_invite_email = (EditText)findViewById(R.id.invite_email);   
        sp = RegisterActivity.this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        
        // ���ذ�ť
        Button backBtn = (Button)findViewById(R.id.btn_register_back);
        backBtn.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View v) {
				RegisterActivity.this.finish();
			}
        	
        });
        
        // ע�ᰴť
        Button registerBtn = (Button)findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(!CheckEdit())
				{
					return;
				}
				
				// ��ע��ʱ��
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
		                	  strResult = "ע��ɹ�";
		                	  issuccess = true;
		                  }
		                  else if(strResult.equals("-1"))
		                  {
		                	  strResult = "ע��ʧ�ܣ��������˺���ע�ᣡ";
		                  }
		                  else if(strResult.equals("-2"))
		                  {
		                	  strResult = "ϵͳ��æ�����Ժ�";
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
                Toast.makeText(RegisterActivity.this, retStr, Toast.LENGTH_SHORT).show();
                
                if(issuccess == true)
		        {
                	// ���ش洢�˺�����
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
    		Toast.makeText(RegisterActivity.this, "Email��ַ��Ч��", Toast.LENGTH_SHORT).show();
		}
    	else if(!register_passwd.getText().toString().trim().equals(reregister_passwd.getText().toString().trim()))
    	{
    		Toast.makeText(RegisterActivity.this, "�����������벻һ�¡�", Toast.LENGTH_SHORT).show();
    	}
    	else if(register_passwd.getText().toString().trim().length() < 6 || register_passwd.getText().toString().trim().length() > 16)
    	{
    		Toast.makeText(RegisterActivity.this, "���볤�ȱ�����6~16λ֮�䡣", Toast.LENGTH_SHORT).show();
    	}
    	else if(reregister_passwd.getText().toString().trim().length() < 6 || reregister_passwd.getText().toString().trim().length() > 16)
    	{
    		Toast.makeText(RegisterActivity.this, "���볤�ȱ�����6~16λ֮�䡣", Toast.LENGTH_SHORT).show();
    	}
    	else if((!register_invite_email.getText().toString().trim().equals("")) && !GlobalFunction.btnValidateEmailAddress(register_invite_email.getText().toString()))
    	{
    		Toast.makeText(RegisterActivity.this, "�������˺Ÿ�ʽ����", Toast.LENGTH_SHORT).show();
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

