package com.example.servlet;

import java.nio.file.Path;

public class AppConfig {
    public static final Path HOMES_ROOT = Path.of(System.getProperty("user.home"), "filemanager");
}