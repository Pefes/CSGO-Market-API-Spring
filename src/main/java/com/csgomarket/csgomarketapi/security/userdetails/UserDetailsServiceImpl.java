package com.csgomarket.csgomarketapi.security.userdetails;

import com.csgomarket.csgomarketapi.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.csgomarket.csgomarketapi.model.ConstansAndMessages.USER_USERNAME;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        User user = mongoTemplate.findOne(query(where(USER_USERNAME).is(username)), User.class);
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .cash(user.getCash())
                .userSettings(user.getUserSettings())
                .ownedItems(user.getOwnedItems())
                .build();
    }
}
