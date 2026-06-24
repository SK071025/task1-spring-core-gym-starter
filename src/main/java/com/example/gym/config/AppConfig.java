package com.example.gym.config;

import com.example.gym.models.Trainee;
import com.example.gym.models.Trainer;
import com.example.gym.models.Training;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class AppConfig
{
    @Bean
    public Map<Long, Trainee> traineeStorage()
    {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Trainer> trainerStorage()
    {
        return new HashMap<>();
    }

    @Bean
    public Map<Long, Training> trainingStorage()
    {
        return new HashMap<>();
    }
}
