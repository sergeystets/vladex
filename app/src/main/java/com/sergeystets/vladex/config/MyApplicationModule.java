package com.sergeystets.vladex.config;

import com.sergeystets.vladex.activity.SignInActivity;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MyApplicationModule {
    @ContributesAndroidInjector
    abstract SignInActivity contributeActivityInjector();

    @Provides
    static RestTemplate restTemplate() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        return restTemplate;
    }

}
