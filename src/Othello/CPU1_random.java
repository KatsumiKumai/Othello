package Othello;

import java.util.ArrayList;

public class CPU1_random extends Player{

	public CPU1_random(Stone getColor, View getBoard) {
		super(getColor);
		// TODO 自動生成されたコンストラクター・スタブ

		getBoard.addPlayer(this);

		if(stoneColor == Stone.Black){
			playerName = "黒";
		} else {
			playerName = "白";
		}
	}

	@Override
	public Point decideNextMove(Board board) {
		// TODO 自動生成されたメソッド・スタブ
		Point bestMove = new Point();

		ArrayList<Point> moveList = new ArrayList<Point>();

		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				//すでに駒があるときはパス
				if(board.data[x][y] != 0)
					continue;

				//ひっくり返せるなら候補として格納
				if(board.canReverse(x, y) == true){
					moveList.add(new Point(x,y));
				}

			}
		}

		//ランダム選択
		if(moveList.isEmpty() == true){
			bestMove.setMove(-1, -1);
			return bestMove;
		}
		int index = (int)(Math.random()*moveList.size());

		bestMove = moveList.get(index);

		bestMove.printMove();

		return bestMove;
	}

	/*
	@Override
	public Move getMove() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	*/

	@Override
	public void setNextMove(int x, int y) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
