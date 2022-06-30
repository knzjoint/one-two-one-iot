package cn.makerknz.product.server.minio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 朱康南
 * @Date: 2021/5/26/026 11:01
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MinioItem {
    private String objectName;
    private long size;
    private String versionId;
    private String presignedObjectUrl;
}
