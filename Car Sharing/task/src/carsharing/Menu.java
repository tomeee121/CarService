package carsharing;

import carsharing.dao.CompanyJpaImpl;
import carsharing.domain.Company;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static final CompanyJpaImpl companies = new CompanyJpaImpl();


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
                    showCompanies();
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

    private static void createCompany() {
        System.out.println("Enter the company name:");
        sc.nextLine();
        String name = sc.nextLine();
        companies.insert(new Company(0, name));
        System.out.println("\nThe company was created!\n");
    }

    private static boolean showCompanies() {
        List<Company> list = companies.findAll();
        if (list.isEmpty()) {
            System.out.println("\nThe company list is empty!\n");
            return false;
        }
        IntStream.iterate(1, i -> i <= list.size(), i -> i + 1)
                 .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1)
                                                                    .getName()));

        return true;
    }

}
