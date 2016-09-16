package Othello;

import java.util.ArrayList;

public class CPU4_param_change extends Player
{

	public CPU4_param_change(Stone getColor, View getBoard, int getLimit){
		super(getColor);

		getBoard.addPlayer(this);

		if(stoneColor == Stone.Black){
			playerName = "黒";
		} else {
			playerName = "白";
		}

		this.getLimit = getLimit;


	}

	class Move extends Point{
		public int eval = 0;
		public Move(){
			super(0,0);
		}

		public Move(int x, int y, int e){
			super(x, y);
			eval = e;
		}
	};

	@Override
	public Point decideNextMove(Board board) {
		// TODO 自動生成されたメソッド・スタブ

		int eval;
		int best_eval = Integer.MIN_VALUE;
		int limit=this.getLimit;

		Point bestMove = new Point();
		Point checkMove = new Point();
		ArrayList<Point> movableList = new ArrayList<Point>();

		if(this.stoneColor.color == 1){
			if(0 <= board.turn && board.turn < 21){
				//序盤
				mobility_w = 60;
				liberty_w = -12;
				stone_w = -10;
				stable_w = 200;
				corner_w = 50000;
				wing_w = -2000;
				mountain_w = 2000;
				Xmove_w = -4500;
				Cmove_w = -5500;
			} else if(21 <= board.turn && board.turn <= 40){
				//中盤
				mobility_w = 60;
				liberty_w = -12;
				stone_w = 10;
				stable_w = 200;
				corner_w = 50000;
				wing_w = -3000;
				mountain_w = 2000;
				Xmove_w = -4500;
				Cmove_w = -5500;
			} else if(41 < board.turn && board.turn <= 60){
				//終盤
				mobility_w = 60;
				liberty_w = -12;
				stone_w = 30;
				stable_w = 200;
				corner_w = 50000;
				wing_w = -2000;
				mountain_w = 200;
				Xmove_w = -4500;
				Cmove_w = -5500;
			}
		}else if(this.stoneColor.color == -1){
			if(0 <= board.turn && board.turn < 21){
				//序盤
				mobility_w = 60;
				liberty_w = -12;
				stone_w = -10;
				stable_w = 200;
				corner_w = 30000;
				wing_w = -2000;
				mountain_w = 2000;
				Xmove_w = -4500;
				Cmove_w = -5500;
			} else if(21 <= board.turn && board.turn <= 40){
				//中盤
				mobility_w = 60;
				liberty_w = -12;
				stone_w = 10;
				stable_w = 200;
				corner_w = 30000;
				wing_w = -3000;
				mountain_w = 2000;
				Xmove_w = -4500;
				Cmove_w = -5500;
			} else if(41 < board.turn && board.turn <= 60){
				//終盤
				mobility_w = 60;
				liberty_w = -12;
				stone_w = 30;
				stable_w = 200;
				corner_w = 30000;
				wing_w = -2000;
				mountain_w = 200;
				Xmove_w = -4500;
				Cmove_w = -5500;
			}
		}

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

		//角周りのチェック
		for(int i=0; i<movableList.size(); i++){
			checkMove = movableList.get(i);
			//角が空白の時
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

		//事前に次の手をよさそうな順にソート
		sort(board, movableList, 4);

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
					best_eval = eval;
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

		int h;
		//辺の計算
		//ウィングとCmove危険石
		//上辺
		if(board.data[0][0] == 0 && board.data[7][0] == 0){
			h = 2;
			while(h<=5){
				if(board.data[h][0] != this.stoneColor.color) break;
				h++;
			}
			if(h==6){
				if(board.data[1][0] == this.stoneColor.color && board.data[6][0] == 0){
					eval = eval + wing_w;
				} else if(board.data[1][0] == 0 && board.data[6][0] == this.stoneColor.color){
					eval = eval + wing_w;
				} else if(board.data[1][0] == this.stoneColor.color && board.data[6][0] == this.stoneColor.color){
					eval = eval + mountain_w;
				}
			}
			else {
				if(board.data[1][0] == this.stoneColor.color)
					eval = eval + Cmove_w;
				if(board.data[6][0] == this.stoneColor.color )
					eval = eval + Cmove_w;
			}
		}
		//右辺
		if(board.data[7][0] == 0 && board.data[7][7] == 0){
			h = 2;
			while(h<=5){
				if(board.data[7][h] != this.stoneColor.color) break;
				h++;
			}
			if(h==6){
				if(board.data[7][1] == this.stoneColor.color && board.data[7][6] == 0){
					eval = eval + wing_w;
				} else if(board.data[7][1] == 0 && board.data[7][6] == this.stoneColor.color){
					eval = eval + wing_w;
				} else if(board.data[7][1] == this.stoneColor.color && board.data[7][6] == this.stoneColor.color){
					eval = eval + mountain_w;
				}
			}
			else {
				if(board.data[7][1] == this.stoneColor.color)
					eval = eval + Cmove_w;
				if(board.data[7][6] == this.stoneColor.color )
					eval = eval + Cmove_w;
			}
		}
		//下辺
		if(board.data[0][7] == 0 && board.data[7][7] == 0){
			h = 2;
			while(h<=5){
				if(board.data[h][7] != this.stoneColor.color) break;
				h++;
			}
			if(h==6){
				if(board.data[1][7] == this.stoneColor.color && board.data[6][7] == 0){
					eval = eval + wing_w;
				} else if(board.data[1][7] == 0 && board.data[6][7] == this.stoneColor.color){
					eval = eval + wing_w;
				} else if(board.data[1][7] == this.stoneColor.color && board.data[6][7] == this.stoneColor.color){
					eval = eval + mountain_w;
				}
			}
			else {
				if(board.data[1][7] == this.stoneColor.color)
					eval = eval + Cmove_w;
				if(board.data[6][7] == this.stoneColor.color )
					eval = eval + Cmove_w;
			}
		}
		//左辺
		if(board.data[0][0] == 0 && board.data[0][7] == 0){
			h = 2;
			while(h<=5){
				if(board.data[0][h] != this.stoneColor.color) break;
				h++;
			}
			if(h==6){
				if(board.data[0][1] == this.stoneColor.color && board.data[0][6] == 0){
					eval = eval + wing_w;
				} else if(board.data[0][1] == 0 && board.data[0][6] == this.stoneColor.color){
					eval = eval + wing_w;
				} else if(board.data[0][1] == this.stoneColor.color && board.data[0][6] == this.stoneColor.color){
					eval = eval + mountain_w;
				}
			}
			else {
				if(board.data[0][1] == this.stoneColor.color)
					eval = eval + Cmove_w;
				if(board.data[0][6] == this.stoneColor.color )
					eval = eval + Cmove_w;
			}
		}

		//確定石
		//上辺
		//左から
		int stable=0;
		for(int i=0; i<8; i++){
			if(board.data[i][0] != this.stoneColor.color) break;
			stable++;
		}
		//右から
		if(stable < 8){
			for(int i=7; i>0; i--){
				if(board.data[i][0] != this.stoneColor.color)break;
				stable++;
			}
		}
		eval = eval + stable * stable_w;

		//右辺
		//上から
		stable=0;
		for(int i=0; i<8; i++){
			if(board.data[7][i] != this.stoneColor.color) break;
			stable++;
		}
		//下から
		if(stable < 8){
			for(int i=7; i>0; i--){
				if(board.data[7][i] != this.stoneColor.color)break;
				stable++;
			}
		}
		eval = eval + stable * stable_w;

		//下辺
		//左から
		stable=0;
		for(int i=0; i<8; i++){
			if(board.data[i][7] != this.stoneColor.color) break;
			stable++;
		}
		//右から
		if(stable < 8){
			for(int i=7; i>0; i--){
				if(board.data[i][7] != this.stoneColor.color)break;
				stable++;
			}
		}
		eval = eval + stable * stable_w;

		//左辺
		//上から
		stable=0;
		for(int i=0; i<8; i++){
			if(board.data[0][i] != this.stoneColor.color) break;
			stable++;
		}
		//下から
		if(stable < 8){
			for(int i=7; i>0; i--){
				if(board.data[0][i] != this.stoneColor.color)break;
				stable++;
			}
		}
		eval = eval + stable * stable_w;

		//角の点数
		//左上
		if(board.data[0][0] == this.stoneColor.color){
			eval = eval + corner_w;
		}else if(board.data[0][0] == this.stoneColor.color * -1){
			eval = eval - corner_w;
		}

		//左下
		if(board.data[0][7] == this.stoneColor.color){
			eval = eval + corner_w;
		}else if(board.data[0][7] == this.stoneColor.color * -1){
			eval = eval - corner_w;
		}

		//右上
		if(board.data[7][0] == this.stoneColor.color){
			eval = eval + corner_w;
		}else if(board.data[7][0] == this.stoneColor.color * -1){
			eval = eval - corner_w;
		}
		//右下
		if(board.data[7][7] == this.stoneColor.color){
			eval = eval + corner_w;
		}else if(board.data[7][7] == this.stoneColor.color * -1){
			eval = eval - corner_w;
		}

		//角周りの危険石
		//角が空白の時
		//左上
		if(board.data[0][0] == 0 || board.data[0][0] == this.stoneColor.color*-1 && board.data[0][2] == 0 || board.data[0][0] == this.stoneColor.color*-1 && board.data[2][0] == 0 || board.data[0][0] == this.stoneColor.color*-1 && board.data[2][2] == 0){
			if(board.data[1][1] == this.stoneColor.color || board.data[0][1] == this.stoneColor.color || board.data[1][0] == this.stoneColor.color){
				eval = eval + Xmove_w;
			} else if(board.data[1][1] == this.stoneColor.color * -1 || board.data[0][1] == this.stoneColor.color * -1 || board.data[1][0] == this.stoneColor.color * -1){
				eval = eval - Xmove_w;
			}
		}
		//左下
		if(board.data[0][7] == 0 || board.data[0][7] == this.stoneColor.color*-1 && board.data[2][7] == 0 || board.data[0][7] == this.stoneColor.color*-1 && board.data[0][5] == 0 || board.data[0][7] == this.stoneColor.color*-1 && board.data[2][5] == 0){
			if(board.data[1][6] == this.stoneColor.color || board.data[0][6] == this.stoneColor.color || board.data[1][7] == this.stoneColor.color){
				eval = eval + Xmove_w;
			} else if(board.data[1][6] == this.stoneColor.color * -1 || board.data[0][6] == this.stoneColor.color * -1 || board.data[1][7] == this.stoneColor.color * -1){
				eval = eval - Xmove_w;
			}
		}
		//右上
		if(board.data[7][0] == 0 || board.data[7][0] == this.stoneColor.color*-1 && board.data[5][0] == 0 || board.data[7][0] == this.stoneColor.color*-1 && board.data[7][2] == 0 || board.data[7][0] == this.stoneColor.color*-1 && board.data[5][2] == 0){
			if(board.data[6][1] == this.stoneColor.color || board.data[7][1] == this.stoneColor.color || board.data[6][0] == this.stoneColor.color){
				eval = eval + Xmove_w;
			} else if(board.data[6][1] == this.stoneColor.color * -1 || board.data[7][1] == this.stoneColor.color * -1 || board.data[6][0] == this.stoneColor.color * -1){
				eval = eval - Xmove_w;
			}
		}
		//右下
		if(board.data[7][7] == 0 || board.data[7][7] == this.stoneColor.color*-1 && board.data[5][7] == 0 || board.data[7][7] == this.stoneColor.color*-1 && board.data[7][5] == 0 || board.data[7][7] == this.stoneColor.color*-1 && board.data[5][5] == 0){
			if(board.data[6][6] == this.stoneColor.color || board.data[7][6] == this.stoneColor.color || board.data[6][7] == this.stoneColor.color){
				eval = eval + Xmove_w;
			} else if(board.data[6][6] == this.stoneColor.color * -1 || board.data[7][6] == this.stoneColor.color * -1 || board.data[6][7] == this.stoneColor.color * -1){
				eval = eval - Xmove_w;
			}
		}


		//駒数
		board.countDisc();
		if(this.stoneColor == Stone.Black){
			eval = eval + board.black_num * stone_w;
			eval = eval - board.white_num * stone_w;
		}else{
			eval = eval + board.white_num * stone_w;
			eval = eval - board.black_num * stone_w;
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
								eval = eval + mobility_w;
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
								eval = eval - mobility_w;
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
								eval = eval - mobility_w;
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
								eval = eval + mobility_w;
							}
						}
					}
					board.now_Player.oppositePlayer();
				}

		//開放度
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				//駒がないときは無視
				if(board.data[x][y] == 0)
					continue;

				//自分の色の時、開放度を
				if(board.data[x][y] == this.stoneColor.color){
					eval = eval + liberty_w * board.liberty[x+1][y+1];
				}else{
					eval = eval - liberty_w * board.liberty[x+1][y+1];
				}
			}
		}


		return eval;
	}

	private void sort(Board board, ArrayList<Point> movableList, int limit)
	{
		ArrayList<Point> moves = new ArrayList<Point>();

		for(int i=0; i<movableList.size(); i++){
			int eval;
			Point p = (Point) movableList.get(i);

			board.put(p, false);
			eval = -alphabeta(board, limit-1, -Integer.MAX_VALUE, -Integer.MIN_VALUE);
			board.undo(false);

			Move move = new Move(p.x, p.y, eval);
			moves.add(move);
		}

		//評価値の大きい順にソート
		int begin, current;
		for(begin=0; begin<moves.size() -1; begin++){
			for(current=1; current<moves.size(); current++){
				Move b = (Move) moves.get(begin);
				Move c = (Move) moves.get(current);
				if(b.eval < c.eval){
					//交換
					moves.set(begin,c);
					moves.set(current,b);
				}
			}
		}
		//結果の書き出し
		movableList.clear();
		for(int i=0; i<moves.size(); i++){
			movableList.add(moves.get(i));
		}

		return;
	}

	@Override
	public void setNextMove(int x, int y) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
