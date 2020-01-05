package com.springtest.demo.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TaskManageParam {
    String oldParam;
    String newParam;
}
