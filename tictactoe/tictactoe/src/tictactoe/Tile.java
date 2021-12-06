package tictactoe;

public class Tile {
	
	private TileType tileType;
	private int tileID;
	
	
	/* long a1 = 0b100_0_000_0_000_0_100_0_000_0_000_0_100_0_000_0L;
	 long a2 = 0b010_0_000_0_000_0_000_0_100_0_000_0_000_0_000_0L;
	 long a3 = 0b001_0_000_0_000_0_000_0_000_0_100_0_000_0_100_0L;
	 long b1 = 0b000_0_100_0_000_0_010_0_000_0_000_0_000_0_000_0L; 
	 long b2 = 0b000_0_010_0_000_0_000_0_010_0_000_0_010_0_010_0L; 
	 long b3 = 0b000_0_001_0_000_0_000_0_000_0_010_0_000_0_000_0L; 
	 long c1 = 0b000_0_000_0_100_0_001_0_000_0_000_0_000_0_001_0L; 
	 long c2 = 0b000_0_000_0_010_0_000_0_001_0_000_0_000_0_000_0L; 
	 long c3 = 0b000_0_000_0_001_0_000_0_000_0_001_0_001_0_000_0L;*/
	
	public Tile(int tileID, TileType tileType) {
		this.tileType = tileType;
		
		/*if(tileID == 0) this.tileID = a1;
		if(tileID == 1) this.tileID = a2;
		if(tileID == 2) this.tileID = a3;
		if(tileID == 3) this.tileID = b1;
		if(tileID == 4) this.tileID = b2;
		if(tileID == 5) this.tileID = b3;
		if(tileID == 6) this.tileID = c1;
		if(tileID == 7) this.tileID = c2;
		if(tileID == 8) this.tileID = c3;*/
		
		this.tileID = tileID;
	}
	
	public String toString() {
		if(tileType == TileType.BLANK) {
			return " | | ";
		}
		else if(tileType == TileType.X) {
			return " |x| ";
		}
		else {
			return " |O| ";
		}
	}
	
	public TileType getTileType() {
		return this.tileType;
	}
	
	public int getTileID() {
		return this.tileID;
	}
}
