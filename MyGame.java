import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;

public class MyGame {
	
	int[][] grid;
	final String SPACE = "\t";
	char empty_x = 'A';
	int empty_y = 0;
	int numRow, numCol;

	// https://stackoverflow.com/questions/6525059/can-i-have-macros-in-java-source-files
	// How to use macros + cpp to benefit java programming
	void display() {
		System.out.print("\n"+(numRow*numCol-1)+"-Puzzle Game\n\n");
		System.out.print("  | "); 
	    for(int i = 0; i < numCol; i++)
	    	System.out.print(SPACE+i);
	   	
	    System.out.print("\n--|-----");
	    for(int i = numCol-1; i >=0; --i) System.out.print("--------");
	    System.out.println();
		for(int i = 0; i < numRow; i++) {
			System.out.print((char)('A' + i) +" | ");
			for(int j = 0; j < numCol; j++) {
				System.out.print(SPACE+grid[i][j]);
			}
			System.out.print("\n  |\n");
		}
	}
	// Java doesn't support default parameters "-"
	public MyGame(int numRow, int numCol) {
	    setSize(numRow,numCol);
	    generate();
	}
	
	public void setSize(int numRow, int numCol) {
		this.numCol = numCol;
	    this.numRow = numRow;
	}
	
	/*
	  Task 1: Generate a puzzle
	  - Shuffle the 8 numbers and the empty cell.
	  - Make sure the generated puzzle has a solution by checking whether there exist a reverse pair.
	 */
	// VECTOR: Pass by value, so need AMPERSAND &
	void fix_reverse_pair(int[] arr){// exclude 0 from the array
		int num = 0;
		int size = arr.length;
		int zero_pos = 0;
		// count the number of reverse pairs
		for(int i = 0; i < size-1; ++i){
			if(arr[i] == 0) {
				zero_pos = i;
				continue;
			}
			for(int j = i+1; j < size; ++j){
				if(arr[j] == 0) zero_pos = j;
				if((arr[i] > arr[j]) && (arr[j] != 0)) num += 1;
			}
		}
		// modify if that number is odd
		if(num%2 == 1){
			int temp;
			if(zero_pos < size/2){
				temp = arr[size-1];
				arr[size-1] = arr[size-2];
				arr[size-2] = temp;
			}
			else{
				temp = arr[0];
				arr[0] = arr[1];
				arr[1] = temp;
			}
		}
	}
	// Implementing Fisher–Yates shuffle
	static void shuffleArray(int[] ar){
	  // If running on Java 6 or older, use `new Random()` on RHS here
	  Random rnd = ThreadLocalRandom.current();
	  for (int i = ar.length - 1; i > 0; i--){
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	  }
	}
	void generate() {
		/*
		Vector<Integer> myvector = new Vector<>(); // Use integer to pass by reference
		for (int i=0; i<9 ; ++i) 
			myvector.add(i);
		*/
		grid = new int[numRow][numCol];
		int[] myarr = new int[numRow*numCol];
		for(int i = numRow*numCol-1; i > -1; --i)
			myarr[i] = i;
		shuffleArray(myarr);
		fix_reverse_pair(myarr);
		
		for(int i = 0; i<numRow ; ++i){
			for(int j = 0; j<numCol ; ++j){
				grid[i][j] = myarr[numCol*i + j];
				if(grid[i][j] == 0){
					empty_x = (char)('A'+i);
					empty_y = j;
				}
			}
		}
	}
	/*
	  Task 2: Check whether the cell is valid to click
	  - A cell is valid to click if and only if the empty cell has the same row or column as it.
	  - You should also check whether the cell exists.

	  - Inputs: row and col refer to the row number and column number of the click
	  - Output: 
	     - Return 1 if the click is valid.
	     - Return 0 if this cell exists but cannot be clicked.
	     - Return -1 for any other error.
	 */
	int check(char row, int col) {
		// Complete this function by putting your code below
		boolean inside = (row >= 'A') && (row <= 'A'+numRow-1) && (col >= 0) && (col <= numCol-1);
		if(inside) // choosable or not
			return (row == empty_x) ^ (col == empty_y)? 1:0; // check ^ later
		else // outside
			return -1;
	}

	/*
	  Task 3: Move the cells according to the following rules
	  - If the clicked cell is next to the empty cell, you just swap the two cells.
	  - If there is another cell between these two cells (the clicked cell and the empty cell),
	    you have to first move the middle cell and then the clicked cell.

	  - Inputs: row and col refer to the row number and column number of the click
	 */
	static int abs(int i) {
		return i>=0? i:-i;
	}
	int sign(int a) {
		return a>0? 1:
				a<0?-1:0;
	}
	
	void operate(char row, int col) {
		if(check(row,col) != 1) return;
		// Complete this function by putting your code below
		if(row == empty_x){
			int r = row - 'A';
			int step = sign(empty_y-col);
			for(int c = empty_y; c != col; c -= step) {
				grid[r][c] = grid[r][c-step];
			}
			grid[r][col] = 0;
		}
		else{ // col == empty_y
			int step = sign(empty_x-row);
			for(int r = empty_x-'A'; r != row-'A'; r -= step) {
				System.out.println(r+","+col+" <- "+(r-step)+","+col);
				grid[r][col] = grid[r-step][col];
			}
			grid[row-'A'][col] = 0;
		}
		empty_x = row; empty_y = col;
	}

	/*
	  Task 4: Check whether the puzzle is solved.
	 */
	public boolean win() {
		// Complete this function by putting your code below
		boolean do_win = true;
		int numCell = numRow*numCol;
		for(int i = 0; i<numRow ; ++i){
			for(int j = 0; j<numCol ; ++j){
				if(!(grid[i][j] == (numCol*i + j + 1)%numCell)){
					do_win = false;
					break;
				}
			}
		}
		return do_win;
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		System.out.print("Welcome back to Java!\nEnter the size Row x Col of table: ");
		MyGame abc = new MyGame(sc.nextInt(),sc.nextInt());
		abc.generate();
		
		
		while(!abc.win()) {
			abc.display();
			System.out.println("Which cell do you want to click (Eg. A 0)?");
			
			char row = sc.next().charAt(0);
			int col = sc.nextInt();
			int ok = abc.check(row, col);

			switch (ok) {
				case 0: System.out.print("This cell can't be clicked!\n");
					 break;
				case 1: abc.operate(row, col);
					 break;
				case -1: System.out.print("Invalid input!\n");
					 break;
				default: break;
			}
			Thread.sleep(100);
			System.out.println("======================");
			try { // doesn't work in Eclipse console, but do in CMD
				new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
			}
			catch(Exception E) {
				System.out.println(E);
			}
			
		}
		
		sc.close();
		abc.display();
		System.out.print("Nice!");
	}
}
