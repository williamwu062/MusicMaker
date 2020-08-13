package com.wwuhoo;

import java.util.HashMap;

public class Notes {
	
	private static HashMap<Integer, Character> notes_map = new HashMap<Integer, Character>();
	
	public final static int E = 83;
	public final static int A = 110;
	public final static int D = 146;
	public final static int G = 196;
	public final static int B = 247;
	public final static int e = 330;
	
	public final static int[] notes_arr = new int[] {E, A, D, G, B, e};
	
	private static void getMapping() {
		if (notes_map.isEmpty()) {
			notes_map.put(E, 'E');
			notes_map.put(A, 'A');
			notes_map.put(D, 'D');
			notes_map.put(G, 'G');
			notes_map.put(B, 'B');
			notes_map.put(e, 'e');
		}
	}
	
	public static Character getNote(int Hz) {
		getMapping();
		return notes_map.get(Hz);
	}
}
