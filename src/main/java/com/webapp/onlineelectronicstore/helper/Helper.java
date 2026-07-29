package com.webapp.onlineelectronicstore.helper;

import com.webapp.onlineelectronicstore.dtos.response.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;

public class Helper {

    public static <U, V> PageableResponse<V> getPageableResponse(
            Page<U> page,
            Class<V> type) {

        ModelMapper modelMapper = new ModelMapper();

        List<U> content = page.getContent();

        List<V> dtoList = content.stream()
                .map(object -> modelMapper.map(object, type))
                .toList();

        PageableResponse<V> response = new PageableResponse<>();

        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastpage(page.isLast());

        return response;
    }
}