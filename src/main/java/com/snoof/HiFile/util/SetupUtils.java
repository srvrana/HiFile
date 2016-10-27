package com.snoof.HiFile.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import com.snoof.HiFile.constants.Constants;
import com.snoof.HiFile.vo.Album;
import com.snoof.HiFile.vo.Artist;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SetupUtils {
	private static final Logger LOGGER = LogManager.getLogger(SetupUtils.class);

	public static void setMusicDirectory(Stage primaryStage) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Music Directory");
		File defaultDirectory = new File("c:/");
		chooser.setInitialDirectory(defaultDirectory);
		File selectedDirectory = chooser.showDialog(primaryStage);
		ConfigFileManger.getProps().setProperty(Constants.MUSIC_DIRECTORY, selectedDirectory.toString());
		LOGGER.info("Directory Set To: " + selectedDirectory.toString());
	}

	public static Map<String, Artist> getFileList(File rootFile) {
		LOGGER.info("Starting File Processing");
		Long start = System.currentTimeMillis();
		List<Mp3File> musicArray = composeMusicArray(rootFile);
		LOGGER.info("Done Composing Music Array - Time: "
				+ TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - start) + ":"
				+ TimeUnit.SECONDS.toMinutes(System.currentTimeMillis() - start) + " Soungs Found: "
				+ musicArray.size());
		Map<String, Artist> result = processFileList(musicArray);
		LOGGER.info("Done Composing Music Map - Time: "
				+ TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - start) + ":"
				+ TimeUnit.SECONDS.toMinutes(System.currentTimeMillis() - start) + " Soungs Found: "
				+ musicArray.size());
		return result;
	}

	public static List<Mp3File> composeMusicArray(File rootFile) {
		ArrayList<Mp3File> music = new ArrayList<>();
		for (File file : rootFile.listFiles()) {
			LOGGER.debug("Processing: " + file.getName());
			if (file.isDirectory()) {
				music.addAll(composeMusicArray(file));
			} else {
				if (file.getName().endsWith(".mp3")) {
					try {
						music.add(new Mp3File(file.getAbsolutePath()));
					} catch (Exception e) {
						LOGGER.error("Unable to process file: " + file.getAbsolutePath());
					}
				}
			}
		}
		return music;
	}

	private static Map<String, Artist> processFileList(List<Mp3File> music) {
		LOGGER.info("Processing Music...");
		HashMap<String, Artist> resultSet = new HashMap<>();
		ID3v1 tag;
		for (Mp3File mp3 : music) {
			if (mp3.hasId3v2Tag()) {
				tag = mp3.getId3v2Tag();
			} else if (mp3.hasId3v1Tag()) {
				tag = mp3.getId3v1Tag();
			} else {
				LOGGER.error("Unsupported Tag Type - " + mp3.getFilename());
				continue;
			}
			LOGGER.debug("File: " + tag.getArtist() + "-" + tag.getTitle());
			if (resultSet.containsKey(tag.getArtist())) {
				LOGGER.debug("\tBand Found");
				List<Album> albums = resultSet.get(tag.getArtist()).getAlbums();
				Album album = new Album(tag.getArtist(), tag.getAlbum(), tag.getYear());
				if (albums.contains(album)) {
					LOGGER.debug("\t\tAlbum Found");
					albums.get(albums.indexOf(album)).getSongs().add(mp3);
				} else {
					LOGGER.debug("\t\tAlbum Not Found");
					album.getSongs().add(mp3);
					albums.add(album);
				}
			} else {
				LOGGER.debug("\tBand Not Found");
				Artist artist = new Artist(tag.getArtist());
				Album album = new Album(tag.getArtist(), tag.getAlbum(), tag.getYear());
				album.getSongs().add(mp3);
				artist.getAlbums().add(album);
				resultSet.put(tag.getArtist(), artist);
			}

		}
		return resultSet;
	}

	public static void printMusicList(Map<String, Artist> input) {
		for (String artist : input.keySet()) {
			System.out.println(artist);
			for (Album album : input.get(artist).getAlbums()) {
				System.out.println("\t" + album.getName());
				for (Mp3File song : album.getSongs()) {
					if (song.hasId3v1Tag())
						System.out.println("\t\t" + song.getId3v1Tag().getTitle());
					else
						System.out.println("\t\t" + song.getId3v2Tag().getTitle());
				}
			}
		}
	}
}
