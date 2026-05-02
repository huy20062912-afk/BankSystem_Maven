package bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho tài khoản vãng lai (Checking Account).
 */
public class CheckingAccount extends Account {

    // 1. Thêm Logger cho class
    private static final Logger logger = LoggerFactory.getLogger(CheckingAccount.class);

    /**
     * Khởi tạo tài khoản vãng lai.
     *
     * @param accountNumber Số tài khoản
     * @param balance       Số dư ban đầu
     */
    public CheckingAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        double initialBalance = getBalance();
        try {
            doDepositing(amount);
            double finalBalance = getBalance();
            Transaction t = new Transaction(
                    Transaction.TYPE_DEPOSIT_CHECKING,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(t);
        } catch (BankException e) {
            // 2. Thay thế System.out.println bằng log mức ERROR
            logger.error("Giao dịch nạp tiền thất bại trên tài khoản vãng lai {}: {}",
                    getAccountNumber(), e.getMessage());
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        try {
            doWithdrawing(amount);
            double finalBalance = getBalance();
            Transaction t = new Transaction(
                    Transaction.TYPE_WITHDRAW_CHECKING,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(t);
        } catch (BankException e) {
            // 2. Thay thế System.out.println bằng log mức ERROR
            logger.error("Giao dịch rút tiền thất bại trên tài khoản vãng lai {}: {}",
                    getAccountNumber(), e.getMessage());
        }
    }
}