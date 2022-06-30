package cn.makerknz.product.server.minio;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @PackageName: com.hope.minio.service
 * @ClassName: MinioService
 * @Author Hope
 * @Date 2020/7/27 9:58
 * @Description: MinioService
 */
public interface IMinioService {


    /**
     * 判断 bucket是否存在
     *
     * @param bucketName
     * @return
     */
    boolean bucketExists(String bucketName);

    /**
     * 创建 bucket
     *
     * @param bucketName
     */
    void makeBucket(String bucketName);

    /**
     * 文件上传
     *
     * @param bucketName
     * @param objectName
     * @param filename
     */

    public void uploadObject(String bucketName, String objectName, String filename);

    /**
     * 文件上传
     *
     * @param bucketName
     * @param objectName
     * @param filename
     */

    public void uploadObject(String bucketName, String objectName, String filename, String contentType);

    /**
     * 文件上传
     *
     * @param bucketName
     * @param objectName
     * @param stream
     */
    void putObject(String bucketName, String objectName, InputStream stream, long size, String contentType);

    /**
     * 删除文件
     * @param bucketName
     * @param objectName
     */
    boolean removeObject(String bucketName, String objectName, String versionId);

    /**
     * 下载文件
     *
     * @param fileName
     * @param originalName
     * @param response
     */
    void downloadFile(String bucketName, String fileName, String originalName, HttpServletResponse response);

    /**
     * 获取文件路径
     * @param bucketName
     * @param objectName
     * @return
     */
    String getObjectUrl(String bucketName, String objectName, Integer expires, Method method);

    /**
     * 分页获取对象
     * @param bucketName
     * @param page
     * @param size
     * @return
     */
    Page<Item> pageObject(String bucketName, Integer page, Integer size) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;

    /**
     * 获取对象列表
     * @param bucketName
     * @param prefix
     * @param startAfter
     * @param maxKeys
     * @return
     */
    List<Item> listObject(String bucketName, String prefix, String startAfter, Integer maxKeys) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;

    /**
     * 获取对象列表
     * @param bucketName
     * @return
     */
    List<MinioItem> listObject(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
}
