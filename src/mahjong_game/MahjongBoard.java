package mahjong_game;
//20926  won this one
//61233
// 30159 
//  5555 testing special very top one

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Predicate;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


//81333
public class MahjongBoard extends JPanel implements MouseListener, MouseMotionListener  {
	
	MahjongBoard thisBoard = this;
	Tile currentSelectedTile;
	Tile previousSelectedTile;
	TileRow currentSelectedTileRow;
	TileRow previousSelectedTileRow;
	
	Undo_MouseListener undo_ML = new Undo_MouseListener();
	MouseListener board_asListener = this;
	
	ImageIcon dragon;
	DClock theTimer;
	PlayClip clip = new PlayClip("/audio/stone-scraping.wav", true);
	Fireworks	fw = new Fireworks(this);
	Robot robby;
	
	final static Dimension boardSize = new Dimension(((15*54)+(Tile.SHADOW_SIZE*9)), ((8*72)+(Tile.SHADOW_SIZE * 5)+Tile.SHADOW_SIZE));
	
	static Hashtable<String, Integer> hashMap_neighborStatus;
	Hashtable<Integer, TileModel> hashMap_tileID = new Hashtable<Integer, TileModel>();
	
	ArrayList<Tile> redoTiles = new ArrayList<Tile>();
	ArrayList<TileRegion> regions;
	ArrayList<TileModel> tileModel_Collection;
	ArrayList<TileLayer> regionOne;
	ArrayList<TileLayer> regionZero;
	ArrayList<JLabel> dummies;
	ArrayList<Long> gameNums_ToFile = new ArrayList<>();
	ArrayList<Long> gameNums_FromFile = new ArrayList<>();
	static ArrayList<ArrayList<String>> neighborStrings_List = new ArrayList<>();
	
	boolean tournMode = false;
	boolean undoLastTwo = false;
	boolean is_RedoEnabled = false;
	boolean is_SoundOn = true;
	
	final static int REGION_DATA = 0;
	final static int QUADRANT_DATA = 1;
	final static int LAYER_DATA = 2;
	final static int ROW_DATA = 3;
	final static int CELL_DATA = 4;
	final static int center_X = boardSize.width/2 - Tile.SHADOW_SIZE * 6;
	final static int center_Y = (boardSize.height)/2 - Tile.SHADOW_SIZE * 2;
	static int numTiles;
	
	long theSeed;
	long theFirstSeed;
	Long[] gameNumsArray;
	
	JMenuBar menuBar = new JMenuBar();
	
	JMenu gameMenu = new JMenu("Game");		
	JMenu moveMenu = new JMenu("Move");
	JMenu undoMenu = new JMenu("Undo Multiple");
	JMenu soundMenu = new JMenu("Sound");
	JMenu helpMenu = new JMenu("Help");
	JMenu gameOperation = new JMenu("Game Operation");
	JMenu gameRules = new JMenu("Game Rules");
	JMenu timerMenu = new JMenu("");
	JMenu numTilesRemoved = new JMenu();
	
	JMenuItem playNew = new JMenuItem("Play New");
	JMenuItem numbered = new JMenuItem("Play Numbered");	
	JMenuItem restart = new JMenuItem("Restart");
	JMenuItem tournament = new JMenuItem("Tournament");
	JMenuItem exit = new JMenuItem("Exit");
	JMenuItem redoLast = new JMenuItem("Redo Last");
	JMenuItem redoAll = new JMenuItem("Redo All");
	JMenuItem undoLast = new JMenuItem("Undo Last");
	JMenuItem soundOnOff = new JMenuItem("Off");
	
	JScrollPane scrollPane = new JScrollPane();
	JScrollPane gameOpsScrollPane = new JScrollPane();
	JScrollPane gameRulesScrollPane = new JScrollPane();
	
	JTextPane gameOps = new JTextPane();
	JTextPane gamePlayRules = new JTextPane();
	
	JPanel undoMenu_twoColumnContainerPanel = new JPanel();
	JPanel undoMenu_column_1 = new JPanel();		
	JPanel undoMenu_column_2 = new JPanel();
	
	
	
	public MahjongBoard() throws AWTException {
		setPreferredSize(boardSize);
		setLayout(null);
		
		tileModel_Collection = new ArrayList<TileModel>();
		dummies = new ArrayList<JLabel>();  //these are component-Z-order placeholders!
		dragon = new ImageIcon(MahjongBoard.class.getResource("/images/dragon_green_cropped.png"));
		robby = new Robot();  //does some mouse clicks for me.
		
		theFirstSeed = System.currentTimeMillis();
				
		read_GameNums_FromFile();
		if (!gameNums_ToFile.contains(theFirstSeed % 100000))
			gameNums_ToFile.add(theFirstSeed % 100000);
		
		initBoard(theFirstSeed % 100000);		
	}


	protected void initBoard(long seed) {
		//setting Background color in this location, so that it can be reset after going black for Fireworks
		setBackground(new Color(87, 8, 11));  //dark red
		numTiles = 144;  //important for indexing!
		
		populate_TileModelCollection();
		Collections.shuffle(tileModel_Collection, new Random(seed));
		
		regions = new ArrayList<TileRegion>();
		for (int i = 0; i < 5; i++)
			regions.add(new TileRegion());
		
		regionZero = regions.get(0);  //a little shorthand so I can stop calling regions.get(0) and so on.
		regionOne = regions.get(1);
		
		addLayersToRegions();
		addRowsToLayers();

		run_populateTileRows();
		run_setBoundsTileModels();
		set_NeighborStatus();
		run_addTileModels();
	}
	
	public void newGame(long seed){
		this.removeAll(); //clear all tiles from the board
		undoMenu_column_1.removeAll(); //clear all undone tiles from the undo bar
		undoMenu_column_2.removeAll(); //clear all undone tiles from the undo bar
		dummies.clear(); //make sure there aren't phantom components existing on the board
		tileModel_Collection.clear(); //clearing out the logical collection of "undone" tiles
		undoLast.setEnabled(false);  //nothing to undo when it's a new game
		undoMenu.setEnabled(false);
		//regions.clear(); //might not need to be here
		initBoard(seed);
		((JFrame)getParent().getParent().getParent().getParent()).setTitle("Game Number " + seed % 100000);
		if (!gameNums_ToFile.contains(seed)) //storing the game number in the file
			gameNums_ToFile.add(seed);
		this.repaint();
	}
	
	/**
	 * check_NoMovesLeft() gets called after every matching Tile pair is removed from the Board.
	 * If it returns true, then a JOptionPane pops up to tell the player that no moves are left.
	 * @return
	 */
	public boolean check_NoMovesLeft(){
		ArrayList<Component> tilesOnBoard = new ArrayList<>();
		ArrayList<Tile> candidates = new ArrayList<Tile>();
		Iterator <Component>itrr;
		int tileID_above;
		boolean noMovesLeft = true;
		
		tilesOnBoard.addAll(Arrays.asList(this.getComponents()));
		/*	calling this.getComponents() returns an array of all components on the board.  
		 *  The components on the board are either dummy JLabels or Tiles.  We use 
		 *  tilesOnBoard.remove() with a Predicate written to remove the dummy JLabels.
		 */
		tilesOnBoard.removeIf(new Predicate<Component>(){
			 	@Override
			    public boolean test(Component t) {
			        return t.getClass().toString().contains("JLabel");
			    }
		});
		
		
		for (TileRegion region : regions){
			for (TileLayer layer : region){
				for (TileRow row : layer){
					if (!row.isEmpty()){
						if (mayBeRemoved(row.peekFirst().tile, row) && isFreeFromSpecialTiles(row.peekFirst().tile)){
							boolean hasTileAbove = false;
							tileID_above = row.peekFirst().tile.tileID + 1000;
							itrr = tilesOnBoard.iterator();
							while (itrr.hasNext()){
								if (((Tile)itrr.next()).tileID == tileID_above)
									hasTileAbove = true;
							}
							if (!hasTileAbove)
								candidates.add(row.peekFirst().tile);
						}
						if (row.peekFirst() != row.peekLast()){
							if (mayBeRemoved(row.peekLast().tile, row) && isFreeFromSpecialTiles(row.peekLast().tile)){
								boolean hasTileAbove = false;
								tileID_above = row.peekLast().tile.tileID + 1000;
								itrr = tilesOnBoard.iterator();
								while (itrr.hasNext()){
									if (((Tile)itrr.next()).tileID == tileID_above)
										hasTileAbove = true;
								}
								if (!hasTileAbove)
									candidates.add(row.peekLast().tile);
							}
						}
					}
				}
			}
		}
		for (Tile tile : candidates){
			Iterator<Tile> itr = candidates.iterator();
			while (itr.hasNext()){
				Tile possibleMatch =  itr.next();
				if (tile.matches(possibleMatch) && tile != possibleMatch)
					noMovesLeft = false;
			}
		}
		if (tileModel_Collection.size() == 144)
			noMovesLeft = false;
		return noMovesLeft;
	}
	
	
	/**
	 * The set****NeighborStatus methods were written BEFORE I discovered that I could ask the game board
	 * for an array of all the components on board.  I think it would have made it a lot simpler to check
	 * for the presence of tiles by TileID.  The way I did it, I had to identify the layers and rows
	 * involved...would have been much cleaner to just check the component array like I do in 
	 * check_NoMovesLeft().  Grr...
	 * 
	 * @param theTile
	 * @param region
	 * @param layer
	 * @param numRows
	 * @param quad
	 * @param row
	 * @param cell
	 */
	
	private void setNorthNeighborStatus(Tile theTile, int region, int layer, int numRows, int quad, int row, int cell){
		int theTileID = theTile.tileID;
		switch (quad){
		case 0:
		case 1:
			if (row + 1 < numRows){
				if (regions.get(region).get(layer).get(row+1).contains(hashMap_tileID.get(theTileID + 10))){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasNorth");
				}
			}
		break;
		case 2:
		case 3:
			if (row != 0){
				if (regions.get(region).get(layer).get(row-1).contains(hashMap_tileID.get(theTileID - 10))){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasNorth");
				}
			}else{
				if (regionZero.get(layer).get(0).contains(hashMap_tileID.get(theTileID - (quad == 2 ? 10100 : 10300) ))){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasNorth");
				}
				}
		break;
		}
	}
	
	private void setEastNeighborStatus(Tile theTile, ArrayDeque<TileModel> theRow, int region, int layer, int numRows, int quad, int row, int cell){
		switch (quad){
		case 0:
		case 1:
		case 2:
		case 3:
			if ( ! theRow.isEmpty() && theTile != theRow.peekFirst().tile   ){
				theTile.neighborStatus += hashMap_neighborStatus.get("hasEast");
			}
			if (!(regions.get(3).get(0).get(0).isEmpty())){
				if (quad == 0 && layer == 0 && row == 0 && cell == 5){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasEastBottomHalf");
				}else if (quad == 3 && layer == 0 && row == 0 && cell == 5){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasEastTopHalf");
				}
			} 
			break;
		}
	}
	
	private void setNorthEastNeighborStatus(Tile theTile, int region, int layer, int numRows, int quad, int row, int cell){
		int theTileID = theTile.tileID;
		switch(quad){
		case 0:
			if (row + 1 < numRows){
				if (regions.get(region).get(layer).get(row+1).contains(hashMap_tileID.get(theTileID + 11))){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
				}
			}
			break;
		case 1:
			if (row + 1 < numRows){
				if (cell != 0){
					if (regions.get(region).get(layer).get(row+1).contains(hashMap_tileID.get(theTileID + 9))){
						theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
					}
				}else{
					if (regionZero.get(layer).get(row+1).contains(hashMap_tileID.get(theTileID -90))){
						theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
					}
				}
			}
			break;
		case 2:
			if (row == 0 || cell == 0){
				if (row == 0 && cell == 0){
					if (regionZero.get(layer).get(0).contains(hashMap_tileID.get(theTileID - 10200))){
						theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
					}
				}else if (row == 0){
					if (regionZero.get(layer).get(0).contains(hashMap_tileID.get(theTileID -10101))){
						theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
					}
				}else if (cell == 0){
					if (regions.get(region).get(layer).get(row-1).contains(hashMap_tileID.get(theTileID + 90))){
						theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
					}
				}
			}else
				if (regions.get(region).get(layer).get(row-1).contains(hashMap_tileID.get(theTileID -11))){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
				}
			break;
		case 3:
			if (row == 0){
				if (regionZero.get(layer).get(0).contains(hashMap_tileID.get(theTileID -10299))){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
				}
			}else
				if (regions.get(region).get(layer).get(row-1).contains(hashMap_tileID.get(theTileID -9))){
					theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
				}
			break;
		}
	}
	
	
	private void setWestOneUpNeighborStatus(Tile theTile, int region, int layer, int numLayers,  int quad, int row, int cell){
		int theTileID = theTile.tileID;  //hopefully layer + 1 evaluates to false before a null pointer exception occurs in the next line

		if (layer + 1 < numLayers){
			if (row < regions.get(region).get(layer+1).size()){
				if ( ! (regions.get(region).get(layer+1).get(row).contains(hashMap_tileID.get(theTileID + 1000)))){
					//tile directly above is not present
					switch(quad){
					case 0:
					case 3:
						if (cell != 0){
							if (regions.get(region).get(layer + 1).get(row).contains(hashMap_tileID.get(theTileID + 999))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasWestOneUp");
							}
						}else
							if (regions.get(region).get(layer + 1).get(row).contains(hashMap_tileID.get(theTileID + (quad == 0? 1100 : 900)))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasWestOneUp");
							}
						break;
					case 1:
					case 2:
						if (regions.get(region).get(layer + 1).get(row).contains(hashMap_tileID.get(theTileID + 1001))){
							theTile.neighborStatus += hashMap_neighborStatus.get("hasWestOneUp");
						}
						break;
					}
				}
			}
		}
	}

	private void setWestTwoUpNeighborStatus(Tile theTile, int region, int layer, int numLayers, int quad, int row, int cell){
		int theTileID = theTile.tileID;  //hopefully layer + 1 evaluates to false before a null pointer exception occurs in the next line
		if (layer + 2 < numLayers){
			if (row < regions.get(region).get(layer+2).size()){
				//tile directly above is not present
				if ( ( ! (regions.get(region).get(layer+1).get(row).contains(hashMap_tileID.get(theTileID + 1000))))
						&&
						
						! (regions.get(region).get(layer+2).get(row).contains(hashMap_tileID.get(theTileID + 2000)))){
					switch(quad){
					case 0:
					case 3:
						if (cell != 0){
							if (regions.get(region).get(layer + 2).get(row).contains(hashMap_tileID.get(theTileID + 1999))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasWestTwoUp");
							}
						}else
							if (regions.get(region).get(layer + 2).get(row).contains(hashMap_tileID.get(theTileID + (quad == 0? 2100 : 1900)))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasWestTwoUp");
							}
						break;
					case 1:
					case 2:
						if (regions.get(region).get(layer + 2).get(row).contains(hashMap_tileID.get(theTileID + 2001))){
							theTile.neighborStatus += hashMap_neighborStatus.get("hasWestTwoUp");
						}
						break;
					}
				}
			}
		}
	}
	
	private void setSouthOneUpNeighborStatus(Tile theTile, int region, int layer, int numLayers, int numRows, int quad, int row, int cell){
		int theTileID = theTile.tileID;  
		boolean isClearOnTop = false;
		if (layer + 1 < numLayers){
			if (row >= regions.get(region).get(layer+1).size())
				isClearOnTop = true;
			else if ( ! (regions.get(region).get(layer+1).get(row).contains(hashMap_tileID.get(theTileID + 1000))))//tile directly above is not present
				isClearOnTop = true;

			if (isClearOnTop){

				switch(quad){
				case 0:
					if (row != 0){
						if (regions.get(region).get(layer + 1).get(row-1).contains(hashMap_tileID.get(theTileID + 990))){
							theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthOneUp");
						}
					}else
						if (regionOne.get(layer + 1).get(0).contains(hashMap_tileID.get(theTileID + 11300))){
							theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthOneUp");
						}
					break;
				case 1:
					if (row != 0){
						if (regions.get(region).get(layer + 1).get(row-1).contains(hashMap_tileID.get(theTileID + 990))){
							theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthOneUp");
						}
					}else
						if (regionOne.get(layer + 1).get(0).contains(hashMap_tileID.get(theTileID + 11100))){
							theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthOneUp");
						}
					break;
				case 2:
					if (row + 1 < regions.get(region).get(layer+1).size()){
						if (row + 1 < numRows){
							if (regions.get(region).get(layer + 1).get(row+1).contains(hashMap_tileID.get(theTileID + 999))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthOneUp");
							}
						}
					}
					break;
				case 3:
					if (row + 1 < regions.get(region).get(layer+1).size()){
						if (row + 1 < numRows){
							if (regions.get(region).get(layer + 1).get(row+1).contains(hashMap_tileID.get(theTileID + 1010))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthOneUp");
							}
						}
					}
					break;
				}
			}
		}
	}
	
	private void setSouthTwoUpNeighborStatus(Tile theTile, int region, int layer, int numLayers, int numRows, int quad, int row, int cell){
		int theTileID = theTile.tileID;  
		boolean isClearOnTop = false;

		if (layer + 2 < numLayers){
			if (row >= regions.get(region).get(layer+2).size())
				isClearOnTop = true;
			else if (  ( ! (regions.get(region).get(layer+1).get(row).contains(hashMap_tileID.get(theTileID + 1000))))
					&&
					! (regions.get(region).get(layer+2).get(row).contains(hashMap_tileID.get(theTileID + 2000))))//tile directly above is not present
				isClearOnTop = true;

			if (isClearOnTop){

				switch(quad){
				case 0:
					if (row != 0){
						if (row - 1 < regions.get(region).get(layer+2).size()){
							if (regions.get(region).get(layer + 2).get(row-1).contains(hashMap_tileID.get(theTileID + 1990))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthTwoUp");
							}
						}
					}else
						if (regionOne.get(layer + 2).get(0).contains(hashMap_tileID.get(theTileID + 12300))){
							theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthTwoUp");
						}
					break;
				case 1:
					if (row != 0){
						if (row - 1 < regions.get(region).get(layer+2).size()){
							if (regions.get(region).get(layer + 2).get(row-1).contains(hashMap_tileID.get(theTileID + 1990))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthTwoUp");
							}
						}
					}else
						if (regionOne.get(layer + 2).get(0).contains(hashMap_tileID.get(theTileID + 12100))){
							theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthTwoUp");
						}
					break;
				case 2:
					if (row + 1 < regions.get(region).get(layer+2).size()){
						if (row + 1 < numRows){
							if (regions.get(region).get(layer + 2).get(row+1).contains(hashMap_tileID.get(theTileID + 1999))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthTwoUp");
							}
						}
					}
					break;
				case 3:
					if (row + 1 < regions.get(region).get(layer+2).size()){
						if (row + 1 < numRows){
							if (regions.get(region).get(layer + 2).get(row+1).contains(hashMap_tileID.get(theTileID + 2010))){
								theTile.neighborStatus += hashMap_neighborStatus.get("hasSouthTwoUp");
							}
						}
					}
					break;
				}
			}
		}
	}
	
	private void set_NeighborStatus(){
		Tile theTile;
		
		for (int region = 0; region < 4; region++){
			ArrayList<TileLayer> theRegion = regions.get(region);
			for (int layer = 0; layer < theRegion.size(); layer++){
				ArrayList<TileRow> theLayer = theRegion.get(layer);
				for (int row = 0; row < theLayer.size(); row++){		
					ArrayDeque<TileModel> theRow = theLayer.get(row);
					for (TileModel tm : theRow){
						theTile = tm.tile;
						if (theTile.neighborStatus != 0)  {
							theTile.setNeighborStatus(0);
						}
						if (theTile.numberAdded == true)
							theTile.numberAdded = false;
						int theQuadrant = tm.tile.getQuadrant();
						int theCell = tm.tile.getCell();
						
						switch (theQuadrant){
						case 0:
						case 1:
						case 2:
						case 3:
							setNorthNeighborStatus(tm.tile, region, layer, theLayer.size(), theQuadrant, row, theCell);
							setEastNeighborStatus(tm.tile, theRow, region, layer, theLayer.size(), theQuadrant, row, theCell);
							setNorthEastNeighborStatus(tm.tile, region, layer, theLayer.size(), theQuadrant, row, theCell);
							setWestOneUpNeighborStatus(tm.tile, region, layer, theRegion.size(), theQuadrant, row, theCell);
							setWestTwoUpNeighborStatus(tm.tile, region, layer, theRegion.size(), theQuadrant, row, theCell);
							setSouthOneUpNeighborStatus(tm.tile, region, layer, theRegion.size(), theLayer.size(), theQuadrant, row, theCell);
							setSouthTwoUpNeighborStatus(tm.tile, region, layer, theRegion.size(), theLayer.size(), theQuadrant, row, theCell);
							break;
						case 4: //the leftmost "special tile"
							theTile.neighborStatus += hashMap_neighborStatus.get("hasEast");
							theTile.neighborStatus += hashMap_neighborStatus.get("hasNorthEast");
							break;
						case 5: //the rightmost two "special tiles"
							if(theTile.tileID == 30500 && tm != regions.get(3).get(0).get(0).peekLast())
								theTile.neighborStatus += hashMap_neighborStatus.get("hasEast");
							break;
						}
						
					}
				}
			}
		}
	}
	
	private void run_addTileModels() {
		add_TileModels(4, 0, 0);
		add_TileModels(2, 0, 0);
		
		add_TileModels(1, 3, 0);
		add_TileModels(0, 3, 0);
		
		add_TileModels(1, 2, 1);
		add_TileModels(1, 2, 0);
		add_TileModels(0, 2, 0);
		add_TileModels(0, 2, 1);
		
		add_TileModels(1,1,2);
		add_TileModels(1,1,1);
		add_TileModels(1,1,0);
		add_TileModels(0,1,0);
		add_TileModels(0,1,1);
		add_TileModels(0,1,2);
		
		add_TileModels(1,0,3);
		add_TileModels(1,0,2);
		add_TileModels(1,0,1);
		add_TileModels(1,0,0);
		add_TileModels(0,0,0);
		add_TileModels(0,0,1);
		add_TileModels(0,0,2);
		add_TileModels(0,0,3);
		
		add_TileModels(3, 0, 0);
	}

	private void run_setBoundsTileModels() {
		setBounds_TileModels(0, 0, 0);
		setBounds_TileModels(0, 0, 1);
		setBounds_TileModels(0, 0, 2);
		setBounds_TileModels(0, 0, 3);
		setBounds_TileModels(1, 0, 0);
		setBounds_TileModels(1, 0, 1);
		setBounds_TileModels(1, 0, 2);
		setBounds_TileModels(1, 0, 3);
		
		setBounds_TileModels(0, 1, 0);
		setBounds_TileModels(0, 1, 1);
		setBounds_TileModels(0, 1, 2);
		setBounds_TileModels(1, 1, 0);
		setBounds_TileModels(1, 1, 1);
		setBounds_TileModels(1, 1, 2);
		
		setBounds_TileModels(0, 2, 0);
		setBounds_TileModels(0, 2, 1);
		setBounds_TileModels(1, 2, 0);
		setBounds_TileModels(1, 2, 1);
		
		setBounds_TileModels(0, 3, 0);
		setBounds_TileModels(1, 3, 0);
		
		setBounds_TileModels(2, 0, 0);
		setBounds_TileModels(3, 0, 0);
		setBounds_TileModels(4, 0, 0);
	}

	private void run_populateTileRows() {
		populate_TileRows(12,0,0,0,0,1);
		populate_TileRows(10,0,0,1,0,1);
		populate_TileRows(8,0,0,2,0,1);
		populate_TileRows(12,0,0,3,0,1);
		populate_TileRows(12,1,0,3,3,2);
		populate_TileRows(8,1,0,2,3,2);
		populate_TileRows(10,1,0,1,3,2);
		populate_TileRows(12,1,0,0,3,2);
		
		populate_TileRows(6,0,1,0,0,1);
		populate_TileRows(6,0,1,1,0,1);
		populate_TileRows(6,0,1,2,0,1);
		populate_TileRows(6,1,1,0,3,2);
		populate_TileRows(6,1,1,1,3,2);
		populate_TileRows(6,1,1,2,3,2);
		
		populate_TileRows(4, 0, 2, 0, 0, 1);
		populate_TileRows(4, 0, 2, 1, 0, 1);
		populate_TileRows(4, 1, 2, 0, 3, 2);
		populate_TileRows(4, 1, 2, 1, 3, 2);
		
		populate_TileRows(2, 0, 3, 0, 0, 1);
		populate_TileRows(2, 1, 3, 0, 3, 2);
		
		populate_SpecialTileRow(2, 0, 0);
		populate_SpecialTileRow(3, 0, 0);
		populate_SpecialTileRow(4, 0, 0);
	}

	private void add_TileModels(int REGION, int LAYER, int ROW) {
		ArrayDeque<TileModel> theRow = regions.get(REGION).get(LAYER).get(ROW);
		Tile theTile;
		if (REGION == 3){
			Iterator<TileModel> itr = theRow.iterator();
			while (itr.hasNext()){
				theTile = ((TileModel)itr.next()).tile;
				add(theTile);
				theTile.original_Z = getComponentZOrder(theTile);
			}
		}else{
			Iterator<TileModel> desc_Itr = theRow.descendingIterator();
			while (desc_Itr.hasNext()){
				theTile = ((TileModel)desc_Itr.next()).tile;
				add(theTile);
				theTile.original_Z = getComponentZOrder(theTile);
			}
		}
	}

	private void setBounds_TileModels(int REGION, int LAYER, int ROW) {
		ArrayDeque<TileModel> theRow = regions.get(REGION).get(LAYER).get(ROW);
		for (TileModel tm : theRow){
			if (REGION < 2){
				switch (tm.tile.getQuadrant()){
				case 0:
					tm.tile.setBounds(center_X+(54*tm.tile.getCell())+(LAYER * 18), center_Y-(72*(tm.tile.getRow()+1))-(LAYER * 18), Tile.SIZE.width, Tile.SIZE.height);
					break;
				case 1:
					tm.tile.setBounds(center_X-(54*(tm.tile.getCell()+1))+(LAYER * 18), center_Y-(72*(tm.tile.getRow()+1))-(LAYER * 18), Tile.SIZE.width, Tile.SIZE.height);
					break;
				case 2:
					tm.tile.setBounds(center_X-(54*(tm.tile.getCell()+1))+(LAYER * 18), center_Y+(72*(tm.tile.getRow()))-(LAYER * 18), Tile.SIZE.width, Tile.SIZE.height);
					break;
				case 3:
					tm.tile.setBounds(center_X+(54*tm.tile.getCell())+(LAYER * 18), center_Y+(72*(tm.tile.getRow()))-(LAYER * 18), Tile.SIZE.width, Tile.SIZE.height);
					break;
				}//end switch
			}//end if (REGION < 2)
			if (REGION >= 2){
				switch(REGION){
				case 2:
					tm.tile.setBounds(center_X-(54*7), center_Y-(72/2), Tile.SIZE.width, Tile.SIZE.height);
					break;
				case 3:
					tm.tile.setBounds(center_X+(54*6)+(tm.tile.getCell()*54), center_Y-(72/2), Tile.SIZE.width, Tile.SIZE.height);
					break;
				case 4:
					tm.tile.setBounds(center_X-(54/2)+(4*18), center_Y-(72/2)-(4*18), Tile.SIZE.width, Tile.SIZE.height);
					break;
				}
			}
		}//end for
	}
	
	private void populate_TileRows(int ROW_LENGTH, int REGION, int LAYER, int ROW, int QUAD_a, int QUAD_b) {
		ArrayDeque<TileModel> theRow = regions.get(REGION).get(LAYER).get(ROW);
		int tileID;
		TileModel theTileModel;
		for (int half_rowLen = ROW_LENGTH/2 - 1; half_rowLen >= 0; half_rowLen--){
			tileID = (REGION * 10000) + (LAYER * 1000) + (QUAD_a * 100) + (ROW * 10) + (half_rowLen);
			theTileModel = tileModel_Collection.get(numTiles-1);
			theTileModel.tile.set_tileID(tileID);
			hashMap_tileID.put(tileID, theTileModel);
			theRow.add(tileModel_Collection.remove(numTiles-1));
			numTiles--;
			theRow.peekLast().tile.setDataFields(REGION, QUAD_a, LAYER, ROW, (half_rowLen));
			theRow.peekLast().tile.addMouseListener(this);
			theRow.peekLast().tile.addMouseMotionListener(this);
		}
		for (int half_rowLen = 0; half_rowLen < ROW_LENGTH/2; half_rowLen++){
			tileID = (REGION * 10000) + (LAYER * 1000) + (QUAD_b * 100) + (ROW * 10) + (half_rowLen);
			theTileModel = tileModel_Collection.get(numTiles-1);
			theTileModel.tile.set_tileID(tileID);
			hashMap_tileID.put(tileID, theTileModel);
			theRow.add(tileModel_Collection.remove(numTiles-1));
			numTiles--;
			theRow.peekLast().tile.setDataFields(REGION, QUAD_b, LAYER, ROW, (half_rowLen));
			theRow.peekLast().tile.addMouseListener(this);
			theRow.peekLast().tile.addMouseMotionListener(this);
		}
	}
	
	private void populate_SpecialTileRow(int REGION, int LAYER, int ROW){
		ArrayDeque<TileModel> theRow = regions.get(REGION).get(LAYER).get(ROW);
		int tileID;
		TileModel theTileModel;
		switch(REGION){
		case 2:
			tileID = (REGION * 10000) + (LAYER * 1000) + (4 * 100) + (ROW * 10) + (0);
			theTileModel = tileModel_Collection.get(numTiles-1);
			//theTileModel.tileID = tileID;
			theTileModel.tile.set_tileID(tileID);
			hashMap_tileID.put(tileID, theTileModel);
			theRow.add(tileModel_Collection.remove(numTiles-1));
			numTiles--;
			theRow.peekLast().tile.setDataFields(REGION, 4, 0, 0, 0);
			theRow.peekLast().tile.addMouseListener(this);
			theRow.peekLast().tile.addMouseMotionListener(this);
			break;
		case 3:
			tileID = (REGION * 10000) + (LAYER * 1000) + (5 * 100) + (ROW * 10) + (0);
			theTileModel = tileModel_Collection.get(numTiles-1);
			//theTileModel.tileID = tileID;
			theTileModel.tile.set_tileID(tileID);
			hashMap_tileID.put(tileID, theTileModel);
			theRow.add(tileModel_Collection.remove(numTiles-1));
			numTiles--;
			tileID = (REGION * 10000) + (LAYER * 1000) + (5 * 100) + (ROW * 10) + (1);
			theTileModel = tileModel_Collection.get(numTiles-1);
			//theTileModel.tileID = tileID;
			theTileModel.tile.set_tileID(tileID);
			hashMap_tileID.put(tileID, theTileModel);
			theRow.add(tileModel_Collection.remove(numTiles-1));
			numTiles--;
			theRow.peekFirst().tile.setDataFields(REGION, 5, 0, 0, 0);
			theRow.peekFirst().tile.addMouseListener(this);
			theRow.peekFirst().tile.addMouseMotionListener(this);
			theRow.peekLast().tile.setDataFields(REGION, 5, 0, 0, 1);
			theRow.peekLast().tile.addMouseListener(this);
			theRow.peekLast().tile.addMouseMotionListener(this);
			break;
		case 4:
			tileID = (REGION * 10000) + (LAYER * 1000) + (6 * 100) + (ROW * 10) + (0);
			theTileModel = tileModel_Collection.get(numTiles-1);
			//theTileModel.tileID = tileID;
			theTileModel.tile.set_tileID(tileID);
			hashMap_tileID.put(tileID, theTileModel);
			theRow.add(tileModel_Collection.remove(numTiles-1));
			numTiles--;
			theRow.peekLast().tile.setDataFields(REGION, 6, 0, 0, 0);
			theRow.peekLast().tile.addMouseListener(this);
			theRow.peekLast().tile.addMouseMotionListener(this);
			break;
		}
	}
	
	private boolean isFreeFromSpecialTiles(Tile theTile){
		int theRegion = theTile.getRegion();
		int theQuadrant = theTile.getQuadrant();
		int theLayer = theTile.getLayer();
		int theRow = theTile.getRow();
		int theCell = theTile.getCell();
		
		boolean isFree = true;
		if (theLayer == 0 && theRow == 0){
			if (theQuadrant == 1 || theQuadrant == 2){
				if (regions.get(2).get(0).get(0).isEmpty() || theTile == regionZero.get(0).get(0).peekFirst().tile 
						|| theTile == regionOne.get(0).get(0).peekFirst().tile )
					isFree = true;
				else
					isFree =  false;
			}else if (theQuadrant == 0 || theQuadrant == 3){
				if (regions.get(3).get(0).get(0).isEmpty())
					isFree =  true;
				else
					isFree =  false;
			}else if (theRegion == 3){
				if (theCell == 0 && regions.get(3).get(0).get(0).size() > 1)
					isFree = false;
			}
		}else if (theLayer == 3){
			if (regions.get(4).get(0).get(0).isEmpty())
				isFree =  true;
			else
				isFree =  false;
		}
		return isFree;
	}
	
	private boolean mayBeRemoved(Tile sourceTile, TileRow sourceRow){
		return (sourceTile == sourceRow.peekLast().tile || sourceTile == sourceRow.peekFirst().tile);		
	}
	
	
	/**
	 * the Tournament time counter is displayed in the menu bar just because
	 * that's a nice location for it.
	 * @param theFrame
	 */
	protected void makeTimeMenu(JFrame theFrame){
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(timerMenu);
		theTimer = new DClock(timerMenu);
	}
	
	protected void makeSoundMenu(JFrame theFrame){
		soundMenu.setMnemonic('S');
		soundOnOff.setMnemonic('O');
		soundOnOff.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		soundMenu.setToolTipText("Toggle Sound On and Off");
		
		soundOnOff.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (soundOnOff.getText().equals("Off")){
					soundOnOff.setText("On");
					is_SoundOn = false;
				}else{
					soundOnOff.setText("Off");
					is_SoundOn = true;
				}					
			}
		});
		
		soundMenu.add(soundOnOff);
		menuBar.add(soundMenu);		
	}
	
	protected void makeHelpMenu(JFrame theFrame){
		helpMenu.setMnemonic('H');
		gameRules.setMnemonic('R');
		gameOperation.setMnemonic('O');
		
		gameOpsScrollPane.setWheelScrollingEnabled(true);
		gameOpsScrollPane.setPreferredSize(new Dimension(boardSize.width - 310, 560));
		gameOpsScrollPane.getViewport().add(gameOps);
		gameOps.setEditable(false);
		gameOps.setMargin(new Insets(15, 15, 15, 15));
		gameOps.setContentType("text/html");
		gameOps.setText("<div style=\"width:400px;\">"
				+ "<span style=\"font-weight: bold;\">Game Menu:</span>"
				+ "<table style=\"margin-top: 10px;\">"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold; text-align: right; padding-right: 20px;\">Play New:</td><td style=\"width: 60%;\">Resets the board with a brand new game.</td></tr>"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold;text-align: right;padding-right: 20px;\">Play Numbered:</td><td>Resets the board with a brand new game having the chosen game number.</td></tr>"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold;text-align: right;padding-right: 20px;\">Restart:</td><td>Restarts the current game.</td></tr>"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold;text-align: right;padding-right: 20px;\">Tournament:</td><td>Resets the board with a brand new game, and immediately starts a timer.  It is not possible to restart the game or to undo any moves.</td></tr>"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold;text-align: right;padding-right: 20px;\">Exit:</td><td>Exits the game.</td></tr>"
				+ "</table>"
				+ "<hr>"
				+ "<span style=\"font-weight: bold;\">Move Menu:</span>"
				+ "<table style=\"margin-top: 10px;\">"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold; text-align: right; padding-right: 20px;\">Undo Last:</td><td style=\"width: 60%;\">Restores the most recently removed pair of tiles.</td></tr>"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold;text-align: right;padding-right: 20px;\">Undo Multiple:</td><td>Displays all removed tiles pairs.  Click on any pair to restore it to the board along with all pairs to the right.</td></tr>"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold;text-align: right;padding-right: 20px;\">Redo Last:</td><td>Only available if the last action was an undo.  Re-removes the last tile pair restored to the board.</td></tr>"
				+ "<tr><td valign=\"top\" style=\"font-weight: bold;text-align: right;padding-right: 20px;\">Redo All:</td><td>Only available if the last action was an undo.  Re-removes all tile pairs restored to the board just prior to the redo.</td></tr>"
				+ "</table>"
				+ "<hr>"
				+ "<table style=\"margin-top: 10px; margin-left: -3%;\">"
				+ "<tr><td style=\"font-weight: bold;  padding-right: 90px;\">Sound Menu:</td><td>Toggles sound on and off.</td></tr>"
				+ "</table>"
				+ "</div>");
		gameOperation.add(gameOpsScrollPane);
		
		gameRulesScrollPane.setWheelScrollingEnabled(true);
		gameRulesScrollPane.setPreferredSize(new Dimension(430, 380));
		gameRulesScrollPane.getViewport().add(gamePlayRules);
		gamePlayRules.setEditable(false);
		gamePlayRules.setMargin(new Insets(15, 25, 15, 15));
		gamePlayRules.setContentType("text/html");
		gamePlayRules.setText("<span style=\"font-weight: bold;\">The object of the game is to:</span>"
				+ "<br>"
				+ "<ul style = \"list-style-type: none;\">"
				+ "<li>Remove every tile by matching identical pairs of free tiles.</li>"
				+ "</ul>"
				+ "<br><br>"
				+ "<span style=\"font-weight: bold;\">A tile is free when:</span>"
				+ "<br>"
				+ "<ul style = \"list-style-type: none;\">"
				+ "<li>No tile is on top of it.</li>"
				+ "<li>It has no tile next to it on either the left or the right side.</li>"
				+ "</ul>"
				+ "<br><br>"
				+ "<span style=\"font-weight: bold;\">Two tiles are a matching pair when:</span>"
				+ "<br>"
				+ "<ul style = \"list-style-type: none\">"
				+ "<li>The tiles are any two Seasons.</li>"
				+ "<li>The tiles are any two Flowers.</li>"
				+ "<li>The tiles are the same kind of Dragon.</li>"
				+ "<li>The tiles are the same kind of Wind.</li>"
				+ "<li>The tiles share the same suit and rank.</li>"
				+ "</ul>");
		gameRules.add(gameRulesScrollPane);
		
		helpMenu.add(gameRules);
		helpMenu.add(gameOperation);
		menuBar.add(helpMenu);
	}

	/**
	 * in Tournament mode, the number of tiles removed is displayed in the center
	 * of the menubar because I like it there.
	 * 
	 * @param theFrame
	 */
	protected void makeNumTilesRemovedMenu(JFrame theFrame){
		numTilesRemoved.setVisible(false);
		menuBar.add(Box.createHorizontalStrut(190));
		menuBar.add(numTilesRemoved);
	}
	
	protected void makeGameMenu(JFrame theFrame){
		gameMenu.setMnemonic('G');
		
		playNew.setMnemonic('N');
		playNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		playNew.setToolTipText("Start A New Game");
		
		numbered.setMnemonic('M');
		numbered.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK));
		numbered.setToolTipText("Start A Numbered Game");
		
		restart.setMnemonic('R');
		restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
		restart.setToolTipText("Restart the Current Game");
		
		tournament.setMnemonic('T');
		tournament.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
		tournament.setToolTipText("Start a New Game in Tournament Mode");
		
		exit.setMnemonic('E');
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK));
		exit.setToolTipText("Quit the Game");
		
		tournament.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				theTimer = new DClock(timerMenu);
				tournMode = true;
				int isSure = JOptionPane.showConfirmDialog((JFrame)getParent().getParent().getParent().getParent(), 
						"Start a new game in Tournament Mode?", "Tournament Mode: Are You Sure?", JOptionPane.YES_NO_OPTION);
				if (isSure == JOptionPane.YES_OPTION){
					newGame(System.currentTimeMillis());
				}
				numTilesRemoved.setText("Tiles Remaining:  " + (144 - tileModel_Collection.size()));
				numTilesRemoved.setVisible(true);	
				timerMenu.setVisible(true);
				tournament.setEnabled(false);
				theTimer.start();
				redoLast.setEnabled(false);
				redoAll.setEnabled(false);
				restart.setEnabled(false);
			}
		});
		
		playNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int isSure = JOptionPane.showConfirmDialog((JFrame)getParent().getParent().getParent().getParent(), 
						"Are you okay with ending this game to start a new one?", "New Game: Are You Sure?", JOptionPane.YES_NO_OPTION);
				if (isSure == JOptionPane.YES_OPTION){
					tournMode = false;
					theTimer.keepRunning = false;
					numTilesRemoved.setVisible(false);
					timerMenu.setVisible(false);
					restart.setEnabled(true);
					tournament.setEnabled(true);
					newGame(System.currentTimeMillis() % 100000);
				}
			}
		});
		
		/**
		 *  the Numbered Game menu option lets the player choose from a dropdown menu of past game numbers
		 *  OR input their own number.  The ArrayLists modResults and toSort are used in the process of
		 *  arranging the numbers in the dropdown menu in ascending order -- just for display.
		 */
		numbered.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				Collections.sort(gameNums_ToFile);
				ArrayList<Long> modResults = new ArrayList<>();
				ArrayList<Long> toSort = new ArrayList<>();
				gameNumsArray = gameNums_ToFile.toArray(new Long[gameNums_ToFile.size()]);
				for (int i = 0; i < gameNumsArray.length; i++){
					gameNumsArray[i] = gameNumsArray[i] % 100000;
					modResults.add(gameNumsArray[i]);
				}
				toSort.addAll(modResults);
				Collections.sort(toSort);
				
				Long theResponse;
				String theStringResponse;
				int isSure = JOptionPane.showConfirmDialog((JFrame)getParent().getParent().getParent().getParent(), 
						"Are you okay with ending this game to start a new one?", "Are You Sure?", JOptionPane.YES_NO_OPTION);
				if (isSure == JOptionPane.YES_OPTION){
						UIManager.put("OptionPane.okButtonText", "Play This Number");
						UIManager.put("OptionPane.cancelButtonText", "Enter My Own Number");
						theResponse = (Long) JOptionPane.showInputDialog(
								((JFrame)getParent().getParent().getParent().getParent()),
								"Choose the game you wish to play.",
								"Numbered Game",
								JOptionPane.QUESTION_MESSAGE,
								UIManager.getIcon("OptionPane.questionIcon"),	
								toSort.toArray(new Long[toSort.size()]),
								"10000");	
						

						if (theResponse != null){
							theResponse = gameNums_ToFile.get(modResults.indexOf(theResponse));
							newGame(theResponse);
						}else{
							UIManager.put("OptionPane.okButtonText", "OK");
							UIManager.put("OptionPane.cancelButtonText", "Cancel");
							theStringResponse =  (String) JOptionPane.showInputDialog(
									((JFrame)getParent().getParent().getParent().getParent()),
									"Enter a number.",
									"Numbered Game",
									JOptionPane.QUESTION_MESSAGE,
									UIManager.getIcon("OptionPane.questionIcon"),	
									null,
									"10000");
							if (theStringResponse != null){
								theResponse = Long.decode(theStringResponse);
								newGame(theResponse);
								
							}
							}
				
						tournMode = false;
						theTimer.keepRunning = false;
						numTilesRemoved.setVisible(false);
						timerMenu.setVisible(false);
						restart.setEnabled(true);
						tournament.setEnabled(true);
				}
				
			}
		});
		restart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				restoreTiles(0);  //puts all undone tiles back on the board, going back from the last one removed to index 0 (the first one removed)
				if (thisBoard.getBackground() == Color.BLACK)
					thisBoard.setBackground(new Color(87, 8, 11)); 
			}
		});
		
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (JOptionPane.showConfirmDialog((JFrame)getParent().getParent().getParent().getParent(), "Quit the game and exit?", "Exit", JOptionPane.YES_NO_OPTION)
						== JOptionPane.YES_OPTION){
					write_GameNums_ToFile();
					System.exit(0);
				}
			}
		});
		
		
		gameMenu.add(playNew);
		gameMenu.add(numbered);
		gameMenu.add(restart);
		gameMenu.add(tournament);
		gameMenu.add(exit);
		menuBar.add(gameMenu);
		
	}
	
	protected void makeMoveMenu(JFrame theFrame){
		moveMenu.setMnemonic('M');
		undoMenu.setMnemonic('M');
		undoMenu.setToolTipText("Undo More Than One");
		
		scrollPane.setPreferredSize(new Dimension(boardSize.width - 200, Tile.SIZE_SANS_SHADOW.height * 2 + (Tile.SHADOW_SIZE * 2) + 4));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setWheelScrollingEnabled(true);

		undoMenu_twoColumnContainerPanel.setLayout(new BoxLayout(undoMenu_twoColumnContainerPanel, BoxLayout.Y_AXIS));
		undoMenu_twoColumnContainerPanel.setPreferredSize(new Dimension(Tile.SIZE.width * 100,scrollPane.getSize().height));
		
		undoMenu_column_1.setLayout(new FlowLayout(FlowLayout.LEADING, -9,0));
		undoMenu_column_2.setLayout(new FlowLayout(FlowLayout.LEADING, -9,0));
		
		undoMenu_twoColumnContainerPanel.add(undoMenu_column_1);
		undoMenu_twoColumnContainerPanel.add(undoMenu_column_2);
		scrollPane.getViewport().add(undoMenu_twoColumnContainerPanel);
		
		redoLast.setMnemonic('R');
		redoLast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK));
		redoLast.setEnabled(is_RedoEnabled & !tournMode);
		redoLast.setToolTipText("Remove Most Recent Pair Restored");
		redoLast.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
				redo(2);
			}
		});
		
		redoAll.setMnemonic('A');
		redoAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK + Event.SHIFT_MASK));
		redoAll.setToolTipText("Remove All Pairs Just Restored");
		redoAll.setEnabled(is_RedoEnabled & !tournMode);
		redoAll.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
				redo(redoTiles.size());
			}
		});
		
		
		undoLast.setMnemonic('U');
		undoLast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK));
		undoLast.setToolTipText("Undo Most Recent Pair");
		undoLast.addActionListener(new ActionListener(){
			public void actionPerformed (ActionEvent e){
				restoreTiles(tileModel_Collection.size()-2);
				is_RedoEnabled = true;
				redoLast.setEnabled(is_RedoEnabled & !tournMode);
				redoAll.setEnabled(is_RedoEnabled & !tournMode);
			}
		});
		
		undoMenu.add(scrollPane);
		moveMenu.add(undoLast);
		moveMenu.add(undoMenu);
		moveMenu.add(redoLast);
		moveMenu.add(redoAll);
		
		menuBar.add(moveMenu);
		theFrame.setJMenuBar(menuBar);
		
		if (tileModel_Collection.isEmpty()){
			undoLast.setEnabled(false);
			undoMenu.setEnabled(false);
		}
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		Tile sourceTile = (Tile)e.getSource(); 
		
		ArrayList<TileLayer> sourceRegion = regions.get(sourceTile.getRegion());
		ArrayList<TileRow> sourceLayer = sourceRegion.get(sourceTile.getLayer());
		TileRow sourceRow = sourceLayer.get(sourceTile.getRow());
		TileModel targetToRemove_1 = null;
		TileModel targetToRemove_2 = null;
		
		if (e.getX() >= Tile.FACE_LEFT_EDGE_X && e.getX() <= Tile.FACE_LEFT_EDGE_X + Tile.FACE_RECTANGLE.width
				&& e.getY() >= Tile.FACE_UPPER_EDGE_Y && e.getY() <= Tile.FACE_UPPER_EDGE_Y + Tile.FACE_RECTANGLE.height){
			
			if (isFreeFromSpecialTiles(sourceTile) && mayBeRemoved(sourceTile, sourceRow)){
				if (currentSelectedTile != null){
					previousSelectedTile = currentSelectedTile;
					previousSelectedTileRow = currentSelectedTileRow;
					previousSelectedTile.isHighlighted = false;
					previousSelectedTile.repaint();
				}
				currentSelectedTile = sourceTile;
				currentSelectedTileRow = sourceRow;

				if (previousSelectedTile != null){
					if (currentSelectedTile.matches(previousSelectedTile)
							&& currentSelectedTile != previousSelectedTile){
						
						currentSelectedTile.originalBounds = currentSelectedTile.getBounds();
						previousSelectedTile.originalBounds = previousSelectedTile.getBounds();
						
						if (currentSelectedTile == currentSelectedTileRow.peekLast().tile){
							targetToRemove_1 = currentSelectedTileRow.peekLast();
							if (currentSelectedTileRow.size() >= 2){								
								targetToRemove_1.tile.firstOrLast = "last";
								if (currentSelectedTileRow.size() == 2){
									currentSelectedTileRow.peekFirst().tile.firstOrLast = "first";
								}
							}
							if (currentSelectedTileRow.size()== 1)
								currentSelectedTileRow.peekFirst().tile.firstOrLast = "first";
							dummies.add(new JLabel());
							this.add(dummies.get(dummies.size()-1));
							this.setComponentZOrder(dummies.get(dummies.size()-1), targetToRemove_1.tile.original_Z);
							targetToRemove_1 = currentSelectedTileRow.pollLast();
							
						}
						else if (currentSelectedTile == currentSelectedTileRow.peekFirst().tile){
							targetToRemove_1 = currentSelectedTileRow.peekFirst();
							if (currentSelectedTileRow.size() >= 2){								
								targetToRemove_1.tile.firstOrLast = "first";
								if (currentSelectedTileRow.size() == 2){
									currentSelectedTileRow.peekLast().tile.firstOrLast = "last";
								}
							}
							if (currentSelectedTileRow.size()== 1)
								currentSelectedTileRow.peekFirst().tile.firstOrLast = "first";
							dummies.add(new JLabel());
							this.add(dummies.get(dummies.size()-1));
							this.setComponentZOrder(dummies.get(dummies.size()-1), targetToRemove_1.tile.original_Z);
							targetToRemove_1 = currentSelectedTileRow.pollFirst();
						}
						tileModel_Collection.add(targetToRemove_1);
						targetToRemove_1.tile.isShadowOn = false;
						undoMenu_column_2.add(targetToRemove_1.tile);
						targetToRemove_1.tile.setVisible(true);
					
						if (previousSelectedTile == previousSelectedTileRow.peekLast().tile){
							targetToRemove_2 = previousSelectedTileRow.peekLast();
							if (previousSelectedTileRow.size() >= 2){								
								targetToRemove_2.tile.firstOrLast = "last";
								if (previousSelectedTileRow.size() == 2){
									previousSelectedTileRow.peekFirst().tile.firstOrLast = "first";
								}
							}
							if (previousSelectedTileRow.size()== 1)
								previousSelectedTileRow.peekFirst().tile.firstOrLast = "first";
							
							dummies.add(new JLabel());
							this.add(dummies.get(dummies.size()-1));
							this.setComponentZOrder(dummies.get(dummies.size()-1), targetToRemove_2.tile.original_Z);
							targetToRemove_2 = previousSelectedTileRow.pollLast();
						}
						else if (previousSelectedTile == previousSelectedTileRow.peekFirst().tile){
							targetToRemove_2 = previousSelectedTileRow.peekFirst();
							if (previousSelectedTileRow.size() >= 2){								
								targetToRemove_2.tile.firstOrLast = "first";
								if (previousSelectedTileRow.size() == 2){
									previousSelectedTileRow.peekLast().tile.firstOrLast = "last";
								}
							}
							if (previousSelectedTileRow.size()== 1)
								previousSelectedTileRow.peekFirst().tile.firstOrLast = "first";
							
							dummies.add(new JLabel());
							this.add(dummies.get(dummies.size()-1));
							this.setComponentZOrder(dummies.get(dummies.size()-1), targetToRemove_2.tile.original_Z);
							targetToRemove_2 = previousSelectedTileRow.pollFirst();
						}
						tileModel_Collection.add(targetToRemove_2);
						targetToRemove_2.tile.isShadowOn = false;
						undoMenu_column_1.add(targetToRemove_2.tile);
						targetToRemove_2.tile.setVisible(true);
						
						playMatchSound(is_SoundOn);
						
						this.remove(targetToRemove_1.tile);
						this.repaint(targetToRemove_1.tile.getBounds());
						this.remove(targetToRemove_2.tile);
						this.repaint(targetToRemove_2.tile.getBounds());
						
						numTilesRemoved.setText("Tiles Remaining:  " + (144 - tileModel_Collection.size()));
						
						//using invokeLater so that mouse cursor goes back to default after the tile vanishes visually
						
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							}
						});
						
						targetToRemove_1.tile.removeMouseListener(this);
						targetToRemove_1.tile.addMouseListener(undo_ML);
						targetToRemove_2.tile.removeMouseListener(this);
						targetToRemove_2.tile.addMouseListener(undo_ML);
						
						
						set_NeighborStatus();
						currentSelectedTile = previousSelectedTile = null;
						
						if (check_NoMovesLeft()){
							UIManager.put("OptionPane.okButtonText", "OK");
						JOptionPane.showMessageDialog(
								((JFrame)getParent().getParent().getParent().getParent()),
								"No moves are left.", "You're Stuck", JOptionPane.INFORMATION_MESSAGE);}
					}	
						if (!tournMode){
							undoLast.setEnabled(true);
							undoMenu.setEnabled(true);
						}
							redoTiles.clear();
							redoLast.setEnabled(false);
							redoAll.setEnabled(false);
				}
				if (currentSelectedTile != null){
					currentSelectedTile.isHighlighted = true;
					currentSelectedTile.repaint();
				}
			}
			if (undoMenu_column_1.getComponentCount() > 9){
			scrollPane.getViewport().setViewPosition(new Point(Tile.SIZE.width * undoMenu_column_1.getComponentCount() - (Tile.SIZE.width * 9),
					10));
			}	
		}
		
		
		if (tileModel_Collection.size() == 144){
			thisBoard.setBackground(Color.BLACK);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					
					fw.setExplosions(0, 1000);
					fw.fire();
					try
					{
						Thread.sleep(10000);
						fw.stop();
					}catch (InterruptedException ie) {}
				}
			});
		}
	}
	
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void mouseDragged(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent arg0) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	//16958

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getX() >= Tile.FACE_LEFT_EDGE_X && e.getX() <= Tile.FACE_LEFT_EDGE_X + Tile.FACE_RECTANGLE.width
				&& e.getY() >= Tile.FACE_UPPER_EDGE_Y && e.getY() <= Tile.FACE_UPPER_EDGE_Y + Tile.FACE_RECTANGLE.height){
				setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	private void populate_TileModelCollection() {
		for (int set = 0; set < 4; set++){
			for (int i = 1; i < 10; i++){
				tileModel_Collection.add(new TileModel(new CharacterTile((""+i).charAt(0))));
				tileModel_Collection.add(new TileModel(new CircleTile(i)));
				if (i == 1)
					tileModel_Collection.add(new TileModel(new Bamboo1Tile()));
				else
					tileModel_Collection.add(new TileModel(new BambooTile(i)));
		}
		
			tileModel_Collection.add(new TileModel(new CharacterTile('N')));
			tileModel_Collection.add(new TileModel(new CharacterTile('E')));
			tileModel_Collection.add(new TileModel(new CharacterTile('W')));
			tileModel_Collection.add(new TileModel(new CharacterTile('S')));
			tileModel_Collection.add(new TileModel(new CharacterTile('C')));
			tileModel_Collection.add(new TileModel(new CharacterTile('F')));
			tileModel_Collection.add(new TileModel(new WhiteDragonTile()));
		}
		tileModel_Collection.add(new TileModel(new FlowerTile("Chrysanthemum")));
		tileModel_Collection.add(new TileModel(new FlowerTile("Orchid")));
		tileModel_Collection.add(new TileModel(new FlowerTile("Plum")));
		tileModel_Collection.add(new TileModel(new FlowerTile("Bamboo")));
		tileModel_Collection.add(new TileModel(new SeasonTile("Fall")));
		tileModel_Collection.add(new TileModel(new SeasonTile("Winter")));
		tileModel_Collection.add(new TileModel(new SeasonTile("Spring")));
		tileModel_Collection.add(new TileModel(new SeasonTile("Summer")));
	}
	
	private void addRowsToLayers() {
		
		//adding rows to the four layers in regions 0 and 1
		for (int row = 0; row < 4; row ++){
			if (row == 0){
				for (int layer = 0; layer < 4; layer ++){
					regionZero.get(layer).add(new TileRow());
					regionOne.get(layer).add(new TileRow());
				}
			}else if (row == 1){
				for (int layer = 0; layer < 3; layer ++){
					regionZero.get(layer).add(new TileRow());
					regionOne.get(layer).add(new TileRow());
				}
			}else if (row == 2){
				for (int layer = 0; layer < 2; layer ++){
					regionZero.get(layer).add(new TileRow());
					regionOne.get(layer).add(new TileRow());
				}
			}else if (row == 3){
				for (int layer = 0; layer < 1; layer ++){
					regionZero.get(layer).add(new TileRow());
					regionOne.get(layer).add(new TileRow());
				}
			}
		}
		
		//adding 1 row to the one and only layer in regions 2, 3, & 4
		for (int region = 2; region < 5; region ++){
			regions.get(region).get(0).add(new TileRow());
		}
	}
	
	private void addLayersToRegions() {
		for (int region = 0; region < 5; region++){ //regions 1 & 2 have 4 TileLayers each.  The Bottom layer is index 0.
			for (int layer = 0; layer < 4; layer++){ //regions 3, 4, & 5 have only 1 TileLayer.
				if (layer == 0)
					regions.get(region).add(new TileLayer());
				else 
					if (layer > 0 && region < 2)
						regions.get(region).add(new TileLayer());
			}
		}
	}

	class TileRegion extends ArrayList<TileLayer>{}
	class TileLayer extends ArrayList<TileRow>{}
	class TileRow extends ArrayDeque<TileModel>{}
	
	class TileModel implements Comparable<TileModel>{
		
		Tile tile;
		
		TileModel(Tile tile){
			this.tile = tile;
		}
		
		@Override
		public int compareTo(TileModel other) {
			return other.tile.original_Z - this.tile.original_Z;
		}
	}
	
	
	class Undo_MouseListener implements MouseListener{
				
		public void mouseClicked(MouseEvent e) {			
			if (currentSelectedTile != null){
				currentSelectedTile.isHighlighted = false;
				currentSelectedTile.repaint();
			}
			Tile sourceTile = (Tile)e.getSource();
			int collectionIndex = tileModel_Collection.size()-1;
			Iterator<TileModel> itr = tileModel_Collection.iterator();
			while (itr.hasNext()){
				collectionIndex = tileModel_Collection.indexOf(itr.next());
				if (tileModel_Collection.get(collectionIndex).tile == sourceTile)
					break;
			}
			
			collectionIndex = (collectionIndex / 2) * 2;
			
			if (undoLastTwo)
				collectionIndex = 0;
			
			restoreTiles(collectionIndex);
			
			undoLastTwo = false;
			is_RedoEnabled = true;
			redoLast.setEnabled(is_RedoEnabled & !tournMode);
			redoAll.setEnabled(is_RedoEnabled & !tournMode);
				
				//move mouse to location to click
				  robby.mouseMove(0, 600);   //this closes the menu
				// and click
				  SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							robby.mousePress(InputEvent.BUTTON1_MASK);
							  robby.mouseRelease(InputEvent.BUTTON1_MASK);
						}
					});
		}



		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}
		public void mousePressed(MouseEvent arg0) {}
		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
	private void restoreTiles(int collectionIndex) {
		TileModel undoTileModel;
		TileRow undoRow;
		for (int i = tileModel_Collection.size()-1; i >= collectionIndex; i--){
			undoTileModel = tileModel_Collection.remove(i);
			
			undoRow = regions.get(undoTileModel.tile.getRegion()).get(undoTileModel.tile.getLayer())
					.get(undoTileModel.tile.getRow());
			undoTileModel.tile.removeMouseListener(undo_ML);
			undoTileModel.tile.addMouseListener(board_asListener);
			if (undoTileModel.tile.firstOrLast == "first")
				undoRow.addFirst(undoTileModel);
			else
				undoRow.addLast(undoTileModel);
			undoTileModel.tile.setBounds(undoTileModel.tile.originalBounds);
			undoTileModel.tile.isShadowOn = true;
			redoTiles.add(undoTileModel.tile);
			
			thisBoard.add(undoTileModel.tile);
			thisBoard.setComponentZOrder(undoTileModel.tile, undoTileModel.tile.original_Z);
			thisBoard.remove(dummies.get(dummies.size()-1));
			dummies.remove(dummies.size()-1);
			thisBoard.set_NeighborStatus();
			thisBoard.repaint(undoTileModel.tile.getBounds());
		}
		 if (tileModel_Collection.isEmpty()){
				undoLast.setEnabled(false);
				undoMenu.setEnabled(false);
			}
	}
	
	private void redo(int howMany){
		Point frameLocation = ((JFrame)getParent().getParent().getParent().getParent()).getLocation();
		Tile redoTile;
		for (int i = howMany; i > 0; i--){
			redoTile = redoTiles.remove(redoTiles.size()-1);
			//robby clicks on the tiles so that they are removed from the board...
			robby.mouseMove(redoTile.getBounds().x + frameLocation.x + Tile.SIZE.width, redoTile.getBounds().y + frameLocation.y + Tile.SIZE.height );
			robby.mousePress(InputEvent.BUTTON1_MASK);
			robby.mouseRelease(InputEvent.BUTTON1_MASK);
		}
	}
	
	
	protected void playMatchSound(boolean is_SoundOn){
		if (is_SoundOn){
			clip.play();
		}
	}
	
	
	/** Reads an ArrayList of game numbers from a file stored in the project.
	 *  If the file is empty, then this method calls the method write_GameNums_ToFile()
	 *  so that there will be something to start reading.
	 */

	@SuppressWarnings("unchecked")
	protected void read_GameNums_FromFile (){
		ObjectInputStream in = null;
		 try
	      {			 
	         FileInputStream fileIn = new FileInputStream("src/files/gameNumbers.ser");	
	         try{
	        	 in = new ObjectInputStream(fileIn);
	        
	         }catch(Exception e){
	        	 gameNums_ToFile.add(theFirstSeed % 100000);
	        	 write_GameNums_ToFile();
	         }finally{
	        	 if (in == null)
	        		 in = new ObjectInputStream(fileIn);
	         
	         gameNums_FromFile = (ArrayList<Long>) in.readObject();
	         
	         for (long l : gameNums_FromFile){
	        	 if (!gameNums_ToFile.contains(l))
	        		 gameNums_ToFile.add(l);
	         }
	         in.close();
	         fileIn.close();
	         }
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("ArrayList<Integer> class not found");
	         c.printStackTrace();
	         return;
	      }
	    }
	
	/**  Writes an ArrayList of game numbers to the file stored in the project.  
	 *   These are all the numbers that were generated randomly during gameplay,
	 *   added to whatever numbers were already stored in the file.
	 */
	protected void write_GameNums_ToFile(){
		 try
	      {
	         FileOutputStream fileOut =
	         new FileOutputStream("src/files/gameNumbers.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(gameNums_ToFile);
	         out.close();
	         fileOut.close();
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
		
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);		
		dragon.paintIcon(this, g, 65, 56);
	}
	
	
	/**  It might have been simpler to make the class MahjongGame extend JFrame, and then add a board to that.
	 * 	 But by the time I considered what to do about a JFrame, the Board was already really, really complicated.
	 * 
	 * @param args
	 * @throws AWTException
	 */
	
	public static void main(String[] args) throws AWTException {
		final MahjongBoard board = new MahjongBoard();
		
		JFrame MahjongGame = new JFrame();
		Dimension	screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		MahjongGame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		MahjongGame.addWindowListener(new WindowAdapter()
		{public void windowClosing(WindowEvent e)
			{
				board.write_GameNums_ToFile();
				if (JOptionPane.showConfirmDialog(MahjongGame, "Quit the game and Exit?", "Exit Game", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					System.exit(0);
				else{};
			}
		});
		MahjongGame.add(board);
		MahjongGame.setLocation(screenSize.width / 4, screenSize.height / 20);
		board.makeGameMenu(MahjongGame);
		board.makeMoveMenu(MahjongGame);
		board.makeSoundMenu(MahjongGame);
		board.makeHelpMenu(MahjongGame);
		board.makeNumTilesRemovedMenu(MahjongGame);
		board.makeTimeMenu(MahjongGame);
		MahjongGame.setTitle("Game Number " + board.theFirstSeed % 100000);
		MahjongGame.pack();
		//board.clip.play();  //seems to help the initial timing of the sound
		MahjongGame.setVisible(true);
	}
	
	static{
		hashMap_neighborStatus = new Hashtable<String, Integer>();
		hashMap_neighborStatus.put("hasNorth", 1);
		hashMap_neighborStatus.put("hasEast", 2);
		hashMap_neighborStatus.put("hasNorthEast", 4);
		hashMap_neighborStatus.put("hasWestOneUp", 8);
		hashMap_neighborStatus.put("hasWestTwoUp", 16);
		hashMap_neighborStatus.put("hasSouthOneUp", 32);
		hashMap_neighborStatus.put("hasSouthTwoUp", 64);
		hashMap_neighborStatus.put("hasEastBottomHalf", 128);
		hashMap_neighborStatus.put("hasEastTopHalf", 256);
	}


}
