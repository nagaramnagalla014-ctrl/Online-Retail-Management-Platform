package com.retail.platform.service;

import com.retail.platform.model.Promotion;
import com.retail.platform.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PromotionService {

    @Autowired private PromotionRepository promotionRepository;

    @Transactional(readOnly = true)
    public List<Promotion> getAll() {
        return promotionRepository.findAll();
    }

    public Promotion save(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public BigDecimal applyPromotion(String code, BigDecimal orderAmount) {
        Optional<Promotion> opt = promotionRepository.findByCodeIgnoreCaseAndIsActiveTrue(code);
        if (!opt.isPresent())
            throw new RuntimeException("Invalid or expired promo code: " + code);

        Promotion promo = opt.get();
        Date now = new Date();
        if (promo.getStartDate() != null && now.before(promo.getStartDate()))
            throw new RuntimeException("Promo code not yet active");
        if (promo.getEndDate() != null && now.after(promo.getEndDate()))
            throw new RuntimeException("Promo code has expired");
        if (orderAmount.compareTo(promo.getMinOrderAmount()) < 0)
            throw new RuntimeException("Order does not meet minimum amount for this promo");
        if (promo.getMaxUses() > 0 && promo.getUsesCount() >= promo.getMaxUses())
            throw new RuntimeException("Promo code usage limit reached");

        BigDecimal discount;
        if ("PERCENTAGE".equals(promo.getDiscountType())) {
            discount = orderAmount.multiply(promo.getDiscountValue()).divide(BigDecimal.valueOf(100));
        } else {
            discount = promo.getDiscountValue();
        }
        if (discount.compareTo(orderAmount) > 0) discount = orderAmount;

        promo.setUsesCount(promo.getUsesCount() + 1);
        promotionRepository.save(promo);
        return discount;
    }

    public void delete(Long id) {
        promotionRepository.deleteById(id);
    }
}
