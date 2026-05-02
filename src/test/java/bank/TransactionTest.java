package bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionTest {

    @Test
    public void testTransactionGettersAndSetters() {
        Transaction transaction = new Transaction(Transaction.TYPE_DEPOSIT_CHECKING, 100.0, 50.0, 150.0);

        // Test Getters
        assertEquals(Transaction.TYPE_DEPOSIT_CHECKING, transaction.getType());
        assertEquals(100.0, transaction.getAmount());
        assertEquals(50.0, transaction.getInitialBalance());
        assertEquals(150.0, transaction.getFinalBalance());

        // Test Setters
        transaction.setType(Transaction.TYPE_WITHDRAW_CHECKING);
        transaction.setAmount(200.0);
        transaction.setInitialBalance(300.0);
        transaction.setFinalBalance(100.0);

        assertEquals(Transaction.TYPE_WITHDRAW_CHECKING, transaction.getType());
        assertEquals(200.0, transaction.getAmount());
        assertEquals(300.0, transaction.getInitialBalance());
        assertEquals(100.0, transaction.getFinalBalance());
    }

    @Test
    public void testGetTypeString() {
        assertEquals("Nạp tiền vãng lai", Transaction.getTypeString(Transaction.TYPE_DEPOSIT_CHECKING));
        assertEquals("Rút tiền vãng lai", Transaction.getTypeString(Transaction.TYPE_WITHDRAW_CHECKING));
        assertEquals("Nạp tiền tiết kiệm", Transaction.getTypeString(Transaction.TYPE_DEPOSIT_SAVINGS));
        assertEquals("Rút tiền tiết kiệm", Transaction.getTypeString(Transaction.TYPE_WITHDRAW_SAVINGS));
        assertEquals("Không rõ", Transaction.getTypeString(999)); // Test case mặc định (default)
    }

    @Test
    public void testGetTransactionSummary() {
        Transaction transaction = new Transaction(Transaction.TYPE_DEPOSIT_CHECKING, 100.0, 50.0, 150.0);
        String summary = transaction.getTransactionSummary();

        // Kiểm tra xem chuỗi tóm tắt có chứa các thông tin quan trọng không
        assertTrue(summary.contains("Nạp tiền vãng lai"));
        assertTrue(summary.contains("50.00"));
        assertTrue(summary.contains("100.00"));
        assertTrue(summary.contains("150.00"));
    }
}