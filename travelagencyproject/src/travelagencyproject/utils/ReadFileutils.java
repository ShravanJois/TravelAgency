package travelagencyproject.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;
import org.json.JSONTokener;

public class ReadFileutils {

	public static JSONObject readJsonResource(String fileName) {
		JSONObject resourceJson = null;
		try (InputStream in = 
				Thread.currentThread().
				getContextClassLoader().
				getResourceAsStream(fileName)) {
			// Create a BufferedReader object that wraps the InputStreamReader object.
	        BufferedReader bufferedReader = 
	        		new BufferedReader(new InputStreamReader(in, "UTF-8"));
			resourceJson = new JSONObject(new JSONTokener(bufferedReader));
		} catch(Exception e){
			System.out.println("error " + e.getMessage());
			e.printStackTrace();
		}
		return resourceJson;
	}

}
