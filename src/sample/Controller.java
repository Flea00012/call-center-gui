package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Class specifies the functions that Controller should perform to execute User Commands
 * <p>
 * The {@code Controller} class defines the Controller responsibilities in the MVC model,
 * which allows the FXML file to describe the User Interface. The result is a program
 * that keeps business logic and functions separate from the User Interface according
 * to the MVC design pattern.
 *
 * @author leefowler
 */
public class Controller {

    @FXML
    private Button CallButton;

    @FXML
    private Button TakeCallButton;

    @FXML
    private Button EscalateCallButton;


    ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(5);
    ExecutorService executorService = Executors.newFixedThreadPool(2);


    EmployeeObservable employeeObservable = new EmployeeObservable(EmployeeType.FRESHER);
    EmployeeObserver employeeObserver = new EmployeeObserver();


    Lock lock = new ReentrantLock();

    public void CallButtonClicked() {

        CallButton.setDisable(true);


        Runnable producer = () -> {

            int i = 0;

            boolean terminate = false;
            while (true) {

                try {

                    arrayBlockingQueue.put(++i);
                    System.out.println("Call #" + i + " added to queue ");
                    TimeUnit.MILLISECONDS.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        };


        Runnable consumer = () -> {
            while (true) {
                try {

                    Integer poll;
                    TimeUnit.MILLISECONDS.sleep(2000);
                    poll = arrayBlockingQueue.take();
                    System.out.println("Call #" + poll + " taken from the queue and handled.");
                    TimeUnit.MILLISECONDS.sleep(2000);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        executorService.submit(consumer);
        executorService.submit(producer);

        executorService.shutdownNow();


    }

    public void EndCallButtonClicked(ActionEvent actionEvent) {

        executorService.shutdownNow();
        System.out.println("Call ended and connection shutdown.");
        System.exit(0);
    }


    public void TakeCallButtonClicked() {
        System.out.println("Call is being handled");


        Runnable interruptingCall = () -> {
            while (true) {
                try {

                    Integer poll;
                    poll = arrayBlockingQueue.take();
                    System.out.println("Consumer Thread was being interrupt when I took the Call.");
                    TimeUnit.MILLISECONDS.sleep(1000);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A default uncaught exception was thrown on "
                        + t.getName() + " and the error is :" + e.getMessage());
            }
        });

        executorService.submit(interruptingCall);

        executorService.shutdownNow();

    }

    public void EscalateCallButtonClicked() {
        System.out.println("Call is being escalated to my supervisor. I am unavailable at present.");
        Thread.currentThread().interrupt();

    }

    public void MeetingRadioClicked(ActionEvent actionEvent) {
        EscalateCallButton.setDisable(true);
        TakeCallButton.setDisable(true);

        employeeObservable.addEmployeeObserver(employeeObserver);
        employeeObservable.setEmployeeStatusUpdate(EmployeeStatus.IN_A_MEETING);
        System.out.println("Employee on " + Thread.currentThread().getName() + " has set their status to: " + employeeObserver.getEmployeeStatus());


    }


    public void LunchRadioClicked(ActionEvent actionEvent) {
        EscalateCallButton.setDisable(true);
        TakeCallButton.setDisable(true);

        employeeObservable.addEmployeeObserver(employeeObserver);
        employeeObservable.setEmployeeStatusUpdate(EmployeeStatus.ON_LUNCH);
        System.out.println("Employee on " + Thread.currentThread().getName() + " has set their status to: " + employeeObserver.getEmployeeStatus());


    }

    public void AwayRadioClicked(ActionEvent actionEvent) {
        EscalateCallButton.setDisable(true);
        TakeCallButton.setDisable(true);

        employeeObservable.addEmployeeObserver(employeeObserver);
        employeeObservable.setEmployeeStatusUpdate(EmployeeStatus.GONE_FOR_THE_DAY);
        System.out.println("Employee on " + Thread.currentThread().getName() + " has set their status to: " + employeeObserver.getEmployeeStatus());


    }
}

