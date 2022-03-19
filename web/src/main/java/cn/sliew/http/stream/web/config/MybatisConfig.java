package cn.sliew.http.stream.web.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;

@Configuration
@MapperScan(basePackages = "cn.sliew.http.stream.dao")
public class MybatisConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Component
    public static class MyMetaObjectHandler implements MetaObjectHandler {
        @Override
        public void insertFill(MetaObject metaObject) {
            this.setInsertFieldValByName("creator", "system", metaObject);
            this.setInsertFieldValByName("modifier", "system", metaObject);
            this.setInsertFieldValByName("gmtCreate", new Date(), metaObject);
            this.setInsertFieldValByName("gmtModified", new Date(), metaObject);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            this.setUpdateFieldValByName("modifier", "system", metaObject);
            this.setUpdateFieldValByName("gmtModified", new Date(), metaObject);
        }
    }
}