package carsharing;


public class Main {
    public static void main(String[] args) {
        H2Utils.setName(args);
        H2Utils.createDb();
        H2Utils.createTableCar();
        H2Utils.createTableCustomer();
        Menu.process();
    }
}