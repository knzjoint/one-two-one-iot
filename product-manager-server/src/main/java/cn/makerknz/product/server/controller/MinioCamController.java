package cn.makerknz.product.server.controller;

import cn.makerknz.product.server.exception.BusinessException;
import cn.makerknz.product.server.exception.ExceptionEnum;
import cn.makerknz.product.server.exception.ResultVO;
import cn.makerknz.product.server.minio.IMinioService;
import cn.makerknz.product.server.minio.MinioConfig;
import cn.makerknz.product.server.minio.MinioItem;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping("/minio")
@RestController
@Slf4j
public class MinioCamController {

    @Autowired
    private IMinioService minioService;

    @Autowired
    private MinioConfig minioConfig;


    @PostMapping("/upload-things-image-file")
    public ResultVO uploadThingsImageFile(MultipartFile file, HttpServletRequest request) {
        try {
//            String bucketName = minioConfig.getBucketName();
            String bucketName = "things";
            if (!minioService.bucketExists(bucketName)) {
                minioService.makeBucket(bucketName);
            }
            String fileName = file.getOriginalFilename();
            String objectName = new SimpleDateFormat("yyyy/MM/dd/").format(new Date()) + UUID.randomUUID().toString().replaceAll("-", "")
                    + fileName.substring(fileName.lastIndexOf("."));
            minioService.putObject(bucketName, objectName, file.getInputStream(), file.getSize(), file.getContentType());
            String objectUrl = minioService.getObjectUrl(bucketName, objectName, 60 * 60 * 24, Method.GET);
            if (objectUrl == null) {
                throw new BusinessException(ExceptionEnum.MINIO_IMAGE_UPLOAD_ERROR);
            }

            String returnUrl = "http://image.makerknz.cn/" + bucketName + "/" + objectName;

            return ResultVO.success(returnUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionEnum.MINIO_IMAGE_UPLOAD_ERROR);
        }
    }


    @PostMapping("/uploadFile")
    public ResultVO uploadFile(MultipartFile file, HttpServletRequest request) {
        try {
            String bucketName = minioConfig.getBucketName();
            if (!minioService.bucketExists(bucketName)) {
                minioService.makeBucket(bucketName);
            }
            String fileName = file.getOriginalFilename();
            String objectName = new SimpleDateFormat("yyyy/MM/dd/").format(new Date()) + UUID.randomUUID().toString().replaceAll("-", "")
                    + fileName.substring(fileName.lastIndexOf("."));
            minioService.putObject(bucketName, objectName, file.getInputStream(), file.getSize(), file.getContentType());
            return ResultVO.success(minioService.getObjectUrl(bucketName, objectName, 60 * 60 * 24, Method.GET));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionEnum.MINIO_IMAGE_UPLOAD_ERROR);
        }
    }


    @PostMapping("/upload-stream")
    public ResultVO uploadFile(@RequestBody String streamStr) {
        InputStream file = new ByteArrayInputStream(streamStr.getBytes());
        try {
            String bucketName = minioConfig.getBucketName();
            if (!minioService.bucketExists(bucketName)) {
                minioService.makeBucket(bucketName);
            }
            String fileName = "1.png";
            String objectName = new SimpleDateFormat("yyyy/MM/dd/").format(new Date()) + UUID.randomUUID().toString().replaceAll("-", "")
                    + fileName.substring(fileName.lastIndexOf("."));
            minioService.putObject(bucketName, objectName, file, streamStr.length(), "image/png");
            return ResultVO.success(minioService.getObjectUrl(bucketName, objectName, 60 * 60 * 24, Method.GET));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionEnum.MINIO_IMAGE_UPLOAD_ERROR);
        }
    }

    @GetMapping("/list-object")
    public ResultVO listObject(@RequestParam("bucketName") String bucketName) {
        try {
            List<MinioItem> minioItems = minioService.listObject(bucketName);
            return ResultVO.success(minioItems);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ExceptionEnum.MINIO_IMAGE_UPLOAD_ERROR);
        }
    }
}