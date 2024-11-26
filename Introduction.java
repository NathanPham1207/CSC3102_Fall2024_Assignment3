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

    /* Method that prints instruction options */
    public static void printStatments() {
        System.out.println("\n WELCOME !!! You can enter either name or enrollment number of student for all functions");
        System.out.println("1. Print members of a given student’s social network: ");
        System.out.println("2. Use Dijkstra's Algorithm to find the quickest path between two students\n: ");
        System.out.println("3. “Disconnect” a given student from another student’s network\n: ");
        System.out.println("4. Increase number of wait days: ");
        System.out.println("5. Decrease number of wait days: ");
        System.out.println("6. Exit.");
    }

    /* Method that parses a string to check if it can be converted to a valid integer.
     * If the parsed integer is greater than 0 and less than or equal to the size of an adjacency list,
     * the method returns TRUE. Otherwise, it returns FALSE. */
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

    /* Method that checks if the input is a string */
    public static boolean isString(String str) {
        try {
            String.valueOf(str);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }

    }

    /* Method that returns a student's ID number by searching for a name in SocialNetwork that matches the input */
    public static String getIdFromName(String studentName)
    {
        /* Initialize the student's ID to null
         * Make sure the student's name is a string */
        String	studentID = null;
        if (isString(studentName) == true)
        {
            /* Initializes the student's enrollment number 0
            * Split components of the student's name by spaces */
            int studentA = 0;
            String[] nameParts = studentName.split(" ");

            /* Check if the student's name consists of exactly two parts (first and last name) */
            if (nameParts.length != 2)
            {
                System.out.println("Invalid input for Name.");
            } else
            {
                /* Assign the first and last name from the split name parts */
                String firstName = nameParts[0];
                String lastName = nameParts[1];

                /* Iterate through every vertex in SocialNetwork's vertex list */
                for (SocialNetwork.Vertex vertex : vList.values())
                {
                    /* Check if the first and last name match the input */
                    if (vertex.getFirstName().equalsIgnoreCase(firstName)
                            && vertex.getLastName().equalsIgnoreCase(lastName))
                    {
                        /* Retrieve and store the enrollment number of the matching student */
                        studentA = vertex.getEnrollmentNum();
                        /* Convert the enrollment number to a string and assign it as the student's ID */
                        studentID = Integer.toString(studentA);
                        /* Print the ID associated with the given name */
                        System.out.println(studentName+ "'s is "+ studentID+".");
                        break;
                    }
                }
            }

        }
        return studentID;
    }

    /* Method that retrieves the name of a student based on their student ID */
    public static String getNameFromID(String input)
    {
        /* Initialize the student's name to "null" */
        String studentName = null;

        /* If the input is an integer */
        if(isInt(input) == true)
        {
            /* Get the enrollment number associated with the input */
            int enrollmentNO = Integer.parseInt(input);
            /* Check every vertex in SocialNetwork for a matching enrollment number */
            for(SocialNetwork.Vertex vertex : vList.values())
            {
                if(vertex.getEnrollmentNum() == enrollmentNO)
                {
                    /* For a matching enrollment number, retrieve the first and last name of the student */
                    studentName = vertex.getFirstName().concat(" ").concat(vertex.getLastName());
                    break;
                }
            }
        }
        return studentName;
    }

    /* Method that removes and edge between two nodes in the graph */
    public static void removeEdge(int source, int target)
    {
        /* Variable "removed" is a boolean that is initialized to "false" */
        boolean removed = false;
        /* If the adjacency list contains the source's key */
        if (socialNetwork.adjList.containsKey(source))
        {
            /* Gets the list of edges associated with the source */
            List<SocialNetwork.Edge> edges = socialNetwork.adjList.get(source);
            if (edges != null)
            {
                /* Iterate through the list */
                Iterator<SocialNetwork.Edge> iterator = edges.iterator();
                while (iterator.hasNext())
                {
                    /* When a matching edge is found, it gets removed from the list of edges */
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
                System.out.println("Connection from student enrollment number" + source + " to student enrollment number" + target + " is removed.");
            } else
            {
                System.out.println("No connection exists from student" + source + " to student" + target + ".");
            }
        }
        else
        {
            System.out.println("Student does not exist in the network.");
        }
    }

    /* Method that increases the wait day value for a student to add a node to their network */
    public static void increaseWaitDay(int studentNumber, int increase)
    {
        int originalWait = 0;
        int changedWait = 0;
        /* get the student's ID from their student number */
        String input = Integer.toString(studentNumber);
        /* Get the student's name from their ID*/
        String studentName = getNameFromID(input);
        /* Find studentNumber in the adjacency list */
        if (socialNetwork.adjList.containsKey(studentNumber))
        {
            for (SocialNetwork.Edge edge : socialNetwork.adjList.get(studentNumber))
            {
                /* Get the weight of the edge */
                originalWait = edge.getWait();
                /* Increase the weight of the edge */
                edge.setWait(edge.getWait() + increase);
                /* changedWait is the new wait day */
                changedWait = edge.getWait();
            }
            System.out.println("Enrollment Number: " + studentNumber + " - " + studentName+ ": " + "Original Wait "+ originalWait+ " changed to "+ changedWait + ".");
        } else
        {
            System.out.println("Student " + studentName + "does not exist in the network.");
        }
    }

    /* Method that decreases the wait day value for a student to add a node to their network */
    public static void decreaseWaitDay(int studentNumber, int decrease)
    {
        int originalWait = 0;
        int changedWait = 0;
        String input = Integer.toString(studentNumber);
        String studentName = getNameFromID(input);
        if (socialNetwork.adjList.containsKey(studentNumber))
        {
            for (SocialNetwork.Edge edge : socialNetwork.adjList.get(studentNumber))
            {
                originalWait = edge.getWait();
                edge.setWait(edge.getWait() - decrease);
                changedWait = edge.getWait();
            }
            System.out.println("Enrollment Number: " + studentNumber + " - " + studentName+ ": " + "Original Wait "+ originalWait+ " changed to "+ changedWait + ".");
        } else
        {
            System.out.println("Student " + studentName + "does not exist in the network.");
        }
    }

    /* Main method*/
    public static void main(String args[])
    {
        Scanner scanner = new Scanner(System.in);
        BufferedReader networkReader = null;
        BufferedReader studentReader = null;
        String networkSheet = "src\\network.csv";
        String studentSheet = "src\\students.csv";
        String var = null;
        String studentName = null;

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
                /* Print members of a given student’s social network */
                case 1:
                    System.out.println("Enter the enrollment number or the full name of the student (e.g., 'John Doe'): ");
                    scanner.nextLine(); // Consume any leftover newline
                    String inputPerson = scanner.nextLine();

                    if (isInt(inputPerson))
                    {
                        int enrollmentNumber = Integer.parseInt(inputPerson);
                        if (socialNetwork.adjList.get(enrollmentNumber) != null)
                        {
                            studentName = getNameFromID(inputPerson);
                            System.out.println("\n Student has enrollment number " + enrollmentNumber + ".(" + studentName + ")" + "'s network: ");
                            for (SocialNetwork.Edge edge : socialNetwork.adjList.get(enrollmentNumber))
                            {
                                for(SocialNetwork.Vertex vertex: vList.values())
                                {
                                    if(edge.getTo() == vertex.getEnrollmentNum())
                                    {
                                        System.out.print(edge.getTo()+ ": " + vertex.getFirstName()+ " " + vertex.getLastName() + " -> ");
                                    }
                                }

                            }
                            System.out.println();
                        }
                        else
                        {
                            System.out.println("The student with enrollment number " + studentName + " does not exist.");
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
                                        for(SocialNetwork.Vertex vertexx: vList.values())
                                        {
                                            if(edge.getTo() == vertexx.getEnrollmentNum())
                                            {
                                                System.out.print(edge.getTo()+ ": " + vertexx.getFirstName()+ " " + vertexx.getLastName() + " -> ");
                                            }
                                        }
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
                /* Use Dijkstra's Algorithm to find the quickest path between two students */
                case 2:
                    System.out.println("Student A(Enrollment number or name): ");
                    scanner.nextLine();
                    String studentID = scanner.nextLine();
                    if (isInt(studentID) == true)
                    {
                        int studentA = Integer.parseInt(studentID);
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
                /* “Disconnect” a given student from another student’s network (remove the edge between them) */
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

                /* Increase number of wait days for user-selected student. */
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

                /* Decrease number of wait days for user-selected student. */
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

                /* Terminate program */
                case 6:
                    scanner.close();
                    System.exit(0);
                    break;

            }
        }
    }
}