package cn.makerknz.product.server.minio;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @PackageName: com.hope.minio.service.impl
 * @ClassName: MinioServiceImpl
 * @Author Hope
 * @Date 2020/7/27 9:59
 * @Description: MinioServiceImpl
 */
@Service
//@DS("master")
public class MinioServiceImpl implements IMinioService {

    @Autowired
    private MinioUtil minioUtil;

    /**
     * 判断 bucket是否存在
     *
     * @param bucketName
     * @return
     */
    @Override
    public boolean bucketExists(String bucketName) {
        return minioUtil.bucketExists(bucketName);
    }

    /**
     * 创建 bucket
     *
     * @param bucketName
     */
    @Override
    public void makeBucket(String bucketName) {
        minioUtil.makeBucket(bucketName);
    }

    /**
     * 文件上传
     *
     * @param bucketName
     * @param objectName
     * @param filename
     */
    @Override
    public void uploadObject(String bucketName, String objectName, String filename) {
        minioUtil.uploadObject(bucketName, objectName, filename);
    }

    /**
     * 文件上传
     *
     * @param bucketName
     * @param objectName
     * @param filename
     */
    @Override
    public void uploadObject(String bucketName, String objectName, String filename, String contentType) {
        minioUtil.uploadObject(bucketName, objectName, filename, contentType);
    }

    @Override
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contentType) {
        minioUtil.putObject(bucketName, objectName, stream, size, contentType);
    }


    /**
     * 删除文件
     * @param bucketName
     * @param objectName
     */
    @Override
    public boolean removeObject(String bucketName,String objectName, String versionId) {
        return minioUtil.removeObject(bucketName,objectName, versionId);
    }

    /**
     * 下载文件
     *
     * @param fileName
     * @param originalName
     * @param response
     */
    @Override
    public void downloadFile(String bucketName, String fileName, String originalName, HttpServletResponse response) {
        minioUtil.downloadFile(bucketName,fileName, originalName, response);
    }

    /**
     * 获取文件路径
     * @param bucketName
     * @param objectName
     * @return
     */
    @Override
    public String getObjectUrl(String bucketName,String objectName, Integer expires, Method method) {
        return minioUtil.getPresignedObjectUrl(bucketName,objectName, expires, method);
    }

    /**
     * 分页获取对象
     *
     * @param bucketName
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Item> pageObject(String bucketName, Integer page, Integer size) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        Iterable<Result<Item>> results = minioUtil.listObjects(bucketName);
        Page<Item> itemPageResult = new Page<>();
        List<Item> listItem = new ArrayList<>();
        Integer i = 0;
        for (Result<Item> result : results) {
            if ( i >= page * size && i < (page + 1) * size) {
                listItem.add(result.get());
            }
            i++;
        }
        itemPageResult.setRecords(listItem);
        itemPageResult.setPages(page);
        itemPageResult.setSize(size);
        itemPageResult.setTotal(i);

        return itemPageResult;
    }

    /**
     * 获取对象列表
     *
     * @param bucketName
     * @param prefix
     * @param startAfter
     * @param maxKeys
     * @return
     */
    @Override
    public List<Item> listObject(String bucketName, String prefix, String startAfter, Integer maxKeys) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        Iterable<Result<Item>> results = minioUtil.listObjects(bucketName, prefix, startAfter, maxKeys);
        List<Item> listItem = new ArrayList<>();
        Integer i = 0;
        for (Result<Item> result : results) {
            Item item = result.get();
            listItem.add(item);
            i++;
        }

        return listItem;
    }

    /**
     * 获取对象列表
     *
     * @param bucketName
     * @return
     */
    @Override
    public List<MinioItem> listObject(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        Iterable<Result<Item>> results = minioUtil.listObjects(bucketName);
        List<MinioItem> listItem = new ArrayList<>();
        Integer i = 0;
        for (Result<Item> result : results) {
            Item item = result.get();
            if (item.isDir()) {
                continue;
            }
            MinioItem minioItem = MinioItem.builder()
                    .objectName(item.objectName())
                    .size(item.size())
                    .versionId(item.versionId())
                    .build();
            String presignedObjectUrl = minioUtil.getPresignedObjectUrl(bucketName, item.objectName(), 60 * 10, Method.GET);
            minioItem.setPresignedObjectUrl(presignedObjectUrl);
            listItem.add(minioItem);
            i++;
        }

        return listItem;
    }

}
