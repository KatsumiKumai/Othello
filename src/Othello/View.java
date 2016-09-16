/**
 *  stateの更新によってコンポーネントのグラフィックを更新する
 *
 */

package Othello;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class View extends JPanel implements MouseListener, Observer{

	static final int GRID_SIZE = 50;
	static final int MARGIN_WIDTH = 50;
	static final int BOARD_WIDTH = GRID_SIZE * 8;

	Board board;

	protected ArrayList<Player> player;

	public View(Board gs){
		player = new ArrayList<Player>();

		setPreferredSize(new Dimension(BOARD_WIDTH+2*MARGIN_WIDTH, BOARD_WIDTH+3*MARGIN_WIDTH));
		addMouseListener(this);

		board = gs;
		board.addObserver(this);
	}

	public void addPlayer(Player str){
		player.add(str);
	}

	public void paintComponent(Graphics g){
		//背景
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, BOARD_WIDTH+2*MARGIN_WIDTH, BOARD_WIDTH+3*MARGIN_WIDTH);

		//盤
		g.setColor(new Color(51, 153,51));
		g.fillRect(MARGIN_WIDTH, MARGIN_WIDTH, BOARD_WIDTH, BOARD_WIDTH);

		//線
		g.setColor(Color.BLACK);
		for(int i=0; i<=8; i++){
			g.drawLine(MARGIN_WIDTH, i*GRID_SIZE+MARGIN_WIDTH, BOARD_WIDTH+MARGIN_WIDTH, i*GRID_SIZE+MARGIN_WIDTH);
			g.drawLine(i*GRID_SIZE+MARGIN_WIDTH, MARGIN_WIDTH, i*GRID_SIZE+MARGIN_WIDTH, BOARD_WIDTH+MARGIN_WIDTH);
		}
		g.setColor(Color.DARK_GRAY);
		//g.drawRect(SIZE*2, SIZE*2, SIZE*4, SIZE*4);

		//駒
		for(int y=0; y<8; y++){
			for(int x=0; x<8; x++){
				if(board.data[x][y] == 1){
					g.setColor(Color.BLACK);
					g.fillOval(x*GRID_SIZE+MARGIN_WIDTH+5, y*GRID_SIZE+MARGIN_WIDTH+5, GRID_SIZE-10, GRID_SIZE-10);
				}else if(board.data[x][y] == -1){
					g.setColor(Color.WHITE);
					g.fillOval(x*GRID_SIZE+MARGIN_WIDTH+5, y*GRID_SIZE+MARGIN_WIDTH+5, GRID_SIZE-10, GRID_SIZE-10);
				}
			}
		}

		//データ表示
		//行・列番号表示
		g.setColor(Color.BLACK);
		String[] alphabet = {"a","b","c","d","e","f","g","h"};
		for(int i=0; i<8; i++){
			g.drawString(alphabet[i], MARGIN_WIDTH+i*GRID_SIZE+GRID_SIZE/2-5, MARGIN_WIDTH-5);
			g.drawString(""+(i+1), MARGIN_WIDTH-10, MARGIN_WIDTH+i*GRID_SIZE+GRID_SIZE/2+5);
		}
		if(board.now_Player == Stone.Black){
			g.drawString("黒の番です", MARGIN_WIDTH+5, BOARD_WIDTH+MARGIN_WIDTH+15);
		}else {
			g.drawString("白の番です", MARGIN_WIDTH+5, BOARD_WIDTH+MARGIN_WIDTH+15);
		}
		g.drawString("黒："+board.black_num+"  白："+board.white_num , MARGIN_WIDTH+5, BOARD_WIDTH+MARGIN_WIDTH+30);

	}

	public void update(Observable o, Object arg) {
		repaint();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

		System.out.println("MouseClicked!");
		int x = e.getX();
		int y = e.getY();

		int i = (x - MARGIN_WIDTH) / GRID_SIZE;
		int j = (y - MARGIN_WIDTH) / GRID_SIZE;

		System.out.println("(i,j)=("+(i+1)+","+(j+1)+")");

		if(board.isOut(i, j) != true){
			Iterator<Player> itr = player.iterator();
			while(itr.hasNext()){
				itr.next().setNextMove(i, j);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
