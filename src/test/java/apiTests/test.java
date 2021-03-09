package apiTests;

import org.testng.annotations.*;

public class test {
    @BeforeMethod
    public void before(){
        System.out.printf("Before each test\n");
    }

    @AfterMethod
    public void after(){
        System.out.printf("After each test\n");
    }

    @BeforeTest
    public void start(){
        System.out.printf("Before test\n");
    }
    @AfterTest
    public void end(){
        System.out.printf("After test\n");
    }

    @Test(priority = 0)
    public void one(){
        System.out.printf("One\n");
    }

    @Test(priority = 1)
    public void two(){
        System.out.printf("Two\n");
    }
}
