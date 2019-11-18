// Name: Armin Bazarjani
// USC NetID: bazarjan
// USC ID Number: 4430621961
// CS 455 PA3
// Fall 2019


/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield), Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
import java.util.*;

public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // Covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // Uncovered states (all non-negative values):
   
   // values in the range [0,8] corresponds to number of mines adjacent to this square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game) (BLACK SQUARE)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game (YELLOW SQUARE WITH X)
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose) (RED SQUARE)
   // ----------------------------------------------------------   
  
   // <put instance variables here>
   public int[][] visibleField;
   private MineField mineField;
   
   private int rowSize;
   private int colSize;
   
   private int numberGuess;
   private int numberQuestion;
   
   private int mineGuessLeft;
   
   private boolean gameOver;
   

   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the mines covered up, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
      this.mineField = mineField; // set the classes mineField to be equal to the mineField provided
      
      // get the dimensions of the mineField
      rowSize = mineField.numRows();
      colSize = mineField.numCols();
      
      visibleField = new int[rowSize][colSize];   // use the dimensions of the mineField to construct our visibleField
      
      // loop through the visibleField and fill it with the initial values
      for (int[] row: visibleField){
          Arrays.fill(row, COVERED);
      }
      
      // set the number of guesses and the number of question variables to zero
      numberGuess = 0;
      numberQuestion = 0;
      
      // initialize the number of guesses left to the total number of mines
      mineGuessLeft = mineField.numMines();
      
      // set our gameOver variable to be false
      gameOver = false;
   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
      // Do the same thing as above, resets everything back to original state
      for (int[] row: visibleField){
          Arrays.fill(row, COVERED);
      }
      
      numberGuess = 0;
      numberQuestion = 0;
      
      mineGuessLeft = mineField.numMines();
      
      gameOver = false;
   }
  
   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {
      return mineField;
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      return visibleField[row][col];
   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
      or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value can
      be negative, if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      return mineGuessLeft;

   }
 
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {
      // if call on COVERED square
      if(visibleField[row][col] == COVERED){
         visibleField[row][col] = MINE_GUESS;   // update the visible field
         numberGuess++;   // update the number of guesses
         
         mineGuessLeft = mineGuessLeft-1;   // update the number of guesses we have left
      }
      
      // if call on MINE_GUESS square
      else if(visibleField[row][col] == MINE_GUESS){
         visibleField[row][col] = QUESTION;
         numberGuess = numberGuess - 1;
         numberQuestion++;
         
         mineGuessLeft++;
         
      }
      
      // if call on QUESTION square
      else if(visibleField[row][col] == QUESTION){
         visibleField[row][col] = COVERED;
         numberQuestion = numberQuestion - 1;
      }
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */
   public boolean uncover(int row, int col) {
      // check if there is a mine and return false iff true
      if(mineField.hasMine(row, col)){
         lostUncover(row, col);   // call our uncover function to display the losing game board
         gameOver = true;   // set our gameOver variable to true
         return false;
      }
      else{
         // find the number of adjacent mines to this square and set that to be our visibleField value
         int numAdjacent = mineField.numAdjacentMines(row, col);
         
         // if there are no adjacent mines we call the flood fill algorithm
         if(numAdjacent == 0){
            floodFill(row, col);
         }
         
         // else we can just fill it in normally
         else{
            visibleField[row][col] = numAdjacent;
         }
         
         // check to see if the game is over
      
         // loop through the board
         for(int i = 0; i < rowSize; i++){
            for(int j = 0; j < colSize; j++){
               // if not the location of a mine
               if(!(mineField.hasMine(i,j))){
                  // then we check to see if it the tile is NOT uncovered
                  if(!(isUncovered(i, j))){ 
                     gameOver = false;   // if not uncovered then we still have tiles to uncover
                     return true;
                  }
               }
            }
         }

         // if we make it through the whole game board and none of the tiles are covered, 
         // the game is over and presumebly the user won
         gameOver = true;
         return true;
      }
   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game over
    */
   public boolean isGameOver() {
      return gameOver;
   }
 
   
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      // returns true if in range and false if not
      if (visibleField[row][col] == QUESTION || visibleField[row][col] == MINE_GUESS || visibleField[row][col] == COVERED){
         return false;
      }
      else{
         return true;   
      }
   }
   
 
   // <put private methods here>
   
   /* 
      Function that we call when the game is lost and we need to uncover the rest of the board.
      Pass the location of the mine that exploded so the function knows what square to make red.
   */
   private void lostUncover(int mineRow, int mineCol){
      // loop through the gameBoard
      for(int row = 0; row < rowSize; row++){
         
         for(int col = 0; col < colSize; col++){
            
            // check if the value is a mine
            if(mineField.hasMine(row, col)){
               
               // check if on losing square
               if(row == mineRow && col == mineCol){
                  visibleField[row][col] = EXPLODED_MINE;   
               }
               
               // check if there was a correct guess
               else if(visibleField[row][col] == MINE_GUESS){ } // do nothing (stays yellow)
               
               // else we are not on the losing mine AND there is no correct guess
               else{
                  visibleField[row][col] = MINE; // (marks as a black tile)
               }
            }
            
            // if not on a mine value, then we know we are on a "safe" square
            else{
               // check if there was a guess to mark as incorrect
               if(visibleField[row][col] == MINE_GUESS){
                  visibleField[row][col] = INCORRECT_GUESS;   // (puts X through yellow tile)
               }
            }
         }
      }
   }
   
   private void floodFill(int row, int col){
      /*
         our base case:
         
         1. Check if the row and col values are in the range of the gameboard.
         2. Uncover the square if it is not a mine and if it is not already uncovered
         3. If the numAdjacent >0 stop, else call flood fill again
      */
      
      // 1. row and col are in range
      // get the numAdjacent for later use
      int numAdjacent = mineField.numAdjacentMines(row, col);
     
      
      if(mineField.inRange(row, col) && !(mineField.hasMine(row,col)) && !(isUncovered(row, col))){
            visibleField[row][col] = numAdjacent;
      }
      else{
         return;   
      }
      
      // 3 if numadjacent is 0, we call flood fill again, else we exit the program 
      if(numAdjacent == 0){
         // recursively call flood fill in the 8 different directions after checking if the location is in range
         if(mineField.inRange(row, col+1)){
            floodFill(row, col+1);   // right 
         }
         if(mineField.inRange(row, col-1)){
            floodFill(row, col-1);   // left 
         }
         if(mineField.inRange(row+1, col)){
            floodFill(row+1, col);   // bottom 
         }
         if(mineField.inRange(row-1, col)){
            floodFill(row-1, col);   // top 
         }
         if(mineField.inRange(row-1, col+1)){
            floodFill(row-1, col+1);   // top right 
         }
         if(mineField.inRange(row-1, col-1)){
            floodFill(row-1, col-1);   // top left 
         }
         if(mineField.inRange(row+1, col+1)){
            floodFill(row+1, col+1);   // bottom right 
         }
         if(mineField.inRange(row+1, col-1)){
            floodFill(row+1, col-1);   // bottom left 
         }
      }
   }
}
