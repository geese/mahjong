package mahjong_game;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.net.URL;
import java.util.ArrayDeque;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class FlowerTile extends PictureTile{
	
	private static ImageIcon flowerImage;
	private String theName;
	
	public FlowerTile(String name){
		super(name);
		theName = name;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.scale(.79, .82);
		
		switch (theName){
		case "Chrysanthemum":
			//URL chrys = FlowerTile.class.getResource("images/Chrysanthemum.png");
			flowerImage = new ImageIcon(FlowerTile.class.getResource("images/Chrysanthemum.png"));
			flowerImage.paintIcon(this, g2, 27, 18+SHADOW_SIZE);
			break;
		case "Plum":
			flowerImage = new ImageIcon(FlowerTile.class.getResource("images/Plum.png"));
			flowerImage.paintIcon(this, g2, 30, 13+SHADOW_SIZE);
			break;
		case "Orchid":
			flowerImage = new ImageIcon(FlowerTile.class.getResource("images/Orchid.png"));;
			flowerImage.paintIcon(this, g2, 27, 13+SHADOW_SIZE);
			break;
		case "Bamboo":
			flowerImage = new ImageIcon(FlowerTile.class.getResource("images/Bamboo.png"));
			flowerImage.paintIcon(this, g2, 31, 13+SHADOW_SIZE);
			break;
		}
	}
	
	public static void main(String[] args)
	{
		JFrame	frame = new JFrame();

		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Flower Tiles");
		
		ArrayDeque<Tile> dq = new ArrayDeque<>(10);
		
		

		dq.add(new FlowerTile("Chrysanthemum"));
		dq.add(new FlowerTile("Orchid"));
		dq.add(new FlowerTile("Plum"));
		dq.add(new FlowerTile("Bamboo"));
		
		dq.addFirst(new CircleTile(1));
		dq.addLast(new BambooTile(8));
		
		while(!dq.isEmpty())
		frame.add(dq.pollFirst());

		frame.pack();
		frame.setVisible(true);
	}
	
}
