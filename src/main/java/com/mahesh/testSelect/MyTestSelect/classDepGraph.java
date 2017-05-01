package com.mahesh.testSelect.MyTestSelect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo( name = "select-tests")
public class classDepGraph extends AbstractMojo {
	public static void init(String packName) {
		jdepFile="jdeps_out";
		packageName=packName;
		FinalTestList = new ArrayList<ClassInfo>();
	}
	public static void createJdepsOutFile() {
		String command = "jdeps -v target > "+classDepGraph.jdepFile;
		StringBuilder output = new StringBuilder();
//		ProcessBuilder builder = new ProcessBuilder("jdeps","-v", "-R","target");
		ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c", ("jdeps -v target/* >"+classDepGraph.jdepFile));
		Process p;
		try {
//			System.out.println("executing command "+command);
			p = builder.start();
			p.waitFor();
			BufferedReader reader =
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
//			System.out.println("DONE SUCCESS WITH command "+command +" and got output :"+output+":");
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
	public static String jdepFile="jdeps_out";
	@Parameter( property = "select-tests.packname", defaultValue = "MaheshExample.Test" )
	private String packname;
	public static String packageName;
	public static ArrayList<ClassInfo> FinalTestList;
	public void execute() throws MojoExecutionException
    {
//		System.out.println("classDepGraph : GOT ARGS: "+args.length+" arg1 is:"+args[0]+":");
		getLog().info("MyTestSelect : starting MyTestSelect tool");
		getLog().info("MyTestSelect : Initializing file names and parameters");
		classDepGraph.init(this.packname);
		if (classDepGraph.packageName.startsWith("Mahesh")) {
			getLog().info("ERROR in MyTestSelect : Package name not initialized properly! Exiting now");
			return;
		}
		getLog().info("MyTestSelect : Initializing with package name : "+classDepGraph.packageName);
		//create a new jdeps out file
		getLog().info("MyTestSelect : Create a new jdeps out file");
		classDepGraph.createJdepsOutFile();
		//check if JDeps has been executed
		//link class info classes
		getLog().info("MyTestSelect : Linking class nodes to form a dependency graph");
		classDepGraph.linkInfoNodes();
		//check for changes and start marking tests for execution
		getLog().info("MyTestSelect : Check for changes and start marking tests for execution");
		ClassNode.getRootClassNode().checkForChanges();
		//flush all hashes back to disk
		getLog().info("MyTestSelect : Flushing new hashes for next run");
		ClassNode.flushHashesToFile();
		getLog().info("MyTestSelect : Number of tests shortlisted for next run is : "+classDepGraph.FinalTestList.size());
		getLog().info("MyTestSelect : command to execute : ");
		if (classDepGraph.FinalTestList.size()==0) {
			getLog().info("None");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("mvn test ");
			for (int i =0; i<classDepGraph.FinalTestList.size(); i++){
				ClassInfo temp =classDepGraph.FinalTestList.get(i);
				if (i==0) {
					sb.append("-Dtest="+temp.className+",");
				} else {
					sb.append(temp.className+",");
				}
			}
			getLog().info(sb.toString());
		}
        getLog().info( "MyTestSelect: Done. Exiting now" );
    }
	public static void main (String[] args) {
//		System.out.println("classDepGraph : GOT ARGS: "+args.length+" arg1 is:"+args[0]+":");
		System.out.println("classDepGraph : starting MyTestSelect tool");
		System.out.println("classDepGraph : Initializing file names and parameters");
		classDepGraph.init("com.mahesh.testSelect");
		classDepGraph.packageName="com.mahesh.testSelect";
		//create a new jdeps out file
		System.out.println("classDepGraph : Create a new jdeps out file");
		classDepGraph.createJdepsOutFile();
		//check if JDeps has been executed
		//link class info classes
		System.out.println("classDepGraph : Linking class nodes to form a dependency graph");
		classDepGraph.linkInfoNodes();
		//check for changes and start marking tests for execution
		System.out.println("classDepGraph : Check for changes and start marking tests for execution");
		ClassNode.getRootClassNode().checkForChanges();
		//flush all hashes back to disk
		System.out.println("classDepGraph : Flushing new hashes for next run");
		ClassNode.flushHashesToFile();
		System.out.println("classDepGraph : Number of tests shortlisted for next run is : "+classDepGraph.FinalTestList.size());
		System.out.println("classDepGraph : command to execute : ");
		if (classDepGraph.FinalTestList.size()==0) {
			System.out.println("None");
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("mvn test ");
			for (int i =0; i<classDepGraph.FinalTestList.size(); i++){
				ClassInfo temp =classDepGraph.FinalTestList.get(i);
				if (i==0) {
					sb.append("-Dtest="+temp.className+",");
				} else {
					sb.append(temp.className+",");
				}
			}
			System.out.println(sb.toString());
		}
	}
	public static String getClassName(String val) {
//		System.out.println("^^^^^Got val "+val);
		String retVal = null;
		if (val.trim().split(" ").length>1) {
			String[] vals = val.split(" ");
			val = vals[0];
//			System.out.println("^^^^^setting val "+val);
		}
		
		retVal = val.substring(val.lastIndexOf('.')+1);
		return retVal;
	}
	public static void linkInfoNodes() {
		 
		 try {
			 BufferedReader br = new BufferedReader(new FileReader(classDepGraph.jdepFile));
			 String line;
			 while ((line=br.readLine()) !=null) {
//				 System.out.println("******* reading"+line);
				 line = line.trim();
				 if (!line.startsWith(packageName)) {
					 continue;
				 }
				 String[] deps = line.split("->");
				 if (deps.length!=2) {
					 System.out.println("ENcountered unparseable string "+line);
					 continue;
				 }
				 deps[0]=deps[0].trim();
				 deps[1] =deps[1].trim();
				 if (!deps[1].startsWith(packageName)) {
					 continue;
				 }
				 //now i have both parts in same package so we find the classInfo of part 1 and add 2 as dependency
				 String firstCN= classDepGraph.getClassName(deps[0]);
				 String secondCN= classDepGraph.getClassName(deps[1]);
				 ClassInfo fClassInfo = ClassNode.getClassInfo(firstCN);
				 ClassInfo sClassInfo = ClassNode.getClassInfo(secondCN);
				 if (fClassInfo !=null && sClassInfo!=null) {
					 sClassInfo.addDep(fClassInfo);
				 }
			 }
			br.close();
		} catch (IOException e) {
			System.out.println("Error reading the JDEPS file");
			e.printStackTrace();
		}
	}
	public static void addTestToBeExecuted(ClassInfo classInfo) {
		classDepGraph.FinalTestList.add(classInfo);
	}
}
