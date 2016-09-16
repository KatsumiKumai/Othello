/**
 * ゲームの状態を管理し、オセロのルールに従って駒の状態を更新させる。
 *
 */

package Othello;

import java.util.ArrayList;
import java.util.Observable;

public class Board extends Observable{



	int data[][];                    //盤面のデータ
	int turn;                        //何ターン目か
	public Stone now_Player;        //黒か白か
	int black_num;                   //黒の駒数
	int white_num;                   //白の駒数
	int liberty[][];				//開放度

	public ArrayList<ArrayList<Point>> updateLog;  //盤面のログ

	public Board(){

		data = new int[8][8];
		data[3][3] = -1;
		data[3][4] = 1;
		data[4][3] = 1;
		data[4][4] = -1;

		turn = 0;
		now_Player = Stone.Black;
		black_num = 2;
		white_num = 2;
		updateLog = new ArrayList<ArrayList<Point>>();

		//開放度
		liberty = new int[10][10];
		for(int y=1; y<9; y++){
			for(int x=1; x<9; x++){
				if(x==1 || y==8 || y==1 || x==9){
					liberty[x][y]=5;
				} else {
					liberty[x][y]=8;
				}
			}
		}
		liberty[1][1]=3;
		liberty[1][8]=3;
		liberty[8][1]=3;
		liberty[8][8]=3;

		liberty[4][3]--;
		liberty[3][3]--;
		liberty[3][4]--;
		liberty[3][5]--;
		liberty[4][5]--;
		liberty[5][5]--;
		liberty[5][4]--;
		liberty[5][3]--;

		liberty[4][4]--;
		liberty[3][4]--;
		liberty[3][5]--;
		liberty[3][6]--;
		liberty[4][6]--;
		liberty[5][6]--;
		liberty[5][5]--;
		liberty[5][4]--;

		liberty[5][3]--;
		liberty[4][3]--;
		liberty[4][4]--;
		liberty[4][5]--;
		liberty[5][5]--;
		liberty[6][5]--;
		liberty[6][4]--;
		liberty[6][3]--;

		liberty[5][4]--;
		liberty[4][4]--;
		liberty[4][5]--;
		liberty[4][6]--;
		liberty[5][6]--;
		liberty[6][6]--;
		liberty[6][5]--;
		liberty[6][4]--;


	}

	public boolean put(Point move, boolean doPut){
		return put(move.x, move.y, doPut);
	}


	public boolean put(int x, int y, boolean doPut){

		//すでに駒があるところには置けない
		if(data[x][y] != 0){
			return false;
		}

		//リバースできないところには置けない
		if(canReverse(x,y)==false){
			return false;
		}

		/*これ以降は駒が置けることがわかっている*/
		//System.out.println("駒を置く座標: " +(x +1)+","+ (y+1));
		//System.out.println("");

		data[x][y] = now_Player.color;  //駒を置く
		reverse(x,y,true);       //駒を反転する

		now_Player = now_Player.oppositePlayer();  //次のプレイヤーの色に変える

		turn++;                             //ターンを増やす
		countDisc();                        //現在の駒数を数える

		//this.printUpdateLog();

		//開放度再計算
		liberty[x+1][y]--;
		liberty[x][y]--;
		liberty[x][y+1]--;
		liberty[x][y+2]--;
		liberty[x+1][y+2]--;
		liberty[x+2][y+2]--;
		liberty[x+2][y+1]--;
		liberty[x+2][y]--;

		if(doPut){
			setChanged();
			notifyObservers();                  //変更を観察者に通知
		}

		return true;
	}

	/*doReverseがtureの時は駒をひっくり返す。
	 *           falseの時は駒がひっくり返るかどうか判定する。
	 */

	public boolean reverse(int x, int y, boolean doReverse){
		int dir[][] = {
				{-1,-1}, {0,-1}, {1,-1},
				{-1, 0},         {1, 0},
				{-1, 1}, {0, 1}, {1, 1}
		};

		boolean reversed =false;

		ArrayList<Point> update = new ArrayList<Point>();

		//駒を置く場所を格納
		if(doReverse){
			update.add(new Point(x,y));
		}

		for(int i=0; i<8; i++){
			//隣のマス
			int x0 = x+dir[i][0];
			int y0 = y+dir[i][1];
			if(isOut(x0,y0) == true){
				continue;
			}
			int nextState = data[x0][y0];
			if(nextState == now_Player.color){
				//System.out.println("隣の駒は同じ色です。リバースできません。: " +(x0+1) +","+ (y0+1));
				continue;
			}else if(nextState == 0){
				//System.out.println("隣は空白です。リバースできません。: " +(x0+1) +","+ (y0+1));
				continue;
			}else{
				//System.out.println("隣は相手の駒です。リバースできる可能性があります。: " +(x0+1) +","+ (y0+1));
				//反転できる可能性あり。次の処理へ
			}

			//隣の隣から端まで走査して、自分の色があればリバース
			int j = 2;

			while(true){

				int x1 = x + (dir[i][0]*j);
				int y1 = y + (dir[i][1]*j);
				//盤面の外に出たらダメ。
				if(isOut(x1,y1) == true){
					break;
				}

				//自分の駒があったら、リバース
				if(data[x1][y1] == now_Player.color){

					if(doReverse){
						//System.out.println("ペアの味方駒: " +(x1+1) +","+ (y1+1));
						for(int k =1; k<j; k++){
							int x2 = x + (dir[i][0]*k);
							int y2 = y + (dir[i][1]*k);
							data[x2][y2] *= -1;
							//System.out.println("反転させる駒: " +(x2+1)+","+ (y2+1));
							//反転させる駒の座標をupdateに格納
							update.add(new Point(x2,y2));
						}
					}
					reversed = true;
					break;
				}

				//空白があったら終了
				if(data[x1][y1] == 0){
					break;
				}

				j++;

			}

		}

		if(doReverse){
			updateLog.add(update);
		}

		return reversed;
	}


	//ひっくり返せるか判定。
	public boolean canReverse(int x, int y){
		return reverse(x, y, false);
	}

	//指定された座標が盤面の外か中か判定。
	public boolean isOut(int x, int y){
		if(x<0 || y<0 || x>=8 || y>=8){
			return true;
		}
		return false;
	}

	//パスチェック
	public boolean checkPass(){

		//コピーデータの全升目に対して、リバースできるかチェック
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){

				//すでに駒があるところはチェックしない
				if(data[x][y] != 0){
					continue;
				}

				//リバースできる（した）とき、falseを返す
				if(canReverse(x,y) == true){
					return false;
				}

			}
		}

		return true;
	}

	public boolean pass(){
		if(this.checkPass() == false)
			return false;
		if(this.turn == 60)
			return false;
		this.updateLog.add(new ArrayList<Point>());
		this.now_Player = this.now_Player.oppositePlayer();
		return true;
	}

	//現在の黒と白のそれぞれの駒数を数える
	public void countDisc(){
		black_num = 0;
		white_num = 0;

		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				if(data[x][y] == 1){
					black_num++;
				}else if(data[x][y] == -1){
					white_num++;
				}
			}
		}
	}

	//一手前に戻す
	public boolean undo(boolean doUndo){
		//ゲーム開始地点なら戻れない
		if(turn == 0){
			return false;
		}

		now_Player = now_Player.oppositePlayer();

		ArrayList<Point> update = new ArrayList<Point>();
		update = updateLog.remove(updateLog.size()-1);

		//前回がパスかどうかで場合分け
		if(update.isEmpty()){
			//前がパス

		} else{
			turn--;

			//石を元に戻す
			Point p = new Point();
			p = update.get(0);

			//開放度再計算
			liberty[p.x+1][p.y]++;
			liberty[p.x][p.y]++;
			liberty[p.x][p.y+1]++;
			liberty[p.x][p.y+2]++;
			liberty[p.x+1][p.y+2]++;
			liberty[p.x+2][p.y+2]++;
			liberty[p.x+2][p.y+1]++;
			liberty[p.x+2][p.y]++;


			data[p.x][p.y] = 0;

			for(int i=1; i<update.size(); i++){
				p = update.get(i);
				data[p.x][p.y] *= -1;
			}

			countDisc();                        //現在の駒数を数える

			if(doUndo){
				setChanged();
				notifyObservers();                  //変更を観察者に通知
			}
		}

		return true;
	}

	public boolean reset(){
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				data[x][y] = 0;
			}
		}


		data[3][3] = -1;
		data[3][4] = 1;
		data[4][3] = 1;
		data[4][4] = -1;

		turn = 0;
		now_Player = Stone.Black;
		black_num = 2;
		white_num = 2;
		updateLog.clear();

		countDisc();                        //現在の駒数を数える

		setChanged();
		notifyObservers();                  //変更を観察者に通知

		return true;
	}

	//現在の駒全体の様子をコンソールに表示
	public void printState(){
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				//System.out.print(data[x][y]+" ");
			}
			//System.out.println("");
		}
	}

	public void printUpdateLog(){

		if(updateLog.isEmpty()){
			//System.out.println("updateLogは空です。");
			return;
		}

		ArrayList<Point> update = new ArrayList<Point>();
		Point tmpPoint = new Point();

		for(int i=0; i < updateLog.size(); i++ ){
			update = updateLog.get(i);
			for(int j=0; j<update.size(); j++){
				tmpPoint = update.get(j);
				//System.out.println(i+"-"+j +"( "+ tmpPoint.x +","+ tmpPoint.y +")");
			}

		}
	}

}
