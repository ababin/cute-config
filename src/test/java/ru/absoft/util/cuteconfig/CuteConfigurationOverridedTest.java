package ru.absoft.util.cuteconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.exc.ParamNotFoundException;
import ru.absoft.util.cuteconfig.model.Value;

public class CuteConfigurationOverridedTest {
	
	String CONFIG_FILE = System.getProperty("user.dir")+"/src/test/resources/first-config.properties";
	String OVERRIDED_FILE = System.getProperty("user.dir")+"/src/test/resources/overrided-config.properties";
			
	@Test
	public void test_overrided() throws FileNotFoundException {
		CuteConfiguration config = createConfig();
		
		Map<String, Value> map = config.getParams();
		
		assertNotNull(map);
		assertEquals(7, map.size());
	}
	
	@Test
	public void test_getString() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
						
		assertEquals("overrided", config.getString("some.property1"));
		assertEquals("TRue", config.getString("some.property2"));
		assertEquals("val3", config.getString("some.property3"));
		assertEquals("overrided", config.getString("some.property4"));
		assertEquals("new overrided", config.getString("some.property5"));
	}
	
	@Test
	public void test_getString_notFound() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
					
		assertThrows(ParamNotFoundException.class,
				() -> config.getString("some.property12")
				);
	}
	
	@Test
	public void test_getString_notFoundWithDefault() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
					
		assertEquals("valll", config.getString("some.property122", "valll"));
	}
	
	@Test
	public void test_getStringList_normal() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
					
		List <String> list = config.getStringList("some.propertyList");
		assertEquals(4, list.size());
	}
	
	@Test
	public void test_getStringList_notFound() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
					
		assertThrows(ParamNotFoundException.class,
				() -> config.getStringList("some.property12")
				);
	}
	
	@Test
	public void test_getStringMap_normal() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
					
		Map <String, String> map = config.getStringMap("some.propertyMap");
		assertEquals(1, map.size());
	}
	
	@Test
	public void test_getBoolean_notFound() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
					
		assertThrows(ParamNotFoundException.class,
				() -> config.getStringList("some.boolean")
				);
	}
	
	@Test
	public void test_getBoolean_notFound_withDefault() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
		assertTrue(config.getBoolean("some.boolean", true));
		assertFalse(config.getBoolean("some.boolean", false));
	}
	
	@Test
	public void test_getBoolean() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
		assertTrue(config.getBoolean("some.property2"));			
	}
	
	@Test
	public void test_getBoolean_withDefault() throws FileNotFoundException {
	    CuteConfiguration config = createConfig();
		assertTrue(config.getBoolean("some.property2", true));			
		assertTrue(config.getBoolean("some.property2", false));
	}
	
	private CuteConfiguration createConfig() throws FileNotFoundException {
	    return CuteConfiguration.builder()
                .filePath(CONFIG_FILE)
                .overridedFilePath(OVERRIDED_FILE)
                .refreshPeriodMS(1000)
                .build();
	}
	
}
