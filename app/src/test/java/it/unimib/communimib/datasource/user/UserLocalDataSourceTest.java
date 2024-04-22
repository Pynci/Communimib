package it.unimib.communimib.datasource.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.database.FakeUserDAO;
import it.unimib.communimib.database.UserDAO;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;

public class UserLocalDataSourceTest {

    UserLocalDataSource userLocalDataSource;
    UserDAO fakeUserDAO;
    User marco;
    volatile Result result;

    @Before
    public void setUp() throws Exception {
        fakeUserDAO = new FakeUserDAO();
        userLocalDataSource = new UserLocalDataSource(fakeUserDAO);
        marco = new User("123456","marco@unimib.it", "Marco", "Ferioli");
    }

    @Test
    public void getUser() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        fakeUserDAO.insertUser(marco);
        userLocalDataSource.getUser(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.UserSuccess);
        Assert.assertEquals(marco, ((Result.UserSuccess) result).getUser());
    }

    @Test
    public void insertUser() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        fakeUserDAO.clearUser();
        userLocalDataSource.insertUser(marco,result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
    }

    @Test
    public void updateUser() throws InterruptedException {
        //TODO: implementare il test
    }

    @Test
    public void deleteUser() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        fakeUserDAO.insertUser(marco);
        userLocalDataSource.deleteUser(result -> {
            this.result = result;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        Assert.assertTrue(result instanceof Result.Success);
    }
}