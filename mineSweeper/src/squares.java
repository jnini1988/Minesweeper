public class squares{
    private String squareType;
    private boolean hidden;
    private boolean flag;

    //Squares will always be hidden and not flagged when game starts
    //String squareType is entered through the random board generator in the main class
    //when square is hidden, it will be printed as ?
    public squares() {
        hidden = true;
        flag = false;
        squareType = "[ ]"; //can be flag, blank, revealed
    }

    public boolean getHidden() {
        return hidden;
    }

    public void changeHidden(){
        hidden=false;
    }
    
    public void changeback() {
    	hidden=true;
    }

    public boolean getFlag() {
        return flag;
    }

    public void changeFlag(){
       flag = !flag;
    }

    public String getSquareType() {
        return squareType;
    }

    public void setSquareType(String symbol) {
        squareType=symbol;
    }
}