package com.example.oauth.service.impl;

import com.example.oauth.service.IMessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements IMessageService {
    @Override
    public String getMessage() {
        return "得到消息内容：　止戈流开阵 ";
    }
}
