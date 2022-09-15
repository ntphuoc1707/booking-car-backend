package com.example.staff_service.service;

import com.example.staff_service.model.*;
import com.example.staff_service.repository.*;
import com.example.staff_service.security.CustomStaffDetails;
import com.example.staff_service.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * com.example.staff_service.service;
 * Created by Phuoc -19127520
 * Date 27/08/2022 - 06:03 CH
 * Description: ...
 */
@Service
public class StaffDAOImpl implements StaffDAO {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    StaffRepo staffRepo;

    @Autowired
    StaffInforRepo staffInforRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    DiscountRepository discountRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    DriverInforRepository driverInforRepository;

    PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Override
    public Result login(Staff staffLogin) {
        try {
            Staff staff=staffRepo.findStaffByUsername(staffLogin.getUsername());
            if(staff!=null){
                if(!passwordEncoder.matches(staffLogin.getPassword(),staff.getPassword())){
                    return new Result(false,null,"Wrong password");
                }
                String token= jwtTokenProvider.generateToken(staff.getId());
                staff.setToken(token);
                staffRepo.save(staff);

                Map<String, String> tokenAndRole=new HashMap<>();
                tokenAndRole.put("role",staff.getType().toString());
                tokenAndRole.put("token",token);

                return new Result(true,tokenAndRole,"Login successfully");
            }
            else return new Result(false,null,"Account doesn't exist");
        }catch (Exception e){
            return new Result(false,null,e.getMessage());
        }
    }

    @Override
    public Result signup(Staff staffSignup) {
        try {
            if(!Arrays.asList(StaffType.values()).contains(staffSignup.getType())){
                return new Result(false, null, "Staff's type doesn't exist");
            }
            else {
                Boolean state = staffRepo.existsByUsername(staffSignup.getUsername());
                if (!state){
                    staffSignup.setPassword(passwordEncoder.encode(staffSignup.getPassword()));
                    StaffInfor staffInfor=new StaffInfor();
                    staffInfor.setId(staffSignup.getId());
                    staffInfor.setStaff(staffSignup);
                    staffSignup.setStaffInfor(staffInfor);
                    staffRepo.save(staffSignup);
                }
                return new Result(state ? false : true, null, state ? "Account already exist" : "Signup successfully");
            }

        }
        catch (Exception e){

            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public String refreshToken(int id) {
        try {
            Staff staff=staffRepo.findStaffById(id);
            String token=jwtTokenProvider.generateToken(staff.getId());
            staff.setToken(token);
            staffRepo.save(staff);
            return token;
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadStaffById(int id) throws UsernameNotFoundException {
        Staff staff=staffRepo.findStaffById(id);
        if(staff==null){
            throw new UsernameNotFoundException(String.valueOf(id));
        }
        return new CustomStaffDetails(staff);
    }

    @Override
    public Result getStaffByUsername(String username) {
        try {
            Staff staff=staffRepo.findStaffByUsername(username);
            return new Result(staff != null,staff!=null?new StaffDTO(staff) :null,staff!=null?"Get staff successfully":"Staff doesn't exist");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getAllUser() {
        try {
            List<User> users=userRepo.findAll();
            List<UserDTO> UserDTOS =new ArrayList<>();
            for(User user: users) UserDTOS.add(new UserDTO(user));
            return new Result(true, UserDTOS,"Get users successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getAllDriver(){
        try {
            List<Driver> drivers=driverRepository.findAll();
            List<DriverDTO> driverDTOS=new ArrayList<>();
            for(Driver driver: drivers) driverDTOS.add(new DriverDTO(driver));
            return new Result(true, driverDTOS,"Get drivers successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    public Result getAllStaff(){
        try {
            List<Staff> staffs = staffRepo.findAll();
            List<StaffDTO> staffDTOS=new ArrayList<>();
            for(Staff stf:staffs) staffDTOS.add(new StaffDTO(stf));
            return new Result(true, staffDTOS, "Get staff successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }

    @Override
    public Result addDiscount(Discount discount) {
        try{
            discountRepository.save(discount);
            return new Result(true, null, "Add discount successfully");
        }catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result editDiscount(Discount discount){
        try {
            Discount foundDiscount =  discountRepository.findByDiscountId(discount.getDiscountId());
            foundDiscount.setDiscountName(discount.getDiscountName());
            foundDiscount.setDiscountPercent(discount.getDiscountPercent());
            foundDiscount.setQuantity(discount.getQuantity());
            foundDiscount.setStartDate(discount.getStartDate());
            foundDiscount.setEndDate(discount.getEndDate());
            discountRepository.saveAndFlush(foundDiscount);
            return new Result(true, null, "Edit discount successfully");
        }catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getAllDiscount() {
        try{
            List<Discount> discounts=discountRepository.findAll();
            return new Result(true, discounts, "Get all discount successfully");
        }catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Transactional
    @Override
    public Result deleteDiscountById(int discountId) {
        try{
            if(discountRepository.existsDiscountByDiscountId(discountId)) {
                discountRepository.deleteByDiscountId(discountId);
                return new Result(true, null, "Delete discount successfully");
            }
            else return new Result(false, null,"Discount doesn't exist");
        }catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result booking(Request request) {
        try {
            request.setStatus("Finding Driver...");
            request.setTrip(null);
            requestRepository.save(request);
            return new Result(true,request,"Request vehicle successfully and receive price");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }



    @Override
    public Result getAllRequest() {
        try {
            List<Request> requests=requestRepository.findAll();
            return new Result(true, requests,"Get all request successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getRequestVehicle() {
        try {
            List<Vehicle> vehicles=vehicleRepository.findVehicleByStatus(false);

            return new Result(true, vehicles,"Get all request adding vehicle");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result acceptVehicle(String licensePlateNum) {
        try {
            Vehicle vehicle=vehicleRepository.findVehicleByLicensePlateNum(licensePlateNum);
            if(vehicle==null) return new Result(false,null,"Vehicle doesn't exist");
            vehicle.setStatus(true);
            vehicleRepository.save(vehicle);
            return new Result(true,null,"Accept vehicle "+licensePlateNum);
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
    public Result getVehicleOfDriver(int driverId) {
        try {
            Driver driver=driverRepository.findDriverByDriverId(driverId);
            if(driver==null) return new Result(false, null,"Driver doesn't exist");
            return new Result(true, driver.getDriverInfor().getVehicleList(),"Get vehicle of driver "+driverId+" successfully!");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getAllVehicle(){
        try {
            List<Vehicle> vehicles=vehicleRepository.findAllByStatus(true);
            String driverName = "";
            List<vehicleDTO> vehicleDTOS = new ArrayList<>();
            for (Vehicle vehicle : vehicles) {
                driverName = driverRepository.findDriverByDriverId(vehicle.getDriverId()).getDriverInfor().getDriverName();
                vehicleDTO vehicleDTO = new vehicleDTO(vehicle, driverName);
                vehicleDTOS.add(vehicleDTO);
            }
            return new Result(true,vehicleDTOS,"Get vehicle successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result changePassword(String username, String password, String rePassword){
        try {
            Staff staff=staffRepo.findStaffByUsername(username);
            if(staff!=null&& passwordEncoder.matches(password,staff.getPassword())){
                staff.setPassword(passwordEncoder.encode(rePassword));
                staffRepo.save(staff);
                return new Result(true,null,"Change password successfully");
            }
            else return new Result(false,null,"Wrong username or password");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }

    @Override
    public Staff getStaffById(int id) {
        try {
            Staff staff=staffRepo.findStaffById(id);
            return staff;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public Result editStaffInfo(String token, StaffInfor editInfor) {
        try {
            if(!staffRepo.existsById(editInfor.getId())){
                return new Result(false,null,"Cannot find staff");
            }
            if(jwtTokenProvider.getIdFromToken(token)!=editInfor.getId()){
                return new Result(false,null,"Out of your scope");
            }
            staffInforRepo.save(editInfor);
            return new Result(true, null, "Edit successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result deleteStaffById(int staffId){
        try {
            if(staffRepo.existsById(staffId)){
                staffRepo.deleteById(staffId);
                //staffInforRepository.deleteById(staffId);
                return new Result(true, null, "Delete staff successfully");

            }
            return new Result(true, null, "Delete staff successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result getAllTrip() {
        try {
            List<Trip> trips=tripRepository.findAll();
            return new Result(true,trips,"Get all trip successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }

    }

    @Override
    public Result get5latestTripForUser(int userId) {
        try {
            List<Trip> trips=tripRepository.findTop5ByUserIdOrderByTripIdDesc(userId);
            return new Result(true,trips,"");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
//        List<Trip> trips=tripRepository.findAllByUserId(userId);
//        if(trips.size()<=5) return new Result(true,trips,"Get 5 latest trip");
//        List<Trip> temp=new ArrayList<>();
//        for(int i=0;i<5;i++) temp.add(trips.get(i));
//        return new Result(true,temp,"Get 5 latest trip ");

    }

    @Override
    public Result getAllTripByDriver(int driverId) {
        try {
            List<Trip> trips=tripRepository.findAllByDriverId(driverId);
            return new Result(true,trips,"Get all trip successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

    @Override
    public Result get5mostDesByUser(int userId) {
        try {
            List<Address> addresses=addressRepository.findTop5ByUserIdOrderByPickNumDesc(userId);
            return new Result(true,addresses,"Get 5 most destination successfully");
        }
        catch (Exception e){
            return new Result(false, null, e.getMessage());
        }
    }

}
