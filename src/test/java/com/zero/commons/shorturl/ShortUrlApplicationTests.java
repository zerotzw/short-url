package com.zero.commons.shorturl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShortUrlApplicationTests {



	@Test
	void contextLoads() {

		System.out.println(IdUtil.createSnowflake(1, 1).nextId());
	}

}
