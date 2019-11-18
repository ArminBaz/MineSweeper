// Name: Armin Bazarjani
// USC NetID: bazarjan
// USC ID Number: 4430621961
// CS 455 PA3
// Fall 2019


/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */

import java.util.*;

public class MineField {
   
   // <put instance variables here>
   
   private int NUM_ROWS;
   private int NUM_COLS;
   private int NUM_MINES;
   
   private boolean[][] mineField;
   
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will corresponds to the number of 'true' values in mineData.
    * @param mineData  the data for the mines; must have at least one row and one col.
    */
   public MineField(boolean[][] mineData) {
      // get the values for the rows and columns
      NUM_ROWS = mineData.length;
      NUM_COLS = mineData[0].length;
      
      int length = mineData.length; // grab the length
      
      mineField = new boolean[length][mineData[0].length]; // create a new object for our mineField
      
      // loop through the new mineField and use System.arraycopy to fill the mineField object
      for (int i = 0; i < length; i++) {
         System.arraycopy(mineData[i], 0, mineField[i], 0, mineData[i].length);
      }
      
      int numMines = 0;
      
      // loop through to update numMines value
      for(int row = 0; row < mineData.length; row++){
         for(int col = 0; col < mineData[row].length; col++){
            if(mineData[row][col] == true){
               numMines++;   
            }
         }
      }
      
      NUM_MINES = numMines;
   }
   
   
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      NUM_ROWS = numRows;
      NUM_COLS = numCols;
      NUM_MINES = numMines;
      
      // create the mineField object
      mineField = new boolean[numRows][numCols];
      
      // fill the mineField with false
      for (boolean[] row: mineField){
          Arrays.fill(row, false);
      }
   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col)
    */
   
   public void populateMineField(int row, int col) {
      // create a new random instance
      Random rand = new Random();
      
      // STEP 1: loop through our mineField and removes all current mines
      for(int i = 0; i < NUM_ROWS; i++){
         for(int j = 0; j < NUM_COLS; j++){
            mineField[i][j] = false;
         }
      }
      
      // STEP 2: loop through the mineField again and randomly put the mines in, cecking to make sure we're not populating [row][col] 
      
      boolean checker;   // initialize a checker that I will use to make sure our random location is suitable
      int mineRow, mineCol;   // variables for the random mine row and col location
      
      // iterate through the number of mines
      for(int i = 1; i <= NUM_MINES; i++){
         checker = false;
         // while loop to keep generating random numbers until we pass the checker test
         while(checker == false){
            // generate the row and col integers using rand
            mineRow = rand.nextInt(NUM_ROWS);
            mineCol = rand.nextInt(NUM_COLS);
            
            // if statement for checker
            if(mineField[mineRow][mineCol] == false && mineRow != row && mineCol != col){
               // if checker is passed, update the mineField and update checker
               mineField[mineRow][mineCol] = true;
               checker = true;
            }
         }
      }
   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state the minefield is in at the beginning of a game.
    */
   public void resetEmpty() {
      // loop through our mine field array and set every value to false
      for(int row = 0; row < NUM_ROWS; row++){
         for(int col = 0; col < NUM_COLS; col++){
            mineField[row][col] = false;
         }
      }
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      // we will have to check 8 different locations
      
      // we need to have different methods of checking based on where our x value is
      int numAdjacent = 0;
      
      // top left of the mineField
      /*
         x 1
         2 3
      */
      if((row == 0) && (col == 0)){
         // check the bottom row and right column
         int bottomRow = row + 1;
         int rightCol = col + 1;
         for(int i = row; i <= bottomRow; i++){
            if(mineField[i][rightCol] == true){
               numAdjacent++;  
            }
         }
         
         if(mineField[bottomRow][col] == true){
            numAdjacent++;      
         }
      }
      
      // top right of the mineField
      /*
      1 x
      2 3
      */
      else if((row == 0) && (col == NUM_COLS-1)){
         // check the bottom and left column
         int bottomRow = row + 1;
         int leftCol = col - 1;
         
         for(int i = row; i <= bottomRow; i++){
            if(mineField[i][leftCol] == true){
               numAdjacent++;  
            }
         }
         
         if(mineField[bottomRow][col] == true){
            numAdjacent++;      
         }
      }
      
      // bottom left of the mineField
      /*
         1 2
         x 3
      */
      else if((row == NUM_ROWS-1) && (col == 0)){
         // check the top row and right col
         int topRow = row - 1;
         int rightCol = col + 1;
         
         for(int i = topRow; i <= row; i++){
            if(mineField[i][rightCol] == true){
               numAdjacent++;  
            }
         }
         
         if(mineField[topRow][col] == true){
            numAdjacent++;      
         }
      }
      
      // bottom right of the mineField
      /*
         1 2
         3 x
      */
      else if((row == NUM_ROWS-1) && (col == NUM_COLS-1)){
         // check the top row and left col
         int topRow = row - 1;
         int leftCol = col - 1;
         
         for(int i = topRow; i <= row; i++){
            if(mineField[i][leftCol] == true){
               numAdjacent++;  
            }
         }
         
         if(mineField[topRow][col] == true){
            numAdjacent++;      
         }
      }
      
      // somewhere along the top
      /*
         1 x 2
         3 4 5
      */
      else if((row == 0) && ((col != 0) && (col != NUM_COLS-1))){
         // check the bottom row, left and right columns
         int bottomRow = row + 1;
         int leftCol = col - 1;
         int rightCol = col + 1;
         
         for(int i = row; i <= bottomRow; i++){
            if(mineField[i][leftCol] == true){
               numAdjacent++;   
            }
            if(mineField[i][rightCol] == true){
               numAdjacent++;   
            }
         }
         
         if(mineField[bottomRow][col] == true){
            numAdjacent++;   
         }
      }
      
      // somewhere along the bottom
      /*
         1 2 3
         4 x 5
      */
      else if((row == NUM_ROWS-1) && ((col != 0) && (col != NUM_COLS-1))){
         // check the top row, and left and right columns
         int topRow = row - 1;
         int leftCol = col - 1;
         int rightCol = col + 1;
         
         for(int i = topRow; i <= row; i++){
            if(mineField[i][leftCol] == true){
               numAdjacent++;  
            }
            if(mineField[i][rightCol] == true){
               numAdjacent++;   
            }
         }
         
         if(mineField[topRow][col] == true){
            numAdjacent++;   
         }
      }
      
      // somewhere along the left
      /*
         1 2
         x 3
         4 5
      */
      else if((col == 0) && ((row != 0) && (row != NUM_ROWS-1))){
         // check the right column and top and botom rows
         int topRow = row - 1;
         int bottomRow = row + 1;
         int rightCol = col + 1;
         
         for(int i = col; i <= rightCol; i++){
            if(mineField[topRow][i] == true){
               numAdjacent++;   
            }
            if(mineField[bottomRow][i] == true){
               numAdjacent++;   
            }
         }
         
         if(mineField[row][rightCol] == true){
            numAdjacent++;   
         }
      }
      
      // somewhere along the right
      /*
         1 2
         3 x
         4 5
      */
      else if((col == NUM_COLS-1) && ((row != 0) && (row != NUM_ROWS-1))){
         // check the left column and top and bottom rows
         int topRow = row - 1;
         int bottomRow = row + 1;
         int leftCol = col - 1;
         
         for(int i = leftCol; i <= col; i++){
            if(mineField[topRow][i] == true){
               numAdjacent++;   
            }
            if(mineField[bottomRow][i] == true){
               numAdjacent++;   
            }
         }
         
         if(mineField[row][leftCol] == true){
            numAdjacent++;   
         }
      }
      
      // somewhere in the middle
      /*
         1 2 3
         4 x 5
         6 7 8
      */
      else{
         // need to check top/bottom rows and left/right columns
         int bottomRow = row + 1;
         int topRow = row - 1;
         int leftCol = col - 1;
         int rightCol = col + 1;
         
         for(int i = topRow; i <= bottomRow; i++){
            if(mineField[i][leftCol] == true){
               numAdjacent++;   
            }
            if(mineField[i][rightCol] == true){
               numAdjacent++;   
            }
         }
         
         if(mineField[bottomRow][col] == true){
            numAdjacent++;   
         }
         if(mineField[topRow][col] == true){
            numAdjacent++;   
         }
      }
     
      
      return numAdjacent;
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      // instantiate two boolean variables for when checking row and col
      boolean goodRow = false;
      boolean goodCol = false;
      
      // if in row range
      if((row >= 0) && (row <= NUM_ROWS-1)){
         goodRow = true;
      }
      
      // if in col range
      if((col >= 0) && (col <= NUM_COLS-1)){
         goodCol = true;
      }
      
      return (goodRow && goodCol);
   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return NUM_ROWS;
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return NUM_COLS;
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      return mineField[row][col];
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
      return NUM_MINES;
   }
}

