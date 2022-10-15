package carsharing;

import carsharing.dao.CarJpaImpl;
import carsharing.dao.CompanyJpaImpl;
import carsharing.dao.CustomerJpaImpl;
import carsharing.domain.Car;
import carsharing.domain.Company;
import carsharing.domain.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static final CompanyJpaImpl companies = new CompanyJpaImpl();
    private static final CarJpaImpl cars = new CarJpaImpl();
    private static final CustomerJpaImpl customers = new CustomerJpaImpl();

    private static final String YOU_DIDNT_RENT_CAR = "You didn't rent a car!";

    public static void process() {
        boolean mainMenu = true;
        while (mainMenu) {
            System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");

            int option = sc.nextInt();
            switch (option) {
                case 3:
                    createCustomer();
                    break;
                case 2:
                    processCustomerMenu();
                    break;
                case 1:
                    processManagerMenu();
                    break;
                case 0:
                    mainMenu = false;
                    break;
                default:
                    System.out.println("Wrong option chosen.");
            }
        }
    }

    private static void processCustomerMenu() {
        while(true){
            final List<Customer> allCustomers = customers.findAll();
            if (allCustomers.isEmpty()) {
                System.out.println("\nThe customer list is empty!\n");
                return;
            }
            System.out.println("Choose a customer:");
            showCustomers();
            System.out.println("0. Back");
            int customerOption = sc.nextInt();
            for (Customer customer : allCustomers) {
                if (customerOption == customer.getId()) {
                    processSingleCustomerMenu(customer);
                }
                if (customerOption == 0) {
                    process();
                }
            }
            return;
        }
    }

    private static void processSingleCustomerMenu(Customer customer) {
        boolean subMenu = true;
        while (subMenu) {
            System.out.println("");

            System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    rentCar(customer);
                    break;
                case 2:
                    returnRentedCar(customer);
                    break;
                case 3:
                    myRentedCar(customer);
                    break;
                case 0:
                    subMenu = false;
                    process();
                    break;
                default:
                    System.out.println("Wrong choice");
            }

        }
    }

    private static void rentCar(Customer customer) {
        while (true) {
            Customer currentCustomer = customers.findByName(customer.getName()).get();
            if (currentCustomer.getRentedCarId() != 0){
                System.out.println("You've already rented a car!");
                return;
            }
            final List<Company> allCompanies = companies.findAll();
            if (allCompanies.isEmpty()) {
                System.out.println("\nThe company list is empty!\n");
                return;
            }

            System.out.println("Choose a company:");
            showCompanies();
            System.out.println("0. Back");
            int customerOption = sc.nextInt();
            for (Company company : allCompanies) {
                if (customerOption == company.getId()) {
                    processSelectionOfCarMenu(customer, company);
                }
                if (customerOption == 0) {
                    processSingleCustomerMenu(customer);
                }
            }
        }
    }

    private static void processSelectionOfCarMenu(Customer customer, Company company) {
        List<Car> availableCars = getCars(company);
        if (availableCars == null) {
            return;
        }

        System.out.println("Choose a car:");

        availableCars.forEach(car -> System.out.printf("%d. %s%n", car.getId(), car.getName()));

        System.out.println("0. Back");
        int customerOption = sc.nextInt();
        for (Car car : availableCars) {
            if (customerOption == car.getId()) {
                customers.insertRentedCar(customer,car.getId());
                System.out.printf("You rented '%s'%n", car.getName());
                processSingleCustomerMenu(new Customer(customer.getId(), customer.getName(), car.getId()));
            }
            if (customerOption == 0) {
                rentCar(customer);
            }
        }

    }

    private static List<Car> getCars(Company company) {
        List<Integer> allRentedCarsId = customers.findAllRentedCars();
        List<Car> companyCars = cars.findByCompany(company);
        List<Car> availableCars = new ArrayList<>();
        for (var car: companyCars) {
            long result = allRentedCarsId.stream()
                                         .filter(id -> car.getId() == id)
                                         .count();
            if(result != 1){
                availableCars.add(car);
            }
        }

        if (availableCars.isEmpty()) {
            System.out.printf("%nNo available cars in the %s company.%n", company.getName());
            return null;
        }
        return availableCars;
    }

    private static void returnRentedCar(Customer customer) {
        Customer currentCustomer = customers.findByName(customer.getName()).get();
        if (currentCustomer.getRentedCarId() == 0){
            System.out.println(YOU_DIDNT_RENT_CAR);
            return;
        }
        boolean carDeleted = customers.deleteRentedCarFromCustomer(currentCustomer);
        if (carDeleted) {
            System.out.println("You've returned a rented car!");
        } else{
            System.out.println("Something goes wrong when returning car!");
        }
    }

    private static void myRentedCar(Customer customer) {
        Customer currentCustomer = customers.findByName(customer.getName()).get();
        if (currentCustomer.getRentedCarId() == 0){
            System.out.println(YOU_DIDNT_RENT_CAR);
            return;
        }
        Car rentedCar = cars.findById(currentCustomer.getRentedCarId())
                            .get();
        Company company = companies.findById(rentedCar.getCompanyId())
                                   .get();
        System.out.printf("Your rented car: %n%s%nCompany:%n%s%n",rentedCar.getName(), company.getName());
    }

    private static void createCustomer() {
        System.out.printf("%n%nEnter the customer name:%n");
        sc.nextLine();
        String customerName = sc.nextLine();
        customers.insert(new Customer(0,customerName, 0));
        System.out.println("Customer was added!");
    }

    private static void processManagerMenu() {
        boolean subMenu = true;
        while (subMenu) {
            System.out.println("1. Company list\n2. Create a company\n0. Back");
            int option = sc.nextInt();

            switch (option) {
                case 1:
                    processCompaniesMenu();
                    break;
                case 2:
                    createCompany();
                    break;
                case 0:
                    subMenu = false;
                    break;
                default:
                    System.out.println("Wrong option chosen.");
            }
        }
    }

    static boolean processCompaniesMenu() {
        final List<Company> allCompanies = companies.findAll();
        if (allCompanies.isEmpty()) {
            System.out.println("\nThe company list is empty!\n");
            return false;
        }
        System.out.println("Choose a company: ");
        showCompanies();
        System.out.println("0. Back");
        int companyOption = sc.nextInt();
        for (Company company : allCompanies) {
            if (companyOption == company.getId()) {
                processSingleCompanyMenu(company);
            }
            if (companyOption == 0) {
                processManagerMenu();
            }
        }
        return true;
    }


    private static void processSingleCompanyMenu(Company company) {
        boolean subMenu = true;
        while (subMenu) {
            System.out.println("\n" + company.getName() + " company");

            System.out.println("1. Car list\n2. Create a car\n0. Back");
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    showCars(company);
                    break;
                case 2:
                    createCar(company);
                    break;
                case 0:
                    subMenu = false;
                    break;
                default:
                    System.out.println("Wrong choice");
            }
        }
    }


    private static void createCar(Company company) {
        System.out.println("Enter the car name:");
        sc.nextLine();
        String name = sc.nextLine();
        final Car insert = cars.insert(new Car(1, name, company.getId()));
        if (insert != null) {
            System.out.println("\nThe car was created!\n");
        } else {
            System.out.println("\nThe car wasn't created\n");
        }
    }

    private static boolean showCars(Company company) {
        List<Car> list = cars.findByCompany(company);
        if (list.isEmpty()) {
            System.out.println("\nThe car list is empty!\n");
        }
        IntStream.iterate(1, i -> i <= list.size(), i -> i + 1)
                .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1)
                        .getName()));
        return true;
    }

    private static void createCompany() {
        System.out.println("Enter the company name:");
        sc.nextLine();
        String name = sc.nextLine();
        final Company insert = companies.insert(new Company(0, name));
        if (insert != null) {
            System.out.println("\nThe company was created!\n");
        } else {
            System.out.println("\nThe company wasn't created\n");
        }
    }

    private static void showCompanies() {
        List<Company> list = companies.findAll();
        IntStream.iterate(1, i -> i <= list.size(), i -> i + 1)
                .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1)
                        .getName()));
    }

    private static void showCustomers() {
        List<Customer> list = customers.findAll();
        IntStream.iterate(1, i -> i <= list.size(), i -> i + 1)
                 .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1)
                                                                    .getName()));
    }

}
