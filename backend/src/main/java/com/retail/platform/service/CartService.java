package com.retail.platform.service;

import com.retail.platform.model.Cart;
import com.retail.platform.model.CartItem;
import com.retail.platform.model.Customer;
import com.retail.platform.model.Product;
import com.retail.platform.repository.CartRepository;
import com.retail.platform.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;

    public Cart getOrCreateCart(Customer customer) {
        return cartRepository.findByCustomer(customer)
            .orElseGet(() -> {
                Cart cart = new Cart();
                cart.setCustomer(customer);
                return cartRepository.save(cart);
            });
    }

    public Cart addItem(Customer customer, Long productId, int quantity) {
        Cart cart = getOrCreateCart(customer);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        cart.getItems().stream()
            .filter(i -> i.getProduct().getProductId().equals(productId))
            .findFirst()
            .ifPresentOrElse(
                item -> item.setQuantity(item.getQuantity() + quantity),
                () -> {
                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    cart.getItems().add(item);
                }
            );
        return cartRepository.save(cart);
    }

    public Cart updateQuantity(Customer customer, Long cartItemId, int quantity) {
        Cart cart = getOrCreateCart(customer);
        cart.getItems().stream()
            .filter(i -> i.getCartItemId().equals(cartItemId))
            .findFirst()
            .ifPresent(item -> {
                if (quantity <= 0) cart.getItems().remove(item);
                else item.setQuantity(quantity);
            });
        return cartRepository.save(cart);
    }

    public Cart removeItem(Customer customer, Long cartItemId) {
        return updateQuantity(customer, cartItemId, 0);
    }

    public void clearCart(Customer customer) {
        cartRepository.findByCustomer(customer).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }
}
