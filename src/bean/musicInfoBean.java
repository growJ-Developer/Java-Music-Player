package bean;

import java.util.Calendar;

public class musicInfoBean {
	private String musicName;				// 음악 이름 
	private String fileName;					// 파일 이름 
	private String filePath;					// 파일 경로 
	private String artist;						// 가수 
	private String lyrics;						// 가사 
	private byte[] artWork;		// 앨범 커버
	private int playCnt;						// 플레이 횟수
	private Calendar addDate;				// 추가 날짜
	private Calendar lastPlayDate;		// 마지막 재생일
	
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
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
	public byte[] getArtWork() {
		return artWork;
	}
	public void setArtWork(byte[] artWork) {
		this.artWork = artWork;
	}
	public int getPlayCnt() {
		return playCnt;
	}
	public void setPlayCnt(int playCnt) {
		this.playCnt = playCnt;
	}
	public Calendar getAddDate() {
		return addDate;
	}
	public void setAddDate(Calendar addDate) {
		this.addDate = addDate;
	}
	public Calendar getLastPlayDate() {
		return lastPlayDate;
	}
	public void setLastPlayDate(Calendar lastPlayDate) {
		this.lastPlayDate = lastPlayDate;
	}
}
