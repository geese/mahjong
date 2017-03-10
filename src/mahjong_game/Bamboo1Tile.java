package mahjong_game;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Bamboo1Tile extends PictureTile{

	private ImageIcon sparrow;
	
	public Bamboo1Tile() {
		super("Sparrow");
		sparrow = new ImageIcon(Bamboo1Tile.class.getResource("images/Sparrow.png"));
	}

	public String toString(){
		return "Bamboo 1";
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(.8,.8);
		sparrow.paintIcon(this, g2, 26, 16+SHADOW_SIZE);
	}
	
	public static void main(String[] args)
	{
		JFrame	frame = new JFrame();

		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Bamboo 1 Tile");

		frame.add(new Bamboo1Tile());

		frame.pack();
		frame.setVisible(true);
	}
	
}
