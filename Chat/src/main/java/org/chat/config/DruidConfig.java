package org.chat.config;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @date 2023/11/22 12:15
 */
@Configuration
public class DruidConfig {
    // 配置 Druid 数据源的监控信息
    @Bean
    public JdkRegexpMethodPointcut druidStatPointcut() {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPatterns("org.chat.*"); // 替换成你的项目包名
        return pointcut;
    }

    // 配置 Druid 数据源的监控信息
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(druidStatPointcut());
//        advisor.setAdvice(druidDataSourceStatInterceptor());
        return advisor;
    }
}
