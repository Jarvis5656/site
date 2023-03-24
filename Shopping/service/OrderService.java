package com.Shopping.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.Shopping.models.Cart;
import com.Shopping.models.Customer;
import com.Shopping.models.Order;
import com.Shopping.models.OrderDetails;
import com.Shopping.models.Product;
import com.Shopping.repos.OrderRepository;

@Service
public class OrderService {
	

	@Autowired private OrderRepository orepo;
	@Autowired private CartService cartsrv;
	@Autowired private CustomerService custsrv;
	@Autowired private OrderDetailsService odsrv;
	public List<Order> allOrders(){
		return orepo.findAll(Sort.by(Direction.DESC,"id"));
	}
//	public List<Order> allOrderss(){
//		return orepo.findByDeletedOrderByIdDesc(false);
//	}

	public List<Order> pendingOrders(){
		return orepo.findByStatusOrderByIdDesc("Pending");
	}
	
	public List<Order> allUserOrders(String userid){
		return orepo.findByUseridOrderByIdDesc(userid);
	}
	
	public Order getOrderDetails(int orderid) {
		return orepo.findById(orderid).get();
	}
	
	public void cancelOrder(int orderid) {
		odsrv.deleteAllItems(orderid);
		orepo.deleteById(orderid);
	}
	
	public void confirmOrder(int orderid) {
		Order order=orepo.getById(orderid);
		order.setStatus("Confirmed");
		orepo.save(order);
	}
	public void deleteorder(int orderid) {
		odsrv.deleteAllItems(orderid);
		orepo.deleteById(orderid);
	} 
		
	public int placeOrder(Order o,String userid) {
		
		Customer customer=custsrv.findByUserId(userid);
		o.setUserid(userid);
		o.setCustomer(customer);
		Order order=orepo.save(o);
		List<Cart> cartitems=cartsrv.findItemsByUserId(userid);
		for(Cart c : cartitems) {
			OrderDetails od=new OrderDetails();
			od.setOrder(order);
			od.setOrderid(order.getId());
			od.setProduct(c.getProduct());
			od.setQty(c.getQty());
			od.setProdid(c.getProdid());
			od.setPrice(c.getProduct().getPrice());
			odsrv.saveItem(od);
		}		
		cartsrv.emptyCart(userid);
		return o.getId();
	}
}
