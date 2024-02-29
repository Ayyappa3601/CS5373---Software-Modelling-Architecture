#Ayyappa Reddy - C++ codes

#include <iostream>
#include <vector>
#include <limits>

class BankInterface;

//Implementation of DeliveryOrder
class DeliveryOrder {
public:
    DeliveryOrder(const std::string& accountId, const std::string& item, int quantity, double totalPrice, int authorizationNumber)
        : accountId(accountId), item(item), quantity(quantity), totalPrice(totalPrice), authorizationNumber(authorizationNumber) {}

    std::string getAccountId() const { return accountId; }
    std::string getItem() const { return item; }
    int getQuantity() const { return quantity; }
    double getTotalPrice() const { return totalPrice; }
    int getAuthorizationNumber() const { return authorizationNumber; }

    static void initializeSampleData(std::vector<DeliveryOrder>& deliveryOrders) {
        deliveryOrders.push_back(DeliveryOrder("alex", "note", 3, 20.00, 3333));
        deliveryOrders.push_back(DeliveryOrder("john", "book", 1, 50.00, 5555));
        deliveryOrders.push_back(DeliveryOrder("sam", "pencil", 20, 10.00, 7777));
    }

private:
    std::string accountId;
    std::string item;
    int quantity;
    double totalPrice;
    int authorizationNumber;
};

//Implementation of CustomerAccount
class CustomerAccount {
public:
    CustomerAccount(const std::string& accountId, int password, long cardNumber, const std::string& customerEmail)
        : accountId(accountId), password(password), cardNumber(cardNumber), customerEmail(customerEmail) {}

    std::string getAccountId() const { return accountId; }
    int getPassword() const { return password; }
    long getCardNumber() const { return cardNumber; }
    std::string getCustomerEmail() const { return customerEmail; }

    static void initializeSampleData(std::vector<CustomerAccount>& customerAccounts) {
        customerAccounts.push_back(CustomerAccount("steve", 2345, 12345678, "steve@ttu.edu"));
        customerAccounts.push_back(CustomerAccount("alex",  4567, 23456789, "alex@ttu.edu"));
        customerAccounts.push_back(CustomerAccount("jane",  6789, 45678901, "jane@ttu.edu"));
        customerAccounts.push_back(CustomerAccount("john",  5678, 56789012, "john@ttu.edu"));
        customerAccounts.push_back(CustomerAccount("sam",   7890, 89012345, "sam@ttu.edu"));
    }

private:
    std::string accountId;
    int password;
    long cardNumber;
    std::string customerEmail;
};

// Implementation of CustomerInterface
class CustomerInterface {
public:
    static bool createNewAccount(std::vector<CustomerAccount>& customerAccounts);
};

bool CustomerInterface::createNewAccount(std::vector<CustomerAccount>& customerAccounts) {
    std::cout << "Do you want to create a new account? (y/n): ";
    char newAcc;
    std::cin >> newAcc;

    if (newAcc == 'y') {
        std::string name, email;
        int password;
        long cardNumber;

        std::cout << "Enter name: ";
        std::cin >> name;
        std::cout << "Enter password: ";
        std::cin >> password;
        std::cout << "Enter Card Number: ";
        std::cin >> cardNumber;
        std::cout << "Enter mail id: ";
        std::cin >> email;

        customerAccounts.push_back(CustomerAccount(name, password, cardNumber, email));
        std::cout << "Account created successfully." << std::endl;
        return true;
    }
    return false;
}

// Implementation of BankInterface
class BankInterface {
public:
    static int requestAuthorizationFromBank();
}; 

int BankInterface::requestAuthorizationFromBank() {
    int authorizationNumber;
    std::cout << "Enter authorization number (4 digits): ";
    std::cin >> authorizationNumber;

    if (authorizationNumber >= 1000 && authorizationNumber <= 9999) {
        return authorizationNumber;
    } else {
        std::cout << "Invalid authorization number." << std::endl;
        return -1;
    }
}

// Implementation of PurchaseOrderManager
class PurchaseOrderManager {
public:
    static std::vector<CustomerAccount> customerAccounts;
    static std::vector<DeliveryOrder> deliveryOrders;

    static void main();

private:
    static bool verifyCustomerDetails(const CustomerAccount& account);
    static CustomerAccount* findCustomerAccount(const std::string& accountId);
};

std::vector<CustomerAccount> PurchaseOrderManager::customerAccounts;
std::vector<DeliveryOrder> PurchaseOrderManager::deliveryOrders;

bool PurchaseOrderManager::verifyCustomerDetails(const CustomerAccount& account) {
    std::cout << "Enter password: ";
    int pwd;
    std::cin >> pwd;

    std::cout << "Enter Card number: ";
    long c_no;
    std::cin >> c_no;

    return (c_no == account.getCardNumber()) && (pwd == account.getPassword());
}

CustomerAccount* PurchaseOrderManager::findCustomerAccount(const std::string& accountId) {
    for (auto& account : customerAccounts) {
        if (account.getAccountId() == accountId) {
            return &account;
        }
    }
    return nullptr;
}

void PurchaseOrderManager::main() {
    CustomerAccount::initializeSampleData(customerAccounts);
    DeliveryOrder::initializeSampleData(deliveryOrders);

    while (true) {
        std::cout << "Enter your account ID: ";
        std::string accountId;
        std::cin >> accountId;

        CustomerAccount* customerAccount = findCustomerAccount(accountId);
        if (customerAccount != nullptr) {
            std::cout << "Welcome, " << customerAccount->getAccountId() << std::endl;
            if (verifyCustomerDetails(*customerAccount)) {
                std::cout << "Enter item name: ";
                std::string item;
                std::cin >> item;
                std::cout << "Enter quantity: ";
                int quantity;
                std::cin >> quantity;
                std::cout << "Enter total price: ";
                double totalPrice;
                std::cin >> totalPrice;

                int authorizationNumber = BankInterface::requestAuthorizationFromBank();
                if (authorizationNumber != -1) {
                    DeliveryOrder newOrder(accountId, item, quantity, totalPrice, authorizationNumber);
                    deliveryOrders.push_back(newOrder);
                    std::cout << "Order placed successfully!" << std::endl;
                    std::cout << "Order details:\nAccount Id: " << newOrder.getAccountId()
                              << "\nItem name: " << newOrder.getItem()
                              << "\nNumber of quantity: " << newOrder.getQuantity()
                              << "\nTotal Price: " << newOrder.getTotalPrice()
                              << "\nAuthorization Number: " << newOrder.getAuthorizationNumber() << std::endl;
                } else {
                    std::cout << "Payment authorization failed." << std::endl;
                }
            } else {
                std::cout << "Invalid Card number or password. Please enter the details correctly" << std::endl;
            }
        } else {
            std::cout << "Customer not found." << std::endl;
            if (!CustomerInterface::createNewAccount(customerAccounts)) {
                break;
            }
        }
    }
}

int main() {
    PurchaseOrderManager::main();
    return 0;
}


//My Assumptions are: 
//1. While writing the code we initialized all the customer details including the credit card details. In the terminal, while asked to give the card details
//we will provide the card details. 

// (A)If the card details matches with the initialized card details then only it will move to the further process. 
// (B)If card details doesn't match with the initialized card details it will ask to enter the correct information.

//2. While writing the code we also initialized the customer Account details mentioned in the sample output.In the terminal, while asked to enter the account details we will be providing the details accordingly.

// (A)If the customer Account details matches with the initialized customer Account details then only it will move to the further process. 
// (B)If the customer Account details doesn't match with the initialized customer Account details it will ask to create an account. And then again it will starts working in loop.

