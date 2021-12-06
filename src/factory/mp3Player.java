package factory;

import java.awt.Dimension;
import java.awt.Image;
import java.io.*;
import java.util.*;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;

import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import app.mainFrame;
import app.playFrame;
import javazoom.spi.mpeg.sampled.convert.DecodedMpegAudioInputStream;


public class mp3Player {
	/* 싱글톤 패턴으로 지정합니다. */
	private static mp3Player singleton = new mp3Player();
	
	private Map<String, Object> properties;		// 재생 파일에 대한 정보 
	private int currentSeconds;							// 재생 시간
	private int frameBytesUnit;							// 재생 시간 계산을 위한 fileByte
	private byte[] allBytes;								// 재생 파일의 전체 Byte
	
	private Thread mThread;								// 음악 재생을 위한 쓰레드
	private File mFile;										// 재생 음악 파일
	
	private AudioInputStream iStream;
	private AudioInputStream oStream;
	
	private FloatControl gainControl;					// 볼륨
	private boolean isVolumeDragging;				// 볼륨 설정 여부
		
	private AudioFormat mFormat ;					// 오디오 포맷 
	
	private boolean isRunning;							// 재생 진행 여부 
	private boolean isDragging;							// 드래그 여부 
	
	
	private mp3Player() {
		properties = new HashMap<String, Object>();
		currentSeconds = 0;
		isRunning = false;
		isDragging = false;
		isVolumeDragging = false;
	}

	/* 재생 동작에 대한 Action을 실행합니다. */
	public void play(String filename) {
		File newFile = new File(filename);
		this.play(newFile);
	}
	public void play(File file) {
		clear();
		mThread = new Thread(()->{
			try {
				mFile = file;
				setInputStream();						// InputStream 생성 
				rawPlay();
			} catch (Exception e) {
				/* 음악 재생에 있어서 오류가 있을 경우 실행됩니다. */
				e.printStackTrace();
			} finally {
				init();
			}
		});
		mThread.start();
	}
	
	public void clear() {
		try {
			if(oStream != null) oStream.close();
			if(iStream != null) iStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void init() {
		try {
			if(oStream != null) oStream.close();
			if(iStream != null) iStream.close();
			/* playFrame의 Timeline을 0으로 설정 */
			JSlider timeLine = (JSlider)mainFrame.getComponentByName("TIME_LINE");
			timeLine.setValue(0);
			/* 숫자의 재생 시간을 설정합니다 */
			JLabel timeSec = (JLabel)mainFrame.getComponentByName("TIME_DISPLAY");
			timeSec.setText("00:00");		
			JLabel timeSecMax = (JLabel)mainFrame.getComponentByName("TIME_MAX_DISPLAY");
			timeSec.setText("00:00");
			
			/* 설정에 따른 다음 재생을 수행합니다 */
			playFrame playFrm = (playFrame)mainFrame.getComponentByName("PLAY_PANEL");
			playFrm.nextPlay();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 쓰레드에 맞춰 재생을 수행합니다. */
	private synchronized void rawPlay() throws IOException, LineUnavailableException{
		frameBytesUnit = (Integer)properties.get("mp3.framesize.bytes");
		allBytes = oStream.readAllBytes();
		isRunning = true;
		
		try {
			setInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		byte[] data = new byte[8192];
		SourceDataLine line = getLine((AudioFormat)mFormat);
		
		/* 볼륨을 설정합니다 */
		
		if(line != null) {
			line.open();
			line.start();																	// line start 
			int nBytesRead = 0, nByteWritten = 0;
			
			gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
			JSlider volumeControl = (JSlider) mainFrame.getComponentByName("VOLUME_CONTROL");
			float max = gainControl.getMaximum();
			float min = gainControl.getMinimum();
			
			gainControl.setValue(percentToVolume(min, max, volumeControl.getValue()));
			
			while(nBytesRead != -1) {												// 파일 전체 재생 여부 체크
				/* 재생 중지 시 (isRunning이 false 일 경우) */
				while(!isRunning) {														// 재생 중지 설정 시, thread 중단  
					try {
						synchronized (mThread) {									// 쓰레드 중지 
							System.out.println(mThread.getName() + " calling thread.wait!");
							mThread.wait();
						}
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						System.out.println("Thread interrupted"); 
					}
				}
				/*  재생 위치를 조정 시 (isDragging이 true일 경우) */
				while(isDragging) {
					try {
						synchronized (mThread) {									// 드래그를 하는 동안,
							mThread.sleep(500);										// 0.5초(500ms) 쓰레드 작동 중지
							isDragging = false;											// 드래그 여부 초기화
							
							//MP3 타입의 파일일 경우, 변경한 위치에 대한 계산을 수행합니다.
							if(oStream instanceof DecodedMpegAudioInputStream) {
								try {
									int offset = 0;
									line.drain();
									line.stop();
									line.close();
									setInputStream();
									line = getLine((AudioFormat)mFormat);
									line.start();
									offset = (int)((DecodedMpegAudioInputStream)oStream).skipFrames(calcFrameFromSeconds(currentSeconds));
									oStream.read(oStream.readNBytes(offset), 0, offset);
								}  catch (EOFException e) {
									e.printStackTrace();
								} catch (IOException e) {
									e.printStackTrace();
								} catch (UnsupportedAudioFileException e) {
									e.printStackTrace();
								}
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				nBytesRead = oStream.read(data, 0, data.length);		// InputStream으로부터 data 획득
				
				if(oStream instanceof DecodedMpegAudioInputStream) {
					Map<String, Object> property = ((DecodedMpegAudioInputStream)oStream).properties();
					setCurrentSeconds(toSeconds((Long)property.get("mp3.position.microseconds")));
					
					if(!isDragging) {
						/* 컨트롤 패널의 현재 재생 시간을 설정합니다 */
						JSlider timeLine = (JSlider)mainFrame.getComponentByName("TIME_LINE");
						timeLine.setValue(getCurrentSeconds());			
						/* 숫자의 현재 재생 시간을 설정합니다 */
						JLabel timeSec = (JLabel)mainFrame.getComponentByName("TIME_DISPLAY");
						timeSec.setText(timeToDisplay(getCurrentSeconds()));
					}
					/* 현재 설정된 볼륨을 Controller에 반영합니다 */
					if(!isVolumeDragging) {
						volumeControl.setValue(volumeToPercent(min, max, gainControl.getValue()));
					}
				}
								
				if (nBytesRead != -1) {
					nByteWritten = line.write(data, 0, nBytesRead);
				}	
			}
		}
		
		/* 재생을 정지합니다. */
		line.drain();
		line.stop();
		line.close();		
	}
	
	/* 파일 재생을 위한 InputSteam을 생성합니다. */
	private void setInputStream() throws IOException, UnsupportedAudioFileException{
		if(oStream != null) {
			/* 재생 파일에 대한 Stream이 있을 경우 */ 
			oStream.close();
		}
		/* Audio Stream 생성합니다. */
		iStream =  AudioSystem.getAudioInputStream(mFile);			// 음악 파일에 대한 Stream 생성 
		setProperties(mFile);															// 재생파일에 대한 정보 설정 
		
		int ch = iStream.getFormat().getChannels();
		float rate = iStream.getFormat().getSampleRate();
				
		/* Audio Format 계산합니다. */
		mFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
			
		/* Format에 따라 계산된 InputStream 생성합니다. */
		oStream = AudioSystem.getAudioInputStream(mFormat, iStream);
	}
	
	/* 파일 재생을 위한 SourceDataLine를 생성합니다. */
	private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException{
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		res = (SourceDataLine)AudioSystem.getLine(info);				// SourceDataLine 획득 
		res.open(audioFormat);														// line Open
		return res;
	}
	
	/* 현재 재생중인 파일에 대한 정보를 설정합니다. */
	private void setProperties(File file) throws IOException, UnsupportedAudioFileException{
		AudioFileFormat baseFileFormat = AudioSystem.getAudioFileFormat(mFile);
		AudioFormat baseFormat = baseFileFormat.getFormat();
		
		if(baseFileFormat instanceof TAudioFileFormat) {
			Map<String, Object> properties = ((TAudioFileFormat)baseFileFormat).properties();
			this.properties.putAll(properties);
		}
		
		if(baseFormat instanceof TAudioFormat) {
			/* playFrame의 TimeLine 내 총 길이 설정 */
			JSlider timeLine = (JSlider)mainFrame.getComponentByName("TIME_LINE");
			timeLine.setMaximum(getDurationInSeconds());
			/* 숫자의 총 시간을 설정합니다 */
			JLabel timeSecMax = (JLabel)mainFrame.getComponentByName("TIME_MAX_DISPLAY");
			timeSecMax.setText(timeToDisplay(getDurationInSeconds()));
			
			mp3Tagger tag = new mp3Tagger(file);
			System.out.println("nowPlay : " + tag.getMusicName());
			/* 앨범 커버를 설정합니다. */
			JLabel artWorkPanel = (JLabel)mainFrame.getComponentByName("ART_WORK_PANEL");
			
			/* 이미지 사이즈를 조정합니다. */
			ImageIcon originIcon = new ImageIcon(tag.getArtWorkBinary());
			Image originImg = originIcon.getImage();
			Image changedImg = originImg.getScaledInstance(artWorkPanel.getWidth(), artWorkPanel.getHeight(), Image.SCALE_SMOOTH);
			artWorkPanel.setIcon(new ImageIcon(changedImg));
			
			/* 가사 정보를 설정합니다. */
			JLabel lycris = (JLabel)mainFrame.getComponentByName("LYRICS_PANEL");
			lycris.setText(tag.getLyrics());
			
			/* 노래 제목을 설정합니다. */
			JLabel nameLabel = (JLabel)mainFrame.getComponentByName("MUSIC_NAME_PANE");
			nameLabel.setText(tag.getMusicName());
			
			/* 가수명을 설정합니다. */
			JLabel artistLabel = (JLabel)mainFrame.getComponentByName("ARTIST_NAME_PANE");
			artistLabel.setText(tag.getArtist());
		}
	}
	
	/* 쓰레드의 전환을 진행합니다. */
	public void toggle() {
		if(mThread != null && mThread.isAlive()) {
			if(isRunning == false) {
				try {
					synchronized (mThread) {
							mThread.notify();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			isRunning = !isRunning;
		}
	}
	
	public String timeToDisplay(int time) {
		int minute = time / 60;
		int second = time % 60;
		return String.format("%02d", minute) + ":" + String.format("%02d", second);
	}

	public int toSeconds(long microSeconds) {
		return (int)(microSeconds / 1000000);
	}
	
	public int getCurrentSeconds() {
		return currentSeconds;
	}
	
	public void setCurrentSeconds(int s) {
		currentSeconds = s;
	}
	
	public int getMaxLengthOfFrames() {
		return (int)properties.get("mp3.length.frames");
	}
	
	public int getDurationInSeconds() {
		return toSeconds(((long)properties.get("duration")));
	}
	
	public int calcFrameFromSeconds(int s) {
		return (int)(s * (double)getMaxLengthOfFrames() / (double)getDurationInSeconds());
	}
	
	public boolean getIsRunning() {
		return isRunning;
	}
	
	public static mp3Player getInstance() {
		return singleton;
	}
	
	public boolean getIsDragging() {
		return isDragging;
	}
	
	public void setIsDragging(boolean dragging) {
		isDragging = dragging;
	}
	
	public long secondsToBytes() {
		return frameBytesUnit*currentSeconds;
	}
	
	public byte[] getAllBytes() {
		return allBytes;
	}
	
	public int getFrameBytesUnit() {
		return frameBytesUnit;
	}
	
	public void setVolume(float volume) {
		if(gainControl != null) {
			gainControl.setValue(percentToVolume(gainControl.getMinimum(), gainControl.getMaximum(), volume));
		}		
	}
	
	public float getVolume() {
		return gainControl.getValue();
	}
	
	public float percentToVolume(float min, float max, float setValue) {
		return min + ((max - min) / 100) * setValue;
	}
	
	public int volumeToPercent(float min, float max, float setValue) {
		return (int) (setValue + Math.abs(min) / (max - min) * 100);
	}
	
	public void setVolumeDragging(boolean isVolumeDragging) {
		this.isVolumeDragging = isVolumeDragging;
	}

}
