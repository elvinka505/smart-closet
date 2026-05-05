package ru.isgaij.smartcloset.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import ru.isgaij.smartcloset.entity.Item;
import ru.isgaij.smartcloset.entity.User;
import ru.isgaij.smartcloset.repository.BrandRepository;
import ru.isgaij.smartcloset.repository.CategoryRepository;
import ru.isgaij.smartcloset.repository.ItemRepository;
import ru.isgaij.smartcloset.repository.TagRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final TagRepository tagRepository;
    private final ColorService colorService;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository, BrandRepository brandRepository, TagRepository tagRepository,  ColorService colorService) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.tagRepository = tagRepository;
        this.colorService = colorService;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item save(Item item) {
        if (item.getId() != null && (item.getColor() == null || item.getColor().isBlank())) {
            Optional<Item> existing = itemRepository.findById(item.getId());
            if (existing.isPresent()) {
                item.setColor(existing.get().getColor());
                item.setColorName(existing.get().getColorName());
                item.setComplementColor(existing.get().getComplementColor());
            }
        }
        if (item.getColor() != null && !item.getColor().isBlank()) {
            String hex = item.getColor().replace("#", "");
            Map<String, String> colorInfo = colorService.getColorInfo(hex);
            item.setColorName(colorInfo.get("name"));
            item.setComplementColor(colorInfo.get("complement"));
        }
        return itemRepository.save(item);
    }
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public List<Item> findExpensiveItems(Long userId, BigDecimal minPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> query = cb.createQuery(Item.class);
        Root<Item> root = query.from(Item.class);

        query.select(root).where(
                cb.equal(root.get("user").get("id"), userId),
                cb.greaterThan(root.get("price"), minPrice)
        );

        return entityManager.createQuery(query).getResultList();
    }

    public List<Item> findAllByUser(User user) {
        return itemRepository.findAllByUser(user);
    }

    public List<Item> findMatchingItems(Item item) {
        if (item.getComplementColor() == null || item.getComplementColor().isBlank() || item.getUser() == null) {
            System.out.println("DEBUG: complementColor пустой или user null для item=" + item.getName());
            return List.of();
        }
        System.out.println("DEBUG: ищем по user=" + item.getUser().getId() + " color=" + item.getComplementColor());
        List<Item> result = itemRepository.findByUserAndColorIgnoreCase(item.getUser(), item.getComplementColor());
        System.out.println("DEBUG: найдено " + result.size() + " совпадений");
        return result;
    }
}
