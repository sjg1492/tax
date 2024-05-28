package com.jobis.tax.components.user.svc;

import com.jobis.tax.components.user.dto.req.UserReq;
import com.jobis.tax.components.user.dto.req.UserSignUpReq;
import com.jobis.tax.components.user.entity.User;
import com.jobis.tax.components.user.repository.UserRepository;
import com.jobis.tax.components.user.repository.VerifiedUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerifiedUserRepository verifiedUserRepository;


    public boolean validateUser(String name, String regNo) {
        return verifiedUserRepository.existsByNameAndRegNo(name,regNo);
    }

    public boolean validateUser(UserReq userReq) {
        return userRepository.existsByUserIdAndPassword(userReq.getUserId(),userReq.getPassword());
    }
    public void insertUser(UserSignUpReq userSignUpReq) {
        User user = new User();
        user.setUserId(userSignUpReq.getUserId());
        user.setName(userSignUpReq.getName());
        user.setPassword(userSignUpReq.getPassword());
        user.setRegNo(userSignUpReq.getRegNo());
        userRepository.save(user);
    }

    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
}
