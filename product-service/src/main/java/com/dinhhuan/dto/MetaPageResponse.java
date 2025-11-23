package com.dinhhuan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaPageResponse {
    private int page;
    private int perPage;
    private long totalElements;
    private int totalPages;
}
