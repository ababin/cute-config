package ru.absoft.util.cuteconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.exc.ParamNotFoundException;
import ru.absoft.util.cuteconfig.model.Value;

public class CuteConfigurationTest {
	
	String CONFIG_FILE = System.getProperty("user.dir")+"/src/test/resources/first-config.properties";
	
	@Test
	public void test_whenFileNotFound() throws FileNotFoundException {
		assertThrows(FileNotFoundException.class,
				() -> CuteConfiguration.builder().filePath("first-config-not-exist.properties").refreshPeriodMS(1000).build()
				);
	}
	
	@Test
	public void test_relatedPath() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath("src/test/resources/first-config.properties").refreshPeriodMS(1000).build();
		assertEquals(6, config.getParams().size());
	}
	
	@Test
	public void test_withPostProcessor() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder()
		        .filePath("src/test/resources/first-config.properties")
		        .refreshPeriodMS(1000)
		        .postProcessor(new IntervalPostProcessor())
		        .build();
				 
		assertEquals(6, config.getParams().size());
		Map<String, String> map = config.getStringMap("some.propertyMap");
		assertEquals(26, map.size());
		
		List<String> list = config.getStringList("some.propertyList");
		assertEquals(42, list.size());
		
		Set<String> set = config.getStringSet("some.propertyList");
		assertEquals(40, set.size());
	}
	
	@Test
	public void test_withNullPostProcessors() throws FileNotFoundException {
		PostProcessor postProcessor = null;
		CuteConfiguration config = CuteConfiguration.builder()
		        .filePath("src/test/resources/first-config.properties")
		        .refreshPeriodMS(1000)
		        .postProcessor(postProcessor)
		        .build();
		
		assertEquals(6, config.getParams().size());
		Map<String, String> map = config.getStringMap("some.propertyMap");
		assertEquals(4, map.size());
		
		List<String> list = config.getStringList("some.propertyList");
		assertEquals(16, list.size());
		
		Set<String> set = config.getStringSet("some.propertyList");
		assertEquals(14, set.size());
	}
	
	@Test
	public void test_absolutePath() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
		
		Map<String, Value> map = config.getParams();
		
		assertNotNull(map);
		assertEquals(6, map.size());
	}
	
	@Test
	public void test_getString() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
						
		assertEquals("val1", config.getString("some.property1"));
	}
	
	@Test
	public void test_getString_notFound() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
					
		assertThrows(ParamNotFoundException.class,
				() -> config.getString("some.property12")
				);
	}
	
	@Test
	public void test_getString_notFoundWithDefault() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
					
		assertEquals("valll", config.getString("some.property122", "valll"));
	}
	
	@Test
	public void test_getStringList_normal() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
					
		List <String> list = config.getStringList("some.propertyList");
		assertEquals(16, list.size());
	}
	
	@Test
	public void test_getStringList_notFound() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
					
		assertThrows(ParamNotFoundException.class,
				() -> config.getStringList("some.property12")
				);
	}
	
	@Test
	public void test_getStringMap_normal() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
					
		Map <String, String> map = config.getStringMap("some.propertyMap");
		assertEquals(4, map.size());
	}
	
	@Test
	public void test_getBoolean_notFound() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
					
		assertThrows(ParamNotFoundException.class,
				() -> config.getStringList("some.boolean")
				);
	}
	
	@Test
	public void test_getBoolean_notFound_withDefault() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
		assertTrue(config.getBoolean("some.boolean", true));
		assertFalse(config.getBoolean("some.boolean", false));
	}
	
	@Test
	public void test_getBoolean() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
		assertTrue(config.getBoolean("some.property2"));			
	}
	
	@Test
	public void test_getBoolean_withDefault() throws FileNotFoundException {
		CuteConfiguration config = CuteConfiguration.builder().filePath(CONFIG_FILE).refreshPeriodMS(1000).build();
		assertTrue(config.getBoolean("some.property2", true));			
		assertTrue(config.getBoolean("some.property2", false));
	}
	
}
