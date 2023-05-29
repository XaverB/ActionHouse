package actionhouse.api.bl.impl;

import actionhouse.api.bl.AuthenticationLogic;
import actionhouse.api.dao.CustomerRepository;
import actionhouse.api.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationLogicImpl implements AuthenticationLogic {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    @Override
    public void login(String username, String password) {
         customerRepository.findByEmail(username).orElseThrow(() -> new AuthorizationException("Login for user %s is invalid.".formatted(username)));
    }
}
