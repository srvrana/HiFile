package com.snoof.HiFile.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.beaglebuddy.mp3.MP3;
import com.snoof.HiFile.constants.Constants;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SetupUtilsOLDVERSION {

	public static void setMusicDirectory(Stage primaryStage) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Music Directory");
		File defaultDirectory = new File("c:/");
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(primaryStage);
		ConfigFileManger.getProps().setProperty(Constants.MUSIC_DIRECTORY, selectedDirectory.toString());
	}

	public static Map<String, Object> getFileList(File rootFile) {
		Map<String, Object> folder = new HashMap<>();
		for (File file : rootFile.listFiles()) {
			if (file.isDirectory()) {
				folder.put(file.getName(), getFileList(file));
			} else {
				if (file.getName().endsWith(".mp3")) {
					try {
						MP3 mp3 = new MP3(file.getAbsolutePath());
						folder.put(file.getName(), mp3);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return folder;
	}

	@SuppressWarnings("unchecked")
	public static void printMusicMap(Map<String, Object> input, String buffer) {
		for (String key : input.keySet()) {
			Object value = input.get(key);
			if (value instanceof MP3) {
				// Key == File name
				System.out.println(buffer + ((MP3) value).getBand()+" : " +((MP3) value).getTitle());
			} else if (value instanceof Map) {
				// Key == folder name
				System.out.println(buffer + key);
				printMusicMap((Map<String, Object>) value, buffer.concat("\t"));
			}
		}
	}
}
