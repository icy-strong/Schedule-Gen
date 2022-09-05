import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;


/**
 * Representation of an Employee
 *
 * An employee has a list of positions they can work, a list of days they can't work, an int representing the place they go in the schedule,
 * the amount of hours they can work in a week, the amount of hours they are scheduled for a week, a name, whether they're a minor,
 * the shits they're scheduled for, a String Array representing the shifts they're scheduled for in order, and
 * a double array of the lengths of those shifts.
 */
public class Employee implements Comparable<Employee>{
    final private ArrayList<Character> positions;
    final private HashSet<Character> daysOff;
    final private int order;
    final private double hours;
    final private String name;
    final private boolean minor;
    private double scheduledHours;
    private final PriorityQueue<Shift> scheduledShifts=new PriorityQueue<>();
    private String[] week={"","","","","","","","",""};
    private double[] times=new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0};


    /**
     * Constructs a new Employee
     *
     * @param positions List of positions they can work, where the first index is the position they're best at
     * @param daysOff List of days of the week they can't work
     * @param hours Amount of time they can work in a week
     * @param name Employee's name
     * @param minor True if minor (Minors can't work shifts longer than 4.0 hrs)
     * @param order int to put employees in order on schedule
     */
    public Employee(ArrayList<Character> positions, HashSet<Character> daysOff, double hours, String name, boolean minor, int order){
        this.positions=positions;
        this.daysOff=daysOff;
        this.hours=hours;
        this.name=name;
        this.minor=minor;
        this.order=order;
        week[0]=name;
    }


    /**
     * Returns the positions the employee can work
     * @return ArrayList</Character> of positions
     */
    public ArrayList<Character> getPositions(){return positions;}


    /**
     * Returns the position the employee is best at
     * @return char of position
     */
    public char getAce(){
        return positions.get(0);
    }


    /**
     * Returns the days the employee can't work
     * @return HashSet</Character> of days
     */
    public HashSet<Character> getDaysOff() {return daysOff;}


    /**
     * Returns the hours the employee can work in a week
     * @return double of hours
     */
    public double getHours(){return hours;}


    /**
     * Adds a shift to the employee's schedule
     * @param shift shift to be added
     */
    public void addShift(Shift shift){
        scheduledShifts.add(shift);
        scheduledHours+=shift.getLength();
    }


    /**
     * Removes the longest shift that an employee is scheduled for to make room for a new one
     * @return Shift that was removed
     */
    public Shift removeLongestShift(){
        Shift shift=scheduledShifts.remove();
        scheduledHours=scheduledHours-shift.getLength();
        shift.assignEmployee(null);
        return shift;
    }


    /**
     * Adds a shift into the week array
     * @param shift Shift to be added
     * @param dayOfWeek the day the Shift is scheduled for
     */
    public void addShiftArray(Shift shift, char dayOfWeek){
        int i=-1;

        switch (dayOfWeek){
            case('M'): i=1; break;
            case('T'): i=2; break;
            case('W'): i=3; break;
            case('R'): i=4; break;
            case('F'): i=5; break;
            case('S'): i=6; break;
            case('Z'): i=7; break;
        }

        week[i]=shift.positionTimeLength();
        times[i-1]=shift.getLength();


    }


    /**
     * Calculates the Employee's hours
     */
    public void calcHours(){
        scheduledHours=0.0;

        for(int i=0; i< times.length; i++){
            scheduledHours+=times[i];
        }


    }


    /**
     * Returns the length of the employee's longest shift
     * @return double of the length
     */
    public double longestShiftLength(){
        return scheduledShifts.peek().getLength();
    }


    /**
     * Returns the employee's name
     * @return String name
     */
    public String getName(){return name;}


    /**
     * Returns if the employee is a minor
     * @return true if minor
     */
    public boolean isMinor(){return minor;}


    /**
     * Returns a copy of the employee
     * @return Employee copy
     */
    public Employee getCopy(){return new Employee(positions,daysOff,hours,name,minor,order);}


    /**
     * Returns the employee as a String with their name and scheduled hours
     * @return String of employee
     */
    public String toString(){
        return name+" "+scheduledHours;
    }


    /**
     * Adds onto the amount of hours and Employee is scheduled for
     * @param time double of how long the shift to be added is
     */
    public void addHours(double time){
        scheduledHours+=time;
    }


    /**
     * Returns how many hours the employee is scheduled for
     * @return double of how many hours
     */
    public double getScheduledHours(){return scheduledHours;}

    /**
     * Returns the String[] representing the Employee's week
     * @return String[] representing the Employee's week
     */
    public String[] getWeek(){week[8]=Double.toString(scheduledHours); return week;}

    public int compareTo(Employee o) {
        return order-o.order;
    }

    public int getOrder(){return order;}
}
