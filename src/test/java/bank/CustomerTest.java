package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    @Test
    public void testCustomerInfo() {
        // Test khởi tạo và lấy thông tin cơ bản
        Customer customer = new Customer(123456789L, "Nguyen Van A");
        assertEquals(123456789L, customer.getIdNumber());
        assertEquals("Nguyen Van A", customer.getFullName());

        // Test đổi tên
        customer.setFullName("Le Van B");
        assertEquals("Le Van B", customer.getFullName());
    }

    @Test
    public void testAccountManagement() {
        Customer customer = new Customer(111222L, "Khach Hang Test");
        Account acc = new CheckingAccount(9999L, 5000.0);

        // Thêm tài khoản
        customer.addAccount(acc);

        // Lấy danh sách tài khoản ra và kiểm tra xem đã có 1 cái bên trong chưa
        assertEquals(1, customer.getAccountList().size());

        // Kiểm tra số dư của cái tài khoản đầu tiên (vị trí 0) trong danh sách
        assertEquals(5000.0, customer.getAccountList().get(0).getBalance());
    }
}