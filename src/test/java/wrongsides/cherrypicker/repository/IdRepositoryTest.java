package wrongsides.cherrypicker.repository;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IdRepositoryTest {

    private IdRepository idRepository = new IdRepository();

    @Test
    public void findRegion_givenTheForge_returns10000002() {
        assertThat(idRepository.findRegion("The Forge")).isEqualTo("10000002");
    }
}