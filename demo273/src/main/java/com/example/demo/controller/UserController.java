package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.UserGroup;
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
        System.out.println("user " + user);
        JSONObject jsonObject = new JSONObject(user);

        List<User> b = userService.login(jsonObject.getString("username"), jsonObject.getString("password"));
        System.out.println("if " + b.isEmpty());
        if (b.isEmpty()) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } else {
            session.setAttribute("name", jsonObject.getString("username"));
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @PostMapping(value = "/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> logout(HttpSession session) {
        System.out.println(session.getAttribute("name"));
        session.invalidate();
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/listDir", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listDir(@RequestBody String path) {
        System.out.println("filename " + path);
        JSONObject jsonObject = new JSONObject(path);
        File directory = new File(jsonObject.getString("path"));
        //get all the files from a directory
       /* File directory = new File("C:\\Users\\Karan\\Downloads");*/
        fList = directory.listFiles();
       /* for (File file : fList){
            System.out.println(file.getName());
        }*/

        return new ResponseEntity(HttpStatus.OK);
    }



    @GetMapping(path = "/listDirFiles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listDirFiles() {
        System.out.println("in get");
        int i = 0;
        String[] allFiles = new String[fList.length];
        for (File file : fList) {
            System.out.println("files" + file.getName());
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
     @PostMapping("/uploadfiles")
     public ResponseEntity<?> singleFileUpload(@RequestParam("file") MultipartFile file, HttpSession session) {
         String userfolder = session.getAttribute("name").toString();
         userService.uploader(file, userfolder);
         return new ResponseEntity(HttpStatus.OK);
     }


    @PostMapping(path = "/share", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> shareFile(@RequestBody User user, HttpSession session) {
        boolean x = userService.sharefile(user);
        return new ResponseEntity(HttpStatus.OK);

    }

    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFile(@RequestBody String filename, HttpSession session) {
        boolean x = userService.deleteFile(fileName, session.getAttribute("name").toString());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/listMembers")
    public ResponseEntity<?> listGroupMembers(@RequestBody String user,
                                              HttpSession session) {
        String userEmail = session.getAttribute("name").toString();
        JSONObject userObj = new JSONObject(user);
        User[] memberList = userService.listMembers(Integer.parseInt(userObj.getString("groupId")));
        int len = memberList.length;
        if ( len > 0) {
            System.out.println("Members found")
            return new ResponseEntity(memberList, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

  /*  @PostMapping("/listUserGroups")
    public ResponseEntity<?> listUserGroups(@RequestBody String user,
                                            HttpSession session) {
        String userEmail = session.getAttribute("name").toString();
        JSONObject userObj = new JSONObject(user);
        System.out.println(userObj);

        GroupT[] groupList = sharefolderservice.listUserGroups(userEmail);

        if (groupList.length > 0) {
            return new ResponseEntity(groupList, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
  */

    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(@RequestBody String user,
                                         HttpSession session) {
        String userEmail = session.getAttribute("name").toString();
        JSONObject userObj = new JSONObject(user);
        System.out.println(userObj);

        boolean success = userService.createGroup(userObj.getString("groupname"), userEmail);

        if (success) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addMembersToGroup") // //new annotation since 4.3
    public ResponseEntity<?> addMembersToGroup(@RequestBody String user,
                                               HttpSession session) {
        String userEmail = session.getAttribute("name").toString();
        JSONObject userObj = new JSONObject(user);
        System.out.println(userObj);

        boolean success = userService.addMembers(Integer.parseInt(userObj.getString("groupId")), userObj.getString("memberEmail"));

        if (success) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/createShareFolder", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSharedFolder(@RequestBody CreateSharedFolder createSharedFolder, HttpSession session) {
        userService.createShareFolder(createShareFolder, session.getAttribute("name").toString());
        return new ResponseEntity(HttpStatus.OK);

    }

    @PostMapping(path = "/createFolder", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createfolder(@RequestBody CreateShareFolder createsharefolder, HttpSession session) {
        userService.createFolder(createsharefolder, session.getAttribute("name").toString());
        return new ResponseEntity(HttpStatus.OK);

    }



}