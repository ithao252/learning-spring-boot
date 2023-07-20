package com.example.learnspringboot.services.impl;

import com.example.learnspringboot.services.IStorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageService implements IStorageService {
    //đường dẫn có chứa resource ảnh trong project, nếu chưa có thì tạo ra file uploads trong project
    private final Path storageFolder = Paths.get("uploads");
    // constructor được gọi khi inject
    public ImageService(){
        try {
            Files.createDirectories(storageFolder);
        }catch (IOException exception){
          throw new RuntimeException("Cannot initialize storage", exception);
        }
    }
    //nhận file từ request và kiểm tra xem file có trống không
    private boolean isImageFile(MultipartFile file){
        //install FileNameUtil, lấy đuôi file
        String fileExtensions= FilenameUtils.getExtension(file.getOriginalFilename());
        //chuyển array string thành List<String> để sử dụng method contains kiểm tra xem đuôi file có trong List không, nếu có trả về true
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "pmg"}).contains(fileExtensions.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("store file active");
            // check xem file có rỗng
            if(file.isEmpty()){
                throw new RuntimeException("Fail to store empty file");
            }
            // check xem file có phải image
            if(!isImageFile(file)){
                throw new RuntimeException("You can only upload image file!");
            }
            //check xem file có dưới 5mb
            float fileSizeUnderMegabyte= file.getSize()/1_000_000;
            if(fileSizeUnderMegabyte>5.0f){
                throw new RuntimeException("File size must <=5MB");
            }
            //File must be renamed, avoid same name of image in server
            String fileExtension= FilenameUtils.getExtension(file.getOriginalFilename());
            String fileNameGenerated= UUID.randomUUID().toString().replace("-", "");
            fileNameGenerated=fileNameGenerated+"."+fileExtension;
            Path destinationPath= this.storageFolder.resolve(Paths.get(fileNameGenerated)).normalize().toAbsolutePath();
            if(!destinationPath.getParent().equals(this.storageFolder.toAbsolutePath())){
                throw new RuntimeException("Can not store the file outside current directory");
            }

            try( InputStream inputStream=file.getInputStream()){
                   Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
            return fileNameGenerated;
        }
        catch (IOException exception){
          throw new RuntimeException("Failed to store file " ,exception);
        }

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            //list all files in storageFolder
            //How to fix this ?
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load stored files", e);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }
            else {
                throw new RuntimeException(
                        "Could not read file: " + fileName);
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }
}
