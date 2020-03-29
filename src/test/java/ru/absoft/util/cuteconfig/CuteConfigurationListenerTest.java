package ru.absoft.util.cuteconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.ConfigurationListener;
import ru.absoft.util.cuteconfig.CuteConfiguration;

public class CuteConfigurationListenerTest {
	
	String CONFIG_FILE = "src/test/resources/tmp.properties";
	
	volatile boolean onReadFileError;
	volatile boolean onFileChanged;
	
	@BeforeEach
	private void before() throws IOException {
		removeFileIfExists();
		onReadFileError = false;
		onFileChanged = false;
	}
	
	@AfterEach
	private void after() throws IOException {
		removeFileIfExists();
	}
	
	private void removeFileIfExists() throws IOException {
		Files.deleteIfExists(Paths.get(CONFIG_FILE));
	}
		
	@Test
	public void test_configRemovedDuringWorking() throws InterruptedException, IOException {
		// 1. create tmp-file
		writeTmpFile("prop1=val1");
		
		// 2. create config
		CuteConfiguration config = CuteConfiguration.builder()
		        .filePath(CONFIG_FILE)
		        .refreshPeriodMS(1_000)
		        .confListener(new ConfigurationListener() {
                    @Override
                    public void onReadFileError(String message, Throwable t) {
                        onReadFileError = true;
                    }
                    @Override
                    public void onFileChanged(long fileModifiedTime) {
                        onFileChanged = true;               
                    }})
		        .build();
		
		// 3. check parameters
		assertEquals(1, config.getParams().size());
		assertEquals("val1", config.getString("prop1"));
		assertFalse(onReadFileError);
		
		// 4. pause 1500ms 
		Thread.sleep(1500);
		
		// 5. remove file
		removeFileIfExists();
		
		// 6. sleep on 2s
		Thread.sleep(1500);
		
		// 7. check params
		assertTrue(onReadFileError);
	}
	
	@Test
	public void test_configChanged() throws InterruptedException, IOException {
		// 1. create tmp-file
		writeTmpFile("prop1=val1");
		
		// 2. create config
		CuteConfiguration config = CuteConfiguration.builder()
		        .filePath(CONFIG_FILE)
		        .refreshPeriodMS(1000)
		        .confListener(new ConfigurationListener() {
        			@Override
        			public void onReadFileError(String message, Throwable t) {
        				onReadFileError = true;
        			}
        			@Override
        			public void onFileChanged(long fileModifiedTime) {
        				onFileChanged = true;				
        			}})
		        .build();
		
		// 3. check parameters
		assertEquals(1, config.getParams().size());
		assertEquals("val1", config.getString("prop1"));
		assertFalse(onFileChanged);
		
		// 4. pause 500ms for modification time was changed
		Thread.sleep(1500);
						
		// 5. change config
		writeTmpFile("prop2=val2");
		
		// 6. sleep on 2s
		Thread.sleep(1500);
		
		// 7. check params
		assertEquals(1, config.getParams().size());
		assertEquals("val2", config.getString("prop2"));
		assertTrue(onFileChanged);
	}
	
	
	private void writeTmpFile(String content) throws IOException {
		Files.write(Paths.get(CONFIG_FILE), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
}
