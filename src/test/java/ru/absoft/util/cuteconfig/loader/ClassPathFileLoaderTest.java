package ru.absoft.util.cuteconfig.loader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.loader.ClassPathFileLoader;

public class ClassPathFileLoaderTest {
			
	@Test
	public void test_fileExists_false() throws Exception {
		assertFalse(ClassPathFileLoader.fileExists("config.properties"));
	}
	
	@Test
	public void test_fileExists_true() throws Exception {
		assertTrue(ClassPathFileLoader.fileExists("in_classpath/config.properties"));
	}
	
	@Test
	public void test_fileExists_true2() throws Exception {
		assertTrue(ClassPathFileLoader.fileExists("first-config.properties"));
	}
	
	@Test
	public void test_getLastTimeModified() throws Exception {
	    long minTimeModified = 1577836800000L; // 01.01.2020 00:00
		ClassPathFileLoader loader = new ClassPathFileLoader("first-config.properties");
		assertTrue(loader.getLastTimeModified() > minTimeModified);
	}
	
}
