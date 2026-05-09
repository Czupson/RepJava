package PD04.session;

import PD04.error.RentalStateException;

import java.util.ArrayList;
import java.util.List;

public class SessionLog implements AutoCloseable {
    private final List<String> entries = new ArrayList<>();
    private boolean closed = false;

    public void log(String line){
        if (closed) {
            throw new RentalStateException("Session is already closed");
        }
        entries.add(line);
        System.out.println(line);
    }
    public int entryCount(){
        return entries.size();
    }
    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        System.out.println("Session ended");
        System.out.println("Operations: " + entries.size());
    }
}
