package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.entity.UserGroup;
import com.example.demo.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> login(String email, String password) {
        System.out.println("userRepository.findByEmailAndPassword(email, password)" + userRepository.findByEmailAndPassword(email, password));
        return userRepository.findByEmailAndPassword(email, password);
    }

    //delete files
    public boolean deleteFile(String fileName, String userFolder) {
        Path toBeDeleted = Paths.get("./" + userFolder + "/" + fileName);
        System.out.println(toBeDeleted);

        try {
            if (Files.deleteIfExists(toBeDeleted)) {
                System.out.println("File deleted successfully");
                return true;
            }
        } catch (IOException e) {
            System.out.println("Exception in deleting file");
            e.printStackTrace();
        }
        return false;
    }

    public void uploader(MultipartFile file, Integer group_id) {

        try {
            byte[] bytes = file.getBytes();

            Path path = Paths.get("./" + group_id);
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createSharedFolder(CreateSharedFolder createSharedFolder, String sharedFolder){

        createFolder();
        Path src = Paths.get("./"+sharefolder + "/" + createSharedFolder.getFoldername());
        String[] shareinfo = createSharedFolder.getEmails().split(",");
        for (String i : shareinfo) {
            Path dest = Paths.get("./" + i + "/" + createSharedFolder.getFoldername());
            try {
                Files.createSymbolicLink(dest, src);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public void createFolder(CreateSharedFolder createSharedFolder, String sharedFolder){
        new File("./"+sharedFolder + "/" + createSharedFolder.getFoldername()).mkdir();
    }

    public boolean createGroup(String groupName, String memberEmail) {
        if (groupName.equals("") && memberEmail.equals("")) {
            return false;
        } else {
            UserGroup userGroup = new UserGroup();
            userGroup.setGroup_name(groupName);
            User user = userRepository.findByEmail(memberEmail);
            userGroup.setOwner_id(user.getId());
            userGroupRepository.save(userGroup);
            boolean x = new File("./" + userGroup.getGroupId()).mkdir();
            if (x) return true;
            else return false;
        }
    }

    public boolean addMembers(Integer group_id, String memberEmail) {

        if (memberEmail.equals("") && group_id == null) {
            return false;
        } else {
            UserGroup group = userGroupRepository.findByGroupId(group_id);
            User groupMemberUser = userRepository.findByEmail(memberEmail);
            Set<User> memberSet = group.getUser();
            memberSet.add(groupMemberUser);
            group.setUser(memberSet);
            Set<UserGroup> groupSet = groupMemberUser.getGroupt();
            groupSet.add(group);
            groupMemberUser.setGroupt(groupSet);

            userRepository.save(groupMemberUser);
            userGroupRepository.save(group);
        }

        return true;

    }

    public UserGroup[] listUserGroups(String userEmail) {

        if (userEmail.equals("")) {
            return new UserGroup[]{};
        } else {
//            UserGroup group = userGroupRepository.findByGroup_id(group_id).get(0);
            User user = userRepository.findByEmail(userEmail);
            Set<UserGroup> groupSet = user.getGroupt();
            return (UserGroup[]) groupSet.toArray();
        }
    }

    public User[] listMembers(Integer group_id) {

        if (group_id == null) {
            return new User[]{};
        } else {
            UserGroup userGroup = userGroupRepository.findByGroupId(group_id);
            Set<User> memberSet = userGroup.getUser();
            return (User[]) memberSet.toArray();
        }
    }


}