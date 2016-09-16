/**
 * メインクラス。
 * クラスを生成する。
 */

package Othello;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MainOthello extends JFrame implements ActionListener {
	static final int BLACK_LIMIT = 8;
	static final int WHITE_LIMIT = 8;

	static final int CROWD = 6;

	static Board board;
	static View view;
	static Player black_player1;
	static Player white_player1;
	static Player black_player2;
	static Player white_player2;
	JButton button[];

	public MainOthello(){
		setTitle("Othello");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//ゲームの状態クラスを生成
		board = new Board();

		//ゲームの状態クラスをゲームボードに渡して、状態を描画する
		view = new View(board);

		view.setLayout(null);

		/*ボタンを配置する*/
		button = new JButton[2];
		button[0] = new JButton("Undo");
		button[1] = new JButton("Reset");
		for(int i=0; i<2; i++){
			button[i].addActionListener(this);
			button[i].setBounds(225+i*100, 470, 75, 25);
			view.add(button[i]);
		}

		getContentPane().add(view);
		pack();

		//プレイヤークラスを生成
		//切り替え部分
		black_player2 = new Manual(Stone.Black, view);
		//black_player2 = new CPU1_random(Stone.Black, view);
		//black_player2 = new CPU2_most_old(Stone.Black, view, BLACK_LIMIT);
		//black_player2 = new CPU3_new_generation(Stone.Black, view, BLACK_LIMIT);
		//black_player2 = new CPU4_param_change(Stone.Black, view, BLACK_LIMIT);

		//white_player2 = new Manual(Stone.White, view);
		white_player2 = new CPU1_random(Stone.White, view);
		//white_player2 = new CPU2_most_old(Stone.White, view, WHITE_LIMIT);
		//white_player2 = new CPU3_new_generation(Stone.White, view, WHITE_LIMIT);
		//white_player2 = new CPU4_param_change(Stone.White, view, WHITE_LIMIT);
	}

	public static void main(String[] args){
		MainOthello app = new MainOthello();
		app.setVisible(true);


		while(board.turn != 60){
			System.out.print(board.turn+"黒mob:"+black_player2.mobility_w+"   "+"白mob:"+white_player2.mobility_w);
			System.out.println("");
			if(board.now_Player == Stone.Black){
				//駒を置く＆置けない場合はメッセージ
				while(board.put(black_player2.decideNextMove(board), true) == false){

				}

				//パスになるかチェック
				//now_Playerは白
				if(board.pass() == true){
					///now_Playerは黒
					JOptionPane.showMessageDialog(view, "パス！次は黒の番です！");
					if(board.checkPass() == true){
						break;
					}
				}
			} else {
				while(board.put(white_player2.decideNextMove(board), true) == false){

				}
				//パスになるかチェック
				//now_Playerは黒
				if(board.pass() == true){
				//now_Playerは白
					JOptionPane.showMessageDialog(view, "パス！次は白の番です！");
					if(board.checkPass() == true){
						break;
					}
				}
			}
		}

		if(board.black_num > board.white_num){
			JOptionPane.showMessageDialog(view, "ゲーム終了！ 黒の勝ちです！");

		}else if(board.black_num < board.white_num){
			JOptionPane.showMessageDialog(view, "ゲーム終了！ 白の勝ちです！");

		}else if(board.black_num == board.white_num){
			JOptionPane.showMessageDialog(view, "ゲーム終了！ 引き分けです！");

		}

	}


		/*
		int step = 0;
		double max;
		double roulette_sum;
		double roulette_point;
		double roulette;
		int kotai1=0;
		int kotai2=0;
		int best=0;
		double best_fitness = 0;
		int flag=0;
		int r;

		Individual individual[] = new Individual[CROWD];
		for(int i=0; i<CROWD; i++){
			individual[i] = new Individual();
		}
		Individual next_individual[] = new Individual[CROWD];
		for(int i=0; i<CROWD; i++){
			next_individual[i] = new Individual();
		}

		for(int i=0; i<CROWD; i++){
			for(int j=0; j<9; j++){
				individual[i].solution[j] = (int) ((double)1000*Math.random());
			}
		}


		while(step < 500){
			for(int i=0; i<CROWD; i++){
				System.out.print("黒mobility:"+black_player2.mobility_w);
				System.out.print("白mobility:"+white_player2.mobility_w);

				while(board.turn != 60){
					black_player2.mobility_w = individual[i].solution[0];
					black_player2.liberty_w = individual[i].solution[1];
					black_player2.stone_w = individual[i].solution[2];
					black_player2.stable_w = individual[i].solution[3];
					black_player2.corner_w = individual[i].solution[4];
					black_player2.wing_w = individual[i].solution[5];
					black_player2.mountain_w = individual[i].solution[6];
					black_player2.Xmove_w = individual[i].solution[7];
					black_player2.Cmove_w = individual[i].solution[8];


					if(board.now_Player == Stone.Black){
						//駒を置く＆置けない場合はメッセージ
						while(board.put(black_player2.decideNextMove(board), true) == false){

						}

						//パスになるかチェック
						//now_Playerは白
						if(board.pass() == true){
							///now_Playerは黒
							//JOptionPane.showMessageDialog(view, "パス！次は黒の番です！");
							if(board.checkPass() == true){
								break;
							}
						}
					} else {
						while(board.put(white_player2.decideNextMove(board), true) == false){

						}
						//パスになるかチェック
						//now_Playerは黒
						if(board.pass() == true){
						//now_Playerは白
							//JOptionPane.showMessageDialog(view, "パス！次は白の番です！");
							if(board.checkPass() == true){
								break;
							}
						}
					}
				}

				System.out.print("黒石"+board.black_num);
				System.out.println("白石"+board.white_num);
				System.out.println("");
				individual[i].fitness = board.black_num/64;
				board.reset();

			}


			max = 0;
			for(int i=0; i<CROWD; i++){
				if(max < individual[i].fitness){
					max = individual[i].fitness;
					best = i;
				}
			}

			if(max > best_fitness){
				best_fitness = max;
			}

			if(max == 1.0){
				flag = 1;
				break;
			}

			double scale_fitness;
			for(int i=0; i<CROWD; i++){
				scale_fitness = 1.0;
				for(int j=0; j<100; j++){
					scale_fitness = scale_fitness * individual[i].fitness;
				}
				individual[i].fitness = scale_fitness;
			}

			for(int i=0; i<CROWD; i+=2){
				//ルーレット戦略による選択
				//適応度の合計値を計算
				roulette_sum = 0.0;
				for(int j=0; j<CROWD; j++){
					roulette_sum += individual[j].fitness;
				}

				//一つ目の個体を選択
				roulette = 0.0;
				//ルーレットが止まった位置を決める
				roulette_point = (double)roulette_sum*Math.random();
				//適応度を足しあわせてルーレットが止まった位置になったら、その個体を選択
				for(int j=0; j<CROWD; j++){
					roulette += individual[j].fitness;
					if(roulette > roulette_point){
						kotai1= j;
						break;
						}
					}

				//二つ目の個体を選択
				roulette = 0.0;
				roulette_point = (double)roulette_sum*Math.random();
				for(int j=0; j<CROWD; j++){
					roulette += individual[j].fitness;
					if(roulette > roulette_point){
						if(j != kotai1){
							kotai2 = j;
							break;
						} else {
							kotai2 = (int) ((double)CROWD*Math.random());
						}
					}
				}

				//交叉により子個体を生成
				for(int j=0; j<9; j++){
					//乱数を発生させ、０か１によって交叉する
					r = (int) ((double)2*Math.random());
					if(r == 0){
						next_individual[i].solution[j] = individual[kotai1].solution[j];
						next_individual[i+1].solution[j] = individual[kotai2].solution[j];
					} else{
						next_individual[i].solution[j] = individual[kotai2].solution[j];
						next_individual[i+1].solution[j] = individual[kotai1].solution[j];
					}
				}


				//子個体に突然変異
				//次世代一つ目
				for(int j=0; j<9; j++){
					if(((double)Math.random()) <= 0.015){
						next_individual[i].solution[j] = (int) ((double)1000*Math.random());
					} else{
						//何もしない
					}
				}

				//次世代二つ目
				for(int j=0; j<9; j++){
					if(((double)Math.random()) <= 0.015){
						next_individual[i+1].solution[j] = (int) ((double)1000*Math.random());
					} else{
						//何もしない
					}
				}

			}

			//生成された集団を次世代集団とする
			for(int i=0; i<CROWD; i++){
				for(int j=0; j<9; j++){
					individual[i].solution[j] = next_individual[i].solution[j];
				}
			}

			for(int i=0; i<9; i++){
				System.out.print(individual[best].solution[i]+",");
			}
			System.out.print("");

			step++;

		}


		if(flag==1){
			for(int i=0; i<9; i++){
				System.out.print(individual[best].solution[i]+",");
			}
			System.out.print("");
		} else if(flag==0){
			for(int i=0; i<9; i++){
				System.out.print(individual[best].solution[i]+",");
			}
			System.out.print("");
		}


	}
	*/

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.button[0]){
			board.undo(true);
		}else if(e.getSource() == this.button[1]){
			//System.out.println("リセット！");
			board.reset();
		}
	}

}
