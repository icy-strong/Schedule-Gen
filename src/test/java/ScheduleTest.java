import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import org.testng.annotations.Test;
import org.junit.jupiter.api.BeforeEach;


/**
 * Tests some of the methods in Employee
 */
public class ScheduleTest {

    static ArrayList<Employee> employees=new ArrayList<>();
    static ArrayList<Shift> shifts;

    @BeforeEach
    void reset(){
        Week best =Main.best;
        employees=best.getEmployees();


    }

    @Test
    public void testIfAllScheduledAndMinorHours(){
        for(int i=0; i<employees.size(); i++){
           Employee worker= employees.get(i);
            assertTrue(worker.getScheduledHours()>0.0);
            if(worker.isMinor()){
                assertTrue(worker.longestShiftLength()<=4.0);
            }
        }
    }


    @Test
    public void testEmployeeCompareTo(){
        Collections.sort(employees);
        for(int i=0; i<employees.size()-1; i++){
            assertTrue(employees.get(i).getOrder()<employees.get(i+1).getOrder());
        }
    }




}
