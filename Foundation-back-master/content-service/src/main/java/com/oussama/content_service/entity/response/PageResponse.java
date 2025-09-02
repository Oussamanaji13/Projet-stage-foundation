package com.oussama.content_service.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Classe générique pour encapsuler les réponses paginées
 *
 * @param <T> Le type d'objet contenu dans la page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * Liste des éléments de la page courante
     */
    private List<T> content;

    /**
     * Numéro de la page courante (0-based)
     */
    private int page;

    /**
     * Taille de la page (nombre d'éléments par page)
     */
    private int size;

    /**
     * Nombre total d'éléments dans toutes les pages
     */
    private long totalElements;

    /**
     * Nombre total de pages
     */
    private int totalPages;

    /**
     * Indique si c'est la première page
     */
    private boolean first;

    /**
     * Indique si c'est la dernière page
     */
    private boolean last;

    /**
     * Indique si la page est vide
     */
    private boolean empty;

    /**
     * Nombre d'éléments dans la page courante
     */
    private int numberOfElements;

    /**
     * Informations de tri (optionnel)
     */
    private Sort sort;

    /**
     * Classe interne pour les informations de tri
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sort {
        private boolean sorted;
        private boolean unsorted;
        private boolean empty;
        private String direction;
        private String property;
    }

    /**
     * Méthode utilitaire pour créer une PageResponse à partir d'une Page Spring
     */
    public static <T> PageResponse<T> from(org.springframework.data.domain.Page<T> springPage) {
        PageResponseBuilder<T> builder = PageResponse.<T>builder()
                .content(springPage.getContent())
                .page(springPage.getNumber())
                .size(springPage.getSize())
                .totalElements(springPage.getTotalElements())
                .totalPages(springPage.getTotalPages())
                .first(springPage.isFirst())
                .last(springPage.isLast())
                .empty(springPage.isEmpty())
                .numberOfElements(springPage.getNumberOfElements());

        // Ajouter les informations de tri si disponibles
        if (springPage.getSort() != null && springPage.getSort().isSorted()) {
            org.springframework.data.domain.Sort.Order firstOrder = springPage.getSort().iterator().next();
            Sort sort = Sort.builder()
                    .sorted(springPage.getSort().isSorted())
                    .unsorted(springPage.getSort().isUnsorted())
                    .empty(springPage.getSort().isEmpty())
                    .direction(firstOrder.getDirection().name())
                    .property(firstOrder.getProperty())
                    .build();
            builder.sort(sort);
        }

        return builder.build();
    }

    /**
     * Méthode utilitaire pour transformer le contenu d'une PageResponse
     */
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
                .sort(this.sort)
                .build();
    }

    /**
     * Vérifie s'il y a une page suivante
     */
    public boolean hasNext() {
        return !this.last;
    }

    /**
     * Vérifie s'il y a une page précédente
     */
    public boolean hasPrevious() {
        return !this.first;
    }

    /**
     * Retourne le numéro de la page suivante (si elle existe)
     */
    public Integer getNextPage() {
        return hasNext() ? this.page + 1 : null;
    }

    /**
     * Retourne le numéro de la page précédente (si elle existe)
     */
    public Integer getPreviousPage() {
        return hasPrevious() ? this.page - 1 : null;
    }
}

