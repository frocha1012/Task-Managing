import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main_Interface {
    public static ArrayList<Task> all_tasks = new ArrayList<>();
    public static ArrayList<User> existing_users = new ArrayList<>();
    public static ArrayList<Admin> existing_admins  = new ArrayList<>();
    public static ArrayList<UserManager> existing_user_managers  = new ArrayList<>();


    public static void main(String[] args){
        try {
            String line;
            String[] divided_line;
            Scanner sc = new Scanner(new File("src/users.txt"));
            while (sc.hasNextLine()){
                line = sc.nextLine();
                divided_line = line.split(",");
                existing_users.add(new User(divided_line[0].trim(), divided_line[1].trim() , divided_line[2].trim()));
            }
            sc = new Scanner(new File("src/usermanagers.txt"));
            while (sc.hasNextLine()){
                line = sc.nextLine();
                divided_line = line.split(",");
                existing_user_managers.add(new UserManager(divided_line[0].trim(), divided_line[1].trim() , divided_line[2].trim()));
            }
            sc = new Scanner(new File("src/admins.txt"));
            while (sc.hasNextLine()){
                line = sc.nextLine();
                divided_line = line.split(",");
                existing_admins.add(new Admin(divided_line[0].trim(), divided_line[1].trim() , divided_line[2].trim()));
            }

            sc = new Scanner(new File("src/tasks.txt"));
            while(sc.hasNextLine()){
             String admin,start_date,end_date,is_completed,description,collaborators;
             line = sc.nextLine();
             divided_line = line.split(",");
             all_tasks.add(new Task(divided_line[0], divided_line[1],divided_line[2],divided_line[3],divided_line[4],divided_line[5]));
            }
            mainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void mainMenu(){
        int choice = 0;
        Scanner sc = new Scanner(System.in);
        do{
            System.out.println("------Choices");
            System.out.println("------Press 1 to login as User");
            System.out.println("------Press 2 to login as Admin");
            System.out.println("------Press 3 to login as User Manager");
            System.out.println("------Press 4 to exit");
            try {
                choice = sc.nextInt();
                switch (choice){
                    case 1:
                        userFunctions();
                        break;
                    case 2:
                        adminFunctions();
                        break;
                    case 3:
                        userManagerFunctions();
                        break;
                    case 4:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Please enter a valid input");
                }
            }
            catch (Exception e){
                System.out.println("Please enter the correct integer");
            }
        }   while (choice!=4);
    }


    public static void userFunctions(){
        User user;
        Scanner sc = new Scanner(System.in);
        String username;
        String password;
        System.out.println("Enter your username");
        username = sc.nextLine();
        if(isUserPresent(username, existing_users)){
            System.out.println("Enter your password");
            password = sc.nextLine();
            if(isPasswordCorrect(username, password, existing_users)){
                user = getUserFromUserName(username, existing_users);

                System.out.println("Login successful, what you want to do?");
                int choice = 0;
                do{
                    String input_string;
                    System.out.println("------Choices");
                    System.out.println("------Press 1 to change your name");
                    System.out.println("------Press 2 to change your User ID");
                    System.out.println("------Press 3 to change your password");
                    System.out.println("------Press 4 to add a new task");
                    System.out.println("------Press 5 to print all tasks");
                    System.out.println("------Press 6 to add a collaborator to a task");
                    System.out.println("------Press 7 to remove a collaborator from a task");
                    System.out.println("------Press 8 to list the tasks between two dates");
                    System.out.println("------Press 9 to log out");

                    choice = sc.nextInt();
                    switch (choice){
                        case 1:
                            changeCredentials(user, "name", existing_users);
                            break;
                        case 2:
                            changeCredentials(user, "username", existing_users);
                            break;
                        case 3:
                            changeCredentials(user, "password", existing_users);
                            break;
                        case 4:
                            try {
                                Task temporary = new Task();
                                temporary.admin = user;
                                all_tasks.add(temporary);
                                updateTasks("src/tasks.txt", all_tasks);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 5:
                            printTasks(getMyTask(all_tasks, username, existing_users));
                            break;
                        case 6:
                            if(existing_users.size() == 1){
                                System.out.println("No user exists other than you!!");
                                break;
                            }
                            ArrayList<Task> myTasks = getMyAdminTasks(all_tasks, username, existing_users);
                            if(myTasks.size() == 0){
                                System.out.println("You don't have any tasks!!");
                                break;
                            }
                            int temp = 0;
                            int sec_temp = 0;

                            do{
                                System.out.println("choices");
                                for(int i = 0 ; i <myTasks.size() ; i++){
                                    System.out.println("press " + (i + 1) + " for :" + "\n" + myTasks.get(0).toString());
                                }
                                temp = sc.nextInt();
                                temp--;
                                if(temp>=myTasks.size()|| temp<0){
                                    System.out.println("Not a valid option");
                                }
                            }while(temp>=myTasks.size()|| temp<0);

                            do{
                                System.out.println("Available collaborators");
                                for(int i = 0 ; i <existing_users.size() ; i++){
                                    System.out.println("press " + (i + 1) + " to add :" + "\n" + existing_users.get(0).username);
                                }
                                sec_temp = sc.nextInt();
                                sec_temp--;
                                if(sec_temp>=existing_users.size()|| sec_temp<0){
                                    System.out.println("Not a valid option");
                                }
                            }while (sec_temp>=existing_users.size()|| sec_temp<0);
                            if(myTasks.get(temp).users.contains(existing_users.get(sec_temp))){
                                System.out.println("The user is already included in the task");
                                break;
                            }
                            if(myTasks.get(temp).admin == existing_users.get(sec_temp)){
                                System.out.println("You are already in the project");
                                break;
                            }
                            myTasks.get(temp).users.add(existing_users.get(sec_temp));
                            break;
                        case 7:
                            try {
                                removeCollab(getMyAdminTasks(all_tasks, username, existing_users));
                            } catch (IOException e) {
                                System.out.println("An error occurred");
                            }
                            break;
                        case 8:
                            if(getMyTask(all_tasks, username, existing_users).size() == 0){
                                System.out.println("You don't have any tasks to show");
                                break;
                            }

                            CustomDate start_date;
                            CustomDate end_date;
                            System.out.println("Enter the start date in the format dd/mm/yy");
                            sc.nextLine();
                            String input = sc.nextLine();
                            String[] input_date = input.split("/");
                            try {
                                start_date = new CustomDate(Integer.parseInt(input_date[0]), Integer.parseInt(input_date[1]) , Integer.parseInt(input_date[2]));
                                System.out.println("Enter the end date in the format dd/mm/yy");
                                input = sc.nextLine();
                                input_date = input.split("/");
                                end_date = new CustomDate(Integer.parseInt(input_date[0]), Integer.parseInt(input_date[1]) , Integer.parseInt(input_date[2]));

                                if(!start_date.isBefore(end_date)){
                                    throw new Exception("The end date cannot be before the start date");
                                }

                                ArrayList<Task> myAllTasks = getMyTask(all_tasks, username, existing_users);
                                for(Task task : myAllTasks){
                                    if(task.start_date.isBefore(start_date) && end_date.isBefore(task.end_date)){
                                        System.out.println(task.toString());
                                    }
                                }
                            }
                            catch (Exception e){
                                System.out.println(e.getMessage());
                            }
                            break;
                        case 9:
                            System.out.println("User logged out successfully");
                            break;
                        default:
                            System.out.println("Not a valid option");
                    }
                }while (choice!=9);
            }
            else {
                System.out.println("Incorrect password, Access Denied");
            }
        }
        else {
            System.out.println("The username is not present in the records");
        }
    }


    public static void adminFunctions(){
        Admin user;
        Scanner sc = new Scanner(System.in);
        String username;
        String password;
        System.out.println("Enter your username");
        username = sc.nextLine();
        if(isUserPresent(username, existing_admins)){
            System.out.println("Enter your password");
            password = sc.nextLine();
            if(isPasswordCorrect(username, password, existing_admins)){
                user = getUserFromUserName(username, existing_admins);
                System.out.println("Login successful, what you want to do?");
                int choice = 0;
                do{
                    System.out.println("------Choices");
                    System.out.println("------Press 1 for user Manager functions");
                    System.out.println("------Press 2 to change user-manager name");
                    System.out.println("------Press 3 to change user-manager username");
                    System.out.println("------Press 4 to change user-manager password");
                    System.out.println("------Press 5 to ");
                    System.out.println("------Press 6 to logout");

                    choice = sc.nextInt();
                    String input;

                    switch (choice){
                        case 1:
                            userManagerAllFuntions(user, existing_admins);
                            break;
                        case 2:
                            System.out.println("Enter the username of the user");
                            sc.nextLine();
                            input = sc.nextLine();
                            if(!isUserPresent(input, existing_user_managers)){
                                System.out.println("The user doesn't exist!!");
                                break;
                            }
                            changeCredentials(getUserFromUserName(input, existing_user_managers), "name", existing_user_managers);
                            break;
                        case 3:
                            System.out.println("Enter the username of the user");
                            sc.nextLine();
                            input = sc.nextLine();
                            if(!isUserPresent(input, existing_user_managers)){
                                System.out.println("The user doesn't exist!!");
                                break;
                            }
                            changeCredentials(getUserFromUserName(input, existing_user_managers), "username",existing_user_managers);
                            break;
                        case 4:
                            System.out.println("Enter the username of the user");
                            sc.nextLine();
                            input = sc.nextLine();
                            if(!isUserPresent(input, existing_user_managers)){
                                System.out.println("The user doesn't exist!!");
                                break;
                            }
                            changeCredentials(getUserFromUserName(input, existing_user_managers), "password", existing_user_managers);
                            break;
                        case 5:

                            break;
                        case 6:
                            System.out.println("Logged out successfully");
                            break;
                        default:
                            System.out.println("Not a valid input");
                            break;
                    }
                }while (choice!=5);
            }
            else {
                System.out.println("Incorrect password, Access Denied");
            }
        }
        else {
            System.out.println("The username is not present in the records");
        }
    }

    public static void userManagerFunctions(){
        UserManager user;
        Scanner sc = new Scanner(System.in);
        String username;
        String password;
        System.out.println("Enter your username");
        username = sc.nextLine();
        if(isUserPresent(username, existing_user_managers)){
            System.out.println("Enter your password");
            password = sc.nextLine();
            if(isPasswordCorrect(username, password, existing_users)){
                user = getUserFromUserName(username, existing_user_managers);
                System.out.println("Login successful, what you want to do?");
                userManagerAllFuntions(user,existing_user_managers);
            }
            else {
                System.out.println("Incorrect password, Access Denied");
            }
        }
        else {
            System.out.println("The username is not present in the records");
        }
    }





    public static<Generic extends UserInterface>  boolean isUserPresent(String username, ArrayList<Generic> input_users){
        for (Generic user: input_users) {
            if(user.getUsername().equals(username) ){
                return true;
            }
        }
        return false;
    }
    public static<Generic extends UserInterface>  boolean isPasswordCorrect(String username, String password, ArrayList<Generic> input_users){
        for (Generic user: input_users) {
            if(user.getUsername().equals(username) ){
                if(user.getPassword().equals(password)){
                    return true;
                }
            }
        }
        return false;
    }
    public static<Generic extends UserInterface>  Generic getUserFromUserName(String username, ArrayList<Generic> input_users){
        for (Generic user: input_users) {
            if(user.getUsername().equals(username) ){
                return user;
            }
        }
        return null;
    }

    public static<Generic extends UserInterface> ArrayList<Task> getMyTask(ArrayList<Task> input, String username, ArrayList<Generic> input_users){
        ArrayList<Task> output = new ArrayList<>();
        for(Task task : input){
            if(task.users.contains(getUserFromUserName(username, input_users))||task.admin == getUserFromUserName(username, input_users)){
                output.add(task);
            }
        }
        return output;
    }


    public static<Generic extends UserInterface> ArrayList<Task> getMyAdminTasks(ArrayList<Task> input, String username, ArrayList<Generic> input_users){
        ArrayList<Task> output = new ArrayList<>();
        for(Task task : input){
            if(task.admin == getUserFromUserName(username, input_users)){
                output.add(task);
            }
        }
        return output;
    }

    public static<Generic extends UserInterface> ArrayList<Task> getMyCollabTasks(ArrayList<Task> input, String username, ArrayList<Generic> input_users){
        ArrayList<Task> output = new ArrayList<>();
        for(Task task : input){
            if(task.users.contains(getUserFromUserName(username, input_users))){
                output.add(task);
            }
        }
        return output;
    }

    public static<Generic extends UserInterface> void changeCredentials(Generic user, String what,ArrayList<Generic> existing_input){
        String input_string;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the new "+ what);
        try {
            input_string = sc.nextLine();
            switch (what) {
                case "name" -> user.setName(input_string);
                case "username" -> user.setUsername(input_string);
                case "password" -> user.setPassword(input_string);
            }
            File obj = new File("src/users.txt");
            FileWriter writer = new FileWriter(obj);
            String output = "";
            for(Generic us: existing_input){
                output += us.getName() +"," +  us.getUsername()+"," + us.getPassword() + "\n";
            }
            writer.write(output);
            System.out.println(what+ " changed successfully");
            writer.flush();
            writer.close();
        }
        catch (Exception e){
            System.out.println("An error occurred");
        }
    }


    public static int removeCollab(ArrayList<Task> myAdminTasks) throws IOException {
        Scanner sc = new Scanner(System.in);
        if(myAdminTasks.size() == 0){
            System.out.println("You don't have any tasks!!");
            return 0;
        }
        int temporary = 0;
        int sec_temporary = 0;

        do{
            System.out.println("choices");
            for(int i = 0 ; i <myAdminTasks.size() ; i++){
                System.out.println("press " + (i + 1) + " for :" + "\n" + myAdminTasks.get(0).toString());
            }
            temporary = sc.nextInt();
            temporary--;
            if(temporary>=myAdminTasks.size()|| temporary<0){
                System.out.println("Not a valid option");
            }
        }while(temporary>=myAdminTasks.size()|| temporary<0);

        if(myAdminTasks.get(temporary).users.size() == 0){
            System.out.println("There are no collaborators in this task");
            return 0;
        }
        do{
            System.out.println("Available collaborators");
            for(int i = 0 ; i <myAdminTasks.get(temporary).users.size() ; i++){
                System.out.println("press " + (i + 1) + " to add :" + "\n" + myAdminTasks.get(temporary).users.get(0).username);
            }
            sec_temporary = sc.nextInt();
            sec_temporary--;
            if(sec_temporary>=myAdminTasks.get(temporary).users.size()|| sec_temporary<0){
                System.out.println("Not a valid option");
            }
        }while (sec_temporary>=myAdminTasks.get(temporary).users.size()|| sec_temporary<0);

        System.out.println(myAdminTasks.get(temporary).users.get(sec_temporary).username + " removed successfully");
        myAdminTasks.get(temporary).users.remove(myAdminTasks.get(temporary).users.get(sec_temporary));

        updateTasks("src/tasks.txt", all_tasks);
        return 0;
    }

    public static<Generic extends UserInterface> void updateFiles(String fileLocation, ArrayList<Generic> existing_input) throws IOException {
        File obj = new File("src/users.txt");
        FileWriter writer = new FileWriter(obj);
        String output = "";
        for(Generic us: existing_input){
            output += us.getName() +"," +  us.getUsername()+"," + us.getPassword() + "\n";
        }
        writer.write(output);
        writer.flush();
        writer.close();
    }

    public static void updateTasks(String fileLocation, ArrayList<Task> tasks) throws IOException {
        File obj = new File(fileLocation);
        FileWriter writer = new FileWriter(obj);
        String output = "";
        for(Task task: tasks){
            String mid = task.completed?"true":"false";
            output += task.description + "," + task.start_date.toString() + ","+ task.end_date.toString() + "," + mid + "," + task.admin.username + ","+task.getAllCollabs() + "\n";
        }
        writer.write(output);
        writer.flush();
        writer.close();
    }

    public static void printTasks(ArrayList<Task> input){
        for(Task task : input){
            if(!task.completed){
                System.out.println(task.toString());
                System.out.println("\n\n");
            }
        }
    }


    public static<Generic extends UserInterface> void userManagerAllFuntions(Generic user, ArrayList<Generic> existing_input){
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do{
            System.out.println("------Choices");
            System.out.println("------Press 1 to change your name");
            System.out.println("------Press 2 to change your User ID");
            System.out.println("------Press 3 to change your password");
            System.out.println("------Press 4 to change a user's name");
            System.out.println("------Press 5 to change a user's username");
            System.out.println("------Press 6 to change a user's password");
            System.out.println("------Press 7 to mark task as complete");
            System.out.println("------Press 8 to delete a task");
            System.out.println("------Press 9 to remove a collaborator from a task");
            System.out.println("------Press 10 to print all tasks");
            System.out.println("------Press 11 to add a user");
            System.out.println("------Press 12 to remove a user");
            System.out.println("------Press 13 to log out");
            choice = sc.nextInt();
            String input;
            switch (choice){
                case 1:
                    changeCredentials(user, "name", existing_input);
                    break;
                case 2:
                    changeCredentials(user, "username", existing_input);
                    break;
                case 3:
                    changeCredentials(user, "password", existing_input);
                    break;
                case 4:
                    System.out.println("Enter the username of the user");
                    sc.nextLine();
                    input = sc.nextLine();
                    if(!isUserPresent(input, existing_users)){
                        System.out.println("The user doesn't exist!!");
                        break;
                    }
                    changeCredentials(getUserFromUserName(input, existing_users), "name", existing_users);
                    break;
                case 5:
                    System.out.println("Enter the username of the user");
                    sc.nextLine();
                    input = sc.nextLine();
                    if(!isUserPresent(input, existing_users)){
                        System.out.println("The user doesn't exist!!");
                        break;
                    }
                    changeCredentials(getUserFromUserName(input, existing_users), "username",existing_users);
                    break;
                case 6:
                    System.out.println("Enter the username of the user");
                    sc.nextLine();
                    input = sc.nextLine();
                    if(!isUserPresent(input, existing_users)){
                        System.out.println("The user doesn't exist!!");
                        break;
                    }
                    changeCredentials(getUserFromUserName(input, existing_users), "password", existing_users);
                    break;
                case 7:
                    int choices = 0;
                    if(all_tasks.size()==0){
                        System.out.println("There are no available tasks");
                        break;
                    }

                    do{
                        System.out.println("choices");
                        for(int i = 0 ; i <all_tasks.size() ; i++){
                            System.out.println("press " + (i + 1) + " for :" + "\n" + all_tasks.get(0).toString()+"\n\n");
                        }
                        choices = sc.nextInt();
                        choices--;
                        if(choices>=all_tasks.size()|| choices<0){
                            System.out.println("Not a valid option");
                        }
                    }while(choices>=all_tasks.size()|| choices<0);


                    try {
                        all_tasks.remove(all_tasks.get(choices));
                        updateTasks("src/tasks.txt", all_tasks);
                    }
                    catch (Exception e){
                        System.out.println("An error occurred");
                    }
                    break;
                case 8:
                    int choiced = 0;
                    if(all_tasks.size()==0){
                        System.out.println("There are no available tasks");
                        break;
                    }

                    do{
                        System.out.println("choices");
                        for(int i = 0 ; i <all_tasks.size() ; i++){
                            System.out.println("press " + (i + 1) + " for :" + "\n" + all_tasks.get(0).toString()+"\n\n");
                        }
                        choiced = sc.nextInt();
                        choiced--;
                        if(choiced>=all_tasks.size()|| choiced<0){
                            System.out.println("Not a valid option");
                        }
                    }while(choiced>=all_tasks.size()|| choiced<0);

                    try {
                        all_tasks.get(choiced).completed = true;
                        updateTasks("src/tasks.txt", all_tasks);
                    }
                    catch (Exception e){
                        System.out.println("An error occurred");
                    }
                    break;
                case 9:
                    try {
                        removeCollab(all_tasks);
                    } catch (IOException e) {
                        System.out.println("An error occurred");
                    }
                    break;
                case 10:
                    printTasks(all_tasks);
                    break;
                case 11:
                    String t1,t2,t3;
                    System.out.println("Enter the name of the user");
                    t1 = sc.nextLine();
                    System.out.println("Enter the user-name");
                    t2 = sc.nextLine();
                    System.out.println("Enter the password");
                    t3 = sc.nextLine();
                    try {
                        existing_users.add(new User(t1,t2,t3));
                        updateFiles("src/users.txt", existing_users);
                    }catch (Exception e){
                        System.out.println("An error occurred");
                    }
                    break;
                case 12:
                    System.out.println("Enter the username of the user");
                    input = sc.nextLine();
                    if(!isUserPresent(input, existing_users)){
                        System.out.println("The user doesn't exist!!");
                    }
                    try {
                        updateFiles("src/users.txt", existing_users);
                        existing_users.remove(getUserFromUserName(input,existing_users));
                    } catch (IOException e) {
                        System.out.println("User removed successfully");
                    }
                    break;
                case 13:
                    System.out.println("User logged out successfully!!");
                    break;
                default:
                    System.out.println("Not a valid option");
            }
        }while (choice!=13);
    }
}
