package src.entities;

public class Player {
    private int x, y;
     private final int speed = 10;

     public Player(int starX, int startY){
        this.x = starX;
        this.y = startY;
     }

     public int gerX() {return x;}
     public int gerY() {return y;}

     public void move (int dx,int dy){
        x += dx * speed;
        y += dy * speed;
     }
}
