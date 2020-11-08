/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lesach.client.engine;

/**
 *
 * @author indiketa
 */
public interface Listener<T> {

    public void handle(T data);

}
