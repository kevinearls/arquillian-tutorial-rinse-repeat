package com.kevinearls.arquillian.rinse.repeat;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BasketTest {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(Basket.class, OrderRepository.class, SingletonOrderRepository.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    Basket basket;

    @EJB
    OrderRepository orderRepository;

    @Test
    @InSequence(1)
    public void placeOrderShouldAddOrder() {
        basket.addItem("sunglasses");
        basket.addItem("suit");
        basket.placeOrder();
        assertEquals(1, orderRepository.getOrderCount());
        assertEquals(0, basket.getItemCount());

        basket.addItem("raygun");
        basket.addItem("spaceship");
        basket.placeOrder();
        assertEquals(2, orderRepository.getOrderCount());
        assertEquals(0, basket.getItemCount());
    }

    @Test
    @InSequence(2)
    public void orderShouldBePersistent() {
        assertEquals(2, orderRepository.getOrderCount());
    }
}
