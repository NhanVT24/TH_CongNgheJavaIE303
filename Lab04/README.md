# Lab04 - Quản lý sản phẩm với CSDL SQLite

Lab này phát triển từ Lab03: giao diện Swing vẫn hiển thị danh sách sản phẩm, nhưng dữ liệu được lấy từ và ghi vào CSDL SQLite thông qua JDBC.

## Chức năng

- Tự tạo file CSDL `Lab04/products.db` khi chương trình chạy lần đầu.
- Tạo bảng `products` nếu chưa tồn tại.
- Tự thêm dữ liệu mẫu khi bảng còn trống.
- Tìm sản phẩm theo tên, thương hiệu hoặc mô tả.
- Chọn một sản phẩm ở bên phải để sửa trực tiếp ở form bên trái.
- Xoá sản phẩm đang được chọn.
- Hiển thị giao diện và nội dung bằng tiếng Việt có dấu.

## Biên dịch

Chạy từ thư mục gốc của project:

```powershell
javac -cp ".;lib\sqlite-jdbc-3.53.2.0.jar;lib\slf4j-api-1.7.36.jar;lib\slf4j-simple-1.7.36.jar" Lab04\VoThanhNhan23521092.java
```

## Chạy chương trình

```powershell
java -cp ".;Lab04;lib\sqlite-jdbc-3.53.2.0.jar;lib\slf4j-api-1.7.36.jar;lib\slf4j-simple-1.7.36.jar" VoThanhNhan23521092
```

## Cách dùng

- Chọn một sản phẩm ở danh sách bên phải.
- Chỉnh dữ liệu ở form bên trái.
- Bấm **Lưu thay đổi** để cập nhật vào CSDL.
- Bấm **Xoá** để xoá sản phẩm đang chọn.
- Bấm **Làm mới** để nạp lại dữ liệu của sản phẩm đang chọn.
- Ô tìm kiếm ở phía trên bên phải cho phép lọc danh sách theo từ khoá.

## Lưu ý

- Đường dẫn ảnh mặc định đang trỏ tới các file trong `Lab03`.
- Nếu bạn đổi tên thư mục hoặc di chuyển file ảnh, hãy cập nhật lại ô **Đường dẫn ảnh** trước khi lưu thay đổi.
