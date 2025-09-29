package com.example.servlet;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("filemanagerPU");

    public static EntityManagerFactory emf() {
        return emf;
    }
}