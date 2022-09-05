/**
 * Representation of a Shift
 *
 * A shift includes a position, a time, a length, and an Employee to work it.
 */
public class Shift implements Comparable<Shift>{
    final private char position;
    final private String time;
    final private double length;
    private Employee worker;


    /**
     * Constructs a new Shift
     *
     * @param position char to represent what type of shift it is
     * @param time String to represent when the shift takes place
     * @param length double of the shift's length
     */
    public Shift(char position, String time, double length){
        this.length=length;
        this.position=position;
        this.time=time;
    }


    /**
     * Assigns a worker to a shift
     * @param worker Employee to run shift
     */
    public void assignEmployee(Employee worker){
        this.worker=worker;
    }


    /**
     * Checks if the worker's best position matches the position of the shift
     * @return true if match
     */
    public boolean isAceInPlace(){
        return position == worker.getAce();
    }

    /**
     * Returns time of shift
     * @return String of time
     */
    public String getTime(){return time;}


    /**
     * Returns the position of the shift
     * @return char of position
     */
    public char getPosition(){return position;}


    /**
     * Returns the length of the shift
     * @return double of length
     */
    public double getLength(){return length;}


    /**
     * Returns the worker assigned to the shift
     * @return Employee working shift
     */
    public Employee getWorker(){return worker;}


    /**
     * Checks if the shift has a worker assigned to it
     * @return true if worker is assigned
     */
    public boolean isTaken(){
        return worker != null;
    }


    /**
     * Returns a copy of the Shift
     * @return Shift copy
     */
    public Shift getCopy(){
        return new Shift(position,time,length);
    }


    /**
     * Compares two shifts based on their length
     * @param other Shift to be compared with
     * @return int based off the shift's comparison
     */
    public int compareTo(Shift other){
        return Double.compare(length, other.length);
    }


    /**
     * Returns the shift as a String with position and time, if the shift has a worker that will also be included
     * @return String of shift
     */
    public String toString(){
        if(worker==null){
            return position+" "+time;
        }
        return worker.getName()+": "+position+" "+time;
    }

    public String positionTimeLength(){
        return position+" "+time+" "+length;
    }
}
