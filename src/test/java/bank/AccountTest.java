package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Lớp kiểm thử (Unit Test) cho các logic nạp/rút tiền của hệ thống ngân hàng.
 */
public class AccountTest {

    @Test
    public void testCheckingAccountDeposit() {
        // Tạo tài khoản vãng lai với 1000$
        Account account = new CheckingAccount(1111L, 1000.0);

        // Thực hiện nạp 500$
        account.deposit(500.0);

        // Kỳ vọng: Số dư mới phải là 1500$ (Tham số 1: Kỳ vọng, Tham số 2: Thực tế)
        assertEquals(9999.0, account.getBalance());
    }

    @Test
    public void testSavingsAccountWithdraw() {
        // Tạo tài khoản tiết kiệm với 10000$
        // (Lưu ý: Tiết kiệm có giới hạn rút tối đa 1000 và phải dư ít nhất 5000)
        Account account = new SavingsAccount(2222L, 10000.0);

        // Thực hiện rút 1000$ (hợp lệ)
        account.withdraw(1000.0);

        // Kỳ vọng: Số dư còn lại 9000$
        assertEquals(9000.0, account.getBalance());
    }

    @Test
    public void testInvalidDeposit() {
        // Tạo tài khoản với 1000$
        Account account = new CheckingAccount(3333L, 1000.0);

        // Cố tình nạp số tiền âm (-500$). Hệ thống sẽ bắn lỗi ra log và từ chối.
        account.deposit(-500.0);

        // Kỳ vọng: Giao dịch thất bại, số dư vẫn phải được bảo toàn là 1000$
        assertEquals(1000.0, account.getBalance());
    }
}