package be.ac.umons.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by clement on 2/26/16.
 */
public class TestRunner {


    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(SegmentTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }

}
