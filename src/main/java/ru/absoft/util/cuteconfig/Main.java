package ru.absoft.util.cuteconfig;

import java.io.IOException;
import java.util.List;

import ru.absoft.util.cuteconfig.loader.Loader;
import ru.absoft.util.cuteconfig.loader.LoaderFactory;

public class Main {

	public static void main(String[] args) throws IOException {
		Loader loader =  LoaderFactory.obtainLoader("myprop.properties");
		List<String> list = loader.readFile();
		list.stream().forEach(l -> System.out.println(l));
	}

}
