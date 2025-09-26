import java.util.*;

//Abstraction
interface Account {
    void credit(int amount);

    void debit(int amount);

    void showBalance();

    String getAccountType();
}

// Inheritance and Encapsulation
class SavingsAccount implements Account {
    private String accountNumber;
    private int balance;

    public SavingsAccount(String accountNumber, int balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void credit(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Credited " + amount + " | Balance: " + balance);
        } else {
            System.out.println("Invalid credit amount.");
        }
    }

    public void debit(int amount) {
        if (balance - amount < 1000) {
            System.out.println("Cannot withdraw. Minimum balance of 1000 required.");
        } else if (amount > 0) {
            balance -= amount;
            System.out.println("Debited " + amount + " | Balance: " + balance);
        } else {
            System.out.println("Invalid debit amount.");
        }
    }

    public void showBalance() {
        System.out.println(getAccountType() + " [" + accountNumber + "] | Balance: " + balance);
    }

    public String getAccountType() {
        return "Savings Account";
    }
}

// Inheritance and Encapsulation
class CurrentAccount implements Account {
    private String accountNumber;
    private int balance;
    private final int overdraftLimit = 5000;

    public CurrentAccount(String accountNumber, int balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void credit(int amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Credited " + amount + " | Balance: " + balance);
        } else {
            System.out.println("Invalid credit amount.");
        }
    }

    public void debit(int amount) {
        if (balance - amount < -overdraftLimit) {
            System.out.println("Overdraft limit exceeded!");
        } else if (amount > 0) {
            balance -= amount;
            System.out.println("Debited " + amount + " | Balance: " + balance);
        } else {
            System.out.println("Invalid debit amount.");
        }
    }

    public void showBalance() {
        System.out.println(getAccountType() + " [" + accountNumber + "] | Balance: " + balance);
    }

    public String getAccountType() {
        return "Current Account";
    }
}

public class BankApp {
    public static void main(String[] args) {

        List<Account> accounts = new ArrayList<>();
        accounts.add(new SavingsAccount("SAV101", 5000));
        accounts.add(new CurrentAccount("CUR202", 2000));
        accounts.add(new SavingsAccount("SAV102", 10000));
        accounts.add(new CurrentAccount("CUR203", 0));

        for (Account acc : accounts) {
            acc.showBalance(); // Behaves differently based on object type - polymorphism
            acc.credit(2000);
            acc.debit(1500);
            System.out.println();
        }

        accounts.get(1).debit(6000);
        accounts.get(0).debit(6000);

    }
}

// SOLID principle
// S - Single Responsibility principle
// Current account and saving accouhts have single responsibilty to manage the
// specific accounts

// O - Open and Close Priciple
// In this we don't need to modifiy the saving accounts and current accounts if
// new type of account introduced.(Example - FixedDepositAccount)

// L - Liskov Substitution Principle
// In case of list we can use Account for the purpose of SavingAccount and
// CurrentAccount operations without breaking the program

// I – Interface Segregation Principle
// Only important abstract methods are kept in interface no class is forced to
// override the method unnecessarily

// D – Dependency Inversion Principle
// Mainly the program depends on the Accounts interface not on the subclasses
// (SavingAccounts,CurrentAcounts)
