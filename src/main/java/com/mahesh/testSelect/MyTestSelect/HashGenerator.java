package com.mahesh.testSelect.MyTestSelect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.commons.codec.digest.*;
import org.apache.commons.io.*;

public class HashGenerator {
//	public enum fileType {SOURCE,TEST};
//	String fileName;
//	String filePath;
//	File fileHandle;
//	String genHash;
//	String hashType = "SHA-1";
//	fileType type;
//	
//	public HashGenerator() {
//		this.fileName=null;
//		this.fileHandle=null;
//		this.genHash=null;
//	}
//	public HashGenerator(String fileName) {
//		this.fileName=fileName;
//		this.fileHandle=new File(this.fileName);
//		this.genHash=null;
//		this.generateHash();
//	}
	//something from nothing
	private String getFileName(String path) {
		System.out.println("");
		return path.substring(path.lastIndexOf('/')+1,path.lastIndexOf('.'));
	}
//	private void generateHash() {
//		String stringToConvertToSHexRepresentation;
//		try {
//			stringToConvertToSHexRepresentation = FileUtils.readFileToString(this.fileHandle);
//			this.genHash=DigestUtils.sha1Hex(stringToConvertToSHexRepresentation);
//		} catch (IOException e) {
//			System.out.println("MyTestAgentError: Encountered Problem during hash generation");
//			e.printStackTrace();
//		}
//	}
	public static String generateHash(File path) {
		String stringToConvertToSHexRepresentation;
		String retHash=null;
		try {
			stringToConvertToSHexRepresentation = FileUtils.readFileToString(path);
			retHash=DigestUtils.sha1Hex(stringToConvertToSHexRepresentation);
		} catch (IOException e) {
			System.out.println("MyTestAgentError: Encountered Problem during hash generation");
			e.printStackTrace();
		}
		return retHash;
	}
//	public static void generateAllFileNames(String path, ArrayList<HashGenerator> fileHashList,fileType type) {
//		File pHome = new File(path);
//		for (File temp: pHome.listFiles()) {
//			if (temp.isFile() && temp.getPath().endsWith(".class")) {
//				fileHashList.add(new HashGenerator(temp,type));
//			} else if (temp.isDirectory()) {
//				generateAllFileNames(temp.getPath(), fileHashList,type);
//			}
//		}
//	}
	//something with nothing why no hash change?
}
