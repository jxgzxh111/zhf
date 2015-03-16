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
    	 return "";
    }
};