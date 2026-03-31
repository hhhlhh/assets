package com.fixedasset.system.service;

import com.fixedasset.system.dto.UserDto;
import com.fixedasset.system.entity.User;
import com.fixedasset.system.exception.DuplicateResourceException;
import com.fixedasset.system.exception.ResourceNotFoundException;
import com.fixedasset.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToDto);
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateResourceException("用户名已存在: " + userDto.getUsername());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("邮箱已被使用: " + userDto.getEmail());
        }

        User user = convertToEntity(userDto);
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + id));

        // 检查用户名和邮箱的唯一性
        if (!existingUser.getUsername().equals(userDto.getUsername()) &&
                userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateResourceException("用户名已存在: " + userDto.getUsername());
        }

        if (!existingUser.getEmail().equals(userDto.getEmail()) &&
                userRepository.existsByEmail(userDto.getEmail())) {
            throw new DuplicateResourceException("邮箱已被使用: " + userDto.getEmail());
        }

        updateEntityFromDto(existingUser, userDto);
        User updatedUser = userRepository.save(existingUser);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("用户不存在，ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户未找到: " + username));
    }

    public List<UserDto> getUsersByRole(com.fixedasset.system.entity.UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToDto)
                .toList();
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDepartment(user.getDepartment());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    private User convertToEntity(UserDto dto) {
        User user = new User();
        updateEntityFromDto(user, dto);
        return user;
    }

    private void updateEntityFromDto(User user, UserDto dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setDepartment(dto.getDepartment());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
    }
}