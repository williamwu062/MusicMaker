package com.wwuhoo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SoundRecorder {
	
	private static File file = new File("/Users/williamwu/Downloads/Miscellaneous/test_audio.wav");
	private TargetDataLine line;
	
	private AudioFormat createAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		return format;
	}
	
	public void start() throws LineUnavailableException, IOException{
		AudioFormat format = createAudioFormat();
		
		DataLine.Info datalineInfo = new DataLine.Info(TargetDataLine.class, format);
		line = (TargetDataLine) AudioSystem.getLine(datalineInfo);
		line.open(format);
		line.start();
		System.out.println("start capturing");
		
		AudioInputStream stream = new AudioInputStream(line);
		
		AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
	}
	
	public void finish() {
		line.stop();
		line.close();
		System.out.println("end");
	}
	
	public static void main(String[] strings) {
		SoundRecorder recorder = new SoundRecorder();
		System.out.println("print");
		try {
			boolean test = file.createNewFile();
			System.out.println(test);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JFrame frame = new JFrame("GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		JButton button = new JButton("Stop");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				recorder.finish();
				System.out.println("Finished");
			}
		});
		panel.add(button);
		frame.add(panel);
		frame.setSize(400, 400);
		frame.setResizable(false);
		frame.setVisible(true);

		/*
		Thread stopper = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				recorder.finish();
			}
		});
		
		stopper.start();
		*/
		
				
		try {
			recorder.start();
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		
	}
}
