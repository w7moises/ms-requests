package co.com.bancolombia.model.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StateTest {
    private static final State STATE_APROBADO = State.builder()
            .id(1L)
            .name("APROBADO")
            .description("APROBADO")
            .build();

    private static final State STATE_PENDIENTE = State.builder()
            .id(2L)
            .name("PENDIENTE")
            .description("PENDIENTE")
            .build();

    @Test
    void shouldBuild() {
        assertEquals(1L, STATE_APROBADO.getId());
        assertEquals("APROBADO", STATE_APROBADO.getName());
        assertEquals("APROBADO", STATE_APROBADO.getDescription());
    }

    @Test
    void settersAndGetters() {
        State s = new State();
        s.setId(STATE_PENDIENTE.getId());
        s.setName(STATE_PENDIENTE.getName());
        s.setDescription(STATE_PENDIENTE.getDescription());
        assertEquals(2L, s.getId());
        assertEquals("PENDIENTE", s.getName());
        assertEquals("PENDIENTE", s.getDescription());
    }

    @Test
    void builder() {
        State mod = STATE_APROBADO.toBuilder()
                .description("CAMBIADO")
                .build();
        assertEquals(1L, mod.getId());
        assertEquals("APROBADO", mod.getName());
        assertEquals("CAMBIADO", mod.getDescription());
    }
}
