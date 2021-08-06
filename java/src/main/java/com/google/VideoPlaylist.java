package com.google;
import java.util.*;

/** A class used to represent a Playlist */
class VideoPlaylist implements Comparable<VideoPlaylist>{
	
	private final String name;
	private ArrayList<Video> videoList;
	
	public VideoPlaylist(String name)
	{
		this.name = name;
		this.videoList = new ArrayList<Video>();
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public ArrayList<Video> getVideoList()
	{
		return this.videoList;
	}
	
	public void addVideo(Video v)
	{
		this.videoList.add(v);
	}

	@Override
	public int compareTo(VideoPlaylist o) {
		// TODO Auto-generated method stub
		return this.getName().compareTo(o.getName());
	}

}
