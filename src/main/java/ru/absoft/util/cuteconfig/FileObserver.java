package ru.absoft.util.cuteconfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import lombok.Getter;
import ru.absoft.util.cuteconfig.loader.Loader;
import ru.absoft.util.cuteconfig.loader.LoaderFactory;
import ru.absoft.util.cuteconfig.model.Value;

class FileObserver {

	private final Timer timer;
	private final Loader fileLoader;
	private final ConfigurationListener confListener;
	private final PostProcessor postProcessor;

	private volatile @Getter Map<String, Value> values;

	public FileObserver(String path, long checkIntervalMs, ConfigurationListener errorListener,
			PostProcessor postProcessor) throws FileNotFoundException {

		fileLoader = LoaderFactory.obtainLoader(path);
		this.confListener = errorListener;
		this.postProcessor = postProcessor;
		this.timer = new Timer(true);
		readFile();
		timer.schedule(new LoaderTask(), checkIntervalMs, checkIntervalMs);
	}

	private boolean readFile() {
		try {
			List<String> lines = fileLoader.readFile();
			values = postProcess(new Parser().parse(lines));
			return true;
		} catch (IOException e) {
			if (confListener != null) {
				confListener.onReadFileError(e.getMessage(), e);
			}
		}
		return false;
	}

	private Map<String, Value> postProcess(List<Value> parsed) {
		Map<String, Value> map = parsed.stream().collect(Collectors.toMap(v -> v.getParamName(), v -> v)); 
		if(postProcessor == null) {
			return map;
		}
		return postProcessor != null ? postProcessor.process(map) : map;
	}
	
	private class LoaderTask extends TimerTask {

		@Override
		public void run() {
			try {
				if (fileLoader.fileIsChanged()) {
					if (readFile()) {
						if (confListener != null) {
							confListener.onFileChanged(fileLoader.getLastTimeModified());
						}
					}
				}
			} catch (IOException e) {
				if (confListener != null) {
					confListener.onReadFileError(e.getMessage(), e);
				}
				return;
			}
		}

	}

}
