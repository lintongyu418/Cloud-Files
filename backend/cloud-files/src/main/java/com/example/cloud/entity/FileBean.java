package com.example.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Data
@Table(name = "file")
@Entity
@TableName("file")
public class FileBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20)")
    private Long fileId;

    @Column(columnDefinition = "varchar(500)")
    private String timeStampName;

    @Column(columnDefinition = "varchar(500)")
    private String fileUrl;

    @Column(columnDefinition = "bigint(10)")
    private Long fileSize;

    @Column(columnDefinition = "int(1)")
    @Deprecated
    private Integer isOSS;

    @Column(columnDefinition = "int(1)")
    private Integer storageType;

    @Column(columnDefinition = "int(11)")
    private Integer pointCount;

    @Column(columnDefinition = "varchar(32)")
    private String identifier;
}
