import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Representation of a Week's Schedule
 *
 * A week includes an Array of 7 days, a fitness score, a set of names, and a list of Employees
 */
public class Week implements Comparable<Week>{
    private final Day[] week= new Day[7];
    private int fitness=0;
    private ArrayList<Employee> employees;
    private HashSet<String> names= new HashSet<>();
    static Random rand= new Random();


    /**
     * Constructs a new Week
     *
     * @param shifts list of shifts that need to be filled
     * @param employees list of employees to fill the shifts with
     */
    public Week(ArrayList<Shift> shifts, ArrayList<Employee> employees){
        this.employees=employees;


        createDays(shifts);
        calculateHours();

        calcFitness();

        areAllScheduled();
    }


    /**
     * Private method to fill each day of the week with workers to run each shift
     * @param shifts list of shifts to be filled
     */
    private void createDays(ArrayList<Shift> shifts){

        char[] daysOfWeek={'M','T','W','R','F','S','Z'};

        //goes through each day of week and fills up the shifts
        for(int i=0; i<7; i++){

            //creates new list with copies of the shifts to prevent shifts being overridden
            ArrayList<Shift> copy= new ArrayList<>();
            for (Shift shift : shifts) {
                copy.add(shift.getCopy());
            }

            week[i]=new Day(daysOfWeek[i],copy, new ArrayList<>(List.copyOf(employees)));

        }


        //checks for shifts that were dropped in the week and attempts to fill them
        for(int i=0; i<7; i++){
            week[i].checkFull();
        }


    }


    /**
     * Calculates the fitness of the week based on how many slots are full
     * and how many aces are in their places, then divides it by 10
     */
    private void calcFitness(){
        //reset the fitness so it doesn't add on top of itself
        fitness=0;

        //goes through each shift, if it is taken +1 point, if it is taken and has an ace, +10 more
        for(int i=0; i<7; i++){
            for(int j=0; j<week[i].getShifts().size(); j++){
                if(week[i].getShifts().get(j).getWorker()!=null) {
                    fitness++;
                    if (week[i].getShifts().get(j).isAceInPlace()) fitness += 10;
                }
            }
        }


        //divides fitness by 10 for more manageable number
        fitness=fitness/10;
    }


    /**
     *Checks if everyone is on the schedule, if not then fitness=0
     */
    private void areAllScheduled(){
        for (Employee employee : employees) {
            if (employee.getScheduledHours() == 0.0) {
                fitness = 0;
                break;
            }

        }
    }


    /**
     * Recalculates how many hours each employee is scheduled for and makes sure the employees in
     * the list reflect the employees scheduled for the week
     */
    private void calculateHours(){

        //makes the employees and names empty so new ones can be added
        employees=new ArrayList<>();
        names=new HashSet<>();

        //clears every employee's hours so they don't add on top of itself
        for(int i=0; i<7; i++){
            for(int j=0; j<week[i].getShifts().size(); j++){
                Shift shift=week[i].getShifts().get(j);
                if(shift.isTaken()) {
                    Employee worker= shift.getWorker();
                    worker.addShiftArray(shift,week[i].getDayOfWeek());

                    //checks if the employee's name is in the hashset, if it's not they're added to
                        //the employees list
                    if(!names.contains(worker.getName())){
                        employees.add(worker);
                    }
                    names.add(worker.getName());
                }
            }
        }

        //calculates the employee's hours
        for(int i=0; i<employees.size(); i++){
            employees.get(i).calcHours();
        }





    }


    /**
     * Returns the fitness of the week
     * @return int of fitness
     */
    public int getFitness(){return fitness;}


    /**
     * Swaps a random day with the day of another day in attempt to make the schedule better, then
     * calculates the new fitness
     * @param week2 second week to swap day with
     */
    public void swap(Week week2){

        int num= rand.nextInt(7);

        week[num]=week2.week[num];
        calcFitness();
        calculateHours();
        areAllScheduled();
    }


    /**
     * Picks a random day and random shift, then picks a new employee to work it, and recalculates fitness
     */
    public void mutate(){
        Day day=week[rand.nextInt(7)];
        Shift shift= day.getShifts().get(rand.nextInt(day.getShifts().size()));
        day.fillEmployee(shift);

        calcFitness();
        calculateHours();
        areAllScheduled();

    }


    /**
     * Returns the Employee objects specific for the given week
     * @return ArrayList</Employee> of employees
     */
    public ArrayList<Employee> getEmployees(){return employees;}


    /**
     * Compares two weeks based on their fitness score
     * @param week2 week to be compared to
     * @return int based on comparison
     */
    public int compareTo(Week week2){
        return week2.fitness-fitness;
    }


    /**
     * Returns a String version of the week including the fitness and schedule for the days of the week
     * @return String of week
     */
    public String toString(){
        String str=fitness + " "+week[0].toString();
        for (int i=1; i< week.length; i++){
            str+="\n"+fitness+" "+week[i].toString();
        }

        return str;
    }



}
