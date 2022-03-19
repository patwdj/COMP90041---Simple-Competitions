/*
 * Student name: Patricia Widjojo
 * Student ID: 913557
 * LMS username: pwidjojo
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;



/**
 * SimpleCompetitions program where users can create LuckyNumbersCompetition and 
 * RandomPickCompetition to select winners.
 * @author patriciawidjojo
 *
 */
public class SimpleCompetitions implements Serializable {

	private static final int MENU_CHOICE1 = 1;
	private static final int MENU_CHOICE2 = 2;
	private static final int MENU_CHOICE3 = 3;
	private static final int MENU_CHOICE4 = 4;
	private static final int MENU_CHOICE5 = 5;

	// static variables
	public static char testMode = 'X';
	public static Scanner keyboard = new Scanner(System.in);

	
	
	
	
	/**
	 * SimpleCompetitions creates lucky draws competitions, a $50 bill value = one entry.
	 * Main program that uses the main SimpleCompetitions class
	 * @param args main program arguments
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// set-up variables
		int id = 1;
		boolean existActiveCompetition = false;
		boolean existCompetition = false;
		int menuChoice = 0;
		char loadCompetition = 'X';
		ArrayList<Competition> allCompetitions = new ArrayList<Competition>();
		DataProvider database = null;

		System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----");

		// ask for valid test mode input
		while (!(loadCompetition == 'Y' || loadCompetition == 'N')) {
			System.out.println("Load competitions from file? (Y/N)?");
			String inputLoad = keyboard.nextLine().toUpperCase();
			if (inputLoad.length() == 1) {
				loadCompetition = inputLoad.charAt(0);
			}
			if (!(loadCompetition == 'Y' || loadCompetition == 'N')) {
				System.out.println("Unsupported option. Please try again!");
			}
		}

		// if selected, load competition list from file and initialises relevant variables.
		if (loadCompetition == 'Y') {
			ObjectInputStream inputStream = null;
			System.out.println("File name:");
			String fileName = keyboard.nextLine();
			File file = new File(fileName);
			try {
				inputStream = new ObjectInputStream(new FileInputStream(file));
				allCompetitions = (ArrayList<Competition>) inputStream.readObject();
				if (allCompetitions.size() > 0) {
					existCompetition = true;
					Competition lastCompetition = allCompetitions.get((allCompetitions.size() - 1));
					existActiveCompetition = lastCompetition.getIsActiveCompetition();
					testMode =  lastCompetition.getTestMode();
					if (existActiveCompetition) {
						id = lastCompetition.getCompetitionID();
					} else {
						id = lastCompetition.getCompetitionID() + 1;
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("File: " + fileName + " could not be found.");
				System.out.println("Goodbye!");
				System.exit(0);
			} catch (InvalidClassException e) {
				System.out.println("File: " + fileName + " has the wrong object type");
				System.out.println("Goodbye!");
				System.exit(0);
			} catch (ClassNotFoundException e) {
				System.out.println("Problems with reading object from file: " + fileName);
				System.out.println("Goodbye!");
				System.exit(0);
			} catch (IOException e) {
				System.out.println("File: " + fileName + " has no relevant object or error with "
						+ "file input.");
				System.out.println("Goodbye!");
				System.exit(0);
			} catch (Exception e) {
				System.out.println("Error Opening File");
				System.out.println("Goodbye!");
				System.exit(0);
			}
		}

		// if not load from file, select testing mode.
		if (loadCompetition == 'N') {
			while (!(testMode == 'T' || testMode == 'N')) {
				System.out.println("Which mode would you like to run? (Type T for Testing, and"
						+ " N for Normal mode):");
				String inputMode = keyboard.nextLine().toUpperCase();
				if (inputMode.length() == 1) {
					testMode = inputMode.charAt(0);
				}
				if (!(testMode == 'T' || testMode == 'N')) {
					System.out.println("Invalid mode! Please choose again.");
				}
			}
		}

		// input member and bill files, check if both files (including contents) are valid.
		try {
			System.out.println("Member file: ");
			String memberFile = keyboard.nextLine();
			database = new DataProvider();
			database.addFileToDatabase(memberFile, 'M');
			System.out.println("Bill file: ");
			String billFile = keyboard.nextLine();
			database.addFileToDatabase(billFile, 'B');
		} catch (DataFormatException e) {
			System.out.println(e.getMessage());
			System.out.println("Goodbye!");
			System.exit(0);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			System.out.println("Goodbye!");
			System.exit(0);
		} catch (Exception e) {
			System.out.println("Error opening file(s).");
			System.out.println("Goodbye!");
			System.exit(0);
		}

		// Check menu input choice validity and handle actions accordingly
		while (menuChoice != MENU_CHOICE5) {

			menuChoice = checkMenuInput(existActiveCompetition, existCompetition);

			switch (menuChoice) {

			case (MENU_CHOICE1):
				existCompetition = true;
				existActiveCompetition = true;
				char competitionType = 'X';

				while (!(competitionType == 'L' || competitionType == 'R')) {
					System.out.println("Type of competition (L: LuckyNumbers, R: RandomPick)?:");
					String inputCompetitionType = keyboard.nextLine().toUpperCase();
					if (inputCompetitionType.length() == 1) {
						competitionType = inputCompetitionType.charAt(0);
					}
					if (!(competitionType == 'L' || competitionType == 'R')) {
						System.out.println("Invalid competition type! Please choose again.");
					}
				}

				if (competitionType == 'R') {
					RandomPickCompetition newRCompetition = new RandomPickCompetition(id, database);
					allCompetitions.add(newRCompetition);
				} else {
					LuckyNumbersCompetition newLCompetition = new LuckyNumbersCompetition(id,
							database);
					allCompetitions.add(newLCompetition);
				}
				break;

			case (MENU_CHOICE2):
				allCompetitions.get(allCompetitions.size()-1).addEntries();
				break;

			case (MENU_CHOICE3):
				Competition activeCompetition = allCompetitions.get(allCompetitions.size() - 1);
				if (activeCompetition.getCountEntries() == 0) {
					System.out.println("The current competition has no entries yet!");
					break;
				}
				activeCompetition.drawWinners();
				existActiveCompetition = false;
				id += 1;
				break;

			case (MENU_CHOICE4):
				report(existActiveCompetition, allCompetitions);
				break;

			case (MENU_CHOICE5):
				char saveCompetition = 'X';
				while (!(saveCompetition == 'Y' || saveCompetition == 'N')) {
					System.out.println("Save competitions to file? (Y/N)?");
					String inputSave = keyboard.nextLine().toUpperCase();
					if (inputSave.length() == 1) {
						saveCompetition = inputSave.charAt(0);
					}
					if (!(saveCompetition == 'Y' || saveCompetition == 'N')) {
						System.out.println("Unsupported option. Please try again!");
					} else if (saveCompetition == 'N') {
						System.out.println("Goodbye!");
						System.exit(0);
					}
				}
				
				ObjectOutputStream outputStream = null;
				String saveFileName = "";
				try {
					System.out.println("File name:");
					saveFileName = keyboard.nextLine();
					outputStream = new ObjectOutputStream(new FileOutputStream(saveFileName));
					outputStream.writeObject(allCompetitions);
					System.out.println("Competitions have been saved to file.");
					database.saveBillFile();
					System.out.println("The bill file has also been automatically updated.");
					System.out.println("Goodbye!");
				} catch (FileNotFoundException e) {
					System.out.println("Invalid file saving name.");
					System.out.println("Goodbye!");
					System.exit(0);
				} catch (IOException e) {
					System.out.println("Could not write to file: " + saveFileName);
					System.out.println("Goodbye!");
					System.exit(0);
				} catch (Exception e) {
					System.out.println("Error saving to file.");
					System.out.println("Goodbye!");
					System.exit(0);
				}

				finally {
					if (outputStream != null) {
						try {
							outputStream.close();
						} catch (IOException e) {
							System.out.println("Error closing!");
							System.exit(0);
						}
					}
				}
			}
		}
		
		keyboard.close();
	}

	
	
	
	
	/** 
	 * Gets a valid user menu input, if invalid input is received, throws errors and repeats.
	 * @param existActiveCompetition, boolean is there an active competition currently
	 * @param existCompetition, boolean has there been any competition at all
	 * @return valid menu choice given current conditions
	 */
	public static int checkMenuInput(boolean existActiveCompetition, boolean existCompetition) {

		int menuChoice = 0;
		boolean validMenuChoice = false;

		do {
			System.out.println("Please select an option. Type 5 to exit.");
			System.out.println("1. Create a new competition");
			System.out.println("2. Add new entries");
			System.out.println("3. Draw winners");
			System.out.println("4. Get a summary report");
			System.out.println("5. Exit");

			try {
				menuChoice = keyboard.nextInt();

				// invalid input checking

				if (!(menuChoice >= MENU_CHOICE1 && menuChoice <= MENU_CHOICE5)) {
					System.out.println("Unsupported option. Please try again!");
				} else if (menuChoice == MENU_CHOICE1 && existActiveCompetition) {
					System.out.println("There is an active competition. SimpleCompetitions does not"
							+ " support concurrent competitions!");
				} else if ((menuChoice == MENU_CHOICE2 || menuChoice == MENU_CHOICE3)
						&& !(existActiveCompetition && existCompetition)) {
					System.out.println("There is no active competition. Please create one!");
				} else if (menuChoice == MENU_CHOICE4 && !existCompetition) {
					System.out.println("No competition has been created yet!");
				} else
					validMenuChoice = true;

			} catch (InputMismatchException e) {
				System.out.println("A number is expected. Please try again.");
			} catch (Exception e) {
				System.out.println("Menu choice input error. Please try again.");
			} finally {
				keyboard.nextLine();
			}

		} while (!validMenuChoice);
		return menuChoice;

	}

	
	
	
	
	/**
	 * Prints a report of all past and current active competitions.
	 * @param existActiveCompetition, boolean is there an active competition currently
	 * @param allCompetitions, arraylist of all competitions thus far.
	 */
	public static void report(boolean existActiveCompetition,
			ArrayList<Competition> allCompetitions) {

		int completedCount;
		int activeCount = 0;

		if (existActiveCompetition) {
			completedCount = allCompetitions.size() - 1;
			activeCount = 1;
		} else {
			completedCount = allCompetitions.size();
		}

		System.out.println("----SUMMARY REPORT----");
		System.out.println("+Number of completed competitions: " + completedCount);
		System.out.println("+Number of active competitions: " + activeCount);

		for (int competition = 0; competition < allCompetitions.size(); competition++) {
			allCompetitions.get(competition).report();
		}
	}

	
	
	
	
}
