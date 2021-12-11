package factory;

import java.io.File;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.images.Artwork;

import bean.musicInfoBean;

public class mp3Tagger {
	private String decoding = "ISO-8859-1";
	private String edcoding = "UTF-8";
	private String musicName;
	private String artist;
	private String lyrics;
	private Artwork artWork;
	private String filePath;
	private String fileName;
	
	public mp3Tagger(File file) {		
		/* 음악 파일에 대한 헤더를 불러옵니다. */
		try {
			AudioFile audioFile = AudioFileIO.read(file);
			Tag tag = audioFile.getTag();
			musicName =  tag.getFirst(FieldKey.TITLE);
			artist = tag.getFirst(FieldKey.ARTIST);
			lyrics = tag.getFirst(FieldKey.LYRICS);
			lyrics = "<html><body style='text-align:center;'><div style=\"width:90px; font-size:8px;\">" + lyrics + "</div></body></html>";
			lyrics = lyrics.replaceAll("(\r\n|\r|\n|\n\r)", "<br/>");
			artWork = tag.getFirstArtwork();
			filePath = file.getPath();
			fileName = file.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public musicInfoBean getMp3Tag() {
		musicInfoBean data = new musicInfoBean();
		data.setMusicName(musicName);
		data.setArtist(artist);
		data.setLyrics(lyrics);
		data.setArtWork(getArtWorkBinary());
		data.setFileName(fileName);
		data.setFilePath(filePath);
		return data;
	}
	
	public byte[] getArtWorkBinary() {
		if(artWork != null) {
			return artWork.getBinaryData();				
		} else {
			return null;
		}
	}

	public String getDecoding() {
		return decoding;
	}

	public void setDecoding(String decoding) {
		this.decoding = decoding;
	}

	public String getEdcoding() {
		return edcoding;
	}

	public void setEdcoding(String edcoding) {
		this.edcoding = edcoding;
	}

	public String getMusicName() {
		return musicName;
	}

	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	public Artwork getArtWork() {
		return artWork;
	}

	public void setArtWork(Artwork artWork) {
		this.artWork = artWork;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
