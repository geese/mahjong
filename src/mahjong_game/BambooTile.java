package mahjong_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BambooTile extends RankTile{

	public BambooTile(int rank){
		super(rank);
	}
	
	public String toString(){
		return "Bamboo " + super.rank;
	}
	
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		Graphics2D g2_eight = (Graphics2D) g2.create();
			
			switch (rank){
			case 3:
				drawBambooSticks((Graphics2D)g2.create(), new Point(40, 6+SHADOW_SIZE), .8, 1.0, LIGHT_BLUE, DARK_BLUE);
				drawBambooSticks((Graphics2D)g2.create(), new Point(24, 41+SHADOW_SIZE), .8, 1.0, LIGHT_GREEN, DARK_GREEN);
				drawBambooSticks((Graphics2D)g2.create(), new Point(56, 41+SHADOW_SIZE), .8, 1.0, LIGHT_GREEN, DARK_GREEN);
				break;
			case 6:
			case 2:
				drawBambooSticks((Graphics2D)g2.create(), new Point(40, 6+SHADOW_SIZE), .8, 1.0, rank == 2 ? LIGHT_BLUE : LIGHT_GREEN, rank == 2 ? DARK_BLUE : DARK_GREEN);
				drawBambooSticks((Graphics2D)g2.create(), new Point(40, 41+SHADOW_SIZE), .8, 1.0, rank == 6 ? LIGHT_BLUE : LIGHT_GREEN, rank == 6 ? DARK_BLUE : DARK_GREEN);
			case 5:
				if (rank == 5)
					drawBambooSticks((Graphics2D)g2.create(), new Point(40, 24+SHADOW_SIZE), .8, 1.0, LIGHT_RED, DARK_RED);
			case 4:
				if (rank != 2){
					drawBambooSticks((Graphics2D)g2.create(), new Point(24, 6+SHADOW_SIZE), .8, 1.0, rank == 4 ? LIGHT_BLUE : LIGHT_GREEN, rank == 4 ? DARK_BLUE : DARK_GREEN);
					drawBambooSticks((Graphics2D)g2.create(), new Point(56, 6+SHADOW_SIZE), .8, 1.0, rank == 5 ? LIGHT_BLUE : LIGHT_GREEN, rank == 5 ? DARK_BLUE : DARK_GREEN);
					drawBambooSticks((Graphics2D)g2.create(), new Point(24, 41+SHADOW_SIZE), .8, 1.0, rank == 4 ? LIGHT_GREEN : LIGHT_BLUE, rank == 4 ? DARK_GREEN : DARK_BLUE);
					drawBambooSticks((Graphics2D)g2.create(), new Point(56, 41+SHADOW_SIZE), .8, 1.0, rank == 5 ? LIGHT_GREEN : LIGHT_BLUE, rank == 5 ? DARK_GREEN : DARK_BLUE);
				}
				break;
			case 7:
			case 9:
				drawBambooSticks((Graphics2D)g2.create(), new Point(42, 4+SHADOW_SIZE), .65, .8, rank == 7 ? LIGHT_RED : LIGHT_BLUE, rank == 7 ? DARK_RED : DARK_BLUE);
				drawBambooSticks((Graphics2D)g2.create(), new Point(42, 26+SHADOW_SIZE), .65, .8, LIGHT_BLUE, DARK_BLUE);
				drawBambooSticks((Graphics2D)g2.create(), new Point(42, 48+SHADOW_SIZE), .65, .8, LIGHT_BLUE, DARK_BLUE);
				drawBambooSticks((Graphics2D)g2.create(), new Point(26, 26+SHADOW_SIZE), .65, .8, rank == 9 ? LIGHT_RED : LIGHT_GREEN, rank == 9 ? DARK_RED : DARK_GREEN);
				drawBambooSticks((Graphics2D)g2.create(), new Point(58, 26+SHADOW_SIZE), .65, .8, LIGHT_GREEN, DARK_GREEN);
			case 8:
				if (rank != 7){
					drawBambooSticks((Graphics2D)g2.create(), new Point(26, 4+SHADOW_SIZE), .65, .8, rank == 9 ? LIGHT_RED : LIGHT_GREEN, rank == 9 ? DARK_RED : DARK_GREEN);
					drawBambooSticks((Graphics2D)g2.create(), new Point(58, 4+SHADOW_SIZE), .65, .8, LIGHT_GREEN, DARK_GREEN);
				}
				Color light = null, dark = null;
				switch (rank){
					case 7: light = LIGHT_GREEN;  dark = DARK_GREEN; break;
					case 8: light = LIGHT_BLUE; dark = DARK_BLUE; break;
					case 9: light = LIGHT_RED; dark = DARK_RED; break;					
				}
				drawBambooSticks((Graphics2D)g2.create(), new Point(26, 48+SHADOW_SIZE), .65, .8, light, dark);
				drawBambooSticks((Graphics2D)g2.create(), new Point(58, 48+SHADOW_SIZE), .65, .8, rank == 8 ? LIGHT_BLUE : LIGHT_GREEN, rank == 8 ? DARK_BLUE : DARK_GREEN);
				
				ArrayList<Point> originList_Eight = new ArrayList<>();
				ArrayList<Double> rotateTheta_List = new ArrayList<>();
				originList_Eight.add(new Point(41,16+SHADOW_SIZE));
				rotateTheta_List.add(.8);
				originList_Eight.add(new Point(45,22+SHADOW_SIZE));
				rotateTheta_List.add(-.8);
				originList_Eight.add(new Point(27,42+SHADOW_SIZE));
				rotateTheta_List.add(-.8);
				originList_Eight.add(new Point(59,37+SHADOW_SIZE));
				rotateTheta_List.add(.8);
				if (rank == 8){
				for (int i = 0; i < originList_Eight.size(); i++){
					Graphics2D g2_eightCopy = (Graphics2D) g2_eight.create();
					drawEight(g2_eightCopy, originList_Eight.get(i), rotateTheta_List.get(i), .65, .8, i < 2 ? LIGHT_GREEN : LIGHT_BLUE, i < 2? DARK_GREEN : DARK_BLUE);
				}
				}
				break;
			}
		}
		

	private void drawBambooSticks(Graphics2D g2, Point origin, Double scaleX, Double scaleY, Color light_color, Color dark_color) {
		g2.setColor(dark_color);
		g2.translate(origin.x, origin.y);
		g2.scale(scaleX, scaleY);
		g2.fillOval(0, 0, 12, 5);
		g2.fillOval(0, 10, 12, 5);
		g2.fillOval(0, 20, 12, 5);
		g2.fillRoundRect(3, 2,6, 18, 3, 3);
		g2.setColor(light_color.brighter());
		g2.scale(scaleX * 1.375, 1);
		g2.fillRoundRect(5, 2,1, 21, 3, 3);
		g2.fillOval(3, 12, 6, 1);
		g2.dispose();
	}
	
	private void drawEight(Graphics2D g2, Point origin, double radians, double scaleX, double scaleY, Color light_color, Color dark_color){
		g2.setColor(dark_color);
		g2.translate(origin.x, origin.y);
		g2.rotate(radians);
		g2.scale(scaleX, scaleY);
		g2.fillOval(0, 0, 12, 5);
		g2.fillOval(0, 10, 12, 5);
		g2.fillOval(0, 20, 12, 5);
		g2.fillRoundRect(3, 2,6, 18, 3, 3);
		g2.setColor(light_color.brighter());
		g2.scale(scaleX * 1.375, 1);
		g2.fillRoundRect(5, 2,1, 21, 3, 3);
		g2.fillOval(3, 12, 6, 1);
		g2.dispose();
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		JFrame	frame = new JFrame();

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(800, 100));
		panel.setLayout(new FlowLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Bamboo Tiles");

		frame.add(panel);
		panel.add(new Bamboo1Tile());
		panel.add(new BambooTile(2));
		panel.add(new BambooTile(3));
		panel.add(new BambooTile(4));
		panel.add(new BambooTile(5));
		panel.add(new BambooTile(6));
		panel.add(new BambooTile(7));
		panel.add(new BambooTile(8));
		panel.add(new BambooTile(9));

		
		
		frame.pack();
		frame.setVisible(true);
	}
	
	
	
}
