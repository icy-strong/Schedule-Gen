import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Main class that holds the main method, creates the population, list of employees, and list of shifts
 */
public class Main {

    static ArrayList<Employee> employees = new ArrayList<>();
    static ArrayList<Shift> shifts=new ArrayList<>();
    static ArrayList<Integer> breaks= new ArrayList<>();
    public static Week best;
    public static Population pop;


    public static void main(String[] args) throws IOException {
        addEmployees();
        addShifts();


        pop= new Population(employees,shifts, 250);

        Week schedule= pop.getLastGenBest();
        best=schedule;

        writeSheet(schedule);


    }


    /**
     * Uses the data from the week and exports the data into a spreadsheet
     * @param schedule Week to gather data from
     * @throws IOException if File not found or other Exception from writing the new File
     */
    public static void writeSheet(Week schedule) throws IOException {

        XSSFWorkbook workbook= new XSSFWorkbook();
        XSSFSheet spreadsheet= workbook.createSheet("Schedule");

        XSSFRow row;


        ArrayList<Employee> employees=schedule.getEmployees();
        Collections.sort(employees);


        int line=0;

        Map<Integer,ArrayList<String>> scheduleData= new TreeMap<>();

        //first row that contains the days of the week and total hours
        scheduleData.put(line, new ArrayList<>(List.of("", "Monday","Tuesday","Wednesday", "Thursday", "Friday","Saturday", "Sunday","Total Hours")));
        line++;

        //Breaks in between the employees that describe their rank
        String[] labels=new String[]{"General Manager","Service Manager","Kitchen Manager","Certified Trainer","KMIT","Crew","Minor"};



        //adds each line of data into the treemap to be printed
        for(int n=0; n<labels.length; n++){
            ArrayList<String> str= new ArrayList<>(List.of(labels[n]));
            scheduleData.put(line, str);
            line++;

            if(n==0){
                for(int i=1;i<breaks.get(n+1)-1; i++){
                    ArrayList<String> kms=new ArrayList<>();
                    Collections.addAll(kms,employees.get(i-1).getWeek());
                    scheduleData.put(line,kms);
                    line++;
                }
            }else if(n!= labels.length-1){
                for(int i= breaks.get(n)-n;i<breaks.get(n+1)-n-1; i++){
                    ArrayList<String> kms=new ArrayList<>();
                    Collections.addAll(kms,employees.get(i-1).getWeek());
                    scheduleData.put(line,kms);
                    line++;
                }
            }else{
                for(int i= breaks.get(n)-n;i<employees.size()+1; i++){
                    ArrayList<String> kms=new ArrayList<>();
                    Collections.addAll(kms,employees.get(i-1).getWeek());
                    scheduleData.put(line,kms);
                    line++;
                }
            }


        }



        Set<Integer> keys=scheduleData.keySet();


        //adds each line from the treemap into the spreadsheet
        for(int i=0; i<keys.size(); i++){

            row=spreadsheet.createRow(i);
            ArrayList<String> data= scheduleData.get(i);

            for(int j=0; j<data.size(); j++){

                Cell cell= row.createCell(j);
                cell.setCellValue(data.get(j));

            }

        }


        FileOutputStream out=new FileOutputStream("Schedule.xlsx");

        workbook.write(out);
        out.close();


    }



    /**
     * Reads a file with the shifts written in the format of position, time, length, then adds them to
     * a list of shifts
     * @throws FileNotFoundException if File is not found
     */
    public static void addShifts() throws FileNotFoundException{
        File file= new File("Shifts.txt");
        Scanner fileScan = new Scanner(file);
        Scanner line;

        while(fileScan.hasNextLine()){
            line= new Scanner(fileScan.nextLine());
            String str=line.next();
            char position=str.charAt(0);
            String time=line.next();
            double length=line.nextDouble();

            shifts.add(new Shift(position,time,length));

        }
    }


    /**
     * Reads a file with employees in the format lastName, firstName, positions, hours, ifMinor(M), days
     * they cant work, then adds them to a list
     * @throws FileNotFoundException if File is not found
     */
    public static void addEmployees() throws FileNotFoundException {
        File file = new File("Employees.txt");
        Scanner fileScan= new Scanner(file);
        Scanner line;


        int where=0;
        while(fileScan.hasNextLine()) {
            where++;
            line = new Scanner(fileScan.nextLine());

            if (!line.hasNext()) {
                breaks.add(where);
            } else {


                String name = line.next() + " " + line.next();
                ArrayList<Character> positions = new ArrayList<>();
                String positionsString = line.next();

                for (int i = 0; i < positionsString.length(); i++) {
                    if (positionsString.charAt(i) != ',') {
                        positions.add(positionsString.charAt(i));
                    }
                }

                double hours = line.nextDouble();

                boolean minor = false;
                HashSet<Character> daysOff = new HashSet<>();

                if (line.hasNext()) {
                    String next = line.next();
                    if (next.equalsIgnoreCase("M")) {
                        minor = true;
                        if (line.hasNext()) {
                            next = line.next();
                            for (int i = 0; i < next.length(); i++) {
                                if (next.charAt(i) != ',') {
                                    daysOff.add(next.charAt(i));
                                }
                            }
                        }
                    }

                    if (!minor) {
                        for (int i = 0; i < next.length(); i++) {
                            if (next.charAt(i) != ',') {
                                daysOff.add(next.charAt(i));
                            }
                        }
                    }
                }

                employees.add(new Employee(positions, daysOff, hours, name, minor, where));

            }
        }

    }
}
