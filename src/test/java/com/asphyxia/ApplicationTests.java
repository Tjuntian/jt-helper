package com.asphyxia;

import com.asphyxia.pojo.Brain;
import com.asphyxia.service.IBrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {
    @Autowired
    private IBrainService brainService;

    @Test
    void contextLoads() {
        Brain brain=new Brain(null,"1","1");
        boolean flag = brainService.save(brain);
    }

}
