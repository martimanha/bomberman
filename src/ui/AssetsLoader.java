package src.ui;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class AssetsLoader{
    private Image playerImage;
    private Image enemyImage;
    private Image bombImage;
    private Image explosionImage;
    private Image powerUpImage;
    private Image wallImage;
    private Image breakableBlockImage;

    public AssetsLoader(){
        loadImages();
    } private void loadImages(){
        playerImage = loadImage("/entities/player.png");
    } private Image loadImage(String path){
        URL imageUrl = getClass().getResource(path);
        if(imageUrl != null){
            return new ImageIcon(imageUrl).getImage();
        } System.err.println("Erro ao carregar imagem " + path);
        return null;
    }

    public Image getPlayerImage(){ 
        return playerImage; 
    } 
    
    public Image getEnemyImage(){
        return enemyImage;
    } 
    
    public Image explosionImage(){
        return explosionImage;
    }
    
    public Image getBombImage(){
        return bombImage;
    }
    
    public Image getPowerUpImage(){
        return powerUpImage;
    }
    
    public Image getWallImage(){
        return wallImage;
    }
    
    public Image getBreakableBlockImage(){ 
        return breakableBlockImage;
    }
}