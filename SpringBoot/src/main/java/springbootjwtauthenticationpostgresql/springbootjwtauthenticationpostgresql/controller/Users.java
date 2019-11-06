package springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.message.request.SignUpForm;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.model.User;
import springbootjwtauthenticationpostgresql.springbootjwtauthenticationpostgresql.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class Users {



    @Autowired
    private UserService userService;


    @GetMapping(value = "/*")//can access with this root
    @PreAuthorize("hasRole('ADMIN')") //Only admin can view all users' details
    public List<User> AllUsers(){

        return this.userService.getAllUsers();
    }



    @GetMapping(value = "/{username}")//can access with this root
    public User GetOneUser(@PathVariable(value = "username") String username){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String auth_name = auth.getName(); //get logged in username

        if(auth_name.equals(username)||this.userService.isAdmin(auth_name)){//admin and user can see the details
            return this.userService.getUser(username);
        }
        else{
            return null;
        }

    }



    @DeleteMapping(value = "/{id}")//can access with this root
    @PreAuthorize("hasRole('ADMIN')") //Only admin can delete user
    public User DeleteUser(@PathVariable(value = "id") Long id){

       return  this.userService.deleteUser(id);
    }




    @PostMapping(value = "/{username}")//can access with this root
    public User EditUser(@PathVariable(value = "username") String username,@RequestBody SignUpForm user) {//only user can edit

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String auth_name = auth.getName(); //get logged in username
      User upUser=null;
       if(auth_name.equals(username)){
            upUser=this.userService.updateUser(username,user);


        }
       return upUser;

    }


}
