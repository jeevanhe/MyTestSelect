package com.mahesh.testSelect.MyTestSelect;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class JsonHelper {
	JSONArray classesInfo;
	//TODO: add path to json to standard name
	public static JSONArray JsonRead(String fileName) {
		JSONParser parser = new JSONParser();
		JSONArray retVal = new JSONArray();
		try {
			//FileReader fRead = new FileReader(fileName);
			String contentOfFile= FileUtils.readFileToString(new File(fileName), "UTF-8");
			if (contentOfFile.length()==0) {
				return retVal;
			}
			retVal = (JSONArray) parser.parse(contentOfFile);
//			Iterator jsonItr = retVal.iterator();
//			System.out.println("#######JSON ITR WORKING");
//			while (jsonItr.hasNext()) {
//				System.out.println(jsonItr.next());
//			}
//			System.out.println("#######JSON ITR WORKING");
		} catch (FileNotFoundException e) {
			System.out.println("MyTestSelect JsonHelper: File for reading existing hashes does not exisit");
		} catch (IOException e) {
			System.out.println("MyTestSelect JsonHelper: ERROR reading File for existing hashes");
		} catch (ParseException e) {
			System.out.println("MyTestSelect JsonHelper: ERROR parsing json from File for existing hashes");
		}
		return retVal;
	}
	public static JSONArray JsonRead() {
		return JsonRead("test.json");
	}
	public static void flushHashToFile(ArrayList<ClassInfo> mainClassInfoList) {
		String jsonFileName = "test.json";
		File wFh = new File(jsonFileName);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter( new FileWriter( jsonFileName));
			String lineOut = JsonHelper.JsonCreate(mainClassInfoList).toJSONString();
//			System.out.println("JSON STRING TO WRITE TO FILE");
//			System.out.println(lineOut);
			try {
				writer.write(lineOut);
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.close();
//			System.out.println("^^^^^^^^^^DONE WITH FLushing hash data to file");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	public static JSONArray JsonCreate( ArrayList<ClassInfo> mainClassInfoList) {
		JSONArray retVal = new JSONArray();
		for (ClassInfo temp: mainClassInfoList) {
			JSONObject obj = new JSONObject();
			obj.put("name", temp.className);
			obj.put("path", temp.path);
			obj.put("hash", temp.newHash);
			obj.put("isTest", ""+temp.isTest);
			retVal.add(obj);
		}
		return retVal;
	}
//	public static ArrayList<HashGenerator> convertToHashGenArray(JSONArray list) {
//		ArrayList<HashGenerator> retVal = new ArrayList<HashGenerator>();
//		return retVal;
//	}
}
