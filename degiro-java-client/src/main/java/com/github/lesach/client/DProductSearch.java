/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lesach.client;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @author indiketa
 */
@Getter
@Setter
public class DProductSearch {
    private int total;
    private int offset;
    private List<DProductDescription> products;

}
