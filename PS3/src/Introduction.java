import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Introduction {

	static SocialNetwork socialNetwork = new SocialNetwork();
	static HashMap<Integer, SocialNetwork.Vertex> vList = new HashMap<Integer, SocialNetwork.Vertex>();
	
	public static void printStatments() {
		System.out.println("\n WELCOME !!! You can enter either name or enrollment number of student for all functions");
		System.out.println("1. Print network list for who: ");
		System.out.println("2. Dijkstra's Algor: ");
		System.out.println("3. Disconnect: ");
		System.out.println("4. Increase number of wait days: ");
		System.out.println("5. Decrease number of wait days: ");
		System.out.println("6. Exit.");
	}

	public static boolean isInt(String str) {
		try {
			if (Integer.parseInt(str) > 0 && Integer.parseInt(str) <= socialNetwork.adjList.size()) {
				Integer.parseInt(str);
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}

	}

	public static boolean isString(String str) {
		try {
			String.valueOf(str);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}

	}
	
	public static String getIdFromName(String studentName) 
	{
		String	studentID = null;
		if (isString(studentName) == true) 
		{
			int studentA = 0;
			String[] nameParts = studentName.split(" ");
			if (nameParts.length != 2) 
			{
				System.out.println("Invalid input for Name.");
			} else 
			{
				String firstName = nameParts[0];
				String lastName = nameParts[1];
				for (SocialNetwork.Vertex vertex : vList.values()) 
				{
					if (vertex.getFirstName().equalsIgnoreCase(firstName)
							&& vertex.getLastName().equalsIgnoreCase(lastName)) 
					{
						studentA = vertex.getEnrollmentNum();
						studentID = Integer.toString(studentA);
						System.out.println(studentName+ "'s is "+ studentID+".");
					}
				}
			}
			
		}
		return studentID;
	}
	
	public static void removeEdge(int source, int target) 
	{
		boolean removed = false;
		if (socialNetwork.adjList.containsKey(source)) 
		{
			List<SocialNetwork.Edge> edges = socialNetwork.adjList.get(source);
			if (edges != null) 
			{
				Iterator<SocialNetwork.Edge> iterator = edges.iterator();
				while (iterator.hasNext()) 
				{
					SocialNetwork.Edge edge = iterator.next();
					if (edge.getTo() == target) 
					{
						iterator.remove();
						removed = true;
						break;
					}
				}
			}
			if (removed) 
			{
				System.out.println("Connection from " + source + " to " + target + " removed.");
			} else 
			{
				System.out.println("No connection exists from " + source + " to " + target + ".");
			}
		} 
		else 
		{
			System.out.println("Student A does not exist in the network.");
		}
	}
	
	public static void increaseWaitDay(int studentNumber, int increase) 
	{
		int originalWait = 0;
		int changedWait = 0;
		if (socialNetwork.adjList.containsKey(studentNumber)) 
		{
			for (SocialNetwork.Edge edge : socialNetwork.adjList.get(studentNumber)) 
			{
				originalWait = edge.getWait();
				edge.setWait(edge.getWait() + increase);
				changedWait = edge.getWait();
			}
			System.out.println("Original Wait "+ originalWait+ " changed to "+ changedWait + " for student " + studentNumber + ".");
		} else 
		{
			System.out.println("Student does not exist in the network.");
		}
	}
	
	public static void decreaseWaitDay(int studentNumber, int decrease) 
	{
		int originalWait = 0;
		int changedWait = 0;
		if (socialNetwork.adjList.containsKey(studentNumber)) 
		{
			for (SocialNetwork.Edge edge : socialNetwork.adjList.get(studentNumber)) 
			{
				originalWait = edge.getWait();
				edge.setWait(edge.getWait() - decrease);
				changedWait = edge.getWait();
			}
			System.out.println("Original Wait "+ originalWait+ " changed to "+ changedWait + " for student " + studentNumber + ".");
		} else 
		{
			System.out.println("Student does not exist in the network.");
		}
	}	
	
	public static void main(String args[]) 
	{
		Scanner scanner = new Scanner(System.in);
		BufferedReader networkReader = null;
		BufferedReader studentReader = null;
		String networkSheet = "src\\network.csv";
		String studentSheet = "src\\students.csv";
		String var = null;
		
		try 
		{
			// Used hashmap to easily match enrollment number to weight, so it can be called
			// later.
			HashMap<Integer, Integer> waitMap = new HashMap<>();
			studentReader = new BufferedReader(new FileReader(studentSheet));
			String line2 = "";
			int lineNumberStudent = 0;
			while ((line2 = studentReader.readLine()) != null) 
			{
				String[] row2 = line2.split(",");
				lineNumberStudent++;
				try 
				{
					int enrollmentNum = Integer.parseInt(row2[0]);
					int wait = Integer.parseInt(row2[3]);
					waitMap.put(enrollmentNum, wait);
				} 
				catch (Exception e) 
				{
					try 
					{
						if(row2[0].isBlank() == true)
						{
							System.out.println("Enrollment field have blank value. Please check file students.csv again !!!");
						}
						if(!(row2[0].toUpperCase().equals("ENROLLMENT") && row2[1].toUpperCase().equals("FIRST NAME")
								&& row2[2].toUpperCase().equals("LAST NAME") && row2[3].toUpperCase().equals("WAIT"))) {
	                            System.out.println("Line " + lineNumberStudent + " from students.csv isn't formatted correctly.");
	                            Thread.sleep(350);
	                        }
					}
					catch(Exception exception) 
					{
                        System.out.println("Line " + lineNumberStudent +" is blank.");
                        Thread.sleep(350);
					}
					
				}
			}
			networkReader = new BufferedReader(new FileReader(networkSheet));
			String line1 = "";
			int lineNumberNetwork = 0;
			while ((line1 = networkReader.readLine()) != null) 
			{
				String[] row = line1.split(",");
				lineNumberNetwork++;
				try 
				{
					int enrollmentNum = Integer.parseInt(row[0]);
					String firstName = row[1];
					String lastName = row[2];
					
					SocialNetwork.Vertex vertex = socialNetwork.new Vertex(enrollmentNum, firstName, lastName);
					// Putting the vertices in a hashmap based on enrollment number.
					vList.put(enrollmentNum, vertex);
					socialNetwork.addVertex(enrollmentNum);
					for (int i = 3; i < row.length; i++) 
					{
						// Adding the weight from the hashmap.
						int weight = waitMap.getOrDefault(enrollmentNum, 0);
						SocialNetwork.Edge edge = socialNetwork.new Edge(enrollmentNum, Integer.parseInt(row[i]),
								weight);
						socialNetwork.addEdge(edge);
					}
				} 
				catch (Exception e) 
				{
					try 
					{
						if(row[0].isBlank() == true)
						{
							System.out.println("Enrollment field have blank value. Please check file network.csv again !!!");
						}
						if(!(row[0].toUpperCase().equals("ENROLLMENT") && row[1].toUpperCase().equals("FIRST NAME")
								&& row[2].toUpperCase().equals("LAST NAME") && row[3].toUpperCase().equals("CONNECTION 1")
								&& row[4].toUpperCase().equals("CONNECTION 2")&& row[5].toUpperCase().equals("CONNECTION 3")
								&& row[6].toUpperCase().equals("CONNECTION 4")&& row[7].toUpperCase().equals("CONNECTION 5"))) {
	                            System.out.println("Line " + lineNumberNetwork + " from network.csv isn't formatted correctly.");
	                            Thread.sleep(350);
	                        }
					}
					catch(Exception exception) 
					{
                        System.out.println("Line " + lineNumberNetwork +" is blank.");
                        Thread.sleep(350);
					}
					
				}
			}
		} 
		catch (Exception e) 
		{
		} 
		finally 
		{
			try 
			{
				if (networkReader != null)
					networkReader.close();
				if (studentReader != null)
					studentReader.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		
		
		while (true) 
		{
			printStatments();
			int input = 0;
			boolean isInt = false;
			while (isInt == false) 
			{
				if (scanner.hasNextInt()) 
				{
					input = scanner.nextInt();
					isInt = true;
				} else 
				{
					scanner.next();
					break;
				}
			}

		switch (input) 
		{
			case 1:
				System.out.println("Enter the enrollment number or the full name of the student (e.g., 'John Doe'): ");
				scanner.nextLine(); // Consume any leftover newline
				String inputPerson = scanner.nextLine();

				if (isInt(inputPerson)) 
				{
					int student = Integer.parseInt(inputPerson);
					if (socialNetwork.adjList.get(student) != null) 
					{
						System.out.println("\n Student has enrollment number " + student + "'s network: ");
						for (SocialNetwork.Edge edge : socialNetwork.adjList.get(student)) 
						{
							System.out.print(edge.getTo() + " -> ");
						}
						System.out.println();
					} 
					else 
					{
						System.out.println("The student with enrollment number " + student + " does not exist.");
					}
				} 
				else if (isString(inputPerson)) 
				{
					String[] nameParts = inputPerson.split(" ");
					if (nameParts.length < 2) 
					{
						System.out.println("Invalid input.");
						break;
					}
					String firstName = nameParts[0];
					StringBuilder lastNameBuilder = new StringBuilder();
					for (int i = 1; i < nameParts.length; i++) 
					{
						if (i > 1) 
						{
							lastNameBuilder.append(" ");
						}
						lastNameBuilder.append(nameParts[i]);
					}
					String lastName = lastNameBuilder.toString();
					boolean found = false;
					for (SocialNetwork.Vertex vertex : vList.values()) 
					{
						if (vertex.getFirstName().equalsIgnoreCase(firstName)
								&& vertex.getLastName().equalsIgnoreCase(lastName)) {
							System.out.println("\n" + firstName + " " + lastName + "'s network: ");
							if (socialNetwork.adjList.get(vertex.getEnrollmentNum()) != null) 
							{
								for (SocialNetwork.Edge edge : socialNetwork.adjList.get(vertex.getEnrollmentNum())) 
								{
									System.out.print(edge.getTo() + " -> ");
								}
								System.out.println();
							} else 
							{
								System.out.println("No connections found for " + firstName + " " + lastName + ".");
							}
							found = true;
							break;
						}
					}
					if (!found) 
					{
						System.out.println("Student with name " + firstName + " " + lastName + " does not exist.");
					}
				} 
				else 
				{
					System.out.println("Invalid input.");
				}
				break;

			case 2:
				System.out.println("Student A(Enrollment number or name): ");
				scanner.nextLine();
				String studentID = scanner.nextLine();
				if (isInt(studentID) == true) 
				{
					int studentA = Integer.parseInt(studentID);
					System.out.println("Student A:" + studentA);
					System.out.println("Student B: ");
					String studenta = scanner.nextLine();
					if (isInt(studenta) == true) 
					{
						int studentB = Integer.parseInt(studenta);
						socialNetwork.dijkstra(studentA, studentB);

					} 
					else 
					{
						System.out.println("Student doesn't exist");
					}
				} 
				else if (isString(studentID) == true) 
				{
					int studentA = 0;
					int studentB1 = 0;
					String tempStudentA = getIdFromName(studentID);
					try {
					studentA = Integer.parseInt(tempStudentA);
					}catch(Exception e) {}
					System.out.println("Student B: ");
					String studentB = scanner.nextLine();
					System.out.println(studentB);
					if (isString(studentB) == true) 
					{
						String tempStudentB = getIdFromName(studentB);
						try {
						studentB1 = Integer.parseInt(tempStudentB);
						}catch(Exception e) {}
					}
					socialNetwork.dijkstra(studentA, studentB1);
				}
				break;

			case 3:
				int studentA = 0;
				int studentB = 0;
		
				System.out.println("Enter the enrollment number or name of the student A (source): ");
				scanner.nextLine();
				String source = scanner.nextLine();						
						
				if (isInt(source) == true) 
				{
					try {
					studentA = Integer.parseInt(source);		
					}catch(Exception e) {}
					System.out.println("Enter the enrollment number or name of the student B (target): ");
					String	target2 = scanner.nextLine();
					if(isInt(target2) == true) 
					{
						try {
						studentB = Integer.parseInt(target2);
						}catch (Exception e) {}
						removeEdge(studentA,studentB);
					} 
					else if(isString(target2) == true) 
					{

						String studentB1 = getIdFromName(target2);
						try {
						studentB = Integer.parseInt(studentB1);
						} catch(Exception e) {}
						removeEdge(studentA, studentB);
					}			
				} 
				else if (isString(source) == true) 
				{
					String tempStudentA = getIdFromName(source);
					try {
					studentA = Integer.parseInt(tempStudentA);		
					}catch(Exception e) {}
					System.out.println("Enter the enrollment number or name of the student B (target): ");
					String target1 = scanner.nextLine();
					if(isInt(target1) == true) 
					{
						try {
						studentB = Integer.parseInt(target1);
						} catch(Exception e) {}
						removeEdge(studentA, studentB);
					} 
					else if(isString(target1) == true) 
					{	

						System.out.println(target1);
						String tempStudentB = getIdFromName(target1);
						try {
						studentB = Integer.parseInt(tempStudentB);
						} catch (Exception e) {}
						removeEdge(studentA, studentB);
					}
				}
				break;

			case 4:
				int increase = 0;
				boolean validIncreaseInput = false;
				System.out.println("Enter the enrollment number or name of the student to increase wait days: ");
				scanner.nextLine();
				var = scanner.nextLine();
				
				while (!validIncreaseInput) 
				{
					try 
					{
						System.out.println("Enter the number of days to increase (FROM 0 - 100): ");
						
							increase = scanner.nextInt();
							validIncreaseInput = true;
					
							break;
							
						
					}
					catch(InputMismatchException e) 
					{
						System.out.println("Invalid input. Please enter an integer.");
						scanner.next(); // Clear the invalid input from the scanner
					}
				}
				if (isInt(var) == true) 
				{
					int studentNumber = 0;
					try {
					 studentNumber = Integer.parseInt(var);
					}catch(Exception e) {}
					if(studentNumber <0 || studentNumber<= 100) {
						increaseWaitDay(studentNumber, increase);
					}
					else {
						System.out.println("Invalid input for number of days.");
						break;
					}
				}
				else if (isString(var) == true)
				{
					int studentNumber = 0;
					String tempStudent = getIdFromName(var);
					try {
					studentNumber = Integer.parseInt(tempStudent);
					}catch(Exception e) {}
					if(studentNumber <0 || studentNumber<= 100) {
						increaseWaitDay(studentNumber, increase);
					}else {
						
						break;
					}
				}
				
				break;

			case 5:
				int decrease = 0;
				boolean validDecreaseInput = false;
				System.out.println("Enter the enrollment number or name of the student to decrease wait days: ");
				scanner.nextLine()	;
				var = scanner.nextLine();
				
				while (!validDecreaseInput) 
				{
					try 
					{
						System.out.println("Enter the number of days to decrease (FROM 0 - 100): ");
						decrease = scanner.nextInt();
						validDecreaseInput = true;
					}
					catch(InputMismatchException e) 
					{
						System.out.println("Invalid input. Please enter an integer.");
						scanner.next(); // Clear the invalid input from the scanner
					}
				}
				if (isInt(var) == true) 
				{
					int studentNumber = 0;
					try {
					 studentNumber= Integer.parseInt(var);
					}catch(Exception e) {}
					if(studentNumber <0 || studentNumber<= 100) {
					decreaseWaitDay(studentNumber, decrease);
					}else{
						System.out.println("Invalid input for number of days.");
						break;
					}
				}
				else if (isString(var) == true)
				{
					int studentNumber = 0;
					String tempStudent = getIdFromName(var);
					try {
					studentNumber = Integer.parseInt(tempStudent);
					}catch (Exception e) {}
					if(studentNumber <0 || studentNumber<= 100) {
					decreaseWaitDay(studentNumber, decrease);
					}else{
						System.out.println("Invalid input for number of days.");
						break;
					}
				}
				break;

			case 6:
				scanner.close();
				System.exit(0);
				break;

			}
		}
	}	
}
