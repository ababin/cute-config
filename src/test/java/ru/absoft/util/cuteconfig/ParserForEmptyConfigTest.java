package ru.absoft.util.cuteconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.model.Value;

public class ParserForEmptyConfigTest {

	@Test
	public void test_common() throws IOException {
		List<Value> props = obtainValues(); 
		assertNotNull(props);
		assertEquals(4, props.size());
	}
	
	@Test
	public void test_simpleProperties() throws IOException {
		List<Value> props = obtainValues();
		
		// check simple properties -------------------------------------------------------------------
		assertEquals("", getValueByName(props, "some.property1").getString());
		assertEquals(0, getValueByName(props, "some.property1").getList().size());
		assertEquals(0, getValueByName(props, "some.property1").getSet().size());
		assertNull(getValueByName(props, "some.property1").getMap());
		
		assertEquals("", getValueByName(props, "some.property2").getString());
		assertEquals(0, getValueByName(props, "some.property2").getList().size());
		assertEquals(0, getValueByName(props, "some.property2").getSet().size());
		assertNull(getValueByName(props, "some.property2").getMap());
	}
	
	@Test
	public void test_list() throws IOException {
		List<Value> props = obtainValues();
						
		assertEquals("", getValueByName(props, "some.propertyList").getString());
		assertEquals(0, getValueByName(props, "some.propertyList").getList().size());
		assertEquals(0, getValueByName(props, "some.propertyList").getSet().size());
		assertNull(getValueByName(props, "some.propertyList").getMap());
	}
	
	@Test
	public void test_map() throws IOException {
		List<Value> props = obtainValues();
						
		assertEquals("", getValueByName(props, "some.propertyMap").getString());
		assertNull(getValueByName(props, "some.propertyMap").getList());
		assertNull(getValueByName(props, "some.propertyMap").getSet());
		assertEquals(0, getValueByName(props, "some.propertyMap").getMap().size());
	}
	
	private Value getValueByName(List<Value> list, String paramName) {
		return list.stream().filter(v -> v.getParamName().equals(paramName)).collect(Collectors.toList()).get(0);
	}
	
	private List<Value> obtainValues() throws IOException{
		Path filePath = Paths.get(System.getProperty("user.dir"), "src/test/resources/empty-config.properties");
		List<String> lines = Files.readAllLines(filePath);
		Parser p = new Parser();
		List<Value> props = p.parse(lines); 
		return props; 
	}
	
}
