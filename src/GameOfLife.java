import javax.swing.*;        
import java.awt.*;
import java.awt.event.*;

public class GameOfLife extends JFrame implements ActionListener {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;
    public static boolean pause = true;
    int intBoardSize;
    
    JPanel buttonPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton boardSizeDialogButton;
    
    JButton[][] buttonArray;
    
    private boolean[][] world;
    private long generation = 0;    
    
    public GameOfLife() {
        super("Game Of Life");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setLayout(new FlowLayout());
        
        boardSizeDialogButton = new JButton("Board Size");
        boardSizeDialogButton.addActionListener(this);
        buttonPanel.add(boardSizeDialogButton);
        
        JButton startButton = new JButton("Start");
        startButton.addActionListener(this);
        buttonPanel.add(startButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(this);
        buttonPanel.add(stopButton);
        
        add(buttonPanel, BorderLayout.NORTH);
        
        JButton temp = new JButton();
        temp.setBackground(Color.red);
        add(temp,BorderLayout.CENTER);
    }
    
    public void actionPerformed(ActionEvent e){
        String buttonString = e.getActionCommand();
        String boardSize;
        if(buttonString == "Board Size"){
            // Open Dialog
            boardSize = JOptionPane.showInputDialog(this, "Board Size: Max. 50", "50");
            System.out.println("boardSize = " + boardSize);
            // if NOT Cancel
            if(boardSize != null)
            {
                intBoardSize = Integer.parseInt(boardSize);
                if(intBoardSize > 50)
                    return;
                
                boardSizeDialogButton.setVisible(false);
                
                boardPanel.setLayout(new GridLayout(intBoardSize, intBoardSize));
                JButton temp = new JButton();
                temp.setBackground(Color.red);
                //boardPanel.add(temp);
                //add(boardPanel, BorderLayout.CENTER);
                add(temp,BorderLayout.CENTER);

                createNewWorld();
                
                buttonArray = new JButton[intBoardSize][intBoardSize];
                /*
                for(int i = 0; i < intBoardSize; i++) {
                    for(int j = 0; j < intBoardSize; j++) {
                        JButton b =new JButton();
                        buttonArray[i][j] = b;
                        b.setBackground(Color.BLACK);
                        b.setActionCommand(i + "," + j);
                        b.addActionListener(this);
                        boardPanel.add(b);
                    }            
                }
                */

            }
        }
        else if(buttonString == "Start") {
            System.out.println("Start... ");
            pause = false;

        }
        else if(buttonString == "Stop") {
            System.out.println("Stop... ");
            pause = true;
        }
        else {
            String[] parts = buttonString.split(",");
            int i = Integer.parseInt(parts[0]);
            int j = Integer.parseInt(parts[1]);
            Color c = ((JButton)e.getSource()).getBackground();
            if(c != Color.yellow) {
                buttonArray[i][j].setBackground(Color.yellow);
                world[i][j] = true;
            }
            else {
                buttonArray[i][j].setBackground(Color.black);
                world[i][j] = false;
            }
                

            System.out.println("log (i,j): " + buttonString);
        }
    }
    
    private void updateBoard() {
    
        // toggle the visibility will SPEED UP the Update!!!
        boardPanel.setVisible(false);
        for(int i = 0; i < world.length; i++) {
            for(int j = 0; j < world[i].length; j++) {
                JButton b = buttonArray[i][j];
                if(world[i][j])
                    b.setBackground(Color.yellow);
                else
                    b.setBackground(Color.black);
            }            
        }
        boardPanel.setVisible(true);
        
    }
    
    private void createNewWorld(){
            boolean[][] newWorld = new boolean[intBoardSize][intBoardSize];
            
            for(int row = 0; row < newWorld.length; row++ ){
                    for(int col = 0; col < newWorld[row].length; col++ ){
                            newWorld[row][col] = false;
                    }
            }
            
            world = newWorld;
    }
    
    // Create the next generation
    public void nextGeneration(){
            boolean[][] newWorld = new boolean[intBoardSize][intBoardSize];
            for(int row = 0; row < newWorld.length; row++ ){
                    for(int col = 0; col < newWorld[row].length; col++ ){
                            newWorld[row][col] = isAlive(row, col);
                    }
            }
            world = newWorld;
            generation++;
            
            updateBoard();
            
            System.out.println("Generation: " + generation);
    }    
    
    /*
        A creature that has two or three neighbors will continue live in the next generation.
        A creature that has more than 3 neighbors will die of overcrowding. Its cell will be empty in the next generation.
        A creature that has less than 2 neighbors will die of loneliness.
        A new creature born in an empty cell that has exactly 3 neighbors.
    */
    private boolean isAlive(int row, int col){
            int neighborsCount = 0;
            boolean cellIsAlive = world[row][col];

            for(int i = -1; i <= 1; i++){
                    int curRow = row + i;
                    curRow = (curRow < 0)? intBoardSize - 1: curRow;
                    curRow = (curRow >= intBoardSize)? 0 : curRow;
                    for(int j = -1; j <= 1; j++){
                            int curCol = col + j;
                            curCol = (curCol < 0)? intBoardSize - 1: curCol;
                            curCol = (curCol >= intBoardSize)? 0 : curCol;
                            if(world[curRow][curCol]){
                                    neighborsCount++;
                            }
                    }
            }

            if(cellIsAlive){
                    neighborsCount--;
            }

            // Rules
            if(neighborsCount == 2 && cellIsAlive){
                    return true;
            } else if(neighborsCount == 3){
                    return true;
            } else {
                    return false;
            }
    }    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        GameOfLife gui = new GameOfLife();
        gui.setVisible(true);
        
        while(true){
            if(!pause)
                gui.nextGeneration();
            Thread.sleep(1000);
        }        
    }
    
}