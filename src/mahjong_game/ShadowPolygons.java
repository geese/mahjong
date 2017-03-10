package mahjong_game;

import java.awt.Point;
import java.awt.Polygon;
import java.util.Hashtable;

public class ShadowPolygons {
	protected static Hashtable<Integer, Polygon> hashMap_topShadowClips;
	protected static Hashtable<Integer, Polygon> hashMap_sideShadowClips;
	protected static Hashtable<Character, Point> hashMap_ShadowClipPoints;
	protected static Polygon topShadowPolygon;
	protected static Polygon sideShadowPolygon;

	public ShadowPolygons() {
		
	}

	static{

		 Point a = new Point(Tile.FACE_LEFT_EDGE_X, Tile.FACE_UPPER_EDGE_Y);
		 Point c = new Point(Tile.SIZE.width, a.y - Tile.SHADOW_SIZE);
		 Point b = new Point(c.x - Tile.SHADOW_SIZE, a.y);
		 Point d = new Point(a.x + Tile.SHADOW_SIZE, c.y);
		 Point e = new Point(b.x - 18, a.y);
		 Point f = new Point(e.x, c.y);
		 Point g = new Point(d.x + 18, a.y);
		 Point h = new Point(g.x, c.y);
		 Point i = new Point(d.x + 36, a.y);
		 Point j = new Point(i.x, c.y);
		 Point A = new Point(b.x, a.y);
		 Point B = new Point(A.x, Tile.FACE_BOTTOM_EDGE_Y);
		 Point C = new Point(c.x, B.y - Tile.SHADOW_SIZE);
		 Point D = new Point(c.x, c.y);
		 Point E = new Point(b.x, a.y + 18);
		 Point F = new Point(c.x, E.y);
		 Point G = new Point(b.x, B.y - Tile.FACE_RECTANGLE.height/2);
		 Point H = new Point(c.x, G.y - Tile.SHADOW_SIZE);
		 Point I = new Point(b.x, B.y - 18);
		 Point J = new Point(c.x, I.y);
		 Point K = new Point(b.x, C.y - 18);
		 Point L = new Point(c.x, K.y);
		 Point M = new Point(c.x, C.y - 36);
		// Point N = new Point(b.x, C.y - 54);
		 
		 topShadowPolygon = new Polygon(new int[]{a.x, b.x, c.x, d.x}, new int[]{a.y, b.y, c.y, d.y}, 4);
		 sideShadowPolygon = new Polygon(new int[]{A.x, B.x, C.x, D.x}, new int[]{A.y, B.y, C.y, D.y}, 4);
		 
		 hashMap_topShadowClips = new Hashtable<Integer, Polygon>();
		 hashMap_topShadowClips.put(0, new Polygon(new int[]{a.x, b.x, c.x, d.x}, new int[]{a.y, b.y, c.y, d.y}, 4));
		 hashMap_topShadowClips.put(2, hashMap_topShadowClips.get(0));
		 hashMap_topShadowClips.put(4, new Polygon(new int[]{a.x, e.x, f.x, d.x}, new int[]{a.y, e.y, f.y, d.y}, 4));
		 hashMap_topShadowClips.put(6, hashMap_topShadowClips.get(4));
		 hashMap_topShadowClips.put(8, new Polygon(new int[]{g.x, h.x, c.x, b.x}, new int[]{g.y, h.y, c.y, b.y}, 4));
		 hashMap_topShadowClips.put(10, hashMap_topShadowClips.get(8));
		 hashMap_topShadowClips.put(12, new Polygon(new int[]{g.x, e.x, f.x, h.x}, new int[]{g.y, e.y, f.y, h.y}, 4));
		 hashMap_topShadowClips.put(14, hashMap_topShadowClips.get(12));
		 hashMap_topShadowClips.put(24, new Polygon(new int[]{i.x, b.x, c.x, j.x}, new int[]{i.y, b.y, c.y, j.y}, 4));
		 hashMap_topShadowClips.put(26, hashMap_topShadowClips.get(24));
		 hashMap_topShadowClips.put(28, new Polygon(new int[]{i.x, e.x, f.x, j.x}, new int[]{i.y, e.y, f.y, j.y}, 4));
		 hashMap_topShadowClips.put(30, hashMap_topShadowClips.get(28));
		 hashMap_topShadowClips.put(32, hashMap_topShadowClips.get(0));
		 hashMap_topShadowClips.put(34, hashMap_topShadowClips.get(0));
		 hashMap_topShadowClips.put(36, hashMap_topShadowClips.get(4));
		 hashMap_topShadowClips.put(38, hashMap_topShadowClips.get(4));
		 hashMap_topShadowClips.put(40, hashMap_topShadowClips.get(8));
		 hashMap_topShadowClips.put(42, hashMap_topShadowClips.get(8));
		 hashMap_topShadowClips.put(44, new Polygon(new int[]{g.x, h.x, f.x, e.x}, new int[]{g.y, h.y, f.y, e.y}, 4));
		 hashMap_topShadowClips.put(46, hashMap_topShadowClips.get(44));
		 hashMap_topShadowClips.put(56, new Polygon(new int[]{i.x, j.x, c.x, b.x}, new int[]{i.y, j.y, c.y, b.y}, 4));
		 hashMap_topShadowClips.put(58, hashMap_topShadowClips.get(56));
		 hashMap_topShadowClips.put(60, new Polygon(new int[]{i.x, j.x, f.x, e.x}, new int[]{i.y, j.y, f.y, e.y}, 4));
		 hashMap_topShadowClips.put(62, hashMap_topShadowClips.get(60));
		 hashMap_topShadowClips.put(96, hashMap_topShadowClips.get(0));
		 hashMap_topShadowClips.put(98, hashMap_topShadowClips.get(0));
		 hashMap_topShadowClips.put(100, hashMap_topShadowClips.get(4));
		 hashMap_topShadowClips.put(102, hashMap_topShadowClips.get(4));
		 hashMap_topShadowClips.put(104, hashMap_topShadowClips.get(8));
		 hashMap_topShadowClips.put(106, hashMap_topShadowClips.get(8));
		 hashMap_topShadowClips.put(108, hashMap_topShadowClips.get(12));
		 hashMap_topShadowClips.put(110, hashMap_topShadowClips.get(12));
		 hashMap_topShadowClips.put(120, hashMap_topShadowClips.get(56));
		 hashMap_topShadowClips.put(122, hashMap_topShadowClips.get(56));
		 hashMap_topShadowClips.put(124, hashMap_topShadowClips.get(60));
		 hashMap_topShadowClips.put(126, hashMap_topShadowClips.get(60));
		 hashMap_topShadowClips.put(128, hashMap_topShadowClips.get(0));
		 
		
				 
		 hashMap_sideShadowClips = new Hashtable<Integer, Polygon>();
		 hashMap_sideShadowClips.put(0, new Polygon(new int[]{A.x, B.x, C.x, D.x}, new int[]{A.y, B.y, C.y, D.y}, 4));
		 hashMap_sideShadowClips.put(1, hashMap_sideShadowClips.get(0));
		 hashMap_sideShadowClips.put(4, new Polygon(new int[]{E.x, B.x, C.x, F.x}, new int[]{E.y, B.y, C.y, F.y}, 4));
		 hashMap_sideShadowClips.put(5, hashMap_sideShadowClips.get(4));
		 hashMap_sideShadowClips.put(8, hashMap_sideShadowClips.get(0));
		 hashMap_sideShadowClips.put(9, hashMap_sideShadowClips.get(0));
		 hashMap_sideShadowClips.put(12, hashMap_sideShadowClips.get(4));
		 hashMap_sideShadowClips.put(13, hashMap_sideShadowClips.get(4));
		 hashMap_sideShadowClips.put(24, hashMap_sideShadowClips.get(0));
		 hashMap_sideShadowClips.put(25, hashMap_sideShadowClips.get(0));
		 hashMap_sideShadowClips.put(28, hashMap_sideShadowClips.get(4));
		 hashMap_sideShadowClips.put(29, hashMap_sideShadowClips.get(4));
		 hashMap_sideShadowClips.put(32, new Polygon(new int[]{A.x, K.x, L.x, D.x}, new int[]{A.y, K.y, L.y, D.y}, 4));
		 hashMap_sideShadowClips.put(33, hashMap_sideShadowClips.get(32));
		 hashMap_sideShadowClips.put(36, new Polygon(new int[]{E.x, K.x, L.x, F.x}, new int[]{E.y, K.y, L.y, F.y}, 4));
		 hashMap_sideShadowClips.put(37, hashMap_sideShadowClips.get(36));
		 hashMap_sideShadowClips.put(40, hashMap_sideShadowClips.get(32));
		 hashMap_sideShadowClips.put(41, hashMap_sideShadowClips.get(32));
		 hashMap_sideShadowClips.put(44, hashMap_sideShadowClips.get(36));
		 hashMap_sideShadowClips.put(45, hashMap_sideShadowClips.get(36));
		 hashMap_sideShadowClips.put(56, hashMap_sideShadowClips.get(32));
		 hashMap_sideShadowClips.put(57, hashMap_sideShadowClips.get(32));
		 hashMap_sideShadowClips.put(60, hashMap_sideShadowClips.get(36));
		 hashMap_sideShadowClips.put(61, hashMap_sideShadowClips.get(36));
		 hashMap_sideShadowClips.put(96, new Polygon(new int[]{A.x, G.x, M.x, D.x}, new int[]{A.y, G.y, M.y, D.y}, 4));
		 hashMap_sideShadowClips.put(97, hashMap_sideShadowClips.get(96));
		 hashMap_sideShadowClips.put(100, new Polygon(new int[]{E.x, G.x, M.x, F.x}, new int[]{E.y, G.y, M.y, F.y}, 4));
		 hashMap_sideShadowClips.put(101, hashMap_sideShadowClips.get(100));
		 hashMap_sideShadowClips.put(104, hashMap_sideShadowClips.get(96));
		 hashMap_sideShadowClips.put(105, hashMap_sideShadowClips.get(96));
		 hashMap_sideShadowClips.put(108, hashMap_sideShadowClips.get(100));
		 hashMap_sideShadowClips.put(109, hashMap_sideShadowClips.get(100));
		 hashMap_sideShadowClips.put(120, hashMap_sideShadowClips.get(96));
		 hashMap_sideShadowClips.put(121, hashMap_sideShadowClips.get(96));
		 hashMap_sideShadowClips.put(124, hashMap_sideShadowClips.get(100));
		 hashMap_sideShadowClips.put(125, hashMap_sideShadowClips.get(100));
		 hashMap_sideShadowClips.put(128, new Polygon(new int[]{A.x, G.x, H.x, D.x}, new int[]{A.y, G.y, H.y, D.y}, 4));
		 hashMap_sideShadowClips.put(257, new Polygon(new int[]{I.x, B.x, C.x, J.x}, new int[]{I.y, B.y, C.y, J.y}, 4));
	}
	
	
}
