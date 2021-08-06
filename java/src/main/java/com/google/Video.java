package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video implements Comparable<Video>{

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean isFlagged;
  private String flagReason;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
    this.isFlagged = false;
    this.flagReason = "(reason: Not supplied)";
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }
  
  public boolean getIsFlagged()
  {
	  return this.isFlagged;
  }
  
  public void flagVideo()
  {
	  this.isFlagged = true;
  }
  
  public void allowVideo()
  {
	  this.isFlagged = false;
  }
  
  public void setFlagReason(String reason)
  {
	  this.flagReason = "(reason: " + reason + ")";
  }
  
  public String getFlagReason()
  {
	  return this.flagReason;
  }
  
  @Override
	public int compareTo(Video o) {
		// TODO Auto-generated method stub
		return this.getTitle().compareTo(o.getTitle());
	}
}
