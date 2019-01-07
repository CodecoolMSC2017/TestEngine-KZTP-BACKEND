package com.kztp.testengine.service;

import com.kztp.testengine.exception.*;
import com.kztp.testengine.model.*;
import com.kztp.testengine.repository.PasswordTokenRepository;
import com.kztp.testengine.repository.UserRepository;
import com.kztp.testengine.repository.UsertokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.naming.InvalidNameException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
public final class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder pwEncoder;

    @Autowired
    private UsertokenRepository usertokenRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private UsersTestService usersTestService;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    public User getUserByUsername(String username) throws IllegalArgumentException {
       /* if (!userDetailsManager.userExists(username)) {
            throw new IllegalArgumentException("There is no user with this name.");
        }
        return userRepository.findByUsername(username);*/
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException(username + " does not found!");
        }

        return user;
    }

    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User createUser(String email,String username,String password,String confirmationPassword) throws MessagingException {
        if(isEveryInputValid(email,username,password,confirmationPassword)) {
            userDetailsManager.createUser(new org.springframework.security.core.userdetails.User(
                    username,
                    pwEncoder.encode(password),
                    AuthorityUtils.createAuthorityList("ROLE_INACTIVE")));
        }
        User user = userRepository.findByUsername(username);
        user.setEmail(email);
        user.setRank("newbie");
        userRepository.save(user);

        LocalDateTime localDateTime = LocalDateTime.now();
        Usertoken usertoken = new Usertoken();
        usertoken.setActivated(false);
        usertoken.setActivationTime(java.sql.Date.valueOf(localDateTime.toLocalDate()));
        usertoken.setToken(tokenService.generateToken());
        usertoken.setUser(user);
        usertokenRepository.save(usertoken);

        mailService.sendEmail(user,"Please verify your email","<h1>Test engine</h1> <br>  <h2> Dear "+username+",</h2> <br> Activate your email: <br>  <a href=localhost:4200/activate?token="+usertoken.getToken()+">Activate</a> <br> localhost:4200/activate?token="+usertoken.getToken());
        return user;
    }

    public void reSendVerificationEmail(String email) throws UserException, TokenException, MessagingException {
        User user = getUserByEmail(email);
        if(user == null) {
            throw new UserException("User with this email address not found.");
        }
        if(user.getUserToken().isActivated()) {
            throw new TokenException("User already activated.");
        }
        mailService.sendEmail(user,"Please verify your email.","<h1>Test engine</h1> <br>  <h2> Dear "+user.getUsername()+",</h2> <br> Activate your email by pasting this verification code: <br> " + user.getUserToken().getToken() + " <br> or by clicking the verification link <br> <a href='localhost:4200/activate?token="+user.getUserToken().getToken()+"'>Activate</a> <br> ");
    }

    public User createAdmin(String username,String email,String password,String confirmationPassword) throws UnauthorizedRequestException {
        User currentUser = getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currentUser.getAuthorities().contains("ROLE_ADMIN")) {
            if (isEveryInputValid(email, username, password, confirmationPassword)) {
                userDetailsManager.createUser(new org.springframework.security.core.userdetails.User(
                        username,
                        pwEncoder.encode(password),
                        AuthorityUtils.createAuthorityList("ADMIN_ROLE")));
            }
            User user = userRepository.findByUsername(username);
            user.setEmail(email);
            user.setRank("elite");
            userRepository.save(user);
        }else {
            throw new UnauthorizedRequestException("You don't have the authority for this action.");
        }
        return userRepository.findByUsername(username);
    }
    public void changePassword(int userId,String oldPassword, String newPassword) {
        User user = userRepository.findById(userId);
        if (oldPassword.equals(pwEncoder.encode(oldPassword))) {
            userDetailsManager.changePassword(oldPassword,pwEncoder.encode(newPassword));
        }
        userRepository.save(user);
    }

    public void changePassword(ResetPassword resetPassword) throws PasswordException, UserException {
        String token = resetPassword.getToken();
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token);
        if(!resetPassword.getPassword().equals(resetPassword.getPassword2())){
            throw new PasswordException("The two passwords are different");
        }
        if(passwordToken.getUser() == null){
            throw new UserException("Bad token!");
        }
        User user = passwordToken.getUser();
        user.setPassword(pwEncoder.encode(resetPassword.getPassword()));
        userRepository.save(user);

        passwordTokenRepository.delete(passwordToken);



    }

    private boolean isEmailTaken(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return false;
        }

        return true;
    }


    public boolean isAdmin(int id) {
        User user = userRepository.findById(id);
        if (user.getAuthorities().contains("ADMIN_ROLE")) {
            return true;
        }

        return false;
    }


    private boolean isEveryInputValid(String email,String username,String password,String confirmationPassword) throws IllegalArgumentException {
        if(password.length() < 8 ) {
            throw new IllegalArgumentException("Password is too short.Enter minimum 8 characters");
        }

        if(email.length() == 0 || !email.contains("@")) {
            throw new IllegalArgumentException("Email is null or invalid.");
        }

        if (isEmailTaken(email)) {
            throw new IllegalArgumentException("This email address already exists, please choose another one!");
        }

        if (email.equals("") || password.equals("")) {
            throw new IllegalArgumentException("Fill out each inputs!");
        }

        if (userDetailsManager.userExists(username)) {
            throw new IllegalArgumentException("This username is already taken,please choose another one.");
        }

        if (!password.equals(confirmationPassword)) {
            throw new IllegalArgumentException("Password and confirmation password doesn't match.");
        }
        return true;
    }

    public void rankUpUser(User user) throws UserException {
        if(user.getRank().equals("elite")) {
            throw new UserException("Your rank is already elite,can't get higher rank.");
        }
        if(user.getRank().equals("user")) {
            user.setRank("elite");
            userRepository.save(user);
        }
        if (user.getRank().equals("newbie")) {
            user.setRank("user");
            userRepository.save(user);
        }

    }

    public int getUserProgress() {
        User user = getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UsersTest> usersTests = usersTestService.getLoggedUserCompletedTests();
        int testCount =0;
        if (user.getRank().equals("user")) { //100test 70%
            int testAbove70Count =0;
            for (UsersTest test:usersTests) {
                testCount++;
                if(test.getPercentage() >= 70) {
                    testAbove70Count++;
                }
            }
            return testAbove70Count;
        }
        else if (user.getRank().equals("newbie")) { //15test 50%
            int testAbove50Count =0;
            for (UsersTest test:usersTests) {
                testCount++;
                if(test.getPercentage() >= 50) {
                    testAbove50Count++;
                }
            }
            return testAbove50Count;
        }
        else {
            return 100;
        }
    }

    public User getLoggedUser(){
        return getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void activateUser(String token) throws TokenException {
        if (token.equals("")) {
            throw new TokenException("Enter token.");
        }
        Usertoken userToken = usertokenRepository.findByToken(token);
        if(userToken == null) {
            throw new TokenException("Invalid activation token.");
        }
        User user =userToken.getUser();
        if(userToken.isActivated()) {
            throw new TokenException("User already activated.");
        }
        user.getAuthorities().set(0, "ROLE_USER");
        userRepository.save(user);
        userToken.setActivated(true);
        usertokenRepository.save(userToken);
    }

    public void requestPasswordReset(String email) throws UserException, MessagingException {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("Email not found");
        }
        String token = tokenService.generateToken();
        PasswordToken passwordToken = new PasswordToken();
        passwordToken.setUser(user);
        passwordToken.setToken(token);
        LocalDateTime localDateTime = LocalDateTime.now();
        passwordToken.setExpirationDate(java.sql.Date.valueOf(localDateTime.toLocalDate().plusDays(1)));
        passwordTokenRepository.save(passwordToken);

        mailService.sendEmail(user,"Password reset","localhost:4200/resetpw?token="+token);
    }


}
