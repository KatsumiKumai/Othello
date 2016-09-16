/**
 * プレイヤーの情報を保持するためのクラス。
 *
 * @author 克仁
 *
 */

package Othello;


public class Manual extends Player{

	Point nextmove = new Point();

	public Manual(Stone getPlayer, View getBoard){
		super(getPlayer);

		getBoard.addPlayer(this);

		if(stoneColor == Stone.Black){
			playerName = "黒";
		} else {
			playerName = "白";
		}
	}

	public synchronized void setNextMove(int getx, int gety){
		this.nextmove.x = getx;
		this.nextmove.y = gety;
		notifyAll();
	}

	public synchronized Point decideNextMove(Board nowState){

		System.out.print(playerName+"の駒を置く場所をクリックしてください。");

		try{
			wait();
		} catch(InterruptedException e){
			System.out.println(e);
		}

		return this.nextmove;
	}

	/*
	public Move getMove(){
		return this.nextmove;
	}
	*/

}
