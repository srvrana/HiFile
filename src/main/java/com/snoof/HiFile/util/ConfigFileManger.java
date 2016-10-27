package com.snoof.HiFile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.snoof.HiFile.constants.Constants;

public class ConfigFileManger {
	private static Properties props;
	private static File propsFile;

	private ConfigFileManger() {
	}

	public static Properties getProps() {
		if (propsFile == null) {
			propsFile = new File(Constants.PROPERTIES_FILE);
			try {
				propsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (props == null) {
			loadProps();
		}
		return props;
	}

	private static void loadProps() {
		FileInputStream in;
		try {
			in = new FileInputStream(propsFile);
			props = new Properties();
			props.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveProps() {
		FileOutputStream out;
		try {
			out = new FileOutputStream(propsFile);
			props.store(out, "HiFile Config File");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}