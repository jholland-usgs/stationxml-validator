package edu.iris.dmc;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) {
		Path path = Paths.get("http://iris.edu");
		
		System.out.println(path.getFileName());
		
		System.out.println(path.startsWith("http://"));
		
		System.out.println(path.toUri());


	}

}
