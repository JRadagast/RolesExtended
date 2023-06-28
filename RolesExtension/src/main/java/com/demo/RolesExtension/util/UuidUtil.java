/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.RolesExtension.util;

import java.util.UUID;

/**
 *
 * @author JRadagast
 */
public class UuidUtil {
    
    /**
     * Generate a random UUID
     * @return 
     */
    public static String generateUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    
}
