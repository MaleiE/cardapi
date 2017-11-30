package com.malei.card.api.demo.controller;

import com.malei.card.api.demo.dto.CreateAndUpdateUserDto;
import com.malei.card.api.demo.dto.UserDto;
import com.malei.card.api.demo.model.User;
import com.malei.card.api.demo.service.UserService;
import com.malei.card.api.demo.validation.UserIdConstraint;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(
            @RequestBody CreateAndUpdateUserDto user){
        User createUser = modelMapper.map(user,User.class);
        userService.saveUser(createUser);
        UserDto userDto = modelMapper.map(userService.saveUser(createUser),UserDto.class);

        Link selfLink = linkTo(UserController.class).slash(userDto.getUserId()).withSelfRel();
        Link cardLink = linkTo(methodOn(CardController.class).getAllCard(userDto.getUserId().toString())).withRel("cards");
        userDto.add(selfLink, cardLink);

        return new ResponseEntity<UserDto>(userDto, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(
            @UserIdConstraint
            @PathVariable String id,
            @RequestBody CreateAndUpdateUserDto user){
        User updateUser = modelMapper.map(user,User.class);
        updateUser.setId(Long.parseLong(id));
        UserDto userDto = modelMapper.map((userService.updateUser(updateUser)),UserDto.class);

        Link selfLink = linkTo(UserController.class).slash(userDto.getUserId()).withSelfRel();
        Link cardLink = linkTo(methodOn(CardController.class).getAllCard(userDto.getUserId().toString())).withRel("cards");
        userDto.add(selfLink, cardLink);

        return  new ResponseEntity<UserDto>(userDto,HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserById(
            @UserIdConstraint
            @PathVariable String id){
        UserDto userDto = modelMapper.map(userService.getById(id),UserDto.class);

        Link selfLink = linkTo(UserController.class).slash(userDto.getUserId()).withSelfRel();
        Link cardLink = linkTo(methodOn(CardController.class).getAllCard(userDto.getUserId().toString())).withRel("cards");
        userDto.add(selfLink, cardLink);

        return new ResponseEntity<UserDto>(userDto,HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(
            @UserIdConstraint
            @PathVariable String id){
                userService.deleteUser(userService.getById(id));

                return ResponseEntity.noContent().build();
    }
}
