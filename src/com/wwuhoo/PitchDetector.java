package com.wwuhoo;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Mixer.Info;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm;

/**
 * This class captures the pitch in Hertz of a live sound.
 * @author williamwu
 */
public class PitchDetector implements PitchDetectionHandler {

	private AudioDispatcher dispatcher;
	private Mixer mixer;
	private PitchEstimationAlgorithm algorithm;
	private TargetDataLine line;
	
	/**
	 * Sets the audio format to a default.
	 * @return the audio format.
	 */
	private AudioFormat setAudioFormat() {
		float sampleRate = 44100;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;

		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	/**
	 * Initializes algorithm parameter and finds an appropriate mixer to use (Default Audio Device).
	 */
	public PitchDetector() {
		algorithm = PitchEstimationAlgorithm.YIN;

		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (final Info mixerinfo : mixers) {
			if (AudioSystem.getMixer(mixerinfo).getSourceLineInfo().length != 0
					&& AudioSystem.getMixer(mixerinfo).getTargetLineInfo().length != 0) {
				// Mixer capable of recording audio if target LineWavelet length != 0
				mixer = AudioSystem.getMixer(mixerinfo);
			}
		}
	}

	/**
	 * Starts the mixer and enables recording of audio.
	 * @throws LineUnavailableException if the line cannot be opened due to resource restrictions.
	 */
	private void startMixer() throws LineUnavailableException {
		AudioFormat format = setAudioFormat();

		DataLine.Info datalineInfo = new DataLine.Info(TargetDataLine.class, format);
		line = (TargetDataLine) mixer.getLine(datalineInfo);

		int bufferSize = 1024;
		int overlap = 0;
		line.open(format, bufferSize);
		line.start();
		AudioInputStream temp_stream = new AudioInputStream(line);
		JVMAudioInputStream stream = new JVMAudioInputStream(temp_stream);

		dispatcher = new AudioDispatcher(stream, bufferSize, overlap);

		dispatcher.addAudioProcessor(new PitchProcessor(algorithm, format.getSampleRate(), bufferSize, this));

		new Thread(dispatcher, "Audio dispatching").start();
	}

	/**
	 * Closes the line and the mixer.
	 */
	private void closeMixer() {
		line.stop();
		mixer.close();
		System.out.println("Closed");
	}
	
	/**
	 * Detects how close the detected pitch is to the pitch of the closest guitar string.
	 * @param pitch the detected pitch
	 * @return an array with the closest note in Hz, and the value of the detected pitch - closest guitar string pitch.
	 */
	private float[] detectCloseness(float pitch) {
		float[] closeness = new float[Notes.notes_arr.length];
		float[] temp = new float[Notes.notes_arr.length];
		
		int closest_note = 0;
		float min = Integer.MAX_VALUE;
		float temp_min = min;
		int min_key = 0;
		
		for (int i = 0; i < Notes.notes_arr.length; i++) {
			closeness[i] = pitch - Notes.notes_arr[i];
			temp[i] = Math.abs(pitch - Notes.notes_arr[i]);
			
			if (min > (temp_min=Math.min(min, temp[i]))) {
				min = temp_min;
				closest_note = Notes.notes_arr[i];
				min_key = i;
			}
		}
		return new float[] {closest_note, closeness[min_key]};
	}

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

	@Override
	public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
		if (pitchDetectionResult.getPitch() != -1) {
			double timeStamp = audioEvent.getTimeStamp();
			float pitch = pitchDetectionResult.getPitch();
			float probability = pitchDetectionResult.getProbability();
			double rms = audioEvent.getRMS() * 100;
			
			float[] closeness = detectCloseness(pitch);
			int closest_note = (int) closeness[0];
			Character note = Notes.getNote(closest_note);
			
			
//			String message = String.format("Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f )\n",
//					timeStamp, pitch, probability, rms);
			
			
			String message = String.format("%s from %c", closeness[1], note);
			System.out.println(message);
		}
	}

}
