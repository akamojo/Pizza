/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pizza;

import java.util.Random;

/**
 * Klasa służąca do generowania losowych nazw.
 * @author akamojo
 */
public class StringGenerator {

    public StringGenerator() {
    }

    char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    
    /**
     * Metoda zwracająca string utworzony z losowych liter.
     * @param length
     * @return 
     */
    public String generate(int length){
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
    
}
