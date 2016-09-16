package Othello;

public enum Stone {
	/**黒プレイヤー */
	Black(1),
	/**白プレイヤー */
	White(-1);

	public int color;

	private Stone(int n){
		this.color=n;
	}

	public Stone oppositePlayer(){
		if(this.color == 1){
			return Stone.White;
		}else{
			return Stone.Black;
		}
	}

}
