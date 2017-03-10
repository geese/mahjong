package mahjong_game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.JFrame;

public class CircleTile extends RankTile {

	private static GeneralPath path1 = new GeneralPath();
    private static GeneralPath path2 = new GeneralPath();
    private static GeneralPath path3 = new GeneralPath();
    private static GeneralPath path4 = new GeneralPath();
	
	public CircleTile(int rank){
		super(rank);
	}

	static{
		path1 = new GeneralPath();
        path2 = new GeneralPath();
        path3 = new GeneralPath();
        path4 = new GeneralPath();
	}


	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		switch(rank){
		case 1:
			draw_outerCircle((Graphics2D)g2.create());
			draw_innerCircle((Graphics2D)g2.create(), new Point(27+18, 36+SHADOW_SIZE), 26, 1.5f, LIGHT_RED, DARK_RED);
			break;
		case 2:
			draw_innerCircle((Graphics2D)g2.create(), new Point(27+18, 19+SHADOW_SIZE), 28, 1.5f, LIGHT_GREEN, DARK_GREEN);
			draw_innerCircle((Graphics2D)g2.create(), new Point(27+18, 53+SHADOW_SIZE), 28, 1.5f, LIGHT_BLUE, DARK_BLUE);
			break;
		case 3:
			draw_innerCircle((Graphics2D)g2.create(), new Point(40+18, 58+SHADOW_SIZE), 23, 1.3f, LIGHT_GREEN, DARK_GREEN);
		case 5:
			draw_innerCircle((Graphics2D)g2.create(), new Point(14+18, 14+SHADOW_SIZE), 23, 1.3f, LIGHT_BLUE, DARK_BLUE);
			draw_innerCircle((Graphics2D)g2.create(), new Point(27+18, 36+SHADOW_SIZE), 23, 1.3f, LIGHT_RED, DARK_RED);
			if (rank == 5){
				draw_innerCircle((Graphics2D)g2.create(), new Point(40+18, 14+SHADOW_SIZE), 23, 1.3f, LIGHT_GREEN, DARK_GREEN);
				draw_innerCircle((Graphics2D)g2.create(), new Point(14+18, 58+SHADOW_SIZE), 23, 1.3f, LIGHT_GREEN, DARK_GREEN);
				draw_innerCircle((Graphics2D)g2.create(), new Point(40+18, 58+SHADOW_SIZE), 23, 1.3f, LIGHT_BLUE, DARK_BLUE);
			}
			break;
		case 4:
			draw_innerCircle((Graphics2D)g2.create(), new Point(13+18, 18+SHADOW_SIZE), 20, 1.2f, LIGHT_BLUE, DARK_BLUE);
			draw_innerCircle((Graphics2D)g2.create(), new Point(41+18, 18+SHADOW_SIZE), 20, 1.2f, LIGHT_GREEN, DARK_GREEN);
			draw_innerCircle((Graphics2D)g2.create(), new Point(13+18, 54+SHADOW_SIZE), 20, 1.2f, LIGHT_GREEN, DARK_GREEN);
			draw_innerCircle((Graphics2D)g2.create(), new Point(41+18, 54+SHADOW_SIZE), 20, 1.2f, LIGHT_BLUE, DARK_BLUE);
			break;
		case 6:
			draw_innerCircle((Graphics2D)g2.create(), new Point(13+19, 13+SHADOW_SIZE), 22, 1.2f, LIGHT_GREEN, DARK_GREEN);
			draw_innerCircle((Graphics2D)g2.create(), new Point(13+19, 36+SHADOW_SIZE), 22, 1.2f, LIGHT_RED, DARK_RED);
			draw_innerCircle((Graphics2D)g2.create(), new Point(13+19, 59+SHADOW_SIZE), 22, 1.2f, LIGHT_RED, DARK_RED);
			draw_innerCircle((Graphics2D)g2.create(), new Point(41+17, 13+SHADOW_SIZE), 22, 1.2f, LIGHT_GREEN, DARK_GREEN);
			draw_innerCircle((Graphics2D)g2.create(), new Point(41+17, 36+SHADOW_SIZE), 22, 1.2f, LIGHT_RED, DARK_RED);
			draw_innerCircle((Graphics2D)g2.create(), new Point(41+17, 59+SHADOW_SIZE), 22, 1.2f, LIGHT_RED, DARK_RED);
			break;
		case 7:
			if (rank == 7){
				draw_innerCircle((Graphics2D)g2.create(), new Point(9+20, 10+SHADOW_SIZE), 16, 1f, LIGHT_GREEN, DARK_GREEN);
				draw_innerCircle((Graphics2D)g2.create(), new Point(27+18, 18+SHADOW_SIZE), 16, 1f, LIGHT_GREEN, DARK_GREEN);
				draw_innerCircle((Graphics2D)g2.create(), new Point(45+16, 26+SHADOW_SIZE), 16, 1f, LIGHT_GREEN, DARK_GREEN);
			}
		case 8:
			if (rank == 8){
				draw_innerCircle((Graphics2D)g2.create(), new Point(13+18, 10+SHADOW_SIZE), 16, 1f, LIGHT_BLUE, DARK_BLUE);
				draw_innerCircle((Graphics2D)g2.create(), new Point(13+18, 27+SHADOW_SIZE), 16, 1f, LIGHT_BLUE, DARK_BLUE);
				draw_innerCircle((Graphics2D)g2.create(), new Point(41+18, 10+SHADOW_SIZE), 16, 1f, LIGHT_BLUE, DARK_BLUE);
				draw_innerCircle((Graphics2D)g2.create(), new Point(41+18, 27+SHADOW_SIZE), 16, 1f, LIGHT_BLUE, DARK_BLUE);
			}
			draw_innerCircle((Graphics2D)g2.create(), new Point(13+18, 45+SHADOW_SIZE), 16, 1f, rank == 7 ? LIGHT_RED : LIGHT_BLUE, rank == 7 ? DARK_RED : DARK_BLUE);
			draw_innerCircle((Graphics2D)g2.create(), new Point(13+18, 62+SHADOW_SIZE), 16, 1f, rank == 7 ? LIGHT_RED : LIGHT_BLUE, rank == 7 ? DARK_RED : DARK_BLUE);
			draw_innerCircle((Graphics2D)g2.create(), new Point(41+18, 45+SHADOW_SIZE), 16, 1f, rank == 7 ? LIGHT_RED : LIGHT_BLUE, rank == 7 ? DARK_RED : DARK_BLUE);
			draw_innerCircle((Graphics2D)g2.create(), new Point(41+18, 62+SHADOW_SIZE), 16, 1f, rank == 7 ? LIGHT_RED : LIGHT_BLUE, rank == 7 ? DARK_RED : DARK_BLUE);
			break;
		case 9:
			draw_innerCircle((Graphics2D)g2.create(), new Point(9+19, 12+SHADOW_SIZE), 16, 1f, LIGHT_GREEN, DARK_GREEN);
			draw_innerCircle((Graphics2D)g2.create(), new Point(27+18, 12+SHADOW_SIZE), 16, 1f, LIGHT_GREEN, DARK_GREEN);
			draw_innerCircle((Graphics2D)g2.create(), new Point(45+17, 12+SHADOW_SIZE), 16, 1f, LIGHT_GREEN, DARK_GREEN);
			draw_innerCircle((Graphics2D)g2.create(), new Point(9+19, 36+SHADOW_SIZE), 16, 1f, LIGHT_RED, DARK_RED);
			draw_innerCircle((Graphics2D)g2.create(), new Point(27+18, 36+SHADOW_SIZE), 16, 1f, LIGHT_RED, DARK_RED);
			draw_innerCircle((Graphics2D)g2.create(), new Point(45+17, 36+SHADOW_SIZE), 16, 1f, LIGHT_RED, DARK_RED);
			draw_innerCircle((Graphics2D)g2.create(), new Point(9+19, 60+SHADOW_SIZE), 16, 1f, LIGHT_BLUE, DARK_BLUE);
			draw_innerCircle((Graphics2D)g2.create(), new Point(27+18, 60+SHADOW_SIZE), 16, 1f, LIGHT_BLUE, DARK_BLUE);
			draw_innerCircle((Graphics2D)g2.create(), new Point(45+17, 60+SHADOW_SIZE), 16, 1f, LIGHT_BLUE, DARK_BLUE);
		}
	}
	
	
	
	
	private void draw_innerCircle(Graphics2D g2, Point center, int diameter, float stroke_width, Color color_light, Color color_dark){
		int amp = (int)(5*diameter)/13;
		double deltaTheta = Math.PI / 180 ;
        int w = center.x;
        int h = center.y;
        
        path1.reset();
        path1.moveTo(w+amp, h );
        
        for (double theta = 0; theta < Math.PI; theta += deltaTheta) {  // the polar graph called a "rose"...yay Calc 2!
            double x = amp * Math.cos(3 * theta) * Math.cos(theta) + w;
            double y = amp * Math.cos(3 * theta) * Math.sin(theta) + h;
            path1.lineTo(x, y);
        }
        
        path2.reset();
        path2.moveTo(w-amp, h );
        
        for (double theta = 0; theta <  Math.PI; theta += deltaTheta) {
            double x = amp * Math.cos(3 * theta - Math.PI) * Math.cos(theta) + w;
            double y = amp * Math.cos(3 * theta - Math.PI) * Math.sin(theta) + h;
            path2.lineTo(x, y);
        }
        
        BasicStroke outlineStroke = new BasicStroke(stroke_width);
		g2.setStroke(outlineStroke);
		
		g2.setColor(color_dark);
		g2.fillOval(w-(diameter/2), h-(diameter/2), diameter, diameter);
		
		g2.setColor(color_light);
		g2.draw(path1);
        g2.draw(path2);
       
        Graphics2D g2copy = (Graphics2D)g2.create();
        Graphics2D g2copy1 = (Graphics2D)g2.create();
        
        g2copy.translate(w-(.78*3*amp/2),h-(.78*3*amp/2));
        g2copy.scale(.78, .78);
        g2copy.drawOval(0, 0, 3 * amp, 3 * amp);
		
        g2.setColor(color_dark);
        g2.fillOval(w-(9*amp)/20, h-(9*amp)/20, (9*amp)/10, (9*amp)/10);
        
        
        g2copy1.setColor(color_light);
        g2copy1.translate(w-(.65*((9*amp)/20)),h-(.65*((9*amp)/20)));
        g2copy1.scale(.65, .65);
        g2copy1.drawOval(0, 0, (9*amp)/10, (9*amp)/10);
        
        g2.dispose();
		
	}
	
	 public void draw_outerCircle(Graphics2D g2) {
		 int amp = 18;
		 
			double deltaTheta = Math.PI / 180 / 100 ;
	        int w = 45; //center
	        int h = 36+SHADOW_SIZE;
	        
	        path3.reset();
	        path3.moveTo(w+amp, h );
	        
	        for (double theta = 0; theta < Math.PI; theta += deltaTheta) {
	            double x = amp * Math.cos(7 * theta) * Math.cos(theta) + w;
	            double y = amp * Math.cos(7 * theta) * Math.sin(theta) + h;
	            path3.lineTo(x, y);
	        }
	        
	        path4.reset();
	        path4.moveTo(w-amp, h );
	        
	        for (double theta = 0; theta <  Math.PI; theta += deltaTheta) {
	            double x = amp * Math.cos(7 * theta - Math.PI) * Math.cos(theta) + w;
	            double y = amp * Math.cos(7 * theta - Math.PI) * Math.sin(theta) + h;
	            path4.lineTo(x, y);
	        }
	        
	        BasicStroke outlineStroke = new BasicStroke(1.5f);
			g2.setStroke(outlineStroke);
			
			g2.setColor(DARK_GREEN);
			g2.fillOval(w-25,h-25, 50, 50);
			
			g2.setColor(LIGHT_GREEN);
			g2.draw(path3);
			g2.draw(path4);
			
			outlineStroke = new BasicStroke(3f);
			g2.setStroke(outlineStroke);
			
			g2.translate(w-.9*25, h-.9*25);
			g2.scale(.9, .9);
			g2.drawOval(0, 0, 50, 50);
			
			g2.dispose();
		}
	
	 public String toString(){
		 return "Circle " + rank;
	 }
	

    public static void main(String[] args) {
    	JFrame	frame = new JFrame();
    	GridLayout gl = new GridLayout(1,8);
    	
    	gl.setHgap(-18);
    	//frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    	
    
    	frame.setLayout(gl);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Circle Tiles");
		
		
		


		frame.add(new CircleTile(1));
		frame.add(new CircleTile(2));
		frame.add(new CircleTile(3));
		frame.add(new CircleTile(4));
		frame.add(new CircleTile(5));
		frame.add(new CircleTile(6));
		frame.add(new CircleTile(7));
		frame.add(new CircleTile(8));
		frame.add(new CircleTile(9));

		frame.pack();
		frame.setVisible(true);
            }
    }
