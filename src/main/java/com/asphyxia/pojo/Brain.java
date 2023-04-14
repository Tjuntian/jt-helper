package com.asphyxia.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author JunTian
 * @since 2023-03-22
 */

@TableName("tb_brain")
public class Brain implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private String username;


    public Brain() {
    }

    public Brain(Integer id, String name, String description, String username) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.username = username;
    }

    /**
     * 获取
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "Brain{id = " + id + ", name = " + name + ", description = " + description + ", username = " + username + "}";
    }
}
