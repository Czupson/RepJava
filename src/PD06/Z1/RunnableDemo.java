package PD06.Z1;

public class RunnableDemo {
    public static void main(String[] args) {
        Runnable runnable1 = () -> System.out.println("Startuje program...");
        Runnable runnable2 = () -> System.out.println("Kończę program...");

        runnable1.run();
        runnable2.run();

        new Thread(runnable2).start();
    }
}
