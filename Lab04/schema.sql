CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price REAL NOT NULL,
    brand TEXT NOT NULL,
    description TEXT NOT NULL,
    image_path TEXT NOT NULL
);

INSERT INTO products(name, price, brand, description, image_path) VALUES
('4DFWD PULSE SHOES', 160.00, 'Adidas',
 'Mẫu giày hiệu năng cao với cảm giác êm và thiết kế hiện đại.',
 'Lab03/img1.png'),
('FORUM MID SHOES', 100.00, 'Adidas',
 'Phong cách bóng rổ cổ điển, phối màu xanh - trắng nổi bật.',
 'Lab03/img2.png'),
('SUPERNOVA SHOES', 150.00, 'Adidas',
 'Đệm êm, phù hợp cho hoạt động hằng ngày và vận động nhẹ.',
 'Lab03/img3.png'),
('ADIDAS RUNNER', 160.00, 'Adidas',
 'Thiết kế nhẹ, đường nét gọn gàng với điểm nhấn trẻ trung.',
 'Lab03/img4.png'),
('NMD CITY STOCK 2', 120.00, 'Adidas',
 'Phong cách đô thị tối giản, cá tính và dễ phối đồ.',
 'Lab03/img5.png'),
('4DFWD PULSE ORANGE', 160.00, 'Adidas',
 'Gam cam nổi bật, phù hợp cho phong cách năng động.',
 'Lab03/img6.png');
