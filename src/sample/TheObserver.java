package sample;


/**
 * Class specifies the functions that Employee Observer should perform to monitor employees
 * <p>
 * The interface {@code TheObserver} defines the update method() that is used by
 * the Observable to keep the Observer updated on its status. This is how the EmployeeObserver
 * monitors the Employees in the call center, is constantly updated on their status changes.
 *
 * @author leefowler
 */
public interface TheObserver {
    public void update(EmployeeStatus employeeStatus);
}
