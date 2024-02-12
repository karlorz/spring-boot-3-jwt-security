package com.alibou.security.bookbinder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookbinderRequest {

    private Integer id;
    private String author;
    private String isbn;
}
