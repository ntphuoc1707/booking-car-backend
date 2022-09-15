package com.example.driver_service.service;

import com.example.driver_service.model.*;
import com.example.driver_service.repository.*;
import com.example.driver_service.security.CustomDriverDetails;
import com.example.driver_service.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * com.example.user_service.service;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 10:38 SA
 * Description: ...
 */
@Service
public class DriverDAOImpl implements DriverDAO {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    DriverInforRepo driverInforRepo;

    @Autowired
    VehicleRepo vehicleRepo;

    @Autowired
    WalletRepository walletRepo;

    @Autowired
    TripRepository tripRepo;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Override
    public Result login(Driver driverLogin) {
        try {
            Driver driver=driverRepo.findDriverByUsername(driverLogin.getUsername());
            if (driver != null) {
                if(!passwordEncoder.matches(driverLogin.getPassword(),driver.getPassword())) {
                    return new Result(false, null, "Wrong password");
                }
                String token = jwtTokenProvider.generateToken(driver.getId());
                driver.setToken(token);
                driverRepo.save(driver);
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
    public Result signup(Driver driverSignup) {
        try{
            Boolean isExist=driverRepo.existsByUsername(driverSignup.getUsername());
            if(!isExist){
                driverSignup.setPassword(passwordEncoder.encode(driverSignup.getPassword()));
                System.out.println(driverSignup.toString());
                DriverInfor driverInfor=driverSignup.getDriverInfor();
                System.out.println(driverInfor.toString());
                List<Vehicle> vehicles=new ArrayList<>();
                if(driverInfor==null) driverInfor=new DriverInfor();
                else{
                    if(driverInforRepo.existsByCitizenId(driverInfor.getCitizenId())){
                        return new Result(false,null,"CitizenID already registered");
                    }

                    vehicles=driverInfor.getVehicleList();
                    System.out.println(vehicles.size());
                    for(Vehicle vehicle: vehicles){
                        if(vehicleRepo.existsByLicensePlateNum(vehicle.getLicensePlateNum()))
                            return new Result(false,null,"Vehicle already registered");
                    }
                }

                driverInfor.setId(driverSignup.getId());
                driverInfor.setPhoneNumber(driverSignup.getUsername());
////            driverInfor.setVehicleList(null);
                driverInfor.setDriver(driverSignup);
//                driverInfor.setTrips(null);

                driverSignup.setDriverInfor(driverInfor);
                driverRepo.save(driverSignup);
//            System.out.println(vehicles.size());
                for(Vehicle vehicle:vehicles){
                    System.out.println(vehicle.toString());
                    vehicle.setDriverId(driverSignup.getId());
                    vehicle.setStatus(false);
                    vehicleRepo.save(vehicle);
                }

//                Wallet wallet=new Wallet();
//                wallet.setWalletType(WalletType.DRIVER);
//                wallet.setIdOwner(driverSignup.getId());
//                wallet.setBalance(0.0);
//                wallet.setStatus(true);
//                walletRepository.save(wallet);

            }
            return new Result(!isExist,null,isExist?"Account already exist":"Signup successfully");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,null,e.getMessage());
        }
    }


    @Override
    public String refreshToken(int id) {
        try {
            Driver driver=driverRepo.findDriverById(id);
            String token=jwtTokenProvider.generateToken(driver.getId());
            driver.setToken(token);
            driverRepo.save(driver);
            return token;
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Driver driver=driverRepo.findDriverByUsername(username);
        if(driver==null){
            throw new UsernameNotFoundException(username);
        }
        return new CustomDriverDetails(driver);
    }

    @Override
    public UserDetails loadDriverById(int id) throws UsernameNotFoundException {
        Driver driver=driverRepo.findDriverById(id);
        if(driver==null){
            throw new UsernameNotFoundException(String.valueOf(id));
        }
        return new CustomDriverDetails(driver);
    }

    @Override
    public Result getDriverByUsername(String username) {
        try {
            Driver driver=driverRepo.findDriverByUsername(username);
            return new Result(driver != null,driver!=null?driver.getDriverInfor():null,driver!=null?"Get driver successfully":"Driver doesn't exist");
        }
        catch (Exception e){
            return  new Result(false,null,e.getMessage());
        }
    }

    @Override
    public Result addVehicle(int driverId, Vehicle vehicle) {
        try{
            Boolean isExistDriver=driverRepo.existsById(driverId);
            if(!isExistDriver) return new Result(false,null,"Driver doesn't exist");

            Boolean isExistVehicleWithDriver=vehicleRepo.existsByDriverIdAndLicensePlateNum(driverId,vehicle.getLicensePlateNum());
            if(isExistVehicleWithDriver) return new Result(false,null,"Driver already has this vehicle");

            Boolean isExistVehicle=vehicleRepo.existsVehicleByLicensePlateNum(vehicle.getLicensePlateNum());
            if(isExistVehicle) return new Result(false,null,"Vehicle already registered");

            vehicle.setDriverId(driverId);
            vehicle.setStatus(false);
            vehicleRepo.save(vehicle);

            return new Result(true,null,"Add vehicle successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getVehicle(int driverId) {
        try {
            List<Vehicle> vehicles=vehicleRepo.findVehicleByDriverId(driverId);
            return new Result(true, vehicles,"Get vehicle successfully");
        }catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Driver getDriverById(int id) {
        try {
            Driver driver=driverRepo.findDriverById(id);
            return driver;
        }
        catch (Exception e){
            return null;
        }
    }

    @Override
    public Result isExistPhoneNumber(String phoneNumber){
        try {
            if(driverRepo.existsByUsername(phoneNumber)){
                return new Result(true,null,"Phone number existed");
            }
            return new Result(false,null,"Phone number oke");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result checkDriverInfo(DriverInfor driverInfor){
        try {
            List<String> error=new ArrayList<>();

            if(driverInforRepo.existsByCitizenId(driverInfor.getCitizenId())){
                error.add("Driver existed");
                /// return new Result(false,null,"Driver existed");
            }
            if(driverInforRepo.existsByEmail(driverInfor.getEmail())){
                error.add("Email existed");
                //return new Result(false,null,"Email existed");
            }
            if(driverInforRepo.existsByDriverLicenseId(driverInfor.getDriverLicenseId())){
                error.add("License is being used");
                //return new Result(false,null,"License is being used");
            }
            if(error.isEmpty()) return new Result(true,null,"Driver oke to register");
            else return new Result(false,error,"Have some error");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result checkVehiclePlateNum(String vehiclePlateNum){
        try {
            if(vehicleRepo.existsVehicleByLicensePlateNum(vehiclePlateNum)){
                return new Result(true, null, "Vehicle already existed");
            }
            return new Result(false, null, "Vehicle oke for register");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getDriverByToken(String token) {
        try {
            int driverId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
            // DriverInfor driverInfor=driverInforRepo.findDriverInforByDriverId(driverId);
            Driver driver=driverRepo.findDriverById(driverId);
            if(driver!=null) return new Result(true, new DriverDTO(driver),"Get driver successfully");
            else return new Result(false,null,"Driver doesn't exist");
        }catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }
    @Override
    public Result getWallet(String token) {
        try {
            int driverId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
            Wallet wallet=walletRepo.findByIdOwnerAndWalletType(driverId,WalletType.DRIVER);
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
                int driverId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
                Wallet wallet=walletRepo.findByIdOwnerAndWalletType(driverId,WalletType.DRIVER);
                if(wallet !=null){
                    wallet.setBalance(wallet.getBalance()+money);
                    walletRepo.save(wallet);
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
                int driverId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
                Wallet wallet=walletRepo.findByIdOwnerAndWalletType(driverId,WalletType.DRIVER);
                if(wallet !=null){
                    double newBalance=wallet.getBalance()-money;
                    if(newBalance>=0) {
                        wallet.setBalance(newBalance);
                        walletRepo.save(wallet);
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

    public int getNumberFromRate(RateStar rateStar){
        switch (rateStar.toString()){
            case "ONE" -> {
                return 1;
            }
            case "TWO" -> {
                return 2;
            }
            case "THREE" -> {
                return 3;
            }
            case "FOUR" -> {
                return 4;
            }
            case "FIVE" -> {
                return 5;
            }
        }
        return 0;
    }

    @Override
    public Result getFeedBacks(String token){
        try {
            int driverId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
            float rate = 0;
            List<Trip> trips = tripRepo.findAllByDriverId(driverId);
            List<Feedback> feedbacks = new ArrayList<>();
            for (Trip trip: trips) {
                feedbacks.add(trip.getFeedback());
                rate += getNumberFromRate(trip.getFeedback().getRateStar());
            }
            rate /= (trips.size()*1.0);
            return new Result(true, feedbacks, Float.toString(rate));
        }catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }

    @Override
    public Result getTrips(String token){
        try {
            int driverId= Math.toIntExact(jwtTokenProvider.getIdFromToken(token));
            List<Trip> trips = tripRepo.findAllByDriverId(driverId);
            return new Result(true, trips, "Get trips successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }
}
