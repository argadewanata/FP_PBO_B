// Import library
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

	// Create an array to store snake length with maximum length is 750
	private int[] snakeXlength = new int[750];
	private int[] snakeYlength = new int[750];
	
	// Create variables to control the snake with default values is false
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	
	// Create ImageIcon variables to store all snake head directions
	private ImageIcon headRight;
	private ImageIcon headLeft;
	private ImageIcon headUp;
	private ImageIcon headDown;
	
	// Create ImageIcon variable to store tail
	private ImageIcon tail;
	
	// Create variable to initialize the length of snake
	private int lengthofsnake = 3;
	
	// Create timer and delay variable to control snake speed
	private Timer timer;
	private int delay = 100;
	
	// Create variables to store moves value, score value, and high score value
	private int moves = 0;
	private int score = 0;
	private String highScore = "";
	
	// Create ImageIcon variable to store fruitimage
	private ImageIcon fruitimage;
	
	// Create array to store all possible positions that fruit will appear 
	private int [] fruitXpos = {25,50,75,100,125,150,175,200,225,250,275,300,325,
								350,375,400,425,450,475,500,525,550,575,600,
								625,650,675,700,725,750,775,800,825,850};
	
	private int [] fruitYpos = {75,100,125,150,175,200,225,250,275,300,325,350,
								375,400,425,450,475,500,525,550,575,600,625};
	
	// Generate a random fruit position that comes from all the possibilities
	private Random random = new Random();
	private int xpos = random.nextInt(34);
	private int ypos = random.nextInt(23);
	 
	// Create  ImageIcon variable called "titleImage"
	private ImageIcon titleImage;
	
	// SFX
	private Sound soundEat = new Sound();
	private Sound soundGameplay = new Sound();
	private Sound soundGameOver = new Sound();
	
	public GamePlay() {
		// Add the components that being listened to key events
		addKeyListener(this);
		// Enabling view's focus event on keypad mode 
		setFocusable(true);
		
		// Create timer object and start it
		timer = new Timer(delay, this);
		timer.start();
		
		// Play sound for gameplay
		soundGameplay.setFile("Music_gameplay.wav");
		soundGameplay.playMusic(0.5);
	}

	public void paint(Graphics g) {
		
		// Default snake position
		if (moves == 0)
		{
			snakeXlength[2] = 50;
			snakeXlength[1] = 75;
			snakeXlength[0] = 100;

			snakeYlength[2] = 100;
			snakeYlength[1] = 100;
			snakeYlength[0] = 100;
			
		}
		
		if (highScore.equals(""))
		{
			// Initialize the highscore
			highScore = this.getHighScore();
		}
		
		// Draw title image
		titleImage = new ImageIcon("title.png");
		titleImage.paintIcon(this, g, 25, 5);
		
	    // Draw border for gameplay
		g.setColor(Color.DARK_GRAY);
		g.drawRect(24, 74, 851, 577);
		
		// Set Background for the gameplay
		g.setColor(Color.black);
		g.fillRect(25,75,850,575);
		
		// Draw Score
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.PLAIN, 14));
		g.drawString("Scores: " + score, 780, 30);
		
		// Draw snake length
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.PLAIN, 14));
		g.drawString("Length: " + lengthofsnake, 780, 50);
		
		// Import default snake position with head facing right
		headRight = new ImageIcon("Easy_headRight.png");
		headRight.paintIcon(this,g , snakeXlength[0], snakeYlength[0]);
		
		//  Import different image if different button is pressed
		for(int i = 0; i < lengthofsnake; i++)
		{
			// If right keypad button is pressed
			if(i==0 && right)
			{
				headRight = new ImageIcon("Easy_headRight.png");
				headRight.paintIcon(this,g , snakeXlength[i], snakeYlength[i]);
				
			}
			// If left keypad button is pressed 
			else if(i==0 && left)
			{
				headLeft = new ImageIcon("Easy_headLeft.png");
				headLeft.paintIcon(this,g , snakeXlength[i], snakeYlength[i]);
				
			}
			// If down keypad button is pressed
			else if(i==0 && down)
			{
				headDown = new ImageIcon("Easy_headDown.png");
				headDown.paintIcon(this,g , snakeXlength[i], snakeYlength[i]);
				
			}
			// If up keypad button is pressed
			else if(i==0 && up)
			{
				headUp = new ImageIcon("Easy_headUp.png");
				headUp.paintIcon(this,g , snakeXlength[i], snakeYlength[i]);
				
			}
			// Import tail image 
			else if(i!=0)
			{
				tail = new ImageIcon("Easy_tail.png");
				tail.paintIcon(this,g , snakeXlength[i], snakeYlength[i]);

			}
		}
		
		// Import fruit image
		fruitimage = new ImageIcon("fruit.png");
		
		//  If fruit is eaten by snake then snake will be bigger and fruit will appear somewhere else
		if(fruitXpos[xpos] == snakeXlength[0] && fruitYpos[ypos] == snakeYlength[0])
		{
			// play sound if fruit is eaten
			soundEat.setFile("Music_eat.wav");
			soundEat.play();	
			score = score + 5;
			lengthofsnake++;
			xpos  = random.nextInt(34);
			ypos  = random.nextInt(23);
		}
		
		// Paint the fruit image
		fruitimage.paintIcon(this,g,fruitXpos[xpos], fruitYpos[ypos]);
		
		for(int i = 1; i<lengthofsnake; i++)
		{
			// If snake head hit his own tail
			if(snakeXlength[i] == snakeXlength[0] && snakeYlength[i] == snakeYlength[0])
			{
				// play sound if gameover
				soundGameOver.setFile("Music_gameover.wav");
				soundGameOver.play();
				right = false;
				left = false;
				up = false;
				down = false;
				
				CheckScore();
				// Display game over message and final score
				g.setColor(Color.RED);
				g.setFont(new Font("arial", Font.BOLD, 40));
				g.drawString("Game Over! Score: " + score, 250, 300);
				
				// Display high Score 
				g.setColor(Color.WHITE);
				g.setFont(new Font("arial",Font.BOLD,20));
				g.drawString("HighScore: " + highScore,360,250);
				
				// Display press enter to restart the game
				g.setColor(Color.WHITE);
				g.setFont(new Font("arial", Font.BOLD, 20));
				g.drawString("Press Enter to restart", 350, 340);
				
			}
		}
		
		// Destroy the JFrame
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		timer.restart();
		
		// Snake move right 
		if(right) 
		{
			for(int n = lengthofsnake-1; n>=0;n--)
			{
				snakeYlength[n+1] = snakeYlength[n];
			}
			for(int n = lengthofsnake; n>=0; n--) 
			{
				if (n==0) 
				{
					// Snake move 25 pixel to the right 
					snakeXlength[n] = snakeXlength[n]+25;
				}
				else 
				{
					snakeXlength[n] = snakeXlength[n-1];
				}
				// If snake reach right border then snake will appear in left border
				if(snakeXlength[n] >850)
				{
					snakeXlength[n] = 25;
				}
				
			}
			repaint();
		}
		
		// Snake move left
		if(left) 
		{
			for(int n = lengthofsnake-1; n>=0;n--)
			{
				snakeYlength[n+1] = snakeYlength[n];
			}
			for(int n = lengthofsnake; n>=0; n--) 
			{
				if (n==0) 
				{
					// Snake move 25 pixel to the left
					snakeXlength[n] = snakeXlength[n]-25;
				}
				else 
				{
					snakeXlength[n] = snakeXlength[n-1];
				}
				// If snake reach left border then snake will appear in right border
				if(snakeXlength[n] < 25)
				{
					snakeXlength[n] = 850;
				}
				
			}
			repaint();			
			
		}
		
		// Snake move up
		if(up) 
		{
			for(int n = lengthofsnake-1; n>=0;n--)
			{
				snakeXlength[n+1] = snakeXlength[n];
			}
			for(int n = lengthofsnake; n>=0; n--) 
			{
				if (n==0) 
				{
					// Snake move 25 pixel to up
					snakeYlength[n] = snakeYlength[n]-25;
				}
				else 
				{
					snakeYlength[n] = snakeYlength[n-1];
				}
				// If snake reach top border then snake will appear in bottom border
				if(snakeYlength[n] < 75)
				{
					snakeYlength[n] = 625;
				}
				
			}
			repaint();
			
		}
		
		// Snake move down
		if(down) 
		{
			for(int n = lengthofsnake-1; n>=0;n--)
			{
				snakeXlength[n+1] = snakeXlength[n];
			}
			for(int n = lengthofsnake; n>=0; n--) 
			{
				if (n==0) 
				{
					// Snake move 25 pixel to bottom
					snakeYlength[n] = snakeYlength[n]+25;
				}
				else 
				{
					snakeYlength[n] = snakeYlength[n-1];
				}
				// If snake reach bottom border then snake will appear in top border
				if(snakeYlength[n] > 625)
				{
					snakeYlength[n] = 75;
				}
				
			}
			
			repaint();
		}
		
		
	}
	
	// Method to get High Score
	public String getHighScore()
	{
		// Example-> Andi: 200
		FileReader readFile = null;
		BufferedReader reader = null;
		try{
			readFile = new FileReader("highscore.dat");
			reader = new BufferedReader(readFile);
			return reader.readLine();
		}
		catch (Exception e){
			return "Nobody:0";
		}
		finally
		{
			try {
				if(reader != null) reader.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}		
	}
	
	// Method to check if current score is higher than the highscore
	public void CheckScore()
	{
		if (highScore.equals(""))
			return;
		// Because high score value is (name):(score), we split the string to two parts with ":"
		// We need the second part of it so we can compare it to the current score
		if (score > Integer.parseInt((highScore.split(":")[1])))
		{
			// User has set a new record
			String name = JOptionPane.showInputDialog("You set a new highscore. What is your name?");
			highScore = name + ":" + score;
			
			File scoreFile = new File("highscore.dat");
			if (!scoreFile.exists())
			{
				try {
					scoreFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			FileWriter writeFile = null;
			BufferedWriter writer = null;
			try
			{
				writeFile = new FileWriter(scoreFile);
				writer    = new BufferedWriter(writeFile);
				writer.write(this.highScore);
			}
			catch (Exception e)
			{
				// If errors occurs
			}
			finally
			{
				try 
				{
					if (writer != null) 
						writer.close();
				}
				catch (Exception e)
				{
					
				}
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
		// Restart the game if enter button is pressed
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			moves = 0;
			score = 0;
			lengthofsnake=3;
			repaint();
		}
		
		// Snake move right if right keypad button is pressed
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			moves++;
			right = true;
			if(!left) {
				right = true;
			}
			else
			{
				// If the snake was heading to the left then it can't immediately turn right
				right = false;
				left = true;
			}
			
			up = false;
			down = false;
			
		}
		
		// Snake move left if left keypad button is pressed
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			moves++;
			left = true;
			if(!right) {
				left = true;
			}
			else
			{
				// If the snake was heading to the right then it can't immediately turn left
				left = false;
				right = true;
			}
			
			up = false;
			down = false;
		}
		
		// Snake move up if up keypad button is pressed
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			moves++;
			up = true;
			if(!down) {
				up = true;
			}
			else
			{
				// If the snake was heading down then it can't immediately heading up
				up = false;
				down = true;
			}
			
			left = false;
			right = false;
		}
		
		// Snake move down if down keypad button is pressed
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			moves++;
			down = true;
			if(!up) {
				down = true;
			}
			else
			{
				// If the snake was heading up then it can't immediately heading down
				up = true;
				down = false;
			}
			
			left = false;
			right = false;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}