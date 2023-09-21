package com.uangel.sim.command;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dajin kim
 */
public class CliManagerTest {
    private final List<String> cliList = new ArrayList<>();

    @Test
    public void testHelpCommandInfo() {
        String[] args = {"-h"};
        CliManager.parseCommandLine(args);
    }

    @Test
    public void prepareOptions() {
        addCliArgs("hh", "127.0.0.1");
        addCliArgs("hp", "8080");
        addCliArgs("sf", "./src/main/resources/scenario/easy_cms_basic.xml");
        addCliArgs("ts", "10");

        String[] args = cliList.toArray(new String[0]);
        printArr(args);

        CliInfo cliInfo = CliManager.parseCommandLine(args);
        System.out.println(cliInfo);

        Assert.assertEquals(8, cliList.size());
    }

    private void addCliArgs(String option, String value) {
        if (!option.startsWith("-")) option = "-" + option;
        cliList.add(option);
        cliList.add(value);
    }

    private void printArr(String[] arr) {
        if ((arr.length % 2) != 0) return;
        for (int i = 0; i < arr.length; i+=2) {
            System.out.println("Option : " + arr[i] + " , " + arr[i+1]);
        }
    }
}
