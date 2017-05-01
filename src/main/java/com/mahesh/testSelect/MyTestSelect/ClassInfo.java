package com.mahesh.testSelect.MyTestSelect;

import java.io.File;
import java.util.ArrayList;

public class ClassInfo {
	
	String className;
	int classId;
	String path;
	String packageVal;
	String existingHash;
	String newHash;
	boolean isTest;
	boolean hasChanged;
	private ArrayList<ClassInfo> classDepList;
	private ArrayList<ClassInfo> testDepList;
	public ClassInfo() {
		this.hasChanged= false;
		this.isTest = checkIfTestClass(path);
	}
	
	public ClassInfo(int classId, String path) {
		this.hasChanged= false;
		this.isTest = checkIfTestClass(path);
		this.classId = classId;
		File temp = new File(path);
		this.className = temp.getName();
		this.newHash = HashGenerator.generateHash(new File(path));
		this.classDepList= new ArrayList<ClassInfo>();
		this.testDepList= new ArrayList<ClassInfo>();
		//this.existingHash= loadExistingHash();
	}
	public ClassInfo(int classId, File fh) {
		this.hasChanged= false;
		this.isTest = checkIfTestClass(fh.getPath());
		this.classId = classId;
		this.path = fh.getPath();
		this.className = fh.getName().substring(0,fh.getName().lastIndexOf('.'));
		this.newHash = HashGenerator.generateHash(fh);
//		System.out.println("init class name "+this.className+" with id: "+this.classId+" hash is: "+this.newHash);
		ClassNode.register(this);
		this.classDepList= new ArrayList<ClassInfo>();
		this.testDepList= new ArrayList<ClassInfo>();
		//this.existingHash= loadExistingHash();
	}
	
	public boolean checkIfTestClass(String path) {
		String[] folders = path.split("/");
		StringBuffer sb = new StringBuffer();
		sb.append("test-classes");
		boolean flag = false;
		for (String temp: folders) {
			if(temp.contentEquals(sb)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public void addDep(ClassInfo csInfo) {
//		System.out.println("^^^^^^^^^"+ "ADDING DEP IN \t"+this.className);
		if (csInfo.isTest) {
//			System.out.println("^^^^^^^^^"+ "adding to TEST dep list "+csInfo.className);
			this.testDepList.add(csInfo);
		} else {
//			System.out.println("^^^^^^^^^"+ "adding to CLASS dep list "+csInfo.className);
			this.classDepList.add(csInfo);
		}
	}
	public void checkForChanges(){
		if (existingHash==null) {
			this.markChanged();
		} else {
			if (newHash.compareTo(existingHash)!=0) {
				this.markChanged();
			}
		}
		
		
	}

	private void markChanged() {
		if (!this.hasChanged) {
			this.hasChanged = true;
			for (ClassInfo temp: this.classDepList) {
				temp.markChanged();
			}
			for (ClassInfo temp: this.testDepList) {
				temp.markChanged();
			}
			if (this.isTest) {
//				System.out.println("Adding this test to finalTestList"+ this.className);
				classDepGraph.addTestToBeExecuted(this);
			}
		}
	}
}
