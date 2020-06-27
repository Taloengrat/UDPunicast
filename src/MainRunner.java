public class MainRunner {
    public static void main(String[] args) throws Exception {
        new Thread(new Server()).start();

        new Thread(new Client()).start();




    }
}
