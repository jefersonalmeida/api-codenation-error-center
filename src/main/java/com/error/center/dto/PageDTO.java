package com.error.center.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageDTO<T> {
    private List<T> content;
    private long number;
    private long size;
    private long totalElements;
    private long totalPages;
}
