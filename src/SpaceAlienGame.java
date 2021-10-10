/*
 Java, JavaSE-1.8
==============================================================================================================================================================
Problem Definition 	� Write a program that randomly generates aliens on a Cartesian plane and alien locations never repeat in a game. The user must enter the
coordinates of the alien as fast as possible. Correct answers get points and answering speed can get points depending on the user's response time. Users must 
to able to see their current scores and highscores during the game as well as review all the alien locations once the game time is up. Scores must be 
automatically updated to a highscore board and the user should have an option to save their highscores to a text file or upload a text file with highscores
from a previous session.

Input 		- User input required determine their username, quit the game, view the rules of the game, view the leaderboards, start the game, enter their
x and y coordinates for the alien, submitting their final answer for the x y coordinates of the alien, returning to the menu, creating a new highscore file
in a user specified location, retrieving a previous highscore file and saving the current highscores to a file selected.

Output	 	- The program outputs a user friendly menu that allows the user to quit, see rules, see highscores, start a game, or change their username. Program
can display the rules to the game and the current highscore standings. If a game is started, the program displays a grid with a single alien, text fields for the user
to enter their answer of the x y coordinates of the alien displayed and a button to submit their answer. Once an answer is submitted, the program displays message 
that tells the user if their answer was correct or not. Based on the correctness, the program also displays the user's current points and stats 
such as the number of aliens missed, caught, percentage accuracy, average time for a correct catch. Once the game time is up, the program displays the user's stats 
containing the same details mentioned earlier. It also displays a grid containing all the alien locations for that game.

Process 	- Determines if the user wants to go to the rules page, start a game, view the highscores, enter a new username, or quit the game. Randomly generates
aliens on a Cartesian Plane and makes sure not to repeat it within the same game. Program determines if user input of x y coordinates is correct and incorrect and
awards points accordingly, updating the number of aliens spawned, caught, percentage accuracy, average time for a correct catch, and points. At the same time, 
the program keeps track of how much game time is left. If game time runs out, the program ends the game and moves onto user stats and alien location review. 
Program also keeps track of how long each alien has been on the game grid. If the user takes too long to answer, the program gives a new alien and counts the 
previous as a missed/incorrect answer. Once the game ends, the program determines if the user's current score is higher than any of the current highscores. If so,
the program inserts the user's score and username into the appropriate location and shifts the consecutive scores down one spot, deleting the 10th place highscore.
On the highscore page, the program determines if the user want to create, retrieve, or save the current highscores. If the user wants to create a new file, the
program allows the user to choose a folder location and creates a text file with the current highscores. If the user wants to retrieve scores, the program lets
the user choose a file and checks if the file is compatible/acceptable and responds accordingly to unsupported file types. If accepted, the overwrites the current
highscores (if there are any) with the ones on the retrieved text file. If the user wants to save the current scores and a text file has been selected, the program 
overwrites the file chosen and replaces it with the current standings. If no file has been selected, the program cannot save and displays an error message.
==============================================================================================================================================================

 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

@SuppressWarnings("serial")
public class SpaceAlienGame extends JFrame{
	public String username="Guest";
	public DecimalFormat df = new DecimalFormat("##.##");
	public boolean alien[][]=new boolean [21][21];
	public int alienX;
	public int alienY;
	public double gameTime;
	public double catchTime;
	public double totalCatchTime;
	public int aliensCaught;
	public int aliensSpawned;
	public String userX;
	public String userY;
	public double points;
	public String hitOrMiss;
	public static String highscoreArray[][]=new String [10][2];
	public static final int usr=0;
	public static final int scr=1;
	public final int gridX=250;
	public final int gridY=310;
	public final int boxSize=15;
	public java.io.File fileName;

	/** main method
	 * This procedural method is called automatically and is used to call the constructor that sets up the window of the game
	 * and then sets the highscore array scores and users to 0 and "Guest"
	 * 
	 * List of Local Variables
	 * w - the constructor SAG_IvanYu which creates a JFrame for the game.
	 * 
	 * @param args <type String>
	 * @return void
	 */
	public static void main(String[] args) {
		SpaceAlienGame w = new SpaceAlienGame();
		w.setVisible(true);
		
		for(byte i=0;i<10;i++) {
			if (highscoreArray[i][scr]==null) {
				highscoreArray[i][scr]=("0.00");
				highscoreArray[i][usr]=("Guest");
			}
		}
	}//end main method

	public SpaceAlienGame() {
		super("Ivan's Space Alien Game");
		setSize(500,700);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		Container content = getContentPane();
		content.setLayout(null);
		content.removeAll();
		content.add(menuPanel());
		content.revalidate();
		content.repaint();
	}

	/**menuPanel method
	 * This procedural method and calls other methods and components to make the menu page for the game. It:
	 * 1) Calls the labelMaker method twice to create the game title and a prompt to the user to enter their username.
	 * 2) Creates and adds a text field called usernameField for the user to type their username.
	 * 3) Calls the buttonMaker method three times to make actionless buttons, called rulesButton, playButton, and highscoreButton.
	 * 4) Creates ActionListeners for the buttons so that each button brings the user to their respective page when clicked.
	 * 5) Adds the three buttons to the frame.
	 * 6) Calls the buttonMaker method, and gives it an ActionListener so that button closes the program when the user clicks it.
	 * 7) Adds the private class MenuAlien to make the menu page look cooler with an alien in the background.
	 * 
	 * List of local Variables
	 * menu - the container that will hold all of the components of the menu page <type JPanel>
	 * usernameField - the area for the user to enter their username <type JTextField>
	 * rulesButton - button that brings the user to the how to play page <type JButton>
	 * playButton - button that lets the user start the the game <type JButton>
	 * highscoreButton - button that brings the user to the highscore board <type JButton>
	 * quitButton - button that closes the program <type JButton>
	 * 
	 * @param	none
	 * @return 	menu - the container that will hold all of the components of the menu page <type JPanel>
	 */
	public JPanel menuPanel() {
		JPanel menu= panelMaker(true);

		JTextField usernameField = new JTextField(username);
		usernameField.setBounds(175,395,150,20);
		menu.add(usernameField);

		menu.add(labelMaker("Ivan's Space Alien Game",30,0,80,500,70,true));
		menu.add(labelMaker("Enter your username (10 characters)",12,0,370,500,20,true));

		JButton rulesButton = buttonMaker("How To Play",40,500,120,50);
		JButton playButton = buttonMaker("Play",190,500,120,50);
		JButton highscoreButton =buttonMaker("Highscores",340,500,120,50);
		
		rulesButton.addActionListener(new ActionListener() {
			/**actionPerformed method
			 * 
			 * This functional method removes everything on the menu panel, calls and adds
			 * the rulesPanel method, revalidates, and repaints when the button
			 * rulesButton is clicked by the user
			 * 
			 * List of Local Variables
			 * n/a
			 * 
			 * @param 	e - the tasks to run if the action of pressing the button is performed <type ActionEvent>
			 * @return 	void
			 */
			public void actionPerformed(ActionEvent e) {
				menu.removeAll();
				menu.add(rulesPanel());
				menu.revalidate();
				menu.repaint();
			}//end actionPerformed method
		});

		playButton.addActionListener(new ActionListener() {
			/**actionPerformed method
			 * 
			 * This functional method runs a set of tasks when the button rulesButton is clicked
			 * This method will get the username that is currently typed into the text field and
			 * shorten it to 10 characters if it is longer. It resets the number of aliens caught
			 * and spawned, the points, and both the time variables to zero and sets the game time
			 * to 30. It also resets the boolean array used to track alien locations. Finally, it
			 * removes everything on the menu panel, calls and adds the gamePanel method,
			 * revalidates and repaints.
			 * 
			 * List of Local Variables
			 * n/a
			 * 
			 * @param 	e - the tasks to run if the action of pressing the button is performed <type ActionEvent>
			 * @return 	void
			 */
			public void actionPerformed(ActionEvent e) {
				username = usernameField.getText();
				if (username.length() > 10) 
					username = username.substring(0, 10);
				aliensCaught=0;
				aliensSpawned=0;
				points=0;
				totalCatchTime=0;
				gameTime=10;
				for (int x=0;x<21;x++) {
					for (int y=0;y<21;y++) {
						alien[x][y]=false;
					}
				}
				menu.removeAll();
				menu.add(gamePanel());
				menu.revalidate();
				menu.repaint();
			}//end actionPerformed method
		});
		
		highscoreButton.addActionListener(new ActionListener() {
			/**actionPerformed method
			 * 
			 * This functional method removes everything on the menu panel, calls and adds
			 * the highscorePanel method, revalidates, and repaints when the button
			 * highscoreButton is clicked by the user
			 * 
			 * List of Local Variables
			 * n/a
			 * 
			 * @param 	e - the tasks to run if the action of pressing the button is performed <type ActionEvent>
			 * @return 	void
			 */
			public void actionPerformed(ActionEvent e) {
				menu.removeAll();
				menu.add(highscorePanel());
				menu.revalidate();
				menu.repaint();
			}//end actionPerformed method
		});
		
		menu.add(rulesButton);
		menu.add(playButton);
		menu.add(highscoreButton);

		JButton endButton = buttonMaker("Quit",390,10,100,20);
		endButton.addActionListener(new ActionListener() {
			/**actionPerformed method
			 * 
			 * This functional method closes the program when the button quitButtonis clicked
			 * 
			 * List of Local Variables
			 * n/a
			 * 
			 * @param 	e - the tasks to run if the action of pressing the button is performed <type ActionEvent>
			 * @return 	void
			 */
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}//end actionPerformed method
		});
		menu.add(endButton);
		
		menu.add(new MenuAlien());
		return menu;
	}//end menuPanel method

	/**gamePanel method
	 *  This procedural method displays a the game when called. First, the panelMaker method is called to make a new
	 *  panel called game. The labelMaker method is then called to create labels for the game title, stats (aliens caught,
	 *  missed, average time, current highscore, points, and percentage accuracy), a set of brackets to surround the coordinate
	 *  textfields as well as a label that displays if the user was correct or incorrect and their time bonus if any. Next, two
	 *  labels are added that display the time left for the game and the time left before the alien disappears. Two textfields are
	 *  added for the user to enter their x y coordinate answer. The variables correct, answered, and catchTime are set to false,
	 *  false, and 10 respectively. The fire button is added, which determines if the user is correct/incorrect and carries 
	 *  out tasks from accordingly. Lastly, the Alien class is called and variables
	 * 
	 * List of Local Variables
	 * game - the container used to hold components of the game page <type JPanel>
	 * invasionTimerLabel - text that displays the time left before the game ends <type JLabel>
	 * alienTimerLabel - text that displays the time left before the current alien disappears <type JLabel>
	 * xField - the text field for the user to enter the x coordinate of the alien <type JTextField>
	 * yField - the text field for the user to enter the y coordinate of the alien <type JTextField>
	 * gameTimer - the timer that updates the invasionTimerLabel and determines if the user has run out of game time <type Timer>
	 * gameTimerTask - the action that will be performed every time the gameTimer ticks <type TimerTask>
	 * fireButton - the button that the user clicks when they have typed their answer into the text fields <type JButton>
	 * 
	 * @param	none
	 * @return	game - the container used to hold the components that make up the game page <type JPanel>
	 */
	public JPanel gamePanel() {		
		JPanel game = panelMaker(true);
		game.add(labelMaker("Ivan's Space Alien Game",15,0,25,500,20,true));

		JLabel invasionTimerLabel = new JLabel();
		invasionTimerLabel.setFont(new Font("", Font.BOLD, 13));
		invasionTimerLabel.setBounds(50,70,200,20);
		game.add(invasionTimerLabel);

		JLabel alienTimerLabel= new JLabel();
		alienTimerLabel.setFont(new Font("", Font.BOLD, 13));
		alienTimerLabel.setBounds(300,70,200,20);
		game.add(alienTimerLabel);

		game.add(labelMaker("<html>Aliens caught: "+aliensCaught
				+"<br/> Aliens missed: "+(aliensSpawned-aliensCaught)
				+"<br/> Average time for a catch: "+df.format(totalCatchTime/(double)aliensCaught)+" sec"
				+"<html>",13,50,90,250,60,false));

		game.add(labelMaker("<html>Current highscore: "+highscoreArray[0][scr]+" pts"
				+"<br/>Points: "+df.format(points)+" pts"
				+"<br/>Percentage accuracy: "+df.format((double)aliensCaught*100/(double)aliensSpawned)+"%"
				+"<html>",13,300,90,250,60,false));

		game.add(labelMaker("(         ,         )",30,0,495,500,30,true));
		game.add(labelMaker(hitOrMiss,20,0,470,500,30,true));

		JTextField xField = new JTextField();
		xField.setBounds(185, 500, 50, 25);
		game.add(xField);

		JTextField yField = new JTextField();
		yField.setBounds(265, 500, 50, 25);
		game.add(yField);

		catchTime=10;

		Timer gameTimer = new Timer();
		TimerTask gameTimerTask = new TimerTask(){
			/**run Method
			 * 
			 * This functional method runs every time the gameTimer calls it. It will subtract 0.1 from the gameTime variable
			 * and update the invasionTimerLabel with the new time. It will subtract 0.1 from the catchTime variable
			 * and update the alienTimerLabel with the new time left for the current alien.
			 * 
			 * If gameTime hits 0, the program will check if the user beat the highscore and update the highscore board
			 * accordingly and update the file selected if there is one selected. Finally, it will remove the game panel
			 * and then call and add the postgamePanel method.
			 * 
			 * If catchTime hits 0, the program will count the alien as a miss and record it in the alien array as it has 
			 * appeared, increased aliensSpawned by 1. Then it will remove the game panel and call and add a new gamePanel
			 * method.
			 * 
			 * List of Local Variables
			 * outputStream - the object used to edit the highscore text file selected (if there is one) <type PrintWriter>
			 * 
			 * @param	none
			 * @return	void
			 */
			public void run(){
				gameTime-=0.1;
				catchTime-=0.1;
				invasionTimerLabel.setText("Invasion ends in: "+df.format(gameTime)+" sec");
				alienTimerLabel.setText("Alien lands in: "+df.format(catchTime)+" sec");
				
				if  (gameTime<=0){
					for (int i=0;i<10;i++) {
						if (points>=(Double.parseDouble(highscoreArray[i][1]))) {
							for (int j=9;j>=i+1;j--) {
								highscoreArray[j][usr]=highscoreArray[j-1][usr];
								highscoreArray[j][scr]=highscoreArray[j-1][scr];
							}
							highscoreArray[i][usr]=username;
							highscoreArray[i][scr]=""+df.format(points);
							break;
						}
					}
					
					PrintWriter outputStream = null;
					try {
						outputStream = new PrintWriter (new FileOutputStream(fileName));
						for (byte i=0;i<10;i++) {
							outputStream.println(highscoreArray[i][usr]);
							outputStream.println(highscoreArray[i][scr]);
						}
						outputStream.close();
					}
					catch (Exception f) {}
					
					game.removeAll();
					game.add(postgamePanel());
					game.revalidate();
					game.repaint();
					gameTimer.cancel();
				}
				
				if (catchTime<=0) {
					alien[alienY][alienX]=true;
					aliensSpawned++;
					
					gameTimer.cancel();
					game.removeAll();
					game.add(gamePanel());
					game.revalidate();
					game.repaint();
				}
			} //end run method  
		};
		
		gameTimer.schedule(gameTimerTask,0,100);

		do{
			alienX=(int)(Math.random()*21);
			alienY=(int)(Math.random()*21);
		}while(alien[alienY][alienX]==true);

		JButton fireButton = buttonMaker("Fire",200,530,100,50);new JButton("Fire!");
		fireButton.setFont(new Font("", Font.BOLD, 30));
		fireButton.addActionListener(new ActionListener() {
			/**actionPerformed method
			 * 
			 * This function method runs tasks when the user clicks the fireButton. When the user clicks the button, the program
			 * will count the alien's location has having appeared in the alien array, increase aliensSpawned by 1, and make the
			 * boolean answered true as the user has submitted an answer. Then, it will get the user's input from the xField
			 * and yField and remove the text from both text fields. The program compares the user input to the correct current
			 * alien location. If the user is correct, the program counts it as a catch, awards points and time bonus points based
			 * on the value left in the catchTime variable, and sets the hitOrMiss string to tell the user that they are correct
			 * and the time bonus received. If the user's answer is wrong, the hitOrMiss string is set to tell the user they missed.
			 * The gameTimer is canceled and then the game panel is removed and the programs calls and adds the gamePanel method
			 * to generate a new alien as well as allow the variable to update and show on their respective labels
			 * 
			 * List of Local Variables
			 * n/a
			 * 
			 * @param 	e - the tasks to run if the action of pressing the button is performed <type ActionEvent>
			 * @return 	void
			 */
			public void actionPerformed(ActionEvent e) {
				alien[alienY][alienX]=true;
				aliensSpawned++;
				
				userX = xField.getText();
				userY = yField.getText();
				xField.setText("");
				yField.setText("");
				
				if (userX.equals(Integer.toString(alienX-10)) && userY.equals(Integer.toString(10-alienY))) {
					aliensCaught++;
					points+=10;
					points+=catchTime;
					totalCatchTime+=(10-catchTime);
					hitOrMiss="Catch! (Time bonus: "+df.format(catchTime)+" pts)";
				}
				else
					hitOrMiss="Miss!";
				
				gameTimer.cancel();
				game.removeAll();
				game.add(gamePanel());
				game.revalidate();
				game.repaint();
			}//end actionPerformed method
		});
		game.add(fireButton);
		
		game.add(new Alien(alienX-10,10-alienY));
		return game;
	}//end gamePanel method

	/**postgamePanel method
	 * This procedural method displays the user's stats for the current game session as well as all the alien locations during this
	 * session. The program calls the panelMaker method to make the new panel, then calls and adds the labelMaker method 5 times to give
	 * the ending page a title,a message to tell the user the invasion is over, the user's game stats, and a sub title for the
	 * correct locations. A loop then runs that displays small labels that show the coordinate locations of the alien locations
	 * that showed up during the game by call the labelMaker method for every alien location that appeared. Finally, the 
	 * menuButtonMaker is called and added to create a button that brings the user back to the menu and the private class
	 * ReviewAlien is called, which uses the global alien array to display all the alien locations of the round.
	 * 
	 * List of Local Variables
	 * postgame - the container that will hold all of the components of the postgame page <type JPanel>
	 * xx - the variable used to determine the x location of the correct coordinate labels based on how many have already been printed <type int>
	 * yy - the variable used to determine the y location of the correct coordinate labels based on how many have already been printed <type int>
	 * 
	 * @param	none
	 * @return	postgame - the container containing the components for the postgame page <type JPanel>
	 */	
	public JPanel postgamePanel() {
		JPanel postgame = panelMaker(true);
		postgame.add(labelMaker("Stats and Review",15,0,25,500,20,true));
		postgame.add(labelMaker("The invasion is over",13,50,70,200,20,false));

		postgame.add(labelMaker("<html>Aliens caught: "+aliensCaught
				+"<br/> Aliens missed: "+(aliensSpawned-aliensCaught)
				+"<br/> Average time for a catch: "+df.format(totalCatchTime/(double)aliensCaught)+" sec"
				+"<html>",13,50,90,250,60,false));

		postgame.add(labelMaker("<html>Current highscore: "+highscoreArray[0][scr]+" pts"
				+"<br/>Points: "+df.format(points)+" pts"
				+"<br/>Percentage accuracy: "+df.format((double)aliensCaught*100/(double)aliensSpawned)+"%"
				+"<html>",13,300,90,250,60,false));

		postgame.add(labelMaker("All correct alien locations during this invasion:",13,30,470,400,20,false));
		int xx=0,yy=0;
		for (byte x=0;x<20;x++ ){
			for (byte y=0;y<20;y++) {
				if (alien[y][x]==true) {
					yy++;			
					
					postgame.add(labelMaker("("+(x-10)+","+(10-y)+")",11,30+40*xx,470+16*yy,100,15,false));
					
					if (yy==10) {
						yy=0;
						xx++;
					}
				}
			}
		}
		
		postgame.add(menuButtonMaker(postgame));
		postgame.add(new ReviewAlien());
		return postgame;
	}//end postgamePanel method

	/**rulesPanel method
	 * This procedural method displays the rules on how to play the game. It calls the panelMaker method to make a new panel,
	 * and then calls the labelMaker method twice to make a title and the instruction set. Then, it calls the menuButtonmaker
	 * method to add a Return to menu button and adds the MenuAlien private class to make the page look prettier.
	 * 
	 * List of Local Variable
	 * rules - the container used to hold the components that make up the How to Play page <type JPanel>
	 * 
	 * @param	none
	 * @return	rules - the container used to hold the components that make up the How to Play page <type JPanel>
	 */
	public JPanel rulesPanel() {
		JPanel rules=panelMaker(true);
		rules.add(labelMaker("How To Play",20,0,100,500,30,true));
		
		rules.add(labelMaker("<html>"
				+ "You are a space alien hunter that catches aliens"
				+ " that are invading Earth."
				+ "<br/><br/>"
				+ "Your sensors helps you map your area on a Cartesian Plane."
				+ "<br/><br/>"
				+ "To catch the aliens, you have to enter the coordinates"
				+ " of the alien as it appears on your map."
				+ "<br/><br/>"
				+ "You get one shot at each alien, so missing allows the alien to land."
				+ "<br/><br/>"
				+ "The invasion lasts 60 seconds. Each alien takes 10 seconds to land."
				+ "<br/><br/>"
				+ "Each succesful catch gives you 10 pts."
				+ "<br/><br/>"
				+ "Bonus points are awarded for a successful catch's speed,"
				+ " given by:"
				+ "<br/>"
				+ "- Time bonus pts = 10 - catch speed."
				+ "<html>",15,50,150,400,500,false));
		
		rules.add(menuButtonMaker(rules));
		rules.add(new MenuAlien());
		return rules;
	}//end rulesPanel method

	/**highscorePanel
	 * This procedural method displays the highscore board as well as options to save or create files to save highscore after the
	 * game is closed. It calls the panelMaker method to make a new panel, then calls the labelMaker method and the menuButtonMaker
	 * method to make a title and a return to menu button. It creates two buttons by calling the buttonMaker method and then gives them
	 * ActionListeners and adds them to the panel. Finally, the program calls the labelMaker method 12 times to print a message that
	 * tells the user which file (if any) is selected, a warning about creating a new file if you already have one, and the top ten
	 * highscores. 
	 * 
	 * List of Local Variables
	 * highscore - the container used to hold the components that make up the highscore page <type JPanel>
	 * chooseButton - a button that allows the user to select an existing file if they have one <type JButton>
	 * newButton - a button that allows the user to select a folder to create a new highscore text file in <type JButton>
	 *  
	 * @param	none
	 * @return 	highscore - the container used to hold the components that make up the highscore page <type JPanel>
	 */
	public JPanel highscorePanel() {
		JPanel highscore = panelMaker(false);
		highscore.add(labelMaker("Highscores",20,0,50,500,30,true));
		highscore.add(menuButtonMaker(highscore));

		JButton chooseButton = buttonMaker("<html><center>Open a file with previous highscores</center><html>",40,470,170,50);
		JButton newButton = buttonMaker("<html><center>Select folder to create new highscore file</center><html>",290,470,170,50);
		
		chooseButton.addActionListener(new ActionListener() {
			/**actionPerformed method
			 * This functional method runs when the user clicks the chooseButton. It lets the user select a text file from their
			 * computer to retrieve previous highscores. The program reads the file and updates the current highscore board with
			 * the previous ones. In the event of any error, the program displays an error message and resets the highscoreArray
			 * and the file chosen, fileName. Then it removes the highscore panel and calls and adds the highscorePanel method
			 * so the page can be updated with the highscores that were retrieved.
			 * 
			 * List of Local Variables
			 * jfc - an object that lets the user select a file from their computer <type JFileChooser>
			 * inputStream - an object that reads the text file that the user selected <type BufferedReader>
			 * 
			 * @param 	e - the tasks to run if the action of pressing the button is performed <type ActionEvent>
			 * @return 	void
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser jfc = new JFileChooser();
					jfc.showDialog(null, null);
					jfc.setVisible(true);
					fileName = jfc.getSelectedFile();
					BufferedReader inputStream = new BufferedReader(new FileReader(fileName));
					for (byte i=0;i<10;i++) {
						highscoreArray[i][usr]=inputStream.readLine();
						if ((highscoreArray[i][usr]).length() > 10) 
							(highscoreArray[i][usr]) = (highscoreArray[i][usr]).substring(0, 10);
						highscoreArray[i][scr]=inputStream.readLine();
						if (0>=(Double.parseDouble(highscoreArray[i][scr])));
					}
					inputStream.close();
				}
				catch (Exception f) {
					JOptionPane.showMessageDialog(highscorePanel(),"Error. Highscores are not readable. "
							+ "Please make sure your highscore file hasn't been tampered with and that it is the right file","Error",JOptionPane.ERROR_MESSAGE);
					for(byte j=0;j<10;j++) {
						highscoreArray[j][scr]=("0.00");
						highscoreArray[j][usr]=("Guest");
					}
					fileName=null;
				}	
				highscore.removeAll();
				highscore.add(highscorePanel());
				highscore.revalidate();
				highscore.repaint();
			}//end actionPerformed method
		});
		
		newButton.addActionListener(new ActionListener() {
			/**actionPerformed method
			 * This functional method lets the user select a folder location to store the current set of highscores
			 * onto a new text file. If the file is succesfully created, the program displays a message and if not,
			 * the program tells the user to try again if they have not selected a folder or if the location is invalid.
			 * 
			 * List of Local Variables
			 * jfc - an object that lets the user select a folder from their computer <type JFileChooser>
			 * outputStream - an object that writes in the text file selected to update the highscores <type PrintWriter>
			 * folderName - the path that the user selects that they want their new text file to be created in <java.io.File>
			 * 
			 * @param 	e - the tasks to run if the action of pressing the button is performed <type ActionEvent>
			 * @return 	void
			 */
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.showDialog(null,null);
				jfc.setVisible(true);
				java.io.File folderName = jfc.getSelectedFile();
				PrintWriter outputStream = null;
				try {
					outputStream = new PrintWriter (new FileOutputStream(folderName+"\\SpaceAlienGameHighscores.txt"));
					for (byte i=0;i<10;i++) {
						outputStream.println(highscoreArray[i][usr]);
						outputStream.println(highscoreArray[i][scr]);
					}
					outputStream.close();
					JOptionPane.showMessageDialog(highscorePanel(),"File created and current highscores saved!");
				}
				catch(Exception f) {
					JOptionPane.showMessageDialog(highscorePanel(),"Error. Please try again","Error",JOptionPane.ERROR_MESSAGE);
				}
			}//end actionPerformed method
		});
		
		highscore.add(chooseButton);
		highscore.add(newButton);
		
		highscore.add(labelMaker("<html><center>If you have previously created a file "
				+ "and have not renamed it, creating a new file in the same folder will overwrite it.<center/><html>"
				,9,285,420,180,50,true));
		highscore.add(labelMaker("<html><center>Current file selected: "+fileName+"</center><html>"
				,12,0,520,500,60,true));
		
		for (int i=0;i<10;i++) {
			highscore.add(labelMaker((i+1)+".  "+highscoreArray[i][usr]+"  ("+highscoreArray[i][scr]+" pts)"
					,15,175,90+30*i,300,30,false));
		}
		
		return highscore;
	}//end highscorePanel method

	/**panelMaker method:
	 * This functional method makes JPanel containers and sets the layout manager of the
	 * JPanel to BorderLayout is the boolean received is true and null is false.
	 * 
	 * List of local variables
	 * panel - a JPanel container <type JPanel>
	 * 
	 * @param	borderLayout - whether the panel should have a BorderLayout or null Layout <type boolean>
	 * @return	panel - a JPanel container <type JPanel>
	 */
	public JPanel panelMaker(boolean borderLayout) {
		JPanel panel = new JPanel();
		panel.setSize(500,700);
		if (borderLayout==true)
			panel.setLayout(new BorderLayout());
		else
			panel.setLayout(null);
		panel.setVisible(true);
		return panel;
	}//end panelMaker method

	/**buttonMaker method
	 * This functional method creates a JButton based on the received phrase, x and y position, width
	 * and height. The returned button does nothing and is used within other methods to add ActionListeners
	 * to them
	 * 
	 * List of local variables
	 * button - a JButton that will do nothing when clicked  <type JButton>
	 * 
	 * @param	phrase - the phrase to be printed out on the JButton <type String>
	 * 			fontSize - the desired font size of the JButton text <type int>
	 * 			x - the desired x position of the JButton <type int>
	 * 			y - the desired y position of the JButton <type int>
	 * 			width - the desired width of the JButton <type int>
	 * 			height - the desired height of the JButton <type int>
	 * @return	button - a JButton that will do nothing when clicked <type JButton>
	 */	
	public JButton buttonMaker(String phrase, int x, int y, int width, int height) {
		JButton button = new JButton(phrase);
		button.setBounds(x,y,width,height);
		return button;
	}//end buttonMaker method

	/**menuButtonMaker method
	 * This functional method makes JButton objects by calling the buttonMaker method to make a 
	 * button and then adding an ActionListener to the button that will take the current panel
	 * as a parameter and then change it to the menu button
	 * 
	 * List of local variables
	 * button - a JButton that will return to the menu panel when clicked  <type JButton>
	 * 
	 * @param	currentPanel - the panel that is currently being shown that needs to be changed <type JPanel>
	 * @return	button - a JButton that will return to the menu panel when clicked <type JButton>
	 */
	public JButton menuButtonMaker(JPanel currentPanel) {
		JButton button = buttonMaker("Return to Menu",10,10,120,20);
		button.addActionListener(new ActionListener() {
			/**actionPerformed method
			 * This functional method is performed when the button is clicked. It will remove the currentPanel
			 * and then call and add the menuPanel to bring the user back to the menu. Then it revalidates and repaints.
			 * 
			 * List of Local Variables
			 * 
			 * @param 	e - the tasks to run if the action of pressing the button is performed <type ActionEvent>
			 * @return 	void
			 */
			public void actionPerformed(ActionEvent e) {
				currentPanel.removeAll();
				currentPanel.add(menuPanel());
				currentPanel.revalidate();
				currentPanel.repaint();
			}//end actionPerformed method
		});
		return button;
	}//end menuButtonMaker method

	/**labelMaker method:
	 * This functional method makes JLabel objects based on the received phrase, x and y position,
	 * width and height, and centers the text if the boolean parameter received is true.
	 * 
	 * List of local variables
	 * label - a JLabel
	 * 
	 * @param	phrase - the phrase to be printed out on the JLabel <type String>
	 * 			fontSize - the desired font size of the JLabel text <type int>
	 * 			x - the desired x position of the JLabel <type int>
	 * 			y - the desired y position of the JLabel <type int>
	 * 			width - the desired width of the JLabel <type int>
	 * 			height - the desired height of the JLabel <type int>
	 * 			center - whether to center the text or not <type boolean>
	 * 
	 * @return	label - a JLabel object <type JLabel>
	 */
	public JLabel labelMaker(String phrase,int fontSize,int x,int y,int width,int height,boolean center) {
		JLabel label= new JLabel(phrase);
		if (center==true)
			label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("", Font.BOLD, fontSize));
		label.setBounds(x,y,width,height);
		return label;
	}//end labelMaker method

	private class Alien extends JPanel{
		int alienX, alienY;
		Alien(int X, int Y){
			alienX=X;
			alienY=Y;
		}

		/**paint method
		 * This procedural method draws a 21x21 grid and an alien on it who's location is 
		 * based on the variables received by the Alien constructor.
		 * 
		 * 1) Creates a for loop that runs from -10 to 10 in increments of 1
		 * 2) If the loop number is divisible by 5, calls the setStroke method
		 * and the drawLine method to draw thick tick marks along the grid axis
		 * 3) If the loop number is 0, calls the setStroke method to make the 
		 * grid lines drawn thicker to indicate the x and y axis.
		 * 4) If neither condition is met, calls the drawString method twice to draw
		 * a thin horizontal and vertical line as grid lines for the Cartesian plane 
		 * 5) Calls the setFont method to set the font size and font style
		 * 6) Calls the drawString method 9 times to draw the scale of the grid
		 * in increments of 5.
		 * 7) Calls the setColor method to set the color of graphics drawn to black.
		 * 8) Calls the drawString method twice to draw the alien antennae
		 * 9) Calls the setColor method to set the color of graphics drawn to green.
		 * 10) Calls the fillOval method to draw the alien's head
		 * 11) Calls the setColor method to set the color of graphics drawn to white.
		 * 12) Calls the fillOval method twice to draw the alien's eyes/irises
		 * 13) Calls the setColor method to set the color of graphics drawn to black.
		 * 14) Calls the fillOval method twice to draw the alien's pupils
		 * 15) Calls the drawArc method to draw a smiling mouth for the alien.

		 * @param 	g - object used to draw the graphics
		 * 			g2 - object that changes line thickness and other more sophisticated graphics functions
		 * @return void
		 */
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			for(int i=-10;i<=10;i++) {
				if(i%5==0) {
					g2.setStroke(new BasicStroke(3));
					g.drawLine(gridX+boxSize*i,gridY-5,gridX+boxSize*i,gridY+5);	
					g.drawLine(gridX-5,gridY+boxSize*i,gridX+5,gridY+boxSize*i);
				}
				g2.setStroke(new BasicStroke(1));
				if (i==0)
					g2.setStroke(new BasicStroke(3));
				g.drawLine(gridX+boxSize*i,gridY+boxSize*10,gridX+boxSize*i,gridY-boxSize*10);
				g.drawLine(gridX+boxSize*10,gridY+boxSize*i,gridX-boxSize*10,gridY+boxSize*i);			
			}
			g.setFont(new Font("TimesRoman", Font.PLAIN, 10)); 
			g.drawString("-10", gridX-boxSize*10-16, gridY+11);
			g.drawString("-5", gridX-boxSize*5-10, gridY+11);
			g.drawString("5", gridX+boxSize*5-7, gridY+11);
			g.drawString("10", gridX+boxSize*10-11, gridY+11);
			g.drawString("0", gridX-7, gridY+11);
			g.drawString("10", gridX-11, gridY-boxSize*10+11);
			g.drawString("5", gridX-7, gridY-boxSize*5+11);
			g.drawString("-5", gridX-10, gridY+boxSize*5+11);
			g.drawString("-10", gridX-13, gridY+boxSize*10+11);

			g.setColor(Color.BLACK);
			g.drawLine(gridX+boxSize*alienX-5, gridY-boxSize*alienY-10, gridX+boxSize*alienX-2, gridY-boxSize*alienY);
			g.drawLine(gridX+boxSize*alienX+5, gridY-boxSize*alienY-10, gridX+boxSize*alienX+2, gridY-boxSize*alienY);
			g.setColor(Color.GREEN);
			g.fillOval(gridX+boxSize*alienX-8, gridY-boxSize*alienY-8, 16, 16);
			g.setColor(Color.WHITE);
			g.fillOval(gridX+boxSize*alienX+2, gridY-boxSize*alienY-5, 4, 4);
			g.fillOval(gridX+boxSize*alienX-4, gridY-boxSize*alienY-5, 4, 4);
			g.setColor(Color.BLACK);
			g.fillOval(gridX+boxSize*alienX+3, gridY-boxSize*alienY-4, 2, 2);
			g.fillOval(gridX+boxSize*alienX-3, gridY-boxSize*alienY-4, 2, 2);
			g.drawArc(gridX+boxSize*alienX-4, gridY-boxSize*alienY, 8, 4, 0, -180);
		}//end paint method
	}

	private class ReviewAlien extends JPanel{
		/**paint method
		 * This procedural method draws a 21x21 grid and aliens for every true location on
		 * the global array of alien locations called alien[][].
		 * 
		 * 1) Creates a for loop that runs from -10 to 10 in increments of 1
		 * 2) If the loop number is divisible by 5, calls the setStroke method
		 * and the drawLine method to draw thick tick marks along the grid axis
		 * 3) If the loop number is 0, calls the setStroke method to make the 
		 * grid lines drawn thicker to indicate the x and y axis.
		 * 4) If neither condition is met, calls the drawString method twice to draw
		 * a thin horizontal and vertical line as grid lines for the Cartesian plane 
		 * 5) Calls the setFont method to set the font size and font style
		 * 6) Calls the drawString method 9 times to draw the scale of the grid
		 * in increments of 5.
		 * 7) Initializes a for loop that checks every index of the alien[][] array. If 
		 * an index is true, 8-16 is called to draw an alien in that location.
		 * 8) Calls the setColor method to set the color of graphics drawn to black.
		 * 9) Calls the drawString method twice to draw the alien antennae
		 * 10) Calls the setColor method to set the color of graphics drawn to green.
		 * 11) Calls the fillOval method to draw the alien's head
		 * 12) Calls the setColor method to set the color of graphics drawn to white.
		 * 13) Calls the fillOval method twice to draw the alien's eyes/irises
		 * 14) Calls the setColor method to set the color of graphics drawn to black.
		 * 15) Calls the fillOval method twice to draw the alien's pupils
		 * 16) Calls the drawArc method to draw a smiling mouth for the alien.

		 * @param 	g - object used to draw the graphics
		 * 			g2 - object that changes line thickness and other more sophisticated graphics functions
		 * @return void
		 */
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			for(int i=-10;i<=10;i++) {
				if(i%5==0) {
					g2.setStroke(new BasicStroke(3));
					g.drawLine(gridX+boxSize*i,gridY-5,gridX+boxSize*i,gridY+5);	
					g.drawLine(gridX-5,gridY+boxSize*i,gridX+5,gridY+boxSize*i);
				}
				g2.setStroke(new BasicStroke(1));
				if (i==0)
					g2.setStroke(new BasicStroke(3));
				g.drawLine(gridX+boxSize*i,gridY+boxSize*10,gridX+boxSize*i,gridY-boxSize*10);
				g.drawLine(gridX+boxSize*10,gridY+boxSize*i,gridX-boxSize*10,gridY+boxSize*i);			
			}
			g.setFont(new Font("TimesRoman", Font.PLAIN, 10)); 
			g.drawString("-10", gridX-boxSize*10-16, gridY+11);
			g.drawString("-5", gridX-boxSize*5-10, gridY+11);
			g.drawString("5", gridX+boxSize*5-7, gridY+11);
			g.drawString("10", gridX+boxSize*10-11, gridY+11);
			g.drawString("0", gridX-7, gridY+11);
			g.drawString("10", gridX-11, gridY-boxSize*10+11);
			g.drawString("5", gridX-7, gridY-boxSize*5+11);
			g.drawString("-5", gridX-10, gridY+boxSize*5+11);
			g.drawString("-10", gridX-13, gridY+boxSize*10+11);

			int alienX,alienY;
			for (byte x=0;x<20;x++ ){
				for (byte y=0;y<20;y++) {
					if (alien[y][x]==true) {
						alienX=x-10;
						alienY=10-y;
						g.setColor(Color.BLACK);
						g.drawLine(gridX+boxSize*alienX-5, gridY-boxSize*alienY-10, gridX+boxSize*alienX-2, gridY-boxSize*alienY);
						g.drawLine(gridX+boxSize*alienX+5, gridY-boxSize*alienY-10, gridX+boxSize*alienX+2, gridY-boxSize*alienY);
						g.setColor(Color.GREEN);
						g.fillOval(gridX+boxSize*alienX-8, gridY-boxSize*alienY-8, 16, 16);
						g.setColor(Color.WHITE);
						g.fillOval(gridX+boxSize*alienX+3, gridY-boxSize*alienY-5, 4, 4);
						g.fillOval(gridX+boxSize*alienX-3, gridY-boxSize*alienY-5, 4, 4);
						g.setColor(Color.BLACK);
						g.fillOval(gridX+boxSize*alienX+3, gridY-boxSize*alienY-4, 2, 2);
						g.fillOval(gridX+boxSize*alienX-3, gridY-boxSize*alienY-4, 2, 2);
						g.drawArc(gridX+boxSize*alienX-4, gridY-boxSize*alienY, 8, 4, 0, -180);
					}
				}
			}
		}//end paint method
	}

	private class MenuAlien extends JPanel{
		/**paint method
		 * This procedural method draws a 21x21 grid and aliens for every true location on
		 * the global array of alien locations called alien[][].
		 * 
		 * 1) Calls the setColor method to set the color of graphics drawn to green.
		 * 2) Calls the setStroke method to set the lines drawn to a thickness of 15
		 * 3) Calls the drawString method to two thick green lines for the alien antennae
		 * 4) Calls the setStroke method to reduce line thickness to 3
		 * 5) Calls the fillOval method three times to draw the alien's head as well as 
		 * two bulbs on the ends of the antennae
		 * 6) Calls the fillArc method to draw the alien's body
		 * 7) Calls the setColor method to set the color of graphics drawn to white
		 * 8) Calls the fillOval method twice to draw the alien's eyes/irises
		 * 9) Calls the setColor method to set the color of graphics drawn to red
		 * 10) Calls the fillOval method twice to draw red pupils
		 * 11) Calls the setColor method to set the color of graphics drawn to black
		 * 12) Calls the fillArc method to draw the aline's smiling mouth.
		 * 13) Calls the setColor method to set the color of graphics drawn to gray
		 * 14) Calls the drawArc method three times to draw shadows under the alien's head
		 * and antennae bulbs
		 * 
		 * Local Variables
		 * g2 - object that changes line thickness and other more sophisticated graphics functions
		 * 
		 * @param 	g - object used to draw the graphics
		 * 			
		 * @return void
		 */
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D g2 = (Graphics2D) g;
			g.setColor(Color.GREEN);
			g2.setStroke(new BasicStroke(15));
			g.drawLine(170, 170, 220, 300);
			g.drawLine(330, 170, 280, 300);
			g2.setStroke(new BasicStroke(3));
			g.fillOval(150,200,200,150);
			g.fillOval(150,150,40,40);
			g.fillOval(310,150,40,40);
			g.fillArc(0, 300, 500, 800,180, -180);
			g.setColor(Color.WHITE);
			g.fillOval(190,230,30,30);
			g.fillOval(280,230,30,30);
			g.setColor(Color.RED);
			g.fillOval(200,240,10,10);
			g.fillOval(290,240,10,10);
			g.setColor(Color.BLACK);
			g.fillArc(200, 270, 100, 50, 180, 180);
			g.setColor(Color.GRAY);
			g.drawArc(150, 200, 200, 150, 219, 103);
			g.drawArc(150,150,40,40,275,40);
			g.drawArc(310,150,40,40,230,40);			
		}//end paint method
	}
}
