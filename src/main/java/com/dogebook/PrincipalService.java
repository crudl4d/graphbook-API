package com.dogebook;

import com.dogebook.configuration.UserContext;
import com.dogebook.entities.User;
import com.dogebook.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

@Service
public class PrincipalService {


    @Autowired
    private UserRepository userRepository;
    public static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    public String createFile(MultipartFile image, Principal principal) throws IOException {
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, UserContext.getUser(principal).getId().toString() + ".jpg");
		try {
			Files.createFile(fileNameAndPath);
		} catch (NoSuchFileException ignored) {}
        Files.write(fileNameAndPath, image.getBytes());
        String fileUrl = userRepository.postProfilePicture(UserContext.getUser(principal).getId(), fileNameAndPath.toString()).getProfilePicturePath();

        BufferedImage resizedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(ImageIO.read(new ByteArrayInputStream(image.getBytes())), 0, 0, 100, 100, null);
        graphics2D.dispose();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        Files.write(Paths.get(UPLOAD_DIRECTORY, UserContext.getUser(principal).getId().toString() + "-thumbnail.jpg"), bytes);
        return fileUrl;
    }

    public User patchUser(User existingUser, User toPatch) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = User.class.getDeclaredFields();
        for (Field field : fields) {
            Object value = User.class.getMethod("get" + StringUtils.capitalize(field.getName())).invoke(toPatch);
            if (value != null) {
                User.class.getMethod("set" + StringUtils.capitalize(field.getName()), field.getType()).invoke(existingUser, value);
            }
        }
        return existingUser;
    }
}
