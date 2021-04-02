package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @FXML
    private CheckBox AwayRadioClicked;

    @FXML
    private CheckBox LunchRadioClicked;

    @FXML
    private CheckBox MeetingRadioClicked;

    @FXML
    private CheckBox AvailableRadioClicked;

    /**
     * ArrayBlockingQueue is used for the Producer - Consumer pattern and executorService
     * is used to control execution of the threads.
     */
    ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(5);
    ExecutorService executorService = Executors.newFixedThreadPool(2);


    EmployeeObservable employeeObservable = new EmployeeObservable(EmployeeType.FRESHER);
    EmployeeObserver employeeObserver = new EmployeeObserver();


    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    Lock lock = new ReentrantLock();

    public void CallButtonClicked() {

        CallButton.setDisable(true);


        Runnable producer = () -> {

            int i = 0;

            while (true) {

                try {
                    arrayBlockingQueue.put(++i);
                    logger.info("Call #" + i + " added to queue ");
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
                    logger.info("Call #" + poll + " taken from the queue and handled.");
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
        logger.info("Call ended and connection shutdown.");
        System.exit(0);
    }


    public void TakeCallButtonClicked() {
        logger.info("Call is being handled by me.");

        Runnable interruptingCall = () -> {
            boolean terminate = false;

            while (!terminate) {
                try {
                    synchronized (this) {
                        Integer poll;
                        poll = arrayBlockingQueue.take();
                        logger.info("Consumer Thread was being interrupt when I took the Call #" + poll);
                        TimeUnit.MILLISECONDS.sleep(1000);
                        logger.info("Call is completed for call #" + poll);
                        terminate = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread takenCall = new Thread((interruptingCall));
        takenCall.setDaemon(true);

        try {
            takenCall.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        takenCall.start();


    }

    public void EscalateCallButtonClicked() {
        logger.info("Call is being escalated to my supervisor. I am unavailable at present.");


        Runnable escalatingCall = () -> {
            boolean terminate = false;

            while (!terminate) {
                try {
                    synchronized (this) {
                        Integer poll;
                        poll = arrayBlockingQueue.take();
                        logger.info("Consumer Thread was being interrupt when I escalated the Call #" + poll);
                        logger.info("Call escalation of call #" + poll + " to my supervisor.");
                        TimeUnit.MILLISECONDS.sleep(1000);
                        terminate = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread passedCall = new Thread((escalatingCall));
        passedCall.setDaemon(true);

        try {
            passedCall.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        passedCall.start();


    }

    public void MeetingRadioClicked(ActionEvent actionEvent) {

            EscalateCallButton.setDisable(true);
            TakeCallButton.setDisable(true);


        employeeObservable.addEmployeeObserver(employeeObserver);
        employeeObservable.setEmployeeStatusUpdate(EmployeeStatus.IN_A_MEETING);
        logger.info("Employee on " + Thread.currentThread().getName() + " has set their status to: " + employeeObserver.getEmployeeStatus());


    }


    public void LunchRadioClicked(ActionEvent actionEvent) {
        EscalateCallButton.setDisable(true);
        TakeCallButton.setDisable(true);

        employeeObservable.addEmployeeObserver(employeeObserver);
        employeeObservable.setEmployeeStatusUpdate(EmployeeStatus.ON_LUNCH);
        logger.info("Employee on " + Thread.currentThread().getName() + " has set their status to: " + employeeObserver.getEmployeeStatus());


    }

    public void AwayRadioClicked(ActionEvent actionEvent) {
        EscalateCallButton.setDisable(true);
        TakeCallButton.setDisable(true);

        employeeObservable.addEmployeeObserver(employeeObserver);
        employeeObservable.setEmployeeStatusUpdate(EmployeeStatus.GONE_FOR_THE_DAY);
        logger.info("Employee on " + Thread.currentThread().getName() + " has set their status to: " + employeeObserver.getEmployeeStatus());


    }

    public void AvailableRadioClicked(ActionEvent actionEvent) {
        EscalateCallButton.setDisable(false);
        TakeCallButton.setDisable(false);

        employeeObservable.addEmployeeObserver(employeeObserver);
        employeeObservable.setEmployeeStatusUpdate(EmployeeStatus.AVAILABLE);
        logger.info("Employee on " + Thread.currentThread().getName() + " has set their status to: " + employeeObserver.getEmployeeStatus());


    }
}

