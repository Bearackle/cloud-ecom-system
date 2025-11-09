package com.dinhhuan.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageBlob {
    @Id
    private Long id;
    @Column(name="url")
    private String url;
    @Column(name="file_name")
    private String fileName;
}
