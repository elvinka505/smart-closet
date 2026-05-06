package ru.isgaij.smartcloset.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.isgaij.smartcloset.dto.RegisterForm;
import ru.isgaij.smartcloset.entity.Role;
import ru.isgaij.smartcloset.entity.User;
import ru.isgaij.smartcloset.repository.RoleRepository;
import ru.isgaij.smartcloset.repository.UserRepository;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(RegisterForm form) {
        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());

        user.setPassword(passwordEncoder.encode(form.getPassword()));


        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }
}
