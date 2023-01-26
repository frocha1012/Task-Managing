import java.util.ArrayList;
import java.util.Scanner;

public class Task {
    User admin;
    ArrayList<User> users = new ArrayList<>();
    String description;
    CustomDate start_date;
    CustomDate end_date;
    boolean completed = false;

    Task() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a short description of the task");
        description = sc.nextLine();
        System.out.println("Enter the start date in the format dd/mm/yy");
        String input = sc.nextLine();
        String[] input_date = input.split("/");
        start_date = new CustomDate(Integer.parseInt(input_date[0]), Integer.parseInt(input_date[1]) , Integer.parseInt(input_date[2]));
        System.out.println("Enter the end date in the format dd/mm/yy");
        input = sc.nextLine();
        input_date = input.split("/");
        end_date = new CustomDate(Integer.parseInt(input_date[0]), Integer.parseInt(input_date[1]) , Integer.parseInt(input_date[2]));

        if(!start_date.isBefore(end_date)){
            throw new Exception("start date cannot be after the end date");
        }
    }

    Task(String description,String start_date,String end_date,String completed,String admin,String collaborators) throws Exception {
        this.admin = Main_Interface.getUserFromUserName(admin, Main_Interface.existing_users);
        this.description = description;
        String[] input_date = start_date.split("/");
        this.start_date = new CustomDate(Integer.parseInt(input_date[0]), Integer.parseInt(input_date[1]) , Integer.parseInt(input_date[2]));
        input_date = end_date.split("/");
        this.end_date = new CustomDate(Integer.parseInt(input_date[0]), Integer.parseInt(input_date[1]) , Integer.parseInt(input_date[2]));
        this.completed = completed == "true";
        input_date = collaborators.split(" ");
        for (String s : input_date) {
            if(!s.equals("none")){
                this.users.add(Main_Interface.getUserFromUserName(s, Main_Interface.existing_users));
            }
        }
    }

    @Override
    public String toString() {
        return "Task \n"+
                "description = " + description + "\n" +
                "Admin of this task = " + admin.username + "\n" +
                "Collaborators = " + getAllCollabs() + "\n" +
                "start date  = " + start_date.toString() +"\n"+
                "end date    = " + end_date.toString()  + "\n" +
                "completed status = " + completed;
    }

    public String getAllCollabs(){
        String output = "";
        if(users.size() == 0){
            return "none";
        }
        for(User user : users){
            output += user.username ;
            output += " ";
        }
        return output;
    }
}
