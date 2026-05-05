package ru.isgaij.smartcloset.service;

import org.springframework.stereotype.Service;
import ru.isgaij.smartcloset.entity.Item;
import ru.isgaij.smartcloset.entity.Purchase;
import ru.isgaij.smartcloset.entity.User;
import ru.isgaij.smartcloset.repository.PurchaseRepository;
import ru.isgaij.smartcloset.repository.UserRepository;

import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public List<Purchase> findAllByUserId(Long userId) {
        return purchaseRepository.findByUserId(userId);
    }

    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }
}
