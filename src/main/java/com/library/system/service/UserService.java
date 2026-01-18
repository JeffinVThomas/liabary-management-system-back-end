package com.library.system.service;

import java.util.Optional;
import com.library.system.model.User;

/**
 * Service interface for managing user-related operations
 * such as registration, login, OTP handling, and password updates.
 */
public interface UserService {


    User register(User user);


    User login(String email, String password);


    User loginWithRole(String email, String password, String role);


    void updatePassword(String email, String newPassword);


    Optional<User> findByEmail(String email);


    Optional<User> findByMobile(String mobile);

 
    boolean existsByEmail(String email);


    boolean existsByMobile(String mobile);


    boolean existsByRole(String role);
    
    // Sends a new OTP to the given mobile number using SMS.
    
    // mobile the recipient mobile number
    
   void sendOtp(String mobile);

    
     // Saves an OTP and its generated time for the given mobile number.
     
     // mobile the mobile number to associate with the OTP
     // otp the one-time password to save
    
    void saveOtp(String mobile, String otp);

    
     // Verifies if the provided OTP is valid and not expired.
     
     // mobile the mobile number associated with the OTP
     // otp the OTP to verify
     // @return true if valid, false if expired or incorrect
    
    boolean verifyOtp(String mobile, String otp);

}
