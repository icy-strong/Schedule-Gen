import java.util.*;

/**
 * Class to represent a Day in the schedule
 *
 * A day includes a day of the week, a list of shifts to fill, and a list of employees to schedule
 */
public class Day {
    final private char dayOfWeek;
    private final ArrayList<Shift> shifts;
    static Random rand= new Random();
    private final ArrayList<Employee> employees;


    /**
     * Constructs a new Day
     *
     * @param dayOfWeek char to represent what the day of the week is
     * @param shifts list of shifts to fill
     * @param employees list of employees to fill the sifts
     */
    public Day (char dayOfWeek, ArrayList<Shift> shifts, ArrayList<Employee> employees){
        this.dayOfWeek=dayOfWeek;
        this.shifts=shifts;
        this.employees=employees;


        fillShifts();
    }


    //static int to show how many people are considered before a shift gets assigned an employee,
    // unused in current code, only for testing purposes
    static int num;

    /**
     * Fills the shifts one shift at a time
     */
    private void fillShifts(){

        //goes through each shift to fill it using a private method
        for (Shift value : shifts) {
            num = 0;
            Shift shift = value;

            //new arraylist is made so each shift can modify the employees that are eligible for it
            ArrayList<Employee> pool = new ArrayList<>(List.copyOf(employees));
            fillEmployee(shift, pool);


        }


    }


    /**
     * Takes a shift and assigns a new employee to it by calling a private method
     * @param shift Shift to be reassigned
     */
    public void fillEmployee(Shift shift){
        fillEmployee(shift,new ArrayList<>(List.copyOf(employees)));
    }


    /**
     * Fills the given shift with a worker to run it
     * @param shift Shift to be filled
     * @param pool list of Employees in the running to get the shift
     */
    private void fillEmployee(Shift shift, ArrayList<Employee> pool){

        //if there are no people who can fill the position, move on
        if(pool.isEmpty()) return;


            //selects a random employee to give shift to
            Employee random= pool.get(rand.nextInt(pool.size()));



            num++;



            //checks if the random Employee can fill the position and checks if it's not on their day off
            if(random.getPositions().contains(shift.getPosition())&&!random.getDaysOff().contains(dayOfWeek)){

                //checks if the Employee is a minor and if the shift length is greater than 4.0 hrs because
                    //minors cannot work for more than 4.0hr shifts
                if(random.isMinor()&&shift.getLength()>4.0) {
                    pool.remove(random);
                    fillEmployee(shift,pool);

                //checks if the employee's hours would go over if they pick up this shift,
                    // if not they are scheduled for the shift
                }else if(random.getScheduledHours()+shift.getLength()<=random.getHours()){


                    shift.assignEmployee(random);
                    random.addShift(shift);

                    employees.remove(random);

                //case if the employee's hours would spill over their weekly limit
                }else {
                    //checks if getting rid of the worker's longest shift would allow them to pick up this one
                        //if yes, remove their longest shift and pick up the new one
                    if (random.getScheduledHours() - random.longestShiftLength() + shift.getLength() <= random.getHours()) {
                        random.removeLongestShift();
                        shift.assignEmployee(random);
                        random.addShift(shift);
                        employees.remove(random);

                    //if the employee is unfit for the shift
                    } else {
                        pool.remove(random);
                        fillEmployee(shift, pool);
                    }
                }
            //if employee can't work the shift
            }else{
                pool.remove(random);
                fillEmployee(shift,pool);
            }

    }


    /**
     * Checks if all the shifts have a worker, if one doesn't then try to fill it
     */
    public void checkFull(){
        for (Shift shift : shifts) {
            if (shift.getWorker() == null) {
                fillEmployee(shift, new ArrayList<>(List.copyOf(employees)));
            }
        }
    }


    /**
     * Returns the shifts for the day
     * @return ArrayList</Shift> of shifts
     */
    public ArrayList<Shift> getShifts(){return shifts;}


    /**
     * Turns a day into a string by adding the day of the week to the schedule for that day
     * @return String of the day
     */
    public String toString(){
        return dayOfWeek+" "+shifts.toString();
    }


    public char getDayOfWeek(){return dayOfWeek;}
}
