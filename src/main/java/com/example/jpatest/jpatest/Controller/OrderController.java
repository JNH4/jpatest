package com.example.jpatest.jpatest.Controller;

import com.example.jpatest.jpatest.domain.Member;
import com.example.jpatest.jpatest.domain.Order;
import com.example.jpatest.jpatest.domain.item.Item;
import com.example.jpatest.jpatest.repository.OrderSearch;
import com.example.jpatest.jpatest.service.ItemService;
import com.example.jpatest.jpatest.service.MemberService;
import com.example.jpatest.jpatest.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private  final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){

        List <Member> members = memberService.findMembers();
        List <Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    /**
     * 컨트롤러 계층에서 서비스 계층으로 직접적인 엔티티를 구해서 넘겨줄수도 있음
     * 하지만 그러한 방식은 기본적으로 JPA의 영속성 컨텍스트 관리 룰에 위배되는 행위기 때문에
     * 컨트롤러 계층에서 엔티티에 관련된 뭔가를 한다기보다는 서비스 안의 @Transactional에서 처리해 주는게 좋
     * @param memberId
     * @param itemId
     * @param count
     * @return
     */
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){

        orderService.order(memberId, itemId, count);

        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch , Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }

    @PostMapping("orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
