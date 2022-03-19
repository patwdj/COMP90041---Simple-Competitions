/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.io.Serializable;



/**
 * RandomPickCompetition where users are picked through a randomiser.
 * @author patriciawidjojo
 *
 */
public class RandomPickCompetition extends Competition implements Serializable {
	
	private static final int FIRST_PRIZE = 50000;
	private static final int SECOND_PRIZE = 5000;
	private static final int THIRD_PRIZE = 1000;
	private static final int[] prizes = { FIRST_PRIZE, SECOND_PRIZE, THIRD_PRIZE };
	private static final int MAX_WINNING_ENTRIES = 3;
	private ArrayList<Entry> allEntries = new ArrayList<Entry>(); //entries in the competition
	private ArrayList<Integer> winnersIndex = new ArrayList<Integer>();
	private ArrayList<Entry> allWinners = new ArrayList<Entry>(); //sorted by entryID

	
    
    
    
	/**
	 * Constructor to create a RandomPickCompetition.
	 * @param id (competitionID)
	 * @param database, DataProvider that holds member and bill information.
	 */
	public RandomPickCompetition(int id, DataProvider database) {
		super(id, database);
		setCompetitionType("RandomPickCompetition");
		handleInput();
	}

    
    
    
    
	/**
	 * Adds entries to this RandomPickCompetition based on valid bill input by user.
	 */
	public void addEntries() {

		char addMoreEntries = 'X';
		
		do {
			Bill bill = getValidBill();
			int numberOfEntries = bill.getNumberOfEntries();
			double billValue = bill.getBillValue();
			String billID = bill.getBillID();
			String memberID = bill.getMemberID();
			int currentID;

			System.out.println("This bill ($" + billValue + ") is eligible for " + numberOfEntries
					+ " entries.");

			//adds and prints the newly added entries
			System.out.println("The following entries have been automatically generated:");
			int firstPrint = getCountEntries();
			for (int i = firstPrint; i < (firstPrint + numberOfEntries); i++) {
				currentID = getCurrentEntryID();
				allEntries.add(new Entry(currentID, billID, memberID, getDatabase()));
				incrementEntryID();
				allEntries.get(i).printEntries();
			}
			
			setCountEntries(firstPrint + numberOfEntries);

			do {
				System.out.println("Add more entries (Y/N)?");
				String moreEntryChoice = SimpleCompetitions.keyboard.nextLine().toUpperCase();
				if (moreEntryChoice.length() == 1) {
					addMoreEntries = moreEntryChoice.charAt(0);
				}
				if (!(addMoreEntries == 'N' || addMoreEntries == 'Y')) {
					System.out.println("Unsupported option. Please try again!");
				}
			} while (!(addMoreEntries == 'N' || addMoreEntries == 'Y'));
		} while (addMoreEntries == 'Y');
	}

	
	
	
	
	/**
	 * Draws winners (max set to 3), updates winner list and prizes where appropriate.
	 */
	public void drawWinners() {
		
		int id = getCompetitionID();
		String name = getCompetitionName();
		int winningEntryCount = 0;
		int countZeroPrizeEntries = 0;
		
		System.out.println("Competition ID: " + id + ", Competition Name: " + name + ", Type: "
				+ "RandomPickCompetition");
		Random randomGenerator = null;
		if (SimpleCompetitions.testMode == 'T') {
			randomGenerator = new Random(getCompetitionID());
		} else {
			randomGenerator = new Random();
		}
		
		//check how many entries are valid to win to ensure no infinite loop.
		for (int entriesCheck = 0; entriesCheck < allEntries.size(); entriesCheck++) {
			int entryPrizeCheck = allEntries.get(entriesCheck).getPrize();
			if (entryPrizeCheck == 0) {
				countZeroPrizeEntries +=1;
			}
		}
 		
		//draw winners
		while ((winningEntryCount < MAX_WINNING_ENTRIES) && (countZeroPrizeEntries > 0)) {
			String memberID;
			boolean priorWin;
			int winningEntryIndex;
			boolean updatePrize = false;
			int currentPrize = -1;
			Entry winningEntry;
			priorWin = false;
			winningEntryIndex = randomGenerator.nextInt(allEntries.size());
			winningEntry = allEntries.get(winningEntryIndex);
			memberID = winningEntry.getMemberID();
			
			if (winningEntry.getPrize() == 0) {
				currentPrize = prizes[winningEntryCount];
				winningEntryCount++;
				countZeroPrizeEntries--;
				updatePrize = true;
			}
			
			//check for prior wins
			for (int indexOfIndex = 0; indexOfIndex < winnersIndex.size(); indexOfIndex++) {
				String testID = allEntries.get(winnersIndex.get(indexOfIndex)).getMemberID();
				if (memberID.equals(testID)) {
					priorWin = true;
				}
			}
			
			//if valid then add winner's index to list and updates prize.
			if (!priorWin && updatePrize) {
				winnersIndex.add(winningEntryIndex);
				winningEntry.setPrize(currentPrize);
				addTotalPrize(currentPrize);
				addCountWinners();
			}
		}
		
		//create a sorted winners arraylist then print.
		for (int winner = 0; winner < winnersIndex.size(); winner++) {
			allWinners.add(allEntries.get(winnersIndex.get(winner)));
		}
		Collections.sort(allWinners, new Comparator<Entry>(){
			public int compare(Entry e1, Entry e2) {
				return Integer.valueOf(e1.getEntryID()).compareTo(Integer.valueOf(e2.getEntryID()));
			}
		});
		System.out.println("Winning entries:");
		for (int printWinners = 0; printWinners < allWinners.size(); printWinners++) {
			Entry winner = allWinners.get(printWinners);
			winner.printWinningEntries(winner);
		}
		setIsActiveCompetition(false);
	}
	

	
	
	
}
