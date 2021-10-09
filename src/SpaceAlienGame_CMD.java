import java.util.Timer;
import java.util.TimerTask;
import java.text.DecimalFormat;
import java.util.Scanner;

public class SpaceAlienGame_CMD {
	public static void main (String[]args) {
		Menu();
	}

	public static void Menu() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner (System.in);	
		String userMenuChoice="";
		while(true){
			System.out.println("=================IVAN'S SPACE ALIEN GAME MENU================="
					+ "       \n| For instructions on how to play, enter 'rules'.            |"
					+ "       \n| To start the game, enter 'start'.                          |" 
					+ "       \n| To see your highscores for this session, enter 'highscore'.|"
					+ "       \n| To quit the game, enter 'quit'.                            |"
					+ "       \n==============================================================");
			
			userMenuChoice= sc.nextLine();
			
			if (userMenuChoice.equalsIgnoreCase("rules")) {
				System.out.println("======================HOW TO PLAY======================"
						+ "\nYou are a space alien hunter that catches aliens"
						+ "\nthat are invading Earth one at a time."
						+ "\n"
						+ "\nYour sensors helps you map your area on a 21x21 grid"
						+ "\nwhere domain={-10<=x<=10} and range={-10<=y<=10}."
						+ "\n"
						+ "\nThere is no scale, but the distance between a"
						+ "\n'+' tick mark and another '+' is one unit"
						+ "\nand the center of the grid is (0,0)."
						+ "\n"
						+ "\nTo catch the aliens, enter the coordinates of the aliens"
						+ "\nas they appear on your map as an 'O',"
						+ "\n"
						+ "\nType the x-coord, enter it, then type the y-coord,"
						+ "\nand enter it to shoot. You get one shot at each alien,"
						+ "\nso missing means the aliens lands on Earth."
						+ "\n"
						+ "\nThe invasion lasts 30 seconds and you have 3 lives."
						+ "\n"
						+ "\nEach succesful catch gives you 10 pts."
						+ "\nEach life left at the end of the invasion gives +10pts"
						+ "\nBonus points are awarded for successful catch speed."
						+ "\n     Given by pts=10 - catch speed"
						+ "\n========================WARNING========================"
						+ "\nEntering non-number values,decimals,numbers less than"
						+ "\n-2,147,483,648 or numbers greater than 2,147,483,647"
						+ "\ninto your coordinate system will crash it, resulting in"
						+ "\ninstant loss and allowing the aliens to take over Earth!"
						+ "\n=======================================================");
				System.out.println("<Press enter to return to menu>");
				sc.nextLine();
				Menu();
			}
			if (userMenuChoice.equalsIgnoreCase("start")) {
				Game();
			}
			if(userMenuChoice.equalsIgnoreCase("highscore")) {
				Highscore();
			}
			if(userMenuChoice.equalsIgnoreCase("quit")) {
				Quit();
			}
			else
				System.out.println("INVALID INPUT, PLEASE TRY AGAIN");
		}
	}
	
	public static String highscoreArray[][]=new String [10][2];
	public static final int usr=0;
	public static final int	scr=1;
	public static boolean alienLocation[][]=new boolean [21][21];
	public static int gameTime;
	public static double userResponseTime;
	public static DecimalFormat df = new DecimalFormat("##.##");
	public static boolean crash=false;

	public static void Game() {
		int userX, userY=3423;
		int alienX, alienY;
		boolean correct;
		int correctCount;
		double points;
		String username;
		String restart;
		int numberOfAliens;
		denullHighscores();
		int lives;
		
		Scanner sc = new Scanner (System.in);
		System.out.println("=================GAME=================");
		System.out.println("Enter your username to start the game. (10 character limit)");
		username=sc.nextLine();
		if (username.length() > 10) 
			username = username.substring(0, 10);

		do {
			points=0;
			correctCount=0;					
			numberOfAliens=0;
			correct=false;
			lives=3;

			TimerTask gameTimerTask = new TimerTask(){
				public void run(){
					gameTime+=1;
				}    
			};

			Timer gameTimer = new Timer();

			gameTimer.schedule(gameTimerTask,0,1000);

			do{
				

				userResponseTime=0;
				TimerTask responseTimerTask = new TimerTask(){
					public void run(){
						userResponseTime+=0.01;
					}    
				};

				Timer responseTimer = new Timer();

				responseTimer.schedule(responseTimerTask,0,10);

				do{
					alienX=(int)(Math.random()*20)-10;
					alienY=(int)(Math.random()*20)-10;
				}while(alienLocation[alienX+10][alienY+10]==true);

				alienLocation[alienX+10][alienY+10]=true;

				System.out.println("\n_____________ALIEN INCOMING!_____________");
				Grid(alienX,alienY);

				System.out.println("Aliens killed: "+correctCount+"\tPoints: "+df.format(points));
				System.out.println("Aliens missed: "+(numberOfAliens-correctCount)+"   \tHighscore: "+df.format(Double.parseDouble(highscoreArray[0][scr])));
				System.out.println("Lives: "+lives);

				if(gameTime<30) {
					System.out.println("\nFire at:");
					try {//Declares the try portion of a try catch statement.
						System.out.print("X = ");
						userX=Integer.parseInt(sc.nextLine());
						if(gameTime>=30)
							break;
						if (userX==alienX)//Declares an if statement that runs if userAns is equal to the the correct answer of the question 'ans'.
							correct=true;
						else
							correct=false;
						System.out.print("Y = ");
						userY=Integer.parseInt(sc.nextLine());
						if(gameTime>=30)
							break;
						if (userY==alienY)//Declares an if statement that runs if userAns is equal to the the correct answer of the question 'ans'.
							correct=true;
						else
							correct=false;
					}//Ends the try portion of the try catch statement
					catch (Exception e){//Declares the catch portion of the try catch statement which runs if an error occurs due to invalid user input.
						System.out.println("!!!System Error!!!");
						crash=true;
						break;
					}//Ends the catch portion of the try catch statement
				}
				else
					break;

				System.out.println("\nResponse time: "+df.format(userResponseTime)+" seconds");
				if (correct==true) {
					System.out.print("Hit! +10 points");
					correctCount++;
					points+=10;
					if (userResponseTime<=10) {
						System.out.println("    Time bonus: +"+df.format(10-userResponseTime)+" points");
						points+=(10-userResponseTime);
					}
					else
						System.out.println("Time bonuse: 0 points");
				}
				else {
					System.out.println("Miss! -1 life"
							+ "\nNo time bonus");
					lives--;
					if(lives==0) {
						crash=true;
						break;
					}
				}
				responseTimer.cancel();
				numberOfAliens++;
			}while(gameTime<30);
			gameTimer.cancel();

			if (crash==true) {
				System.out.println("\nEARTH IS DOOMED!");
				System.out.println("\n__________STATS__________");
				System.out.println("Lives left: 0");
				System.out.println("Aliens captured: "+correctCount);
				System.out.println("Aliens missed: Infinite.");
				points=0;
				System.out.println("Time Bonus: 0");
				System.out.println("Points: 0");
			}
			else {
				System.out.println("\nTHE INVASION IS OVER!");
				System.out.println("\n__________STATS__________");
				System.out.println("\tLives left: "+lives+" ("+lives*10+" pts)");
					points+=lives*10;
				System.out.println("\tAliens captured: "+correctCount+" ("+(10*correctCount)+" pts)");
				System.out.println("\tAliens missed: "+(numberOfAliens-correctCount));
				System.out.println("\tTime Bonus: "+df.format((points-correctCount*10-lives*10))+" pts");
				System.out.println("*Points: "+df.format(points)+" pts*");
				if(numberOfAliens!=0)
					System.out.println("Percentage acccuracy: "+df.format((correctCount*100)/numberOfAliens)+"%");
				else
					System.out.println("Percentage acccuracy: 0%");
				if (points>(Double.parseDouble(highscoreArray[0][scr])))
					System.out.println("You beat the highscore of "+df.format((Double.parseDouble(highscoreArray[0][scr])))+" by "+df.format(points-(Double.parseDouble(highscoreArray[0][scr])))+" pts");
			}
			System.out.println("\nPress enter to see the alien location(s) during this invasion.");
			sc.nextLine();
			ReviewGrid(alienLocation);

			for (byte a=0;a<21;a++) {
				for (byte b=0;b<21;b++) {
					alienLocation[a][b]=false;					
				}
			}

			for (int i=0;i<10;i++) {
				if (points>(Double.parseDouble(highscoreArray[i][1]))) {
					for (int j=9;j>=i+1;j--) {
						highscoreArray[j][usr]=highscoreArray[j-1][usr];
						highscoreArray[j][scr]=highscoreArray[j-1][scr];
					}
					highscoreArray[i][usr]=username;
					highscoreArray[i][scr]=""+points;
					break;
				}
			}

			System.out.println("\nEnter 'play' to play again. Enter anything else to return to the menu.");//Asks the user to enter 'Play' to play the game again.
			restart=sc.nextLine();//Reads the user input.

		}while(restart.equalsIgnoreCase("play"));

		Menu();
		sc.close();
	}

	public static void Grid(int alienX, int alienY) {
		for (byte y=10;y>=-10;y-- ){
			for (byte x=-10;x<=10;x++) {
				if (x==alienX && y==alienY)
					System.out.print("O");
				else {
					if(y==0) {
						if(x==0)
							System.out.print("+");
						else
							System.out.print("-");
					}
					else{
						if(x==0)
							System.out.print("|");
						else
							System.out.print("+");
					}
				}
				if (x!=10)
					System.out.print("-");
			}
			System.out.println("");
		}
	}

	public static void ReviewGrid(boolean alienLocation[][]) {
		if (crash==true) {
			System.out.println("Your tech crashed and the aliens invaded everywhere!");
		}
		else {
			System.out.println("_________________REVIEW!_________________");
			for (byte y=10;y>=-10;y-- ){
				for (byte x=-10;x<=10;x++) {
					if (alienLocation[x+10][y+10]==true) {
						System.out.print("O");
					}
					else{
						if(y==0) {
							if(x==0)
								System.out.print("+");
							else
								System.out.print("-");
						}
						else{
							if(x==0)
								System.out.print("|");
							else
								System.out.print("+");
						}
					}
					if (x!=10)
						System.out.print("-");
				}
				System.out.println("");
			}

			System.out.println("All correct alien coordinates for this game:");
			for (byte y=10;y>=-10;y-- ){
				for (byte x=-10;x<=10;x++) {
					if (alienLocation[x+10][y+10]==true)
						System.out.print("("+(x)+","+(y)+") , ");
				}
			}
		}
		System.out.println();
	}

	public static void Highscore() {
		denullHighscores();
		Scanner sc = new Scanner (System.in);
		System.out.println("=================HIGHSCORES=================");
		for (int i=0;i<10;i++) {
			System.out.println((i+1)+".\t"+(df.format(Double.parseDouble(highscoreArray[i][scr])))+"\t("+highscoreArray[i][usr]+")");
		}
		System.out.println("\n<Press enter to return to the menu>");
		sc.nextLine();
		Menu();
		sc.close();
	}

	public static void denullHighscores() {
		for(byte i=0;i<10;i++) {
			if (highscoreArray[i][scr]==null)
				highscoreArray[i][scr]=("0.00");
		}
	}

	public static void Quit() {
		System.out.println("Thank you for using Ivan's Space Alien Game");
	}
}