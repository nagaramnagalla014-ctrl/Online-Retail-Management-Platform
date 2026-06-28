package com.retail.platform.config;

import com.retail.platform.model.*;
import com.retail.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PromotionRepository promotionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() > 0) return;

        // Categories
        Category electronics = createCategory("Electronics", "Gadgets and electronic devices", "electronics.jpg");
        Category clothing = createCategory("Clothing", "Fashion and apparel", "clothing.jpg");
        Category books = createCategory("Books", "Books and literature", "books.jpg");
        Category home = createCategory("Home & Kitchen", "Home appliances and kitchen tools", "home.jpg");
        List<Category> categories = Arrays.asList(electronics, clothing, books, home);
        categoryRepository.saveAll(categories);

        // Products - Electronics
        createProduct("EL-001", "Wireless Bluetooth Headphones", "Premium noise-cancelling headphones with 30h battery",
            new BigDecimal("2999.00"), 50, electronics, "Sony", "headphones.jpg");
        createProduct("EL-002", "Smartphone Stand", "Adjustable desktop stand for phones and tablets",
            new BigDecimal("499.00"), 120, electronics, "Anker", "stand.jpg");
        createProduct("EL-003", "USB-C Hub 7-in-1", "Multiport adapter with HDMI, USB-A, SD card",
            new BigDecimal("1799.00"), 80, electronics, "Ugreen", "hub.jpg");
        createProduct("EL-004", "Mechanical Keyboard", "TKL mechanical keyboard with RGB backlight",
            new BigDecimal("3499.00"), 30, electronics, "Keychron", "keyboard.jpg");

        // Products - Clothing
        createProduct("CL-001", "Men's Casual T-Shirt", "100% cotton comfortable everyday t-shirt",
            new BigDecimal("349.00"), 200, clothing, "United Colors", "tshirt.jpg");
        createProduct("CL-002", "Women's Denim Jeans", "Slim fit high-waist blue jeans",
            new BigDecimal("999.00"), 150, clothing, "Levi's", "jeans.jpg");
        createProduct("CL-003", "Running Shoes", "Lightweight breathable mesh running shoes",
            new BigDecimal("1299.00"), 100, clothing, "Nike", "shoes.jpg");

        // Products - Books
        createProduct("BK-001", "Clean Code", "A Handbook of Agile Software Craftsmanship",
            new BigDecimal("499.00"), 75, books, "Robert Martin", "cleancode.jpg");
        createProduct("BK-002", "Spring Boot in Action", "Mastering Spring Boot for Java developers",
            new BigDecimal("399.00"), 60, books, "Manning", "springboot.jpg");

        // Products - Home
        createProduct("HM-001", "Stainless Steel Water Bottle", "1L insulated bottle keeps drinks cold for 24h",
            new BigDecimal("699.00"), 90, home, "Milton", "bottle.jpg");
        createProduct("HM-002", "Non-Stick Cookware Set", "5-piece induction-compatible cookware set",
            new BigDecimal("2499.00"), 40, home, "Prestige", "cookware.jpg");

        // Demo customer
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("password123");
        customer.setPhone("9876543210");
        customer.setAddress("123 Main Street");
        customer.setCity("Mumbai");
        customer.setState("Maharashtra");
        customer.setPincode("400001");
        customerRepository.save(customer);

        // Promotions
        Promotion promo1 = new Promotion();
        promo1.setCode("WELCOME10");
        promo1.setDescription("10% off on your first order");
        promo1.setDiscountType("PERCENTAGE");
        promo1.setDiscountValue(new BigDecimal("10"));
        promo1.setMinOrderAmount(BigDecimal.ZERO);
        promo1.setMaxUses(100);
        promotionRepository.save(promo1);

        Promotion promo2 = new Promotion();
        promo2.setCode("FLAT100");
        promo2.setDescription("Rs. 100 off on orders above Rs. 999");
        promo2.setDiscountType("FLAT");
        promo2.setDiscountValue(new BigDecimal("100"));
        promo2.setMinOrderAmount(new BigDecimal("999"));
        promotionRepository.save(promo2);
    }

    private Category createCategory(String name, String desc, String img) {
        Category c = new Category();
        c.setName(name);
        c.setDescription(desc);
        c.setImageUrl(img);
        return c;
    }

    private void createProduct(String sku, String name, String desc, BigDecimal price,
                                int stock, Category cat, String brand, String img) {
        Product p = new Product();
        p.setSku(sku);
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setStockQuantity(stock);
        p.setCategory(cat);
        p.setBrand(brand);
        p.setImageUrl(img);
        productRepository.save(p);
    }
}
