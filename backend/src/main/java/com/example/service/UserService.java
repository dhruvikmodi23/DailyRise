package com.example.service;

import com.example.exception.InformationExistException;
import com.example.exception.InformationNotFoundException;
import com.example.model.Profile;
import com.example.model.User;
import com.example.model.request.LoginRequest;
import com.example.repository.UserRepository;
import com.example.security.JWTUtils;
import com.example.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Represents the User Service, responsible for housing business logic related to users.<br>
 *
 * This class serves as an intermediary between the controller and the repository,
 * invoking the repository to perform CRUD operations on users.
 *
 * @version 1.4.2
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    /**
     * Injects dependencies and enables userService to access the resources.
     *
     * @param userRepository The repository for user-related CRUD operations.
     * @param passwordEncoder The encoder used for password hashing.
     * @param jwtUtils The utility class for JWT token generation and validation.
     * @param authenticationManager Manages authentication within the security context.
     */
    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils,
                       @Lazy AuthenticationManager authenticationManager) { //@LAZY - loads as needed
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user.
     * @param user User object containing details.
     * @return The registered User.
     * @throws InformationExistException If email is already registered.
     */
    public User createUser(User user){
        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByEmailAddress(user.getEmailAddress())); //checks if email address already exists in database
        if (userOptional.isEmpty()){ // email not registered yet
            user.setPassword(passwordEncoder.encode(user.getPassword())); //encode password given
            user.setProfile(new Profile());
            return userRepository.save(user);
        } else {
            throw new InformationExistException("user with email address " + user.getEmailAddress() + " already exist.");
        }
    }
    /**
     * Authenticate a user based on the provided login request.
     *
     * @param loginRequest The login request with email and password.
     * @return Optional JWT token if authentication is successful;
     *         otherwise, an empty Optional.
     */
    public Optional<String> loginUser(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(loginRequest.getEmailAddress(), loginRequest.getPassword());
        try{
            Authentication authentication = authenticationManager.authenticate((authenticationToken)); //authenticate the user
            SecurityContextHolder.getContext().setAuthentication(authentication); //set security context
            MyUserDetails myUserDetails = ( MyUserDetails ) authentication.getPrincipal(); //get user details from authenticated object
            return Optional.of(jwtUtils.generateJwtToken(myUserDetails)); // generate a token for the authenticated user
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Finds a User entity based on the provided email address.
     *
     * @param emailAddress The email address to search for.
     * @return The User entity,
     *         or null if no matching user is found.
     */
    public User findUserByEmailAddress(String emailAddress){
        return userRepository.findUserByEmailAddress(emailAddress);
    }

    /**
     * Updates the user's profile with the provided details.
     *
     * @param user User with new profile details.
     * @return Updated User object.
     * @throws InformationExistException If user matches database entry.
     * @throws InformationNotFoundException If user not found.
     */
    public User updateUserProfile(User user){
        Optional<User> userOptional = Optional.ofNullable(findUserByEmailAddress(user.getEmailAddress()));
        if(userOptional.isPresent()){ //user exists in database
            //throws error if provided profile is equal to original
            if (userOptional.get().getProfile() == user.getProfile()){
                throw new InformationExistException("Profile details are the same. No update needed.");
            }
            //updates first name if not null and different from original
            if(user.getProfile().getFirstName() != null &&
                    !String.valueOf(userOptional.get().getProfile().getFirstName()).equals(user.getProfile().getFirstName())){
                userOptional.get().getProfile().setFirstName(user.getProfile().getFirstName());
            }
            //updates bio if not null and different from original
            if (user.getProfile().getLastName() != null &&
                    !String.valueOf(userOptional.get().getProfile().getLastName()).equals(user.getProfile().getLastName())){
                userOptional.get().getProfile().setLastName(user.getProfile().getLastName());
            }
            //updates first name if not null and different from original
            if (user.getProfile().getBio() != null &&
                    !String.valueOf(userOptional.get().getProfile().getBio()).equals(user.getProfile().getBio())){
                userOptional.get().getProfile().setBio(user.getProfile().getBio());
            }
            return userRepository.save(userOptional.get());
        } else {
            throw new InformationNotFoundException("user with email address " + user.getEmailAddress() + " not found.");
        }
    }
}
