package Othello;

import java.util.*;

public class RandomCPU {
	
	int color;	//BLACK or WHITE
	
	public RandomCPU(){
		color = -1;
	}
	
	int[] decide(Board state){
		ArrayList<int[]> array = new ArrayList<int[]>();
		
		//盤面の空マスを置けるかチェック
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				
				//すでに駒があるときはパス
				if(state.data[x][y] != 0)
					continue;
				
				//置けるマスの時、候補として記憶
				if(state.canReverse(x,y) == true){
					int pos[] = {x,y};
					array.add(pos);
				}
			}
		}
		
		//ランダム選択
		if(array.size() <= 0){
			int pos[] = {-1, -1};
			return pos;
		}
		Random rnd = new Random();
		int index = rnd.nextInt(array.size());
		
		return array.get(index);
		
	}

}
