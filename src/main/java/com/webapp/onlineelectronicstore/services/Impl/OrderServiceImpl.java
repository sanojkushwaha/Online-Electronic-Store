package com.webapp.onlineelectronicstore.services.Impl;

import com.webapp.onlineelectronicstore.dtos.request.CreateOrderRequest;
import com.webapp.onlineelectronicstore.dtos.response.OrderDto;
import com.webapp.onlineelectronicstore.dtos.response.OrderItemDto;
import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import com.webapp.onlineelectronicstore.dtos.response.UserDto;
import com.webapp.onlineelectronicstore.entites.*;
import com.webapp.onlineelectronicstore.enums.OrderStatus;
import com.webapp.onlineelectronicstore.enums.PaymentStatus;
import com.webapp.onlineelectronicstore.exceptions.OutOfStockException;
import com.webapp.onlineelectronicstore.exceptions.ResourceNotFoundException;
import com.webapp.onlineelectronicstore.helper.Helper;
import com.webapp.onlineelectronicstore.repositories.*;
import com.webapp.onlineelectronicstore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    //dependency Injection
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            UserRepository userRepository,
                            CartRepository cartRepository,
                            ProductRepository productRepository,
                            ModelMapper modelMapper) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDto placeOrder(String userId, CreateOrderRequest request) {

        //Core logic: placeOrder()

        //find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        //find cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        //cart validation
        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }
        //create order
        Order order = new Order();
        order.setUser(user);

        order.setBillingName(request.getBillingName());
        order.setBillingPhone(request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);

       //convert: CartItem->OrderItem
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;
        // Loop through cart items
        for (CartItem cartItem : cart.getItems()) {

            Product product = cartItem.getProduct();

            logger.info("===========================================");
            logger.info("Product: {}", product.getTitle());
            logger.info("Stock: {}", product.getQuantity());
            logger.info("Cart Quantity: {}", cartItem.getQuantity());

            if (product.getQuantity() < cartItem.getQuantity()) {
                throw new OutOfStockException(
                        product.getTitle() + " is out of stock");
            }

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductTitle(product.getTitle());
            orderItem.setProductImage(product.getProductImage());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductPrice(product.getDiscountedPrice());

            double total = product.getDiscountedPrice() * cartItem.getQuantity();
            orderItem.setTotalPrice(total);
            totalAmount += total;
            orderItems.add(orderItem);

            // Reduce stock
            product.setQuantity( product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }
        //set total
        order.setOrderAmount(totalAmount);
        order.getOrderItems().addAll(orderItems);

        //save order
        Order savedOrder = orderRepository.save(order);
        //clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        //return RTO
        return convertToOrderDto(savedOrder);
    }


    @Override
    public OrderDto getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return convertToOrderDto(order);
    }


    @Override
    public PageableResponse<OrderDto> getOrdersByUser(String userId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findByUser(user, pageable);

        return Helper.getPageableResponse(page, OrderDto.class);
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Order> page = orderRepository.findAll(pageable);

        return Helper.getPageableResponse(page, OrderDto.class);
    }


    @Override
    public OrderDto updateOrderStatus(String orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToOrderDto(updatedOrder);
    }


    @Override
    public OrderDto updatePaymentStatus(String orderId, PaymentStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setPaymentStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return convertToOrderDto(updatedOrder);
    }

    @Override
    public void cancelOrder(String orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }


    //conversion::convertOrderDto()
    private OrderDto convertToOrderDto(Order order) {

        OrderDto dto = new OrderDto();

        dto.setOrderId(order.getOrderId());
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliveryDate(order.getDeliveryDate());

        dto.setBillingName(order.getBillingName());
        dto.setBillingPhone(order.getBillingPhone());
        dto.setBillingAddress(order.getBillingAddress());

        dto.setPaymentMethod(order.getPaymentMethod());

        dto.setOrderAmount(order.getOrderAmount());

        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());

        // User
        UserDto userDto = new UserDto();
        userDto.setUserId(order.getUser().getUserId());
        userDto.setName(order.getUser().getName());
        userDto.setEmail(order.getUser().getEmail());

        dto.setUser(userDto);

        // Order Items
        List<OrderItemDto> items = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setOrderItemId(item.getOrderItemId());
            orderItemDto.setProductTitle(item.getProductTitle());
            orderItemDto.setProductImage(item.getProductImage());
            orderItemDto.setQuantity(item.getQuantity());
            orderItemDto.setProductPrice(item.getProductPrice());
            orderItemDto.setTotalPrice(item.getTotalPrice());

            items.add(orderItemDto);
        }

        dto.setOrderItems(items);

        return dto;
    }

}
