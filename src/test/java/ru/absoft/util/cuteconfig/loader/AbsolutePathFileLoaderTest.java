package ru.absoft.util.cuteconfig.loader;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.loader.AbsolutePathFileLoader;

public class AbsolutePathFileLoaderTest {

	@Test
	public void test_fileExists_false() throws Exception {
		assertFalse(AbsolutePathFileLoader.fileExists("config.properties"));
	}
	
	@Test
	public void test_fileExists_true() throws Exception {
		assertTrue(AbsolutePathFileLoader.fileExists("src/test/resources/in_classpath/config.properties"));
	}
	
	@Test
	public void test_fileExists_true2() throws Exception {
		assertTrue(AbsolutePathFileLoader.fileExists("src/test/resources/first-config.properties"));
	}
	
	@Test
	public void test_getLastTimeModified() throws Exception {
	    long minTimeModified = 1577836800000L; // 01.01.2020 00:00
	    AbsolutePathFileLoader loader = new AbsolutePathFileLoader("src/test/resources/first-config.properties");
		assertTrue(loader.getLastTimeModified() > minTimeModified);
	}
	
}
