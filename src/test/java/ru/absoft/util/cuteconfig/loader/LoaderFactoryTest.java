package ru.absoft.util.cuteconfig.loader;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.loader.AbsolutePathFileLoader;
import ru.absoft.util.cuteconfig.loader.ClassPathFileLoader;
import ru.absoft.util.cuteconfig.loader.Loader;
import ru.absoft.util.cuteconfig.loader.LoaderFactory;

public class LoaderFactoryTest {
	
	@Test
	public void test_throw() throws FileNotFoundException {
		assertThrows(FileNotFoundException.class, 
				() -> LoaderFactory.obtainLoader("config.properties")
				);
	}
	
	@Test
	public void test_classPathFileLoader() throws FileNotFoundException {
		Loader loader = LoaderFactory.obtainLoader("in_classpath/config.properties");
		assertTrue(loader instanceof ClassPathFileLoader);
	}
	
	@Test
	public void test_absolutePathFileLoader() throws FileNotFoundException {
		Loader loader = LoaderFactory.obtainLoader("src/test/resources/in_classpath/config.properties");
		assertTrue(loader instanceof AbsolutePathFileLoader);
	}
	
}
