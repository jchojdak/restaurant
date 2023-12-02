package com.jchojdak.restaurant.service;

import com.jchojdak.restaurant.exception.NotFoundException;
import com.jchojdak.restaurant.model.Order;
import com.jchojdak.restaurant.model.OrderProduct;
import com.jchojdak.restaurant.model.Product;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.model.dto.OrderDto;
import com.jchojdak.restaurant.model.dto.OrderProductDto;
import com.jchojdak.restaurant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements  IOrderService {

    private final OrderRepository orderRepository;
    private final IProductService productService;
    private final ModelMapper modelMapper;

    @Override
    public void createOrder(User user, OrderDto orderDto) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("CREATED");
        order.setTotalPrice(calculateTotalPrice(orderDto.getOrderProductsDto()));
        order.setOptionalDeliveryAddress(orderDto.getOptionalDeliveryAddress());
        order.setOptional_table_number(orderDto.getOptional_table_number());
        order.setUser(user);

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDto orderProductDto : orderDto.getOrderProductsDto()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);

            Product product = productService.getProductById(orderProductDto.getId());
            orderProduct.setProduct(product);

            orderProduct.setQuantity(orderProductDto.getQuantity());
            orderProducts.add(orderProduct);
        }

        order.setOrderProducts(orderProducts);

        orderRepository.save(order);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return mapToOrderDto(order.get());
        } else {
            throw new NotFoundException("Order does not exist");
        }
    }

    private OrderDto mapToOrderDto(Order order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);

        List<OrderProductDto> orderProductsDto = new ArrayList<>();

        for (OrderProduct orderProduct : order.getOrderProducts()) {
            OrderProductDto orderProductDto = new OrderProductDto();

            orderProductDto.setId(orderProduct.getProduct().getId());
            orderProductDto.setQuantity(orderProduct.getQuantity());

            orderProductsDto.add(orderProductDto);
        }

        orderDto.setOrderProductsDto(orderProductsDto);


        return orderDto;
    }

    private BigDecimal calculateTotalPrice(List<OrderProductDto> orderProductsDto) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderProductDto orderProductDto : orderProductsDto) {
            Product product = productService.getProductById(orderProductDto.getId());

            BigDecimal productPrice = product.getPrice();
            int quantity = orderProductDto.getQuantity();
            totalPrice = totalPrice.add(productPrice.multiply(BigDecimal.valueOf(quantity)));
        }

        return totalPrice;
    }
}
