package sumoarena;

import java.util.List;
import java.util.ListIterator;

import sumoarena.algorithm.GameProcessor2;
import valueobjects.AccelerationVector;
import valueobjects.PlayingInfo;
import valueobjects.RoundEndInfo;
import valueobjects.RoundStartInfo;


public class Player {
	
	/**
	 * The description of the current round
	 */
	private RoundStartInfo roundInfo;
	private GameProcessor2 processor = new GameProcessor2();
	
	/**
	 * Called each time a new round begins
	 * @param RoundStartInfo the data sent by the game server
	 */
	public void onRoundStart(RoundStartInfo roundStartInfo)
	{
		roundInfo = roundStartInfo;
		processor.setRoundInfo(roundStartInfo);
	}
	
	/**
	 * Called when server sends a update during the game
	 * @param playingInfo
	 * @return the variation to be applied on the velocity vector, if the sphere is always in the game, or null otherwise
	 */
	public AccelerationVector onPlayRequest(PlayingInfo playingInfo){
		AccelerationVector accelerationVector = null; 
        if (roundInfo != null && playingInfo.getSpheres()[roundInfo.myIndex].inArena)
        {
        	///////////////////////////// insert your code here ///////////////////////
        	accelerationVector = processor.getAccVector(playingInfo);
        	
        	//////////////////////////////////////////////////////////////////////////

        	// check if the values are in the limits
        	if(accelerationVector.getdVx() * accelerationVector.getdVx() 
        			+ accelerationVector.getdVy() * accelerationVector.getdVy() 
        			> roundInfo.maxSpeedVariation * roundInfo.maxSpeedVariation)
        	{
        		accelerationVector = null;
        		System.out.println("Nulling acc Vector");
        	}
        }
        return accelerationVector;
	}
	
	/**
	 * Called each time a round ends
	 * @param roundEndInfo the data sent by the game server
	 */
	public void onRoundEnd(RoundEndInfo roundEndInfo)
	{
		if(roundInfo == null)
		{
			return;
		}
		if(isInList(roundInfo.myIndex, roundEndInfo.roundWinners))
		{
			System.out.println("Congratulations, you won this round!");
		}
		else 
		{
			System.out.println("You lost this round.");
		}
		if(isInList(roundInfo.myIndex, roundEndInfo.gameWinners))
		{
			System.out.println("Congratulations, you won this game!");
		}
		else if (!roundEndInfo.gameWinners.isEmpty())
		{
			System.out.println("You lost this game.");
		}
	}
	
	/**
	 * Tests if a given value is present in a given list of Numbers
	 * @param value the value to be searched in the list
	 * @param list a list of Numbers
	 * @return true if the number is present, false otherwise
	 */
	private Boolean isInList(int value, List<Number> list)
	{
		ListIterator<Number> iterator = list.listIterator();
		Number valueInList = null;
		while(iterator.hasNext()) {
			valueInList = iterator.next();
			if (valueInList.intValue() == value)
			{
				return true;
			}
		}
		return false;
	}
	
}
