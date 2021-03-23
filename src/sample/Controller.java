package sample;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Controller {

    ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(5);
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    Lock lock = new ReentrantLock();

    public void CallButtonClicked() {


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
                    poll = arrayBlockingQueue.take();
                    System.out.println("Call #" + poll + " taken from the queue and handled.");
                    TimeUnit.MILLISECONDS.sleep(1000);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        executorService.submit(consumer);
        executorService.submit(producer);

        executorService.shutdown();


    }

    public void TakeCallButtonClicked() {
        System.out.println("Call is being handled");


        Runnable interruptingCall = () -> {
            while (true) {
                try {

                    Integer poll;
                    poll = arrayBlockingQueue.take();
                    System.out.println("Call is being interrupted");
                    TimeUnit.MILLISECONDS.sleep(1000);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        executorService.submit(interruptingCall);

        executorService.shutdown();



    }

    public void EscalateCallButtonClicked() {
        System.out.println("Call is being escalated");
        Thread.currentThread().interrupt();

    }

}

