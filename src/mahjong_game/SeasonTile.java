package mahjong_game;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class SeasonTile extends PictureTile{
	private static ImageIcon seasonImage;
	private String theName;
	
	public SeasonTile(String name){
		super(name);
		theName = name;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.scale(.88,.88);
		
		switch (theName){
		case "Spring":
			seasonImage = new ImageIcon(SeasonTile.class.getResource("images/Spring.png"));
			seasonImage.paintIcon(this, g2, 20, 19+SHADOW_SIZE);
			break;
		case "Summer":
			seasonImage = new ImageIcon(SeasonTile.class.getResource("images/Summer.png"));
			seasonImage.paintIcon(this, g2, 20, 13+SHADOW_SIZE);
			break;
		case "Winter":
			seasonImage = new ImageIcon(SeasonTile.class.getResource("images/Winter.png"));
			seasonImage.paintIcon(this, g2, 20, 13+SHADOW_SIZE);
			break;
		case "Fall":
			seasonImage = new ImageIcon(SeasonTile.class.getResource("images/Fall.png"));
			seasonImage.paintIcon(this, g2, 20, 13+SHADOW_SIZE);
			break;
		}
	}
	
	public static void main(String[] args)
	{
		JFrame	frame = new JFrame();

		frame.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Season Tiles");

		frame.add(new SeasonTile("Spring"));
		frame.add(new SeasonTile("Summer"));
		frame.add(new SeasonTile("Fall"));
		frame.add(new SeasonTile("Winter"));

		frame.pack();
		frame.setVisible(true);
	}
	
}
