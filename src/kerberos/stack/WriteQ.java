package kerberos.stack;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingDeque;

public class WriteQ {

    private Exception exception;

    private final LinkedBlockingDeque<LockedOperation> output;
    private final LinkedList<Thread> waiters;

    /**
     * Create a Write-Queue that acts as interface between environments with 
     * different Threads.
     */
    public WriteQ() {
        output = new LinkedBlockingDeque<LockedOperation>();
        waiters = new LinkedList<Thread>();
    }

    /**
     * Takes an LockedOperation from the output queue.
     * @return an LockedOperation from the output queue
     * @throws Exception if the Write-Queue was closed by a different Thread
     */
    public LockedOperation take() throws Exception{
        return output.take();
    }

    /**
     * Adds an object to the write queue and blocks until the object has been
     * processed in the other environment and the blocking thread gets notified.
     * @param o the object to process
     * @throws Exception if the Write-Queue has been terminated or the blocking
     *                   Thread is interrupted
     */
    public void add(Object o) throws Exception{
        if(exception != null)
            throw exception;

        Thread thread = Thread.currentThread();
        synchronized(waiters){
            waiters.add(thread);
        }
        try{
            LockedOperation data = new LockedOperation(o);
            output.add(data);
            data.pause();
        }catch(InterruptedException e){
            if(exception != null)
                throw exception;
        }finally{
            synchronized(waiters){
                waiters.remove(thread);
            }
        }
    }

    /**
     * Interrupts all blocking Threads from add and lets them throw exceptions.
     * @param e the exception the blocking Threads should throw
     */
    public void terminate(Exception e){
        exception = e;
        synchronized(waiters){
            for(Thread t: waiters) // wake waiters
                t.interrupt();
            waiters.clear();
        }
    }

    public class LockedOperation{

        private final Object data;
        private final Object lock;

        private boolean resumed = false;
        private Exception exception = null;

        /**
         * Synchronizes two Threads performing one operation across differently
         * threaded environments.
         * One Thread blocks until the other Thread finishes execution and 
         * wakes the first.
         * @param data the data, that should be processed
         */
        public LockedOperation(Object data) {
            this.data = data;
            this.lock = new Object();
        }

        public Object getData() {
            return data;
        }

        /**
         * Blocks the calling Thread until the data has been processed and the 
         * resume method is called.
         * @throws Exception if an exception has occurred during the execution
         */
        public void pause() throws Exception{
            synchronized(lock){
                if(resumed){
                    return;
                }
                while(!resumed){
                    lock.wait();
                }
            }
            if(exception != null)
                throw exception;
        }

        /**
         * Wakes all Threads, that wait for the processing to finish.
         */
        public void resume() {
            synchronized (lock) {
                resumed = true;
                lock.notifyAll();
            }
        }

        /**
         * Wakes all waiting Threads and lets them throw the given exception.
         * @param exception the exception the Threads should throw
         */
        public void interrupt(Exception exception){
            this.exception = exception;
            this.resume();
        }

    }

}
