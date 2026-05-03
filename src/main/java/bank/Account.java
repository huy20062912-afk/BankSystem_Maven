package bank;

import java.util.ArrayList;
import java.util.List;

import bank.InvalidFundingAmountException;
import bank.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho tài khoản ngân hàng cơ bản.
 */
public abstract class Account {

    // Khởi tạo Logger cho class Account
    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    public static final String CHECKING_TYPE = "CHECKING";
    public static final String SAVINGS_TYPE = "SAVINGS";

    private long accountNumber;
    private double balance;
    protected List<Transaction> transactionList;

    /**
     * Khởi tạo tài khoản mới.
     */
    public Account(long accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionList = new ArrayList<>();
        // Ghi log mức INFO khi một tài khoản mới được tạo ra
        logger.info("Khởi tạo tài khoản {} với số dư ban đầu: {}", accountNumber, balance);
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        if (transactionList == null) {
            this.transactionList = new ArrayList<>();
        } else {
            this.transactionList = transactionList;
        }
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    protected void doDepositing(double amount) throws InvalidFundingAmountException {
        if (amount <= 0) {
            // Ghi log mức WARN vì đầu vào sai, nhưng không gây sập hệ thống
            logger.warn("Giao dịch nạp tiền thất bại do số tiền không hợp lệ: {}", amount);
            throw new InvalidFundingAmountException(amount);
        }
        balance += amount;
        // Ghi log mức INFO khi nạp tiền thành công
        logger.info("Nạp tiền thành công vào tài khoản {}. Số dư mới: {}", accountNumber, balance);
    }

    protected void doWithdrawing(double amount) throws InsufficientFundsException, InvalidFundingAmountException {
        if (amount <= 0) {
            logger.warn("Giao dịch rút tiền thất bại do số tiền không hợp lệ: {}", amount);
            throw new InvalidFundingAmountException(amount);
        }
        if (amount > balance) {
            // Ghi log mức ERROR vì đây là lỗi logic nghiệp vụ quan trọng (thiếu tiền)
            logger.error("Rút tiền thất bại! Tài khoản {} không đủ số dư. Cần: {}, Có: {}",
                    accountNumber, amount, balance);
            throw new InsufficientFundsException(amount);
        }
        balance -= amount;
        logger.info("Rút tiền thành công từ tài khoản {}. Số tiền: {}, Số dư mới: {}",
                accountNumber, amount, balance);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactionList.add(transaction);
        }
    }

    public String getTransactionHistory() {
        // Đã sửa lỗi Performance: Dùng StringBuilder thay vì String +=
        StringBuilder summary = new StringBuilder("Lịch sử giao dịch của tài khoản ")
                .append(accountNumber).append(":\n");

        for (int i = 0; i < transactionList.size(); i++) {
            summary.append(transactionList.get(i).getTransactionSummary());
            if (i < transactionList.size() - 1) {
                summary.append("\n");
            }
        }

        // Thay thế System.out.println bằng log mức DEBUG
        logger.debug("Đã truy xuất lịch sử giao dịch cho tài khoản: {}", accountNumber);

        return summary.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Account)) {
            return false;
        }
        Account other = (Account) obj;
        return this.accountNumber == other.accountNumber;
    }

    @Override
    public int hashCode() {
        return (int) (accountNumber ^ (accountNumber >>> 32));
    }
}