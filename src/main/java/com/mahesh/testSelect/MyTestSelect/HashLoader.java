package com.mahesh.testSelect.MyTestSelect;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HashLoader {
	static {
		HashLoader.jsonFilePath = "test.json";
		HashLoader.loadSuccess=false;
	}
	
	private static String jsonFilePath;
	private static boolean loadSuccess;
	private static HashMap<String,String> hMap;
	public static void loadMap() {
		HashLoader.hMap= new HashMap<String,String>();
		JSONArray existingFileHash= JsonHelper.JsonRead();
		Iterator jsonItr = existingFileHash.iterator();
		int hcount=0;
		while (jsonItr.hasNext()) {
			JSONObject temp = (JSONObject) jsonItr.next();
			String className = (String) temp.get("name");
			String existingHash =  (String) temp.get("hash");
			if (className==null || existingHash==null) {
				continue;
			}
			ClassInfo tempCs =ClassNode.getClassInfo(className);
			if (tempCs==null) {
				continue;
			}
			tempCs.existingHash = existingHash ;
			hcount++;
		}
		System.out.println("HashLoader : hash loaded successfully. Number of existing hashes loaded :"+hcount);
		HashLoader.loadSuccess=true;
	}
}
