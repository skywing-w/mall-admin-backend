package com.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.PageResult;
import com.mall.dto.OrderQuery;
import com.mall.entity.Order;
import com.mall.exception.BusinessException;
import com.mall.mapper.OrderItemMapper;
import com.mall.mapper.OrderMapper;
import com.mall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单服务实现。
 * <p>
 * 订单状态流转：0 待支付 → 1 待发货 → 2 待收货 → 3 已完成；
 * 0/1 可取消（→ 4），2/3 可退款（→ 6）。状态值集中定义在下方常量中，避免魔法数字散落。
 *
 * @author wwty
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    /** 待支付 */
    private static final int STATUS_UNPAID = 0;
    /** 待发货 */
    private static final int STATUS_UNSHIPPED = 1;
    /** 待收货 */
    private static final int STATUS_UNRECEIVED = 2;
    /** 已完成 */
    private static final int STATUS_FINISHED = 3;
    /** 已取消 */
    private static final int STATUS_CANCELED = 4;
    /** 退款中 */
    private static final int STATUS_REFUNDING = 6;

    private final OrderItemMapper orderItemMapper;

    @Override
    public PageResult<Order> page(OrderQuery query) {
        IPage<Order> page = new Page<>(query.getPage(), query.getPageSize());
        IPage<Order> res = baseMapper.pageByQuery(page, query);
        return PageResult.of(res.getTotal(), res.getRecords());
    }

    @Override
    public Order detail(Long id) {
        Order order = baseMapper.selectDetail(id);
        if (order == null) return null;
        order.setItems(orderItemMapper.selectByOrderId(id));
        return order;
    }

    @Override
    @Transactional
    public boolean ship(Long id) {
        Order o = getById(id);
        if (o == null) throw new BusinessException("订单不存在");
        // 用 equals 比较而非 != 直接拆箱：status 为 null 时不会抛 NullPointerException
        if (!Integer.valueOf(STATUS_UNSHIPPED).equals(o.getStatus())) throw new BusinessException("仅待发货订单可发货");
        Order upd = new Order();
        upd.setId(id);
        upd.setStatus(STATUS_UNRECEIVED);
        return updateById(upd);
    }

    @Override
    @Transactional
    public boolean cancel(Long id) {
        Order o = getById(id);
        if (o == null) throw new BusinessException("订单不存在");
        Integer status = o.getStatus();
        if (!Integer.valueOf(STATUS_UNPAID).equals(status) && !Integer.valueOf(STATUS_UNSHIPPED).equals(status)) {
            throw new BusinessException("仅待支付或待发货订单可取消");
        }
        Order upd = new Order();
        upd.setId(id);
        upd.setStatus(STATUS_CANCELED);
        return updateById(upd);
    }

    @Override
    @Transactional
    public boolean refund(Long id) {
        Order o = getById(id);
        if (o == null) throw new BusinessException("订单不存在");
        Integer status = o.getStatus();
        if (!Integer.valueOf(STATUS_UNRECEIVED).equals(status) && !Integer.valueOf(STATUS_FINISHED).equals(status)) {
            throw new BusinessException("仅待收货或已完成订单可退款");
        }
        Order upd = new Order();
        upd.setId(id);
        upd.setStatus(STATUS_REFUNDING);
        return updateById(upd);
    }
}