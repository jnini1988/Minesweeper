import java.util.ArrayList;
import java.util.Scanner;

public class mineSweeper{
//    public static int gridSize;
//    public static int numOfmine;
	public static int numOfflag; 

    public static void main(String[] args) {
    	welcome();
        setupGame();
    }
    
    public static void welcome() {
    	System.out.println("Welcome to mineSweeper v0.001!");
        System.out.println("Designed by Shuyue Chen and Daniel Dultsin.");
        System.out.println("Enjoy!");
        System.out.println();
        System.out.println("x represent the row number and y repressent the column number");
        System.out.println("left click means reveals a square and right click places a flag");
        System.out.println("Oh, and small note: coordinates are shifted one backward each.");
        System.out.println("So the top left coordinate has an x = 0 and y = 0,");
        System.out.println();
    }

    public static void setupGame() {
        Scanner scan=new Scanner(System.in);
        boolean play=true;
        int numOfflag=0; 
        
        /***Determine setting for one play***/
        //let user chose grid size and number of mines
        String gridSizeo = "";
        int gridSize = -1;
        while (gridSize < 10 || gridSize > 30) {
            System.out.println("Enter an integer between 10-30 to be the size of the grid: ");
            while (!scan.hasNextInt()) {
                System.out.println("Invalid input. Enter an integer that is in bounds.");
                System.out.println("Enter an integer between 10-30 to be the size of the grid: ");
                gridSizeo = scan.next();
            }
            gridSizeo = scan.next();
            gridSize = Integer.parseInt(gridSizeo);
        }

        //TODO change back mine
        String numOfmineo = "";
        int numOfmine = -1;
        while (numOfmine < 10 || numOfmine > 30) {
            System.out.println("Enter an integer between 10-30 to be the number of mines: ");
            while (!scan.hasNextInt()) {
                System.out.println("Invalid input. Enter an integer that is in bounds.");
                System.out.println("Enter an integer between 10-30 to be the number of mines: ");
                numOfmineo = scan.next();
            }
            numOfmineo = scan.next();
            numOfmine = Integer.parseInt(numOfmineo);
        }

        /*** set up grid ***/
        squares[][] grid = new squares[gridSize][gridSize];
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                grid[r][c] = new squares();
            }
        }
        setUpGrid(gridSize, numOfmine, grid);
        printGrid(grid);
        
        /***Playing phase***/
        while(play) {
        	//Keep track of game progress (updated 8/24/21)
        	System.out.println(numOfflag+" flag(s) placed, "+numOfmine+" mines in total");
        	
            //USER INPUT LEFT/RIGHT CLICK & GRID COORDINATES
            String mouse="";
            while(!(mouse.toUpperCase().equals("L") || mouse.toUpperCase().equals("R"))) {
                System.out.println("Enter L to left click, enter R to right click: ");
                mouse=scan.next();
            }

            String xo = "";
            int x = -1;
            while(x < 0 || x > gridSize-1) {
                System.out.println("Enter the x-value of the coordinate that you want to select: ");
                while (!scan.hasNextInt()) {
                    System.out.println("Invalid input. Enter an integer that is in bounds.");
                    System.out.println("Enter the x-value of the coordinate that you want to select: ");
                    xo = scan.next();
                }
                xo = scan.next();
                x = Integer.parseInt(xo);
            }
            String yo = "";
            int y = -1;
            while(y < 0 || y > gridSize-1) {
                System.out.println("Enter the y-value of the coordinate that you want to select: ");
                while (!scan.hasNextInt()) {
                    System.out.println("Invalid input. Enter an integer that is in bounds.");
                    System.out.println("Enter the y-value of the coordinate that you want to select: ");
                    yo = scan.next();
                }
                yo = scan.next();
                y = Integer.parseInt(yo);
            }
            
            if(!grid[x][y].getFlag()) {
                if (mouse.toUpperCase().equals("L")&&grid[x][y].getHidden()) {
                    grid[x][y].changeHidden();
                    //left click and hidden and mine -> game end & all revealed
                    if(grid[x][y].getSquareType().equals("*")) {
                        play=false;
                        grid = revealMines(grid);
                        printGrid(grid);
                        System.out.print("Sorry, you lose! Do you want to replay(Y/N)?'");
                        String playAgain=scan.next();
                        if(playAgain.toUpperCase().equals("Y")) {
                            setupGame(); 
                        }
                    }
                    //left click and hidden and empty -> clear up space
                    if(grid[x][y].getSquareType().equals(" ")) {
                        clearSpace(grid,x,y);
                        printGrid(grid);
                    }
                    //left click and hidden and number -> reveal nothing needs to be done for that
                    else{ printGrid(grid);}
                }
            }
            //right click and hidden -> setFlag
            if (mouse.toUpperCase().equals("R")&&grid[x][y].getHidden()) {
                //FYI: squares with flags will have hidden = true, but the flag will be printed
                grid[x][y].changeFlag();
                if(grid[x][y].getFlag()) {
                	numOfflag++;
                }
                else {
                	numOfflag--;
                }
                printGrid(grid);
            }
            
            //user win and ask them whether they want another game
            //The user wins if ONLY the squares with mines are the ones that have flags on them getFlag = true;
            if(checkWin(grid)&&(numOfflag==numOfmine)) {
                play=false; //escape the setting of this play
                System.out.print("Congratulations! You win! Do you want to replay (Y/N)? ");
                String playAgain=scan.next();
                if(playAgain.toUpperCase().equals("Y")) {
                    setupGame();
                    // start a new game (new settings can be set)
                }

            }
        }
        scan.close();
    }

    public static void setUpGrid(int size, int mines, squares[][] grid) {

        //RANDOMLY ASSIGNING MINES
        int assigned = 0;
        while (assigned < mines) {
            int row = (int)(Math.random() * size);
            int col = (int)(Math.random() * size);
            //checks to make sure there is'nt already a mine assigned there
            if (!grid[row][col].getSquareType().equals("*")) {
                grid[row][col].setSquareType("*");
                assigned++;
            }
        }
        
       //ASSIGN NUMBERS AND BLANKS
      for (int r = 0; r < size; r++) {
          for (int c = 0; c < size; c++) {
              int minesNear = 0;

              for(int row=r-1;row<=r+1;row++) {
            	  for(int col=c-1;col<=c+1;col++) {
            		  if(row>=0&&row<size&&col>=0&&col<size) {
            			  if (grid[row][col].getSquareType().equals("*")) {
                            minesNear++;
                        }
            		  }
            	  }
              }

              if (!grid[r][c].getSquareType().equals("*")) {
                  if (minesNear==0) {
                      grid[r][c].setSquareType(" ");
                  } else {
                      grid[r][c].setSquareType("" + minesNear);
                  }
              }
          }
      }
        
    }

    public static boolean checkWin(squares[][] grid) {
    	boolean result = true;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid.length; col++) {
            	//if it is not a mine but flag placed, result is false
                if (!grid[row][col].getSquareType().equals("*") && grid[row][col].getFlag()) {
                    return false; 
                }
            }
        }
        return result;
    }

    public static void printGrid(squares[][] grid){
        int size = grid.length;
        for (int row = 0; row < size; row++) {

            for (int formatting = 0; formatting <= size * 4 ; formatting++) {
                System.out.print("─");
            }
            System.out.println();
            System.out.print("│ ");
            for (int col = 0; col < size; col++) {
                //if square is hidden print either F or ?
                if (grid[row][col].getHidden()) {
                    if (grid[row][col].getFlag()) {
                        System.out.print("F");
                    } else {
                        System.out.print("?");
                    }
                    //if square is not hidden, print square type
                } else {
                    System.out.print(grid[row][col].getSquareType());
                }

                System.out.print(" │ ");
            }
            System.out.println();
        }
        for (int formatting = 0; formatting <= size * 4 ; formatting++) {
            System.out.print("─");
        }
        System.out.println();
    }

    public static squares[][] revealMines(squares[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col].getSquareType().equals("*")) {
                    grid[row][col].changeHidden();
                }
            }
        }
        return grid;
    }
    
    public static void clearSpace(squares[][] grid, int x, int y) {
        ArrayList<Integer> xcor = new ArrayList<Integer>();
        xcor.add(x);
        ArrayList<Integer> ycor = new ArrayList<Integer>();
        ycor.add(y);

        //ADDS ALL BLANKS SPACES TO THE X AND Y ARRAY
        //AND UNHIDES THE BLANK SPACE
        for (int cor=0;cor<xcor.size();cor++) {
            int r = xcor.get(cor);
            int c = ycor.get(cor);

            //check above
            if (r - 1 >= 0) {
                if (grid[r-1][c].getSquareType().equals(" ")) {
                    if (!alreadyThere(xcor, ycor, r-1, c)) {
                        xcor.add(r - 1);
                        ycor.add(c);
                        grid[r - 1][c].changeHidden();
                    }
                }
            }

            //check below
            if (r + 1 < grid.length) {
                if (grid[r+1][c].getSquareType().equals(" ")) {
                    if (!alreadyThere(xcor, ycor, r+1, c)) {
                        xcor.add(r + 1);
                        ycor.add(c);
                        grid[r + 1][c].changeHidden();
                    }
                }
            }

            //check left
            if (c - 1 >= 0) {
                if (grid[r][c-1].getSquareType().equals(" ")) {
                    if (!alreadyThere(xcor, ycor, r, c-1)) {
                        xcor.add(r);
                        ycor.add(c - 1);
                        grid[r][c - 1].changeHidden();
                    }
                }
            }
            //check rigt
            if (c+1 < grid.length) {
                if (grid[r][c+1].getSquareType().equals(" ")) {
                    if (!alreadyThere(xcor, ycor, r, c+1)) {
                        xcor.add(r);
                        ycor.add(c + 1);
                        grid[r][c + 1].changeHidden();
                    }
                }
            }

            //check top left
            if (r-1 >= 0 && c-1 >= 0) {
                if (grid[r-1][c-1].getSquareType().equals(" ")) {
                    if (!alreadyThere(xcor, ycor, r-1, c-1)) {
                        xcor.add(r - 1);
                        ycor.add(c - 1);
                        grid[r - 1][c - 1].changeHidden();
                    }
                }
            }

            //check top right
            if (r-1 >= 0 && c+1 < grid.length) {
                if (grid[r-1][c+1].getSquareType().equals(" ")) {
                    if (!alreadyThere(xcor, ycor, r-1, c+1)) {
                        xcor.add(r - 1);
                        ycor.add(c + 1);
                        grid[r - 1][c + 1].changeHidden();
                    }
                }
            }

            //check bottom left
            if (r+1 < grid.length && c-1 >= 0) {
                if (grid[r+1][c-1].getSquareType().equals(" ")) {
                    if (!alreadyThere(xcor, ycor, r+1, c-1)) {
                        xcor.add(r + 1);
                        ycor.add(c - 1);
                        grid[r + 1][c - 1].changeHidden();
                    }
                }
            }

            //cehck bottom right
            if (r+1 < grid.length && c+1 < grid.length) {
                if (grid[r+1][c+1].getSquareType().equals(" ")) {
                    if (!alreadyThere(xcor, ycor, r+1, c+1)) {
                        xcor.add(r + 1);
                        ycor.add(c + 1);
                        grid[r + 1][c + 1].changeHidden();
                    }
                }
            }
        }

        //UNHIDES ALL SQUARES ADJACENT TO BLANKS SPACES DISCOVERED
        for (int cor=0;cor<xcor.size();cor++) {
            int r = xcor.get(cor);
            int c = ycor.get(cor);
            if(r-1>=0) {
            	grid[r - 1][c].changeHidden();
            	if(c-1>=0) {
            		 grid[r - 1][c - 1].changeHidden();
            	}
            	if(c+1<grid.length) {
            		grid[r - 1][c + 1].changeHidden();
            	}
            }
            if(r+1<grid.length) {
            	grid[r + 1][c].changeHidden();
            	if(c-1>=0) {
            		grid[r + 1][c - 1].changeHidden();
	           	}
	           	if(c+1<grid.length) {
	           		grid[r + 1][c + 1].changeHidden();
	           	}
            }
            if(c-1>=0) {
            	grid[r][c-1].changeHidden();
            }
            if(c+1<grid.length) {
            	grid[r][c+1].changeHidden();
            }
        }
            
        }
    
    public static boolean alreadyThere(ArrayList<Integer> xcor, ArrayList<Integer> ycor, int x, int y) {
        for (int i = 0; i < xcor.size(); i++) {
            if (xcor.get(i) == x && ycor.get(i) == y) {
                return true;
            }
        }
        return false;
    }
    
}