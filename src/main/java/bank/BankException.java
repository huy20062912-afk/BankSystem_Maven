/**
 * Ngoại lệ chung trong hệ thống ngân hàng.
 */
package bank;
public class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }
}
