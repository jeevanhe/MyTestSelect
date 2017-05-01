package com.mahesh.testSelect.MyTestSelect;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;


public class ClassNode {
	
	static {
		System.out.println("ClassNode : init of new hashes and map for faster access");
		ClassNode root = new ClassNode("./target");
		ClassNode.setRootClassNode(root);
		HashLoader.loadMap();
	}
	private int level;
	private static ClassNode rootClassNode;
	private static ArrayList<ClassInfo> mainClassInfoList;
	private static HashMap<String,Integer> classInfoTable;
	private static int classIdGen;
	private boolean isRoot;
	private String name;
	private String path;
	private ArrayList<ClassInfo> classInfoList;
	private ArrayList<ClassNode> classNodeList;
	
	public synchronized static void register(ClassInfo csInfo) {
		ClassNode.classInfoTable.put(csInfo.className, ClassNode.classIdGen);
		ClassNode.mainClassInfoList.add(csInfo);
		ClassNode.classIdGen++;
	}
	//root node initializer
	public ClassNode(String path ){
		this.setName("rootNode");
		this.setPath("./target");
		this.setRoot(true);
		this.setClassInfoList(new ArrayList<ClassInfo>());
		ClassNode.classIdGen=0;
		ClassNode.classInfoTable=classInfoTable = new HashMap<String,Integer>();
		ClassNode.mainClassInfoList = new ArrayList<ClassInfo>();
		this.setClassNodeList(new ArrayList<ClassNode>());
		this.level = 0;
		this.loadClassNodes();
	}
	public ClassNode(int level, String path){
		this.level=level;
		File temp = new File(path);
		this.setName(temp.getName());
		this.setPath(path);
		this.setRoot(false);
		this.setClassInfoList(new ArrayList<ClassInfo>());
		this.setClassNodeList(new ArrayList<ClassNode>());
	}
	public ClassNode(int level, File fh){
		this.level=level;
		this.setName(fh.getName());
		this.setPath(fh.getPath());
		this.setRoot(false);
		this.setClassInfoList(new ArrayList<ClassInfo>());
		this.setClassNodeList(new ArrayList<ClassNode>());
//		System.out.println("init class name"+this.getName()+" with id: "+this.level);
		this.loadClassNodes();
	}
	public ClassNode(String name, String path,ArrayList<Integer> list){
		this.setName(name);
		this.setPath(path);
	}
	public static ClassInfo getClassInfo(String className) {
		ClassInfo retVal = null;
		
		//first find index of this className using hashMap
		
		Integer index = ClassNode.classInfoTable.get(className);
		if (index!=null) {
			retVal = ClassNode.mainClassInfoList.get(index);
		}
		return retVal;
	}
	//adders
	
	public void loadClassNodes() {
		File pHome = new File(this.path); 
		int fileId = Integer.parseInt(this.level+"0");
		//first process all files under this folder
		for (File temp: pHome.listFiles()) {
			if (temp.isFile() && temp.getPath().endsWith(".class")) {
				this.classInfoList.add(new ClassInfo(fileId, temp));
				fileId++;
			}
		}
		//first process all files under this folder
		fileId = Integer.parseInt(this.level+"0");
		for (File temp: pHome.listFiles()) {
			if (temp.isDirectory()) {
				this.classNodeList.add(new ClassNode(fileId,temp));
				fileId++;
			}
		}
	}
	
	//setters and getters
	public static ClassNode getRootClassNode() {
		return rootClassNode;
	}
	public static void setRootClassNode(ClassNode rootClassNode) {
		ClassNode.rootClassNode = rootClassNode;
	}
	public ArrayList<ClassInfo> getClassInfoList() {
		return classInfoList;
	}
	public void setClassInfoList(ArrayList<ClassInfo> classInfoList) {
		this.classInfoList = classInfoList;
	}
	public ArrayList<ClassNode> getClassNodeList() {
		return classNodeList;
	}
	public void setClassNodeList(ArrayList<ClassNode> classNodeList) {
		this.classNodeList = classNodeList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isRoot() {
		return isRoot;
	}
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void checkForChanges() {
		//check all class infos
		for(ClassInfo temp: this.classInfoList) {
			temp.checkForChanges();
		}
		
		for(ClassNode temp: this.classNodeList) {
			temp.checkForChanges();
		}
	}
	public static void flushHashesToFile() {
		JsonHelper.flushHashToFile(mainClassInfoList);
	}
}
