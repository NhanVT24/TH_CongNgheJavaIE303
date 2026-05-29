import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class VoThanhNhan23521092 extends JFrame {
    private final List<Product> products = new ArrayList<>();
    private final List<ProductCard> productCards = new ArrayList<>();

    private final HeroPanel heroPanel = new HeroPanel();
    private final JLabel titleLabel = new JLabel();
    private final JLabel priceLabel = new JLabel();
    private final JLabel brandLabel = new JLabel();
    private final JLabel descLabel = new JLabel();

    public VoThanhNhan23521092() {
        initProducts();
        initFrame();
        initUi();
        selectProduct(products.get(0));
    }

    private void initProducts() {
        products.add(new Product("4DFWD PULSE SHOES", 160.00, "Adidas",
                "This product is excluded from all promotional discounts and offers.",
                "Lab03/img1.png"));
        products.add(new Product("FORUM MID SHOES", 100.00, "Adidas",
                "Classic basketball style with bold blue-and-white contrast.",
                "Lab03/img2.png"));
        products.add(new Product("SUPERNOVA SHOES", 150.00, "Adidas",
                "NMD City Stock 2 inspired everyday comfort for active wear.",
                "Lab03/img3.png"));
        products.add(new Product("ADIDAS RUNNER", 160.00, "Adidas",
                "Lightweight cushioning with a clean white finish and neon accents.",
                "Lab03/img4.png"));
        products.add(new Product("NMD CITY STOCK 2", 120.00, "Adidas",
                "Dark modern styling designed for a sleek urban sports look.",
                "Lab03/img5.png"));
        products.add(new Product("4DFWD PULSE ORANGE", 160.00, "Adidas",
                "Responsive 4D midsole with eye-catching orange performance design.",
                "Lab03/img6.png"));
    }

    private void initFrame() {
        setTitle("Lab03 - Product Showcase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 650);
        setMinimumSize(new Dimension(1000, 600));
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

    private JPanel createHeroSection() {
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(280, 0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        heroPanel.setOpaque(false);
        heroPanel.setPreferredSize(new Dimension(260, 220));
        heroPanel.setMaximumSize(new Dimension(260, 220));
        heroPanel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel line = new JPanel();
        line.setOpaque(false);
        line.setMaximumSize(new Dimension(280, 1));
        line.setPreferredSize(new Dimension(280, 1));
        line.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(185, 190, 196)));

        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(67, 67, 67));
        titleLabel.setAlignmentX(LEFT_ALIGNMENT);

        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        priceLabel.setForeground(new Color(67, 67, 67));
        priceLabel.setAlignmentX(LEFT_ALIGNMENT);

        brandLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        brandLabel.setForeground(new Color(92, 92, 92));
        brandLabel.setAlignmentX(LEFT_ALIGNMENT);

        descLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        descLabel.setForeground(new Color(157, 157, 157));
        descLabel.setAlignmentX(LEFT_ALIGNMENT);

        left.add(Box.createVerticalStrut(54));
        left.add(heroPanel);
        left.add(Box.createVerticalStrut(22));
        left.add(line);
        left.add(Box.createVerticalStrut(18));
        left.add(titleLabel);
        left.add(Box.createVerticalStrut(12));
        left.add(priceLabel);
        left.add(Box.createVerticalStrut(8));
        left.add(brandLabel);
        left.add(Box.createVerticalStrut(10));
        left.add(descLabel);

        return left;
    }

    private JScrollPane createListSection() {
        JPanel grid = new JPanel(new GridLayout(0, 4, 12, 12));
        grid.setOpaque(false);

        for (Product product : buildCardProducts()) {
            ProductCard card = new ProductCard(product);
            productCards.add(card);
            grid.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        return scrollPane;
    }

    private List<Product> buildCardProducts() {
        List<Product> cardProducts = new ArrayList<>();
        cardProducts.add(products.get(0));
        cardProducts.add(products.get(1));
        cardProducts.add(products.get(2));
        cardProducts.add(products.get(3));
        cardProducts.add(products.get(4));
        cardProducts.add(products.get(5));
        cardProducts.add(products.get(0));
        cardProducts.add(products.get(1));
        return cardProducts;
    }

    private void selectProduct(Product product) {
        titleLabel.setText(product.name);
        priceLabel.setText(formatPrice(product.price));
        brandLabel.setText(product.brand);
        descLabel.setText("<html><body style='width:230px'>" + product.description + "</body></html>");
        heroPanel.animateTo(product);

        for (ProductCard card : productCards) {
            card.setSelected(card.product == product);
        }
    }

    private String formatPrice(double price) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(price);
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

    private static class Product {
        private final String name;
        private final double price;
        private final String brand;
        private final String description;
        private final String imagePath;

        private Product(String name, double price, String brand, String description, String imagePath) {
            this.name = name;
            this.price = price;
            this.brand = brand;
            this.description = description;
            this.imagePath = imagePath;
        }
    }

    private class ProductCard extends JPanel {
        private final Product product;
        private final JLabel title = new JLabel();
        private final JLabel subtitle = new JLabel();
        private final JLabel image = new JLabel();
        private final JLabel brand = new JLabel();
        private final JLabel price = new JLabel();
        private boolean selected;

        private ProductCard(Product product) {
            this.product = product;
            setOpaque(false);
            setPreferredSize(new Dimension(190, 238));
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(10, 10, 10, 10));

            JPanel textPanel = new JPanel();
            textPanel.setOpaque(false);
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

            title.setText(clip(product.name, 18));
            title.setFont(new Font("SansSerif", Font.BOLD, 16));
            title.setForeground(new Color(71, 71, 71));

            subtitle.setText(clip(product.description, 28));
            subtitle.setFont(new Font("SansSerif", Font.BOLD, 11));
            subtitle.setForeground(new Color(188, 188, 188));

            textPanel.add(title);
            textPanel.add(Box.createVerticalStrut(8));
            textPanel.add(subtitle);

            image.setHorizontalAlignment(SwingConstants.CENTER);
            image.setIcon(loadScaledIcon(product.imagePath, 150, 90));

            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setOpaque(false);

            brand.setText(product.brand);
            brand.setFont(new Font("SansSerif", Font.PLAIN, 13));
            brand.setForeground(new Color(84, 84, 84));

            price.setText(formatPrice(product.price));
            price.setFont(new Font("SansSerif", Font.BOLD, 16));
            price.setForeground(new Color(76, 76, 76));

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
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(245, 245, 245));
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 18, 18));

            if (selected) {
                g2.setColor(new Color(106, 155, 255));
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 18, 18));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class HeroPanel extends JPanel {
        private ImageIcon currentIcon;
        private ImageIcon nextIcon;
        private float alpha = 1f;
        private Timer timer;

        private void animateTo(Product product) {
            nextIcon = loadScaledIcon(product.imagePath, 235, 145);

            if (currentIcon == null) {
                currentIcon = nextIcon;
                repaint();
                return;
            }

            if (timer != null && timer.isRunning()) {
                timer.stop();
            }

            alpha = 0f;
            timer = new Timer(22, e -> {
                alpha += 0.08f;
                if (alpha >= 1f) {
                    alpha = 1f;
                    currentIcon = nextIcon;
                    timer.stop();
                }
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 28));
            g2.fillOval(38, 140, 165, 24);

            if (currentIcon != null) {
                g2.drawImage(currentIcon.getImage(), 18, 40, null);
            }

            if (nextIcon != null && alpha < 1f) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.drawImage(nextIcon.getImage(), 18, 40, null);
            }

            g2.dispose();
        }
    }

    private static class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255),
                    getWidth(), getHeight(), new Color(247, 248, 250)));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
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
