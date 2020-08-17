package com.wwuhoo;

import javax.sound.sampled.LineUnavailableException;

public class Main {

	public static void main(String[] args) {
		PitchDetector detector = new PitchDetector();

		/*
		Thread stopper = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				detector.closeMixer();
			}
		});
		stopper.start();
		*/

		try {
			detector.startMixer();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
