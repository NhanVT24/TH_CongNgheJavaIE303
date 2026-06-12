import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class VoThanhNhan23521092 extends JFrame {
    private final ProductDatabase database = new ProductDatabase();
    private final List<Product> products = new ArrayList<>();
    private final List<ProductCard> productCards = new ArrayList<>();

    private final HeroPanel heroPanel = new HeroPanel();
    private final JLabel titleLabel = new JLabel();
    private final JLabel priceLabel = new JLabel();
    private final JLabel brandLabel = new JLabel();
    private final JLabel descLabel = new JLabel();
    private final JLabel statusLabel = new JLabel();

    private final JTextField searchField = new JTextField(22);
    private final JTextField nameField = new JTextField(18);
    private final JTextField priceField = new JTextField(18);
    private final JTextField brandField = new JTextField(18);
    private final JTextField imageField = new JTextField(18);
    private final JTextArea descriptionArea = new JTextArea(4, 18);

    private final JPanel grid = new JPanel(new GridLayout(0, 3, 14, 14));
    private String currentKeyword = "";
    private Product selectedProduct;

    public VoThanhNhan23521092() {
        initFrame();
        initUi();
        initDatabase();
    }

    private void initFrame() {
        setTitle("Lab04 - Quản lý sản phẩm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 720);
        setMinimumSize(new Dimension(1180, 660));
        setLocationRelativeTo(null);
        setContentPane(new BackgroundPanel());
    }

    private void initUi() {
        JPanel root = (JPanel) getContentPane();
        root.setLayout(new BorderLayout(24, 24));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));

        root.add(createHeroSection(), BorderLayout.WEST);
        root.add(createListSection(), BorderLayout.CENTER);
    }

    private void initDatabase() {
        try {
            database.connect();
            database.createTable();
            database.seedProducts();
            loadProducts("");
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private JPanel createHeroSection() {
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(360, 0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel sectionTitle = new JLabel("Thông tin sản phẩm");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        sectionTitle.setForeground(new Color(31, 48, 74));
        sectionTitle.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sectionSubTitle = new JLabel("Chọn một sản phẩm ở bên phải để sửa, xoá hoặc tìm kiếm");
        sectionSubTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sectionSubTitle.setForeground(new Color(87, 98, 115));
        sectionSubTitle.setAlignmentX(LEFT_ALIGNMENT);

        heroPanel.setOpaque(false);
        heroPanel.setPreferredSize(new Dimension(300, 240));
        heroPanel.setMaximumSize(new Dimension(300, 240));
        heroPanel.setAlignmentX(LEFT_ALIGNMENT);

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(37, 45, 59));
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        priceLabel.setForeground(new Color(40, 108, 198));
        priceLabel.setAlignmentX(LEFT_ALIGNMENT);

        brandLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        brandLabel.setForeground(new Color(84, 92, 107));
        brandLabel.setAlignmentX(LEFT_ALIGNMENT);

        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descLabel.setForeground(new Color(108, 116, 130));
        descLabel.setAlignmentX(LEFT_ALIGNMENT);

        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(97, 105, 119));
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel formCard = createFormSection();
        formCard.setAlignmentX(LEFT_ALIGNMENT);

        left.add(sectionTitle);
        left.add(Box.createVerticalStrut(4));
        left.add(sectionSubTitle);
        left.add(Box.createVerticalStrut(14));
        left.add(heroPanel);
        left.add(Box.createVerticalStrut(12));
        left.add(makeSeparator());
        left.add(Box.createVerticalStrut(12));
        left.add(titleLabel);
        left.add(Box.createVerticalStrut(10));
        left.add(priceLabel);
        left.add(Box.createVerticalStrut(6));
        left.add(brandLabel);
        left.add(Box.createVerticalStrut(8));
        left.add(descLabel);
        left.add(Box.createVerticalStrut(8));
        left.add(statusLabel);
        left.add(Box.createVerticalStrut(16));
        left.add(formCard);
        left.add(Box.createVerticalGlue());

        return left;
    }

    private JPanel createFormSection() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(true);
        card.setBackground(new Color(255, 255, 255, 240));
        card.setBorder(new CompoundRoundedBorder());
        card.setMaximumSize(new Dimension(340, 330));
        card.setPreferredSize(new Dimension(340, 330));

        JLabel formTitle = new JLabel("Sửa sản phẩm");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        formTitle.setForeground(new Color(38, 49, 68));

        styleTextField(nameField, "Tên sản phẩm");
        styleTextField(priceField, "Giá");
        styleTextField(brandField, "Thương hiệu");
        styleTextField(imageField, "Đường dẫn ảnh");

        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setRows(4);
        descriptionArea.setBorder(new LineBorder(new Color(210, 219, 231), 1, true));
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setPreferredSize(new Dimension(260, 84));
        descriptionScroll.setBorder(new LineBorder(new Color(210, 219, 231), 1, true));

        JButton saveButton = createAccentButton("Lưu thay đổi", new Color(40, 108, 198), Color.WHITE);
        JButton deleteButton = createAccentButton("Xoá", new Color(208, 75, 90), Color.WHITE);
        JButton reloadButton = createAccentButton("Làm mới", new Color(241, 244, 248), new Color(54, 64, 83));

        saveButton.addActionListener(e -> updateSelectedProductFromForm());
        deleteButton.addActionListener(e -> deleteSelectedProduct());
        reloadButton.addActionListener(e -> reloadSelectedProduct());

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(saveButton);
        buttonRow.add(deleteButton);
        buttonRow.add(reloadButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.anchor = GridBagConstraints.WEST;

        card.add(formTitle, gbc);

        gbc.gridy++;
        card.add(makeSmallLabel("Tên"), gbc);
        gbc.gridy++;
        card.add(nameField, gbc);
        gbc.gridy++;
        card.add(makeSmallLabel("Giá"), gbc);
        gbc.gridy++;
        card.add(priceField, gbc);
        gbc.gridy++;
        card.add(makeSmallLabel("Thương hiệu"), gbc);
        gbc.gridy++;
        card.add(brandField, gbc);
        gbc.gridy++;
        card.add(makeSmallLabel("Mô tả"), gbc);
        gbc.gridy++;
        card.add(descriptionScroll, gbc);
        gbc.gridy++;
        card.add(makeSmallLabel("Ảnh"), gbc);
        gbc.gridy++;
        card.add(imageField, gbc);
        gbc.gridy++;
        card.add(buttonRow, gbc);

        return card;
    }

    private JPanel createListSection() {
        JPanel section = new JPanel(new BorderLayout(0, 12));
        section.setOpaque(false);

        JPanel headerCard = new JPanel(new BorderLayout(12, 10));
        headerCard.setOpaque(true);
        headerCard.setBackground(new Color(255, 255, 255, 240));
        headerCard.setBorder(new CompoundRoundedBorder());

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setOpaque(false);

        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(280, 36));
        searchField.setBorder(new LineBorder(new Color(210, 219, 231), 1, true));

        JButton searchButton = createAccentButton("Tìm kiếm", new Color(40, 108, 198), Color.WHITE);
        JButton allButton = createAccentButton("Tất cả", new Color(241, 244, 248), new Color(54, 64, 83));

        searchButton.addActionListener(e -> {
            currentKeyword = searchField.getText().trim();
            loadProducts(currentKeyword);
        });
        searchField.addActionListener(e -> {
            currentKeyword = searchField.getText().trim();
            loadProducts(currentKeyword);
        });
        allButton.addActionListener(e -> {
            searchField.setText("");
            currentKeyword = "";
            loadProducts("");
        });

        toolbar.add(makeSmallLabel("Tìm sản phẩm"));
        toolbar.add(searchField);
        toolbar.add(searchButton);
        toolbar.add(allButton);

        JLabel listTitle = new JLabel("Danh sách sản phẩm trong CSDL");
        listTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        listTitle.setForeground(new Color(31, 48, 74));
        listTitle.setBorder(new EmptyBorder(12, 16, 0, 16));

        JLabel listSubtitle = new JLabel("Bấm vào một thẻ để nạp dữ liệu sang form bên trái");
        listSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        listSubtitle.setForeground(new Color(88, 98, 116));
        listSubtitle.setBorder(new EmptyBorder(0, 16, 0, 16));

        headerCard.add(toolbar, BorderLayout.NORTH);
        headerCard.add(listTitle, BorderLayout.CENTER);
        headerCard.add(listSubtitle, BorderLayout.SOUTH);

        grid.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));

        section.add(headerCard, BorderLayout.NORTH);
        section.add(scrollPane, BorderLayout.CENTER);
        return section;
    }

    private void loadProducts(String keyword) {
        if (!database.isConnected()) {
            statusLabel.setText("Chưa kết nối được CSDL. Hãy thêm sqlite-jdbc.jar vào classpath.");
            return;
        }

        try {
            products.clear();
            products.addAll(database.findProducts(keyword));
            renderProducts();
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void renderProducts() {
        productCards.clear();
        grid.removeAll();

        for (Product product : products) {
            ProductCard card = new ProductCard(product);
            productCards.add(card);
            grid.add(card);
        }

        grid.revalidate();
        grid.repaint();

        if (products.isEmpty()) {
            selectedProduct = null;
            clearDetails();
            statusLabel.setText("Không tìm thấy sản phẩm nào phù hợp.");
            updateCardSelection(null);
            return;
        }

        Product toSelect = products.get(0);
        if (selectedProduct != null) {
            for (Product product : products) {
                if (product.id == selectedProduct.id) {
                    toSelect = product;
                    break;
                }
            }
        }

        selectProduct(toSelect);
        statusLabel.setText("Đã tải " + products.size() + " sản phẩm từ CSDL.");
    }

    private void selectProduct(Product product) {
        selectedProduct = product;
        titleLabel.setText(product.name);
        priceLabel.setText("Giá: " + formatPrice(product.price));
        brandLabel.setText("Thương hiệu: " + product.brand);
        descLabel.setText("<html><body style='width:250px; line-height:1.4;'>" + product.description + "</body></html>");
        heroPanel.animateTo(product);
        populateFormFromProduct(product);
        updateCardSelection(product.id);
    }

    private void updateCardSelection(Integer productId) {
        for (ProductCard card : productCards) {
            card.setSelected(productId != null && card.product.id == productId);
        }
    }

    private void populateFormFromProduct(Product product) {
        nameField.setText(product.name);
        priceField.setText(String.format(Locale.US, "%.2f", product.price));
        brandField.setText(product.brand);
        descriptionArea.setText(product.description);
        imageField.setText(product.imagePath);
    }

    private void clearDetails() {
        titleLabel.setText("Chưa chọn sản phẩm");
        priceLabel.setText("Giá: --");
        brandLabel.setText("Thương hiệu: --");
        descLabel.setText("<html><body style='width:250px; line-height:1.4;'>Hãy chọn một sản phẩm ở bên phải.</body></html>");
        heroPanel.clear();
    }

    private void reloadSelectedProduct() {
        if (selectedProduct == null) {
            clearFormFields();
            clearDetails();
            statusLabel.setText("Chưa có sản phẩm được chọn.");
            return;
        }

        populateFormFromProduct(selectedProduct);
        selectProduct(selectedProduct);
        statusLabel.setText("Đã khôi phục dữ liệu của sản phẩm đang chọn.");
    }

    private void clearFormFields() {
        nameField.setText("");
        priceField.setText("");
        brandField.setText("");
        descriptionArea.setText("");
        imageField.setText("");
    }

    private void updateSelectedProductFromForm() {
        if (!database.isConnected()) {
            statusLabel.setText("Chưa kết nối được CSDL.");
            return;
        }

        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this,
                    "Hãy chọn một sản phẩm ở bên phải trước khi lưu thay đổi.",
                    "Chưa chọn sản phẩm", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String brand = brandField.getText().trim();
        String description = descriptionArea.getText().trim();
        String imagePath = imageField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || brand.isEmpty() || description.isEmpty() || imagePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin sản phẩm.", "Thiếu dữ liệu",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá phải là một con số hợp lệ.", "Sai dữ liệu",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = selectedProduct.id;
            database.updateProduct(id, name, price, brand, description, imagePath);
            currentKeyword = searchField.getText().trim();
            loadProducts(currentKeyword);

            Product updated = database.findProductById(id);
            if (updated != null) {
                selectProduct(updated);
            }

            statusLabel.setText("Đã lưu thay đổi cho: " + name);
        } catch (Exception ex) {
            showDatabaseError(ex);
        }
    }

    private void deleteSelectedProduct() {
        if (!database.isConnected()) {
            statusLabel.setText("Chưa kết nối được CSDL.");
            return;
        }

        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sản phẩm trước khi xoá.", "Chưa chọn sản phẩm",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xoá sản phẩm \"" + selectedProduct.name + "\" không?",
                "Xác nhận xoá", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            database.deleteProduct(selectedProduct.id);
            statusLabel.setText("Đã xoá sản phẩm: " + selectedProduct.name);
            selectedProduct = null;
            loadProducts(currentKeyword);
            if (!products.isEmpty()) {
                selectProduct(products.get(0));
            } else {
                clearDetails();
                clearFormFields();
            }
        } catch (SQLException ex) {
            showDatabaseError(ex);
        }
    }

    private void showDatabaseError(Exception ex) {
        String message = "Không thể kết nối CSDL SQLite.\n\n"
                + "Hãy thêm `sqlite-jdbc.jar` vào classpath khi chạy Lab04.\n"
                + "Ví dụ:\n"
                + "java -cp \".;sqlite-jdbc.jar;Lab04\" VoThanhNhan23521092\n\n"
                + "Lỗi: " + ex.getMessage();
        JOptionPane.showMessageDialog(this, message, "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Chưa kết nối được CSDL.");
    }

    private String formatPrice(double price) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
    }

    private static void styleTextField(JTextField field, String tooltip) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(new LineBorder(new Color(210, 219, 231), 1, true));
        field.setMaximumSize(new Dimension(320, 34));
        field.setPreferredSize(new Dimension(320, 34));
        field.setToolTipText(tooltip);
    }

    private static JLabel makeSmallLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(new Color(78, 86, 102));
        return label;
    }

    private static JButton createAccentButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setUI(new BasicButtonUI());
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(new EmptyBorder(8, 14, 8, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        return button;
    }

    private static JPanel makeSeparator() {
        JPanel line = new JPanel();
        line.setOpaque(false);
        line.setMaximumSize(new Dimension(360, 1));
        line.setPreferredSize(new Dimension(360, 1));
        line.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(196, 205, 219)));
        return line;
    }

    private static ImageIcon loadScaledIcon(String path, int width, int height) {
        File file = new File(path);
        if (!file.exists()) {
            return new ImageIcon(new java.awt.image.BufferedImage(width, height,
                    java.awt.image.BufferedImage.TYPE_INT_ARGB));
        }

        Image image = new ImageIcon(path).getImage();
        Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static String clip(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private static class ProductDatabase {
        private static final String DB_URL = "jdbc:sqlite:Lab04/products.db";
        private Connection connection;

        private boolean isConnected() {
            return connection != null;
        }

        private void connect() throws SQLException, ClassNotFoundException {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
        }

        private void createTable() throws SQLException {
            String sql = "CREATE TABLE IF NOT EXISTS products ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "price REAL NOT NULL,"
                    + "brand TEXT NOT NULL,"
                    + "description TEXT NOT NULL,"
                    + "image_path TEXT NOT NULL"
                    + ")";

            try (Statement statement = connection.createStatement()) {
                statement.execute(sql);
            }
        }

        private void seedProducts() throws SQLException {
            upsertSeedProduct("4DFWD PULSE SHOES", 160.00, "Adidas",
                    "Mẫu giày hiệu năng cao với cảm giác êm và thiết kế hiện đại.",
                    "Lab03/img1.png");
            upsertSeedProduct("FORUM MID SHOES", 100.00, "Adidas",
                    "Phong cách bóng rổ cổ điển, phối màu xanh - trắng nổi bật.",
                    "Lab03/img2.png");
            upsertSeedProduct("SUPERNOVA SHOES", 150.00, "Adidas",
                    "Đệm êm, phù hợp cho hoạt động hằng ngày và vận động nhẹ.",
                    "Lab03/img3.png");
            upsertSeedProduct("ADIDAS RUNNER", 160.00, "Adidas",
                    "Thiết kế nhẹ, đường nét gọn gàng với điểm nhấn trẻ trung.",
                    "Lab03/img4.png");
            upsertSeedProduct("NMD CITY STOCK 2", 120.00, "Adidas",
                    "Phong cách đô thị tối giản, cá tính và dễ phối đồ.",
                    "Lab03/img5.png");
            upsertSeedProduct("4DFWD PULSE ORANGE", 160.00, "Adidas",
                    "Gam cam nổi bật, phù hợp cho phong cách năng động.",
                    "Lab03/img6.png");
        }

        private void upsertSeedProduct(String name, double price, String brand, String description, String imagePath)
                throws SQLException {
            String updateSql = "UPDATE products SET price = ?, brand = ?, description = ?, image_path = ? WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                statement.setDouble(1, price);
                statement.setString(2, brand);
                statement.setString(3, description);
                statement.setString(4, imagePath);
                statement.setString(5, name);

                if (statement.executeUpdate() > 0) {
                    return;
                }
            }

            insertProduct(name, price, brand, description, imagePath);
        }

        private int insertProduct(String name, double price, String brand, String description, String imagePath)
                throws SQLException {
            String sql = "INSERT INTO products(name, price, brand, description, image_path) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, name);
                statement.setDouble(2, price);
                statement.setString(3, brand);
                statement.setString(4, description);
                statement.setString(5, imagePath);
                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
            return -1;
        }

        private void updateProduct(int id, String name, double price, String brand, String description,
                String imagePath) throws SQLException {
            String sql = "UPDATE products SET name = ?, price = ?, brand = ?, description = ?, image_path = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setDouble(2, price);
                statement.setString(3, brand);
                statement.setString(4, description);
                statement.setString(5, imagePath);
                statement.setInt(6, id);
                statement.executeUpdate();
            }
        }

        private void deleteProduct(int id) throws SQLException {
            String sql = "DELETE FROM products WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        }

        private Product findProductById(int id) throws SQLException {
            String sql = "SELECT id, name, price, brand, description, image_path FROM products WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new Product(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getDouble("price"),
                                resultSet.getString("brand"),
                                resultSet.getString("description"),
                                resultSet.getString("image_path"));
                    }
                }
            }
            return null;
        }

        private List<Product> findProducts(String keyword) throws SQLException {
            List<Product> result = new ArrayList<>();
            String trimmed = keyword == null ? "" : keyword.trim();
            String sql;

            if (trimmed.isEmpty()) {
                sql = "SELECT id, name, price, brand, description, image_path FROM products ORDER BY id";
            } else {
                sql = "SELECT id, name, price, brand, description, image_path "
                        + "FROM products "
                        + "WHERE name LIKE ? OR brand LIKE ? OR description LIKE ? "
                        + "ORDER BY id";
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (!trimmed.isEmpty()) {
                    String value = "%" + trimmed + "%";
                    statement.setString(1, value);
                    statement.setString(2, value);
                    statement.setString(3, value);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        result.add(new Product(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getDouble("price"),
                                resultSet.getString("brand"),
                                resultSet.getString("description"),
                                resultSet.getString("image_path")));
                    }
                }
            }
            return result;
        }
    }

    private static class Product {
        private final int id;
        private final String name;
        private final double price;
        private final String brand;
        private final String description;
        private final String imagePath;

        private Product(int id, String name, double price, String brand, String description, String imagePath) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.brand = brand;
            this.description = description;
            this.imagePath = imagePath;
        }
    }

    private class ProductCard extends JPanel {
        private final Product product;
        private boolean selected;

        private ProductCard(Product product) {
            this.product = product;
            setOpaque(false);
            setPreferredSize(new Dimension(230, 250));
            setLayout(new BorderLayout(0, 8));
            setBorder(new EmptyBorder(12, 12, 12, 12));

            JPanel textPanel = new JPanel();
            textPanel.setOpaque(false);
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

            JLabel title = new JLabel(clip(product.name, 22));
            title.setFont(new Font("SansSerif", Font.BOLD, 15));
            title.setForeground(new Color(42, 52, 68));

            JLabel subtitle = new JLabel(clip(product.description, 42));
            subtitle.setFont(new Font("SansSerif", Font.PLAIN, 11));
            subtitle.setForeground(new Color(134, 142, 155));

            textPanel.add(title);
            textPanel.add(Box.createVerticalStrut(6));
            textPanel.add(subtitle);

            JLabel image = new JLabel();
            image.setHorizontalAlignment(SwingConstants.CENTER);
            image.setIcon(loadScaledIcon(product.imagePath, 160, 95));

            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setOpaque(false);

            JLabel brand = new JLabel(product.brand);
            brand.setFont(new Font("SansSerif", Font.PLAIN, 12));
            brand.setForeground(new Color(90, 98, 112));

            JLabel price = new JLabel(formatPrice(product.price));
            price.setFont(new Font("SansSerif", Font.BOLD, 15));
            price.setForeground(new Color(40, 108, 198));

            bottom.add(brand, BorderLayout.WEST);
            bottom.add(price, BorderLayout.EAST);

            add(textPanel, BorderLayout.NORTH);
            add(image, BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);

            MouseAdapter clickHandler = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectProduct(product);
                }
            };

            addMouseListener(clickHandler);
            textPanel.addMouseListener(clickHandler);
            image.addMouseListener(clickHandler);
            bottom.addMouseListener(clickHandler);
        }

        private void setSelected(boolean selected) {
            this.selected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(255, 255, 255, 230));
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 22, 22));

            if (selected) {
                g2.setColor(new Color(40, 108, 198));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 3, getHeight() - 3, 20, 20));
            } else {
                g2.setColor(new Color(219, 227, 237));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 3, getHeight() - 3, 20, 20));
            }

            g2.dispose();
        }
    }

    private static class HeroPanel extends JPanel {
        private ImageIcon currentIcon;
        private ImageIcon nextIcon;
        private float alpha = 1f;
        private Timer timer;

        private void animateTo(Product product) {
            nextIcon = loadScaledIcon(product.imagePath, 260, 160);

            if (currentIcon == null) {
                currentIcon = nextIcon;
                repaint();
                return;
            }

            if (timer != null && timer.isRunning()) {
                timer.stop();
            }

            alpha = 0f;
            timer = new Timer(20, e -> {
                alpha += 0.09f;
                if (alpha >= 1f) {
                    alpha = 1f;
                    currentIcon = nextIcon;
                    timer.stop();
                }
                repaint();
            });
            timer.start();
        }

        private void clear() {
            currentIcon = null;
            nextIcon = null;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(255, 255, 255, 180));
            g2.fill(new RoundRectangle2D.Double(10, 12, getWidth() - 20, getHeight() - 24, 28, 28));

            g2.setColor(new Color(40, 108, 198, 25));
            g2.fillOval(42, 150, Math.max(140, getWidth() - 84), 28);

            if (currentIcon != null) {
                g2.drawImage(currentIcon.getImage(), 18, 36, null);
            }

            if (nextIcon != null && alpha < 1f) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.drawImage(nextIcon.getImage(), 18, 36, null);
            }

            g2.setColor(new Color(222, 229, 239));
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Double(10, 12, getWidth() - 20, getHeight() - 24, 28, 28));

            g2.dispose();
        }
    }

    private static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setPaint(new GradientPaint(0, 0, new Color(248, 251, 255),
                    getWidth(), getHeight(), new Color(231, 238, 248)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(new Color(255, 255, 255, 70));
            g2.fillOval(-80, -40, 260, 260);
            g2.fillOval(getWidth() - 180, getHeight() - 180, 240, 240);
            g2.dispose();
        }
    }

    private static class CompoundRoundedBorder extends LineBorder {
        private CompoundRoundedBorder() {
            super(new Color(215, 224, 235), 1, true);
        }

        @Override
        public Insets getBorderInsets(java.awt.Component c) {
            return new Insets(14, 14, 14, 14);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        EventQueue.invokeLater(() -> {
            VoThanhNhan23521092 frame = new VoThanhNhan23521092();
            frame.setVisible(true);
        });
    }
}
