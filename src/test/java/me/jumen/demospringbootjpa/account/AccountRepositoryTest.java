package me.jumen.demospringbootjpa.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest //통합 테스트 -> @SpringBootApplication을 찾아서 모든 bean을 등록한다 -> postgres를 사용한다
@DataJpaTest// 슬라이싱 테스트 -> 임베디드 DB를 사용하게 자동 설정 -> H2를 사용한다
class AccountRepositoryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void init() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println(metaData.getURL());
            System.out.println(metaData.getDriverName());   // H2 JDBC Driver
            System.out.println(metaData.getUserName()); // SA
        }
    }

    @Test
    public void test() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Account account = new Account();
            account.setUsername("jumen");
            account.setPassword("5215");

            Account newAccount = accountRepository.save(account);
            assertThat(newAccount).isNotNull();

            Optional<Account> existingAccount = accountRepository.findByUsername(newAccount.getUsername());
            assertThat(existingAccount).isNotEmpty();

            Optional<Account> nonexistingAccount = accountRepository.findByUsername("won ju young");
            assertThat(nonexistingAccount).isEmpty();

        }
    }

}