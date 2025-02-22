package mbm.playback_data_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PlaybackDataServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void writeDocumentationSnippets() {
		final var modules = ApplicationModules.of(PlaybackDataServiceApplication.class).verify();
		modules.forEach(System.out::println);

		new Documenter(modules).writeModulesAsPlantUml().writeIndividualModulesAsPlantUml();
	}

}
