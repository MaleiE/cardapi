package com.malei.card.api.demo;

import com.malei.card.api.demo.repository.UserRepository;
import com.malei.card.api.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    @Autowired
    static
    UserRepository userRepository;

    private static void get(){
        System.out.println(userRepository.findAll());
    }


    public static void main(String[] args) {

        get();

        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher("d");
        System.out.println(m.matches());
        List<String> strings = Arrays.asList();
        System.out.println(strings.size());
    }

}
