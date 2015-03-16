package com.sean.zhf;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.widget.Toast;

public class GlobalFunction
{
	static String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
    public static boolean btnValidateEmailAddress(String str)  
    {  
        Matcher matcherObj = Pattern.compile(strPattern).matcher(str.trim());  
  
        if (matcherObj.matches())  
        {  
    	    return true;
        } 
       
        return false;
    }  
    
    public static String SendMsg(String phpStr,String args)
    {
    	 String httpUrl="http://116.228.240.106/"+phpStr+"?"+args;
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
    	 return "";
    }
};