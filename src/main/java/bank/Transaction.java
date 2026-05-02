package bank;

import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho một giao dịch ngân hàng.
 */
public class Transaction {

    // 1. Thêm Logger
    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);

    public static final int TYPE_DEPOSIT_CHECKING = 1;
    public static final int TYPE_WITHDRAW_CHECKING = 2;
    public static final int TYPE_DEPOSIT_SAVINGS = 3;
    public static final int TYPE_WITHDRAW_SAVINGS = 4;

    private int type;
    private double amount;
    private double initialBalance;
    private double finalBalance;

    /**
     * Khởi tạo một giao dịch mới.
     *
     * @param type           Loại giao dịch.
     * @param amount         Số tiền giao dịch.
     * @param initialBalance Số dư ban đầu.
     * @param finalBalance   Số dư sau khi giao dịch.
     */
    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(double finalBalance) {
        this.finalBalance = finalBalance;
    }

    /**
     * Trả về chuỗi mô tả cho từng loại giao dịch.
     *
     * @param transactionType Mã loại giao dịch.
     * @return Chuỗi mô tả tên giao dịch.
     */
    // 2. Sửa lỗi Checkstyle: Đổi tên thành getTypeString (camelCase) và biến rõ nghĩa
    public static String getTypeString(int transactionType) {
        switch (transactionType) {
            // 3. Xóa Magic Numbers: Thay bằng các hằng số đã định nghĩa
            case TYPE_DEPOSIT_CHECKING:
                return "Nạp tiền vãng lai";
            case TYPE_WITHDRAW_CHECKING:
                return "Rút tiền vãng lai";
            case TYPE_DEPOSIT_SAVINGS:
                return "Nạp tiền tiết kiệm";
            case TYPE_WITHDRAW_SAVINGS:
                return "Rút tiền tiết kiệm";
            default:
                return "Không rõ";
        }
    }

    /**
     * Lấy chuỗi tóm tắt thông tin của giao dịch hiện tại.
     *
     * @return Chuỗi định dạng chứa chi tiết giao dịch.
     */
    public String getTransactionSummary() {
        // 4. Thay System.out.println bằng logger.debug
        logger.debug("Đang tạo chuỗi tóm tắt cho giao dịch loại: {}", this.type);

        // 5. Cứu rỗi Line Length và Performance: Dùng String.format gom chung lại và ngắt dòng
        return String.format(Locale.US,
                "- Kiểu giao dịch: %s. Số dư ban đầu: $%.2f. Số tiền: $%.2f. Số dư cuối: $%.2f.",
                getTypeString(type), initialBalance, amount, finalBalance);
    }
}