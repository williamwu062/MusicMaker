package com.wwuhoo;

import java.util.ArrayList;

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
 * 
 * @author williamwu
 */
public class PitchDetector implements PitchDetectionHandler {

	private AudioDispatcher dispatcher;
	private Mixer mixer;
	private PitchEstimationAlgorithm algorithm;
	private TargetDataLine line;

	private ArrayList<float[]> pitchInfo;

	/**
	 * Sets the audio format to a default.
	 * 
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
	 * Initializes algorithm parameter and finds an appropriate mixer to use
	 * (Default Audio Device).
	 */
	public PitchDetector() {
		algorithm = PitchEstimationAlgorithm.YIN;

		pitchInfo = new ArrayList<float[]>();

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
	 * 
	 * @throws LineUnavailableException if the line cannot be opened due to resource
	 *                                  restrictions.
	 */
	public void startMixer() throws LineUnavailableException {
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
	public void closeMixer() {
		line.stop();
		mixer.close();
		System.out.println("Closed");
	}

	/**
	 * Detects how close the detected pitch is to the pitch of the closest guitar
	 * string.
	 * 
	 * @param pitch the detected pitch
	 * @return an array with the closest note in Hz, and the value of the detected
	 *         pitch - closest guitar string pitch.
	 */
	private float[] detectCloseness(float pitch) {
		int size = 0;
		for (int i = 0; i < Notes.notes_arr.length; i++) {
			for (int j = 0; j < Notes.notes_arr[i].length; j++) {
				size++;
			}
		}
		
		float[] closeness = new float[size];

		float closest_note = 0;
		float min = Integer.MAX_VALUE;
		float temp_min = min;
		int min_key = 0;

		for (int i = 0; i < Notes.notes_arr.length; i++) {
			for (int j = 0; j < Notes.notes_arr[i].length; j++) {
				closeness[i+j] = (float) (pitch - Notes.notes_arr[i][j]);

				if (min > (temp_min = Math.min(min, Math.abs(closeness[i+j])))) {
					min = temp_min;
					closest_note = (float) Notes.notes_arr[i][j];
					min_key = i+j;
				}
			}
		}
		return new float[] { closest_note, closeness[min_key] };
	}

	private void infoToList(float timeStamp, float pitch, float rms) {
		pitchInfo.add(new float[] { timeStamp, rms, pitch });
	}

	@Override
	public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
		if (pitchDetectionResult.getPitch() != -1) {
			double timeStamp = audioEvent.getTimeStamp();
			float pitch = pitchDetectionResult.getPitch();
			float probability = pitchDetectionResult.getProbability();
			double rms = audioEvent.getRMS() * 100;

			float[] closeness = detectCloseness(pitch);
			float closest_note = closeness[0];
			String note = Notes.getNote(closest_note);

			//String message = String.format("Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f )\n",
					//timeStamp, pitch, probability, rms);

			String message = String.format("%s from %s", closeness[1], note);
			System.out.println(message);

			infoToList((float) timeStamp, pitch, (float) rms);
		}
	}

	public ArrayList<float[]> getList() {
		return pitchInfo;
	}

	/**
	 * Gets the notes of an float[] arrayList formatted to be in the form:
	 * {timeStamp, rms, pitch}.
	 * 
	 * @param list the arrayList holding the pitch information.
	 */
	public static void getNotes(ArrayList<float[]> list) {
		ArrayList<float[]> notesAndTime = new ArrayList<float[]>();
		float prevRMS = 0;
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				prevRMS = list.get(i)[1];
				notesAndTime.add(new float[] { list.get(i)[0], list.get(i)[2] });

			} else {
				// prevRMS =
			}
		}
	}

}
