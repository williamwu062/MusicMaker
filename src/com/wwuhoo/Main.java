package com.wwuhoo;

import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;

import org.jfugue.player.Player;

public class Main {

	public static void main(String[] args) {
		PitchDetector detector = new PitchDetector();

		Thread stopper = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				detector.closeMixer();
				ArrayList<String> arr = PitchDetector.getNotes(detector.getList());
				
				StringBuilder all_notes = new StringBuilder();
				for (int i = 0; i < arr.size(); i++) {
					if (i == 0) {
						all_notes.append(arr.get(i));
					} else {
						all_notes.append(" " + arr.get(i));
					}
				}
				
				Player player = new Player();
				player.play("V0 I[Piano] " + all_notes);
			}
		});
		stopper.start();

		try {
			detector.startMixer();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}