package data;

import org.newdawn.slick.opengl.Texture;

import static helpers.Artist.*;
import static helpers.Clock.*;

import java.util.ArrayList;

public class Enemy {
	private int width, height, health, currentCheckpoint;
	private float speed, x, y;
	private Texture texture;
	private Tile startTile;
	private boolean first = true, alive = true;
	private TileGrid grid;
	
	private ArrayList<Checkpoint> checkpoints;
	private int[] directions;
	
	
	public Enemy(Texture texture, Tile startTile, TileGrid grid, int width, int height, float speed, int health){
		this.texture = texture;
		this.startTile = startTile;
		this.x = startTile.getX();
		this.y = startTile.getY();
		this.width = width;
		this.height = height;
		this.speed = speed;
		this.health = health;
		this.grid = grid;
		
		this.checkpoints = new ArrayList<Checkpoint>();
		this.directions = new int[2];
		this.directions[0] = 0; //x direction
		this.directions[1] = 0; //y direction
		directions = FindNextD(startTile);
		this.currentCheckpoint = 0;
		PopulateCheckpointList();
	}
	
	public void update(){
		if(first)
			first = false;
		else{
			if(CheckpointReached()){
				if(currentCheckpoint + 1 == checkpoints.size())
					Die();
				else
					currentCheckpoint++;
			}else{
				x += Delta() * checkpoints.get(currentCheckpoint).getXDirection() * speed;
				y += Delta() * checkpoints.get(currentCheckpoint).getYDirection() * speed;
			}
		}
	}
	
	private boolean CheckpointReached(){
		boolean reached = false;
		Tile t = checkpoints.get(currentCheckpoint).getTile();
		if(x > t.getX() - 3 && x < t.getX() + 3 && y > t.getY() - 3 && y < t.getY() + 3){
			reached = true;
			x = t.getX();
			y = t.getY();
		}
		
		return reached;
	}
	
	private void PopulateCheckpointList(){
		checkpoints.add(FindNextC(startTile, directions = FindNextD(startTile)));
		
		int counter = 0;
		boolean cont = true;
		while(cont){
			int[] currentD = FindNextD(checkpoints.get(counter).getTile());
			if(currentD[0] == 2 || counter == 20){
				cont = false;
			}else{
				checkpoints.add(FindNextC(checkpoints.get(counter).getTile(), directions = FindNextD(checkpoints.get(counter).getTile())));
			}
			counter++;
		}
	}
	
	private Checkpoint FindNextC(Tile s, int[] dir){
		Tile next = null;
		Checkpoint c = null;
		
		boolean found = false;
		int counter = 1;
		
		while(!found){
			int x = s.getXPlace() + dir[0] * counter;
			int y = s.getYPlace() + dir[1] * counter;
			if(x == grid.getTilesWide() || y == grid.getTilesHigh() || s.getType() != grid.getTile(x, y).getType()){
				found = true;
				counter -= 1;
				next = grid.getTile(s.getXPlace() + dir[0] * counter, s.getYPlace() + dir[1] * counter);
			}
			
			counter++;
		}
		
		c = new Checkpoint(next, dir[0], dir[1]);
		return c;
	}
	
	private int[] FindNextD(Tile s){
		int[] dir = new int[2];
		Tile u = grid.getTile(s.getXPlace(), s.getYPlace() - 1);
		Tile r = grid.getTile(s.getXPlace() + 1, s.getYPlace());
		Tile d = grid.getTile(s.getXPlace(), s.getYPlace() + 1);
		Tile l = grid.getTile(s.getXPlace() - 1, s.getYPlace());
		
		if(s.getType() == u.getType() && directions[1] != 1){
			dir[0] = 0;
			dir[1] = -1;
		}else if(s.getType() == r.getType() && directions[0] != -1){
			dir[0] = 1;
			dir[1] = 0;
		}else if(s.getType() == d.getType() && directions[1] != -1){
			dir[0] = 0;
			dir[1] = 1;
		}else if(s.getType() == l.getType() && directions[0] != 1){
			dir[0] = -1;
			dir[1] = 0;
		}else{
			dir[0] = 2;
			dir[1] = 2;
		}
		
		return dir;
	}
	
	public void damage(int amount){
		health -= amount;
		if(health <= 0)
			Die();
	}
	
	private void Die(){
		alive = false;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void Draw(){
		DrawQuadTex(texture, x, y, width, height);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Tile getStartTile() {
		return startTile;
	}

	public void setStartTile(Tile startTile) {
		this.startTile = startTile;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}
	
	public TileGrid getTileGrid(){
		return grid;
	}
	
	
}
