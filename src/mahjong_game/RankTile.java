package mahjong_game;

public abstract class RankTile extends Tile {
	
	protected int rank;
	
	public RankTile(int rank){
		this.rank = rank;
		setToolTipText(this.toString());
	}
	
	public boolean matches(Tile other){
		return super.matches(other) && (this.rank == ((RankTile)other).rank);
	}
}
