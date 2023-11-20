package com.soluvis.croffle.v1;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

public class ZTEST {

	public static void main(String[] args) {
		JSONArray ja = new JSONArray();
		for (int i = 0; i < 3; i++) {
			JSONObject jo = new JSONObject();
			jo.put("id", i);
			jo.put("name", "nm"+i);
			ja.put(jo);
		}
		ObjectMapper om = new ObjectMapper();
		
		TypeReference<List<Map<String,Object>>> typeReference = new TypeReference<>() {};
		try {
			System.out.println(om.readValue(ja.toString(), typeReference).toString());
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
