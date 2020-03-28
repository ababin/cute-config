package ru.absoft.util.cuteconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.CuteConfiguration;

public class CuteConfigurationRefreshTest {
	
	String CONFIG_FILE = System.getProperty("user.dir")+"/src/test/resources/tmp.properties";
	
	@BeforeEach
	private void before() throws IOException {
		Files.deleteIfExists(Paths.get(CONFIG_FILE));
	}
	
	@AfterEach
	private void after() throws IOException {
		Files.deleteIfExists(Paths.get(CONFIG_FILE));
	}
		
	@Test
	public void test_ok() throws InterruptedException, IOException {
		// 1. create tmp-file
		writeTmpFile("prop1=val1\nprop2=val2\nprop3=val3");
		
		// 2. create config
		CuteConfiguration config = new CuteConfiguration(CONFIG_FILE, 1000);
		
		// 3. check parameters
		assertEquals(3, config.getParams().size());
		assertEquals("val1", config.getString("prop1"));
		assertEquals("val2", config.getString("prop2"));
		assertEquals("val3", config.getString("prop3"));
		
		// 4. sleep 1500ms for lastModifiedTime could be applied
		Thread.sleep(1500);
		
		// 5. change properties file:
		writeTmpFile("prop4=val4\nprop5=val5");
		
		// 6. timeout:
		Thread.sleep(1500);
		
		// 7. check parameters
		assertEquals(2, config.getParams().size());
		assertEquals("val4", config.getString("prop4"));
		assertEquals("val5", config.getString("prop5"));
	}
	
	
	private void writeTmpFile(String content) throws IOException {
		Files.write(Paths.get(CONFIG_FILE), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
}
