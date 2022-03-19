/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.Serializable;



/**
 * Member object relating to a memberID and contains more information about the member who has the
 * said memberID.
 * @author patriciawidjojo
 *
 */
public class Member implements Serializable{
	
	private String memberID = "";
	private String memberName = "";
	private String memberEmail = "";
	
	
	
	
	
	/**
	 * Empty constructor, set member details through using setters.
	 */
	public Member() {}
	
	
	
	
	
	/**
	 * Full constructor when member details are available
	 * @param memberID, valid 6-digit memberID as a string
	 * @param memberName, member's first name
	 * @param memberEmail, member's email
	 */
	public Member(String memberID, String memberName, String memberEmail) {
		this.memberID = memberID;
		this.memberName = memberName;
		this.memberEmail = memberEmail;
	}
	
	
	
	
	
	//setters and getter.
	public String getMemberID() {
		return memberID; 
	}
    
	public void setMemberID(String memberID) {
		this.memberID = memberID;
	}
    

	public String getMemberName() {
		return memberName; 
	}
    
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
    
	public String getMemberEmail() {
		return memberEmail; 
	}
    
	public void setMemberEmail(String memberEmail) {
		this.memberEmail = memberEmail;
	}

    
    
    
    
}
