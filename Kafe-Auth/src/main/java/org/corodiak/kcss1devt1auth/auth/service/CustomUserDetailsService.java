package org.corodiak.kcss1devt1auth.auth.service;

import java.util.Optional;

import org.corodiak.kcss1devt1auth.repository.UserRepository;
import org.corodiak.kcss1devt1auth.type.dto.UserPrincipal;
import org.corodiak.kcss1devt1auth.type.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findById(Long.parseLong(username));
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(username);
		}
		return UserPrincipal.create(user.get());
	}
}
