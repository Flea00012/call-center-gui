package sample;


/**
 * Class specifies the functions that Employee Observer should perform to monitor employees
 * <p>
 * The {@code EmployeeObserver} class defines the employee observer object,
 * which is used to monitor status updates in the EmployeeObservable class.
 * This allows the status changes of the employees to be monitored throughout
 * the process of the work.
 *
 * @author leefowler
 */
public class EmployeeObserver implements TheObserver {

    private EmployeeStatus employeeStatus;


    public EmployeeStatus getEmployeeStatus() {
        return this.employeeStatus;
    }

    @Override
    public void update(EmployeeStatus employeeStatus) {
        this.employeeStatus = employeeStatus;
        System.out.println("Employee status has been updated to " + this.employeeStatus);

    }
}
