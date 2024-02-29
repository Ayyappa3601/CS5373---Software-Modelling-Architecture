// Ayyappa Reddy - Java codes

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

//implementation of Purchase order manager 

class PurchaseOrderManager {
    public static List<CustomerAccount> customerAccounts = new ArrayList<>();
    public static List<DeliveryOrder> deliveryOrders = new ArrayList<>();

    public static void main(String[] args) {
        CustomerAccount.initializeSampleData();
        DeliveryOrder.initializeSampleData();
        while(true){

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your account ID: ");
            String accountId = scanner.next();

            CustomerAccount customerAccount = findCustomerAccount(accountId);
            if (customerAccount != null) {
                System.out.println("Welcome, " + customerAccount.getAccountId());
                if(verifyCustomerDetails(customerAccount)) {

                    System.out.print("Enter item name: ");
                    String item = scanner.next();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    System.out.print("Enter total price: ");
                    double totalPrice = scanner.nextDouble();

                    int authorizationNumber = BankInterface.requestAuthorizationFromBank();
                    if (authorizationNumber != -1) {
                        DeliveryOrder newOrder = new DeliveryOrder(accountId, item, quantity, totalPrice, authorizationNumber);
                        deliveryOrders.add(newOrder);
                        System.out.println("Order placed successfully!");
                        System.out.println("Order details :\n Account Id :"+newOrder.getAccountId()+" \n Item name : "+newOrder.getItem()+"\n Number of quantity : "+newOrder.getQuantity()+"\n Total Price : " + newOrder.getTotalPrice()+" \n Authorization Number :"+newOrder.getAuthorizationNumber());
                    } else {
                        System.out.println("Payment authorization failed.");
                    }
                }else{
                    System.out.println("Invalid Card number or password. Please enter the details correctly");

                }
            } else {
                System.out.println("Customer not found.");
                if(!CustomerInterface.createNewAccount()){
                    break;
                }
            }
        }
    }


    private static boolean verifyCustomerDetails(CustomerAccount account) {
        Scanner sc = new Scanner(System.in);

        if(account !=null){
            System.out.print("Enter password: ");
            int pwd = sc.nextInt();
            System.out.print("Enter Card number: ");
            long c_no = sc.nextLong();
            return Objects.equals(c_no, account.getCardNumber()) && Objects.equals(pwd, account.getPassword());
        }
        return false;
    }
    private static CustomerAccount findCustomerAccount(String accountId) {
        for (CustomerAccount account : customerAccounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        return null;
    }

}

//implementation of Bank interface

class BankInterface {

    public static int requestAuthorizationFromBank() {
        // Simulate bank authorization process
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter authorization number (4 digits): ");
        int authorizationNumber = scanner.nextInt();
        if (authorizationNumber >= 1000 && authorizationNumber <= 9999) {
            return authorizationNumber;
        } else {
            System.out.println("Invalid authorization number.");
            return -1;
        }
    }
}

//implementation of DeliveryOrder 

class DeliveryOrder {
    private String accountId;
    private String item;
    private int quantity;
    private double totalPrice;
    private int authorizationNumber;

    public static void initializeSampleData() {
        PurchaseOrderManager.deliveryOrders.add(new DeliveryOrder("alex", "note", 3, 20.00, 3333));
        PurchaseOrderManager.deliveryOrders.add(new DeliveryOrder("john", "book", 1, 50.00, 5555));
        PurchaseOrderManager.deliveryOrders.add(new DeliveryOrder("sam", "pencil", 20, 10.00, 7777));
    }

    public DeliveryOrder(String accountId, String item, int quantity, double totalPrice, int authorizationNumber) {
        this.accountId = accountId;
        this.item = item;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.authorizationNumber = authorizationNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getAuthorizationNumber() {
        return authorizationNumber;
    }
}

//implementation of CustomerAccount

class CustomerAccount {
    private String accountId;
    private int password;
    private long cardNumber;
    private String customerEmail;
    public static void initializeSampleData() {
        PurchaseOrderManager.customerAccounts.add(new CustomerAccount("steve", 2345, 12345678, "steve@ttu.edu"));
        PurchaseOrderManager.customerAccounts.add(new CustomerAccount("alex",  4567, 23456789, "alex@ttu.edu"));
        PurchaseOrderManager.customerAccounts.add(new CustomerAccount("jane",  6789, 45678901, "jane@ttu.edu"));
        PurchaseOrderManager.customerAccounts.add(new CustomerAccount("john",  5678, 56789012, "john@ttu.edu"));
        PurchaseOrderManager.customerAccounts.add(new CustomerAccount("sam",   7890, 89012345, "sam@ttu.edu"));
    }
    public CustomerAccount(String accountId, int password, long cardNumber, String customerEmail) {
        this.accountId = accountId;
        this.password = password;
        this.cardNumber = cardNumber;
        this.customerEmail = customerEmail;
    }


    public String getAccountId() {
        return accountId;
    }

    public int getPassword() {
        return password;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}

//implementation of CustomerInterface

class CustomerInterface {
    public static boolean createNewAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to create a new account?(y/n):");
        char newAcc = scanner.next().charAt(0);
        if (Objects.equals(newAcc, 'y')) {

            System.out.print("Enter name: ");
            String name = scanner.next();
            System.out.print("Enter Card Number: ");
            long cardNumber = scanner.nextLong();
            System.out.print("Enter password: ");
            int pass = scanner.nextInt();
            System.out.print("Enter mail id: ");
            String mail = scanner.next();
            PurchaseOrderManager.customerAccounts.add(new CustomerAccount(name, pass, cardNumber, mail));
            return true;
        }
        return false;
    }
}

//My understanding is - The credit card numbers will be initialized while writing the code. In the terminal when the credit card number is asked, we have to enter the credit card number.
//if it matches the already initialized credit card numbers of the CustomerAccount it will process the further request.
//if it not matches through the CustomerAccount credit card details it will ask to enter correct details.
//if the account is not created in the above code the alternative sequence will comes in to picture here and ask to create an account wiht all the details as mentioned in the code.
//1. Account ID 2. Password 3. Creditcard number 4. Mail 
//And then it will create account later on we can enter the account details and purchase the order.

