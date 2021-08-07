package bsmart.technology.rru.base.api;

import java.io.IOException;

public class UpgradeHttpException extends IOException {

    public String getNewVersionName() {
        return newVersionName;
    }

    public void setNewVersionName(String newVersionName) {
        this.newVersionName = newVersionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    private String newVersionName;
    private String downloadUrl;
    private String fileMd5;

    public UpgradeHttpException(String newVersionName,
                                String downloadUrl,
                                String md5) {
        super("Update to the latest app version.");
        this.newVersionName = newVersionName;
        this.fileMd5 = md5;
        this.downloadUrl = downloadUrl;
    }

}
