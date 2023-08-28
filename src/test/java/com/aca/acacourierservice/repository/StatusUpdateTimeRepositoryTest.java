package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.Order;
import com.aca.acacourierservice.entity.StatusUpdateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class StatusUpdateTimeRepositoryTest {
    @Autowired
    private StatusUpdateTimeRepository statusUpdateTimeRepository;
    @BeforeEach
    public void setUp(){
        StatusUpdateTime firstStatusUpdateTime = new StatusUpdateTime();
        firstStatusUpdateTime.setId(1L);
        firstStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERING);
        firstStatusUpdateTime.setUpdatedFrom(Order.Status.SHIPPED);
        firstStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST, 28, 21, 23, 23));
        statusUpdateTimeRepository.save(firstStatusUpdateTime);
        StatusUpdateTime secondStatusUpdateTime = new StatusUpdateTime();
        secondStatusUpdateTime.setId(2L);
        secondStatusUpdateTime.setUpdatedTo(Order.Status.DELIVERED);
        secondStatusUpdateTime.setUpdatedFrom(Order.Status.DELIVERING);
        secondStatusUpdateTime.setUpdateTime(LocalDateTime.of(2023, Month.AUGUST,28,22,23,23));
        secondStatusUpdateTime.setAdditionalInfo("Ok");
        statusUpdateTimeRepository.save(secondStatusUpdateTime);
    }
    @AfterEach
    public void destroy(){
        statusUpdateTimeRepository.deleteAll();
    }
    @Test
    public void testGetAllStatusUpdateTimes(){
        List<StatusUpdateTime> statusUpdateTimes = statusUpdateTimeRepository.findAll();
        Assertions.assertThat(statusUpdateTimes.size()).isEqualTo(2);
        Assertions.assertThat(statusUpdateTimes.get(0).getAdditionalInfo()).isNull();
        Assertions.assertThat(statusUpdateTimes.get(1).getAdditionalInfo()).isEqualTo("Ok");
        Assertions.assertThat(statusUpdateTimes.get(0).getId()).isNotNegative();
        Assertions.assertThat(statusUpdateTimes.get(1).getId()).isNotNegative();
        Assertions.assertThat(statusUpdateTimes.get(1).getUpdateTime())
                .hasYear(2023)
                .hasMonth(Month.AUGUST)
                .hasDayOfMonth(28)
                .hasHour(22)
                .hasMinute(23)
                .hasSecond(23);
    }
}
