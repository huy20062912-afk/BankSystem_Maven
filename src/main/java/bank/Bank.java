package bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho ngân hàng, quản lý danh sách khách hàng.
 */
public class Bank {
    // 1. Tích hợp Logging
    private static final Logger logger = LoggerFactory.getLogger(Bank.class);

    // 2. Sửa Checkstyle: Đổi tên biến c_list thành customerList (camelCase)
    private List<Customer> customerList;

    /**
     * Khởi tạo ngân hàng với danh sách khách hàng rỗng.
     */
    public Bank() {
        this.customerList = new ArrayList<>();
        logger.info("Khởi tạo hệ thống ngân hàng thành công.");
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    /**
     * Thiết lập danh sách khách hàng.
     *
     * @param customerList Danh sách khách hàng cần thiết lập.
     */
    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.customerList = new ArrayList<>();
            logger.warn("Cảnh báo: Danh sách khách hàng truyền vào bị null, đã khởi tạo list rỗng.");
        } else {
            this.customerList = customerList;
        }
    }

    /**
     * Đọc thông tin khách hàng và tài khoản từ luồng đầu vào (InputStream).
     *
     * @param inputStream Luồng đầu vào chứa dữ liệu định dạng text.
     */
    public void readCustomerList(InputStream inputStream) {
        logger.info("Bắt đầu đọc dữ liệu khách hàng từ InputStream...");

        if (inputStream == null) {
            logger.error("InputStream bị null. Dừng đọc dữ liệu.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            Customer currentCustomer = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // Bỏ qua dòng trống
                }

                // 3. Tái cấu trúc: Tách hàm con để giảm độ phức tạp (Cyclomatic Complexity)
                currentCustomer = parseLine(line, currentCustomer);
            }
            logger.info("Đọc dữ liệu hoàn tất. Tổng số khách hàng hiện tại: {}", customerList.size());

        } catch (IOException e) {
            // Sửa lỗi: Bắt đúng IOException thay vì Exception chung chung
            logger.error("Lỗi I/O khi đọc dữ liệu: {}", e.getMessage(), e);
        }
    }

    /**
     * Hàm phụ trợ (Helper method) để xử lý từng dòng văn bản.
     */
    private Customer parseLine(String line, Customer currentCustomer) {
        int lastSpaceIndex = line.lastIndexOf(' ');
        if (lastSpaceIndex <= 0) {
            logger.warn("Bỏ qua dòng dữ liệu không hợp lệ: {}", line);
            return currentCustomer;
        }

        String lastToken = line.substring(lastSpaceIndex + 1).trim();

        // CMMND thường có 9 hoặc 12 số, giả sử chuẩn định dạng ở đây là chuỗi số
        if (lastToken.matches("\\d+")) {
            // Đây là dòng tạo Khách hàng mới
            String name = line.substring(0, lastSpaceIndex).trim();
            long cccd = Long.parseLong(lastToken);
            Customer newCustomer = new Customer(cccd, name);

            customerList.add(newCustomer);
            logger.info("Đã thêm khách hàng mới: {} - CCCD: {}", name, cccd);

            return newCustomer;

        } else {
            // Đây là dòng thêm Tài khoản cho Khách hàng hiện tại
            if (currentCustomer != null) {
                processAccountLine(line, currentCustomer);
            } else {
                logger.warn("Phát hiện dữ liệu tài khoản nhưng chưa có khách hàng sở hữu: {}", line);
            }
            return currentCustomer;
        }
    }

    /**
     * Hàm phụ trợ (Helper method) để phân tích và tạo tài khoản.
     */
    private void processAccountLine(String line, Customer currentCustomer) {
        String[] parts = line.split("\\s+");
        if (parts.length >= 3) {
            try {
                long accountNumber = Long.parseLong(parts[0]);
                String accountType = parts[1];
                double balance = Double.parseDouble(parts[2]);

                if (Account.CHECKING_TYPE.equals(accountType)) {
                    currentCustomer.addAccount(new CheckingAccount(accountNumber, balance));
                    logger.debug("Thêm CheckingAccount {} cho khách hàng ID: {}", accountNumber, currentCustomer.getIdNumber());
                } else if (Account.SAVINGS_TYPE.equals(accountType)) {
                    currentCustomer.addAccount(new SavingsAccount(accountNumber, balance));
                    logger.debug("Thêm SavingsAccount {} cho khách hàng ID: {}", accountNumber, currentCustomer.getIdNumber());
                } else {
                    logger.warn("Loại tài khoản không được hỗ trợ: {}", accountType);
                }
            } catch (NumberFormatException e) {
                logger.error("Lỗi sai định dạng số ở dòng: {}", line);
            }
        }
    }

    /**
     * Trả về chuỗi thông tin khách hàng, sắp xếp theo số CCCD/ID tăng dần.
     */
    public String getCustomersInfoByIdOrder() {
        // Sửa lỗi: Dùng Lambda thay vì Anonymous Class dài dòng
        customerList.sort(Comparator.comparingLong(Customer::getIdNumber));

        logger.debug("Đã sắp xếp danh sách khách hàng theo ID.");
        return buildCustomerInfoString(customerList);
    }

    /**
     * Trả về chuỗi thông tin khách hàng, sắp xếp theo tên. Nếu trùng tên thì xếp theo ID.
     */
    public String getCustomersInfoByNameOrder() {
        List<Customer> copyList = new ArrayList<>(customerList);

        copyList.sort((c1, c2) -> {
            int nameCompare = c1.getFullName().compareTo(c2.getFullName());
            if (nameCompare != 0) {
                return nameCompare;
            }
            return Long.compare(c1.getIdNumber(), c2.getIdNumber());
        });

        logger.debug("Đã sắp xếp danh sách khách hàng theo Tên.");
        return buildCustomerInfoString(copyList);
    }

    /**
     * Hàm phụ trợ để xây dựng chuỗi (Giảm Duplicate Code).
     */
    private String buildCustomerInfoString(List<Customer> listToBuild) {
        // Sửa lỗi Performance: Dùng StringBuilder thay vì String +=
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < listToBuild.size(); i++) {
            builder.append(listToBuild.get(i).getCustomerInfo());
            if (i < listToBuild.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}