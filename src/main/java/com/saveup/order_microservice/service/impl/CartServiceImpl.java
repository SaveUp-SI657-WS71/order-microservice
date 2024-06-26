package com.saveup.order_microservice.service.impl;

import com.saveup.order_microservice.dto.CartDto;
import com.saveup.order_microservice.dto.ProductDto;
import com.saveup.order_microservice.exception.ResourceNotFoundException;
import com.saveup.order_microservice.model.Cart;
import com.saveup.order_microservice.model.Order;
import com.saveup.order_microservice.repository.CartRepository;
import com.saveup.order_microservice.repository.OrderRepository;
import com.saveup.order_microservice.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    private CartRepository cartRepository;
    private ProductServiceImpl productServiceImpl;
    private OrderRepository orderRepository;

    private final JdbcTemplate jdbcTemplate;
    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductServiceImpl productServiceImpl, OrderRepository orderRepository, ModelMapper modelMapper, JdbcTemplate jdbcTemplate) {
        this.cartRepository = cartRepository;
        this.productServiceImpl = productServiceImpl;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CartDto createCart(CartDto cartDto) {
        Cart cart = DtoToEntity(cartDto);
        ProductDto productDto = productServiceImpl.getProductById(cartDto.getProductId());
        if (productDto == null) {
            throw new ResourceNotFoundException("No se encontró el producto con id: " + cartDto.getProductId());
        }
        cart.setProductId(productDto.getId());

        Order order = orderRepository.findById(cartDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la orden con id: " + cartDto.getOrderId()));
        cart.setOrder(order);

        return EntityToDto(cartRepository.save(cart));
    }

    @Override
    public void updateCart(Cart cart) { cartRepository.save(cart); }

    @Override
    public Cart getCart(int id) { return cartRepository.findById(id).get(); }

    @Override
    public void deleteCart(int id) { cartRepository.deleteById(id); }

    @Override
    public void deleteCartsByOrder(int orderId) { cartRepository.deleteByOrderId(orderId); }

    @Override
    public List<CartDto> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(this::EntityToDto).toList();
    }

    @Override
    public boolean isCartExist(int id) { return cartRepository.existsById(id); }

    @Override
    public List<Map<String, Object>> getCartByOrder(int orderId) {
        String query = "SELECT p.id AS product_id, " +
                "p.name AS product_name, " +
                "p.description, " +
                "p.price, " +
                "p.stock, " +
                "p.image, " +
                "p.expiration_date, " +
                "c.quantity " +
                "FROM saveup.cart c " +
                "JOIN saveup.product p ON c.product_id = p.id " +
                "WHERE c.order_id = ?;";

        return jdbcTemplate.queryForList(query, orderId);
    }


    @Override
    public void deleteCartByOrderAndProduct(int orderId, int productId) {
        cartRepository.deleteByOrderIdAndProductId(orderId, productId);
    }

    private CartDto EntityToDto(Cart cart) { return modelMapper.map(cart, CartDto.class); }

    private Cart DtoToEntity(CartDto cartDto) {
        return modelMapper.map(cartDto, Cart.class);
    }
}