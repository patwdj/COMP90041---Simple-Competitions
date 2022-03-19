/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;



/**
 * Bill object representing one billID including its details.
 * @author patriciawidjojo
 *
 */
public class Bill implements Serializable{
	
	private String billID = "";
	private String memberID = "";
	private double billValue;
	private boolean billUsed;
	private boolean validBill = true;
	private int numberOfEntries;
	private final int ENTRY_VALUE = 50;
	
	
	
	
	
	/**
	 * Empty constructor, set bill details through using setters.
	 */
	public Bill() {}
	
	
	
	
	
	/**
	 * full constructor when member details are available
	 * @param billID, valid 6-digit billID as a string
	 * @param memberID, valid 6-digit memberID as a string if available, otherwise empty string
	 * @param billValue, bill total amount
	 * @param billUsed, true/false bill has been used in a competition
	 */
	public Bill(String billID, String memberID, double billValue, boolean billUsed) {
		this.billID = billID;
		this.memberID = memberID;
		this.billValue = billValue;
		this.billUsed = billUsed;
		numberOfEntries = (int) (billValue / ENTRY_VALUE);
		if (memberID.equals("")){
			validBill = false;
		}
		if (memberID.equals("")) {
			validBill = false;
		}
	}
	
	

	
	
	//setters and getters.
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
    
    public double getBillValue () {
    	return billValue;
    }
    public void setBillValue (double billValue) {
    	this.billValue = billValue;
    }
    public boolean getBillUsed() {
    	return billUsed;
    }
    public void setBillUsed(boolean billUsed) {
    	this.billUsed = billUsed;
    }
    
    public boolean isValidBill() {
    	return validBill;
    }
    
    public int getNumberOfEntries() {
		return numberOfEntries;
    }
    
    

    
    
}
