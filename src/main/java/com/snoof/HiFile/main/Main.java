package com.snoof.HiFile.main;

import java.io.File;
import java.util.Map;

import com.snoof.HiFile.constants.Constants;
import com.snoof.HiFile.util.ConfigFileManger;
import com.snoof.HiFile.util.SetupUtils;
import com.snoof.HiFile.vo.Artist;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	Map<String, Artist> music;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("HiFile");
		StackPane root = new StackPane();
		primaryStage.setScene(new Scene(root, 2000, 1000));
		while (ConfigFileManger.getProps().getProperty(Constants.MUSIC_DIRECTORY) == null) {
			SetupUtils.setMusicDirectory(primaryStage);
		}
		while(! new File(ConfigFileManger.getProps().getProperty(Constants.MUSIC_DIRECTORY)).exists()){
			SetupUtils.setMusicDirectory(primaryStage);
		}
		
		music = SetupUtils.getFileList(new File((String)ConfigFileManger.getProps().get(Constants.MUSIC_DIRECTORY)));
		SetupUtils.printMusicList(music);
		
//		primaryStage.show();
	}

	@Override
	public void stop() {
		ConfigFileManger.saveProps();
	}


}