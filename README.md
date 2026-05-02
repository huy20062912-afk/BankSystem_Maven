readme_content = """# Báo cáo: Tái cấu trúc (Refactoring) và Khả năng quan sát (Observability) cho BankSystem

Dự án `BankSystem` ban đầu là mã nguồn nguyên thủy (Legacy code) chứa nhiều vi phạm về tiêu chuẩn thiết kế, Code Quality và thiếu khả năng giám sát. Dưới đây là chi tiết các bước giải pháp đã được áp dụng để nâng cấp dự án đạt chuẩn doanh nghiệp.

## 1. Nâng cấp Kiến trúc Dự án (Mavenization)
*   Chuyển đổi dự án sang quản lý bằng công cụ tự động hóa **Apache Maven** thông qua tệp `pom.xml`.
*   Tích hợp Maven Checkstyle Plugin (sử dụng bộ quy tắc Google Checks) vào quy trình build để kiểm soát mã nguồn chặt chẽ.
*   Thêm các thư viện cần thiết như `logback-classic` (cho Logging) và `junit-jupiter` (cho kiểm thử).

## 2. Quản lý Chất lượng Mã nguồn (Checkstyle Refactoring)
Toàn bộ mã nguồn đã được tái cấu trúc để vượt qua lệnh kiểm tra `mvn checkstyle:check` với các thay đổi chính:
*   **Quy tắc đặt tên (Naming Conventions):** Chuẩn hóa các biến không rõ nghĩa, sai quy tắc (như `_accNum`, `B`, `c_list`) thành chuẩn *camelCase* (`accountNumber`, `balance`, `customerList`).
*   **Loại bỏ Magic Numbers:** Thay thế các con số cứng nhắc (VD: `1000.0`, `5000.0`, `1`, `2`) bằng các hằng số tĩnh có ý nghĩa rõ ràng (VD: `MAX_WITHDRAW_AMOUNT`, `Transaction.TYPE_DEPOSIT_CHECKING`) để dễ bảo trì.
*   **Độ dài dòng code (Line Length):** Cắt ngắn các dòng code dài quá 100 ký tự (đặc biệt trong hàm tạo chuỗi lịch sử giao dịch) bằng cách ngắt dòng hoặc dùng `String.format()`.
*   **Bổ sung Javadoc:** Thêm tài liệu chú thích chuẩn cho toàn bộ class và các hàm `public`, giải thích rõ các tham số `@param` và `@return`.

## 3. Tối ưu Hiệu năng và Cấu trúc (Performance & Architecture)
*   **Thay thế String Concatenation:** Xóa bỏ các phép cộng chuỗi (`+=`) trong vòng lặp tại lớp `Account` và `Bank`. Thay thế bằng đối tượng `StringBuilder` để tối ưu hóa hiệu năng cấp phát bộ nhớ.
*   **Giảm độ phức tạp thuật toán (Cyclomatic Complexity):** Refactor phương thức `readCustomerList()` khổng lồ chứa nhiều lệnh `if-else` lồng nhau. Tách luồng logic ra thành các hàm phụ trợ nhỏ hơn (`parseLine`, `processAccountLine`) để code dễ đọc và dễ test.
*   **Xử lý Ngoại lệ (Exception Handling):** Bỏ việc bắt class cha `Exception` chung chung. Thay vào đó, bắt/ném các ngoại lệ nghiệp vụ cụ thể (`InsufficientFundsException`, `InvalidFundingAmountException`). Khắc phục lỗi "nuốt exception" ẩn dấu vết lỗi hệ thống.

## 4. Chiến lược Logging (Khả năng quan sát)
Dự án đã loại bỏ hoàn toàn các lệnh `System.out.println` (do nhược điểm chặn I/O và thiếu thông tin luồng) và thay thế bằng thư viện **SLF4J kết hợp Logback**. Cú pháp Parameterized Logging (`{}`) được sử dụng để tối ưu hiệu năng xử lý chuỗi.

Chiến lược lựa chọn các cấp độ Log (Log Levels) và điểm dữ liệu ghi nhận như sau:
*   **Cấp độ `INFO` (Thông tin vận hành bình thường):**
    *   *Mục đích:* Theo dõi (Auditing) các luồng nghiệp vụ chính và trạng thái thành công của hệ thống ngân hàng.
    *   *Điểm dữ liệu:* Ghi nhận khi khởi tạo tài khoản mới, ID tài khoản, loại giao dịch (nạp/rút), số tiền giao dịch và số dư mới sau khi thành công.
*   **Cấp độ `WARN` (Cảnh báo):**
    *   *Mục đích:* Cảnh báo rủi ro hoặc dữ liệu đầu vào không hợp lệ nhưng hệ thống vẫn tiếp tục hoạt động được.
    *   *Điểm dữ liệu:* Nhập số tiền nạp âm (<= 0), hoặc cố tình rút tiền vượt hạn mức tối đa quy định của tài khoản tiết kiệm.
*   **Cấp độ `ERROR` (Lỗi nghiêm trọng):**
    *   *Mục đích:* Ghi lại các ngoại lệ nghiệp vụ quan trọng hoặc sự cố hệ thống cần được xử lý.
    *   *Điểm dữ liệu:* Giao dịch bị từ chối do không đủ số dư (kèm thông tin tài khoản, số dư hiện tại, số tiền định rút), lỗi I/O khi đọc dữ liệu khách hàng từ file.
*   **Cấp độ `DEBUG` / `TRACE` (Gỡ lỗi hệ thống):**
    *   *Mục đích:* Phục vụ riêng cho lập trình viên theo dõi luồng tính toán trong môi trường phát triển.
    *   *Điểm dữ liệu:* Quá trình phân tích (parse) từng dòng text, thông báo xác nhận đã sắp xếp xong danh sách. Mức log này sẽ bị ẩn đi khi dự án chạy trên môi trường thật (Production).
        """

with open("README.md", "w", encoding="utf-8") as f:
f.write(readme_content)