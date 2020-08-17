package com.wwuhoo;

import java.util.HashMap;

public class Notes {
	
	private static HashMap<Float, String> notes_map = new HashMap<Float, String>();
	
	public final static float[] E = new float[] {82.407f, 164.814f, 329.628f};
	public final static float[] F = new float[] {87.307f, 174.614f, 349.228f};
	public final static float[] Fshp = new float[] {92.499f, 184.997f, 369.994f};
	public final static float[] G = new float[] {97.999f, 195.998f, 391.995f};
	public final static float[] Gshp = new float[] {103.826f, 207.652f, 415.305f};
	public final static float[] A = new float[] {110f, 220f, 440f};
	public final static float[] Ashp = new float[] {116.541f, 233.082f, 466.164f};
	public final static float[] B = new float[] {123.471f, 246.942f, 493.883f};
	public final static float[] C = new float[] {130.813f, 261.626f, 523.251f};
	public final static float[] Cshp = new float[] {138.591f, 277.183f, 554.365f};
 	public final static float[] D = new float[] {146.832f, 293.665f, 587.330f};
	public final static float[] Dshp = new float[] {155.563f, 311.127f, 622.254f};
	
	public final static float[][] notes_arr = new float[][] {E, F, Fshp, G, Gshp,
		A, Ashp, B, C, Cshp, D, Dshp};
	
	private static void getMapping() {
		if (notes_map.isEmpty()) {
			for (int i = 0; i < E.length; i++) {
				notes_map.put(E[i], "E" + (i + 3));
			}
			for (int i = 0; i < F.length; i++) {
				notes_map.put(F[i], "F" + (i + 3));
			}
			for (int i = 0; i < Fshp.length; i++) {
				notes_map.put(Fshp[i], "F#" + (i + 3));
			}
			for (int i = 0; i < G.length; i++) {
				notes_map.put(G[i], "G" + (i + 3));
			}
			for (int i = 0; i < Gshp.length; i++) {
				notes_map.put(Gshp[i], "G#" + (i + 3));
			}
			for (int i = 0; i < A.length; i++) {
				notes_map.put(A[i], "A" + (i + 3));
			}
			for (int i = 0; i < Ashp.length; i++) {
				notes_map.put(Ashp[i], "A#" + (i + 3));
			}
			for (int i = 0; i < B.length; i++) {
				notes_map.put(B[i], "B" + (i + 3));
			}
			for (int i = 0; i < C.length; i++) {
				notes_map.put(C[i], "C" + (i + 4));
			}
			for (int i = 0; i < Cshp.length; i++) {
				notes_map.put(Cshp[i], "C#" + (i + 4));
			}
			for (int i = 0; i < D.length; i++) {
				notes_map.put(D[i], "D" + (i + 4));
			}
			for (int i = 0; i < Dshp.length; i++) {
				notes_map.put(Dshp[i], "D#" + (i + 4));
			}
		}
	}
	
	public static String getNote(float Hz) {
		getMapping();
		return notes_map.get(Hz);
	}
}
