package org.bidve.hotelmanagement.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bidve.hotelmanagement.POJO.Category;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HotelUtils {
	

	private HotelUtils() {
		
	}
	
	public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
		
		return new ResponseEntity<String>( "body:" +"{\"message\":\"" + responseMessage + "\"}", httpStatus);
		
	}
	
	public static String getUUID() {
		Date date = new Date();
		long time = date.getTime();
		return "BIll" + time;
	}
	
	public static JSONArray getJsonArrayFromString(String data) throws JSONException{
		JSONArray jsonArray = new JSONArray(data);
		return jsonArray;
	}
	
	public static Map<String,Object> getMapFromJson(String data){
		if(!Strings.isNullOrEmpty(data))
			return new Gson().fromJson(data,new TypeToken<Map<String,Object>>(){	
			}.getType());
		return new HashMap<>();
			
		
	}

}
