package bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho tài khoản tiết kiệm (Savings Account).
 * Lớp này thực thi các quy định về giới hạn nạp và rút tiền.
 */
public class SavingsAccount extends Account {

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);

    // Xóa "Magic Numbers" bằng cách định nghĩa hằng số rõ ràng
    private static final double MAX_WITHDRAW_AMOUNT = 1000.0;
    private static final double MIN_BALANCE = 5000.0;

    /**
     * Khởi tạo tài khoản tiết kiệm.
     *
     * @param accountNumber Số tài khoản.
     * @param balance       Số dư ban đầu.
     */
    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
        logger.debug("Khởi tạo tài khoản tiết kiệm {} với số dư: {}", accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        logger.debug("Bắt đầu xử lý giao dịch nạp tiền cho tài khoản tiết kiệm {}", getAccountNumber());
        double initialBalance = getBalance();
        try {
            doDepositing(amount);
            double finalBalance = getBalance();

            // Sửa Magic Number '3' thành hằng số của Transaction
            Transaction transaction = new Transaction(
                    Transaction.TYPE_DEPOSIT_SAVINGS,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(transaction);

            logger.info("Nạp tiền tiết kiệm thành công. Tài khoản: {}, Số tiền: +{}", getAccountNumber(), amount);
        } catch (BankException e) {
            // Sửa Exception chung chung thành BankException và log lỗi bằng ERROR
            logger.error("Lỗi nạp tiền tài khoản tiết kiệm {}: {}", getAccountNumber(), e.getMessage());
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        try {
            // Áp dụng hằng số thay cho 1000.0
            if (amount > MAX_WITHDRAW_AMOUNT) {
                logger.warn("Từ chối rút tiền: Số tiền {} vượt quá hạn mức tối đa {}", amount, MAX_WITHDRAW_AMOUNT);
                throw new InvalidFundingAmountException(amount);
            }

            // Áp dụng hằng số thay cho 5000.0
            if (initialBalance - amount < MIN_BALANCE) {
                logger.warn("Từ chối rút tiền: Số dư sau khi rút sẽ nhỏ hơn mức duy trì tối thiểu {}", MIN_BALANCE);
                throw new InsufficientFundsException(amount);
            }

            doWithdrawing(amount);
            double finalBalance = getBalance();

            // Sửa Magic Number '4' thành hằng số của Transaction
            Transaction transaction = new Transaction(
                    Transaction.TYPE_WITHDRAW_SAVINGS,
                    amount,
                    initialBalance,
                    finalBalance);
            addTransaction(transaction);

            logger.info("Rút tiền tiết kiệm thành công. Tài khoản: {}, Số tiền: -{}, Số dư còn lại: {}",
                    getAccountNumber(), amount, finalBalance);

        } catch (BankException e) {
            // Ghi rõ lỗi và thêm biến để dễ debug
            logger.error("Lỗi rút tiền tài khoản tiết kiệm {}: {}", getAccountNumber(), e.getMessage());
        }
    }
}