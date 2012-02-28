package kerberos.stack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class ReadQ {

    private Exception exception;

    private final LinkedBlockingDeque<Object> input;
    private final LinkedList<Thread> waiters;

    /**
     * Create a Read-Queue that acts as interface between environments with 
     * different Threads.
     */
    public ReadQ() {
        input = new LinkedBlockingDeque<Object>();
        waiters = new LinkedList<Thread>();
    }

    /**
     * Takes an Object from the input queue of blocks until one is available.
     * @return an Object from the input queue
     * @throws Exception if the Read-Queue was closed by a different Thread
     */
    public Object take() throws Exception{
        if(exception != null)
            throw exception;

        Thread thread = Thread.currentThread();
        synchronized(waiters){
            waiters.add(thread);
        }
        try{
            Object object = input.take();

            if(object instanceof Exception)
                throw (Exception) object;

            return object;
        }catch(InterruptedException e){
            if(exception != null)
                throw exception;
        }finally{
            synchronized(waiters){
                waiters.remove(thread);
            }
        }

        return null;
    }

    /**
     * Takes all objects currently in the input queue without blocking.
     * If the input queue is empty a empty collection is returned
     * @return a collection of objects
     */
    public Collection<Object> takeAvailable(){
        List<Object> objects = new ArrayList<Object>();
        input.drainTo(objects);
        return objects;
    }

    /**
     * Adds an object to the input queue, which will wake one blocking Thread.
     * @param o the object to add
     */
    public void add(Object o){
        input.add(o);
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

}
