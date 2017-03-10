package mahjong_game;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JPanel;

public  abstract class Tile extends JPanel {
	
	protected boolean isHighlighted = false;
	protected boolean isShadowOn = true;
	
	public static final int SHADOW_SIZE = 6;
	public static final int FACE_LEFT_EDGE_X = 18;
	public static final int FACE_UPPER_EDGE_Y = 0 + SHADOW_SIZE;
	public static final int FACE_BOTTOM_EDGE_Y = FACE_UPPER_EDGE_Y + 72;
	public static final Rectangle FACE_RECTANGLE = new Rectangle(FACE_LEFT_EDGE_X, FACE_UPPER_EDGE_Y, 54, 72);
	public static final Dimension SIZE = new Dimension(72+SHADOW_SIZE,90+SHADOW_SIZE);//will need to be bigger
	public static final Dimension SIZE_SANS_SHADOW = new Dimension(72, 90);
	public static final Color DARKER_TAN = new Color(234,197,169);
	public static final Color LIGHTER_TAN = new Color(227, 221, 212).brighter();
	public static final Color CORNER_ACCENT_TAN = (new Color(252, 213, 182));
//	public static final Color CORNER_ACCENT_TAN = (new Color(247, 208, 179));//also nice
	public static final Color DARKER_GREEN = (new Color(63, 102, 0));
	public static final Color LIGHTER_GREEN = (new Color(63, 169, 0));
	public static final Color CORNER_ACCENT_GREEN = new Color(63,120,0);
//	public static final Color CORNER_ACCENT_GREEN = new Color(63,126,0);//also usable
	
	public static final Color DARK_GREEN = new Color(15, 84, 15);
	public static final Color LIGHT_GREEN = new Color(100, 161, 100);
	public static final Color DARK_BLUE = new Color(20, 30, 122);
	public static final Color LIGHT_BLUE = new Color(126, 126, 228);
	public static final Color DARK_RED = new Color(148, 30, 20);
	public static final Color LIGHT_RED = new Color(241, 114, 113);
	
	private static Polygon[] polygons;
	private static RadialGradientPaint topGradientPaint;
	private static RadialGradientPaint [] sideGradientPaints;
	//private static BasicStroke cornerStroke;
	
	private int region, quadrant, layer, row, cell;
	protected int original_Z;
	protected String firstOrLast;
	protected Rectangle originalBounds;
	protected int neighborStatus = 0;
	protected int tileID = 0;
	protected static Hashtable<Integer, Polygon> hashMap_topShadowClips;
	protected static Hashtable<Integer, Polygon> hashMap_sideShadowClips;
	protected static Hashtable<Character, Point> hashMap_ShadowClipPoints;
	protected boolean numberAdded = false; //used in mouseMoved
	
	public Tile(){
		setPreferredSize(SIZE);
		setOpaque(false);
	}
	
	public void setDataFields(int region, int quadrant, int layer, int row, int cell){
		this.region = region;
		this.quadrant = quadrant;
		this.layer = layer;
		this.row = row;
		this.cell = cell;
	}
	
	public void set_tileID(int ID){
		this.tileID = ID;
	}
	
	public void setNeighborStatus(int neighborStatus){
		this.neighborStatus = neighborStatus;
	}
	
	public int getRegion(){return region;}
	public int getQuadrant(){return quadrant;}
	public int getLayer(){return layer;}
	public int getRow(){return row;}
	public int getCell(){return cell;}
	public String getFirstOrLast(){return firstOrLast;}
	
	public String getDataFields(){
		return "Region: " + region + "\n"
				+ "Quadrant: " + quadrant + "\n"
				+ "Layer: " + layer + "\n"
				+ "Row: " + row + "\n"
				+ "Cell: " + cell + "\n"
				+ "ID: " + tileID + "\n";
	}
	
	public boolean matches (Tile other){
		return (this.getClass() == other.getClass());
	}
	
	static{
		 polygons = new Polygon[4];
		 polygons[0] = new Polygon(new int[]{9,18,18,9},new int[]{9+SHADOW_SIZE,0+SHADOW_SIZE,72+SHADOW_SIZE,81+SHADOW_SIZE},4); // x-array, y-array
		 polygons[1] = new Polygon(new int[]{18,70,61,9},new int[]{72+SHADOW_SIZE,72+SHADOW_SIZE,81+SHADOW_SIZE,81+SHADOW_SIZE},4);
		 polygons[2] = new Polygon(new int[]{0,9,9,0},new int[]{18+SHADOW_SIZE,9+SHADOW_SIZE,81+SHADOW_SIZE,90+SHADOW_SIZE},4);
		 polygons[3] = new Polygon(new int[]{9,61,52,0},new int[]{81+SHADOW_SIZE,81+SHADOW_SIZE,90+SHADOW_SIZE,90+SHADOW_SIZE},4);
		 
		 Point2D topGradient_Center = new Point2D.Float(48,40+SHADOW_SIZE);
		 Point2D topGradient_Focus = new Point2D.Float(38, 50+SHADOW_SIZE);
		 float[] top_dist = {0.2f, 1.0f};
		 Color[] top_colors = {LIGHTER_TAN, DARKER_TAN};
		 topGradientPaint = new RadialGradientPaint(topGradient_Center, 50f, topGradient_Focus, top_dist, top_colors, CycleMethod.NO_CYCLE);
		 
		 Point2D [] sideGradient_CenterPoints = {new Point2D.Float(-8, 50+2+SHADOW_SIZE), new Point2D.Float(40,94+SHADOW_SIZE)};
		 Point2D [] sideGradient_FocusPoints = {new Point2D.Float(-8, 44+SHADOW_SIZE), new Point2D.Float(24,94+SHADOW_SIZE)};
		 float [] radii = {43, 30};
		 float[] dist = {0.0f, 0.9f};
		 Color[] colors_upperSide = {LIGHTER_TAN, DARKER_TAN};
		 Color[] colors_lowerSide = {LIGHTER_GREEN, DARKER_GREEN};
		 
		 sideGradientPaints = new RadialGradientPaint[4];
		 sideGradientPaints[0] = new RadialGradientPaint(sideGradient_CenterPoints[0], radii[0], sideGradient_FocusPoints[0], dist, colors_upperSide, CycleMethod.NO_CYCLE);
		 sideGradientPaints[1] = new RadialGradientPaint(sideGradient_CenterPoints[1], radii[1], sideGradient_FocusPoints[1], dist, colors_upperSide, CycleMethod.NO_CYCLE);
		 sideGradientPaints[2] = new RadialGradientPaint(sideGradient_CenterPoints[0], radii[0], sideGradient_FocusPoints[0], dist, colors_lowerSide, CycleMethod.NO_CYCLE);
		 sideGradientPaints[3] = new RadialGradientPaint(sideGradient_CenterPoints[1], 36, sideGradient_FocusPoints[1], dist, colors_lowerSide, CycleMethod.NO_CYCLE);
		 
		 //cornerStroke = new BasicStroke(2.6F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
		 
		
	}
	
	
	
	

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		Graphics2D	g2 = (Graphics2D) g;
		
		if (!isHighlighted)
			g2.setPaint(topGradientPaint);
		else
			g2.setPaint(Color.GREEN.brighter());
		
		g2.fillRect(FACE_RECTANGLE.x, FACE_RECTANGLE.y, FACE_RECTANGLE.width, FACE_RECTANGLE.height);
		g2.setColor(DARKER_TAN);
		g2.drawRoundRect(FACE_RECTANGLE.x, FACE_RECTANGLE.y, FACE_RECTANGLE.width-1, FACE_RECTANGLE.height-1, 4, 4);
		
		for(int i = 0; i < 4; i++){
			if (i < 2)
				g2.setColor(DARKER_TAN);
			else
				g2.setColor(CORNER_ACCENT_GREEN);
			g2.drawPolygon(polygons[i]);
			
			g2.setPaint(sideGradientPaints[i]);
			g2.fillPolygon(polygons[i]);
		}
		if (isShadowOn)
			paintShadows((Graphics2D)(g.create()));

	}
	
	public void paintShadows(Graphics2D g2){
		g2.setColor(Color.DARK_GRAY);
		
		Composite cOld = g2.getComposite();
		Composite cNew = ((AlphaComposite)cOld).derive(.36f);
		
		g2.setComposite(cNew);
		
		//Shape oldClip = g2.getClip();
		
		if (ShadowPolygons.hashMap_topShadowClips.containsKey(neighborStatus)){
			g2.setClip(ShadowPolygons.hashMap_topShadowClips.get(neighborStatus));
			g2.fillPolygon(ShadowPolygons.topShadowPolygon);
		}
		
		//g2.setClip(oldClip);
		
		if (ShadowPolygons.hashMap_sideShadowClips.containsKey(neighborStatus)){
			g2.setClip(ShadowPolygons.hashMap_sideShadowClips.get(neighborStatus));
			g2.fillPolygon(ShadowPolygons.sideShadowPolygon);
		}
	}


	
	public static void main(String[] args) {

	}
	
	
}

