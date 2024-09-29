package com.coseemo.pkmnambra.models;

public class TileMap {
    private int width, height;
    private Tile[][] tileMap;
    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;

        tileMap = new Tile[width][height];

        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
                if(Math.random() > 0.5d){
                    tileMap[x][y] = new Tile(TERRAIN.SAND_1);
                }else{
                    tileMap[x][y] = new Tile(TERRAIN.SAND_2);
                }
            }
        }
    }
    public Tile getTile(int targetX, int targetY){
        if(targetX >= 0 && targetX < width && targetY >= 0 && targetY < height) {
            return tileMap[targetX][targetY];
        } else {
            return null;
        }
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
