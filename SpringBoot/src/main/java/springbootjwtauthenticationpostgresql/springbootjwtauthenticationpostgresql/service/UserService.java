package springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.message.request.SignUpForm;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.model.Role;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.model.RoleName;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.model.User;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.repository.RoleRepository;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.repository.UserRepository;

import java.util.*;


@Service
public class UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;



    PasswordEncoder encoder;

    public UserService(UserRepository userRepository,RoleRepository roleRepository,PasswordEncoder encoder){
        this.userRepository=userRepository;
        this.roleRepository=roleRepository;
        this.encoder=encoder;
    }


    public List<User> getAllUsers(){
        // return topics;
        List<User> users=new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public boolean isAdmin(String username){

        boolean flag=false;
        if(this.userRepository.existsByUsername(username)){
            User user=this.userRepository.findByUsername(username).get();
            flag=user.getRoles().stream()
                    .filter(r->r.getName().equals(RoleName.ROLE_ADMIN))
                    .findFirst().orElse(new Role(RoleName.ROLE_USER))
                    .getName()
                    .equals(RoleName.ROLE_ADMIN);
            return flag;
        }
        return flag;

    }

    public User getUser(String username){


        return userRepository.findByUsername(username).orElse(null);
    }

    public ResponseEntity<String> createUser(SignUpForm user){
        if(userRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<String>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email is already taken!",
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User newUser = new User(user.getName(), user.getUsername(),
                user.getEmail(), encoder.encode(user.getPassword()));

        Set<String> strRoles = user.getRole();
        Set<Role> roles = new HashSet<>();

        strRoles.forEach(role -> {
            switch(role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(adminRole);

                    break;
                case "pm":
                    Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(pmRole);

                    break;
                default:
                    Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                    roles.add(userRole);
            }
        });

        newUser.setRoles(roles);
        userRepository.save(newUser);

        return ResponseEntity.ok().body("User registered successfully!");
}



    public User updateUser(String username, SignUpForm user) {

        if(userRepository.existsByUsername(username)) {
            User ExistingUser = userRepository.findByUsername(username).get();
            if (ExistingUser.getName() != user.getName()) {
                ExistingUser.setName(user.getName());
            }
            if (ExistingUser.getUsername() != user.getUsername()) {
                ExistingUser.setUsername(user.getUsername());
            }
            if (ExistingUser.getPassword() != user.getPassword()) {
                ExistingUser.setPassword(encoder.encode(user.getPassword()));
            }

            if (ExistingUser.getEmail() != user.getEmail()) {
                ExistingUser.setEmail(user.getEmail());
            }


            Set<String> Roles = user.getRole();
            Set<Role> roles = new HashSet<>();

            Roles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(adminRole);

                        break;
                    case "pm":
                        Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(pmRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                        roles.add(userRole);
                }
            });

            if (ExistingUser.getRoles() != roles) {
                ExistingUser.setRoles(roles);
            }


            userRepository.save(ExistingUser);


            return userRepository.findByUsername(ExistingUser.getUsername()).get();
        }
        else{
            return null;
        }


        }






        public User deleteUser(Long id) {
        User user=userRepository.findById(id).orElse(null);
        userRepository.deleteById(id);
        return user;

    }

}
