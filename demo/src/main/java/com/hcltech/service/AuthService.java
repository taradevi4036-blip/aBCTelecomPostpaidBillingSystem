package com.hcltech.service;

import com.hcltech.dto.LoginRequest;
import com.hcltech.dto.LoginResponse;
import com.hcltech.dto.RegisterRequest;
import com.hcltech.entity.User;
import com.hcltech.repository.UserRepository;
import com.hcltech.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private com.hcltech.repository.CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) throws Exception {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Email already exists");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new Exception("Passwords do not match");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null && request.getRole().equalsIgnoreCase("admin") 
            ? User.UserRole.ADMIN 
            : User.UserRole.CUSTOMER);

        userRepository.save(user);
        // create a linked customer profile when registering a CUSTOMER
        if (user.getRole() == User.UserRole.CUSTOMER) {
            try {
                com.hcltech.entity.Customer customer = new com.hcltech.entity.Customer();
                customer.setUser(user);
                customer.setFullName(request.getFullName() != null ? request.getFullName() : "");
                customer.setAddress(request.getAddress() != null ? request.getAddress() : "");
                customer.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : "");
                customerRepository.save(customer);
            } catch (Exception ex) {
                // don't fail registration if customer creation fails; log in real app
            }
        }
    }

    public LoginResponse login(LoginRequest request) throws Exception {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new Exception("Invalid password");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().toString(), user.getUserId());

        Long customerId = null;
        try {
            com.hcltech.entity.Customer customer = customerRepository.findByUserUserId(user.getUserId()).orElse(null);
            if (customer != null) customerId = customer.getCustomerId();
        } catch (Exception ignored) {}

        LoginResponse resp = new LoginResponse(token, user.getUsername(), user.getEmail(), user.getRole().toString(), user.getUserId(), customerId);
        return resp;
    }

    public User getUserById(Long id) throws Exception {
        return userRepository.findById(id)
            .orElseThrow(() -> new Exception("User not found"));
    }

    public void updateUser(Long id, User updatedUser) throws Exception {
        User user = getUserById(id);
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        userRepository.save(user);
    }

    public void deleteUser(Long id) throws Exception {
        userRepository.deleteById(id);
    }
}
