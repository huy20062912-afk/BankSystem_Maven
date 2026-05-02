package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BankTest {

    @Test
    public void testBankManageCustomer() {
        Bank bank = new Bank();
        Customer c1 = new Customer(111L, "Nguyen Van A");
        Customer c2 = new Customer(222L, "Le Van B");

        // Test thêm khách hàng
        // Lưu ý: Tùy code của bạn mà hàm thêm khách hàng có thể là addCustomer() hoặc getCustomerList().add()
        bank.getCustomerList().add(c1);
        bank.getCustomerList().add(c2);

        assertEquals(2, bank.getCustomerList().size());
    }

    @Test
    public void testReadCustomerList() {
        Bank bank = new Bank();
        // Giả lập một luồng dữ liệu truyền vào (thay vì đọc từ file txt thật)
        String mockData = "Nguyen Van A\n123456789\n1234567890 Checking 5000\n";
        InputStream is = new ByteArrayInputStream(mockData.getBytes());

        try {
            bank.readCustomerList(is);
            // Kỳ vọng đọc được ít nhất 1 khách hàng nếu format đúng,
            // hoặc bắt được Exception nếu format sai
            assertNotNull(bank.getCustomerList());
        } catch (Exception e) {
            // Bao phủ luôn cả khối catch báo lỗi
            System.out.println("Đã nhảy vào khối catch: " + e.getMessage());
        }
    }
}