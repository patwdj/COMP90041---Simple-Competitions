/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;



/**
 * DataProvider containing entries about all bills and all members information from the input files.
 * Acts as a database to run the program.
 * @author patriciawidjojo
 *
 */
public class DataProvider implements Serializable {
	
	File memberFile = null;
	File billFile = null;
	private ArrayList<Member> allMembers = new ArrayList<Member>();
	private ArrayList<Bill> allBills = new ArrayList<Bill>();
	public static final int ID_LENGTH = 6;
	public static final int ASCII_0 = 48;
	public static final int ASCII_9 = 57;
	private static final int MEMBER_FILE_COLUMN_LENGTH = 3;
	private static final int BILL_FILE_COLUMN_LENGTH = 4;
	private boolean existUsableBill = false;


	
	

	
	/**
	 * Empty constructor to create database (i.e. DataProvider object)
	 */
	public DataProvider() {
	}
	
	
	
	
	
	/**
	 * Searches database for Member object given a memberID, returns null if no matches.
	 * @param memberID, memberID to be used for search
	 * @return Member object with corresponding memberID
	 */
	public Member searchMember(String memberID) {
		for (int member = 0; member < allMembers.size(); member++) {
			if (allMembers.get(member).getMemberID().equals(memberID)) {
				return allMembers.get(member);
			}
		}
		return null;
	}

	
	
	
	
	/**
	 * Searches database for Bill object given a billID, returns null if no matches.
	 * @param billID, billID to be used for search
	 * @return Bill object with corresponding billID
	 */
	public Bill searchBill(String billID) {
		for (int bill = 0; bill < allBills.size(); bill++) {
			if (allBills.get(bill).getBillID().equals(billID)) {
				return allBills.get(bill);
			}
		}
		return null;
	}
	
	
	

	
	/**
	 * Adds bill/member file to database
	 * @param fileName, A path to the member file (e.g., members.csv/bills.csv)
	 * @param fileType, member file ('M') or bill file ('B')
	 * @throws DataAccessException If a file cannot be opened/read
	 * @throws DataFormatException If the format of the the content is incorrect
	 */
	public void addFileToDatabase (String fileName, char fileType) throws DataAccessException,
			DataFormatException {
		
		Scanner inputStream = null;
		String line = "";

		// checking member file validity and adds member information to database
		if (fileType == 'M') {
			this.memberFile = new File(fileName);

			try {
				inputStream = new Scanner(new FileInputStream(memberFile));
			} catch (FileNotFoundException e) {
				throw new DataAccessException("Member File: " + memberFile + " not found.");
			} catch (Exception e) {
				throw new DataAccessException();
			}

			while (inputStream.hasNextLine()) {
				line = inputStream.nextLine();
				String[] memberDetails = line.split(",");
				if (!((memberDetails.length == MEMBER_FILE_COLUMN_LENGTH) || 
						((memberDetails.length == 1) && (line.trim().equals(""))))) {
					throw new DataFormatException("Member File: " + memberFile + " contains row(s) "
							+ "with the wrong number of columns.");
				}
				if(memberDetails.length == 1) {
					continue;
				}
				String memberIDMemberFile = memberDetails[0].trim();
				String memberName = memberDetails[1].trim();
				String memberEmail = memberDetails[2].trim();
				
				if (!checkIDValidity(memberIDMemberFile)) {
					throw new DataFormatException("Member File: " + memberFile + " contains at "
							+ "least one invalid memberID.");
				}
				if (memberName.equals("") || memberEmail.equals("")) {
					throw new DataFormatException("Member File: " + memberFile + " has at least "
							+ "one row with incomplete member details.");
				}
				Member newMember = new Member(memberIDMemberFile, memberName, memberEmail);
				if (searchMember(memberIDMemberFile) != null) {
					Member sameMember = searchMember(memberIDMemberFile);
					if (!(sameMember.getMemberName().equals(memberName) && 
							sameMember.getMemberEmail().equals(memberEmail))) {
						throw new DataFormatException("Member File: " + memberFile + " contains "
								+ "rows with same memberID but different details.");
					}
				}
				allMembers.add(newMember);
			}
			if (allMembers.size() == 0) {
				throw new DataFormatException ("Member File: " + memberFile + " contains no valid "
						+ "data.");
			}
		}

		// checking bill file validity and adds bill information to database
		else {
			this.billFile = new File(fileName);
			try {
				inputStream = new Scanner(new FileInputStream(billFile));
			} catch (FileNotFoundException e) {
				throw new DataAccessException("Bill File: " + billFile + " not found.");
			} catch (Exception e) {
				throw new DataAccessException();
			}

			while (inputStream.hasNextLine()) {
				line = inputStream.nextLine();
				String[] billDetails = line.split(",");
				if (!((billDetails.length == BILL_FILE_COLUMN_LENGTH) || 
						((billDetails.length == 1) && (line.trim().equals(""))))){
					throw new DataFormatException("Bill File: " + billFile + " contains row(s) "
							+ "with the wrong number of columns.");
				}
				if(billDetails.length == 1) {
					continue;
				}
				
				String billID = String.valueOf(billDetails[0].trim());
				String memberID = String.valueOf(billDetails[1].trim());
				double billValue;
				boolean billUsed;
				
				try {
					billValue = Double.parseDouble(billDetails[2].trim());
					billUsed = Boolean.parseBoolean(billDetails[3].trim().toLowerCase());
				} catch (InputMismatchException e) {
					throw new DataFormatException("Bill File: " + billFile
							+ " contains at least one entry with the wrong data type.");
				} catch (Exception e) {
					throw new DataFormatException("Bill File: " + billFile + " contains incorrect "
							+ "data format somewhere.");
				}
				if (!checkIDValidity(billID)) {
					throw new DataFormatException("Bill File: " + billFile + " contains at "
							+ "least one row with invalid billID.");
				}
				if (!memberID.equals("")) {
					if (!checkIDValidity(memberID)) {
						throw new DataFormatException("Bill File: " + billFile + " contains at "
								+ "least one row with invalid non-empty memberID.");
					}
				}
				Bill newBill = new Bill(billID, memberID, billValue, billUsed);
				if (searchBill(billID)!=null) {
					Bill sameBill = searchBill(billID);
					if (!((sameBill.getMemberID().equals(memberID) && 
							(sameBill.getBillValue()==billValue) && 
							(sameBill.getBillUsed() == billUsed)))){
						throw new DataFormatException("Bill File: " + billFile + " contains "
								+ "rows with same BillID but different details.");
					}
				}
				if (searchBill(billID)!= null) {
					throw new DataFormatException("Bill File: " + billFile + " contains multiple "
							+ "rows with the same BillID. BillID must be distinct.");
				}
				if ((newBill.isValidBill() == true) && (newBill.getBillUsed() == false)) {
					existUsableBill = true;
				}
				allBills.add(newBill);
			}	
			if (allBills.size()==0) {
				throw new DataFormatException("Bill File: " + billFile + " contains no valid "
						+ "data.");
			}
			if (!existUsableBill) {
				throw new DataFormatException("Bill File: " + billFile + " contains no valid "
						+ "unused bill. A valid usable bill needs to satisfy all of the following:"
						+ " value >$50, has memberID and is still unused.");
			}
		}
	}
	
	
	
	
	
	/**
	 * Checks billID/memberID validity according to format.
	 * @param id (billID/memberID)
	 * @return boolean whether ID is valid.
	 */
	private boolean checkIDValidity(String id) {
		
		String testID = id;
		
		if (testID.length() == ID_LENGTH) {
			for (int i = 0; i < ID_LENGTH; i++) {
				int asciiValueTestID = testID.charAt(i);
				if (!(ASCII_0 <= asciiValueTestID && asciiValueTestID <= ASCII_9)) {
					return false;
				}
			}
		} else {
			return false;
		}

		return true;
	}
	

	
	
	
	/**
	 * Updates bill file when save option is selected.
	 */
	public void saveBillFile() {
		
		PrintWriter outputStream = null;
		
		try {
			outputStream = new PrintWriter(new FileOutputStream(billFile));
			for (int line = 0; line < allBills.size(); line++) {
				Bill currentBill = allBills.get(line);
				outputStream.println(currentBill.getBillID() + "," + currentBill.getMemberID() + ","
						+ currentBill.getBillValue() + "," + currentBill.getBillUsed());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error updating Bill file");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Error updating Bill file");
			System.exit(0);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	
	
	
	
}
