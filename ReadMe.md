## Mine Sweeper Project

This was a project that was done for my csci455 class at USC. The implementation is in java and you will have to be able to run the program within a desktop that allows for pop ups. 

<br/>

In order to run the program, compile all the files ending with .java:
```linux
javac *.java
```

<br/>

Afterwards, you will want to run the main program that allows for the game to be played.
```linux
java MineSweeper
```

<br/>

If you would like to change the dimensions of the game board, or add more mines, feel free to change these lines of code within the MineSweeper.java file itself.
 ```java
 private static final int FRAME_WIDTH = 400;
 private static final int FRAME_HEIGHT = 425;
   
 private static int SIDE_LENGTH = 9;
 private static int NUM_MINES = 15;
 ```
