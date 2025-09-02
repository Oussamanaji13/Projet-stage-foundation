package com.oussama.social_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean empty;
    private int numberOfElements;

    public static <T> PageResponse<T> from(org.springframework.data.domain.Page<T> springPage) {
        return PageResponse.<T>builder()
                .content(springPage.getContent())
                .page(springPage.getNumber())
                .size(springPage.getSize())
                .totalElements(springPage.getTotalElements())
                .totalPages(springPage.getTotalPages())
                .first(springPage.isFirst())
                .last(springPage.isLast())
                .empty(springPage.isEmpty())
                .numberOfElements(springPage.getNumberOfElements())
                .build();
    }

    public <R> PageResponse<R> map(java.util.function.Function<T, R> mapper) {
        List<R> mappedContent = this.content.stream()
                .map(mapper)
                .collect(java.util.stream.Collectors.toList());

        return PageResponse.<R>builder()
                .content(mappedContent)
                .page(this.page)
                .size(this.size)
                .totalElements(this.totalElements)
                .totalPages(this.totalPages)
                .first(this.first)
                .last(this.last)
                .empty(this.empty)
                .numberOfElements(this.numberOfElements)
                .build();
    }
}
