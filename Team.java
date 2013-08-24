package team01;

import java.awt.Color;

import hockey.api.Player;
import hockey.api.GoalKeeper;
import hockey.api.ITeam;
import hockey.api.*;

public class Team implements ITeam {
    public int getLuckyNumber() { return 1337; }
    public String getShortName() { return "WON" ; }
    public String getTeamName() { return "Team Won"; }
    public Color getTeamColor() { return Color.MAGENTA; }
    public Color getSecondaryTeamColor() { return Color.ORANGE; }
    public GoalKeeper getGoalKeeper() { return new Goalie(); }
    public Player getPlayer(int index) { if(index < 3){return new Attacker(index);}else{return new Defender(index);} }
}

class Attacker extends Player {
    private static int[] numbers = {1, 2, 3, 4, 5, 6};
    private static String[] names = {
	"", "Glock", "Ruger", "Smith", "Wesson", "Colt"
    };
    private int index;

    public Attacker(int index) { this.index = index; }
    public int getNumber() { return numbers[index]; }
    public String getName() { return names[index]; }
    public boolean isLeftHanded() { return false; }
    public void step() {
	if (hasPuck()) // If player has the puck...
	    if (Math.abs(Util.dangle(getHeading(), // ...and is turned towards the goal.
				     Util.datan2(0 - getY(),
						 2500 - getX()))) < 90) {
		int target = (int)(Math.random()*200)-100;
		shoot(2600, target, 10000); // Shoot.
	    } else // If not
		skate(2600, 0, 1000); // Turn towards goal.
	else // If not
	    skate(getPuck(), 1000); // Get the puck.
    }
}

class Defender extends Player {
    private static int[] numbers = {1, 2, 3, 4, 5, 6};
    private static String[] names = {
	"Wesson", "Colt"
    };
    private int index;

    public Defender(int index) { this.index = index; }
    public int getNumber() { return numbers[index]; }
    public String getName() { return names[index]; }
    public boolean isLeftHanded() { return false; }
	public void step() {
		if (hasPuck()) // If we have the puck.
			skate(2600, 0, 1000); // Skate towards the goal.
		else if (Util.dist(getX() - getPuck().getX(), // If the puck is within
														// 5m.
				getY() - getPuck().getY()) < 500) {
			
			// Prevent offsides
			if (getX() < 2000) {

				IPlayer goalie = null;

				for (int i = 0; i < 12; ++i) {
					IPlayer cur = getPlayer(i);

					if (cur.isOpponent() && cur instanceof IGoalKeeper) {
						goalie = cur; 
					}
				}

				//always try to move toward the goalie
				turn(goalie, 1000);
				skate(getPuck(), 1000);
			}
		} else {
			IPlayer best = null;
			for (int i = 0; i < 12; ++i) { 
				IPlayer cur = getPlayer(i);

				if (cur.isOpponent() && 
						(best == null || Util.dist(getX() - cur.getX(), 
								getY() - cur.getY()) < Util.dist(
								getX() - best.getX(), getY() - best.getY())))
					best = cur; 
			}

			skate(best, 1000); 
		}
	}	
}



class Goalie extends GoalKeeper {
    public int getNumber() { return 1; }
    public String getName() { return ""; }
    public boolean isLeftHanded() { return false; }
    public void step() {
		if(getPuck().getY()< 90 && getPuck().getY() > -90){
			skate(-2550, getPuck().getY(), 200); // Stand in the middle of the goal.
		}
	turn(getPuck(), MAX_TURN_SPEED); // Turn towards puck.
    }

}

