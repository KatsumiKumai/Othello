package Othello;

import java.util.ArrayList;

public class CPU2_most_old extends Player{

	public CPU2_most_old(Stone getColor, View getBoard, int getLimit){
		super(getColor);

		getBoard.addPlayer(this);

		if(stoneColor == Stone.Black){
			playerName = "黒";
		} else {
			playerName = "白";
		}

		this.getLimit = getLimit;
	}

	@Override
	public Point decideNextMove(Board board) {
		// TODO 自動生成されたメソッド・スタブ

		int eval;
		int best_eval = Integer.MIN_VALUE;
		int limit=this.getLimit;

		Point bestMove = new Point();
		Point checkMove = new Point();
		ArrayList<Point> movableList = new ArrayList<Point>();

		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				//すでに駒があるときはパス
				if(board.data[x][y] != 0)
					continue;

				//ひっくり返せるなら候補として格納
				if(board.canReverse(x, y) == true){
					movableList.add(new Point(x,y));
				}
			}
		}

		//置ける場所がないとき
		if(movableList.isEmpty() == true){
			bestMove.setMove(-1, -1);
			return bestMove;
		}

		//置ける場所が一か所しかないときは探索しないでそのまま返す
		if(movableList.size() == 1){
			bestMove = movableList.get(0);
			return bestMove;
		}

		//角が取れるかチェック
		for(int i=0; i<movableList.size(); i++){
			checkMove = movableList.get(i);
			if(checkMove.x == 0 && checkMove.y == 0)
				return checkMove;
			if(checkMove.x == 7 && checkMove.y == 0)
				return checkMove;
			if(checkMove.x == 0 && checkMove.y == 7)
				return checkMove;
			if(checkMove.x == 7 && checkMove.y == 7)
				return checkMove;
		}
		//角周りのチェック
		for(int i=0; i<movableList.size(); i++){
			checkMove = movableList.get(i);
			//左上
			if(board.data[0][0] == 0){
				if((checkMove.x == 0 && checkMove.y == 1) || (checkMove.x == 1 && checkMove.y == 0) || (checkMove.x == 1 && checkMove.y == 1)){
					movableList.remove(i);
				}
			}
			//左下
			if(board.data[0][7] == 0){
				if((checkMove.x == 0 && checkMove.y == 6) || (checkMove.x == 1 && checkMove.y == 7) || (checkMove.x == 1 && checkMove.y == 6)){
					movableList.remove(i);
				}
			}
			//右上
			if(board.data[7][0] == 0){
				if((checkMove.x == 6 && checkMove.y == 0) || (checkMove.x == 7 && checkMove.y == 1) || (checkMove.x == 6 && checkMove.y == 1)){
					movableList.remove(i);
				}
			}
			//右下
			if(board.data[7][7] == 0){
				if((checkMove.x == 6 && checkMove.y == 7) || (checkMove.x == 7 && checkMove.y == 6) || (checkMove.x == 6 && checkMove.y == 6)){
					movableList.remove(i);
				}
			}
		}


		//αβで探索
		if(board.turn > 50){
			//System.out.println("完全モードです！！！！！！！！！！！");
			best_eval = Integer.MIN_VALUE;
			for(int i=0; i<movableList.size(); i++){
				board.put(movableList.get(i), false);
				eval = -lastAlphabeta(board, limit-1, Integer.MAX_VALUE, Integer.MIN_VALUE);
				board.undo(false);

				if(eval > best_eval){
					best_eval = eval;
					bestMove = movableList.get(i);
				}
			}

		} else {
			for(int i=0; i<movableList.size(); i++){
				board.put(movableList.get(i), false);
				eval = -alphabeta(board, limit-1, -Integer.MAX_VALUE, -Integer.MIN_VALUE);
				board.undo(false);

				if(eval > best_eval){
					//best_eval = eval;
					bestMove = movableList.get(i);
				}
			}
		}

		return bestMove;
	}

	private int alphabeta(Board board, int limit, int alpha, int beta){
		if(board.turn == 60 || limit == 0)
			return evaluate(board);

		ArrayList<Point> movableList = new ArrayList<Point>();
		int eval = 0;

		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				//すでに駒があるときは無視
				if(board.data[x][y] != 0)
					continue;

				//ひっくり返せるなら候補として格納
				if(board.canReverse(x, y) == true){
					movableList.add(new Point(x,y));
				}
			}
		}

		//置けるところがないときパスして相手の色にする
		if(movableList.size() == 0){
			board.pass();
			if(board.checkPass() == true){
				limit = 0;
			}
			eval = -alphabeta(board, limit, -beta, -alpha);
			board.undo(false);
			return eval;
		}

		for(int i=0; i<movableList.size(); i++){
			board.put(movableList.get(i), false);
			eval = -alphabeta(board, limit-1, -beta, -alpha);
			board.undo(false);

			alpha = Math.max(alpha, eval);

			if(alpha >= beta){
				return alpha;
			}
		}

		return alpha;

	}

	private int lastAlphabeta(Board board, int limit, int alpha, int beta){
		if(board.turn == 60 || limit == 0)
			return lastEvaluate(board);

		ArrayList<Point> movableList = new ArrayList<Point>();
		int eval = 0;

		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				//すでに駒があるときは無視
				if(board.data[x][y] != 0)
					continue;

				//ひっくり返せるなら候補として格納
				if(board.canReverse(x, y) == true){
					movableList.add(new Point(x,y));
				}
			}
		}

		//置けるところがないときパスして相手の色にする
		if(movableList.size() == 0){
			board.pass();
			if(board.checkPass() == true){
				limit = 0;
			}
			eval = -lastAlphabeta(board, limit, -beta, -alpha);
			board.undo(false);
			return eval;
		}

		for(int i=0; i<movableList.size(); i++){
			board.put(movableList.get(i), false);
			eval = -lastAlphabeta(board, limit-1, -beta, -alpha);
			board.undo(false);

			alpha = Math.max(alpha, eval);

			if(alpha >= beta){
				return alpha;
			}
		}

		return alpha;
	}

	private int lastEvaluate(Board board){
		int eval=0;
		//駒数
		board.countDisc();
		if(this.stoneColor == Stone.Black){
			eval = eval + board.black_num *1000;
			eval = eval - board.white_num*1000;
		}else{
			eval = eval + board.white_num *1000;
			eval = eval - board.black_num*1000;
		}

		return eval;
	}

	private int evaluate(Board board) {
		// TODO 自動生成されたメソッド・スタブ
		int eval=0;

		//角の点数
		//左上
		if(board.data[0][0] == this.stoneColor.color){
			eval = eval+50000;
		}else if(board.data[0][0] == this.stoneColor.color*-1){
			eval = eval-50000;
		} else if(board.data[0][0]==0){
			if(board.data[0][1] == this.stoneColor.color || board.data[1][0] == this.stoneColor.color || board.data[1][1] == this.stoneColor.color)
				eval = eval -30000;
		}
		//左下
		if(board.data[0][7] == this.stoneColor.color){
			eval = eval+50000;
		}else if(board.data[0][7] == this.stoneColor.color*-1){
			eval = eval-50000;
		} else if(board.data[0][7]==0){
			if(board.data[0][6] == this.stoneColor.color || board.data[1][7] == this.stoneColor.color || board.data[1][6] == this.stoneColor.color)
				eval = eval -30000;
		}
		//右上
		if(board.data[7][0] == this.stoneColor.color){
			eval = eval+50000;
		}else if(board.data[7][0] == this.stoneColor.color*-1){
			eval = eval-50000;
		} else if(board.data[7][0]==0){
			if(board.data[6][0] == this.stoneColor.color || board.data[7][1] == this.stoneColor.color || board.data[6][1] == this.stoneColor.color)
				eval = eval -30000;
		}
		//右下
		if(board.data[7][7] == this.stoneColor.color){
			eval = eval+50000;
		}else if(board.data[7][7] == this.stoneColor.color*-1){
			eval = eval-50000;
		} else if(board.data[7][7]==0){
			if(board.data[7][6] == this.stoneColor.color || board.data[6][7] == this.stoneColor.color || board.data[6][6] == this.stoneColor.color)
				eval = eval -30000;
		}

		//駒数
		board.countDisc();
		if(this.stoneColor == Stone.Black){
			eval = eval + board.black_num *1000;
			eval = eval - board.white_num*1000;
		}else{
			eval = eval + board.white_num *1000;
			eval = eval - board.black_num*1000;
		}

		//着手可能数
		if(board.now_Player == this.stoneColor){
			//自分の着手可能数
			for(int y=0; y<8; y++){
				for(int x=0; x<8; x++){
					//すでに駒があるときは無視
					if(board.data[x][y] != 0)
						continue;

					//ひっくり返せるなら点数を計算
					if(board.canReverse(x, y) == true){
						eval = eval + 1000;
					}
				}
			}
			//相手の着手可能数
			board.now_Player.oppositePlayer();
			for(int y=0; y<8; y++){
				for(int x=0; x<8; x++){
					//すでに駒があるときは無視
					if(board.data[x][y] != 0)
						continue;

					//ひっくり返せるなら点数を計算
					if(board.canReverse(x, y) == true){
						eval = eval - 1000;
					}
				}
			}
			board.now_Player.oppositePlayer();
		} else {
			//相手の着手可能数
			for(int y=0; y<8; y++){
				for(int x=0; x<8; x++){
					//すでに駒があるときは無視
					if(board.data[x][y] != 0)
						continue;

					//ひっくり返せるなら点数を計算
					if(board.canReverse(x, y) == true){
						eval = eval - 1000;
					}
				}
			}
			//自分の着手可能数
			board.now_Player.oppositePlayer();
			for(int y=0; y<8; y++){
				for(int x=0; x<8; x++){
					//すでに駒があるときは無視
					if(board.data[x][y] != 0)
						continue;

					//ひっくり返せるなら点数を計算
					if(board.canReverse(x, y) == true){
						eval = eval + 1000;
					}
				}
			}
			board.now_Player.oppositePlayer();
		}

		return eval;
	}


	@Override
	public void setNextMove(int x, int y) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
