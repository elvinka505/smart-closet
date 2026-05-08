package ru.isgaij.smartcloset.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import ru.isgaij.smartcloset.entity.User;
import ru.isgaij.smartcloset.entity.WishlistItem;
import ru.isgaij.smartcloset.exception.ResourceNotFoundException;
import ru.isgaij.smartcloset.repository.UserRepository;
import ru.isgaij.smartcloset.service.WishlistItemService;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistItemRestController {
    private final WishlistItemService wishlistItemService;
    private final UserRepository userRepository;
    public WishlistItemRestController(WishlistItemService wishlistItemService, UserRepository userRepository) {
        this.wishlistItemService = wishlistItemService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Get current user's wishlist items")
    public List<WishlistItem> getWishlistItems(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return wishlistItemService.findAllByUserId(user.getId());
    }

    @PostMapping
    @Operation(summary = "Add item to current user's wishlist")
    public WishlistItem addWishlistItem(@RequestBody WishlistItem wishlistItem, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        wishlistItem.setUser(user);
        return wishlistItemService.save(wishlistItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete wishlist item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wishlist item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Wishlist item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public void deleteWishlistItem(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        WishlistItem wishlistItem = wishlistItemService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));

        if (!wishlistItem.getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Wishlist item not found");
        }

        wishlistItemService.deleteById(id);
    }
}
