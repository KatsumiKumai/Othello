package Othello;

public abstract class Player {

	protected final Stone stoneColor;     //黒か白か    黒(1)  白(-1)

	String playerName;
	int getLimit;

	public int mobility_w;
	public int liberty_w;
	public int stone_w;
	public int stable_w;

	public int corner_w;

	public int wing_w;

	public int mountain_w;

	public int Xmove_w;

	public int Cmove_w;

	public Player(Stone getColor){
		stoneColor = getColor;

	}

	abstract public void setNextMove(int x, int y);

	abstract public Point decideNextMove(Board nowState);

	//abstract public Move getMove();

}
