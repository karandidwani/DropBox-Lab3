package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Entity;
import org.w3c.dom.html.HTMLParagraphElement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller    // This means that this class is a Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/user") // This means URL's start with /demo (after Application path)
public class UserController {
    @Autowired
    private UserService userService;

    File[] fList;

    @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE) // Map ONLY POST Requests
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        userService.addUser(user);
        System.out.println("Saved");
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Iterable<User> getAllUsers() {
        // This returns a JSON with the users
        return userService.getAllUsers();
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody String user, HttpSession session) {
        System.out.println("user "+user);
        JSONObject jsonObject = new JSONObject(user);

        List<User> b = userService.login(jsonObject.getString("username"),jsonObject.getString("password"));
        System.out.println("if "+b.isEmpty());
        if(b.isEmpty()){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }else {
            session.setAttribute("name", jsonObject.getString("username"));
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @PostMapping(path = "/listDir", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listDir(@RequestBody String path) {
       System.out.println("filename "+path);
        JSONObject jsonObject = new JSONObject(path);
        File directory = new File(jsonObject.getString("path"));
        //get all the files from a directory
       /* File directory = new File("C:\\Users\\Karan\\Downloads");*/
        fList = directory.listFiles();
       /* for (File file : fList){
            System.out.println(file.getName());
        }*/

        return new ResponseEntity( HttpStatus.OK);
    }

    @GetMapping(path = "/listDirFiles", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listDirFiles()
    {
        System.out.println("in get");
        int i =0;
        String[] allFiles = new String[fList.length];
        for (File file : fList){
            System.out.println("files"+file.getName());
            allFiles[i] = file.getName();
            i++;
        }
        return new ResponseEntity(allFiles, HttpStatus.OK);
    }

   /* @GetMapping(path="/listDirFiles",consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String[] listDirFiles(@RequestBody String req)
    {
        JSONObject pathJSON = new JSONObject(req);
        System.out.println(pathJSON);
        File folder = new File("./files/" + pathJSON.getString("path"));
        String[] files = folder.list();
        return files;
    }
*/
    @PostMapping(value = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(HttpSession session) {
        System.out.println(session.getAttribute("name"));
        session.invalidate();
        return new ResponseEntity(HttpStatus.OK);
    }
}