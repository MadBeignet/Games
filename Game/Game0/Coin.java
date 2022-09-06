package Game0;

public class Coin extends Parent {
    
    // image that represents the coin's position on the board
    // current position of the coin on the board grid

    private boolean timeOut;
    private Count count;
    private int value = 100;
    private boolean isSpecial = false;

    public Coin(int x, int y) {
        super("Game/images/coin.png",x,y);
        // load the assets
        count = new Count();
        // initialize the state
        value = 100;
        timeOut = false;
    }
    public Coin(int x, int y, String f, Count c) {
        super(f, x, y);
        count = c;
        timeOut = false;

    }
    public int getValue() {
        return value;
    }
    public void makeSpecial() {
        isSpecial = true;
        value = 500;
        setFileName("Game/images/gold_coin.png");
    }
    public boolean isSpecial() {
        return isSpecial;
    }
    public void updateCount() {
        count.updateCount();
        if (count.getCount() >= 400)
            timeOut = true;
        //System.out.print(timeOut ? "Coin removed.\n" : "");
    }

    public boolean getTimeOut() {
        return timeOut;
    }


}