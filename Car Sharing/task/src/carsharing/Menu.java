package carsharing;

import carsharing.dao.CarJpaImpl;
import carsharing.dao.CompanyJpaImpl;
import carsharing.domain.Car;
import carsharing.domain.Company;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static final CompanyJpaImpl companies = new CompanyJpaImpl();
    private static final CarJpaImpl cars = new CarJpaImpl();


    public static void process() {
        boolean mainMenu = true;
        while (mainMenu) {
            System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");

            int option = sc.nextInt();
            switch (option) {
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
            System.out.println(company.getName() + " company\n");

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
        Optional<Company> byId = companies.findById(company.getId());
        Company thisCompany = byId.get();

        if (list.isEmpty()) {
            System.out.println("\nThe car list is empty!\n");
        }

        for (Car car : list) {
            if (car.getCompanyId() == thisCompany.getId()) {
                IntStream.iterate(1, i -> i <= list.size(), i -> i + 1)
                        .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1)
                                .getName()));
            }
        }

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

}
