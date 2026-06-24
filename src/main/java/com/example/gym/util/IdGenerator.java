package com.example.gym.util;

public class IdGenerator
{
    private static Long userId = 0L;
    private static Long trainerId = 0L;
    private static Long traineeId = 0L;
    private static Long trainingId = 0L;

    public static synchronized Long nextUserId() { return ++userId; }
    public static synchronized Long nextTrainerId() { return ++trainerId; }
    public static synchronized Long nextTraineeId() { return ++traineeId; }
    public static synchronized Long nextTrainingId() { return ++trainingId; }
}
