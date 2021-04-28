package com.tommy.board.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResult<T> {
    private boolean isSuccess = true;
    private String errorCode;
    private T data;

    public ApiResult(T data) {
       this.data = data;
    }
}
