package com.wwuhoo;

import org.jfugue.player.Player;
/*
public class HelloWorld {
  public static void main(String[] args) {
    Player player = new Player();
    player.play("V0 I[Guitar] Eq Ch. | Eq Ch. | Dq Eq Dq Cq   V1 I[Flute] Rw | Rw | GmajQQQ CmajQ");
  }
}
*/
import org.jfugue.theory.Note;

/*
public class HelloWorld {
    public static void main(String[] args) {
        Player player = new Player();
        player.play("V0 I[Distortion_Guitar] D4q F4q G4q. | D4i Ri F4i Ri Ab4i G4h | D4q F4q G4q. | F4q D4i D4q.");
   }
}
*/

public class HelloWorld {
    public static void main(String[] args) {
        Player player = new Player();
        //player.play("T110 " +
           // "V0 I[Distortion_Guitar] D4q F4q  G4q.  | D4i Ri F4i  Ri Ab4i G4h | D4q F4q  G4q. | F4i  D4qh. " +
           // "V1 I[Overdriven_Guitar] G4q Bb4q C5q.  | G4i Ri Bb4i Ri Db5i C5h | G4q Bb4q C5q. | Bb4i G4qh.");
        
        System.out.println((Note.getFrequencyForNote("E#3")));
    }
}