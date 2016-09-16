/**
 * 次の一手の座標を保持したクラス。putメソッドに渡す。
 *
 */

package Othello;

public class Point {
	public int x, y;

	public Point(){}

	public Point(int getx, int gety){
		this.x = getx;
		this.y = gety;
	}

	public void printMove(){
		System.out.println("(x,y)=("+(this.x+1)+","+(this.y+1)+")");
	}

	public void setMove(int getx, int gety){
		this.x = getx;
		this.y = gety;
	}
}
