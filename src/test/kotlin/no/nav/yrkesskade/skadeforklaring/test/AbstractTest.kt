package no.nav.yrkesskade.skadeforklaring.test

import no.nav.yrkesskade.skadeforklaring.integration.mottak.SkadeforklaringInnsendtKafkaConsumer
import no.nav.yrkesskade.skadeforklaring.test.docker.KafkaDockerContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils

@ActiveProfiles("integration")
@SpringBootTest
@ContextConfiguration(initializers = [AbstractTest.DockerConfigInitializer::class, TestMockServerInitialization::class])
@DirtiesContext
abstract class AbstractTest {

    init {
        KafkaDockerContainer.container
    }

    class DockerConfigInitializer: ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.kafka.bootstrap-servers=" + KafkaDockerContainer.container.bootstrapServers
            );
        }
    }

    @Autowired
    lateinit var mottakConsumer: SkadeforklaringInnsendtKafkaConsumer
}