package com.example.user_service.service;

import com.example.user_service.model.*;
import com.example.user_service.repository.*;
import com.example.user_service.security.CustomUserDetails;
import com.example.user_service.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * com.example.user_service.service;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:38 SA
 * Description: ...
 */
@Service
public class UserDAOImpl implements UserDAO {

    @Autowired
    UserRepo userRepo;

    @Autowired
    UserInforRepo userInforRepo;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    PathOnFBRepo pathOnFBRepo;

    PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Override
    public Result login(User userLogin) {
        try {
            User user=userRepo.findUserByUsername(userLogin.getUsername());
            if (user != null) {
                if(!passwordEncoder.matches(userLogin.getPassword(),user.getPassword())) {
                    return new Result(false, null, "Wrong password");
                }
                String token = jwtTokenProvider.generateToken(user.getId());
                user.setToken(token);
                userRepo.save(user);
                return new Result(true, token, "Login successfully");
            }
            else return new Result(false, null, "Account doesn't exist");
        }
        catch (Exception e){
            e.printStackTrace();
            return new Result(false,null,e.getMessage());
        }
    }

    @Override
    public Result signup(User userSignup) {
        try {
            System.out.println(userSignup);
            Boolean isExist=userRepo.existsByUsername(userSignup.getUsername());
            if(!isExist) {
                userSignup.setPassword(passwordEncoder.encode(userSignup.getPassword()));
                UserInfor userInfor=userSignup.getUserInfor();
                if(userInfor==null) userInfor=new UserInfor();
                //userInfor.setId(userSignup.getId());
                userInfor.setPhoneNumber(userSignup.getUsername());
                userInfor.setFullName(userSignup.getUserInfor().getFullName());
                userInfor.setHomeAddress(userSignup.getUserInfor().getHomeAddress());
                userInfor.setGender(userSignup.getUserInfor().getGender());
                userInfor.setUser(userSignup);

                userSignup.setUserInfor(userInfor);
                userRepo.save(userSignup);

                Wallet wallet=new Wallet();
                wallet.setWalletType(WalletType.USER);
                wallet.setIdOwner(userSignup.getId());
                wallet.setBalance(0.0);
                wallet.setStatus(true);
                walletRepository.save(wallet);
            }
            return new Result(!isExist,null,isExist?"Account already exist":"Signup successfully");
        }
        catch (Exception e){
            e.printStackTrace();
            return new Result(false,null,e.getMessage());
        }
    }

    @Override
    public String refreshToken(int id) {
        try {
            User user=userRepo.findUserById(id);
            String token=jwtTokenProvider.generateToken(user.getId());
            user.setToken(token);
            userRepo.save(user);
            return token;
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public Boolean checkId(int id) {
        return userRepo.existsById(id);
    }

    @Override
    public Result getListDiscount() {
        try{
            return new Result(true, discountRepository.findAll(), "Get list discount successfully");
        }catch (Exception e){

            return new Result(false,null,e.getMessage());
        }
    }

    @Override
    public Result getWallet(String token) {
        try {
            int userId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
            Wallet wallet=walletRepository.findByIdOwnerAndWalletType(userId,WalletType.USER);
            return new Result(wallet != null,wallet,wallet==null?"Wallet doesn't exist":"Get wallet successfully");
        }
        catch (Exception e){

            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result recharge(String token, double money) {
        try {
            if(money>0){
                int userId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
                Wallet wallet=walletRepository.findByIdOwnerAndWalletType(userId,WalletType.USER);
                if(wallet !=null){
                    wallet.setBalance(wallet.getBalance()+money);
                    walletRepository.save(wallet);
                    return new Result(true,null,"Recharge successfully");
                }
                return new Result(false,null,"Wallet doesn't exist");
            }
            return new Result(false,null,"The amount deposited into the account is invalid");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result withdraw(String token, double money) {
        try {
            if(money>0){
                int userId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
                Wallet wallet=walletRepository.findByIdOwnerAndWalletType(userId,WalletType.USER);
                if(wallet !=null){
                    double newBalance=wallet.getBalance()-money;
                    if(newBalance>=0) {
                        wallet.setBalance(newBalance);
                        walletRepository.save(wallet);
                        return new Result(true, null, "Withdraw successfully");
                    }
                    return new Result(false,null,"Cannot withdraw an amount larger than the balance");
                }
                return new Result(false,null,"Wallet doesn't exist");
            }
            return new Result(false,null,"The amount deposited into the account is invalid");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getLinkAfterBooking(int requestId) {
        try {
            PathOnFirebase pathOnFirebase=pathOnFBRepo.findByTypeAndAndIdRrD("Trip",requestId);
            if(pathOnFirebase==null) return new Result(false,null,"Link doesn't exist");
            return new Result(true,pathOnFirebase.getPath(),"Get link successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findUserByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }

    @Override
    public UserDetails loadUserById(int id) throws UsernameNotFoundException {
        User user=userRepo.findUserById(id);
        if(user==null){
            throw new UsernameNotFoundException(String.valueOf(id));
        }
        return new CustomUserDetails(user);
    }

    @Override
    public Result booking(int userId, Request request) {
        try {
            if(request.getPaymentType().equals(PaymentType.E_WALLET)) {
                Wallet wallet = walletRepository.findByIdOwnerAndWalletType(userId, WalletType.USER);
                if (wallet.getBalance() < request.getVehicleAndPrice().getPrice())
                    return new Result(false, null, "Balance doesn't enough");
                wallet.setBalance(wallet.getBalance()-request.getVehicleAndPrice().getPrice());
                walletRepository.save(wallet);
            }
            request.setUserId(userId);
            request.setStatus("Finding Driver...");
            UserInfor userInfor=userInforRepo.findUserInforById(userId);
            request.setUserInfor(userInfor);
            requestRepository.save(request);

            return new Result(true,request,"Booking successfully, please wait our driver");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getVehicleAndPrice(DistanceAndTime distanceAndTime) {
        try {
            ResponseBooking responseBooking=new ResponseBooking(distanceAndTime.getDistance(), distanceAndTime.getTimeSecond());
            return new Result(true,responseBooking,"Get vehicle and price successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getUserInforByToken(String token) {
        try {
            int userId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
            //UserInfor userInfor=userInforRepository.findUserInforById(userId);
            User user=userRepo.findUserById(userId);
            if(user!=null) return new Result(true, new UserDTO(user),"Get infor user successfully");
            else return new Result(false,null,"User doesn't exist");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getUserByToken(String token) {
        try {
            int userId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
            User user=userRepo.findUserById(userId);
            if(user!=null) return new Result(true, user,"Get user successfully");
            else return new Result(false,null,"User doesn't exist");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result cancelBooking(int requestId) {
        try {
            Request request=requestRepository.findRequestByRequestId(requestId);
            if(request==null) return new Result(false, null, "Request doesn't exist");
            request.setStatus("Canceled");
            requestRepository.save(request);
            return new Result(true, null,"Cancel booking successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public User getUserById(int id) {
        return userRepo.findUserById(id);
    }

    @Override
    public Result getUserByUsername(String username) {
        try {
            User user=userRepo.findUserByUsername(username);
            return new Result(user != null,user!=null?user.getUserInfor():null,user!=null?"Get user successfully":"User doesn't exist");
        }
        catch (Exception e){
//            Log log = new Log();
//            log.setLogMessage(e.getMessage());
//            log.setSource(this.toString());
//            log.setTime(LocalDateTime.now().toString());
//            logRepository.save(log);
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result checkPhoneNumber(String phoneNumber) {
        try {
            Boolean isExist = userRepo.existsByUsername(phoneNumber);
            if(!isExist) {
                return new Result(false,null,"Phone number doesn't exist");
            }
            return new Result(true,null,"Phone number existed");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result checkEmail(String email) {
        try {
            Boolean isExist = userInforRepo.existsByEmail(email);
            if(!isExist) {
                return new Result(false,null,"Email doesn't exist");
            }
            return new Result(true,null,"Email existed");
        }
        catch (Exception e){
            return new Result(false,null,e.getMessage());
        }
    }


}
