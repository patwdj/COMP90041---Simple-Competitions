/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;



/**
 * Entry object each representing an entry into the relevant competition.
 * @author patriciawidjojo
 *
 */
public class Entry implements Serializable {

	private int entryID;
	private String billID;
	private String memberID;
	private boolean isManual;
	private int prize;
	private DataProvider database;

	
	
	
	
	/**
	 * Empty constructor to create Entry/Subclass object
	 */
	public Entry() {}

	
	
	
	
	/**
	 * Constructor to create entry object for AutoEntry (isManual is always false)
	 * @param currentID, entryID to assign this entry.
	 * @param billID relating to this entry.
	 * @param memberID relating to this entry.
	 * @param database, DataProvider that holds member and bill information
	 */
	public Entry(int currentID, String billID, String memberID, DataProvider database) {
		this.entryID = currentID;
		this.billID = billID;
		this.memberID = memberID;
		this.database = database;
	}

	
	
	
	
	/**
	 * Constructor to create an Entry/Subclass object (excl. AutoEntry)
	 * @param currentID, entryID to assign this entry.
	 * @param billID relating to this entry.
	 * @param memberID relating to this entry.
	 * @param isManual, boolean where true means that this entry is a manual entry, auto otherwise
	 * @param database, DataProvider that holds member and bill information
	 */
	public Entry(int currentID, String billID, String memberID, boolean isManual,
			DataProvider database) {
		this.entryID = currentID;
		this.billID = billID;
		this.memberID = memberID;
		this.isManual = isManual;
		this.database = database;
	}

	
	
	
	
	// setters and getters.
	public int getEntryID() {
		return entryID;
	}

	public void setEntryID(int entryID) {
		this.entryID = entryID;
	}

	public String getBillID() {
		return billID;
	}

	public void setBillID(String billID) {
		this.billID = billID;
	}

	public String getMemberID() {
		return memberID;
	}

	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}

	public boolean getIsManual() {
		return isManual;
	}

	public void setIsManual(boolean isManual) {
		this.isManual = isManual;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prize) {
		this.prize = prize;
	}

	public DataProvider getDatabase() {
		return database;
	}

	
	
	
	
	/**
	 * Prints this entry in basic format i.e. entryID only
	 */
	public void printEntries() {
		System.out.print("Entry ID: ");
		System.out.printf("%-6d", entryID);
		System.out.println();
	}

	
	
	
	
	/**
	 * Prints entries in basic winner format i.e. without numbers
	 * 
	 * @param winner Entry to be printed.
	 */
	public void printWinningEntries(Entry winner) {

		Member winningMember = database.searchMember(memberID);
		
		System.out.print("Member ID: " + memberID + ", Member Name: " + winningMember
				.getMemberName() + ", Entry ID: " + entryID);
		System.out.print(", Prize: ");
		System.out.printf("%-5d", prize);
		System.out.println();
	}

	
	
	
	
}
