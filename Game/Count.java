package Game;

public class Count {

    private int count;
    private boolean isSpecial = false;
    private int maxCoolDown = 100;


    public Count() {
        count = 0;
    }
    public void updateCount() {
        count++;
    }
    public int getCount() {
        return count;
    }
    public boolean getSpecial() {
        return isSpecial;
    }
    public void makeSpecial() {
        isSpecial = true;
        maxCoolDown = 200;
    }
    
}
