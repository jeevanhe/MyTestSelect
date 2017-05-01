package com.mahesh.testSelect.MyTestSelect;
import java.io.File;

import org.junit.Test;

import junit.framework.Assert;

public class HashGenTest {
	
	@Test
	public void hashTest() {
		File fh = new File("test.json");
		String x = HashGenerator.generateHash(fh);
		Assert.assertNotNull(x);
	}
}
