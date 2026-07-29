package com.webapp.onlineelectronicstore.services.Impl;

import com.webapp.onlineelectronicstore.dtos.request.AddItemToCartRequest;
import com.webapp.onlineelectronicstore.dtos.response.CartDto;
import com.webapp.onlineelectronicstore.entites.Cart;
import com.webapp.onlineelectronicstore.entites.CartItem;
import com.webapp.onlineelectronicstore.entites.Product;
import com.webapp.onlineelectronicstore.entites.User;
import com.webapp.onlineelectronicstore.exceptions.ResourceNotFoundException;
import com.webapp.onlineelectronicstore.repositories.CartItemRepository;
import com.webapp.onlineelectronicstore.repositories.CartRepository;
import com.webapp.onlineelectronicstore.repositories.ProductRepository;
import com.webapp.onlineelectronicstore.repositories.UserRepository;
import com.webapp.onlineelectronicstore.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository,
                           ModelMapper modelMapper) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found in db"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUser(user).orElse(null);

        if (cart == null) {

            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());

            cart = cartRepository.save(cart);

        } else {

            Optional<CartItem> optionalItem =
                    cartItemRepository.findByCartAndProduct(cart, product);

            if (optionalItem.isPresent()) {

                CartItem item = optionalItem.get();

                item.setQuantity(item.getQuantity() + request.getQuantity());

                item.setTotalPrice(
                        item.getQuantity() * product.getDiscountedPrice());

                cartItemRepository.save(item);

                return modelMapper.map(cart, CartDto.class);
            }
        }
        CartItem item = new CartItem();

        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(request.getQuantity());

        item.setTotalPrice(
                request.getQuantity() * product.getDiscountedPrice()
        );

        cart.getItems().add(item);

        //save the cart
        Cart savedCart = cartRepository.save(cart);

        return modelMapper.map(savedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, String cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart Item not found"));

        cartItemRepository.delete(item);
    }

    @Override
    public CartDto updateItemQuantity(String userId,
                                      String cartItemId,
                                      int quantity) {

        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() ->
                        new ResourceNotFoundException("Cart Item not found"));

        item.setQuantity(quantity);

        item.setTotalPrice(
                quantity * item.getProduct().getDiscountedPrice());

        CartItem updatedItem = cartItemRepository.save(item);

        return modelMapper.map(updatedItem.getCart(), CartDto.class);
    }

    @Override
    public CartDto getCartByUser(String userId) {

        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart of user not found"));

        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public void clearCart(String userId) {

        Cart cart = cartRepository.findByUserUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();

        cartRepository.save(cart);
    }
}