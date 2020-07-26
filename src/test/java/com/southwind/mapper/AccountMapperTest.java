package com.southwind.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountMapperTest {
    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void test1() {
        accountMapper.selectList(null).forEach(System.out::println);
    }
}
