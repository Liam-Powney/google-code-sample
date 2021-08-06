package com.google;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video playingVideo;
  private boolean vidIsPaused;
  private HashMap<String, VideoPlaylist> playlists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    this.playingVideo = null;
    this.vidIsPaused = false;
    this.playlists = new HashMap<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }
  
  public void showAllVideos() {
	  // initial string output 
	  System.out.println("Here's a list of all available videos:");
	  // create a string array of each output that can be sorted into lexicographical order 
	  String[] output = new String[videoLibrary.getVideos().size()];
	  // makes correctly formatted string for each video and adds it to the string array 
	  int counter = 0;
	  for (var entry : videoLibrary.getVideos())
	  {
		  StringJoiner line = new StringJoiner(" ");
		  line.add(entry.getTitle());
		  line.add("(" + entry.getVideoId() + ")");
		  StringJoiner tagString = new StringJoiner(" ", "[", "]");
		  for (String tag : entry.getTags())
		  {
			  tagString.add(tag);
		  }
		  line.add(tagString.toString());
		  if ( entry.getIsFlagged() )
		  {
			  line.add("-");
			  line.add("FLAGGED");
			  line.add(entry.getFlagReason());
		  }
		  output[counter] = line.toString();
		  counter++;
	  }
	  // sorts the string array 
	  Arrays.sort(output);
	  // prints the output
	  for ( String line : output )
	  {
		  System.out.println(line);
	  }
  }

  public void playVideo(String videoId) {
	  // search for a video mathcing the id and play that video if it exists and the return
	  for ( Video v : videoLibrary.getVideos())
	  {
		  if ( videoId.equals(v.getVideoId()) )
		  {
			  if ( v.getIsFlagged() )
			  {
				  System.out.println("Cannot play video: Video is currently flagged " + v.getFlagReason());
			  }
			  else
			  {
				// if something is already playing, stop that video
				  if ( playingVideo != null)
				  {
					  stopVideo();
				  }
				  playingVideo = v;
				  vidIsPaused = false;
				  System.out.println("Playing video: " + v.getTitle());
			  }
			  return;
		  }
	  }
	  // if no video found, alert user
	  System.out.println("Cannot play video: Video does not exist");
  }

  public void stopVideo() {
	  // if there is no video playing, alert user
	  if (playingVideo == null)
	  {
		  System.out.println("Cannot stop video: No video is currently playing");
	  }
	  // otherwise, stop the currently playing video
	  else
	  {
		  System.out.println("Stopping video: " + playingVideo.getTitle());
		  playingVideo = null;
	  }
  }

  public void playRandomVideo() {
	  // copy the current video library and remove all flagged videos
	  if ( videoLibrary.getUnflaggedVideos().isEmpty() )
	  {
		  System.out.println("No videos available");
	  }
	  else
	  {
		  int randomNumber = ThreadLocalRandom.current().nextInt( 0, videoLibrary.getUnflaggedVideos().size() );
		  playVideo( videoLibrary.getUnflaggedVideos().get(randomNumber).getVideoId() );
	  }
  }

  public void pauseVideo() {
	  if (  playingVideo == null )
	  {
		  System.out.println("Cannot pause video: No video is currently playing");
	  }
	  else if ( vidIsPaused )
	  {
		  System.out.println("Video already paused: " + playingVideo.getTitle() );
	  }
	  else
	  {
		  vidIsPaused = true;
		  System.out.println("Pausing video: " + playingVideo.getTitle() );
	  }
  }

  public void continueVideo() {
	  if (  playingVideo == null )
	  {
		  System.out.println("Cannot continue video: No video is currently playing");
	  }
	  else if ( !vidIsPaused )
	  {
		  System.out.println("Cannot continue video: Video is not paused" );
	  }
	  else
	  {
		  vidIsPaused = false;
		  System.out.println("Continuing video: " + playingVideo.getTitle() );
	  }
  }

  public void showPlaying() {
	  if ( playingVideo == null )
	  {
		  System.out.println("No video is currently playing");
	  }
	  else
	  {
		  StringJoiner output = new StringJoiner(" ");
		  output.add("Currently playing:");
		  output.add(playingVideo.getTitle());
		  output.add("(" + playingVideo.getVideoId() + ")");
		  StringJoiner tags = new StringJoiner(" ", "[", "]");
		  for ( String tag : playingVideo.getTags() )
		  {
			  tags.add(tag);
		  }
		  output.add(tags.toString());
		  if ( vidIsPaused )
		  {
			  output.add("-");
			  output.add("PAUSED");
		  }
		  System.out.println(output);
	  }
  }

  public void createPlaylist(String playlistName) {
	  if ( playlists.containsKey(playlistName.toLowerCase()))
	  {
		  System.out.println("Cannot create playlist: A playlist with the same name already exists");
	  }
	  else
	  {
		  playlists.put(playlistName.toLowerCase(), new VideoPlaylist(playlistName));
		  System.out.println("Successfully created new playlist: " + playlistName);
	  }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
	  // if the playlist exists
	  if ( playlists.containsKey(playlistName.toLowerCase()) )
	  {
		  // find the video associated with the video id if it exists
		  for ( Video v : videoLibrary.getVideos() )
		  {
			  if ( v.getVideoId().toLowerCase().equals(videoId.toLowerCase()) )
			  {
				  // if the video is flagged, alert user
				  if ( v.getIsFlagged() )
				  {
					  System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged " + v.getFlagReason());
				  }
				  // if the playlist doesn't contain the video then add it
				  else if ( ( playlists.get(playlistName.toLowerCase()).getVideoList().contains(v) ) )
				  {
					  System.out.println("Cannot add video to " + playlistName + ": Video already added");
				  }
				  // alert user that 
				  else
				  {
					  playlists.get(playlistName.toLowerCase()).addVideo(v);
					  System.out.println("Added video to " + playlistName + ": " + v.getTitle());
				  }
				  return;
			  }
		  }
		  System.out.println("Cannot add video to my_playlist: Video does not exist");
	  }
	  else
	  {
		  System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
	  }
  }

  public void showAllPlaylists() {
	  // if there are no playlists, display error
	  if ( playlists.isEmpty() )
	  {
		  System.out.print("No playlists exist yet");
	  }
	  // otherwise, show all playlists by adding playlist names to an array and sorting array before
	  //displaying for lexicographical order
	  else
	  {
		  System.out.println("Showing all playlists:");
		  
		  ArrayList<String> output = new ArrayList<>();
		  
		  for (  var entry : this.playlists.entrySet() )
		  {
			  output.add(entry.getValue().getName());
		  }
		  Collections.sort(output);
		  for ( String s : output )
		  {
			  System.out.println(s);
		  }
	  }
  }

  public void showPlaylist(String playlistName) {
	  // if the playlist exists
	  if ( playlists.containsKey(playlistName.toLowerCase()) )
	  {
		  System.out.println("Showing playlist: " + playlistName);
		  // if there are no videos in the playlist
		  if ( playlists.get(playlistName.toLowerCase()).getVideoList().isEmpty() )
		  {
			  System.out.println("No videos here yet");
		  }
		  else
		  {
			  for ( Video v : playlists.get(playlistName.toLowerCase()).getVideoList() )
			  {
				  StringJoiner output = new StringJoiner(" ");
				  output.add(v.getTitle());
				  output.add("(" + v.getVideoId() + ")" );
				  StringJoiner tags = new StringJoiner(" ", "[", "]");
				  for ( String tag : v.getTags())
				  {
					  tags.add(tag);
				  }
				  output.add(tags.toString());
				  if ( v.getIsFlagged() )
				  {
					  output.add("-");
					  output.add("FLAGGED");
					  output.add(v.getFlagReason());
				  }
				  System.out.println(output.toString());
			  }
		  }
	  }
	  else
	  {
		  System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
	  }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
	  // if the playlist exists
	  if ( playlists.containsKey(playlistName.toLowerCase()) )
	  {
		  // find the video user is wanting to remove if it exists
		  for ( Video v : playlists.get(playlistName.toLowerCase()).getVideoList() )
		  {
			  if ( v.getVideoId().toLowerCase().equals(videoId.toLowerCase()))
			  {
				  // remove video
				  playlists.get(playlistName.toLowerCase()).getVideoList().remove(v);
				  System.out.println("Removed video from " + playlistName + ": " + v.getTitle() );
				  return;
			  }
		  }
		  // if video is in library but not playlist, alert user
		  if ( videoLibrary.getVideos().stream().anyMatch( o -> o.getVideoId().equals(videoId) ) )
		  {
			  System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
		  }
		  // if video doesn't exist, alert user
		  else
		  {
			  System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
		  } 
	  }
	  // if playlist doesn't exist, alert user
	  else
	  {
		  System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
	  }
  }

  public void clearPlaylist(String playlistName) {
	  // if the playlist exists
	  if ( playlists.containsKey(playlistName.toLowerCase()) )
	  {
		  playlists.get(playlistName.toLowerCase()).getVideoList().clear();
		  System.out.println("Successfully removed all videos from " + playlistName);
	  }
	  else
	  {
		  System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
	  }
  }

  public void deletePlaylist(String playlistName) {
	  // if the playlist exists
	  if ( playlists.containsKey(playlistName.toLowerCase()) )
	  {
		  playlists.remove(playlistName.toLowerCase(), playlists.get(playlistName.toLowerCase()));
		  System.out.println("Deleted playlist: " + playlistName);
	  }
	  else
	  {
		  System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
	  }
  }

  public void searchVideos(String searchTerm) {
	  // create an output list of video that match the search term
	  ArrayList<Video> outputList = new ArrayList<>();
	  // if a video in the video library contains the searchterm string, add it to the output
	  for ( Video v : videoLibrary.getVideos())
	  {
		  if (   ( v.getTitle().toLowerCase().contains(searchTerm) )  &&  ( !v.getIsFlagged() )    )
		  {
			  outputList.add(v);
		  }
	  }
	  // sort the output into lexicographical order
	  Collections.sort(outputList);
	  // show user results
	  if ( outputList.isEmpty() )
	  {
		  System.out.println("No search results for " + searchTerm);
	  }
	  else
	  {
		  System.out.println("Here are the results for " + searchTerm + ":");
		  for ( Video v : outputList )
		  {
			  StringJoiner line = new StringJoiner(" ");
			  line.add( ( outputList.indexOf(v)+1 ) + ")" );
			  line.add(v.getTitle());
			  line.add("(" + v.getVideoId() + ")");
			  StringJoiner tagString = new StringJoiner(" ", "[", "]");
			  for (String tag : v.getTags())
			  {
				  tagString.add(tag);
			  }
			  line.add(tagString.toString());
			  System.out.println(line.toString());
		  }
		  System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
		  System.out.println("If your answer is not a valid number, we will assume it's a no.");
		  // get input from user
		  var scanner = new Scanner(System.in);
		  var input = scanner.nextLine();
		  // if number is valid, play the corresponding video
		  try 
		  {
			  Integer inputInt = Integer.parseInt(input);
			  if ( ( inputInt > 0 ) && ( inputInt <= outputList.size() ) )
			  {
				  String id = outputList.get(inputInt-1).getVideoId();
				  playVideo(id);
			  }
		  }
		  // if can't parse the input as an int
		  catch (NumberFormatException e)
		  {
			  return;
		  }
	  }
  }

  public void searchVideosWithTag(String videoTag) {
	// create an output list of video that match the search term
		  ArrayList<Video> outputList = new ArrayList<>();
		  // if a video in the video library contains the searchterm string, add it to the output
		  for ( Video v : videoLibrary.getVideos())
		  {
			  if (  ( v.getTags().stream().anyMatch( o -> o.toLowerCase().equals(videoTag.toLowerCase()) ) ) && ( !v.getIsFlagged() )   )
			  {
				  outputList.add(v);
			  }
		  }
		  // sort the output into lexicographical order
		  Collections.sort(outputList);
		  // show user results
		  if ( outputList.isEmpty() )
		  {
			  System.out.println("No search results for " + videoTag);
		  }
		  else
		  {
			  System.out.println("Here are the results for " + videoTag + ":");
			  for ( Video v : outputList )
			  {
				  StringJoiner line = new StringJoiner(" ");
				  line.add( ( outputList.indexOf(v)+1 ) + ")" );
				  line.add(v.getTitle());
				  line.add("(" + v.getVideoId() + ")");
				  StringJoiner tagString = new StringJoiner(" ", "[", "]");
				  for (String tag : v.getTags())
				  {
					  tagString.add(tag);
				  }
				  line.add(tagString.toString());
				  System.out.println(line.toString());
			  }
			  System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
			  System.out.println("If your answer is not a valid number, we will assume it's a no.");
			  // get input from user
			  var scanner = new Scanner(System.in);
			  var input = scanner.nextLine();
			  // if number is valid, play the corresponding video
			  try 
			  {
				  Integer inputInt = Integer.parseInt(input);
				  if ( ( inputInt > 0 ) && ( inputInt <= outputList.size() ) )
				  {
					  String id = outputList.get(inputInt-1).getVideoId();
					  playVideo(id);
				  }
			  }
			  // if can't parse the input as an int
			  catch (NumberFormatException e)
			  {
				  return;
			  }
		  }
  }

  public void flagVideo(String videoId) {
	  // if the video id matches the id of a video in the library 
	  for ( Video v : videoLibrary.getVideos() )
	  {
		  if ( v.getVideoId().toLowerCase().equals(videoId.toLowerCase()))
		  {
			  // is the video already flagged?
			  if ( v.getIsFlagged() )
			  {
				  System.out.println("Cannot flag video: Video is already flagged");
			  }
			  else
			  {
				  // if the video is currently playing, stop the video
				  if (   ( this.playingVideo != null) && ( this.playingVideo == v )   )
				  {
						  stopVideo();
				  }
				  v.flagVideo();
				  System.out.println("Successfully flagged video: " + v.getTitle() + " " + v.getFlagReason() );
			  }
			  return;
		  }
	  }
	  // if there is no video match
	  System.out.println("Cannot flag video: Video does not exist");
  }

  public void flagVideo(String videoId, String reason) {
	// if the video id matches the id of a video in the library 
		  for ( Video v : videoLibrary.getVideos() )
		  {
			  if ( v.getVideoId().toLowerCase().equals(videoId.toLowerCase()))
			  {
				  // is the video already flagged?
				  if ( v.getIsFlagged() )
				  {
					  System.out.println("Cannot flag video: Video is already flagged");
				  }
				  else
				  {
					// if the video is currently playing, stop the video
					  if (   ( this.playingVideo != null) && ( this.playingVideo == v )   )
					  {
							  stopVideo();
					  }
					  v.flagVideo();
					  v.setFlagReason(reason);
					  System.out.println("Successfully flagged video: " + v.getTitle() + " " + v.getFlagReason());
				  }
				  return;
			  }
		  }
		  // if there is no video match
		  System.out.println("Cannot flag video: Video does not exist");
  }

  public void allowVideo(String videoId) {
	// if the video id matches the id of a video in the library 
		  for ( Video v : videoLibrary.getVideos() )
		  {
			  if ( v.getVideoId().toLowerCase().equals(videoId.toLowerCase()))
			  {
				  // is the video already allowed?
				  if ( !v.getIsFlagged() )
				  {
					  System.out.println("Cannot remove flag from video: Video is not flagged");
				  }
				  else
				  {
					  v.allowVideo();
					  v.setFlagReason("Not supplied");
					  System.out.println("Successfully removed flag from video: " + v.getTitle() );
				  }
				  return;
			  }
		  }
		  // if there is no video match
		  System.out.println("Cannot remove flag from video: Video does not exist");
  }
}