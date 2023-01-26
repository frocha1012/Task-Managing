import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomDate{
    int day;
    int month;
    int year;
    CustomDate(int day, int month, int year) throws Exception{
        this.day = day;
        this.month = month;
        this.year = year;
        ArrayList<Integer> thirty_one = new ArrayList(Arrays.asList(1,3,5,7,8,10,12));
        ArrayList<Integer> thirty = new ArrayList(Arrays.asList(4,6,9,11));

        if((month==2&&year%4==0&&day>29)||(month ==2 && year%4!=0 && day>28)){
            throw new Exception("The date does not exist1");
        }
        else if(thirty_one.contains(month)&&day>31){
            throw new Exception("The date does not exis2t");
        }
        else if(thirty.contains(month)&&day>30){
            throw new Exception("The date does not exist3");
        }
        else if(month>12 || month<1){
            System.out.println(month);
            throw new Exception("The date does not exist4");
        }
    }

    public boolean isBefore(CustomDate second) throws Exception {
        if(second.year> year){
            return true;
        }else if(second.year == year){
         if(second.month> month){
             return true;
         }else if(second.month == month){
             if(second.day> day){
                 return true;
             }
             else {
                 return false;
             }
         }
         else {
             return false;
         }
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return day+"/"+month+"/"+year;
    }
}
