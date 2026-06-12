package trabajo.aplicacionSaludable.Servicios;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import trabajo.aplicacionSaludable.Assemblers.IngredienteAssembler;
import trabajo.aplicacionSaludable.Dominio.*;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.CantidadNegativaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.IngredienteDuplicadoException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.IngredienteNoPerteneceADietaException;
import trabajo.aplicacionSaludable.Repositorios.AlimentoRepository;
import trabajo.aplicacionSaludable.Repositorios.ComidaRepository;
import trabajo.aplicacionSaludable.Repositorios.IngredienteRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class IngredientesServiceTest {

    @Mock
    private ComidaRepository comidaRepository;
    
    @Mock
    private IngredienteRepository ingredienteRepository;

    @Mock
    private AlimentoRepository alimentoRepository;

    @InjectMocks
    private IngredienteService ingredienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ingredienteService = new IngredienteService(
                comidaRepository,
                ingredienteRepository,
                alimentoRepository,
                new IngredienteAssembler()
        );
    }

    @Test
    void crearIngredienteValidoTest() {

        DietaCompleta dieta = new DietaCompleta();
        dieta.setDiasDeDieta(new ArrayList<>());

        DiaEnDieta dia = new DiaEnDieta();
        dia.setComidas(new ArrayList<>());
        dia.setDietaCompleta(dieta);

        Comida comida = new Comida();
        comida.setIdComida(1L);
        comida.setDiaEnDieta(dia);

        Alimento alimento = new Alimento();
        alimento.setIdAlimento(1L);
        alimento.setNombre("Arroz");
        alimento.setCalorias(100);
        alimento.setProteinas(10);
        alimento.setCarbohidratos(20);
        alimento.setGrasas(5);

        IngredienteDTO dto = new IngredienteDTO();
        dto.setIdAlimento(1L);
        dto.setCantidadEnGramos(200);

        when(comidaRepository.findById(1L))
                .thenReturn(Optional.of(comida));

        when(alimentoRepository.findById(1L))
                .thenReturn(Optional.of(alimento));

        when(ingredienteRepository.findByAlimentoAndComida(alimento, comida))
                .thenReturn(Optional.empty());

        // comprueba que se guarda bien el ingrediente
        when(ingredienteRepository.save(any(Ingrediente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        IngredienteDTO resultado =
                ingredienteService.crearIngrediente(1L, dto);

        assertNotNull(resultado);

        assertEquals(200, resultado.getCantidadEnGramos());
        assertEquals(200, resultado.getCaloriasTotales());
        assertEquals(20, resultado.getProteinas());
        assertEquals(40, resultado.getCarbohidratos());
        assertEquals(10, resultado.getGrasas());

    }

    @Test
    void anhadeIngredienteDuplicadoTest() {
        Comida comida = new Comida();
        comida.setIdComida(1L);

        Alimento alimento = new Alimento();
        alimento.setIdAlimento(1L);
        alimento.setNombre("Arroz");

        Ingrediente ingredienteExistente = new Ingrediente();
        ingredienteExistente.setAlimento(alimento);
        ingredienteExistente.setComida(comida);

        IngredienteDTO dto = new IngredienteDTO();
        dto.setIdAlimento(1L);
        dto.setCantidadEnGramos(100);

        when(comidaRepository.findById(1L))
                .thenReturn(Optional.of(comida));

        when(alimentoRepository.findById(1L))
                .thenReturn(Optional.of(alimento));

        // Busco el alimento que ya existe en la comida
        when(ingredienteRepository.findByAlimentoAndComida(alimento, comida))
                .thenReturn(Optional.of(ingredienteExistente));

        assertThrows(
                IngredienteDuplicadoException.class,
                () -> ingredienteService.crearIngrediente(1L, dto)
        );
    }

    @Test
    void CreaIngredienteGramosNegativosTest() {

        Comida comida = new Comida();
        comida.setIdComida(1L);

        IngredienteDTO dto = new IngredienteDTO();
        dto.setCantidadEnGramos(-50);

        when(comidaRepository.findById(1L))
                .thenReturn(Optional.of(comida));

        assertThrows(
                CantidadNegativaException.class,
                () -> ingredienteService.crearIngrediente(1L, dto)
        );
    }

    @Test
    void editarIngredienteCorrectoTest() {
        DietaCompleta dieta = new DietaCompleta();
        dieta.setDiasDeDieta(new ArrayList<>());

        DiaEnDieta dia = new DiaEnDieta();
        dia.setComidas(new ArrayList<>());
        dia.setDietaCompleta(dieta);

        Comida comida = new Comida();
        comida.setIdComida(1L);
        comida.setDiaEnDieta(dia);

        dia.getComidas().add(comida);

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setIdIngrediente(1L);
        ingrediente.setCantidadEnGramos(200);

        ingrediente.setCaloriasTotales(100);
        ingrediente.setProteinas(10);
        ingrediente.setCarbohidratos(20);
        ingrediente.setGrasas(5);

        ingrediente.setComida(comida);

        IngredienteDTO dto = new IngredienteDTO();
        dto.setCantidadEnGramos(400);

        when(ingredienteRepository.findById(1L))
                .thenReturn(Optional.of(ingrediente));

        when(ingredienteRepository.save(any(Ingrediente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        IngredienteDTO resultado =
                ingredienteService.editarIngrediente(1L, 1L, dto);

        assertNotNull(resultado);

        assertEquals(400, resultado.getCantidadEnGramos());
        assertEquals(200, resultado.getCaloriasTotales());
        assertEquals(20, resultado.getProteinas());
        assertEquals(40, resultado.getCarbohidratos());
        assertEquals(10, resultado.getGrasas());
    }

    @Test
    void eliminarIngredienteValidoTest() {
        DietaCompleta dieta = new DietaCompleta();
        dieta.setDiasDeDieta(new ArrayList<>());

        DiaEnDieta dia = new DiaEnDieta();
        dia.setComidas(new ArrayList<>());
        dia.setDietaCompleta(dieta);

        Comida comida = new Comida();
        comida.setIdComida(1L);

        comida.setDiaEnDieta(dia);
        comida.setIngredientes(new HashSet<>());

        dia.getComidas().add(comida);

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setIdIngrediente(1L);
        ingrediente.setComida(comida);

        comida.getIngredientes().add(ingrediente);

        when(ingredienteRepository.findById(1L))
                .thenReturn(Optional.of(ingrediente));

        boolean resultado =
                ingredienteService.borrarIngrediente(1L, 1L);

        assertTrue(resultado);
    }

    @Test
    void eliminarIngredienteDeOtraComidaTest() {

        Comida comidaReal = new Comida();
        comidaReal.setIdComida(1L);

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setIdIngrediente(1L);
        ingrediente.setComida(comidaReal);

        when(ingredienteRepository.findById(1L))
                .thenReturn(Optional.of(ingrediente));

        assertThrows(
                IngredienteNoPerteneceADietaException.class,
                () -> ingredienteService.borrarIngrediente(1L, 2L)
        );

    }

}
