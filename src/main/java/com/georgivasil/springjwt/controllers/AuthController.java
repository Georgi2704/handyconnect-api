package com.georgivasil.springjwt.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.georgivasil.springjwt.models.AlreadyExistsError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.georgivasil.springjwt.models.ERole;
import com.georgivasil.springjwt.models.Role;
import com.georgivasil.springjwt.models.User;
import com.georgivasil.springjwt.payload.request.LoginRequest;
import com.georgivasil.springjwt.payload.request.SignupRequest;
import com.georgivasil.springjwt.payload.response.JwtResponse;
import com.georgivasil.springjwt.payload.response.MessageResponse;
import com.georgivasil.springjwt.repository.RoleRepository;
import com.georgivasil.springjwt.repository.UserRepository;
import com.georgivasil.springjwt.security.jwt.JwtUtils;
import com.georgivasil.springjwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@RequestMapping(value = "/getusername", method = RequestMethod.GET)
	@ResponseBody
	public String currentUserName(Authentication authentication) {
		return authentication.getName();
	}

	@RequestMapping(value = "/getuserdetails", method = RequestMethod.GET)
	@ResponseBody
	public User currentUserDetails(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Set<Role> roles = new HashSet<>();
		Set<String> rolesstring = new HashSet<>();
		for (GrantedAuthority a: userDetails.getAuthorities()) {
			rolesstring.add(a.getAuthority());
			System.out.println(a.getAuthority());
		}
		roles = getRoles(rolesstring);
		User user = new User(userDetails.getUsername(), userDetails.getEmail(), "", userDetails.getFirstName(), userDetails.getLastName());
		user.setRoles(roles);
		for (Role r: roles) {
			System.out.println(r.getName());
		}
		user.setProfilePic(userDetails.getProfilepic());
		user.setRegistered_at(userDetails.getRegistered_at());

		return user;
	}


	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles, userDetails.getProfilepic()));
	}

	private Set<Role> getRoles (Set<String> strRoles){
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		}
		else {
			strRoles.forEach(role -> {
					if(role.equals("admin") || role.equals("ROLE_ADMIN")) {
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);
					}
					else if(role.equals("handyman") || role.equals("ROLE_HANDYMAN")) {
						Role handymanRole = roleRepository.findByName(ERole.ROLE_HANDYMAN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(handymanRole);
					}
					else {
						Role userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(userRole);
					}
			});
		}
		return roles;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		AlreadyExistsError AlreadyExistsError = new AlreadyExistsError();

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			AlreadyExistsError.setUserAlreadyExists(true);
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			AlreadyExistsError.setEmailAlreadyExists(true);
		}

		if (AlreadyExistsError.hasAnyErrors()){
			return ResponseEntity
					.badRequest()
					.body(AlreadyExistsError);
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getFirstName(),
				signUpRequest.getLastName());

		Set<String> strRoles = new HashSet<>();
		strRoles = signUpRequest.getRole();

		user.setRoles(getRoles(strRoles));
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@CrossOrigin
	@GetMapping("/users")
	@ResponseBody
	public List<User> searchUser(@RequestParam String username){
		return  userRepository.findByUsernameContaining(username);
	}
}

