package ru.isgaij.smartcloset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.isgaij.smartcloset.controller.WishlistItemRestController;
import ru.isgaij.smartcloset.entity.User;
import ru.isgaij.smartcloset.repository.UserRepository;
import ru.isgaij.smartcloset.service.WishlistItemService;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishlistItemRestController.class)
public class WishlistItemRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishlistItemService wishlistItemService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void getWishlist_shouldReturnOk() throws Exception {
        User user = new User();
        when(userRepository.findByUsername("test"))
                .thenReturn(Optional.of(user));
        when(wishlistItemService.findAllByUserId(null))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/wishlist")
                        .with(user("test").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteWishlistItem_shouldReturn404_whenNotFound() throws Exception {
        when(wishlistItemService.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/wishlist/99")
                        .with(user("test").roles("USER"))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}