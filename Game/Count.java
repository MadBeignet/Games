package Game;



public class Count {

    private int count;
    boolean inPlay;

    public Count() {
        count = 0;
        inPlay = true;
    }

    public void updateCount() {
        count++;
    }
    public int getCount() {
        return count;
    }
    
}
