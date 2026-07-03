import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args){
        String input = "";
        int choice = 0;
        VendingMachine vendingMachine = new VendingMachine(20);
        Scanner scanner = new Scanner(System.in);

        do{
            clear_terminal();
            System.out.println("===== Vending Machine Simulator =====");
            System.out.println("1. Test Vending Machine");
            System.out.println("2. Exit");
            System.out.print("Choice: ");
            input = scanner.nextLine();

            try{
                choice = Integer.parseInt(input);
            }
            catch (NumberFormatException e){
                choice = 0;
            }

            if (choice == 1){
                testMachine(vendingMachine, scanner);
            }
            else if(choice < 1 || choice > 2){
                System.out.println("invalid Input!");
                pause(scanner);
            }
        } while(choice != 2);
    }

    static void clear_terminal(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pause(Scanner scanner){
        System.out.print("Press 'Enter' to continue...");
        scanner.nextLine();
    }

    static void testMachine(VendingMachine vendingMachine, Scanner scanner){
        String input = "";
        int choice = 0;

         do{
            clear_terminal();
            System.out.println("1. Vending Features");
            System.out.println("2. Maintenance Features");
            System.out.println("3. Back to Main Menu");
            System.out.print("Choice: ");
            input = scanner.nextLine();

            try{
                choice = Integer.parseInt(input);
            }
            catch (NumberFormatException e){
                choice = 0;
            }

            if (choice == 1){
                vending(vendingMachine, scanner);
            }
            else if(choice == 2){
                maintenance(vendingMachine, scanner);
            }
            else if(choice < 1 || choice > 3){
                System.out.println("Invalid Input!");
                pause(scanner);
            }
        }while(choice != 3);
    }

    static void vending(VendingMachine vendingMachine, Scanner scanner){
        String input = "";
        int choice = 0;

         do{
            clear_terminal();
            System.out.println("1. Display All Available Items");
            System.out.println("2. Make a Purchase");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            input = scanner.nextLine();

            try{
                choice = Integer.parseInt(input);
            }
            catch (NumberFormatException e){
                choice = 0;
            }

            if (choice == 1){
                clear_terminal();
                vendingMachine.displayItems();
                pause(scanner);
            }
            else if(choice == 2){
                vendingMachine.purchaseItem(scanner);
            }
            else if(choice < 1 || choice > 3){
                System.out.println("Invalid Input!");
                pause(scanner);
            }
        }while(choice != 3);
    }

    static void maintenance(VendingMachine vendingMachine, Scanner scanner){
        String input = "";
        int choice = 0;

         do{
            clear_terminal();
            System.out.println("1. Restock Items");
            System.out.println("2. Set Item Price");
            System.out.println("3. Replenish Cash Reserves");
            System.out.println("4. View Transaction Summary");
            System.out.println("5. Collect Money");
            System.out.println("6. Back");
            System.out.print("Choice: ");
            input = scanner.nextLine();

            try{
                choice = Integer.parseInt(input);
            }
            catch (NumberFormatException e){
                choice = 0;
            }

             switch(choice){
                case 1:
                    vendingMachine.restockItems(scanner);
                    break;
                case 2:
                    vendingMachine.setPrices(scanner);
                    break;
                case 3:
                    vendingMachine.replenishCash(scanner);
                    break;
                case 4:
                    vendingMachine.displayTransactionSummary(scanner);
                    break;
                case 5:
                    clear_terminal();
                    vendingMachine.collectCash();
                    pause(scanner);
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid Input!");
                    pause(scanner);
             }
        }while(choice != 6);
    }
}