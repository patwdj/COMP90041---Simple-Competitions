/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;



/**
 * Competition is an abstract class containting variables and methods relevant to both 
 * LuckyNumbersCompetition and RandomPickCompetition.
 * @author patriciawidjojo
 *
 */
public abstract class Competition implements Serializable {
	
	private String name; // competition name
	private int id; // competition identifier
	private String competitionType;
	private int countEntries = 0;
	private int countWinners = 0;
	private int entryID = 1;
	private int totalPrize = 0;
	private boolean isActiveCompetition;
	private DataProvider database;
	private char testMode = 'X';

	
	
	
	/**
	 * Constructor to create a new competition/subclass
	 * @param id (competitionID)
	 * @param database, DataProvider that holds member and bill information.
	 */
	public Competition(int id, DataProvider database) {
		this.id = id;
		this.database = database;
		isActiveCompetition = true;
		this.testMode = SimpleCompetitions.testMode;
	}

	
	
	
	
	//setters and getters.
	public String getCompetitionName() {
		return name;
	}

	public void setCompetitionName(String name) {
		this.name = name;
	}
	
	public int getCompetitionID() {
		return id;
	}

	public void setCompetitionID(int id) {
		this.id = id;
	}
	
	public void setCompetitionType(String competitionType) {
		this.competitionType = competitionType;
	}
	
	public int getCountEntries() {
		return countEntries;
	}
	
	public void setCountEntries(int countEntries) {
		this.countEntries = countEntries;
	}
	
	public int getCountWinners() {
		return countWinners;
	}

	public void addCountWinners() {
		countWinners += 1;
	}
	
	public int getCurrentEntryID() {
		return entryID;
	}
	
	public void setCurrentEntryID(int entryID) {
		this.entryID = entryID;
	}

	public void incrementEntryID() {
		entryID += 1;
	}
	
	public void addTotalPrize(int addPrize) {
		totalPrize += addPrize;
	}
	
	public boolean getIsActiveCompetition() {
		return isActiveCompetition;
	}
	
	public DataProvider getDatabase() {
		return database;
	}

	public void setIsActiveCompetition(boolean isActiveCompetition) {
		this.isActiveCompetition = isActiveCompetition;
	}
	
	public char getTestMode () {
		return testMode;
	}

	
	
	
	
	/**
	 * Handle user inputs about competition details.
	 */
	public void handleInput() {
		System.out.println("Competition name: ");
		name = SimpleCompetitions.keyboard.nextLine();
		System.out.println("A new competition has been created!");
		System.out.println("Competition ID: " + id + ", Competition Name: " + name + ", Type: "
				+ competitionType);
	}

	
	
	
	
	/**
	 * Abstract method to be updated with drawWinners method.
	 */
	public abstract void drawWinners();
	
	

	
	
	/**
	 * Abstract method to be updated with addEntries method.
	 */
	public abstract void addEntries();

	

	
	
	/**
	 * Takes user billID input and repeats until a valid billID is received.
	 * @return a valid Bill object corresponding to user input billID.
	 */
	public Bill getValidBill() {

		String testID;
		boolean validTestID;
		Bill bill = null;

		do {
			validTestID = true;
			System.out.println("Bill ID: ");
			testID = SimpleCompetitions.keyboard.nextLine();

			// check format
			if (testID.length() == DataProvider.ID_LENGTH) {
				for (int i = 0; i < DataProvider.ID_LENGTH; i++) {
					int asciiValueTestID = testID.charAt(i);
					if (!(DataProvider.ASCII_0 <= asciiValueTestID
							&& asciiValueTestID <= DataProvider.ASCII_9)) {
						validTestID = false;
						System.out.println("Invalid bill id! It must be a 6-digit number. "
								+ "Please try again.");
						break;
					}
				}
			} else {
				validTestID = false;
				System.out.println("Invalid bill id! It must be a 6-digit number. Please try "
						+ "again.");
			}
			
			// if format is valid, check that said bill (and memberID) exist in database.
			if (validTestID) {
				bill = database.searchBill(testID);

				if (bill == null) {
					System.out.println("This bill does not exist. Please try again.");
					validTestID = false;
				} else if (!bill.isValidBill()) {
					System.out.println("This bill has no member id. Please try again.");
					validTestID = false;
				} 
			}

			// if bill exists, check if bill can be used for new entries.
			if (validTestID) {
				if (bill.getBillUsed()) {
					System.out.println("This bill has already been used for a competition. Please "
							+ "try again.");
					validTestID = false;
				} else if (bill.getNumberOfEntries() < 1) { 
					System.out.println( "This bill is not eligible for an entry. The total "
								+ "amount is smaller than $50.0");
					validTestID = false;
				} else {
					Member memberExist = database.searchMember(bill.getMemberID());
					if (memberExist == null) {
						System.out.println("This bill's member id (Member ID: " + bill.getMemberID() 
						+ ") does not exist in member file. Please try again.");
						validTestID = false;
					} else {
						bill.setBillUsed(true);
					}
				}
			}
			
		} while (!validTestID);

		return bill;
	}

	
	
	
	
	/**
	 * Prints competition details in correct report format.
	 */
	public void report() {
		System.out.println();
		System.out.print("Competition ID: " + id + ", name: " + name + ", active: ");
		if (isActiveCompetition) {
			System.out.println("yes");
		} else {
			System.out.println("no");
		}
		System.out.println("Number of entries: " + countEntries);
		if (!isActiveCompetition) {
			System.out.println("Number of winning entries: " + countWinners);
			System.out.println("Total awarded prizes: " + totalPrize);
		}
	}

	
	
	
	
}
