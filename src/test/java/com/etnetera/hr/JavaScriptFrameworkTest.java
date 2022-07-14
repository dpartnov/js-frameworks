package com.etnetera.hr;

import com.etnetera.hr.data.dto.FrameworkDto;
import com.etnetera.hr.data.dto.FrameworkVersionDto;
import com.etnetera.hr.startup.Application;
import java.time.LocalDate;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Class used for Spring Boot/MVC based tests.
 *
 * @author Etnetera
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JavaScriptFrameworkTest {

    private static final String LOCAL_URL = "http://localhost:";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getAllFrameworksTest() throws Exception {
        assertThat(this.restTemplate.getForObject(LOCAL_URL + port + "/api/v1/frameworks",
                String.class)).contains(Arrays.asList("Angular", "React", "Vue"));
    }

    @Test
    public void getFrameworkByFilterTest() throws Exception {
        assertThat(this.restTemplate.getForObject(LOCAL_URL + port + "/api/v1/frameworks/find?name=vue",
                String.class)).contains(Arrays.asList("Vue"));
    }

    @Test
    public void getFrameworkDetailTest() throws Exception {
        assertThat(this.restTemplate.getForObject(LOCAL_URL + port + "/api/v1/frameworks/3",
                String.class)).contains(Arrays.asList("Vue", "8", "3.2.37", "2023-12-01"));
    }

    @Test
    public void createFrameworkTest() throws Exception {
        this.restTemplate.postForObject(LOCAL_URL + port + "/api/v1/frameworks",
                getNewFrameworkDto(), String.class);

        assertThat(this.restTemplate.getForObject(LOCAL_URL + port + "/api/v1/frameworks/find?name=node",
                String.class)).contains(Arrays.asList("Node"));
    }

    @Test
    public void updateFrameworkTest() throws Exception {
        this.restTemplate.put(LOCAL_URL + port + "/api/v1/frameworks/3",
                getUpdateFrameworkDto());

        assertThat(this.restTemplate.getForObject(LOCAL_URL + port + "/api/v1/frameworks/find?name=vue",
                String.class)).contains(Arrays.asList("2", "2.2.1", "2019-05-10"));
    }

    @Test
    public void deleteFrameworkTest() throws Exception {
        this.restTemplate.delete(LOCAL_URL + port + "/api/v1/frameworks/1");

        assertThat(this.restTemplate.getForObject(LOCAL_URL + port + "/api/v1/frameworks",
                String.class)).doesNotContain("Angular");
    }

    private FrameworkDto getNewFrameworkDto() {
        FrameworkDto dto = new FrameworkDto();
        dto.setName("Node");
        dto.setHypeLevel(7);
        FrameworkVersionDto versionDto = new FrameworkVersionDto();
        versionDto.setVersion("18.6.0");
        versionDto.setDeprecationDate(LocalDate.of(2025, 4, 30));
        dto.setVersions(Arrays.asList(versionDto));
        return dto;
    }

    private FrameworkDto getUpdateFrameworkDto() {
        FrameworkDto dto = new FrameworkDto();
        dto.setId(3L);
        dto.setName("Vue");
        dto.setHypeLevel(2);
        FrameworkVersionDto versionDto = new FrameworkVersionDto();
        versionDto.setVersion("2.2.1");
        versionDto.setDeprecationDate(LocalDate.of(2019, 5, 10));
        dto.setVersions(Arrays.asList(versionDto));
        return dto;
    }
}
