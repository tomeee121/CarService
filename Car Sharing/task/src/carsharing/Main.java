package carsharing;


public class Main {
    public static void main(String[] args) {
        H2Utils.setName(args);

        H2Utils.createDb();
        Menu.process();
    }
}