/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;



/**
 * LuckyNumbersCompetition where winners are chosen based on the amount of matching numbers.
 * @author patriciawidjojo
 *
 */
public class LuckyNumbersCompetition extends Competition implements Serializable {

	private static final int PRIZE_7_MATCH = 50000;
	private static final int PRIZE_6_MATCH = 5000;
	private static final int PRIZE_5_MATCH = 1000;
	private static final int PRIZE_4_MATCH = 500;
	private static final int PRIZE_3_MATCH = 100;
	private static final int PRIZE_2_MATCH = 50;
	private static final int TWO_MATCHES = 2;
	private static final int THREE_MATCHES = 3;
	private static final int FOUR_MATCHES = 4;
	private static final int FIVE_MATCHES = 5;
	private static final int SIX_MATCHES = 6;
	private static final int SEVEN_MATCHES = 7;
	private ArrayList<NumbersEntry> allEntries = new ArrayList<NumbersEntry>(); 
	private ArrayList<Integer> winnersIndex = new ArrayList<Integer>();
	private ArrayList<Entry> allWinners = new ArrayList<Entry>(); //sorted by entryID

	
	
	
	
	/**
	 * Constructor to create a LuckyNumbersCompetition object
	 * @param id (competitionID)
	 * @param database, DataProvider that holds member and bill information.
	 */
	public LuckyNumbersCompetition(int id, DataProvider database) {
		super(id, database);
		setCompetitionType("LuckyNumbersCompetition");
		handleInput();
	}

	
	
	
	
	/**
	 * Adds entries to this LuckyNumbersCompetition based on valid bill input by user.
	 */
	public void addEntries() {

		char addMoreEntries = 'X';

		do {
			Bill bill = getValidBill();
			int numberOfEntries = bill.getNumberOfEntries();
			double billValue = bill.getBillValue();
			String billID = bill.getBillID();
			String memberID = bill.getMemberID();
			int currentID = getCurrentEntryID();
			int numberOfManualEntries = 0;
			boolean validEntry;

			System.out.println("This bill ($" + billValue + ") is eligible for " + numberOfEntries
					+ " entries. How many manual entries did the customer " + "fill up?: ");
			do {
				validEntry = true;
				try {
					numberOfManualEntries = Integer.parseInt(SimpleCompetitions.keyboard.next());
					SimpleCompetitions.keyboard.nextLine();
					if (numberOfManualEntries > numberOfEntries) {
						System.out.println("The number must be in the range from 0 to "
								+ numberOfEntries + ". Please try again.");
						validEntry=false;
					}
					else if (!(numberOfManualEntries == (int) numberOfManualEntries)) {
						System.out.println("Must be an integer. Please try again.");
						validEntry=false;
					}
				} catch (InputMismatchException e) {
					System.out.println("Please enter a valid number. Please try again.");
					validEntry = false;
				} catch (Exception e) {
					System.out.println("Invalid value. Please try again.");
					validEntry = false;
				}
			} while (!validEntry);

			//add entries to the arraylist of entries
			for (int newEntry = currentID; newEntry < (currentID + numberOfEntries); newEntry++) {
				boolean isManual = false;
				if (newEntry < (currentID + numberOfManualEntries)) {
					isManual = true;
					allEntries.add(new NumbersEntry(newEntry, billID, memberID, isManual,
							getDatabase()));
				} else {
					// argument for getValidEntry is seed which is the total number of entries in
					// this competition which in this case is equal to newEntry.
					allEntries.add(new AutoNumbersEntry(newEntry, billID, memberID,
							getDatabase(), newEntry - 1));
				}
				incrementEntryID();
			}

			//print the newly added entries
			System.out.println("The following entries have been added:");
			int firstPrint = getCountEntries();
			for (int i = firstPrint; i < (firstPrint + numberOfEntries); i++) {
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
	 * Draws winners, updates winner list and prizes where appropriate.
	 */
	public void drawWinners() {
		
		int id = getCompetitionID();
		String name = getCompetitionName();
		

		System.out.println("Competition ID: " + id + ", Competition Name: " + name + ", Type: "
				+ "LuckyNumbersCompetition");
		
		int[] winningNumbers = getWinningNumbers();

		for (int entryCheck = 0; entryCheck < allEntries.size(); entryCheck++) {
			int numberOfMatch = 0;
			boolean priorWin = false;
			// details of the entry we are checking for win
			NumbersEntry tempCheck = allEntries.get(entryCheck);
			String tempMemberID = tempCheck.getMemberID();
			int[] tempNumbers = tempCheck.getNumbers();

			for (int digit = 0; digit < NumbersEntry.NUMBERS_LENGTH; digit++) {
				for (int winDigit = 0; winDigit < NumbersEntry.NUMBERS_LENGTH; winDigit++) {
					if (tempNumbers[digit] == winningNumbers[winDigit]) {
						numberOfMatch += 1;
					}
				}
			}
			
			tempCheck.setNumberOfMatch(numberOfMatch);
			tempCheck.setPrize(prizeValue(numberOfMatch));

			// checks for prior win and updates accordingly
			int countWinners = winnersIndex.size();
			if (numberOfMatch > 1 && countWinners > 0) {
				for (int indexOfIndex = 0; indexOfIndex < countWinners; indexOfIndex++) {
					int priorWinIndex = winnersIndex.get(indexOfIndex);
					NumbersEntry priorWinEntry = allEntries.get(priorWinIndex);
					if (tempMemberID.equals(priorWinEntry.getMemberID())) {
						priorWin = true;
						if (numberOfMatch > priorWinEntry.getNumberOfMatch()) {
							winnersIndex.set(indexOfIndex, entryCheck);
						}
						break;
					}
				}
			}
			if (numberOfMatch > 1 && !priorWin) {
				winnersIndex.add(entryCheck);
			}
		}

		//create a sorted winners arraylist then print.
		for (int winner = 0; winner < winnersIndex.size(); winner++) {
			allWinners.add(allEntries.get(winnersIndex.get(winner)));
		}
		Collections.sort(allWinners, new Comparator<Entry>() {
			public int compare(Entry e1, Entry e2) {
				return Integer.valueOf(e1.getEntryID()).compareTo(Integer.valueOf(e2.getEntryID()));
			}
		});
		System.out.println("Winning entries:");
		for (int printWinners = 0; printWinners < allWinners.size(); printWinners++) {
			NumbersEntry winner = (NumbersEntry) allWinners.get(printWinners);
			winner.printWinningEntries(winner);
			if (printWinners != (getCountWinners() - 1)) {
				System.out.println();
			}
			addTotalPrize(winner.getPrize());
			addCountWinners();
		}
		setIsActiveCompetition(false);
	}

	
	
	
	
	/**
	 * Gets and prints winning numbers (numbers picked as an autoentry)
	 * @return winning numbers as an array of integers.
	 */
	private int[] getWinningNumbers() {

		AutoNumbersEntry winningEntry;
		int[] winningNumbers = new int[NumbersEntry.NUMBERS_LENGTH];

		if (SimpleCompetitions.testMode == 'T') {
			winningEntry = new AutoNumbersEntry(getCompetitionID());
		} else {
			winningEntry = new AutoNumbersEntry();
		}
		winningNumbers = winningEntry.getNumbers();
		Arrays.sort(winningNumbers);

		//prints Lucky numbers according to format
		System.out.print("Lucky Numbers:");
		for (int digit = 0; digit < NumbersEntry.NUMBERS_LENGTH; digit++) {
			System.out.printf("%3s", Integer.toString(winningNumbers[digit]));
		}
		System.out.println(" [Auto]");
		return winningNumbers;
	}

	
	
	
	
	/**
	 * Calculates prize based on number of matching digits
	 * @param numberOfMatch
	 * @return prize value for that given number of match.
	 */
	private int prizeValue(int numberOfMatch) {

		switch (numberOfMatch) {
		case TWO_MATCHES:
			return PRIZE_2_MATCH;
		case THREE_MATCHES:
			return PRIZE_3_MATCH;
		case FOUR_MATCHES:
			return PRIZE_4_MATCH;
		case FIVE_MATCHES:
			return PRIZE_5_MATCH;
		case SIX_MATCHES:
			return PRIZE_6_MATCH;
		case SEVEN_MATCHES:
			return PRIZE_7_MATCH;
		default:
			return 0;
		}
	}

	
	
	
	
}
