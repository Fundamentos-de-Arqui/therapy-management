package com.soulware.therapymanagement.infrastructure.messaging.dto;

import java.util.List;

public record PagedResponseResource<T>(
        List<T> items,
        long totalItems,
        int totalPages,
        int page,
        int size
) {}