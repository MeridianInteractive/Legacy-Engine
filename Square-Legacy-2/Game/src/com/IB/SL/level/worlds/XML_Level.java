package com.IB.SL.level.worlds;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.IB.SL.Boot;
import com.IB.SL.entity.mob.Player;
import com.IB.SL.level.Level;
import com.IB.SL.level.TileCoord;
import com.IB.SL.level.interactables.Location_Shrine;

public class XML_Level extends Level{
		
	public static boolean spawnASM = false;

	protected static java.util.Random Random = new Random();
	public static java.util.Random random = Random;

	public ArrayList<LevelExit> exits;
	
	public String XML_String = "";
	public String name = "";
	public int id = -1;
	
	public XML_Level(String path) {
		super(path, true);
	}
	
	public void LoadXML(String XML) {
		readXML(XML);
	}
	
	public void readXML(String path) {
		this.XML_String = path;
		try {
		InputStream fXmlFile = getClass().getResourceAsStream(path);
		DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFac.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		
		System.out.println("ROOT: " + doc.getDocumentElement().getNodeName());
		initLevel(doc);
		initExits(doc);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void initLevel(Document doc) {
		NodeList nList = doc.getElementsByTagName("props");
		System.out.println("----------------------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				try {
					this.id = Integer.parseInt(eElement.getAttribute("id"));
					this.name = (eElement.getElementsByTagName("name").item(0).getTextContent());
				} catch (Exception e) {
				}
			}
		}
	}
	
	public void initExits(Document doc) {
		exits = new ArrayList<LevelExit>();
		NodeList nList = doc.getElementsByTagName("exits");
		System.out.println("--------------EXITS--------------");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			System.err.println("PRE CHECK");
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				for (int i = 0; i < (((Element)nNode).getElementsByTagName("exit").getLength()); i++) {
				Element eElement = (Element) ((Element)nNode).getElementsByTagName("exit").item(i);
					System.out.println(Integer.parseInt(eElement.getAttribute("x")));
					this.exits.add(new LevelExit(eElement));
				}
			}
		}
	}
	
	
	protected void loadLevel(String path) {
		minimap_enabled  = true;
		SpawnList.clear();
		
		SpawnTime_MOD = 150;
		
		this.tiles = readTiles(path + "/level.png");
		this.overlayTiles = readTiles(path + "/overlay.png");
		this.torchTiles = readTiles(path + "/overlay.png");
		
		add(new Location_Shrine(672, 227, new TileCoord(673, 228)));
	} 
	
	public int[] readTiles(String path) {
		int[] tiles = null;
		try {
			BufferedImage image = ImageIO.read(XML_Level.class.getResource(path));
			int w = width = image.getWidth();
			int h = height = image.getHeight();

			tiles = new int[w * h];
			image.getRGB(0, 0, w, h, tiles, 0, w);
		}catch (IOException e) {	
			e.printStackTrace();
			Boot.log("IOException! Failed To Load Level File!", "XML_Level.java", true);
		}
		return tiles;
	}
	
	public void checkExits(Player player, Level level, int x, int y) {
		refresh();
		
		System.err.println(exits.size());
		
		for (int i = 0; i < exits.size(); i++) {
			LevelExit exit = exits.get(i);
			System.out.println(exit);
			if (x >= exit.xo && x <= (exit.xo + exit.w)) {
				if (y >= exit.yo && y <= (exit.yo + exit.h)) {
					player.setPosition(exit.tx, exit.ty, Maps.spawnHavenId, true);
				}
			}
		}
	}
}
	


