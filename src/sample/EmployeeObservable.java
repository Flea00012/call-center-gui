package sample;


import com.sun.glass.ui.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Class specifies the functions that Employees in call center should perform
 * <p>
 * The {@code EmployeeObservable} class defines the employee object and uses a
 * Composition (has-a) relationship with EmployeeStatus and EmployeeType,
 * to construct the EmployeeObservable object. The class also holds an
 * EmployeeObserver object, to ensure monitoring of the Observable.
 *
 * @author leefowler
 */
public class EmployeeObservable {


    private List<EmployeeObserver> employeeObservers = new ArrayList<>();

    private EmployeeType employeeType;

    private EmployeeStatus employeeStatus;


    public void addEmployeeObserver(EmployeeObserver employeeObserver) {
        this.employeeObservers.add(employeeObserver);
    }

    public void removeEmployeeObserver(EmployeeObserver employeeObserver) {
        this.employeeObservers.remove(employeeObserver);
    }


    public void setEmployeeStatusUpdate(EmployeeStatus employeeStatus) {
        this.employeeStatus = employeeStatus;
        for (EmployeeObserver employeeObserver : this.employeeObservers) {
            employeeObserver.update(this.employeeStatus);
        }
    }


    public EmployeeObservable(EmployeeType employeeType) {

        this.employeeType = employeeType;
        this.employeeStatus = EmployeeStatus.AVAILABLE;
    }


    public EmployeeType getEmployeeType() {
        return employeeType;
    }


}

