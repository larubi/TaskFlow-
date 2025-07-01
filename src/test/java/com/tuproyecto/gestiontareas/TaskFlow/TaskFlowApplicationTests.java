package com.tuproyecto.gestiontareas.TaskFlow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = { // <--- Añade esta anotación
		"jwt.secret=secretoparatestsmuylargoaleatorioyseguroparalaaplicacion",
		"jwt.expiration=18000000"
})
class TaskFlowApplicationTests {

	@Test
	void contextLoads() {
		// Este test simplemente verifica que el contexto de Spring se carga correctamente
	}
}