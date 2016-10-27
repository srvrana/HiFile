package com.snoof.HiFile.vo;

import java.util.ArrayList;
import java.util.List;

import com.mpatric.mp3agic.Mp3File;

public class Album {
	private String artist;
	private String name;
	private int year;
	private List<Mp3File> songs;

	public Album(String artist, String name, String year) {
		this.artist = artist;
		this.name = name;
		try {
			this.year = Integer.valueOf(year);
		} catch (Exception e) {

		}
		this.songs = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<Mp3File> getSongs() {
		return songs;
	}

	public void setSongs(List<Mp3File> songs) {
		this.songs = songs;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Album [artist=" + artist + ", name=" + name + ", year=" + year + ", songs=" + songs + "]";
	}

}
