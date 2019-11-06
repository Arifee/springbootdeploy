package springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.model;


import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.message.request.SignUpForm;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.repository.RoleRepository;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.repository.UserRepository;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.service.UserService;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder encoder;

    private User user1 = new User();

    private User user2 = new User();
    private  SignUpForm sign_user=new SignUpForm();

   private Optional<Role> userRole;
   private Optional<Role> adminRole;
   private Optional<Role> pmRole;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        user1.setId(Long.parseLong("98"));
        Set<Role> roles=new HashSet<>();
        roles.add(new Role(RoleName.ROLE_USER));
        user1.setRoles(roles);
        sign_user.setEmail("jack.smith@hotmail.com");
        sign_user.setName("jack");
        sign_user.setPassword("jack123");
        sign_user.setUsername("jacksmith");
        Set<String> role=new HashSet<>();
        role.add("user");
        sign_user.setRole(role);

        Role userR=new Role(RoleName.ROLE_USER);
        Role adminR=new Role(RoleName.ROLE_ADMIN);
        Role pmR=new Role(RoleName.ROLE_PM);
        userRole=Optional.of(userR);
        adminRole=Optional.of(adminR);
        pmRole=Optional.of(pmR);


        user2.setEmail("arife@hotmail.com");
        user2.setName("arife");
        user2.setPassword("arife123");
        user2.setUsername("arife");
        Set<Role> r=new HashSet<>();
        r.add(new Role(RoleName.ROLE_USER));
        user2.setRoles(r);
    }
    @Test
    public void get_user_when_username_exist_in_database_test(){
        Optional<User> userr=Optional.of(user1);
        when(userRepository.findByUsername("jacksmith")).thenReturn(userr);
        assertThat(userService.getUser("jacksmith")).isNotNull();
    }
    @Test
    public void get_user_when_username_dont_exist_in_database_test(){
        Optional<User> userr=Optional.of(user1);
        when(userRepository.findByUsername("jacksmith")).thenReturn(userr);
        assertThat(userService.getUser("jfcgfht")).isNull();
    }
    @Test
    public void creating_user_when_username_and_password_dont_exist_in_database_test(){//then we can create user
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        roles.add(new Role(RoleName.ROLE_USER));
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);
        sign_user.setEmail("jack.smith@hotmail.com");
        sign_user.setName("jack");
        sign_user.setPassword("jack123");
        sign_user.setUsername("jacksmith");
        Set<String> role=new HashSet<>();
        role.add("user");
        sign_user.setRole(role);
        Role userR=new Role(RoleName.ROLE_USER);
        Role adminR=new Role(RoleName.ROLE_ADMIN);
        Role pmR=new Role(RoleName.ROLE_PM);
        userRole=Optional.of(userR);
        adminRole=Optional.of(adminR);
        pmRole=Optional.of(pmR);
        when(userRepository.existsByUsername(userr.get().getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userr.get().getEmail())).thenReturn(false);
        when(userRepository.save(userr.get())).thenReturn(userr.get());
        when(encoder.encode(userr.get().getPassword())).thenReturn(userr.get().getPassword());
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(userRole);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
        when(roleRepository.findByName(RoleName.ROLE_PM)).thenReturn(pmRole);
        assertThat(userService.createUser(sign_user)).isEqualTo(ResponseEntity.ok().body("User registered successfully!"));
    }



    @Test
    public void creating_user_when_username_exist_in_database_test() {
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        roles.add(new Role(RoleName.ROLE_USER));
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);
        sign_user.setEmail("jack.smith@hotmail.com");
        sign_user.setName("jack");
        sign_user.setPassword("jack123");
        sign_user.setUsername("jacksmith");
        Set<String> role=new HashSet<>();
        role.add("user");
        sign_user.setRole(role);
        Role userR=new Role(RoleName.ROLE_USER);
        Role adminR=new Role(RoleName.ROLE_ADMIN);
        Role pmR=new Role(RoleName.ROLE_PM);
        userRole=Optional.of(userR);
        adminRole=Optional.of(adminR);
        pmRole=Optional.of(pmR);
        when(userRepository.existsByUsername(userr.get().getUsername())).thenReturn(true);
        when(userRepository.existsByEmail(userr.get().getEmail())).thenReturn(false);
        when(userRepository.save(userr.get())).thenReturn(userr.get());
        when(encoder.encode(userr.get().getPassword())).thenReturn(userr.get().getPassword());
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(userRole);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
        when(roleRepository.findByName(RoleName.ROLE_PM)).thenReturn(pmRole);
        assertThat(userService.createUser(sign_user)).isEqualTo(new ResponseEntity<String>("Fail -> Username is already taken!",
                HttpStatus.BAD_REQUEST));
    }


    @Test
    public void get_all_users_test(){
        List<User> users=new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userRepository.findAll()).thenReturn(users);
        assertThat(userService.getAllUsers()).isNotNull();
    }

    @Test
    public void get_all_users_when_database_is_empty_test(){
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        assertThat(userService.getAllUsers()).isEmpty();
    }
    @Test
    public void creating_user_when_email_exist_in_database_test() {
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        roles.add(new Role(RoleName.ROLE_USER));
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);
        sign_user.setEmail("jack.smith@hotmail.com");
        sign_user.setName("jack");
        sign_user.setPassword("jack123");
        sign_user.setUsername("jacksmith");
        Set<String> role=new HashSet<>();
        role.add("user");
        sign_user.setRole(role);
        Role userR=new Role(RoleName.ROLE_USER);
        Role adminR=new Role(RoleName.ROLE_ADMIN);
        Role pmR=new Role(RoleName.ROLE_PM);
        userRole=Optional.of(userR);
        adminRole=Optional.of(adminR);
        pmRole=Optional.of(pmR);
        when(userRepository.existsByUsername(userr.get().getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userr.get().getEmail())).thenReturn(true);
        when(userRepository.save(userr.get())).thenReturn(userr.get());
        when(encoder.encode(userr.get().getPassword())).thenReturn(userr.get().getPassword());
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(userRole);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
        when(roleRepository.findByName(RoleName.ROLE_PM)).thenReturn(pmRole);
        assertThat(userService.createUser(sign_user)).isEqualTo(new ResponseEntity<String>("Fail -> Email is already taken!",
                HttpStatus.BAD_REQUEST));
    }


    @Test
    public void delete_user_that_exist_in_database_test(){
        user1.setEmail("jack.smith@hotmail.com");
        user1.setId(Long.parseLong("98"));
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        roles.add(new Role(RoleName.ROLE_USER));
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);
        doNothing().when(userRepository).deleteById(user1.getId());
        when(userRepository.findById(Long.parseLong("98"))).thenReturn(userr);
        assertThat(userService.deleteUser(user1.getId())).isNotNull();

    }
    @Test
    public void delete_user_that_dont_exist_in_database_test(){
        user1.setEmail("jack.smith@hotmail.com");
        user1.setId(Long.parseLong("98"));
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        roles.add(new Role(RoleName.ROLE_USER));
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);
        doNothing().when(userRepository).deleteById(user1.getId());
        when(userRepository.findById(Long.parseLong("8"))).thenReturn(userr);
        assertThat(userService.deleteUser(user1.getId())).isNull();

    }


    @Test
    public void updating_user_when_user_exist_in_database_test(){
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        roles.add(new Role(RoleName.ROLE_USER));
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);
        sign_user.setEmail("jack.smith@hotmail.com");
        sign_user.setName("jack_updated");
        sign_user.setPassword("jack123");
        sign_user.setUsername("jacksmith");
        Set<String> role=new HashSet<>();
        role.add("user");
        sign_user.setRole(role);
        Role userR=new Role(RoleName.ROLE_USER);
        Role adminR=new Role(RoleName.ROLE_ADMIN);
        Role pmR=new Role(RoleName.ROLE_PM);
        userRole=Optional.of(userR);
        adminRole=Optional.of(adminR);
        pmRole=Optional.of(pmR);
        User user=new User();
        user.setId(user1.getId());
        user.setRoles(user1.getRoles());
        user.setUsername(sign_user.getUsername());
        user.setName(sign_user.getName());
        user.setPassword(sign_user.getPassword());
        user.setEmail(sign_user.getEmail());
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(userRole);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
        when(roleRepository.findByName(RoleName.ROLE_PM)).thenReturn(pmRole);
        when(userRepository.save(user)).thenReturn(user);
        assertThat(userService.updateUser("jacksmith",sign_user)).isEqualTo(user);
    }

    @Test
    public void updating_user_when_username_dont_exist_in_database_test(){
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        roles.add(new Role(RoleName.ROLE_USER));
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);
        sign_user.setEmail("jack.smith@hotmail.com");
        sign_user.setName("jack_updated");
        sign_user.setPassword("jack123");
        sign_user.setUsername("jacksmith");
        Set<String> role=new HashSet<>();
        role.add("user");
        sign_user.setRole(role);
        Role userR=new Role(RoleName.ROLE_USER);
        Role adminR=new Role(RoleName.ROLE_ADMIN);
        Role pmR=new Role(RoleName.ROLE_PM);
        userRole=Optional.of(userR);
        adminRole=Optional.of(adminR);
        pmRole=Optional.of(pmR);
        User user=new User();
        user.setId(user1.getId());
        user.setRoles(user1.getRoles());
        user.setUsername(sign_user.getUsername());
        user.setName(sign_user.getName());
        user.setPassword(sign_user.getPassword());
        user.setEmail(sign_user.getEmail());
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(encoder.encode(user.getPassword())).thenReturn(user.getPassword());
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(userRole);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(adminRole);
        when(roleRepository.findByName(RoleName.ROLE_PM)).thenReturn(pmRole);
        when(userRepository.save(user)).thenReturn(user);
        assertThat(userService.updateUser("jaregrg",sign_user)).isNotEqualTo(user);
    }

    @Test
    public void is_admin_test(){
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        Role adminR=new Role(RoleName.ROLE_ADMIN);
        roles.add(adminR);
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(userr);
        assertThat(userService.isAdmin(user1.getUsername())).isTrue();

    }
    @Test
    public void is_not_admin_test(){
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        Role userR=new Role(RoleName.ROLE_USER);
        roles.add(userR);
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);;
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(true);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(userr);
        assertThat(userService.isAdmin(userr.get().getUsername())).isFalse();
    }

    @Test
    public void is_admin_with_username_that_dont_exist_in_database_test(){
        user1.setEmail("jack.smith@hotmail.com");
        user1.setName("jack");
        user1.setPassword("jack123");
        user1.setUsername("jacksmith");
        Set<Role> roles=new HashSet<>();
        Role userR=new Role(RoleName.ROLE_USER);
        roles.add(userR);
        user1.setRoles(roles);
        Optional<User> userr=Optional.of(user1);;
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(false);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(null);
        assertThat(userService.isAdmin(userr.get().getUsername())).isFalse();
    }
}
