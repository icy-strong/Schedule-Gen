import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Class that Represents a population of Schedules.
 *
 * A Population includes a list of weeks to represent a mating pool, a list of weeks that represent a new
 * generation from that mating pool, a list of employees to make the weeks with, a list of shifts to fill,
 * and a list of weeks to hold the best of each generation
 */
public class Population {

    private ArrayList<Week> matingPool=new ArrayList<>();
    private ArrayList<Week> newGeneration= new ArrayList<>();
    private ArrayList<Employee> employees;
    private ArrayList<Shift> shifts;
    private ArrayList<Week> bestOf=new ArrayList<>();
    static Random rand = new Random();



    /**
     * Constructs a new Population
     *
     * @param employees employees to make the weeks with
     * @param shifts shifts that need to be filled
     */
    public Population(ArrayList<Employee> employees, ArrayList<Shift> shifts, int gens){
        this.employees=employees;
        this.shifts=shifts;


        //makes the generations
        naturalSelection(gens);


    }


    /**
     * Constructs empty population
     */
    public Population(){

    }



    /**
     * Creates the first and subsequent generations based off the given int
     * @param gens how many generations to be made (excluding the first gen)
     */
    private void naturalSelection(int gens){
        //creates the first gen of weeks to then make the next generations
        createGen1(10);

        //sorts the generation to add the best to the new gen
        Collections.sort(newGeneration);
        bestOf.add(newGeneration.get(0));


        //creates the new gens based on given int and adds best of each to bestOf list
        for(int i=0; i<gens; i++){
            createNextGen();
            Collections.sort(newGeneration);
            bestOf.add(newGeneration.get(0));
        }


    }


    /**
     * Creates a random first generation as big as given int
     * @param children how big the first gen is going to be
     */
    private void createGen1(int children){

        //creates the new children
        for(int i=0; i<children; i++){

            //creates a new list with new employees to prevent from everything overriding itself
            ArrayList<Employee> copy=new ArrayList<>();
            for(int j=0; j<employees.size(); j++){
                copy.add(employees.get(j).getCopy());
            }

            newGeneration.add(new Week(shifts,copy));

            //if the fitness of the parent is 0, remove it and make a new one
            if(newGeneration.get(i).getFitness()==0){
                newGeneration.remove(i);
                i--;
            }
        }


        //fills the mating pool for next gen
        fillMatingPool();

    }


    /**
     * Makes the generations after the first one
     */
    private void createNextGen(){

        //Erases the newGeneration to refill it with the actual new generation
        newGeneration= new ArrayList<>();

        //runs until there are 10 valid children
        while(newGeneration.size()!=10) {


            //picks two random parents to mate and make a child
            Week p1 = matingPool.get(rand.nextInt(matingPool.size()));
            Week p2 = matingPool.get(rand.nextInt(matingPool.size()));


            //swaps a random day of the week between the parents
            p1.swap(p2);


            //checks if the child's fitness is 0, if it is then it's disregarded
            if (p1.getFitness() != 0) {

                //chance 2/5 chance to mutate a random shift in the schedule
                int chance= rand.nextInt(6);
                if(chance==3||chance==2){
                    p1.mutate();
                    if(p1.getFitness()!=0){
                        newGeneration.add(p1);
                    }
                }else{
                    newGeneration.add(p1);
                }

            }

        }

        //fill the mating pool for next gen
        fillMatingPool();
    }


    /**
     * Adds each week to the mating pool equal to its fitness
     */
    private void fillMatingPool(){
        matingPool=new ArrayList<>();

        for(int i=0; i<newGeneration.size(); i++){
            for(int j=0; j<newGeneration.get(i).getFitness(); j++){
                matingPool.add(newGeneration.get(i));
            }
        }
    }


    public Week getLastGenBest(){
        return bestOf.get(bestOf.size()-1);
    }


    /**
     * Makes a population into a String by combining the best of each generation
     * @return String of population
     */
    public String toString(){

        String str="";

        for(int i=0; i<bestOf.size(); i++){

            str+="Best of Generation "+i+":\n"+bestOf.get(i).toString()+"\n";

        }

        return str;

    }


    public ArrayList<Week> getBestOf(){return bestOf;}

}
