package com.github.lesach.client.engine;

import java.io.File;

/**
 *
 * @author indiketa
 */
public class Main {

    public static void main(String[] args) throws Exception {
        DEngine e = new DEngine(new Credentials(new File("/home/ecatala/dg.properties")));
        e.startEngine();
    }
}

