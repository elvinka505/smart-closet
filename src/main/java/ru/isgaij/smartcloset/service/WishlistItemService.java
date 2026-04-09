package ru.isgaij.smartcloset.service;

import org.springframework.stereotype.Service;
import ru.isgaij.smartcloset.entity.WishlistItem;
import ru.isgaij.smartcloset.repository.WishlistItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistItemService {
    private final WishlistItemRepository wishlistItemRepository;

    public WishlistItemService(WishlistItemRepository wishlistItemRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
    }

    public List<WishlistItem> findAllByUserId(Long userId) {
        return wishlistItemRepository.findByUserId(userId);
    }

    public WishlistItem save(WishlistItem item) {
        return wishlistItemRepository.save(item);
    }

    public void deleteById(Long id) {
        wishlistItemRepository.deleteById(id);
    }

    public Optional<WishlistItem> findById(Long id) {
        return wishlistItemRepository.findById(id);
    }
}
