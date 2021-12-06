package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Board {
	
	private ArrayList<Tile> tileList = new ArrayList<>();
	private final TileType BLANK = TileType.BLANK;
	private final TileType X = TileType.X;
	private final TileType CIRCLE = TileType.CIRCLE;
	public boolean isGameOver = false;
	private TileType playerTileType;
	private TileType AITileType;
	private int AIMovesPlayed = 0;
	private boolean gameStalemate = false;

	/*
	 * Board converted to binary. For the tile type, 1 is filled, 0 is blank or opposite tile.
	 */

	ArrayList<Integer> binaryBoard = new ArrayList<>();
	ArrayList<Integer> binaryBoardCircle = new ArrayList<>();
	ArrayList<Integer> binaryBoardX = new ArrayList<>();
	ArrayList<Integer> allOnes = new ArrayList<>(Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1));
	
	private boolean isGameOverSim;


	// updates the board
	public void update(final int tileID, final TileType tiletype, final boolean sim) {
	if(sim == false) {
		System.out.println();
		final Tile changedTile = new Tile(tileID, tiletype);
		tileList.set(tileID, changedTile);
		// updates binary board
		if(tiletype == CIRCLE) {
			binaryBoard.set(tileID, 1);
			binaryBoardCircle.set(tileID, 1);

		} else if(tiletype == X) {
			binaryBoard.set(tileID, 1);
			binaryBoardX.set(tileID, 1);

		}
		if(binaryBoard.equals(allOnes)) {
			gameStalemate = true;
		}
		// checks if the game is over
		checkIfWon(false, tiletype);
		
		if(!isGameOver) {
			printBoard();
			}
		}
	
	// simulation of game
	else if(sim == true) {
		// updates binary board
		if(tiletype == CIRCLE) {
			binaryBoardCircle.set(tileID, 1);

		} else if(tiletype == X) {
			binaryBoardX.set(tileID, 1);
		}
		// checks if the game is over simulation
		checkIfWon(true, tiletype);
		// reverts to old board
		if(tiletype == CIRCLE) {
			binaryBoardCircle.set(tileID, 0);

		} else if(tiletype == X) {
			binaryBoardX.set(tileID, 0);
		}	
	}
}
	

	public void createBoard() {
		// Creates 9 blank tiles and creates binary board
		for(int i = 0; i < 9; i++) {
			Tile tile = new Tile(i, BLANK);
			tileList.add(tile);
			
			
			binaryBoardX.add(0);
			binaryBoardCircle.add(0);
			binaryBoard.add(0);
		}
		printBoard();
	}


	// prints board
	private void printBoard() {
		for(int i = 0; i < 9; i++) {
			if(i == 3 || i == 6 || i == 9) System.out.println();
			System.out.print(tileList.get(i));
		}
		System.out.println();
	}

	public ArrayList<Tile> getTileList() {
		return tileList;
	}

	public void setTileList(ArrayList<Tile> tileList) {
		this.tileList = tileList;
	}

	// returns tile type of the tileID
	public TileType getTileType(int tileID) {
		if(tileID <= -1 || tileID >= 10 || tileID == 9) {
			return null;
		} else {
			return tileList.get(tileID).getTileType();
		}

	}
	
	private void chooseTileType() {
		System.out.println("Would you like to be X or CIRCLE?");
		Scanner myScan = new Scanner(System.in);
		String playerType = myScan.next();
		if(playerType.equals("CIRCLE")) {
			playerTileType = CIRCLE;
			AITileType = X;
		} else if(playerType.equals("X")) {
			playerTileType = X;
			AITileType = CIRCLE;
		} else {
			System.out.println("Invalid, input must be X or CIRCLE");
			chooseTileType();
		}
		
		playerTurn();
		
	}

	private void playerTurn() {
		System.out.println("\n\n Enter a number, 0 - 8, that you would like to place your circle on! (0 top left, 1 top middle, ect.)");
		Scanner scan = new Scanner(System.in);
		String tileChange = scan.next();
		if(CheckIfValidMove(tileChange)) {
			int changedTile = Integer.valueOf(tileChange);
			update(changedTile, playerTileType, false);
		} else {
			System.out.println("Invalid move!");
			playerTurn();
		}
		while(!isGameOver) {
			AIturn();
		}
	}

	private void AIturn() {
		
		System.out.println("\n\nAI thinking...\n");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Integer> validMoves = getValidMoves();
		Collections.shuffle(validMoves);
		int changeTile = validMoves.get(1);
		System.out.println(changeTile);
		int counter = 0;
		int recursiveCalls = 0;
		
		AIMovesPlayed++;
		
		Iterator<Integer> iterator = getValidMoves().iterator();
		
	ForEachValidMove:	
		for(Integer tile : getValidMoves()) {
			if(counter++ != getValidMoves().size() - 1) {
				//System.out.println(tile + " is a valid move!");
				// If yes run the simulation to see if it wins
					update(tile, AITileType, true);
						if(isGameOverSim == true) {
							System.out.println("Simulation worked and the sim game ended");
							// If it wins then play it in game
							isGameOverSim = false;
							update(tile, AITileType, false);
							break ForEachValidMove;
					} else {
						//System.out.println(tile + " is not a winning move");
					} 
						update(tile, playerTileType, true);
							if(isGameOverSim == true) {
							//	System.out.println("X can win, block the tile at " + tile);
								// If it wins then play it in game
								isGameOverSim = false;
								update(tile, AITileType, false);
							break ForEachValidMove;
					} else {
						//System.out.println("Opponent has no winning move that needs to be blocked.");
					}
		} else {
		// Temporary solution because above can not go to full array size
			update(8, AITileType, true);
			if(isGameOverSim == true) {
				//System.out.println("Simulation worked and the sim game ended");
				// If it wins then play it in game
				isGameOverSim = false;
				update(8, AITileType, false);
				break ForEachValidMove;
		} else {
			//System.out.println(8 + " is not a winning move");
		} 
			update(8, playerTileType, true);
			if(isGameOverSim == true) {
				//System.out.println("X can win, block the tile at " + 8);
				// If it wins then play it in game
				isGameOverSim = false;
				update(8, AITileType, false);
				break ForEachValidMove;
		} else {
			//System.out.println("Opponent has no winning move that needs to be blocked.");
		}	// Finishes the 8th call
			
			recursiveCalls++;
			// Optimize this a bit?
			// I think best move in general is to do the middle so if it's open take it on the first move
			if(AIMovesPlayed == 1) {
				if(CheckIfValidMove(String.valueOf(4))) {
					update(4, AITileType, false);
					break ForEachValidMove;
				}
			}
			String tileChange = String.valueOf(changeTile);
			if(CheckIfValidMove(tileChange)) {
				update(changeTile, AITileType, false);
				break ForEachValidMove;
			} else {
				AIturn();
			}
		}
	}
		while(!isGameOver) {
			playerTurn();
		}
	}

	public void checkIfWon(boolean simulation, TileType tileType) {
		if(gameStalemate == false) {
		ArrayList<Integer> binaryBoard =  returnBinaryBoard(tileType);
			if(simulation == false) {
				if(combineBinaryBoard(binaryBoard)) {
					if(tileType == playerTileType) {
					System.out.println("\n\n Game over, you won!");
					} 
					else {
						System.out.println("You lost");
					}
				isGameOver = true;
				}
			} else if(simulation == true) { 
				if(combineBinaryBoard(binaryBoard)) {
				isGameOverSim = true;
				}
			}
		} else {
			System.out.println("Stalemate!");
			isGameOver = true;
		}
	}

	private ArrayList<Integer> returnBinaryBoard(TileType type) {
		if(type == CIRCLE) return binaryBoardCircle;
		else if(type == X) return binaryBoardX;
		else return null;
		
	}

	
	private ArrayList<Integer> getValidMoves() {
		ArrayList<Integer> validMoves = new ArrayList<Integer>();
		for(Tile tile : tileList) {
			if(CheckIfValidMove(String.valueOf(tile.getTileID()))) {
				validMoves.add(tile.getTileID());
			}
		}
		return validMoves;
	}

	private boolean CheckIfValidMove(String tile) {
		try {
			int tileID = Integer.valueOf(tile);
			if(getTileType(tileID) == X || getTileType(tileID) == CIRCLE || tileID <= -1 || tileID >= 10) {
				return false;
			} else {
				return true;
			}
		} catch(NumberFormatException e) {
			return false;
		}
	}

	private boolean combineBinaryBoard(ArrayList<Integer> binaryBoard) {
		// WINNING COMBINATIONS
		 final ArrayList<Integer> won = new ArrayList<>(Arrays.asList(1, 1, 1));
		 final ArrayList<Integer> topRowWin = new ArrayList<>(Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0));
		 final ArrayList<Integer> middleRowWin = new ArrayList<>(Arrays.asList(0, 0, 0, 1, 1, 1, 0, 0, 0));
		 final ArrayList<Integer> lowestRowWin = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 1, 1, 1));
		 final ArrayList<Integer> LeftColumnWin = new ArrayList<>(Arrays.asList(1, 0, 0, 1, 0, 0, 1, 0, 0));
		 final ArrayList<Integer> MiddleColumnWin = new ArrayList<>(Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0));
		 final ArrayList<Integer> RightColumnWin = new ArrayList<>(Arrays.asList(0, 0, 1, 0, 0, 1, 0, 0, 1));
		 final ArrayList<Integer> DiagRightWin = new ArrayList<>(Arrays.asList(1, 0, 0, 0, 1, 0, 0, 0, 1));
		 final ArrayList<Integer> DiagLeftWin = new ArrayList<>(Arrays.asList(0, 0, 1, 0, 1, 0, 1, 0, 0));

		    ArrayList<Integer> TRowWin = new ArrayList<>();
			ArrayList<Integer> MRowWin = new ArrayList<>();
			ArrayList<Integer> LRowWin = new ArrayList<>();
			ArrayList<Integer> LColWin = new ArrayList<>();
			ArrayList<Integer> MColWin = new ArrayList<>();
			ArrayList<Integer> RColWin = new ArrayList<>();
			ArrayList<Integer> RDiagWin = new ArrayList<>();
			ArrayList<Integer> LDiagWin = new ArrayList<>();

		for(int i = 0; i < binaryBoard.size(); i++) {
			if(topRowWin.get(i) == 1 &&
					binaryBoard.get(i) == 1) {
				TRowWin.add(1);
			} if(middleRowWin.get(i) == 1 &&
					binaryBoard.get(i) == 1) {
				MRowWin.add(1);

			} if(lowestRowWin.get(i) == 1 &&
					binaryBoard.get(i) == 1) {
				LRowWin.add(1);
			} if(LeftColumnWin.get(i) == 1 &&
					binaryBoard.get(i) == 1) {
				LColWin.add(1);

			} if(MiddleColumnWin.get(i) == 1 &&
					binaryBoard.get(i) == 1) {
				MColWin.add(1);

			} if(RightColumnWin.get(i) == 1 &&
					binaryBoard.get(i) == 1) {
				RColWin.add(1);

			} if(DiagRightWin.get(i) == 1 &&
					binaryBoard.get(i) == 1) {
				RDiagWin.add(1);

			} if(DiagLeftWin.get(i) == 1 &&
					binaryBoard.get(i) == 1) {
				LDiagWin.add(1);

			}
		}

			if(TRowWin.equals(won) ||
			   MRowWin.equals(won) ||
			   LRowWin.equals(won) ||
			   LColWin.equals(won) ||
			   MColWin.equals(won) ||
			   RColWin.equals(won) ||
			   RDiagWin.equals(won)||
			   LDiagWin.equals(won)) {
				return true;
			} else {
				return false;
			}

	}


	public void startGame() {
		createBoard();
		chooseTileType();
	}
}

























