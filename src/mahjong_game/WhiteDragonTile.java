package mahjong_game;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class WhiteDragonTile extends Tile{
	
	public WhiteDragonTile(){
		setToolTipText(this.toString());
	}
	
	public String toString(){
		return "White Dragon";
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(DARK_BLUE);
		
		BasicStroke outlineStroke = new BasicStroke(10);
		g2.setStroke(outlineStroke);
		
		g2.drawRoundRect(26, 8+SHADOW_SIZE, 38, 55, 1, 1);
		g2.setColor(LIGHT_BLUE.brighter());
		outlineStroke = new BasicStroke(2f);
		g2.setStroke(outlineStroke);
		
		g2.drawArc(23, 5+SHADOW_SIZE, 12, 12, 80, 90);
		g2.translate(23, 9+SHADOW_SIZE);
		g2.drawArc(0, 0, 12, 12, 80, 90);
		g2.drawArc(0, 4, 12, 12, 80, 90);
		
		g2.translate(33,46);
		g2.drawArc(0, 0, 12, 12, -100, 90);
		g2.drawArc(0, -4, 12, 12, -100, 90);
		g2.drawArc(0, -8, 12, 12, -100, 90);
		
		g2.translate(-32, 4);
		g2.setColor(LIGHT_BLUE.brighter());
		g2.fillOval(0, 0, 6, 6);
		g2.setColor(LIGHT_BLUE);
		g2.fillOval(0, -8, 5, 5);
		g2.fillOval(0, -16, 4, 4);

		g2.translate(37, -53);
		g2.setColor(LIGHT_BLUE.brighter());
		g2.fillOval(0, 0, 6, 6);
		g2.setColor(LIGHT_BLUE);
		g2.fillOval(1, 8, 5, 5);
		g2.fillOval(1, 16, 4, 4);
	}
	
	
	public static void main(String[] args) {
		Tile t = new WhiteDragonTile();
		JFrame theJFrame = new JFrame();
		theJFrame.setSize(300, 300);
		theJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		theJFrame.setLocation((screenSize.width-theJFrame.getWidth())/2, (screenSize.height-theJFrame.getHeight())/2);
		theJFrame.setLayout(new FlowLayout());
		theJFrame.add(t);
		theJFrame.setVisible(true);

	}

}