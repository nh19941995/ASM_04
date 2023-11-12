package src.Models;

public interface ITransfer {
    void transfer(Account receiveAccount, double amount,boolean status);
    void isAcceptedTransfer(Account receiveAccount, double amount);

}
