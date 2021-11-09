package com.csgomarket.csgomarketapi.service;

import com.csgomarket.csgomarketapi.model.user.User;
import com.csgomarket.csgomarketapi.payload.request.authentication.AuthenticationRequest;
import com.csgomarket.csgomarketapi.payload.response.ApiResponse;
import com.csgomarket.csgomarketapi.payload.response.authentication.LoginResponse;
import com.csgomarket.csgomarketapi.payload.response.authentication.UserData;
import com.csgomarket.csgomarketapi.security.jwt.JwtUtils;
import com.csgomarket.csgomarketapi.security.userdetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.*;
import static com.csgomarket.csgomarketapi.util.GetApiResponse.getApiResponse;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class AuthenticationService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${jwtExpirationMs}")
    private String tokenExpirationTime;

    public ApiResponse<?> register(AuthenticationRequest request) {
        if (userExistsByUsername(request.getUsername())) {
            return getApiResponse(FAIL, MESSAGE_USERNAME_ALREADY_EXISTS, null);
        }

        String encodedPassword = encoder.encode(request.getPassword());
        mongoTemplate.save(User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .build());
        return getApiResponse(SUCCESS, null, null);
    }

    private boolean userExistsByUsername(String username) {
        return mongoTemplate.findOne(query(where(USER_USERNAME).is(username)), User.class) != null;
    }

    public ApiResponse<LoginResponse> login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        LoginResponse response = LoginResponse.builder()
                .accessToken(jwt)
                .expiresIn(tokenExpirationTime)
                .userData(new UserData(userDetails.getUsername(), userDetails.getCash(), userDetails.getSettings()))
                .build();

        return getApiResponse(SUCCESS, null, response);
    }
}
