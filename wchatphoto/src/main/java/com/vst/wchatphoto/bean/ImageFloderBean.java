package com.vst.wchatphoto.bean;

/**
 * @author zwy
 * @email 16681805@qq.com
 * created on 2017/3/14
 * class description:请输入类描述
 */
public class ImageFloderBean {
    //图片的文件夹路径
    private String dirPath;
    //第一张图片的路径
    private String firstImagePath;
    //文件夹名称
    private String dirName;
    //图片数量
    private int imgCount;

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
        int lastIndexOf = dirPath.lastIndexOf("/");
        this.dirName = dirPath.substring(lastIndexOf);
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getDirName() {
        return dirName;
    }

    public int getImgCount() {
        return imgCount;
    }

    public void setImgCount(int imgCount) {
        this.imgCount = imgCount;
    }
}
