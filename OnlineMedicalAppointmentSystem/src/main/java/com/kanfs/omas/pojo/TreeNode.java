package com.kanfs.omas.pojo;

import java.util.List;

public class TreeNode {
    private int id;                    // 节点id
    private String text;               // 节点名称
    private int parentId;              // 父节点id
    private String iconfont;           // 节点图标字体
    private String url;                // 跳转路径
    private List<TreeNode> children;   // 子节点列表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getIconfont() {
        return iconfont;
    }

    public void setIconfont(String iconfont) {
        this.iconfont = iconfont;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
