package com.springtest.demo.entity;

import com.springtest.demo.constant.TaskCopyProcessEnum;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskCopyProcessVo {
    float process;
    TaskCopyProcessEnum status;
    public String getStatus() {
        return status.name();
    }
}
