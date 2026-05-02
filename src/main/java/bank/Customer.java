package bank;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp Customer đại diện cho một khách hàng trong hệ thống ngân hàng.
 */
public class Customer {

    // 1. Thêm Logger
    private static final Logger logger = LoggerFactory.getLogger(Customer.class);

    private long idNumber;
    private String fullName;
    private List<Account> accountList;

    /**
     * Khởi tạo khách hàng không tham số (phục vụ MyTest).
     */
    public Customer() {
        this(0L, "");
    }

    /**
     * Khởi tạo khách hàng với đầy đủ thông tin.
     *
     * @param idNumber Số CMND/CCCD của khách hàng.
     * @param fullName Họ và tên đầy đủ của khách hàng.
     */
    public Customer(long idNumber, String fullName) {
        this.idNumber = idNumber;
        this.fullName = fullName;
        // Dùng Diamond operator <> thay cho <Account>
        this.accountList = new ArrayList<>();

        // Ghi log khởi tạo thành công
        logger.debug("Khởi tạo khách hàng thành công: {} - ID: {}", fullName, idNumber);
    }

    /**
     * Lấy số CMND/CCCD.
     *
     * @return Số định danh của khách hàng.
     */
    public long getIdNumber() {
        return idNumber;
    }

    /**
     * Thiết lập số CMND/CCCD.
     *
     * @param idNumber Số định danh mới.
     */
    public void setIdNumber(long idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * Lấy họ tên khách hàng.
     *
     * @return Họ và tên khách hàng.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Thiết lập họ tên khách hàng.
     *
     * @param fullName Họ và tên mới.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Lấy danh sách tài khoản của khách hàng.
     *
     * @return Danh sách tài khoản.
     */
    public List<Account> getAccountList() {
        return accountList;
    }

    /**
     * Thiết lập danh sách tài khoản.
     *
     * @param accountList Danh sách tài khoản mới.
     */
    public void setAccountList(List<Account> accountList) {
        if (accountList == null) {
            this.accountList = new ArrayList<>();
        } else {
            this.accountList = accountList;
        }
    }

    /**
     * Thêm tài khoản mới cho khách hàng.
     *
     * @param account Đối tượng tài khoản cần thêm.
     */
    public void addAccount(Account account) {
        if (account == null) {
            logger.warn("Cố gắng thêm tài khoản null vào khách hàng {}", idNumber);
            return;
        }
        if (!accountList.contains(account)) {
            accountList.add(account);
            logger.info("Đã thêm tài khoản {} cho khách hàng {}", account.getAccountNumber(), idNumber);
        } else {
            logger.debug("Tài khoản {} đã tồn tại trong danh sách của khách hàng {}", account.getAccountNumber(), idNumber);
        }
    }

    /**
     * Xóa tài khoản khỏi danh sách của khách hàng.
     *
     * @param account Đối tượng tài khoản cần xóa.
     */
    public void removeAccount(Account account) {
        if (account == null) {
            return;
        }
        if (accountList.remove(account)) {
            logger.info("Đã xóa tài khoản {} khỏi khách hàng {}", account.getAccountNumber(), idNumber);
        }
    }

    /**
     * Lấy thông tin khách hàng dưới dạng chuỗi văn bản.
     *
     * @return Chuỗi chứa thông tin CMND và Họ tên.
     */
    public String getCustomerInfo() {
        return "Số CMND: " + idNumber + ". Họ tên: " + fullName + ".";
    }
}