package mahjong_game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class CharacterTile extends Tile{


		protected char symbol;
		protected static Hashtable<String, String> hashMap;
		
	
		public CharacterTile(char symbol){
			this.symbol = symbol;
			setToolTipText(this.toString());
		}
		
		static{
			hashMap = new Hashtable<String, String>();
			hashMap.put("1", "\u4E00");
			hashMap.put("2", "\u4E8C");
			hashMap.put("3", "\u4E09");
			hashMap.put("4", "\u56DB");
			hashMap.put("5", "\u4E94");
			hashMap.put("6", "\u516D");
			hashMap.put("7", "\u4E03");
			hashMap.put("8", "\u516B");
			hashMap.put("9", "\u4E5D");
			hashMap.put("N", "\u5317");//North
			hashMap.put("E", "\u6771");//East
			hashMap.put("W", "\u8997");//West
			hashMap.put("S", "\u5357");//South
			hashMap.put("C", "\u4E2D");//Red
			hashMap.put("F", "\u767C");//Green
			hashMap.put("w", "\u842C");
		}
		
		
		
		
		public boolean matches(Tile other){
			return super.matches(other) && (this.symbol == ((CharacterTile)other).symbol);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

			Font f = g.getFont();
			f = f.deriveFont(Font.BOLD, f.getSize2D()*2);
			Font f_num = f.deriveFont(Font.ITALIC, f.getSize2D()*.5f);
			Font f_wind = f.deriveFont(Font.BOLD, f.getSize2D()*2f);
			
			if (String.valueOf(symbol).matches("[0-9]")){
				drawWan(g2, f);
				g2.drawString(hashMap.get((String.valueOf(symbol)).toString()), 30, 30+SHADOW_SIZE);
				g2.setColor(DARK_RED);
				g2.setFont(f_num);
				g2.drawString(String.valueOf(symbol), 61, 14+SHADOW_SIZE);
			}else{
				g2.setColor(DARK_RED);
				g2.setFont(f_num);
				g2.drawString(String.valueOf(symbol), 58, 14+SHADOW_SIZE);
				Color redOrGreen = null;
				switch (symbol){case 'C': redOrGreen = DARK_RED; break; case 'F': redOrGreen = DARK_GREEN; break;}
				g2.setColor((symbol == 'C' || symbol == 'F') ? redOrGreen : Color.BLACK);
				g2.setFont(f_wind);
				g2.drawString(hashMap.get((String.valueOf(symbol)).toString()), 20, 60+SHADOW_SIZE);
			}
				
		}

		private void drawWan(Graphics2D g2, Font f) {
			g2.setFont(f);
			g2.setColor(DARK_RED);
			g2.drawString(hashMap.get("w"), 32, 62+SHADOW_SIZE);
			g2.setColor(Color.BLACK);
		}
		
		@Override
		public String toString() {//REGEX
			String theString = "";
			if (String.valueOf(symbol).matches("[0-9]")){
				theString = "Character " + symbol;
				
			//might not need the breaks in here?
			}else {
				switch(symbol){
				case 'N':
					theString = "North Wind";	
					break;
				case 'E':
					theString = "East Wind";
					break;
				case 'W':
					theString = "West Wind";
					break;
				case 'S':
					theString = "South Wind";
					break;
				case 'C':
					theString = "Red Dragon";
					break;
				case 'F':
					theString = "Green Dragon";
					break;
				}
			}
			return theString;
		}

	public static void main(String[] args) {
		
		JFrame		frame = new JFrame();
		JPanel		tiles = new JPanel();
		JScrollPane	scroller = new JScrollPane(tiles);
		tiles.setPreferredSize(new Dimension(500,500));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Character Tiles");
		frame.add(scroller);
		tiles.setLayout(null);
		tiles.setBackground(Color.DARK_GRAY.darker().darker());
		// Try something like this if your tiles don't fit on the screen.
		// Replace "tile width" and "tile height" with your values.
		//scroller.setPreferredSize(new Dimension(8 * tile width, 40 + tile height));

		BambooTile b1 = new BambooTile(2);
		b1.setBounds(0, 0, 54+18, 72+18);
		
		BambooTile b2 = new BambooTile(3);
		b2.setBounds(54, 0, (54+18), 72+18);
		
		BambooTile b3 = new BambooTile(4);
		b3.setBounds(0, 72, 54+18, 72+18);
		
		BambooTile b4 = new BambooTile(5);
		b4.setBounds(54, 72, (54+18), 72+18);
		
		BambooTile b5 = new BambooTile(6);
		b5.setBounds(27+25, 18, (54+18), 72+18);
		
		BambooTile b8 = new BambooTile(8);
		b8.setBounds(54+18, 72-18, (54+18), 72+18);
		
		BambooTile b7 = new BambooTile(7);
		b7.setBounds(18, 72-18, (54+18), 72+18);

		tiles.add(b5);
		tiles.add(b7);
		tiles.add(b8);
		tiles.add(b3);
		tiles.add(b4);
		tiles.add(b1);
		tiles.add(b2);

		
		
		tiles.add(new CharacterTile('2'));
		tiles.add(new CharacterTile('3'));
		tiles.add(new CharacterTile('4'));
		tiles.add(new CharacterTile('5'));
		tiles.add(new CharacterTile('6'));
		tiles.add(new CharacterTile('7'));
		tiles.add(new CharacterTile('8'));
		tiles.add(new CharacterTile('9'));
		tiles.add(new CharacterTile('N'));
		tiles.add(new CharacterTile('E'));
		tiles.add(new CharacterTile('W'));
		tiles.add(new CharacterTile('S'));
		tiles.add(new CharacterTile('C'));
		tiles.add(new CharacterTile('F'));

		frame.pack();
		frame.setVisible(true);

	}

}