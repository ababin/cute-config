package ru.absoft.util.cuteconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.absoft.util.cuteconfig.Context;
import ru.absoft.util.cuteconfig.Context.Pair;

public class ContextTest {
	
	Context ctx = new Context();
	
	@Test
	public void test_isList() {
		assertFalse(ctx.isStartList("12323542"));
		assertTrue(ctx.isStartList("[some.property]"));
		assertTrue(ctx.isStartList("[  so me.pro perty ]"));
	}
	
	@Test
	public void test_getListParamName() {
		assertNull(ctx.getListParamName("12323542"));
		assertEquals("some.property", ctx.getListParamName("[some.property]"));
		assertEquals("some.property", ctx.getListParamName("[  so me.pro perty ]"));
	}
	
	@Test
	public void test_isMap() {
		assertFalse(ctx.isStartMap("12323542"));
		assertFalse(ctx.isStartMap("[some.property]"));
		assertTrue(ctx.isStartMap("{ some. property }"));
		assertTrue(ctx.isStartMap("{some.property}"));
	}
	
	@Test
	public void test_getMapParamName() {
		assertNull(ctx.getMapParamName("12323542"));
		assertNull(ctx.getMapParamName("[some.property]"));
		assertEquals("some.property", ctx.getMapParamName("{ some. property }"));
		assertEquals("some.property", ctx.getMapParamName("{some.property}"));
	}
	
	@Test
	public void test_parseLine() {
		Pair pair = ctx.parseLine("qwe.rty=123");
		assertEquals("qwe.rty", pair.key);
		assertEquals("123", pair.value);
	}
	
	@Test
	public void test_parseLine2() {
		Pair pair = ctx.parseLine("qwe.rty.uiop=123");
		assertEquals("qwe.rty.uiop", pair.key);
		assertEquals("123", pair.value);
	}
	
	@Test
	public void test_parseLine3() {
		Pair pair = ctx.parseLine("qwe.rty  =  123");
		assertEquals("qwe.rty", pair.key);
		assertEquals("123", pair.value);
	}
	
}
